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


/**
 * Representing a response for a PickupRequest, including all ElevatorStatus.
 *
 * @param passenger the passenger for the PickupRequest
 * @param elevators the list of status of all Elevators
 */
case class PickupStatusResponse(passenger: Passenger, elevators: List[ElevatorStatus])

/**
 * Representing the request for picking up a Passenger
 *
 * @param passenger the Passenger who wants to be picked up
 */
case class PickupRequest(passenger: Passenger)

/**
 * The Tick, simulating one time unit in this simulation
 */
case class Tick()