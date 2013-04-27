/**
  * Game state class.<br /><br />
  *
  * Describes a single game state. A game of Numerate must have one of these
  * going on at all times. Is usually an active game, or a menu.
  */

public abstract class GameState
{
	/** Mouse x-position */
	protected int mouseX = -1;
	/** Mouse y-position */
	protected int mouseY = -1;
	
	/** The "next state", which is used by the game itself to go from state to state. */
	public GameState nextState = null;
	
	/** The game state's scores list */
	protected ScoresList scores;
	
	/** Mouse is being dragged from one position to the other */
	public void mouseDragged(int x, int y, boolean right)
	{
		mouseX = x;
		mouseY = y;
	}
	
	/** Mouse is being moved from one position to the other. */
	public void mouseMoved(int x, int y)
	{
		mouseX = x;
		mouseY = y;
		
	}
	
	/** Mouse is being clicked. */
	public void mouseClicked(boolean right) 
	{
	}
	
	/** Mouse is entering the main component. */
	public void mouseEntered(int x, int y) 
	{
		mouseX = x;
		mouseY = y;
	}
	
	/** Mouse is leaving the main component. */
	public void mouseExited() 
	{
		// mouse exited the component, so reset the x/y components
		mouseX = -1;
		mouseY = -1;
	}
	
	/** Mouse is being pressed. */
	public void mousePressed(boolean right) 
	{
	}
	
	/** Mouse is being released. */
	public void mouseReleased(boolean right) 
	{
	}
	
	/** Receive a key press. */
	public void keyPressed(int keyCode)
	{
	}
	
	/** Receive a key release. */
	public void keyReleased(int keyCode)
	{
	}
	
	/** Receive a key type. */
	public void keyTyped(char c)
	{
	}
	
	/** Elapse game time. Should be done every cycle, not just every frame render. */
	public void elapseTime(double dtime)
	{
	}
	
	/** Render the game state. */
	public void draw(GameGraphics graphics)
	{
	}
}