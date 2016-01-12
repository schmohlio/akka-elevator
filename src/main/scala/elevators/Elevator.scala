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
      sender ! ElevatorStatus(id, Idle(currentFloor))
    case PickupRequest(passenger: Passenger) =>
      log.debug("start moving")
      context become moveReceive(currentFloor, Set(passenger), passenger.travelingDirection)
  }

  def moveReceive(currentFloor: Int, targets: Set[Passenger], direction: Direction): Receive = {
    // what messages can be received if an elevator is idling? -> StatusRequest and PickUpRequests
    case SystemStatusRequest =>
      sender ! ElevatorStatus(id, Moving(currentFloor, direction))
    case PickupRequest(passenger: Passenger) =>
      context become moveReceive(currentFloor, targets + passenger, direction)
  }
}
