package scheduler;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class EDFPeriodic {

	private LinkedList<Task> taskSet;
	
	private LinkedList<Task> readyQueue;
	
	private HashMap<Integer, Integer> missedDeadlineMap = new HashMap<Integer, Integer>();
	
	Task currentTask;
	
	int currentTime;
	
	public EDFPeriodic(LinkedList<Task> taskSet)
	{
		this.taskSet = taskSet;
		readyQueue = new LinkedList<Task>();
		
		currentTime = 0;
	}
	
	public void run()
	{	
		/* get the first task*/
		Task t = taskSet.peek();
		
		if(t != null)
		{
			currentTime = t.getArrivalTime();
		
			/*Retrieve the tasks with same arrival time and add to the ready queue*/
			updateReadyQueue();
			
			do
			{
				/* retrieve the first task from the ready queue*/
				currentTask = readyQueue.poll();
				
				Task nextArrTask = taskSet.peek();
				int nextArrTime;
				int execTime;
				int currentTaskRemTime = currentTask.getRemainingTime();
				int currentTaskEndTime = currentTime + currentTaskRemTime;
				
				if(nextArrTask != null)
					nextArrTime = nextArrTask.getArrivalTime();
				else
					nextArrTime = currentTaskEndTime;
				
				execTime = minimum(nextArrTime, currentTaskEndTime, currentTask.getDeadline()) - currentTime;
				
				execTask(execTime);
				
				/* update the remaining time for the task, this also sets the task state to finished if it runs to completion*/
				currentTaskRemTime -= execTime;
				currentTask.setRemainingTime(currentTaskRemTime);
				
				if(currentTask.isFinished())
				{
					System.out.println(currentTime + " " + currentTask.getName() + " ending");
					
					if(readyQueue.isEmpty())
						currentTime = nextArrTime;
					currentTask = null;
				}
				else
				{
					/*check if deadline is missed*/
					if(currentTask.getDeadline() == currentTime)
					{
						System.out.println(currentTime + " " + currentTask.getName() + " deadline missed");
						
						int currentTaskId = currentTask.getId();
						/*record missed deadline*/
						if(missedDeadlineMap.containsKey(currentTaskId))
							missedDeadlineMap.put(currentTaskId, missedDeadlineMap.get(currentTaskId) + 1);
						else
							missedDeadlineMap.put(currentTaskId, 1);
						/*stop further execution of current task*/
						currentTask = null;
					}
				}
				updateReadyQueue();
			}while(!taskSet.isEmpty() || !readyQueue.isEmpty());
		}
		else
		{
			/* Empty task set do nothing*/
		}
		
		/* print missed deadlines*/
		if(missedDeadlineMap.isEmpty())
			System.out.println("No deadlines missed");
		else
		{
			Set<Integer> s = missedDeadlineMap.keySet();
			
			System.out.println("Missed deadlines");
			
			for(int taskId : s)
				System.out.println("Task" + taskId + ": " + missedDeadlineMap.get(taskId));	
		}
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
	
	private void updateReadyQueue()
	{

		while(!taskSet.isEmpty() && taskSet.peek().getArrivalTime() <= currentTime)
			readyQueue.addLast(taskSet.poll());
		
		if(currentTask != null)
		{
			/* current task not preempted*/
			if(currentTask.getDeadline() <= getMinDeadline(currentTask.getDeadline()))
			{
				readyQueue.add(currentTask);
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
	
	private void execTask(int execTime)
	{
		if(currentTask.isNew())
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
}
