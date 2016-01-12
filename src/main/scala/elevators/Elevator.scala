package elevators

import akka.actor.{Actor, ActorLogging, Props}
import elevators.model._

/**
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
      context become moveReceive(currentFloor, Set(passenger), Direction.direction(currentFloor, passenger.startFloor))
    case Tick =>
    // just relax and stay idling
      log.debug(s"$id is currently idling and received Tick")
  }

  def moveReceive(currentFloor: Int, targets: Set[Passenger], direction: Direction): Receive = {
    // what messages can be received if an elevator is idling? -> StatusRequest and PickUpRequests
    case SystemStatusRequest =>
      sender ! ElevatorStatus(id, Moving(currentFloor, direction))
    case PickupRequest(passenger: Passenger) =>
      context become moveReceive(currentFloor, targets + passenger, direction)
    case Tick =>
      // floor of tick is newFloor
      val newFloor = direction.next(currentFloor)
      log.debug(s"Elevator $id is now on $newFloor with $direction")
      // calculate if the elevator has to do something on the newFloor (collect or release passengers)
      if (targets.map(passenger => passenger.targetFloor).contains(newFloor) || targets.map(passenger => passenger.startFloor).contains(newFloor)) {
        // new targets are passengers where the targetFloor is not reached
        val newTargets = targets.filter(passenger => passenger.targetFloor != currentFloor)
        // if this is empty, relax and idle
        if (newTargets.isEmpty) {
          context become idleReceive(newFloor)
        } else {
          log.debug("here should be a re-direction be calculated")
          // test if we reached to top or bottom
          val borderFlor = if (direction == Up) {
            List(targets.map(passenger => passenger.startFloor).max, targets.map(passenger => passenger.targetFloor).max).max
          } else {
            List(targets.map(passenger => passenger.startFloor).min, targets.map(passenger => passenger.targetFloor).min).min
          }
          if (borderFlor == newFloor) {
            // change direction
            context become moveReceive(newFloor, newTargets, if (direction == Up) Down else Up)
          } else {
            // keep direction
            context become moveReceive(newFloor, newTargets, direction)
          }
        }
      } else {
        context become moveReceive(newFloor, targets, direction)
      }
  }
}
