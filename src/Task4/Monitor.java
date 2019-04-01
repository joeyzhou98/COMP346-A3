package Task4;

import java.util.ArrayList;
import java.util.Collections;

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
	enum State {THINKING, HUNGRY, EATING, SLEEPING, TALKING, WAITING}
	private State state[]; //state of each philosopher
	private int priorities[]; //priority of each philosopher where index + 1 is the philosopher ID and priorities[index]
	//is that philosopher's priority where 1 is the highest priority
	private boolean someoneTalking; //flag on whether a philosopher is talking

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		n = piNumberOfPhilosophers; //set n to the number of philosophers
		state = new State[n]; //allocate n State memory space
		priorities = new int[n]; //allocate n integer memory space
		ArrayList<Integer> list = new ArrayList<Integer>(); //declare temporary list
		for (int i = 0; i < n; i++)
		{
			state[i] = State.THINKING; //initialize each philosopher to THINKING
			list.add(i + 1); //set values of list to be index + 1
		}
		someoneTalking = false; //initialize someone talking flag to false
		Collections.shuffle(list); //shuffle list
		System.out.println("Priorities: "); //print out priority array
		for (int i = 0; i < n; i++)
		{
			priorities[i] = list.get(i); //copy over the values from list to priorities array
			System.out.print(list.get(i) + " "); //print out priority number
		}
		System.out.println(); //return line
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */
	public synchronized boolean check(final int piTID) //check function for priority for philosopher with number id
	{
		int index = piTID - 1; //get index
		for (int i = 0; i < n; i++) //loop through priorities array
		{
			//if the state of a philosopher is hungry and his priority is higher,
			//modified to lower!!!
			if (state[i] == State.HUNGRY && priorities[i] < priorities[index])
			{
				//then print out that philosopher piTID tired to eat with their priorities
				System.out.println("Philosopher " + piTID + " tried to eat, with priority " + priorities[index] +
								" but philosopher " + (i + 1) + " has higher priority: " + priorities[i]);
				return false; //return false
			}
		}
		return true; //if no philosopher is hungry and have higher priority then let that philosopher eat
	}
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
						state[index] != State.EATING &&
						check(piTID)) //add check for priority
				{
					state[index] = State.EATING; //set state to eating
					//do not need notifyall here????
					//notifyAll(); //allow waiting threads to try to eat
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
		return; //exit
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 * 
	 */
	public synchronized void requestTalk(final int piTID)
	{
		try
		{
			///false? out of array
		state[piTID-1] = State.HUNGRY;
		
			while(true) //infinite loop
			{
				if (!someoneTalking&&state[piTID-1]!= State.EATING) //only if someone isn't talking
				{
					state[piTID-1]=State.TALKING;
					someoneTalking = true; //set talking flag to true
					
					break; //exit
				}
				else
				{
					state[piTID-1]=State.WAITING;
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
		state[piTID-1]=State.THINKING;
		someoneTalking = false; //set flag to false
		notifyAll(); //allow all threads to try to request talk
	}
	

	/**
	 * philopher cannot sleep while other are talking
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
					state[piTID-1]=State.SLEEPING;
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
	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endSleep(final int piTID)
	{
		state[piTID-1]=State.THINKING;
	}
	
}

// EOF
