package scheduler;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class EDFPeriodic {

	private LinkedList<Task> taskSet;
	
	private LinkedList<Task> readyQueue;
	
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
				
				if(nextArrTask != null)
					nextArrTime = nextArrTask.getArrivalTime();
				else
					nextArrTime = currentTaskRemTime;
				
				if(nextArrTime < currentTaskRemTime)
					execTime = nextArrTime;
				else
					execTime = currentTaskRemTime;
				
				execTask(execTime);
				
				currentTaskRemTime -= execTime;
				currentTask.setRemainingTime(currentTaskRemTime);
				
				updateReadyQueue();
			}while(!taskSet.isEmpty() || !readyQueue.isEmpty());
		}
		else
		{
			/* Empty task set do nothing*/
		}
	}
	
	private void updateReadyQueue()
	{
		if(!taskSet.isEmpty())
		{	
			while(taskSet.peek().getArrivalTime() == currentTime)
				readyQueue.add(taskSet.poll());
			
			if(readyQueue.isEmpty())
			{
				/*no tasks available at currentTime*/
				/*advance time*/
				currentTime = taskSet.peek().getArrivalTime();	
			}
			
			Collections.sort(readyQueue, new Comparator<Task>(){
				public int compare(Task t1, Task t2)
				{
					return t1.getDeadline() - t2.getDeadline();
				}
			});
		}
	}
	
	private void execTask(int execTime)
	{
		System.out.println(currentTime + " " + currentTask.getName());
		
		try {
			Thread.sleep(execTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentTime += execTime;
		
		System.out.println(currentTime + " " + currentTask.getName());
	}
}
