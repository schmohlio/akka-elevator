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
      val elevatorActor = system.actorOf(ElevatorControlSystem.props(16))
      elevatorActor ! PickupRequest(Passenger(10, 0))
      elevatorActor ! PickupRequest(Passenger(12, 4))
    }
  }

}
