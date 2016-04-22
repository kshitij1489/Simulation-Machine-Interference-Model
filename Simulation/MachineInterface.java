import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class MachineInterface {
	
	public static double getNext(Random rand, double interArriv) {
	    return  Math.log(1-rand.nextDouble())/(-(double)1/interArriv);
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
		randArrival .setSeed(1);
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
		int repairCount = 0;
		ArrayList<Integer> breakdownList = new ArrayList<Integer>();
		int MC = 0;
		System.out.println(" MC =  " + MC + "| CL1 " + CL1.getTime()+ "| CL2 " + CL2.getTime() +  "| CL3 " + CL3.getTime() + "| CL4 " + " - " + "| n " + queue.size() + " | " + serverStatus  );
		while( repairCount < 10 ) {
		
			if( (!arrival.isEmpty() &&  MC == arrival.peek().getTime()) || MC == server.getTime() ) {
				if (!arrival.isEmpty() && (arrival.peek().getTime() < server.getTime())) {
					while(!arrival.isEmpty() && arrival.peek().getTime() == MC)
					{
					//enter time
					Machine m = arrival.remove();
					m.brokenTime = MC;
					queue.add(m);
					}	
					if(serverBusy == false) {
						serverBusy = true;
						server = queue.remove();
						server.setTime(MC + (int) Math.ceil(getNext(randService, meanServiceTime)));
					}
				} else if(!arrival.isEmpty() && arrival.peek().getTime().equals(server.getTime())) {
					while(!arrival.isEmpty() && arrival.peek().getTime() == MC)
						{ 
						//enter time
						Machine m = arrival.remove();
						m.brokenTime=MC;
						queue.add(m);
						}
					Machine temp = server;
					temp.setTime(MC + (int) Math.ceil(getNext(randArrival, meanOperationalTime)));
					// add time
					breakdownList.add(MC-temp.brokenTime);
					arrival.add(temp);
					temp.brokenTime=-1;
					//Update repair count
					repairCount++;
					server = queue.remove();
					server.setTime(MC+(int) Math.ceil(getNext(randService, meanServiceTime))); 					
				} else {
					Machine temp = server;
					
					temp.setTime(MC + (int) Math.ceil(getNext(randArrival, meanOperationalTime)));
					//Update repair count
					breakdownList.add(MC-temp.brokenTime);
					temp.brokenTime = -1;
					// add time
					temp.insertReparTime(MC);
					arrival.add(temp);
					server = new Machine("dummy", Integer.MAX_VALUE);
					repairCount++;
					if(queue.isEmpty()){
						serverBusy=false;
					} else{
						server = queue.remove();
						server.setTime(MC+(int) Math.ceil(getNext(randService, meanServiceTime)));
					}
				}
				// Printing
				int serverCount = serverBusy?1:0;
				serverStatus = serverBusy?"busy":"idle";
				String printCL1, printCL2, printCL3;
				printCL1 = CL1.getTime().toString();
				printCL2 = CL2.getTime().toString();
				printCL3 = CL3.getTime().toString();
				Iterator<Machine> it = queue.iterator();
				PriorityQueue<Machine> q = new PriorityQueue<Machine>();
				while(it.hasNext())
					q.add((Machine)it.next());
				q.add(server);
				while(!q.isEmpty()){
					String CL = q.remove().getName().toString();
					switch(Integer.valueOf(CL.charAt(2))) {
					case 1: printCL1 = " - ";
					break;
					case 2: printCL2 = " - ";
					break;
					case 3: printCL3 = " - ";
					break;
					}
				}
				String serverTime = server.getTime().toString();
				if (server.getTime()==Integer.MAX_VALUE)
					serverTime = " - ";
				System.out.println(" MC =  " + MC + "| CL1 " + printCL1 + "| CL2 " + printCL2 +  "| CL3 " + printCL3 + "| CL4 " + serverTime + "| n " + (queue.size()+serverCount) + " | " + serverStatus  );								
			}
			MC++;
		}
		//System.out.println("exit");
		System.out.println(" Number of Observations = " + breakdownList.size());
		
	}
		
	}

