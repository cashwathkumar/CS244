package scheduler;

public class Task{

	public enum State {
		NEW, READY, RUNNING, WAITING, FINISHED
	}
	
	String name;
	
	int totalExecTime;
	
	int absDeadline;
	
	int arrivalTime;
	
	int remainingTime;
	
	int period;
	
	State state;
	
	public Task(String name, int arrivalTime, int execTime, int deadline, int period)
	{
		this.name = name;
		this.state = State.NEW;
		this.arrivalTime = arrivalTime;
		this.totalExecTime = execTime;
		this.absDeadline = deadline;
		this.remainingTime = execTime;
		this.period = period;
	}
	
	public Task(Task copy)
	{
		this.name = copy.name;
		this.state = copy.state;
		this.arrivalTime = copy.arrivalTime;
		this.totalExecTime = copy.totalExecTime;
		this.absDeadline = copy.absDeadline;
		this.remainingTime = copy.remainingTime;
		this.period = copy.period;
	}
	
	public void setReady()
	{
		state = State.READY;
	}
	
	public boolean isReady()
	{
		return state == State.READY;
	}
	
	public void setRunning()
	{
		state = State.RUNNING;
	}
	
	public void setWaiting()
	{
		state = State.WAITING;
	}
	
	public void setFinished()
	{
		state = State.FINISHED;
	}
	
	public boolean isFinished()
	{
		return state == State.FINISHED;
	}
	
	public int getArrivalTime()
	{
		return arrivalTime;
	}
	
	public int getDeadline()
	{
		return absDeadline;
	}
	
	public int getExecutionTime()
	{
		return totalExecTime;
	}
	
	public int getRemainingTime()
	{
		return remainingTime;
	}
	
	public void setRemainingTime(int rtime)
	{
		this.remainingTime = rtime;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getPeriod()
	{
		return period;
	}
	
	public void setArrivalTime(int arrTime)
	{
		this.arrivalTime = arrTime;
	}
}
