package elevators

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import elevators.model._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class ElevatorSystemLoadSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A ElevatorControlSystem" must {
    "react to PickupRequest" in {
      val elevatorControlSystemActor = system.actorOf(ElevatorControlSystem.props(16))

      scheduleSomeRequests(elevatorControlSystemActor)
      scheduleSomeTicks(elevatorControlSystemActor)
      scheduleSomeRequests(elevatorControlSystemActor)
      scheduleSomeTicks(elevatorControlSystemActor)
      scheduleSomeTicks(elevatorControlSystemActor)
    }
  }

  def scheduleSomeTicks(elevatorControlSystemActor: ActorRef) {
    (0 to 100).foreach {
      i =>
        elevatorControlSystemActor ! Tick
    }
  }

  def scheduleSomeRequests(elevatorControlSystemActor: ActorRef) {
    (1 to 20).map(i => createPickupRequest()).foreach {
      request =>
        elevatorControlSystemActor ! request
        Thread.sleep(100) // wait a few millis until PickupRequest is scheduled
    }
  }

  val random = scala.util.Random
  val maxNumberOfFloor = 50

  def createPickupRequest(): PickupRequest = PickupRequest(Passenger(random.nextInt(maxNumberOfFloor), random.nextInt(maxNumberOfFloor)))
}
