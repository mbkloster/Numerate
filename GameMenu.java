import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
  * Game main menu class.<br /><br />
  *
  * Describes an instance of the game's main menu. This includes game, settings, etc. menus.
  */

public class GameMenu extends GameState
{
	/** Constant for main menu placement */
	public static final int MAIN_MENU = 0;
	/** Constant for how to play placement */
	public static final int HOW_TO_PLAY = 1;
	/** Constant for how to play placement */
	public static final int CREDITS = 2;
	/** Constant for quickplay mode select placement */
	public static final int QUICKPLAY_MODE_SELECT = 3;
	/** Constant for quickplay difficulty select placement */
	public static final int QUICKPLAY_DIFFICULTY_SELECT = 4;
	
	/** Constant for the padding before the logo (in px). */
	public static final int TOP_PADDING = 64;
	/** Constant for the padding below the logo (in px). */
	public static final int MENU_PADDING = 48;
	/** Constant for the padding between menu items (in px). */
	public static final int ITEM_PADDING = 32;
	/** Padding between credit lines (in px). */
	public static final int CREDIT_PADDING = 32;
	/** Padding on the right side of the credits screen (in px). */
	public static final int CREDIT_SIDE_PADDING = 10;
	/** How to play padding */
	public static final int HOW_TO_PLAY_PADDING = 16;
	/** How to play side padding */
	public static final int HOW_TO_PLAY_SIDE_PADDING = 10;
	
	/** Current submenu placement */
	private int placement;
	/** Current selected item */
	private int selectedItem;
	/** Text position (for scrolling text) */
	private double textPosition;
	
	/** Credit text velocity (in pixels/sec) */
	private final double creditTextVelocity = NumerateGame.WINDOW_X*1.25;
	/** Is the text currently moving? */
	private boolean textMoving = false;
	
	/** Game mode */
	private int gameMode = NumerateGame.NO_MODE;
	
	/** Does the screen need a complete redaw? */
	private boolean needsCompleteRedraw = true;
	
	/** Game graphics buffer. */
	GameGraphics graphics;
	
	/** Grab the selected item from the mouse's current position and set it */
	public void setSelectedItem()
	{
		int mouseY = this.mouseY;
		if (placement == MAIN_MENU)
		{
			mouseY -= (TOP_PADDING + NumerateGame.LOGO_FONT.getSize() + MENU_PADDING) - NumerateGame.MENU_FONT.getSize();
			if (mouseY >= 0 && mouseY <= (NumerateGame.MENU_FONT.getSize()+ITEM_PADDING)*(TextBank.MAIN_MENU_OPTIONS.length))
			{
				if (mouseY/(NumerateGame.MENU_FONT.getSize()+ITEM_PADDING) != selectedItem)
				{
					// our selected items differ... so redraw the screen
					needsCompleteRedraw = true;
				}
				selectedItem = mouseY/(NumerateGame.MENU_FONT.getSize()+ITEM_PADDING);
			}
			else
			{
				if (selectedItem != -1)
				{
					needsCompleteRedraw = true;
				}
				selectedItem = -1;
			}
		}
		else if (placement == QUICKPLAY_MODE_SELECT)
		{
			mouseY -= (TOP_PADDING + NumerateGame.LOGO_FONT.getSize() + MENU_PADDING) - NumerateGame.MENU_FONT.getSize();
			if (mouseY >= 0 && mouseY <= (NumerateGame.MENU_FONT.getSize()+ITEM_PADDING)*(TextBank.MODES.length+1))
			{
				if (mouseY/(NumerateGame.MENU_FONT.getSize()+ITEM_PADDING) != selectedItem)
				{
					// our selected items differ... so redraw the screen
					needsCompleteRedraw = true;
				}
				selectedItem = mouseY/(NumerateGame.MENU_FONT.getSize()+ITEM_PADDING);
			}
			else
			{
				if (selectedItem != -1)
				{
					needsCompleteRedraw = true;
				}
				selectedItem = -1;
			}
		}
		else if (placement == QUICKPLAY_DIFFICULTY_SELECT)
		{
			mouseY -= (TOP_PADDING + NumerateGame.LOGO_FONT.getSize() + MENU_PADDING) - NumerateGame.MENU_FONT.getSize();
			if (mouseY >= 0 && mouseY <= (NumerateGame.MENU_FONT.getSize()+ITEM_PADDING)*(TextBank.MODES.length+1))
			{
				if (mouseY/(NumerateGame.MENU_FONT.getSize()+ITEM_PADDING) != selectedItem)
				{
					// our selected items differ... so redraw the screen
					needsCompleteRedraw = true;
				}
				selectedItem = mouseY/(NumerateGame.MENU_FONT.getSize()+ITEM_PADDING);
			}
			else
			{
				if (selectedItem != -1)
				{
					needsCompleteRedraw = true;
				}
				selectedItem = -1;
			}
		}
	}
	
	/** Get the max number of selected items in the menu  for this particular placement */
	private int maxItems()
	{
		if (placement == MAIN_MENU)
		{
			return TextBank.MAIN_MENU_OPTIONS.length;
		}
		else if (placement == QUICKPLAY_MODE_SELECT)
		{
			return TextBank.MODES.length + 1;
		}
		else if (placement == QUICKPLAY_DIFFICULTY_SELECT)
		{
			return TextBank.DIFFICULTIES.length + 1;
		}
		
		return 0;
	}
	
	/** Scroll the selection down */
	private void scrollDown(int amount)
	{
		if (selectedItem == -1)
		{
			selectedItem = 0;
		}
		else
		{
			selectedItem += amount;
			while (selectedItem >= maxItems())
			{
				selectedItem -= maxItems();
			}
		}
		needsCompleteRedraw = true;
	}
	
	/** Scroll the selection up */
	private void scrollUp(int amount)
	{
		if (selectedItem == -1)
		{
			selectedItem = 0;
		}
		else
		{
			selectedItem -= amount;
			while (selectedItem < 0)
			{
				selectedItem += maxItems();
			}
		}
		needsCompleteRedraw = true;
	}
	
	/** "Select" something - usually triggered by mouse clicks */
	public void select(boolean keyboard)
	{
		if (placement == MAIN_MENU)
		{
			if (selectedItem == 0)
			{
				placement = QUICKPLAY_MODE_SELECT;
				if (graphics != null ) { graphics.setHelpBoxRefreshed(false); }
				needsCompleteRedraw = true;
				if (keyboard)
				{
					selectedItem = 0;
				}
			}
			else if (selectedItem == 5)
			{
				// show credits...
				placement = CREDITS;
				textMoving = true;
				textPosition = 0;
				needsCompleteRedraw = true;
			}
			else if (selectedItem == 6)
			{
				// quit out
				nextState = null;
			}
		}
		else if (placement == CREDITS && !textMoving)
		{
			placement = MAIN_MENU;
			setSelectedItem();
			if (graphics != null ) { graphics.setHelpBoxRefreshed(false); }
			needsCompleteRedraw = true;
			if (keyboard)
			{
				selectedItem = 5;
			}
		}
		else if (placement == QUICKPLAY_MODE_SELECT)
		{
			if (selectedItem >= TextBank.MODES.length)
			{
				placement = MAIN_MENU;
				setSelectedItem();
				if (graphics != null ) { graphics.setHelpBoxRefreshed(false); }
				needsCompleteRedraw = true;
				if (keyboard)
				{
					selectedItem = 0;
				}
			}
			else if (selectedItem >= 0)
			{
				placement = QUICKPLAY_DIFFICULTY_SELECT;
				gameMode = selectedItem;
				setSelectedItem();
				if (graphics != null ) { graphics.setHelpBoxRefreshed(false); }
				needsCompleteRedraw = true;
				if (keyboard)
				{
					selectedItem = 0;
				}
			}
			
		}
		else if (placement == QUICKPLAY_DIFFICULTY_SELECT)
		{
			if (selectedItem >= TextBank.DIFFICULTIES.length)
			{
				placement = QUICKPLAY_MODE_SELECT;
				setSelectedItem();
				if (graphics != null ) { if (graphics != null ) { graphics.setHelpBoxRefreshed(false); } }
				needsCompleteRedraw = true;
				if (keyboard)
				{
					selectedItem = gameMode;
				}
			}
			else if (selectedItem >= 0)
			{
				nextState = new Game(scores,gameMode,selectedItem*2+4, null);
				graphics.setHelpBoxRefreshed(false);
				needsCompleteRedraw = true;
			}
		}
	}
	
	/** "Deselect" something - usually triggered by mouse right clicks */
	private void deselect(boolean keyboard)
	{
		
		if (placement == CREDITS && !textMoving)
		{
			placement = MAIN_MENU;
			selectedItem = 5;
			if (graphics != null ) { if (graphics != null ) { graphics.setHelpBoxRefreshed(false); } }
			needsCompleteRedraw = true;
		}
		else if (placement == QUICKPLAY_MODE_SELECT)
		{
			placement = MAIN_MENU;
			selectedItem = 0;
			if (graphics != null ) { if (graphics != null ) { graphics.setHelpBoxRefreshed(false); } }
			needsCompleteRedraw = true;
		}
		else if (placement == QUICKPLAY_DIFFICULTY_SELECT)
		{
			placement = QUICKPLAY_MODE_SELECT;
			selectedItem = gameMode;
			if (graphics != null ) { if (graphics != null ) { graphics.setHelpBoxRefreshed(false); } }
			needsCompleteRedraw = true;
		}
		
		if (!keyboard)
		{
			setSelectedItem();
		}
		
	}
	
	/** Pass game time on the menu. */
	public void elapseTime(double dtime)
	{
		textPosition = Math.min((double)NumerateGame.WINDOW_X-CREDIT_SIDE_PADDING, textPosition + dtime*creditTextVelocity);
	}
	
	/** Render the game menu. */
	public void draw(GameGraphics graphics)
	{
		if (this.graphics == null)
		{
			this.graphics = graphics;
		}
		graphics.needsCompleteRedraw = graphics.needsCompleteRedraw || needsCompleteRedraw;
		graphics.redraw();
		needsCompleteRedraw = false;
		graphics.g.setColor(NumerateGame.MENU_BG_COLOR);
		graphics.g.fillRect(0,0,NumerateGame.WINDOW_X,NumerateGame.WINDOW_Y);
		
		graphics.g.setColor(NumerateGame.MENU_TEXT_COLOR);
		if (placement == MAIN_MENU)
		{
			if (!graphics.isHelpBoxRefreshed())
			{
				graphics.clearHelpLines();
				graphics.addHelpLine(TextBank.HELP_MENU_SCROLL);
				graphics.addHelpLine(TextBank.HELP_MENU_SELECT);
				graphics.updateHelpBoxDimensions();
				graphics.setHelpBoxRefreshed(true);
			}
			graphics.drawHelpBox();
			
			int textCursor = TOP_PADDING;
			graphics.g.setFont(NumerateGame.LOGO_FONT);
			graphics.g.drawString("NUMERATE",NumerateGame.WINDOW_X/2 - graphics.g.getFontMetrics().stringWidth("NUMERATE")/2,textCursor);
			
			textCursor += NumerateGame.LOGO_FONT.getSize()+MENU_PADDING;
			graphics.g.setFont(NumerateGame.MENU_FONT);
			for (int i = 0; i < TextBank.MAIN_MENU_OPTIONS.length; i++)
			{
				if (i == selectedItem)
				{
					graphics.g.setColor(NumerateGame.SELECTED_MENU_TEXT_COLOR);
					graphics.drawCaption(TextBank.MAIN_MENU_CAPTIONS[i]);
				}
				graphics.g.drawString(TextBank.MAIN_MENU_OPTIONS[i],NumerateGame.WINDOW_X/2 - graphics.g.getFontMetrics().stringWidth(TextBank.MAIN_MENU_OPTIONS[i])/2,textCursor);
				if (i == selectedItem)
				{
					graphics.g.setColor(NumerateGame.MENU_TEXT_COLOR);
				}
				textCursor += NumerateGame.MENU_FONT.getSize()+ITEM_PADDING;
			}
		}
		else if (placement == CREDITS)
		{
			int textCursor = CREDIT_PADDING;
			
			graphics.g.setColor(NumerateGame.MENU_BG_COLOR);
			graphics.g.fillRect(0,0,NumerateGame.WINDOW_X,NumerateGame.WINDOW_Y);
			graphics.g.setFont(NumerateGame.CREDITS_FONT);
			graphics.g.setColor(NumerateGame.MENU_TEXT_COLOR);
			
			if (textMoving)
			{
				if (textPosition >= NumerateGame.WINDOW_X-CREDIT_SIDE_PADDING)
				{
					textPosition = NumerateGame.WINDOW_X-CREDIT_SIDE_PADDING;
					graphics.setHelpBoxRefreshed(false);
					textMoving = false;
				}
				needsCompleteRedraw = true;
			}
			else
			{
				if (!graphics.isHelpBoxRefreshed())
				{
					graphics.clearHelpLines();
					graphics.addHelpLine(TextBank.HELP_MENU_BACK);
					graphics.updateHelpBoxDimensions();
					graphics.setHelpBoxRefreshed(true);
				}
				
				graphics.drawHelpBox();
				//graphics.g.drawString(TextBank.CREDITS_RETURN,CREDIT_SIDE_PADDING,NumerateGame.WINDOW_Y-CREDIT_PADDING);
			}
			
			graphics.g.drawString(NumerateGame.GAME_TITLE+" v"+NumerateGame.GAME_VERSION,(int)textPosition-graphics.g.getFontMetrics().stringWidth(NumerateGame.GAME_TITLE+" v"+NumerateGame.GAME_VERSION),textCursor);
			textCursor += CREDIT_PADDING + NumerateGame.CREDITS_FONT.getSize();
			for (int i = 0; i < TextBank.CREDITS_LINES.length; i++)
			{
				graphics.g.drawString(TextBank.CREDITS_LINES[i],(int)textPosition-graphics.g.getFontMetrics().stringWidth(TextBank.CREDITS_LINES[i]),textCursor);
				textCursor += CREDIT_PADDING + NumerateGame.CREDITS_FONT.getSize();
			}
		}
		else if (placement == QUICKPLAY_MODE_SELECT)
		{
			if (!graphics.isHelpBoxRefreshed())
			{
				graphics.clearHelpLines();
				graphics.addHelpLine(TextBank.HELP_MENU_SCROLL);
				graphics.addHelpLine(TextBank.HELP_MENU_SELECT);
				graphics.addHelpLine(TextBank.HELP_MENU_BACK);
				graphics.updateHelpBoxDimensions();
				graphics.setHelpBoxRefreshed(true);
			}
			graphics.drawHelpBox();
			
			int textCursor = TOP_PADDING;
			graphics.g.setFont(NumerateGame.HEADING_FONT);
			graphics.g.setColor(NumerateGame.MENU_TEXT_COLOR);
			graphics.g.drawString(TextBank.SELECT_GAME_MODE,NumerateGame.WINDOW_X/2-graphics.g.getFontMetrics().stringWidth(TextBank.SELECT_GAME_MODE)/2,textCursor);
			textCursor += graphics.g.getFont().getSize() + MENU_PADDING;
			
			graphics.g.setFont(NumerateGame.MENU_FONT);
			
			for (int i = 0; i < TextBank.MODES.length; i++)
			{
				if (selectedItem == i)
				{
					graphics.drawCaption(TextBank.MODE_DESCRIPTIONS[i]);
					graphics.g.setColor(NumerateGame.SELECTED_MENU_TEXT_COLOR);
				}
				graphics.g.drawString(TextBank.MODES[i],NumerateGame.WINDOW_X/2-graphics.g.getFontMetrics().stringWidth(TextBank.MODES[i])/2,textCursor);
				if (selectedItem == i)
				{
					graphics.g.setColor(NumerateGame.MENU_TEXT_COLOR);
				}
				textCursor += graphics.g.getFont().getSize() + ITEM_PADDING;
			}
			if (selectedItem == TextBank.MODES.length)
			{
				graphics.drawCaption(TextBank.BACK_TO_MENU_CAPTION);
				graphics.g.setColor(NumerateGame.SELECTED_MENU_TEXT_COLOR);
			}
			graphics.g.drawString(TextBank.BACK_TO_MENU,NumerateGame.WINDOW_X/2-graphics.g.getFontMetrics().stringWidth(TextBank.BACK_TO_MENU)/2,textCursor);
		}
		else if (placement == QUICKPLAY_DIFFICULTY_SELECT)
		{
			if (!graphics.isHelpBoxRefreshed())
			{
				graphics.clearHelpLines();
				graphics.addHelpLine(TextBank.HELP_MENU_SCROLL);
				graphics.addHelpLine(TextBank.HELP_MENU_SELECT);
				graphics.addHelpLine(TextBank.HELP_MENU_BACK);
				graphics.updateHelpBoxDimensions();
				graphics.setHelpBoxRefreshed(true);
			}
			graphics.drawHelpBox();
			
			int textCursor = TOP_PADDING;
			graphics.g.setFont(NumerateGame.HEADING_FONT);
			graphics.g.setColor(NumerateGame.MENU_TEXT_COLOR);
			graphics.g.drawString(TextBank.SELECT_DIFFICULTY,NumerateGame.WINDOW_X/2-graphics.g.getFontMetrics().stringWidth(TextBank.SELECT_DIFFICULTY)/2,textCursor);
			textCursor += graphics.g.getFont().getSize() + MENU_PADDING;
			
			graphics.g.setFont(NumerateGame.MENU_FONT);
			
			for (int i = 0; i < TextBank.DIFFICULTIES.length; i++)
			{
				if (selectedItem == i)
				{
					graphics.g.setColor(NumerateGame.SELECTED_MENU_TEXT_COLOR);
				}
				graphics.g.drawString(TextBank.DIFFICULTIES[i],NumerateGame.WINDOW_X/2-graphics.g.getFontMetrics().stringWidth(TextBank.DIFFICULTIES[i])/2,textCursor);
				if (selectedItem == i)
				{
					graphics.g.setColor(NumerateGame.MENU_TEXT_COLOR);
				}
				textCursor += graphics.g.getFont().getSize() + ITEM_PADDING;
			}
			if (selectedItem == TextBank.MODES.length)
			{
				graphics.drawCaption(TextBank.BACK_TO_MODE_SELECT_CAPTION);
				graphics.g.setColor(NumerateGame.SELECTED_MENU_TEXT_COLOR);
			}
			graphics.g.drawString(TextBank.BACK_TO_MODE_SELECT,NumerateGame.WINDOW_X/2-graphics.g.getFontMetrics().stringWidth(TextBank.BACK_TO_MODE_SELECT)/2,textCursor);
		}
		
	}
	
	/** Mouse moved - update selected item? */
	public void mouseMoved(int x, int y)
	{
		super.mouseMoved(x,y);
		setSelectedItem();
	}
	/** Mouse dragged - let's find out what to do! */
	public void mouseDragged(int x, int y, boolean right) 
	{
		super.mouseDragged(x,y,right);
	}
	
	/** Mouse clicked - let's find out what to do! */
	public void mousePressed(boolean right) 
	{
		if (!right)
		{
			select(false);
		}
		else
		{
			deselect(false);
		}
	}
	
	/** Receive a key press. */
	public void keyPressed(int keyCode)
	{
		if (keyCode == KeyEvent.VK_DOWN)
		{
			scrollDown(1);
		}
		else if (keyCode == KeyEvent.VK_UP)
		{
			scrollUp(1);
		}
		else if (keyCode == KeyEvent.VK_ENTER)
		{
			select(true);
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			deselect(true);
		}
	}
	
	/** Receive a key release. */
	public void keyReleased(int keyCode)
	{
	}
	
	/** Receive a key type. */
	public void keyTyped(char c)
	{
	}
	
	/** Standard constructor. */
	public GameMenu(ScoresList d_scores, int d_placement)
	{
		scores = d_scores;
		placement = d_placement;
		selectedItem = -1;
		nextState = this;
		graphics = null;
	}
	
	/** Placement-less constructor -- defaults to the main menu */
	public GameMenu(ScoresList d_scores)
	{
		this(d_scores, MAIN_MENU);
	}
}