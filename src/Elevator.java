import java.util.ArrayList;
import java.util.Arrays;

public class Elevator implements Runnable
{
	private int elevatorId; // unique ID for specific elevator
	private int currentFloor; // current floor the elevator is on
	private int numPassengers; // current number of passengers in the elevator
	private int totalLoadedPassengers; // total number of passengers this elevator loaded
	private int totalUnloadedPassengers; // total number of passengers this elevator unloaded
	private ArrayList<ElevatorEvent> moveQueue; // an ArrayList that contains Elevator Events that define the movement of an elevator and the anticipated time of arriving at the destination
	private int[] passengerDestinations; // an array where the ith element represents the number of current passengers whose destination is the ith floor
	private BuildingManager manager; // an instance of a building manager to update the state of the building
	
	public Elevator(int elevatorId, BuildingManager manager) {
		/* Constructor */
		this.elevatorId = elevatorId;
		this.manager = manager;
		currentFloor = 0;
		numPassengers = 0;
		totalLoadedPassengers = 0;
		totalUnloadedPassengers = 0;
		moveQueue = new ArrayList<ElevatorEvent>();
		passengerDestinations = new int[5];
		Arrays.fill(passengerDestinations, 0);
	}
	
	/* Getter methods for the Elevator variables */
	
	public int getElevatorId() {
		return elevatorId;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public int getNumPassengers() {
		return numPassengers;
	}

	public int getTotalLoadedPassengers() {
		return totalLoadedPassengers;
	}

	public int getTotalUnloadedPassengers() {
		return totalUnloadedPassengers;
	}

	public int[] getPassengerDestinations() {
		return passengerDestinations;
	}

	public void run() {
		/* Runs the required Elevator runnable method */
		while (true && !Thread.interrupted()) { // if elevator is in idle state
			if (numPassengers == 0 && moveQueue.size() == 0) {
				Arrays.fill(passengerDestinations, 0);
				int requestFloor = manager.hasPassengerRequest(currentFloor, elevatorId);
				if (requestFloor == -1) { // if no requests, continue to next iteration of while loop and check again
					continue;
				}
				for (int i = 0; i < manager.getFloors()[requestFloor].getPassengerRequests().length; i++) {
					passengerDestinations[i] = manager.getFloors()[requestFloor].getPassengerRequests()[i];
				}
				Arrays.fill(manager.getFloors()[requestFloor].getPassengerRequests(), 0);
				int currentTime = SimClock.getTime();
				int moveQueueCount = 0;
				for (int i = requestFloor + 1; i < passengerDestinations.length; i++) {
					if (passengerDestinations[i] == 0) {
						continue;
					} else {
						int expectedArrival = currentTime + (Math.abs(requestFloor - currentFloor)*5) + 10 + (Math.abs(i - requestFloor)*5) + 10 + (moveQueueCount*10);
						ElevatorEvent elevatorEvent = new ElevatorEvent(i, expectedArrival);
						moveQueue.add(elevatorEvent);
						totalLoadedPassengers += passengerDestinations[i];
						numPassengers += passengerDestinations[i];
						moveQueueCount++;
					}
				}
				if (moveQueue.size() == 0) {
					currentTime = SimClock.getTime();
					moveQueueCount = 0;
					for (int i = requestFloor - 1; i >= 0; i--) {
						if (passengerDestinations[i] == 0) {
							continue;
						} else {
							int expectedArrival = currentTime + (Math.abs(requestFloor - currentFloor)*5) + 10 + (Math.abs(i - requestFloor)*5) + 10 + (moveQueueCount*10);
							ElevatorEvent elevatorEvent = new ElevatorEvent(i, expectedArrival);
							moveQueue.add(elevatorEvent);
							totalLoadedPassengers = totalLoadedPassengers + passengerDestinations[i];
							numPassengers += passengerDestinations[i];
							moveQueueCount++;
						}
					}
				}
				System.out.format("Time %-4d | %-8s | Elevator %d On Floor %d | moving to Floor %d%n", SimClock.getTime(), "PICK UP", elevatorId, currentFloor, requestFloor);
				currentFloor = requestFloor;
				manager.getFloors()[currentFloor].setApproachingElevator(-1);
			} else if (numPassengers > 0 && moveQueue.size() > 0) { // if elevator is in dropoff state
				for (int i = 0; i < moveQueue.size(); i++) {
					if (SimClock.getTime() == moveQueue.get(i).getExpectedArrival()) {
						currentFloor = moveQueue.get(i).getDestination();
						totalUnloadedPassengers = totalUnloadedPassengers + passengerDestinations[currentFloor];
						numPassengers -= passengerDestinations[currentFloor];
						System.out.format("Time %-4d | %-8s | Elevator %d On Floor %d | unloading %d passengers on Floor %d%n", SimClock.getTime(), "DROP OFF", elevatorId, currentFloor, passengerDestinations[currentFloor], moveQueue.get(i).getDestination());
						manager.updateFloorArrivals(currentFloor, passengerDestinations[currentFloor], elevatorId);
						passengerDestinations[currentFloor] = 0;
						moveQueue.remove(i);
					} 
				}
			}
		}
	}
}
