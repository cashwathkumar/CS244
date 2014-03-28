package scheduler;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.HashMap;

import scheduler.Task;


public class EDFAperiodic {

	private LinkedList<Task> waitQueue;
	
	private LinkedList<Task> readyQueue;
	
	private LinkedList<Task> taskSet;
	
	private HashMap<Integer, Boolean> completedTask;
	
	private Task currentTask;
	
	private int currentTime;
	
	public EDFAperiodic(LinkedList<Task> taskSet)
	{
		completedTask = new HashMap<Integer, Boolean>();
		
		this.taskSet = taskSet;
		
		currentTime = 0;
		
		readyQueue = new LinkedList<Task>();
		
		waitQueue = new LinkedList<Task>();
		
		for(Task t : taskSet)
		{	
			completedTask.put(t.getTaskId(), false);
		}
	}
	
	public void run()
	{
		updateQueues();
		
		while(!readyQueue.isEmpty() || !taskSet.isEmpty())
		{
			while(readyQueue.isEmpty())
			{
				/*Advance time until next task arrives*/
				currentTime++;
				updateQueues();
			}
			
			currentTask = readyQueue.poll();
			
			int currentTaskRemTime = currentTask.getRemainingTime();
			
			int nextArrivalTime;
			
			if(taskSet.isEmpty())
				nextArrivalTime = currentTask.getDeadline();
			else
				nextArrivalTime = taskSet.peek().getArrivalTime();
			
			int execTime = minimum(nextArrivalTime - currentTime, currentTaskRemTime, currentTask.getDeadline() - currentTime);
			
			execTask(execTime);
			
			currentTask.setRemainingTime(currentTaskRemTime - execTime);
			
			
			if(currentTask.isFinished())
			{
				System.out.println(currentTime + " " + currentTask.getName() + " ending");
				
				completedTask.put(currentTask.getTaskId(), true); 
				currentTask = null;
			}
			else
			{
				if(currentTask.getDeadline() == currentTime)
				{
					/*check if deadline is missed*/
					System.out.println(currentTime + " " + currentTask.getName() + " deadline missed");
					
					/*stop further execution of current task*/
					currentTask = null;
				}
			}
			
			updateQueues();
			
		}
		
	}
	
	private void execTask(int execTime)
	{
		if(currentTask.isNew() || currentTask.isWaiting())
		{
			currentTask.setRunning();
			System.out.println(currentTime + " " + currentTask.getName() + " starting");
		}
		else if(currentTask.isReady())
		{
			currentTask.setRunning();
			System.out.println(currentTime + " " + currentTask.getName() + " resuming");
		}
		else
		{
			/*Task already running*/
		}
		
		try {
			Thread.sleep(execTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentTime += execTime;
	}
	
	private void updateQueues()
	{

		while(!taskSet.isEmpty() && taskSet.peek().getArrivalTime() <= currentTime)
		{
			if(isPreSatisfied(taskSet.peek()))
			{
				readyQueue.addLast(taskSet.poll());
			}
			else
			{
				Task t = taskSet.poll();
				t.setWaiting();
				waitQueue.addLast(t);
			}
		}
		
		for(Task t : waitQueue)
		{
			if(isPreSatisfied(t))
				readyQueue.addLast(t);
		}
		
		for(Task t : readyQueue)
		{
			waitQueue.remove(t);
		}
		
		if(currentTask != null)
		{
			/* current task not preempted*/
			if(currentTask.getDeadline() <= getMinDeadline(currentTask.getDeadline()))
			{
				readyQueue.addFirst(currentTask);
			}
			else
			{
				/*current task gets preempted, add to end of the queue*/
				System.out.println(currentTime + " " + currentTask.getName() + " gets preempted");
				currentTask.setReady();
				readyQueue.addLast(currentTask);
			}
		}
		
		Collections.sort(readyQueue, new Comparator<Task>(){
			public int compare(Task t1, Task t2)
			{
				return t1.getDeadline() - t2.getDeadline();
			}
		});
	}
	
	private boolean isPreSatisfied(Task t)
	{	
		for(int i : t.getPrecedenceSet())
		{
			if(completedTask.get(i) == false)
				return false;
		}
		
		return true;
	}
	
	private int minimum(int x1, int x2, int x3)
	{
		int arr[] = {x1, x2, x3};
		
		int min = arr[0];
		
		for(int i = 0; i < arr.length; i++)
		{
			if(arr[i] < min)
				min = arr[i];
		}
		
		return min;
	}
	
	private int getMinDeadline(int deadline)
	{
		int minDeadline = deadline;
		
		for(Task t : readyQueue)
		{
			if(t.getDeadline() < minDeadline)
				minDeadline = t.getDeadline();
		}
		
		return minDeadline;
	}
}
