package scheduler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Scheduler {

	private static LinkedList<Task> inputTaskSet = new LinkedList<Task>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		readTaskSet();
		
		runScheduler();
	}
	
	private static void readTaskSet()
	{	
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader("TaskSet1.txt"));
			
			String line = "";
			
			while((line = br.readLine()) != null)
			{
				/*split line on spaces*/
				String[] tokens = line.split("[ ]+");
				
				if(tokens.length != 5)
					throw new IOException();
				
				String taskName = tokens[0];
				
				int arrivalTime = Integer.parseInt(tokens[1]);
				int execTime = Integer.parseInt(tokens[2]);
				int deadline = Integer.parseInt(tokens[3]);
				int period = Integer.parseInt(tokens[4]);
				
				inputTaskSet.add(new Task(taskName, arrivalTime, execTime, deadline, period));
				
				Collections.sort(inputTaskSet, new Comparator<Task>(){
					public int compare(Task t1, Task t2)
					{
						return t1.getArrivalTime() - t2.getArrivalTime();
					}
				});
			}
			
			br.close();
		} 
		
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Invalid input");
			e.printStackTrace();
		}
		
		/*check if periodic and generate task set for a hyperperiod*/
		if(inputTaskSet.get(0).getPeriod() > 0)
		{
			int hyperPeriod = calculateHyperPeriod();
			
			LinkedList<Task> tempTaskSet = new LinkedList<Task>();
			
			int firstArrivalTime = inputTaskSet.peek().getArrivalTime();
			
			for(int i = 0; i < inputTaskSet.size(); i++)
			{
				Task t = inputTaskSet.get(i);
				int taskPeriod = t.getPeriod();
				int nextArrTime = t.getArrivalTime();
				int nextDeadline = t.getDeadline();
				int instance = 0;
				
				while(nextArrTime < hyperPeriod + firstArrivalTime)
				{
					Task newInstance = new Task(t);
					
					newInstance.setName(t.getName() + " instance " + instance);
					newInstance.setArrivalTime(nextArrTime);
					newInstance.setDeadline(nextDeadline);
					tempTaskSet.add(newInstance);
					
					nextArrTime += taskPeriod;
					nextDeadline += taskPeriod;
					instance++;
				}
			}
			
			Collections.sort(tempTaskSet, new Comparator<Task>(){
				public int compare(Task t1, Task t2)
				{
					return t1.getArrivalTime() - t2.getArrivalTime();
				}
			});
			
			inputTaskSet = tempTaskSet;
		}
	}
	
	private static int calculateHyperPeriod()
	{
		int[] periods = new int[inputTaskSet.size()];
		int product = 1;
		
		/*read all the periods*/
		for(int i = 0; i < inputTaskSet.size(); i++)
		{
			periods[i] = inputTaskSet.get(i).getPeriod();
			product *= periods[i];
		}
		
		return lcmofarray(periods, 0, periods.length);
	}
	
	/* GCD of two numbers*/
	private static int gcd(int number1, int number2) 
	{
	    if(number2 == 0)
	    {
	        return number1;
	    }
	    return gcd(number2, number1%number2);
	}
	
	/*lcm of 2 numbers*/
	private static int lcm(int a, int b){
	    return ((a*b)/gcd(a,b));

	} 
	
	/*lcm of array of numbers*/
	private static int lcmofarray(int[] arr, int start, int end){
	    if ((end-start)==1) return lcm(arr[start],arr[end-1]);
	    else return (lcm (arr[start], lcmofarray(arr, start+1, end)));
	}
	
	private static void runScheduler()
	{
		runEDFPeriodic();
		
		//runRMPeriodic();
	}
	
	private static void runEDFPeriodic()
	{
		EDFPeriodic sched = new EDFPeriodic(inputTaskSet);
		sched.run();
	}
	
	private static void runRMPeriodic()
	{
		RMPeriodic sched = new RMPeriodic(inputTaskSet);
		sched.run();
	}

}
