package elevators.model

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */
sealed trait ElevatorBehavior

case class Idle(id: Int, currentFloor: Int) extends ElevatorBehavior

case class Moving(id: Int, currentFloor: Int, direction: Direction) extends ElevatorBehavior
