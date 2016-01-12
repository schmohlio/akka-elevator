package elevators

import akka.actor.ActorSystem

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */
object ElevatorApp extends App {

  val system = ActorSystem("MyActorSystem")

  val firstElevator = system.actorOf(Elevator.props(1), "firstElevator")
  firstElevator ! "ping"

}
