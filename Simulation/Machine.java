public class Machine implements Comparable<Machine> {

	private String name;
	
	private Integer activityTime;
	
	private Integer breakTime;
	
	private boolean isBroken;
			
	Machine(String name, Integer time) {
		this.name = name;
		this.activityTime = time;
		this.isBroken = false;
		}
	
	public String getName() {
		return name;
	}

	public Integer getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(Integer time) {
		this.activityTime = time;
	}

	public Integer getBreakTime() {
		return breakTime;
	}

	public void setBreakTime(Integer breakTime) {
		this.breakTime = breakTime;
	}

	public boolean isBroken() {
		return isBroken;
	}

	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}
	
	@Override
	public int compareTo(Machine o) {
		return (this.activityTime >= o.activityTime ) ? 1:-1;
	}
	
}
