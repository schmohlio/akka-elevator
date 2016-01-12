package elevators

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import elevators.model._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class ElevatorSystemPickupSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A ElevatorControlSystem" must {
    "react to PickupRequest" in {
      val elevatorControlSysstemActor = system.actorOf(ElevatorControlSystem.props(1))
      elevatorControlSysstemActor ! PickupRequest(Passenger(10, 0))
      Thread.sleep(1000) // wait a second until PickupRequest is scheduled
      (0 to 20).foreach {
        i =>
          elevatorControlSysstemActor ! Tick
      }
    }
  }

}
