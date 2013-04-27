/**
  * Timer class.<br /><br />
  *
  * Stores an in-game timer, which records time intervals and reports
  * when the timer is "triggered" by a certain time interval elapsing.
  * Useful for timing events without using multithreading.
  *
  * @author Matthew Kloster
  * @version 1.0.0
  */

public class Timer
{
	/** Named id for the timer. */
	private String id;
	
	/** Period of execution */
	private double period;
	/** Current set period - when this is greater than or equal to period, this resets */
	private double currentPeriod;
	
	/** Max execution count */
	private int maxExecCount;
	/** Current execution count */
	private int execCount;
	
	/** Is this timer active? */
	private boolean isActive;
	
	/** Is this timer triggered? If set to true, timer will ignore the timer's actual period and simply think it's been triggered. */
	private boolean isTriggered;
	
	/**
	  * Default constructor. Contains all timer options.
	  * @param d_id Named ID for this timer.
	  * @param d_period Execution period for this timer.
	  * @param d_maxExecCount Max execution count for this timer.
	  * @param d_isActive <tt>true</tt> if the timer is active, <tt>false</tt> if the timer is inactive.
	  */
	public Timer(String d_id, double d_period, int d_maxExecCount, boolean d_isActive)
	{
		id = d_id;
		
		period = d_period;
		currentPeriod = 0;
		
		maxExecCount = d_maxExecCount;
		execCount = 0;
		
		isActive = d_isActive;
		
		isTriggered = false;
	}
	
	/**
	  * Constructor which only includes options for period and max execution count. Set to be active by default.
	  * @param d_id Named ID for this timer.
	  * @param d_period Execution period for this timer.
	  * @param d_maxExecCount Max execution count for this timer.
	  */
	public Timer(String d_id, double d_period, int d_maxExecCount)
	{
		this(d_id, d_period, d_maxExecCount,true);
	}
	
	/**
	  * Constructor which only includes options for period. Set to be active and infinitely executing by default.
	  * @param d_id Named ID for this timer.
	  * @param d_period Execution period for this timer.
	  */
	public Timer(String d_id, double d_period)
	{
		this (d_id, d_period, 0, true);
	}
	
	/** Accessor method for this timer's ID. */
	public String getId()
	{
		return new String(id);
	}
	
	/** Accessor method for this timer's period. */
	public double getPeriod()
	{
		return period;
	}
	
	/** Accessor method for this timer's current period. */
	public double getCurrentPeriod()
	{
		return currentPeriod;
	}
	
	/** Accessor method for this timer's maximum execution count. */
	public int getMaxExecCount()
	{
		return maxExecCount;
	}
	
	/** Accessor method for this timer's current execution count. */
	public int getExecCount()
	{
		return execCount;
	}
	
	/** Accessor method for this timer's active status. */
	public boolean isActive()
	{
		return isActive;
	}
	
	/** Mutator method for this timer's ID. */
	public void setId(String d_id)
	{
		id = d_id;
	}
	
	/** Mutator method for this timer's period. */
	public void setPeriod(double d_period)
	{
		period = d_period;
	}
	
	/** Mutator method for this timer's max execution count. */
	public void setMaxExecCount(int d_maxExecCount)
	{
		maxExecCount = d_maxExecCount;
		if (execCount >= maxExecCount && maxExecCount > 0)
		{
			// since we're over the max exec count, disable this timer
			currentPeriod = 0.0;
			isActive = false;
		}
	}
	
	/** Mutator method for this timer's current execution count. */
	public void setExecCount(int d_execCount)
	{
		execCount = d_execCount;
		if (execCount < 0)
		{
			// can't have an exec count of less than 0
			execCount = 0;
		}
		else if (execCount >= maxExecCount && maxExecCount > 0)
		{
			// since we're over the max exec count, disable this timer
			currentPeriod = 0.0;
			isActive = false;
		}
	}
	
	/** Mutator method for this timer's active status. */
	public void setIsActive(boolean d_isActive)
	{
		isActive = d_isActive;
	}
	
	/** Mutator method for this timer's triggered status. */
	public void setIsTriggered(boolean d_isTriggered)
	{
		isTriggered = d_isTriggered;
	}
	
	/** Increment a certain amount of time to the current period. Should be done periodically to keep timer up to date. */
	public void increment(double amount)
	{
		if (isActive) // only increment if our timer is current active
		{
			currentPeriod += amount;
		}
	}
	
	/** Has this timer been triggered? */
	public boolean triggered()
	{
		if ((isActive && currentPeriod >= period) || isTriggered)
		{
			isTriggered = false;
			execCount++;
			currentPeriod = Math.max(0.0,currentPeriod-period);
			if (execCount >= maxExecCount && maxExecCount > 0)
			{
				// since we're over the max exec count, disable this timer
				currentPeriod = 0.0;
				isActive = false;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/** Resets the timer (ie: the period and execution counts) */
	public void reset()
	{
		execCount = 0;
		currentPeriod = 0.0;
	}
	
}