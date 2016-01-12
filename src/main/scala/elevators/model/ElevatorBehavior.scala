package elevators.model

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */
sealed trait ElevatorBehavior

case class Idle(currentFloor: Int) extends ElevatorBehavior

case class Moving(currentFloor: Int, direction: Direction) extends ElevatorBehavior
