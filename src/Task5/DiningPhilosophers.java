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

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_ADD_REMOVE_ITERATIONS = 10;

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 10;

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
				TimeUnit.SECONDS.sleep(1);
				if (count > 2 && Math.random() < 0.5) //as long as there is 3 or more philosophers left and random < 0.5
				{ //kill someone
					int killIndex = getRandomIndex(count);
					while (in(killIndex, deadList)) {killIndex = getRandomIndex(count);}
					deadList.add(killIndex);
					int id = aoPhilosophers.get(killIndex).getTID();
					System.out.println("Terminating philosopher " + id);
					soMonitor.requestRemove(id);
					aoPhilosophers.get(killIndex).terminate();
					count--;
				}
				else //else add a philosopher
				{
					count++;
					Philosopher temp = new Philosopher();
					System.out.println("Adding philosopher " + temp.getTID());
					soMonitor.requestAdd(temp.getTID());
					aoPhilosophers.add(temp);
					temp.start();
				}
			}

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < aoPhilosophers.size(); j++)
			{
//				if (in(j, deadList))
//					continue;

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
		return (int) Math.floor(Math.random() * (count - 1) + 1);
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
