import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ElevatorSimulation 
{
	private BuildingManager manager; // BuildManager object shared by the five elevators
	private int simulation; // total simulation time
	private int simSecondRate; // simulated second rate
	private Elevator r0; // runnable elevator 0
	private Elevator r1; // runnable elevator 1
	private Elevator r2; // runnable elevator 2
	private Elevator r3; // runnable elevator 3
	private Elevator r4; // runnable elevator 4
	private Thread t0; // elevator 0 thread
	private Thread t1; // elevator 1 thread
	private Thread t2; // elevator 2 thread
	private Thread t3; // elevator 3 thread
	private Thread t4; // elevator 4 thread
	private ArrayList<ArrayList<PassengerArrival>> passengerBehaviorPerFloor; // arraylist of arraylist of PassengerArrival objects
	
	public ElevatorSimulation() {
		/* Constructor */
		manager = new BuildingManager();
		passengerBehaviorPerFloor = new ArrayList<ArrayList<PassengerArrival>>();
		r0 = new Elevator(0, manager);
		r1 = new Elevator(1, manager);
		r2 = new Elevator(2, manager);
		r3 = new Elevator(3, manager);
		r4 = new Elevator(4, manager);
		t0 = new Thread(r0);
		t1 = new Thread(r1);
		t2 = new Thread(r2);
		t3 = new Thread(r3);
		t4 = new Thread(r4);
	}
	
	public void start() {
		/* Starts the ElevatorSimulation */
		readPassengerFile("ElevatorConfig.txt");
		t0.start();
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		while(SimClock.getTime() < simulation) {
			try {
				Thread.sleep(simSecondRate);
				SimClock.tick();
				checkPassengerArrivals();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		t0.interrupt();
		t1.interrupt();
		t2.interrupt();
		t3.interrupt();
		t4.interrupt();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		printBuildingState();
	}

	public void checkPassengerArrivals() {
		/* Loops through the ArrayList of ArrayList of PassengerArrival objects,
		 * when passengers are expected to spawn, updateFloorRequests in manager */
		for (int i = 0; i < passengerBehaviorPerFloor.size(); i++) {
			ArrayList <PassengerArrival> floorArrivals = passengerBehaviorPerFloor.get(i);
			for (int j = 0; j < floorArrivals.size(); j++) {
				if (SimClock.getTime() == floorArrivals.get(j).getExpectedTimeOfArrival()) {
					manager.updateFloorRequests(i, floorArrivals.get(j));
					floorArrivals.get(j).setExpectedTimeOfArrival(floorArrivals.get(j).getExpectedTimeOfArrival() + floorArrivals.get(j).getTimePeriod());
				}
			}
		}
	}

	public void readPassengerFile(String file) {
		/* Reads the ElevatorConfig.txt file, parses the information, and 
		 * creates the ArrayList of ArrayList of PassengerArrival objects */
		String s;
		Scanner inFile = null;
		String[] stringArray = null;
		String[] passengerBehaviorArray = null;
		ArrayList<PassengerArrival> passengerArrayList;
		try {
			inFile = new Scanner(new File(file));
			simulation = Integer.parseInt(inFile.nextLine());
			simSecondRate = Integer.parseInt(inFile.nextLine());
			while(inFile.hasNextLine()) {
				s = inFile.nextLine();
				stringArray = s.split(";");
				passengerArrayList = new ArrayList<PassengerArrival>();
				for (int i = 0; i < stringArray.length; i++) {
					passengerBehaviorArray = stringArray[i].split(" ");
					PassengerArrival passengerArrival = new PassengerArrival(Integer.parseInt(passengerBehaviorArray[0]), Integer.parseInt(passengerBehaviorArray[1]), Integer.parseInt(passengerBehaviorArray[2]));
					passengerArrayList.add(passengerArrival);
				}
				passengerBehaviorPerFloor.add(passengerArrayList);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (inFile != null) {
				inFile.close();
			}
		}
	}
	
	public int sumofArray(int[] array) {
		/* Takes an integer array and calculates the sum of all the elements in the array */
		int sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum;
	}
	
	public void printBuildingState() {
		/* Prints the elevator traffic report */
		System.out.println();
		System.out.println("--------------- ELEVATOR TRAFFIC REPORT ---------------");
		System.out.print("Floor 0: ");
		String approachingElevator = (manager.getFloors()[0].getApproachingElevator() > 0) ? Integer.toString(manager.getFloors()[0].getApproachingElevator()):"None";
		System.out.format("%n\t%s %d%n\t%s %d%n\t%s %d%n\t%s %s%n",
				"Total passengers requesting elevator access:", sumofArray(manager.getFloors()[0].getTotalDestinationRequests()),
				"Total passengers exited elevator:", sumofArray(manager.getFloors()[0].getArrivedPassengers()),
				"Current passengers waiting for elevator:", sumofArray(manager.getFloors()[0].getPassengerRequests()),
				"Elevator currently heading to this floor for pickup:", approachingElevator);
		approachingElevator = (manager.getFloors()[1].getApproachingElevator() > 0) ? Integer.toString(manager.getFloors()[1].getApproachingElevator()):"None";
		System.out.print("Floor 1: ");
		System.out.format("%n\t%s %d%n\t%s %d%n\t%s %d%n\t%s %s%n",
				"Total passengers requesting elevator access:", sumofArray(manager.getFloors()[1].getTotalDestinationRequests()),
				"Total passengers exited elevator:", sumofArray(manager.getFloors()[1].getArrivedPassengers()),
				"Current passengers waiting for elevator:", sumofArray(manager.getFloors()[1].getPassengerRequests()),
				"Elevator currently heading to this floor for pickup:", approachingElevator);
		approachingElevator = (manager.getFloors()[2].getApproachingElevator() > 0) ? Integer.toString(manager.getFloors()[2].getApproachingElevator()):"None";
		System.out.print("Floor 2: ");
		System.out.format("%n\t%s %d%n\t%s %d%n\t%s %d%n\t%s %s%n",
				"Total passengers requesting elevator access:", sumofArray(manager.getFloors()[2].getTotalDestinationRequests()),
				"Total passengers exited elevator:", sumofArray(manager.getFloors()[2].getArrivedPassengers()),
				"Current passengers waiting for elevator:", sumofArray(manager.getFloors()[2].getPassengerRequests()),
				"Elevator currently heading to this floor for pickup:", approachingElevator);
		approachingElevator = (manager.getFloors()[3].getApproachingElevator() > 0) ? Integer.toString(manager.getFloors()[3].getApproachingElevator()):"None";
		System.out.print("Floor 3: ");
		System.out.format("%n\t%s %d%n\t%s %d%n\t%s %d%n\t%s %s%n",
				"Total passengers requesting elevator access:", sumofArray(manager.getFloors()[3].getTotalDestinationRequests()),
				"Total passengers exited elevator:", sumofArray(manager.getFloors()[3].getArrivedPassengers()),
				"Current passengers waiting for elevator:", sumofArray(manager.getFloors()[3].getPassengerRequests()),
				"Elevator currently heading to this floor for pickup:", approachingElevator);
		approachingElevator = (manager.getFloors()[4].getApproachingElevator() > 0) ? Integer.toString(manager.getFloors()[4].getApproachingElevator()):"None";
		System.out.print("Floor 4: ");
		System.out.format("%n\t%s %d%n\t%s %d%n\t%s %d%n\t%s %s%n",
				"Total passengers requesting elevator access:", sumofArray(manager.getFloors()[4].getTotalDestinationRequests()),
				"Total passengers exited elevator:", sumofArray(manager.getFloors()[4].getArrivedPassengers()),
				"Current passengers waiting for elevator:", sumofArray(manager.getFloors()[4].getPassengerRequests()),
				"Elevator currently heading to this floor for pickup:", approachingElevator);
		System.out.println();
		System.out.print("Elevator 0: ");
		System.out.format("%n\t%s %d%n\t%s %d%n\t%s %d%n",
				"Total passengers that entered elevator:", r0.getTotalLoadedPassengers(),
				"Total passengers that exited elevator:", r0.getTotalUnloadedPassengers(),
				"Current passengers heading floors:", sumofArray(r0.getPassengerDestinations()));
		System.out.print("Elevator 1: ");
		System.out.format("%n\t%s %d%n\t%s %d%n\t%s %d%n",
				"Total passengers that entered elevator:", r1.getTotalLoadedPassengers(),
				"Total passengers that exited elevator:", r1.getTotalUnloadedPassengers(),
				"Current passengers heading floors:", sumofArray(r1.getPassengerDestinations()));
		System.out.print("Elevator 2: ");
		System.out.format("%n\t%s %d%n\t%s %d%n\t%s %d%n",
				"Total passengers that entered elevator:", r2.getTotalLoadedPassengers(),
				"Total passengers that exited elevator:", r2.getTotalUnloadedPassengers(),
				"Current passengers heading floors:", sumofArray(r2.getPassengerDestinations()));
		System.out.print("Elevator 3: ");
		System.out.format("%n\t%s %d%n\t%s %d%n\t%s %d%n",
				"Total passengers that entered elevator:", r3.getTotalLoadedPassengers(),
				"Total passengers that exited elevator:", r3.getTotalUnloadedPassengers(),
				"Current passengers heading floors:", sumofArray(r3.getPassengerDestinations()));
		System.out.print("Elevator 4: ");
		System.out.format("%n\t%s %d%n\t%s %d%n\t%s %d%n",
				"Total passengers that entered elevator:", r4.getTotalLoadedPassengers(),
				"Total passengers that exited elevator:", r4.getTotalUnloadedPassengers(),
				"Current passengers heading floors:", sumofArray(r4.getPassengerDestinations()));
		System.out.println();
		System.out.println("-------------------------------------------------------");
	}
}
