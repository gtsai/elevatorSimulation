import java.util.Arrays;

public class BuildingFloor 
{
	private int[] totalDestinationRequests; // array where ith element represents the number of passengers who have requested to go to the ith floor throughout the simulation
	private int[] arrivedPassengers; // array where ith element represents the number of passengers who have arrived on this floor from an elevator throughout the simulation
	private int[] passengerRequests; // array where ith element represents the number of people who currently want to travel to the ith floor
	private int approachingElevator; // elevator id that is currently heading to the floor, -1 represents no elevator
	
	
	public BuildingFloor() {
		/* Constructor */
		totalDestinationRequests = new int[5];
		arrivedPassengers = new int[5];
		passengerRequests = new int[5];
		Arrays.fill(totalDestinationRequests, 0);
		Arrays.fill(arrivedPassengers, 0);
		Arrays.fill(passengerRequests, 0);
		approachingElevator = -1;
	}
	
	/* Getter methods for the BuildingFloor variables */
	
	public int[] getTotalDestinationRequests() {
		return totalDestinationRequests;
	}
	
	public int[] getArrivedPassengers() {
		return arrivedPassengers;
	}
	
	public int[] getPassengerRequests() {
		return passengerRequests;
	}
	
	public int getApproachingElevator() {
		return approachingElevator;
	}
	
	/* Setter methods for the BuildingFloor variables */
	
	public void setTotalDestinationRequests(int[] totalDestinationRequests) {
		this.totalDestinationRequests = totalDestinationRequests;
	}

	public void setArrivedPassengers(int[] arrivedPassengers) {
		this.arrivedPassengers = arrivedPassengers;
	}

	public void setPassengerRequests(int[] passengerRequests) {
		this.passengerRequests = passengerRequests;
	}

	public void setApproachingElevator(int approachingElevator) {
		this.approachingElevator = approachingElevator;
	}
}
