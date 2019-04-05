package Task5;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Class DiningPhilosophers
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class DiningPhilosophers
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	public static final int DEFAULT_ADD_REMOVE_ITERATIONS = 10;

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;

	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv)
	{
		try
		{
			/*
			 * TODO:
			 * Should be settable from the command line
			 * or the default if no arguments supplied.
			 */
			int iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;

			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers);

			// Space for all the philosophers
			ArrayList<Philosopher> aoPhilosophers = new ArrayList<>();

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				Philosopher temp = new Philosopher();
				aoPhilosophers.add(temp);
				aoPhilosophers.get(j).start();
			}

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);

			//start randomly adding or killing philosophers
			ArrayList<Integer> deadList = new ArrayList<>(); //list of killed philosopher indexes
			int count = iPhilosophers; //number of philosophers
			for (int i = 0; i < DEFAULT_ADD_REMOVE_ITERATIONS; i++)
			{
				TimeUnit.SECONDS.sleep(2); //space out time in between adding/removing philosophers
				if (count > 2 && Math.random() < 0.5) //as long as there is 3 or more philosophers left and random < 0.5
				{ //kill someone
					int killIndex = getRandomIndex(aoPhilosophers.size()); //get random index of philosopher to kill
					//and while the index we got is in the dead list, meaning that philosopher is already dead, get a new index
					while (in(killIndex, deadList)) {killIndex = getRandomIndex(aoPhilosophers.size());}
					deadList.add(killIndex); //add that new index to the dead list
					int id = aoPhilosophers.get(killIndex).getTID(); //get that soon-to-be dead philosopher thread id
					System.out.println("Terminating philosopher " + id);
					soMonitor.requestRemove(id); //tell the monitor to remove this thread
					aoPhilosophers.get(killIndex).terminate(); //once monitor gives the green light, terminate thread
					count--; //decrement count
				}
				else //else add a philosopher
				{
					count++; //increment count
					Philosopher temp = new Philosopher();
					System.out.println("Adding philosopher " + temp.getTID());
					soMonitor.requestAdd(temp.getTID()); //tell the monitor we are adding a new philosopher
					aoPhilosophers.add(temp);
					temp.start(); //add and start thread
				}
			}

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for (int j = 0; j < aoPhilosophers.size(); j++)
			{
				aoPhilosophers.get(j).join();
			}

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}

	public static int getRandomIndex(int count)
	{
		return (int) Math.floor(Math.random() * count);
	}

	public static boolean in(int killIndex, ArrayList<Integer> deadList)
	{
		for (int i = 0; i < deadList.size(); i++)
		{
			if (killIndex == deadList.get(i))
			{
				return true;
			}
		}
		return false;
	}
}

// EOF
