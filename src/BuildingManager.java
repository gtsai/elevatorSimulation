import java.util.Arrays;

public class BuildingManager 
{
	private BuildingFloor[] floors; // array of BuildingFloor objects
	
	public BuildingManager() {
		/* Constructor */
		floors = new BuildingFloor[5];
		for (int i = 0; i < floors.length; i++) {
			floors[i] = new BuildingFloor();
		}
	}

	public void updateFloorRequests(int currentFloor, PassengerArrival passengerArrival) {
		/* Updates the totalDestinationRequest array and passengerRequests array for each floor */
		floors[currentFloor].getTotalDestinationRequests()[passengerArrival.getDestinationFloor()] += passengerArrival.getNumPassengers();
		Arrays.fill(floors[currentFloor].getPassengerRequests(), 0);
		floors[currentFloor].getPassengerRequests()[passengerArrival.getDestinationFloor()] = passengerArrival.getNumPassengers();
		System.out.format("Time %-4d | %-8s | On Floor %d            | %d passengers request to go to floor %d%n", SimClock.getTime(), "SPAWNING", currentFloor, passengerArrival.getNumPassengers(), passengerArrival.getDestinationFloor());
	}

	public void updateFloorArrivals(int currentFloor, int numOfPassengers, int elevator) {
		/* Updates the arrivedPassengers array for each floor */
		floors[currentFloor].getArrivedPassengers()[elevator] += numOfPassengers;
	}
	
	public synchronized int hasPassengerRequest(int floor, int elevatorId) {
		/* Checks all the floors to see if there are passenger requests
		 * Returns the floor with requests */
		for (int i = floor; i < floors.length; i++) {
			for (int j = 0; j < floors[i].getPassengerRequests().length; j++) {
				if (floors[i].getPassengerRequests()[j] > 0) {
					if (checkAndUpdateApproachingElevator(i, elevatorId)) {
						return i;
					}
				}
			}
		}
		for (int i = floor; i >= 0; i--) {
			for (int j = 0; j < floors[i].getPassengerRequests().length; j++) {
				if (floors[i].getPassengerRequests()[j] > 0) {
					if (checkAndUpdateApproachingElevator(i, elevatorId)) {
						return i;
					}
				}
			}
		}
		return -1; // return -1 for no passenger requests
	}
	
	public synchronized boolean checkAndUpdateApproachingElevator(int floor, int elevatorId) {
		/* Checks if there is no elevator approaching the floor and updates 
		 * the approachingElevator for a given floor to an elevatorId, returns
		 * true if variable was updated */
		if (floors[floor].getApproachingElevator() == -1) {
			floors[floor].setApproachingElevator(elevatorId);
			return true;
		}
		return false;
	}
	
	public void printArray(int[] array) {
		/* Takes an integer array and prints out each element in the array */
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + " ");
		}
		System.out.println();
	}
	
	public BuildingFloor[] getFloors() {
		/* Getter method for the array of BuildingFloor objects */
		return floors;
	}

}