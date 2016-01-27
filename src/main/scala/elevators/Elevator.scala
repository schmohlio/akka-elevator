package elevators

import akka.actor.{Actor, ActorLogging, Props}
import elevators.model._

/**
 * The actor represents the Elevator itself.
 *
 * The actor has two states: Idling and Moving.
 * Depending on the actor state the behavior is a little bit different when it comes to the Tick message.
 * This actor moves autonomously according the list of passengers inside and the list of passengers to pick up.
 *
 * @author Johannes Unterstein (unterstein@me.com)
 */
object Elevator {
  def props(elevatorId: Int): Props = Props(classOf[Elevator], elevatorId)
}

class Elevator(id: Int) extends Actor with ActorLogging {

  // implement elevator state through different receive methods
  override def receive: Receive = idleReceive(0)

  def idleReceive(currentFloor: Int): Receive = {
    // what messages can be received if an elevator is idling? -> StatusRequest and PickUpRequests
    case SystemStatusRequest =>
      log.debug(s"$id is currently idling")
      sender ! ElevatorStatus(id, Idle(currentFloor))
    case PickupRequest(passenger: Passenger) =>
      log.debug(s"$id is now moving")
      context become moveReceive(currentFloor, Set(), Set(passenger), Direction.direction(currentFloor, passenger.startFloor))
    case Tick =>
      // just relax and stay idling
      log.debug(s"$id is currently idling and received Tick")
  }

  def moveReceive(currentFloor: Int, inside: Set[Passenger], toPickup: Set[Passenger], direction: Direction): Receive = {
    // what messages can be received if an elevator is idling? -> StatusRequest and PickUpRequests
    case SystemStatusRequest =>
      sender ! ElevatorStatus(id, Moving(currentFloor, direction))
    case PickupRequest(passenger: Passenger) =>
      context become moveReceive(currentFloor, inside, toPickup + passenger, direction)
    case Tick =>
      // assumption: Our elevator is traveling to the same direction until no targets are left in this direction
      // then change direction (if needed)

      // floor of tick is newFloor
      val newFloor = direction.next(currentFloor)
      log.debug(s"Elevator $id is now on $newFloor with $direction and inside $inside and toPickup $toPickup")
      // calculate if the elevator has to do something on the newFloor (collect or release passengers)

      // collected passengers
      val collected = toPickup.filter(passenger => passenger.startFloor == newFloor)
      if (collected.size > 0) {
        log.debug(s"joined passengers: $collected")
      }
      val released = inside.filter(passenger => passenger.targetFloor == newFloor)
      if (released.size > 0) {
        log.debug(s"left passengers: $released")
      }

      // calculate the newly inside passengers and the according less passengers to pick up
      val newInside = inside ++ collected -- released
      val newPickups = toPickup -- collected

      // if this is empty, relax and idle
      if (newInside.isEmpty && newPickups.isEmpty) {
        context become idleReceive(newFloor)
      } else {
        log.debug("here should a re-direction be calculated")
        // test if we reached to top or bottom of our current working queue.
        val floors = newPickups.map(passenger => passenger.startFloor) ++ newInside.map(passenger => passenger.targetFloor)
        val borderReached = if (direction == Up) floors.max <= newFloor else floors.min >= newFloor

        // if the elevator is traveling upstairs, there should no activities above to change direction and vice versa
        if (borderReached) {
          // change direction
          context become moveReceive(newFloor, newInside, newPickups, if (direction == Up) Down else Up)
        } else {
          // keep direction
          context become moveReceive(newFloor, newInside, newPickups, direction)
        }
      }
  }

}
