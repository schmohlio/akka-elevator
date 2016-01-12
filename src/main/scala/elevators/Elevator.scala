package elevators

import akka.actor.{Actor, ActorLogging, Props}
import elevators.model.{Idle, ElevatorStatus, SystemStatusRequest}

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
    // what messages can be received if an elevator is idling?
    case SystemStatusRequest =>
      sender ! ElevatorStatus(id, Idle(currentFloor))
  }
}
