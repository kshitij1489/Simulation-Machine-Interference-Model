import java.lang.reflect.Array;
import java.util.ArrayList;

public class Machine implements Comparable{

	private String name;
	
	private Integer activityTime;
	
	Integer brokenTime;
	
	private ArrayList<Integer> brokenTimeList = new ArrayList<Integer>();
	private ArrayList<Integer> repairTimeList = new ArrayList<Integer>();
	
	boolean isBroken = false;
	
	boolean inServer = false;
	
	Machine(String name, Integer time) {
		this.name = name;
		this.activityTime = time;
		}

	public String getName() {
		return name;
	}

	public Integer getTime() {
		return activityTime;
	}

	public void setTime(Integer time) {
		this.activityTime = time;
	}

	public boolean isBroken() {
		return isBroken;
	}

	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}

	public boolean isInServer() {
		return inServer;
	}

	public void setInServer(boolean inServer) {
		this.inServer = inServer;
	}

	@Override
	public int compareTo(Object o) {
		Machine m = (Machine)o;
		
		return (this.activityTime >= m.activityTime ) ? 1:-1;
	}

	public void insertBrokenTime(Integer brokenTime) {
		this.brokenTimeList.add(brokenTime);
	}
	
	public void insertReparTime(Integer brokenTime) {
		this.repairTimeList.add(brokenTime);
	}
	
	public ArrayList<Integer> getBrokenTimeList() {
		return brokenTimeList;
	}
	
	public ArrayList<Integer> getReparTimeList() {
		return repairTimeList;
	}
	
	public int getBreakDown(){
		if(this.repairTimeList.size() == 0)
			return 0;
		
		return this.repairTimeList.get(this.repairTimeList.size()-1) - this.brokenTimeList.get(this.repairTimeList.size()-1);
	}
}
