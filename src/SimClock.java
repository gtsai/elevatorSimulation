public class SimClock 
{
	private static int simTime; // represents the simulated time
	
	public SimClock() {
		/* Constructor that initializes simulated time to 0 */
		simTime = 0;
	}
	
	public static void tick() {
		/* Increments the simulated time by 1 */
		simTime += 1;
	}
	
	public static int getTime() {
		/* Returns the value of the simulated time */
		return simTime;
	}
}
