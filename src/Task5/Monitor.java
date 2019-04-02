package Task5;

import java.util.ArrayList;

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
	enum State {THINKING, HUNGRY, EATING, TALKING}
	private DoublyLinkedListImpl list; //state of each philosopher
	private ArrayList<Integer> deadList;
	private boolean someoneTalking; //flag on whether a philosopher is talking

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		n = piNumberOfPhilosophers; //set n to the number of philosophers
		list = new DoublyLinkedListImpl();
		for (int i = 0; i < n; i++)
		{
			list.addLast(i + 1, State.THINKING);
		}
		someoneTalking = false; //initialize someone talking flag to false
		deadList = new ArrayList<>();
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
		if (in(piTID, deadList)) {return;}

		try
		{
			DoublyLinkedListImpl.Node target = list.find(piTID);
			target.state = State.HUNGRY; //set state to hungry
			while(true) //infinite loop
			{
				//only if philosopher neighbours aren't eating (meaning both chopsticks are free)
				//and that he isn't currently eating, then he can proceed
				if (target.next().state != State.EATING &&
						target.prev().state != State.EATING &&
						target.state != State.EATING)
				{
					target.state = State.EATING; //set state to eating
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
		if (in(piTID, deadList)) {return;}

		DoublyLinkedListImpl.Node target = list.find(piTID);
		target.state = State.THINKING; //set state to thinking
		notifyAll(); //wake threads and exit
		return;
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk(final int piTID)
	{
		if (in(piTID, deadList)) {return;}

		try
		{
			while(true) //infinite loop
			{
				if (!someoneTalking) //only if someone isn't talking
				{
					list.find(piTID).state = State.TALKING;
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
	public synchronized void endTalk(final int piTID)
	{
		if (in(piTID, deadList)) {return;}

		list.find(piTID).state = State.THINKING;
		someoneTalking = false; //set flag to false
		notifyAll(); //allow all threads to try to request talk
		return; //exit
	}

	public synchronized void requestRemove(final int piTID)
	{
		if (in(piTID, deadList)) {return;}

		try
		{
			DoublyLinkedListImpl.Node target = list.find(piTID);
			while (true) //infinite loop
			{
				if (target.next().state != State.EATING ||
								target.prev().state != State.EATING ||
								target.state != State.TALKING)
				{
					list.remove(piTID);
					n--;
					deadList.add(piTID);
					notifyAll();
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

	public synchronized void requestAdd(final int piTID)
	{
		if (in(piTID, deadList)) {return;}

		list.addLast(piTID, State.THINKING);
		n++;
		notifyAll();
	}

	public synchronized boolean in(int id, ArrayList<Integer> deadList)
	{
		for (int i = 0; i < deadList.size(); i++)
		{
			if (id == deadList.get(i))
			{
				return true;
			}
		}
		return false;
	}
}

// EOF
