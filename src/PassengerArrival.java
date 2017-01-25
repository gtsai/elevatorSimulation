public class PassengerArrival 
{
	private int numPassengers; // number of passengers that will request an elevator
	private int destinationFloor; // desired destination floor for the passengers
	private int timePeriod; // the periodic time period these passengers will request an elevator
	private int expectedTimeOfArrival; // the simulated time where the next batch of passengers will enter the simulation
	
	public PassengerArrival(int numPassengers, int destinationFloor, int timePeriod) {
		/* Constructor */
		this.numPassengers = numPassengers;
		this.destinationFloor = destinationFloor;
		this.timePeriod = timePeriod;
		int currentSimulatedTime = SimClock.getTime();
		expectedTimeOfArrival = currentSimulatedTime + timePeriod;
	}
	
	/* Getter methods for the PassengerArrival variables */
	
	public int getNumPassengers() {
		return numPassengers;
	}
	
	public int getDestinationFloor() {
		return destinationFloor;
	}
	
	public int getTimePeriod() {
		return timePeriod;
	}
	
	public int getExpectedTimeOfArrival() {
		return expectedTimeOfArrival;
	}
	
	/* Setter methods for the PassengerArrival variables */
	
	public void setNumPassengers(int numPassengers) {
		this.numPassengers = numPassengers;
	}

	public void setDestinationFloor(int destinationFloor) {
		this.destinationFloor = destinationFloor;
	}

	public void setTimePeriod(int timePeriod) {
		this.timePeriod = timePeriod;
	}

	public void setExpectedTimeOfArrival(int expectedTimeOfArrival) {
		this.expectedTimeOfArrival = expectedTimeOfArrival;
	}
}
