package elevators.model

/**
 * Represents the traveling direction of a passenger
 *
 * @author Johannes Unterstein (unterstein@me.com)
 */
sealed abstract class Direction() {
  abstract def next(currentFloor: Int): Int
}

// traveling upstairs
object Up extends Direction {
  def next(currentFloor: Int) = currentFloor + 1
}

// traveling downstairs
object Down extends Direction {
  def next(currentFloor: Int) = currentFloor - 1
}


object Direction {
  def direction(startFloor: Int, targetFloor: Int): Direction = if (startFloor > targetFloor) Down else Up
}