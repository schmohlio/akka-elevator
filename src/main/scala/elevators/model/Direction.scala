package elevators.model

/**
 * Represents the traveling direction of a passenger
 *
 * @author Johannes Unterstein (unterstein@me.com)
 */
sealed abstract class Direction() {
  def next(currentFloor: Int): Int = currentFloor // TODO why is abstract not working? .. o_O
}

// traveling upstairs
object Up extends Direction {
  override def next(currentFloor: Int) = currentFloor + 1

  override def toString: String = "Up"
}

// traveling downstairs
object Down extends Direction {
  override def next(currentFloor: Int) = currentFloor - 1

  override def toString: String = "Down"
}


object Direction {
  def direction(startFloor: Int, targetFloor: Int): Direction = if (startFloor > targetFloor) Down else Up
}