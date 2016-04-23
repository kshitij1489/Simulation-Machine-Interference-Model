import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class MachineInterface {

	public static double getNext(Random rand, double interArriv) {
		return Math.log(1-rand.nextDouble())/(-(double)1/interArriv);
		
		
	}

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Mean Operational Time");
		double meanOperationalTime = sc.nextDouble();
		System.out.println("Enter the Mean Repair Time");
		double meanServiceTime = sc.nextDouble();

		Random randService = new Random();
		Random randArrival = new Random();
		randService.setSeed(1);
		randArrival.setSeed(1);
		boolean serverBusy = false;
		String serverStatus = "idle";
		Machine server = new Machine("dummy", Integer.MAX_VALUE);
		PriorityQueue<Machine> queue = new PriorityQueue<Machine>();
		PriorityQueue<Machine> arrival = new PriorityQueue<Machine>();
		Machine CL1 = new Machine("CL1", 1);
		Machine CL2 = new Machine("CL2", 4);
		Machine CL3 = new Machine("CL3", 9);

		arrival.add(CL1);
		arrival.add(CL2);
		arrival.add(CL3);
		ArrayList<Integer> breakdownList = new ArrayList<Integer>();
		int MC = 0;
		System.out.println(" MC =  " + MC + "| CL1 " + CL1.getActivityTime() + "| CL2 " + CL2.getActivityTime()
				+ "| CL3 " + CL3.getActivityTime() + "| CL4 " + " - " + "| n " + queue.size() + " | " + serverStatus);
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
				// Printing
				int serverCount = serverBusy ? 1 : 0;
				serverStatus = serverBusy ? "busy" : "idle";
				String printCL1, printCL2, printCL3;
				printCL1 = CL1.isBroken() ? "-": CL1.getActivityTime().toString();
				printCL2 = CL2.isBroken() ? "-": CL2.getActivityTime().toString();
				printCL3 = CL3.isBroken() ? "-": CL3.getActivityTime().toString();
				String serverTime = server.getActivityTime().toString();
				if (server.getActivityTime() == Integer.MAX_VALUE)
					serverTime = " - ";
				System.out.println(" MC =  " + MC + "| CL1 " + printCL1 + "| CL2 " + printCL2 + "| CL3 " + printCL3
						+ "| CL4 " + serverTime + "| n " + (queue.size() + serverCount) + " | " + serverStatus);
			}
			MC++;
		}
		// System.out.println("exit");
		System.out.println(" Number of Observations = " + breakdownList.size());

	}

}
