package elevators

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
 * An elevator control system should provide (more or less) the functionality of the following interface:
 * <pre>
 * trait ElevatorControlSystem {
 * def status(): Seq[(Int, Int, Int)] // Querying the state of the elevators (what floor are they on and where they are going),
 * def update(Int, Int, Int)          // receiving an update about the status of an elevator
 * def pickup(Int, Int)               // receiving a pickup request
 * def step()                         // time-stepping the simulation
 * }
 * </pre>
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
 * @param elevatorAmount the number of elevaors in our system
 */
class ElevatorControlSystem(elevatorAmount: Int) extends Actor with ActorLogging {

  private val elevators: List[ActorRef] = (0 to elevatorAmount).map(id => context.actorOf(Elevator.props(id))).toList

  override def receive: Receive = {
    case message: AnyRef => elevators.map(elevator => elevator ! message)
  }
}
