package Task2;

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	private int n;  //number of philosophers
	enum State {THINKING, HUNGRY, EATING}
	private State state[]; //state of each philosopher
	private boolean someoneTalking; //flag on whether a philosopher is talking

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		n = piNumberOfPhilosophers; //set n to the number of philosophers
		state = new State[n]; //allocate n State memory space
		for (int i = 0; i < n; i++)
		{
			state[i] = State.THINKING; //initialize each philosopher to THINKING
		}
		someoneTalking = false; //initialize someone talking flag to false
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */
	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		try
		{
			int index = piTID - 1; //index corresponding to our philosopher
			state[index] = State.HUNGRY; //set state to hungry
			while(true) //infinite loop
			{
				//only if philosopher neighbours aren't eating (meaning both chopsticks are free)
				//and that he isn't currently eating, then he can proceed
				if (state[(index + 1) % n] != State.EATING &&
						state[(index + n - 1) % n] != State.EATING &&
						state[index] != State.EATING)
				{
					state[index] = State.EATING; //set state to eating
					notifyAll(); //allow waiting threads to try to eat
					return; //exits
				}
				else
				{
					wait(); //else waits
				}
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		int index = piTID - 1; //index corresponding to our philosopher
		state[index] = State.THINKING; //set state to thinking
		notifyAll(); //wake threads and exit
		return;
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		try
		{
			while(true) //infinite loop
			{
				if (!someoneTalking) //only if someone isn't talking
				{
					someoneTalking = true; //set talking flag to true
					return; //exit
				}
				else
				{
					wait(); //else wait for someone to finish talking
				}
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		someoneTalking = false; //set flag to false
		notifyAll(); //allow all threads to try to request talk
		return; //exit
	}
}

// EOF
