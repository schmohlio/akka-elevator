package elevators.model

/**
 * A passenger is represented through a tuple of integers,
 * the start and the target floor.
 *
 * @author Johannes Unterstein (unterstein@me.com)
 */
case class Passenger(startFloor: Int, targetFloor: Int) { // TODO case class -> unique id

  val travelingDirection: Direction = Direction.direction(startFloor, targetFloor)
}
