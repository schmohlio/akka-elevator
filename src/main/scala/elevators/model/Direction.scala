package elevators.model

/**
 * Represents the traveling direction of a passenger
 *
 * @author Johannes Unterstein (unterstein@me.com)
 */
sealed abstract class Direction()

// traveling upstairs
object Up extends Direction

// traveling downstairs
object Down extends Direction


object Direction {
  def direction(startFloor: Int, targetFloor: Int): Direction = if (startFloor > targetFloor) Down else Up
}