package elevators

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
 * An elevator control system should provide (more or less) the functionality of the following interface:
 * <pre>
 * trait ElevatorControlSystem {
 * def status(): Seq[(Int, Int, Int)]
 * def update(Int, Int, Int)
 * def pickup(Int, Int)
 * def step()
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
