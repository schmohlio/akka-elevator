package elevators

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import elevators.model._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class ElevatorSystemMultiPickupSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A ElevatorControlSystem" must {
    "react to PickupRequest" in {
      val elevatorControlSysstemActor = system.actorOf(ElevatorControlSystem.props(2))
      elevatorControlSysstemActor ! PickupRequest(Passenger(10, 0))
      Thread.sleep(1000) // wait a second until PickupRequest is scheduled
      elevatorControlSysstemActor ! PickupRequest(Passenger(8, 5))
      Thread.sleep(1000) // wait a second until PickupRequest is scheduled
      (0 to 40).foreach {
        i =>
          elevatorControlSysstemActor ! Tick
      }
    }
  }

}
