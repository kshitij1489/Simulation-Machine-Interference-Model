import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class MachineInterface {

	// Function returns a number which follows exponential distribution,
	// with rate parameter lambda as interArriv.
	public static double getNext(Random rand, double interArriv) {
		return Math.log(1-rand.nextDouble())/(-(double)1/interArriv);
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Mean Operational Time");
		double meanOperationalTime = sc.nextDouble();
		System.out.println("Enter the Mean Repair Time");
		double meanServiceTime = sc.nextDouble();
		System.out.println("Enter the number of machines in the System");
		int machineCount = sc.nextInt();

		Random randService = new Random();
		Random randArrival = new Random();
		randService.setSeed(1);
		randArrival.setSeed(1);
		boolean serverBusy = false;
		String serverStatus = "idle";

		// This object is the Repairman
		Machine server = new Machine("dummy", Integer.MAX_VALUE);
		
		// This queue holds the broken machines to be serviced/ repaired.
		PriorityQueue<Machine> queue = new PriorityQueue<Machine>();
		
		// This queue simulates the breaking down of the machines.
		PriorityQueue<Machine> arrival = new PriorityQueue<Machine>();
		
		// Create Machines objects
		Machine[] machineList = initializeMachines(arrival, randArrival, meanOperationalTime, machineCount);

		ArrayList<Integer> breakdownList = new ArrayList<Integer>();
		int MC = 0;
		// This method is used to print the initial states of the machines
		printInitialState(machineList, MC, queue, serverStatus);
		while (MC < 100) {
			// This statement checks if a machine is breaking down or is getting
			// repaired.
			if ((!arrival.isEmpty() && MC == arrival.peek().getActivityTime()) || MC == server.getActivityTime()) {
				// Event: 1. Machine/s is/are breaking down
				// 2. No machine is getting repaired.
				if (!arrival.isEmpty() && (arrival.peek().getActivityTime() < server.getActivityTime())) {
					while (!arrival.isEmpty() && arrival.peek().getActivityTime() == MC) {
						// enter time
						Machine m = arrival.remove();
						m.setBreakTime(MC);
						m.setBroken(true);
						queue.add(m);
					}
					if (serverBusy == false) {
						serverBusy = true;
						server = queue.remove();
						server.setActivityTime(MC + (int) Math.ceil(getNext(randService, meanServiceTime)));
					}
				}
				// Event: 1. A machine is getting repaired
				// 2. machines are breaking down.
				else if (!arrival.isEmpty() && arrival.peek().getActivityTime().equals(server.getActivityTime())) {
					while (!arrival.isEmpty() && arrival.peek().getActivityTime() == MC) {
						// enter time
						Machine m = arrival.remove();
						m.setBreakTime(MC);
						m.setBroken(true);
						queue.add(m);
					}
					Machine temp = server;
					temp.setBroken(false);
					temp.setActivityTime(MC + (int) Math.ceil(getNext(randArrival, meanOperationalTime)));
					breakdownList.add(MC - temp.getBreakTime());
					arrival.add(temp);
					temp.setBreakTime(-1);
					// Update repair count
					server = queue.remove();
					server.setActivityTime(MC + (int) Math.ceil(getNext(randService, meanServiceTime)));
					// Event: 1. A Machine getting repaired.
				} else {
					Machine temp = server;
					temp.setBroken(false);
					temp.setActivityTime(MC + (int) Math.ceil(getNext(randArrival, meanOperationalTime)));
					// Update repair count
					breakdownList.add(MC - temp.getBreakTime());
					arrival.add(temp);
					server = new Machine("dummy", Integer.MAX_VALUE);
					if (queue.isEmpty()) {
						serverBusy = false;
					} else {
						server = queue.remove();
						server.setActivityTime(MC + (int) Math.ceil(getNext(randService, meanServiceTime)));
					}
				}
				// Printing the current activity and states of each machines.
				printActivity(machineList, server, serverBusy,  MC, queue, serverStatus);
				
			}
			MC++;
		}

	}
	// This method create Machine objects, sets their initial breakdown time and returns the Machine object array.
	static Machine[] initializeMachines(PriorityQueue<Machine> arrival, Random randArrival, double meanOperationalTime, int count) {
		
		Machine[] array = new Machine[count];
		for(int i = 0; i < count; i++) {
			Machine m = new Machine("CL"+(i+1), (int)getNext(randArrival, meanOperationalTime));
			arrival.add(m);
			array[i] = m;
		}
		return array;
	}
	
	// This method prints the initial states of the machines
	private static void printInitialState(Machine[] machines, int MC, PriorityQueue<Machine> queue, String serverStatus) {
		StringBuilder br = new StringBuilder();
		br.append(" MC = " + MC);
		for(int i = 0; i < machines.length; i++) {
			br.append("| " +machines[i].getName() +" ");
			br.append(machines[i].getActivityTime() +" "); 
			
		}
		br.append("| RepairStation - | n " + queue.size() + " | " + serverStatus);
		System.out.println(br.toString());
	}
	
	// This method prints the current activity and states of each machines.
	static void printActivity(Machine[] machines, Machine server, boolean serverBusy, int MC, PriorityQueue<Machine> queue, String serverStatus) {
		
		StringBuilder br = new StringBuilder();
		int serverCount = serverBusy ? 1 : 0;
		serverStatus = serverBusy ? "busy" : "idle";
		br.append(" MC = " + MC);
		for(int i = 0; i < machines.length; i++) {
			br.append("| " +machines[i].getName() +" ");
			br.append(machines[i].isBroken() ? "-": machines[i].getActivityTime().toString() +" "); 
		}
		String serverTime = server.getActivityTime().toString();
		if (server.getActivityTime() == Integer.MAX_VALUE)
			serverTime = " - ";
		br.append("| RepairStation " + serverTime  +"| n " + (queue.size() + serverCount) + " | " + serverStatus);
		System.out.println(br.toString());
	}
	
	
}
