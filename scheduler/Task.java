package scheduler;

import java.util.ArrayList;

public class Task{

	public enum State {
		NEW, READY, RUNNING, WAITING, FINISHED
	}
	
	int taskId;
	
	String name;
	
	int totalExecTime;
	
	int absDeadline;
	
	int arrivalTime;
	
	int remainingTime;
	
	int period;
	
	State state;
	
	ArrayList<Integer> preList = new ArrayList<Integer>();
	
	public Task(int taskId, int arrivalTime, int execTime, int deadline, int period)
	{
		this.taskId = taskId;
		this.name = "Task" + taskId;
		this.state = State.NEW;
		this.arrivalTime = arrivalTime;
		this.totalExecTime = execTime;
		this.absDeadline = deadline;
		this.remainingTime = execTime;
		this.period = period;
	}
	
	public Task(Task copy)
	{
		this.taskId = copy.taskId;
		this.name = copy.name;
		this.state = copy.state;
		this.arrivalTime = copy.arrivalTime;
		this.totalExecTime = copy.totalExecTime;
		this.absDeadline = copy.absDeadline;
		this.remainingTime = copy.remainingTime;
		this.period = copy.period;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setReady()
	{
		state = State.READY;
	}
	
	public boolean isReady()
	{
		return state == State.READY;
	}
	
	public boolean isNew()
	{
		return state == State.NEW;
	}
	
	public boolean isWaiting()
	{
		return state == State.WAITING;
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
	
	public void setDeadline(int deadline)
	{
		this.absDeadline = deadline;
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
		
		if(remainingTime == 0)
			setFinished();
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getTaskId()
	{
		return taskId;
	}
	
	public int getPeriod()
	{
		return period;
	}
	
	public void setArrivalTime(int arrTime)
	{
		this.arrivalTime = arrTime;
	}
	
	public String toString()
	{
		return name + "\narrivalTime: " + arrivalTime + "\nExecTime: " + totalExecTime + "\nDeadline: " + absDeadline + "\nPeriod" + period;
	}
	
	public void setPrecedence(int taskId)
	{
		preList.add(taskId);
	}
	
	public ArrayList<Integer> getPrecedenceSet()
	{
		return preList;
	}
}
