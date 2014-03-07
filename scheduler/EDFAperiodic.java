package scheduler;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.HashMap;
import scheduler.Task;


public class EDFAperiodic {

	private LinkedList<Task> waitQueue;
	
	private LinkedList<Task> readyQueue;
	
	private HashMap<Integer, Boolean> completedTask;
	
	private Task currentTask;
	
	private int currentTime;
	
	public EDFAperiodic(LinkedList<Task> taskSet)
	{
		completedTask = new HashMap<Integer, Boolean>();
		
		this.waitQueue = taskSet;
		
		for(Task t : waitQueue)
		{
			t.setWaiting();
			
			completedTask.put(t.getId(), false);
		}
		
		
		readyQueue = new LinkedList<Task>();
		
		currentTime = 0;
	}
	
	public void run()
	{
		updateQueues();
		
		while(!readyQueue.isEmpty())
		{
			
			
			currentTask = readyQueue.poll();
			
			int currentTaskRemTime = currentTask.getRemainingTime();
			
			int execTime = Math.min(currentTaskRemTime, currentTask.getDeadline() - currentTime);
			
			execTask(execTime);
			
			currentTask.setRemainingTime(currentTaskRemTime - execTime);
			
			
			if(currentTask.isFinished())
			{
				System.out.println(currentTime + " " + currentTask.getName() + " ending");
				
				completedTask.put(currentTask.getId(), true); 
				currentTask = null;
			}
			else
			{
				/*check if deadline is missed*/
				System.out.println(currentTime + " " + currentTask.getName() + " deadline missed");
				
				/*stop further execution of current task*/
				currentTask = null;
			}
			
			updateQueues();
			
		}
		
	}
	
	private void execTask(int execTime)
	{
		if(currentTask.isReady())
		{
			currentTask.setRunning();
			System.out.println(currentTime + " " + currentTask.getName() + " starting");
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

		for(Task t : waitQueue)
		{
			if(isPreSatisfied(t))
			{
				t.setReady();
				readyQueue.addLast(t);
			}
		}
		
		for(Task t : readyQueue)
		{
			waitQueue.remove(t);
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
}
