package elevators.model

/**
 * A passenger is represented through a tuple of integers,
 * the start and the target floor.
 *
 * @author Johannes Unterstein (unterstein@me.com)
 */
case class Passenger(startFloor: Int, targetFloor: Int, passengerId: String = java.util.UUID.randomUUID.toString) {

  val travelingDirection: Direction = Direction.direction(startFloor, targetFloor)
}
