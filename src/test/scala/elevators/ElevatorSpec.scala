package elevators

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import elevators.model._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class ElevatorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A Elevator" must {
    "send back a idle behavior if he idles" in {
      val elevatorActor = system.actorOf(Elevator.props(0))
      elevatorActor ! SystemStatusRequest
      expectMsg(ElevatorStatus(0, Idle(0)))
    }
  }

  "A ElevatorControlSystem" must {
    "return a list of all elevator status" in {
      val elevatorControlSysstemActor = system.actorOf(ElevatorControlSystem.props(16))
      elevatorControlSysstemActor ! SystemStatusRequest
      val answer = receiveOne(5.seconds).asInstanceOf[SystemStatusResponse]
      assert(answer.elevators.size == 16)
    }
  }

}
