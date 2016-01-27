package elevators.model

/**
 * Represents the traveling direction of a passenger
 *
 * @author Johannes Unterstein (unterstein@me.com)
 */
sealed abstract class Direction() {

  /**
   * Calculating the next floor number, according to the direction Down or Up and the given floor number
   * @param currentFloor the current flor number
   * @return the next floor number
   */
  def next(currentFloor: Int): Int = currentFloor

  /**
   * Calculates if based on the given currentFloor and the direction, if the targetFloor is on the current way.
   * This is needed to calculate if a passenger can be collected easily by a Elevator.
   *
   * @param otherDirection current direction
   * @param currentFloor the current floor number of the elevator
   * @param targetFloor the to be checked floor number
   * @return true if the targetf loor number is on the way, false otherwise
   */
  def onTheWay(otherDirection: Direction, currentFloor: Int, targetFloor: Int) = otherDirection == this
}

// traveling upstairs
object Up extends Direction {
  override def next(currentFloor: Int) = currentFloor + 1

  override def onTheWay(otherDirection: Direction, currentFloor: Int, targetFloor: Int): Boolean = super.onTheWay(otherDirection, currentFloor, targetFloor) && currentFloor <= targetFloor

  override def toString: String = "Up"
}

// traveling downstairs
object Down extends Direction {
  override def next(currentFloor: Int) = currentFloor - 1

  override def onTheWay(otherDirection: Direction, currentFloor: Int, targetFloor: Int): Boolean = super.onTheWay(otherDirection, currentFloor, targetFloor) && currentFloor >= targetFloor

  override def toString: String = "Down"
}


object Direction {
  def direction(startFloor: Int, targetFloor: Int): Direction = if (startFloor > targetFloor) Down else Up
}