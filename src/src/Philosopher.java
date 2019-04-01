
import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			System.out.println("Philosopher " + getTID() + " has started eating");
			yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			if(Math.round(Math.random()) == 1)
			{
				DiningPhilosophers.soMonitor.requestPepper(getTID());
				yield();
				usePepper();
				yield();
				DiningPhilosophers.soMonitor.endPepper(getTID());
			}
			yield();
			System.out.println("Philosopher " + getTID() + " has finished eating");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		try
		{
			System.out.println("Philosopher " + getTID() + " has started thinking");
			yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Philosopher " + getTID() + " has finished thinking");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}
	/**
	 * The act of sleeping.
	 * - Print the fact that a given phil (their TID) has started sleeping.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done talking.
	 */
	public void sleep()
	{
		System.out.println("Philosopher " + getTID() + " has started sleeping");
		yield();
		try {
			sleep((long)(Math.random() * TIME_TO_WASTE));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		yield();
		System.out.println("Philosopher " + getTID() + " has finished sleeping");
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		System.out.println("Philosopher " + getTID() + " has started talking");
		yield();
		saySomething();
		yield();
		System.out.println("Philosopher " + getTID() + " has finished talking");
	}
	
	/**
	 * task 6
	 */
	public void usePepper()
	{
		System.out.println("Philosopher " + getTID() + " has started using pepper");
		yield();
		try {
			sleep((long)(Math.random() * TIME_TO_WASTE));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		yield();
		System.out.println("Philosopher " + getTID() + " has finished using pepper");
		
	}
	
	

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			DiningPhilosophers.soMonitor.pickUp(getTID());

			eat();

			DiningPhilosophers.soMonitor.putDown(getTID());

			think();

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			// if the rounding of a random number between 0 and 1 is equal to 1, then talk (~50% chance)
			if(Math.round(Math.random()) == 1)
			{
				DiningPhilosophers.soMonitor.requestTalk(getTID());
				yield();
				talk();
				yield();
				DiningPhilosophers.soMonitor.endTalk(getTID());
			}
			
			// if the rounding of a random number is less than 0.2, then sleep (~20% chance)
			if(Math.round(Math.random()) < 0.2)
			{
				DiningPhilosophers.soMonitor.requestSleep(getTID());
				yield();
				sleep();
				yield();
				DiningPhilosophers.soMonitor.endSleep(getTID());
			}
		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	
		for(int i=0;i<9999999;i++)
		{
			double random = Math.random();
			double k = 999999 / random;
		}
			
	}
	
	
}

// EOF
