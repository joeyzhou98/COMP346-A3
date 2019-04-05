package Task6;


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
	enum State {THINKING, HUNGRY, EATING, SLEEPING, TALKING}
	private State state[]; //state of each philosopher
	private boolean someoneTalking; //flag on whether a philosopher is talking
	private int pepperShakers = 2;//number of available pepperShakers

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
	public synchronized void requestTalk(final int piTID)
	{
		try
		{
			while(true) //infinite loop
			{
				//only if there is no one talking and the person requesting to talk isn't eating or sleeping
				if (!someoneTalking && state[piTID - 1] != State.EATING && state[piTID - 1] != State.SLEEPING)
				{
					state[piTID - 1] = State.TALKING; //set state to talking
					someoneTalking = true; //set talking flag to true
					notifyAll();
					break; //exit
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
	public synchronized void endTalk(final int piTID)
	{
		state[piTID - 1] = State.THINKING; //set state to thinking
		someoneTalking = false; //set flag to false
		notifyAll(); //allow all threads to try to request talk
		return; //exit
	}

	/**
	 * philosopher cannot sleep while other are talking
	 * 
	 */
	public synchronized void requestSleep(final int piTID)
	{
		try
		{
			while(true) //infinite loop
			{
				if (!someoneTalking) //only if someone isn't talking, he can sleep
				{
					state[piTID - 1] = State.SLEEPING;
					notifyAll();
					break; //exit
				}
				else
				{
					//System.out.println("Someone is talking, " + (piTID -1)+ " philosopher cannot sleep.");
					wait(); //else wait for someone to finish talking
				}
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void endSleep(final int piTID)
	{
		state[piTID - 1] = State.THINKING;
	}
	/**
	 * task 6
	 * @param piTID
	 */
	public synchronized void requestPepper(final int piTID)
	{
		int index = piTID - 1;
		while(true) //infinite loop
		{
			try {
			if (state[index] == State.EATING && pepperShakers > 0) //only if he is eating and there is a shaker available
			{
				pepperShakers--; //decrement number of pepper shakers
				notifyAll();
				return; //exit
			}
			else
			{
				System.out.println("No pepper shaker is available, philsopher " + piTID + " has to wait.");				
				wait();
			}
			} 
			catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //else wait for someone to finish talking
			
		}
	}
	public synchronized void endPepper(final int piTID)
	{
		pepperShakers++; //increment number of pepper shakers
		notifyAll();
	}
	
}

// EOF
