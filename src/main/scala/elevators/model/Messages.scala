package elevators.model

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */


/**
 * The request that can be sent to an ElevatorControlSystem to receive the system status
 */
case class SystemStatusRequest()

/**
 * The response for a SystemStatusRequest message
 *
 * @param elevators list representing the different elevator status
 */
case class SystemStatusResponse(elevators: List[ElevatorStatus])


/**
 * Representing the current status of an elevator
 *
 * @param id elevator id
 * @param direction current traveling direction
 */
case class ElevatorStatus(id: Int, direction: ElevatorBehavior)


case class PickupStatusResponse(passenger: Passenger, elevators: List[ElevatorStatus])

case class PickupRequest(passenger: Passenger)

case class Tick