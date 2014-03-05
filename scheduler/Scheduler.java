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
			
			for(int i = 0; i < inputTaskSet.size(); i++)
			{
				Task t = inputTaskSet.get(i);
				int taskPeriod = t.getPeriod();
				int arrivalTime = t.getArrivalTime();
				int nextArrTime = arrivalTime;
				
				while(nextArrTime < hyperPeriod + arrivalTime)
				{
					Task newInstance = new Task(t);
					newInstance.setArrivalTime(nextArrTime);
					tempTaskSet.add(newInstance);
					
					nextArrTime += taskPeriod;
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
		
		return product/gcd(periods);
	}
	
	/* GCD of n numbers*/
	private static int gcd(int[] numbers)
	{
	    int result = numbers[0];
	    
	    for(int i = 1; i < numbers.length; i++)
	        result = gcd(result, numbers[i]);
	    return result;
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
