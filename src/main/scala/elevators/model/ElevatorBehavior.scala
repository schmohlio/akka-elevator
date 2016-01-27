package elevators.model

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */
sealed trait ElevatorBehavior {
  val currentFloor: Int
}

/**
 * Representing that the according Elevator is idling.
 *
 * @param currentFloor the floor number where the Elevator idles
 */
case class Idle(currentFloor: Int) extends ElevatorBehavior

/**
 * Representing that the according Elevator is Moving.
 *
 * @param currentFloor the current floor number of the Elevator
 * @param direction the traveling direction
 */
case class Moving(currentFloor: Int, direction: Direction) extends ElevatorBehavior
