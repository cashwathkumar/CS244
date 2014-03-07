package scheduler;


import java.awt.Adjustable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.HashMap;

import scheduler.Task;


public class LDFAperiodic {

	private LinkedList<Task> waitQueue;
	
	private LinkedList<Task> readyQueue;
	
	private HashMap<Integer, Boolean> completedTask;
	
	private HashMap<Integer, Task> taskIdMap;
	
	private Task currentTask;
	
	private int currentTime;
	
	private Digraph taskGraph;
	
	public LDFAperiodic(LinkedList<Task> taskSet)
	{
		completedTask = new HashMap<Integer, Boolean>();
		
		taskIdMap = new HashMap<Integer, Task>();
		
		this.waitQueue = taskSet;
		
		int maxId = 0;
		
		for(Task t : waitQueue)
		{
			t.setWaiting();
			
			completedTask.put(t.getTaskId(), false);
			taskIdMap.put(t.getTaskId(), t);
			
			if(t.getTaskId() > maxId)
				maxId = t.getTaskId();
		}
		
		taskGraph = new Digraph(maxId);
		
		readyQueue = new LinkedList<Task>();
		
		currentTime = 0;
	}
	
	public void run()
	{
		constructTaskGraph();
		
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
				
				completedTask.put(currentTask.getTaskId(), true); 
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
				return getEffectiveDeadline(t1) - getEffectiveDeadline(t2);
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
	
	private void constructTaskGraph()
	{
		/* Construct the graph*/
		for(Task t : waitQueue)
		{
			int taskId = t.getTaskId();
			
			ArrayList<Integer> precedenceSet = t.getPrecedenceSet();
			
			for(int pid : precedenceSet)
			{
				taskGraph.addEdge(pid, taskId);
			}
		}
	}
	
	private int getEffectiveDeadline(Task t)
	{
		int minDeadline = t.getDeadline();
		
		for(int id : taskGraph.adj(t.getTaskId()))
		{
			int effTaskDeadline = getEffectiveDeadline(taskIdMap.get(id));
			
			if(effTaskDeadline < minDeadline)
				minDeadline = effTaskDeadline;
		}
		
		return minDeadline;
	}
}

