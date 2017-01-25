public class ElevatorEvent 
{
	private int destination; // floor that the elevator needs to move to
	private int expectedArrival; // expected simulated time that the elevator arrive at the destination floor
	
	public ElevatorEvent(int destination, int expectedArrival) {
		/* Constructor */
		this.destination = destination;
		this.expectedArrival = expectedArrival;
	}
	
	/* Getter methods for the ElevatorEvent variables */
	
	public int getDestination() {
		return destination;
	}
	
	public int getExpectedArrival() {
		return expectedArrival;
	}
	
	/* Setter methods for the ElevatorEvent variables */
	
	public void setDestination(int destination) {
		this.destination = destination;
	}

	public void setExpectedArrival(int expectedArrival) {
		this.expectedArrival = expectedArrival;
	}
}
