package elevators

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import elevators.model._

import scala.collection._


/**
 * An elevator control system should provide (more or less) the functionality of the following interface:
 * <pre>
 * trait ElevatorControlSystem {
 * def status(): Seq[(Int, Int, Int)] // Querying the state of the elevators (what floor are they on and where they are going)
 * def update(Int, Int, Int)          // receiving an update about the status of an elevator
 * def pickup(Int, Int)               // receiving a pickup request
 * def step()                         // time-stepping the simulation
 * }
 * </pre>
 *
 * In this simple scenario
 * - an elevator is represented through a triple of (elevator ID, current floor number, goal floor number)
 * - an pickup request is represented through a tuple of (current floor number, direction (positive=up, negative=down)
 *
 * In the world of actors this should be a little bit changed...let´s see!
 *
 * @author Johannes Unterstein (unterstein@me.com)
 */
object ElevatorControlSystem {
  def props(elevatorAmount: Int): Props = Props(classOf[ElevatorControlSystem], elevatorAmount)
}

/**
 * First attempt, we are handling a fixed amount of elevators to control.
 *
 * @param elevatorAmount the number of elevators in our system
 */
class ElevatorControlSystem(elevatorAmount: Int) extends Actor with ActorLogging {

  private val elevators: Map[Int, ActorRef] = (1 to elevatorAmount).map(id => id -> context.actorOf(Elevator.props(id))).toMap

  override def receive: Receive = {
    case SystemStatusRequest =>
      context.actorOf(ElevatorStatusRetriever.props(sender(), elevators.values.toList, None)) ! SystemStatusRequest
    case PickupRequest(passenger: Passenger) =>
      context.actorOf(ElevatorStatusRetriever.props(self, elevators.values.toList, Option(passenger))) ! SystemStatusRequest
    case PickupStatusResponse(passenger: Passenger, status: List[ElevatorStatus]) =>
      // if we receive a SystemStatusResponse this must be triggered through a PickupRequest
      val bestMatch = pickBestElevatorForPickup(status)
      log.info("picked: " + bestMatch)
      elevators.get(bestMatch).get ! PickupRequest(passenger)
  }


  // FIXME: currently a random elevator will be picked
  def pickBestElevatorForPickup(statusList: List[ElevatorStatus]): Int = {
    import scala.util.Random
    Random.shuffle(statusList).head.id
  }
}

object ElevatorStatusRetriever {
  def props(originalSender: ActorRef, elevators: List[ActorRef], passenger: Option[Passenger]): Props = Props(classOf[ElevatorStatusRetriever], originalSender, elevators, passenger)
}

class ElevatorStatusRetriever(originalSender: ActorRef, elevators: List[ActorRef], passenger: Option[Passenger]) extends Actor with ActorLogging {

  val results = mutable.ArrayBuffer.empty[ElevatorStatus]

  override def receive: Receive = {
    case SystemStatusRequest =>
      elevators.map(elevator => elevator ! SystemStatusRequest)
    case status: ElevatorStatus =>
      results += status
      collectStatus()
  }

  def collectStatus(force: Boolean = false) {
    if (results.size == elevators.size || force) {
      if (passenger.isDefined) {
        originalSender ! PickupStatusResponse(passenger.get, results.toList)
      } else {
        originalSender ! SystemStatusResponse(results.toList)
      }
      context.stop(self)
    }
  }
}


