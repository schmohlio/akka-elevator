package elevators

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import elevators.model._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.duration._

class ElevatorSystemPickupSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A ElevatorControlSystem" must {
    "react to PickupRequest" in {
      val elevatorControlSystemActor = system.actorOf(ElevatorControlSystem.props(1))
      elevatorControlSystemActor ! PickupRequest(Passenger(10, 0))
      Thread.sleep(1000) // wait a second until PickupRequest is scheduled
      (0 to 20).foreach {
        i =>
          elevatorControlSystemActor ! Tick
      }
      elevatorControlSystemActor ! SystemStatusRequest
      val answer = receiveOne(5.seconds).asInstanceOf[SystemStatusResponse]
      assert(answer.elevators.size == 1)
      assert(answer.elevators(0).direction.isInstanceOf[Idle])
    }
  }

}
