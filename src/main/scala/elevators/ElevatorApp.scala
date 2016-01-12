package elevators

import akka.actor.ActorSystem

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */
object ElevatorApp extends App {

  val system = ActorSystem("MyActorSystem")

  // let´s have fun with 16 elevators
  val elevatorControlSystem = system.actorOf(ElevatorControlSystem.props(16), "elevatorControlSystem")

  elevatorControlSystem ! "ping"
}
