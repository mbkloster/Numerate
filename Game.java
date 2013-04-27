import java.awt.event.KeyEvent;

/** Represents a single, simple active or replayed game. */
public class Game extends GameState
{
	/** The amount of time (in seconds) game waits before starting */
	private static final int GAME_START_DELAY = 4;
	/** The amount of time (in seconds) game waits after moving piece to change status */
	private static final int PIECE_MOVED_DELAY = 3;
	
	/** The padding between the grid and the countdown (in px). */
	private static final int COUNTDOWN_PADDING = 12;
	/** The padding between the countdown and the status bar (in px). */
	private static final int STATUS_PADDING = 12;
	/** Padding between the border of the counter and the heading text (in px). */
	private static final int COUNTER_TOP_PADDING = 7;
	/** Padding between the heading of the counter and the text (in px) */
	private static final int COUNTER_TEXT_PADDING = 16;
	
	/** x-position of time counter */
	private static final int TIME_COUNTER_X = 350;
	/** y-position of time counter */
	private static final int TIME_COUNTER_Y = 96;
	/** width of time counter */
	private static final int TIME_COUNTER_WIDTH = 118;
	/** height of time counter */
	private static final int TIME_COUNTER_HEIGHT = 40;
	/** x-position of move counter */
	private static final int MOVE_COUNTER_X = 480;
	/** y-position of move counter */
	private static final int MOVE_COUNTER_Y = 96;
	/** width of move counter */
	private static final int MOVE_COUNTER_WIDTH = 118;
	/** height of move counter */
	private static final int MOVE_COUNTER_HEIGHT = 40;
	
	/** Score y padding */
	private static final int SCORE_Y_PADDING = 16;
	/** Score x padding */
	private static final int SCORE_X_PADDING = 6;
	/** Victory x padding */
	private static final int VICTORY_X_PADDING = 10;
	/** Victory y padding */
	private static final int VICTORY_Y_PADDING = 10;
	/** Padding between victory lines. */
	private static final int VICTORY_LINE_PADDING = 4;
	
	/** Width of the status bar */
	private static final int STATUS_BAR_WIDTH = 440;
	/** Height of the status bar */
	private static final int STATUS_BAR_HEIGHT = 18;
	/** Status bar padding to the left side */
	private static final int STATUS_BAR_PADDING = 5;
	
	/** Width for buttons */
	private static final int BUTTON_WIDTH = 35;
	
	/** Digit width for turn/time counts */
	private static final int CHAR_WIDTH = 14;
	/** Minimum digits for minutes in the counter */
	private static final int MIN_MINUTE_DIGITS = 2;
	/** Maximum digits for minutes in the counter */
	private static final int MAX_MINUTE_DIGITS = 3;
	
	/** Starting scores, arranged by mode and then difficulty */
	private static final int STARTING_SCORES[][] = {
		// Starting line-up scores
		{50000,96000,145000,210000},{50000,96000,145000,210000},{50000,96000,145000,210000},{50000,96000,145000,210000}
	};
	
	/** The grid for this game. */
	private Grid grid;
	
	/** Are we victorious? If so, show the victory screen */
	private boolean victorious;
	/** If victorious, what place on the high scores? */
	private int scoresPlacement;
	
	/** Current score for this game. */
	private int score;
	
	/** The amount of moved used for this game. */
	private int moves;
	/** The amount of time used for this game. */
	private double time;
	/** An integer representation of the time. */
	private int intTime;
	/** A formatted string representation of the time. */
	private String strTime;
	
	/** Associated challenge - leave this as NULL to assume no challenge. */
	private GameChallenge challenge;
	
	/** Status bar text. This spot displays notifications like "Piece moved", "Select a piece", etc. */
	private String statusBar;
	/** The nature of the status bar text. -1 = negative, 0 = neutral, 1 = positive */
	private int statusBarNature;
	/** Default status bar text. Used when reverting from a timer. */
	private String defaultStatusBar;
	/** Default status bar nature. */
	private int defaultStatusBarNature;
	/** Countdown number - should be an integer representation of the current countdown to display on the screen */
	private int countdown;
	
	/** High scores list */
	private ScoresList scores;
	
	/** The undo button */
	private Button undoButton;
	/** The hint button */
	private Button hintButton;
	
	/** Timer for the game starting. */
	private Timer gameStartTimer;
	/** Timer for "Piece moved" text to disappear from status bar. */
	private Timer pieceMovedTimer;
	
	/** Needs complete redraw? */
	private boolean needsCompleteRedraw;
	/** Should the help box be refreshed? set this to true to redo the help box */
	private boolean helpBoxNeedsRefreshing;
	
	/** Standard constructor. */
	public Game(ScoresList d_scores, int mode, int difficulty, GameChallenge d_challenge)
	{
		scores = d_scores;
		int x = NumerateGame.GRID_X_PADDING,
		    y = NumerateGame.GRID_Y_PADDING;
		x += (NumerateGame.GRID_TILE_SIZE*(TextBank.DIFFICULTIES.length*2+2)-difficulty*NumerateGame.GRID_TILE_SIZE)/2;
		y += (NumerateGame.GRID_TILE_SIZE*(TextBank.DIFFICULTIES.length*2+2)-difficulty*NumerateGame.GRID_TILE_SIZE)/2;
		grid = new Grid(mode, difficulty, x, y, mouseX, mouseY);
		challenge = d_challenge;
		needsCompleteRedraw = true;
		
		if (mode >= 0 && (difficulty-4)/2 >= 0 && mode < STARTING_SCORES.length && (difficulty-4)/2 < STARTING_SCORES[mode].length)
		{
			score = STARTING_SCORES[mode][(difficulty-4)/2];
		}
		else
		{
			score = 0;
		}
		
		gameStartTimer = new Timer("game_start",GAME_START_DELAY,1,true);
		pieceMovedTimer = new Timer("piece_moved",PIECE_MOVED_DELAY,1,false);
		countdown = GAME_START_DELAY;
		
		time = 0;
		intTime = 0;
		strTime = "00:00";
		moves = 0;
		
		victorious = false;
		
		statusBar = TextBank.GAME_BEGINNING_WAIT;
		statusBarNature = 0;
		defaultStatusBar = TextBank.SELECT_PIECE_PROMPT;
		defaultStatusBarNature = 0;
		
		undoButton = new Button(TextBank.UNDO_BUTTON, BUTTON_WIDTH, NumerateGame.GRID_X_PADDING+BUTTON_WIDTH/2,grid.getY()+grid.getYSize()*NumerateGame.GRID_TILE_SIZE+1+COUNTDOWN_PADDING,false);
		hintButton = new Button(TextBank.HINT_BUTTON, BUTTON_WIDTH, NumerateGame.GRID_X_PADDING+1+(TextBank.DIFFICULTIES.length*2+2)*NumerateGame.GRID_TILE_SIZE,grid.getY()+grid.getYSize()*NumerateGame.GRID_TILE_SIZE+1+COUNTDOWN_PADDING,true);
		
		nextState = this;
		helpBoxNeedsRefreshing = true;
	}
	
	/** Draw the game. */
	public void draw(GameGraphics graphics)
	{
		graphics.needsCompleteRedraw = needsCompleteRedraw || grid.needsCompleteRedraw || undoButton.needsCompleteRedraw || hintButton.needsCompleteRedraw;
		graphics.redraw();
		needsCompleteRedraw = grid.needsCompleteRedraw = false;
		
		graphics.g.setColor(NumerateGame.GAME_BG_COLOR);
		graphics.g.fillRect(0,0,NumerateGame.WINDOW_X,NumerateGame.WINDOW_Y);
		
		grid.draw(graphics);
		int textCursor = grid.getY();
		textCursor += 2 + grid.getYSize()*NumerateGame.GRID_TILE_SIZE;
		
		graphics.setHelpBoxRefreshed(!helpBoxNeedsRefreshing);
		if (!graphics.isHelpBoxRefreshed())
		{
			graphics.clearHelpLines();
			if (grid.pieceSelected())
			{
				graphics.addHelpLine(TextBank.HELP_GAME_SCROLL);
				graphics.addHelpLine(TextBank.HELP_GAME_MOVE);
				graphics.addHelpLine(TextBank.HELP_MENU_DESELECT);
			}
			else
			{
				graphics.addHelpLine(TextBank.HELP_GAME_SCROLL);
				graphics.addHelpLine(TextBank.HELP_GAME_SELECT);
				graphics.addHelpLine(TextBank.HELP_GAME_UNDOREDO);
				graphics.addHelpLine(TextBank.HELP_GAME_HINT);
				graphics.addHelpLine(TextBank.HELP_MENU_QUIT);
			}
			graphics.updateHelpBoxDimensions();
			graphics.setHelpBoxRefreshed(true);
			helpBoxNeedsRefreshing = false;
		}
		graphics.drawHelpBox();
		
		graphics.g.setFont(NumerateGame.GAME_HEADING_FONT);
		graphics.g.setColor(NumerateGame.MENU_TEXT_COLOR);
		textCursor += COUNTDOWN_PADDING + graphics.g.getFont().getSize()/2;
		if (countdown > 0)
		{
			// draw the start counter
			graphics.g.drawString(Integer.toString(countdown)+"...",grid.getX(),textCursor);
		}
		else
		{
			// draw the undo/hint buttons
			undoButton.draw(graphics,mouseX,mouseY);
			hintButton.draw(graphics,mouseX,mouseY);
			if (undoButton.isHighlighted())
			{
				if (undoButton.isEnabled() && !grid.getMoveUndone())
				{
					graphics.drawCaption(TextBank.UNDO_BUTTON_CAPTION);
				}
				else if (undoButton.isEnabled() && grid.getMoveUndone())
				{
					graphics.drawCaption(TextBank.REDO_BUTTON_CAPTION);
				}
				else if (victorious)
				{
					graphics.drawCaption(TextBank.UNDO_BUTTON_VICTORY_CAPTION);
				}
				else if (!undoButton.isEnabled() && grid.pieceSelected())
				{
					graphics.drawCaption(TextBank.MOVING_BUTTON_CAPTION);
				}
				else if (!undoButton.isEnabled())
				{
					graphics.drawCaption(TextBank.CANT_UNDO_BUTTON_CAPTION);
				}
			}
			else if (hintButton.isHighlighted())
			{
				if (victorious)
				{
					graphics.drawCaption(TextBank.HINT_BUTTON_VICTORY_CAPTION);
				}
			}
		}
		
		textCursor += graphics.g.getFont().getSize()/2;
		if (!victorious)
		{
			// now draw the score
			graphics.g.setFont(NumerateGame.SCORE_FONT);
			graphics.g.drawString(NumerateGame.INTEGER_FORMAT.format(score),NumerateGame.WINDOW_X-SCORE_X_PADDING-graphics.g.getFontMetrics().stringWidth(NumerateGame.INTEGER_FORMAT.format(score)),SCORE_Y_PADDING+graphics.g.getFont().getSize()/2);
			
			int textCursor2 = TIME_COUNTER_Y + COUNTER_TOP_PADDING + 1;
			// now draw the counters
			graphics.g.setColor(NumerateGame.COUNTER_BORDER_COLOR);
			graphics.drawBorder(TIME_COUNTER_X,TIME_COUNTER_Y,TIME_COUNTER_X + 1 + TIME_COUNTER_WIDTH, TIME_COUNTER_Y + 1 + TIME_COUNTER_HEIGHT);
			graphics.g.setColor(NumerateGame.COUNTER_BACKGROUND_COLOR);
			graphics.g.fillRect(TIME_COUNTER_X + 1, TIME_COUNTER_Y + 1, TIME_COUNTER_WIDTH, TIME_COUNTER_HEIGHT);
			
			graphics.g.setColor(NumerateGame.COUNTER_BORDER_COLOR);
			graphics.drawBorder(MOVE_COUNTER_X,MOVE_COUNTER_Y,MOVE_COUNTER_X + 1 + MOVE_COUNTER_WIDTH, MOVE_COUNTER_Y + 1 + MOVE_COUNTER_HEIGHT);
			graphics.g.setColor(NumerateGame.COUNTER_BACKGROUND_COLOR);
			graphics.g.fillRect(MOVE_COUNTER_X + 1, MOVE_COUNTER_Y + 1, MOVE_COUNTER_WIDTH, MOVE_COUNTER_HEIGHT);
			
			graphics.g.setFont(NumerateGame.COUNTER_HEADING_FONT);
			graphics.g.setColor(NumerateGame.COUNTER_HEADING_COLOR);
			graphics.g.drawString(TextBank.MOVE_COUNTER_HEADING,MOVE_COUNTER_X+MOVE_COUNTER_WIDTH/2-graphics.g.getFontMetrics().stringWidth(TextBank.MOVE_COUNTER_HEADING)/2,MOVE_COUNTER_Y+COUNTER_TOP_PADDING+graphics.g.getFont().getSize()/2);
			graphics.g.drawString(TextBank.TIME_COUNTER_HEADING,TIME_COUNTER_X+TIME_COUNTER_WIDTH/2-graphics.g.getFontMetrics().stringWidth(TextBank.TIME_COUNTER_HEADING)/2,TIME_COUNTER_Y+COUNTER_TOP_PADDING+graphics.g.getFont().getSize()/2);
			textCursor2 += graphics.g.getFont().getSize()*2/3 + COUNTER_TEXT_PADDING;
			
			graphics.g.setFont(NumerateGame.COUNTER_TEXT_FONT);
			graphics.g.setColor(NumerateGame.COUNTER_TEXT_COLOR);
			
			drawUniformString(graphics, strTime, TIME_COUNTER_X+1+TIME_COUNTER_WIDTH/2, textCursor2);
			drawUniformString(graphics, Integer.toString(grid.getMoves()), MOVE_COUNTER_X+1+MOVE_COUNTER_WIDTH/2, textCursor2);
		}
		else
		{
			// Victorious... so print the stuff out.
			graphics.g.setFont(NumerateGame.SMALL_VICTORY_FONT);
			int textCursor2 = NumerateGame.GRID_Y_PADDING+1+VICTORY_Y_PADDING+graphics.g.getFont().getSize()/2;
			graphics.g.drawString(TextBank.FINAL_MOVES+" "+Integer.toString(grid.getMoves()),NumerateGame.GRID_X_PADDING+1+NumerateGame.GRID_TILE_SIZE*(TextBank.DIFFICULTIES.length*2+2)+VICTORY_X_PADDING,textCursor2);
			textCursor2 += graphics.g.getFont().getSize()+VICTORY_LINE_PADDING;
			graphics.g.drawString(TextBank.FINAL_TIME+" "+formattedTime(intTime,1,15),NumerateGame.GRID_X_PADDING+1+NumerateGame.GRID_TILE_SIZE*(TextBank.DIFFICULTIES.length*2+2)+VICTORY_X_PADDING,textCursor2);
			graphics.g.setFont(NumerateGame.LARGE_VICTORY_FONT);
			textCursor2 += graphics.g.getFont().getSize()+VICTORY_LINE_PADDING;
			graphics.g.drawString(TextBank.FINAL_SCORE+" "+NumerateGame.INTEGER_FORMAT.format(score),NumerateGame.GRID_X_PADDING+1+NumerateGame.GRID_TILE_SIZE*(TextBank.DIFFICULTIES.length*2+2)+VICTORY_X_PADDING,textCursor2);
			
		}
		
		// now draw the status bar
		textCursor += STATUS_PADDING;
		graphics.g.setColor(NumerateGame.STATUS_BAR_BORDER_COLOR);
		graphics.drawBorder(NumerateGame.GRID_X_PADDING,textCursor,NumerateGame.GRID_X_PADDING + STATUS_BAR_WIDTH + 1,textCursor + STATUS_BAR_HEIGHT + 1);
		graphics.g.setColor(NumerateGame.STATUS_BAR_BACKGROUND_COLOR);
		graphics.g.fillRect(NumerateGame.GRID_X_PADDING + 1, textCursor + 1,STATUS_BAR_WIDTH,STATUS_BAR_HEIGHT);
		graphics.g.setFont(NumerateGame.STATUS_BAR_FONT);
		if (statusBarNature == 0)
		{
			graphics.g.setColor(NumerateGame.STATUS_BAR_TEXT_COLOR);
		}
		else if (statusBarNature > 0)
		{
			graphics.g.setColor(NumerateGame.STATUS_BAR_POSITIVE_TEXT_COLOR);
		}
		else
		{
			graphics.g.setColor(NumerateGame.STATUS_BAR_NEGATIVE_TEXT_COLOR);
		}
		graphics.g.drawString(statusBar,NumerateGame.GRID_X_PADDING + 1 + STATUS_BAR_PADDING,textCursor + STATUS_BAR_HEIGHT/2 + 1 + graphics.g.getFont().getSize()/3);
	}
	
	/** Draw a uniform-sized string centered at x,y */
	private void drawUniformString(GameGraphics graphics, String str, int x, int y)
	{
		x = x - str.length()*CHAR_WIDTH/2;
		for (int i = 0; i < str.length(); i++)
		{
			String s = "";
			s += str.charAt(i);
			graphics.g.drawString(s,x/* + CHAR_WIDTH/2-graphics.g.getFontMetrics().stringWidth(s)*/,y);
			x += CHAR_WIDTH;
		}
	}
	
	/** Convert an integer amount for time to a formatted version, in mm:ss format */
	private String formattedTime(int time, int minDigits, int maxDigits)
	{
		int minutes = time/60;
		int seconds = time%60;
		String formatted = "";
		
		int minuteDigits = NumerateGame.getDigits(minutes);
		int secondDigits = NumerateGame.getDigits(seconds);
		
		for (int i = minuteDigits; i < minDigits; i++)
		{
			formatted += "0";
		}
		
		if (minuteDigits > maxDigits)
		{
			for (int i = 0; i < maxDigits; i++)
			{
				formatted += "9";
			}
			formatted += ":59";
			return formatted;
		}
		else
		{
			formatted += minutes + ":";
		}
		
		if (secondDigits < 2)
		{
			formatted += "0";
		}
		formatted += seconds;
		
		return formatted;
	}
	
	/** Elapse time. */
	public void elapseTime(double dtime)
	{
		undoButton.elapseTime(dtime);
		hintButton.elapseTime(dtime);
		if (!victorious)
		{
			gameStartTimer.increment(dtime);
			pieceMovedTimer.increment(dtime);
			
			if (gameStartTimer.triggered())
			{
				grid.setActive(true);
				countdown = 0;
				time += dtime;
			}
			else if (gameStartTimer.isActive())
			{
				int oldCountdown = countdown;
				countdown = (int)(gameStartTimer.getPeriod()-gameStartTimer.getCurrentPeriod()+1);
				if (oldCountdown != countdown)
				{
					needsCompleteRedraw = true;
				}
			}
			else
			{
				time += dtime;
				if ((int)time != intTime)
				{
					scoreSecond((int)time-intTime);
					intTime = (int)time;
					strTime = formattedTime(intTime,MIN_MINUTE_DIGITS,MAX_MINUTE_DIGITS);
					needsCompleteRedraw = true;
				}
				
				// now deduct the score for possible moves we've made
				scoreMove(grid.getUncheckedMoves());
			}
			
			if (pieceMovedTimer.isActive() && pieceMovedTimer.triggered())
			{
				statusBar = defaultStatusBar;
				statusBarNature = defaultStatusBarNature;
				needsCompleteRedraw = true;
			}
			
			if (grid.changedStatusBar())
			{
				statusBar = grid.getStatusBar();
				statusBarNature = grid.getStatusBarNature();
				defaultStatusBar = grid.getDefaultStatusBar();
				defaultStatusBarNature = grid.getDefaultStatusBarNature();
				if (grid.statusBarDelay()) // we have a status bar delay... so set a timer to restore it to its "default" state
				{
					pieceMovedTimer.reset();
					pieceMovedTimer.setIsActive(true);
					pieceMovedTimer.increment(dtime);
				}
				else if (pieceMovedTimer.isActive())
				{
					// We don't want our piece moved timer to trigger in the middle of some other status
					// bar case, so stop this timer
					pieceMovedTimer.setIsActive(false);
				}
				needsCompleteRedraw = true;
			}
			
			if (grid.victorious())
			{
				// WINWINWIN
				grid.setActive(false);
				victorious = true;
				statusBar = TextBank.VICTORY_NOTIFICATION;
				statusBarNature = 1;
				defaultStatusBar = TextBank.VICTORY_NOTIFICATION;
				defaultStatusBarNature = 1;
				undoButton.setEnabled(false);
				hintButton.setEnabled(false);
				
				scoresPlacement = scores.scoreFits((grid.getHighestNumber()-4)/2,score);
			}
		}
	}
	
	/** Mouse moved. */
	public void mouseMoved(int x, int y)
	{
		super.mouseMoved(x,y);
		grid.updateHighlight(x,y);
		undoButton.mouseMoved(x,y);
		hintButton.mouseMoved(x,y);
	}
	
	/** Mouse clicked. */
	public void mousePressed(boolean right)
	{
		if (!right)
		{
			boolean pieceSelected = grid.pieceSelected();
			if (grid.selectPiece() == Grid.SUCCESS)
			{
				if (pieceSelected)
				{
					// piece already selected... so change the undo button
					undoButton.setText(TextBank.UNDO_BUTTON);
					if (grid.getMoves() > 0 || grid.getMoveUndone())
					{
						undoButton.setEnabled(true);
					}
					hintButton.setEnabled(true);
				}
				else
				{
					// disable the undo/redo buttons while we make a turn
					undoButton.setEnabled(false);
					hintButton.setEnabled(false);
				}
				
				// and of course, set our help box to be un-refreshed
				helpBoxNeedsRefreshing = true;
			}
			undoButton.press();
			hintButton.press();
			
			if (undoButton.isEnabled() && undoButton.isPressed())
			{
				grid.undoRedoMove();
				if (grid.getMoveUndone())
				{
					undoButton.setText(TextBank.REDO_BUTTON);
				}
				else
				{
					undoButton.setText(TextBank.UNDO_BUTTON);
				}
			}
		}
		else
		{
			if (grid.deselectPiece() == Grid.SUCCESS)
			{
				helpBoxNeedsRefreshing = true;
			}
			
			if (grid.getMoves() > 0 || grid.getMoveUndone())
			{
				undoButton.setEnabled(true);
			}
			hintButton.setEnabled(true);
		}
		needsCompleteRedraw = true;
	}
	
	/** Receive a key press */
	public void keyPressed(int keyCode)
	{
		if (grid.focused())
		{
			if (keyCode == KeyEvent.VK_UP)
			{
				grid.moveHighlight(Grid.UP,1);
			}
			else if (keyCode == KeyEvent.VK_DOWN)
			{
				grid.moveHighlight(Grid.DOWN,1);
			}
			else if (keyCode == KeyEvent.VK_LEFT)
			{
				grid.moveHighlight(Grid.LEFT,1);
			}
			else if (keyCode == KeyEvent.VK_RIGHT)
			{
				grid.moveHighlight(Grid.RIGHT,1);
			}
			else if (keyCode == KeyEvent.VK_ENTER)
			{
				grid.selectPiece();
			}
		}
	}
	
	/** Elapse a second on the score, deducting the score as necessary */
	private void scoreSecond(int times)
	{
		if (score > 0)
		{
			score = Math.max(score-times*3,0);
			needsCompleteRedraw = true;
		}
	}
	
	/** Makes a move, deducting the score as necessary */
	private void scoreMove(int times)
	{
		if (score > 0)
		{
			score = Math.max(score-times*13,0);
			needsCompleteRedraw = true;
		}
	}
}