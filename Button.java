import java.awt.Color;
import java.awt.Font;
/**
  * Button class<br /><br />
  *
  * This class represents an on-screen button, clickable (if activated)
  */
public class Button
{
	/** Constant for the height of a button. */
	private static final int BUTTON_HEIGHT = 24;
	/** Minimum side padding on both sides. */
	private static final int BUTTON_PADDING = 3;
	
	/** Duration of a button press, visual (in seconds) */
	private static final double BUTTON_PRESS_DURATION = 0.1;
	
	/** Is this button enabled? */
	private boolean enabled;
	/** Is this button highlighted? (eg: is the mouse over it?) */
	private boolean highlighted;
	
	/** Has the mouse been moved since the last update? */
	private boolean mouseMoved;
	
	/** Center X position of the button. */
	private int x;
	/** Top Y position of the button. */
	private int y;
	/** Expected width of the button. */
	private int width;
	/** Render width of the button. This will occasionally differ from the expected width if the expected width is too small. */
	private int renderWidth;
	
	/** Does the graphics screen need a complete redraw? */
	public boolean needsCompleteRedraw = false;
	
	/** Button text. */
	private String text;
	
	/** Button press timer. */
	private Timer buttonPressTimer;
	
	/** Standard constructor. */
	public Button(String d_text, int d_width, int d_x, int d_y, boolean d_enabled)
	{
		text = d_text;
		
		width = d_width;
		renderWidth = d_width;
		x = d_x;
		y = d_y;
		
		enabled = d_enabled;
		mouseMoved = false;
		
		buttonPressTimer = new Timer("button_press",BUTTON_PRESS_DURATION,1,false);
	}
	
	/** Re-adjust the render width of the button based on width vs. position */
	private void readjustWidth(GameGraphics graphics)
	{
		graphics.g.setFont(NumerateGame.BUTTON_FONT);
		if (graphics.g.getFontMetrics().stringWidth(text)+2*BUTTON_PADDING > width)
		{
			renderWidth = graphics.g.getFontMetrics().stringWidth(text)+2*BUTTON_PADDING;
		}
		else
		{
			renderWidth = width;
		}
	}
	
	/** Re-adjust the highlight after the mouse has been moved */
	private void readjustHighlight(int mouseX, int mouseY)
	{
		if (mouseX >= x-renderWidth/2 && mouseX <= x+renderWidth/2 && mouseY >= y && mouseY <= y + BUTTON_HEIGHT)
		{
			highlighted = true;
		}
		else
		{
			highlighted = false;
		}
	}
	
	/** Set the mouse to be moved. */
	public void mouseMoved(int mouseX, int mouseY)
	{
		mouseMoved = true;
		boolean highlightedNow = highlighted;
		readjustHighlight(mouseX, mouseY);
		if (highlightedNow != highlighted)
		{
			needsCompleteRedraw = true;
		}
	}
	
	/** Draw the button. */
	public void draw(GameGraphics graphics, int mouseX, int mouseY)
	{
		Font previousFont = graphics.g.getFont();
		Color previousColor = graphics.g.getColor();
		
		readjustWidth(graphics);
		if (mouseMoved)
		{
			readjustHighlight(mouseX, mouseY);
			mouseMoved = false;
		}

		// starting x point
		int x2 = x-renderWidth/2;
		graphics.g.setColor(NumerateGame.BUTTON_BORDER_COLOR);
		graphics.drawBorder(x2,y,x2+renderWidth,y+BUTTON_HEIGHT);
		
		if (buttonPressTimer.isActive())
		{
			graphics.g.setColor(NumerateGame.BUTTON_PRESSED_BACKGROUND_COLOR);
		}
		else
		{
			graphics.g.setColor(NumerateGame.BUTTON_BACKGROUND_COLOR);
		}
		graphics.g.fillRect(x2+1,y+1,renderWidth-1, BUTTON_HEIGHT-1);
		if (!enabled)
		{
			graphics.g.setColor(NumerateGame.BUTTON_UNENABLED_TEXT_COLOR);
		}
		else if (highlighted)
		{
			graphics.g.setColor(NumerateGame.BUTTON_HIGHLIGHTED_TEXT_COLOR);
		}
		else
		{
			graphics.g.setColor(NumerateGame.BUTTON_TEXT_COLOR);
		}
		graphics.g.setFont(NumerateGame.BUTTON_FONT);
		graphics.g.drawString(text,x-graphics.g.getFontMetrics().stringWidth(text)/2,y+BUTTON_HEIGHT/2+graphics.g.getFontMetrics().getHeight()/3);
		
		graphics.g.setFont(previousFont);
		graphics.g.setColor(previousColor);
	}
	
	/** Sets the button text. */
	public void setText(String d_text)
	{
		text = d_text;
	}
	
	/** Sets the preferred width. */
	public void setWidth(int d_width)
	{
		width = d_width;
	}
	
	/** Sets whether or not the button is enabled. */
	public void setEnabled(boolean d_enabled)
	{
		if (d_enabled != enabled)
		{
			needsCompleteRedraw = true;
		}
		enabled = d_enabled;
	}
	
	/** Accessor method for this button being enabled or not. */
	public boolean isEnabled()
	{
		return enabled;
	}
	
	/** Accessor method for this button being enabled or not. */
	public boolean isHighlighted()
	{
		return highlighted;
	}
	
	/** Detects if the button is being pressed. */
	public boolean isPressed()
	{
		return buttonPressTimer.isActive();
	}
	
	/** (Attempt to) Press a button down. */
	public void press()
	{
		if (!buttonPressTimer.isActive() && enabled && highlighted)
		{
			buttonPressTimer.reset();
			buttonPressTimer.setIsActive(true);
			needsCompleteRedraw = true;
		}
	}
	
	/** Elapse time. Used only for press durations. */
	public void elapseTime(double dtime)
	{
		buttonPressTimer.increment(dtime);
		if (buttonPressTimer.triggered()) { needsCompleteRedraw = true; } // we don't need to put anything else here, since the timer will automatically deactive
	}
	
}