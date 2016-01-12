# Having fun with akka and elevators

This project shows how to i would handle the problem of an elevator control system with the actor based programming model akka.

# Implementation

The implementation is done via the akka actor based framework. There are two main actor types, the Elevator actor and the
ElevatorControlSystemActor.

# Scheduling algorithm

I am not that fancy scheduling wizard, but my though about scheduling elevators was:
- Elevators do have the same direction until there are no events (pickups or releases) are on their current direction
- Then they change their direction if there are currently passengers inside the elevator or to pickup
- Then the procedure starts at the beginning

First i had a random scheduling of elevators to PickupRequests which was quite simple and not effective. I changed
this implementation to a (hopefully) more efficient scheduling, which works as followed.
- If idling elevators are available pick nearest elevator located to the pickup location
- If no idling elevators are available, but elevators on the same way, pick the nearest of them
- If non above is available pick the overall nearest elevator

The last step in this scheduling may be optimized by further development (load of elevator, other passengers, ..)

