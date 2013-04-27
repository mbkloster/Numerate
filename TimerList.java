import java.util.ArrayList;

/**
  * Timer list class.<br /><br />
  *
  * Stores a list of timers, categorized by branch number and
  * index number. Contains functions to collectively increment,
  * reset and adjust timers within the list.
  *
  * @author Matthew Kloster
  * @version 1.0.0
  */

public class TimerList
{
	/** Initial capacity for branch list. */
	private final int BRANCH_LIST_SIZE = 4;
	
	/** Initial capacity for timer list within branches. */
	private final int TIMER_LIST_SIZE = 6;
	
	/** List of timers/branches. */
	private ArrayList<ArrayList<Timer>> branches;
	
	/**
	  * Default constructor. Creates a list with a certain number of branches.
	  * @param branchCount The number of branches to start with.
	  */
	public TimerList(int branchCount)
	{
		branches = new ArrayList<ArrayList<Timer>>(BRANCH_LIST_SIZE);
		
		// now create the initial branches...
		for (int i = 0; i < branchCount; i++)
		{
			branches.add(new ArrayList<Timer>(TIMER_LIST_SIZE));
		}
	}
	
	/**
	  * Branch count free constructor. Creates a list with 0 branches by default.
	  */
	public TimerList()
	{
		this(0);
	}
	
	/** Counts the number of branches in the timer list. */
	public int branchCount()
	{
		return branches.size();
	}
	
	/** Counts the number of timers in the timer list. */
	public int timerCount()
	{
		int count = 0;
		for (int i = 0; i < branches.size(); i++)
		{
			count += branches.get(i).size();
		}
		return count;
	}
	
	/** Counts the number of timers from a specific branch. */
	public int timerCount(int branchNumber)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			return branches.get(branchNumber).size();
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Adds new branches. */
	public void addBranches(int branchCount)
	{
		for (int i = 0; i < branchCount; i++)
		{
			branches.add(new ArrayList<Timer>());
		}
	}
	
	/** Adds a single new branch. */
	public void addBranch()
	{
		addBranches(1);
	}
	
	/** Adds a new timer, or replaces an existing one. */
	public void addTimer(int branchNumber, int timerNumber, Timer timer)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			branches.get(branchNumber).add(timerNumber,timer);
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Gets a specific timer's id. */
	public String getId(int branchNumber, int timerNumber)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				return branch.get(timerNumber).getId();
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Gets the period of a specific timer in the list. */
	public double getPeriod(int branchNumber, int timerNumber)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				return branch.get(timerNumber).getPeriod();
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Gets the maximum execution count of a specific timer in the list. */
	public int getMaxExecCount(int branchNumber, int timerNumber)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				return branch.get(timerNumber).getMaxExecCount();
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Gets the execution count of a specific timer in the list. */
	public int getExecCount(int branchNumber, int timerNumber)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				return branch.get(timerNumber).getExecCount();
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Checks if there are any active timers in the list. */
	public boolean isActive()
	{
		for (int i = 0; i < branches.size(); i++)
		{
			ArrayList<Timer> branch = branches.get(i);
			for (int j = 0; j < branch.size(); j++)
			{
				if (branch.get(j).isActive())
				{
					// we've found an active timer - return true
					return true;
				}
			}
		}
		
		// no active timers found - return false
		return false;
	}
	
	/** Checks if there are any active timers in a specific branch. */
	public boolean isActive(int branchNumber)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			for (int j = 0; j < branch.size(); j++)
			{
				if (branch.get(j).isActive())
				{
					// we've found an active timer - return true
					return true;
				}
			}
			
			// no active timers found - return false
			return false;
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Checks if a specific timer is active. */
	public boolean isActive(int branchNumber, int timerNumber)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				return branch.get(timerNumber).isActive();
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Sets a timer's id */
	public void setId(int branchNumber, int timerNumber, String id)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				branch.get(timerNumber).setId(id);
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Sets a timer's period */
	public void setPeriod(int branchNumber, int timerNumber, double period)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				branch.get(timerNumber).setPeriod(period);
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Sets a timer's max execution count */
	public void setMaxExecCount(int branchNumber, int timerNumber, int maxExecCount)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				branch.get(timerNumber).setMaxExecCount(maxExecCount);
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Sets a timer's execution count */
	public void setExecCount(int branchNumber, int timerNumber, int execCount)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				branch.get(timerNumber).setExecCount(execCount);
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Changes the active status of every timer in the list. */
	public void setIsActive(boolean isActive)
	{
		for (int i = 0; i < branches.size(); i++)
		{
			ArrayList<Timer> branch = branches.get(i);
			for (int j = 0; j < branch.size(); j++)
			{
				branch.get(j).setIsActive(isActive);
			}
		}
	}
	
	/** Changes the active status of every timer in a specific branch. */
	public void setIsActive(int branchNumber, boolean isActive)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			for (int j = 0; j < branch.size(); j++)
			{
				branch.get(j).setIsActive(isActive);
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Changes the active status of a specific timer. */
	public void setIsActive(int branchNumber, int timerNumber, boolean isActive)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				branch.get(timerNumber).setIsActive(isActive);
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Changes the triggered status of every timer in the list. */
	public void setIsTriggered(boolean isTriggered)
	{
		for (int i = 0; i < branches.size(); i++)
		{
			ArrayList<Timer> branch = branches.get(i);
			for (int j = 0; j < branch.size(); j++)
			{
				branch.get(j).setIsTriggered(isTriggered);
			}
		}
	}
	
	/** Changes the triggered status of every timer in a specific branch. */
	public void setIsTriggered(int branchNumber, boolean isTriggered)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			for (int j = 0; j < branch.size(); j++)
			{
				branch.get(j).setIsTriggered(isTriggered);
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Changes the triggered status of a specific timer. */
	public void setIsTriggered(int branchNumber, int timerNumber, boolean isTriggered)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				branch.get(timerNumber).setIsTriggered(isTriggered);
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Increments all timers in the list. */
	public void increment(double amount)
	{
		for (int i = 0; i < branches.size(); i++)
		{
			ArrayList<Timer> branch = branches.get(i);
			for (int j = 0; j < branch.size(); j++)
			{
				branch.get(j).increment(amount);
			}
		}
	}
	
	/** Increments all timers in a specific branch. */
	public void increment(int branchNumber, double amount)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			for (int j = 0; j < branch.size(); j++)
			{
				branch.get(j).increment(amount);
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Increments a specific timer. */
	public void increment(int branchNumber, int timerNumber, double amount)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				branch.get(timerNumber).increment(amount);
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Checks if any timer in the list has been triggered. */
	public boolean triggered()
	{
		boolean triggered = false;
		
		for (int i = 0; i < branches.size(); i++)
		{
			ArrayList<Timer> branch = branches.get(i);
			for (int j = 0; j < branch.size(); j++)
			{
				if (branch.get(j).triggered())
				{
					triggered = true;
				}
			}
		}
		
		return triggered;
	}
	
	/** Checks if any timer in a specific branch has been triggered. */
	public boolean triggered(int branchNumber)
	{
		boolean triggered = false;
		
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			for (int j = 0; j < branch.size(); j++)
			{
				if (branch.get(j).triggered())
				{
					triggered = true;
				}
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
		
		return triggered;
	}
	
	/** Checks if any timer in a specific branch has been triggered. */
	public boolean triggered(int branchNumber, int timerNumber)
	{
		
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				return branch.get(timerNumber).triggered();
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Rests all timers in the list. */
	public void reset()
	{	
		for (int i = 0; i < branches.size(); i++)
		{
			ArrayList<Timer> branch = branches.get(i);
			for (int j = 0; j < branch.size(); j++)
			{
				branch.get(j).reset();
			}
		}
	}
	
	/** Resets all timers in a specific branch. */
	public void reset(int branchNumber)
	{
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			for (int j = 0; j < branch.size(); j++)
			{
				branch.get(j).reset();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Resets a specific timer. */
	public void reset(int branchNumber, int timerNumber)
	{
		
		if (branchNumber >= 0 && branchNumber < branches.size())
		{
			ArrayList<Timer> branch = branches.get(branchNumber);
			if (timerNumber >= 0 && timerNumber < branch.size())
			{
				branch.get(timerNumber).reset();
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
}