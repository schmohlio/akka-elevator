package elevators

import akka.actor.{Actor, ActorLogging, Props}

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */
object Elevator {
  def props(elevatorId: Int): Props = Props(classOf[Elevator], elevatorId)
}

class Elevator(id: Int) extends Actor with ActorLogging {

  override def receive: Receive = {
    case message: AnyRef => log.info(s"elevator $id received $message")
  }
}
