import java.util.Random;

/**
  * Numerate grid class. <br /><br />
  *
  * Describes an instance of a Numerate grid.
  */

public class Grid
{
	/** Constant for a failed move. */
	public static final int FAILURE = 0;
	/** Constant for a successful move. */
	public static final int SUCCESS = 1;
	/** Constant for an irrelevant move. */
	public static final int IRRELEVANT = 2;
	
	/** Constant for up */
	public static final int UP = 0;
	/** Constant for down */
	public static final int DOWN = 1;
	/** Constant for left */
	public static final int LEFT = 2;
	/** Constant for right */
	public static final int RIGHT = 3;
	
	/** How many times will the game attempt to avoid a repeated number? */
	public static final int PLACEMENT_RETRY_ATTEMPTS = 2;
	/** % chance, in scatter mode, that a grid tile will naturally mimic the previous tile */
	public static final double REPEAT_TILE_CHANCE = 0.15;
	/** Multiplier for the % chance each tile a repeat tile is naturally used. */
	public static final double REPEAT_TILE_MULTIPLIER = 0.8;
	
	/** The grid's x position (on-screen) */
	private int x;
	/** The grid's y position (on-screen) */
	private int y;
	
	/** The game mode used. */
	private int mode;
	
	/** Contents of the grid. */
	private int grid[][];
	/** Grid highlights. Items in this array should be set to TRUE if they are movable. */
	private boolean highlights[][];
	/** The highest number in the grid. Used for piece selection purposes. */
	private int highestNumber;
	
	/** Mouse-highlighted x coordinate */
	private int highlightedX;
	/** Mouse-highlighted y coordinate */
	private int highlightedY;
	
	/** Selected x coordinate */
	private int selectedX;
	/** Selected y coordinate */
	private int selectedY;
	
	/** The number of moves taken in the grid so far. */
	private int moves;
	/** The number of moves made since the last check by the game. */
	private int uncheckedMoves;
	
	/** Will we be changing the status bar? */
	private boolean changedStatusBar;
	/** New status bar text */
	private String statusBar;
	/** New status bar nature */
	private int statusBarNature;
	/** Will the status bar have a delay before returning to its "normal" nature? */
	private boolean statusBarDelay;
	/** The default ("normal") status */
	private String defaultStatusBar;
	/** The default status bar nature */
	private int defaultStatusBarNature;
	
	/** Is the grid active? If not, will be greyed out and unmovable. */
	private boolean active;
	/** Is the grid focused? If it is, then pieces will be highlighted actively on the screen */
	private boolean focused;
	/** Using the keyboard? If this is enabled, display umovable pieces as being greyed out. */
	private boolean usingKeyboard;
	
	/** Does the grid (and hence, the whole screen) need a complete redraw? */
	public boolean needsCompleteRedraw;
	
	/** X position of grid swap position 1 */
	private int x1;
	/** Y position of grid swap position 1 */
	private int y1;
	/** X position of grid swap position 2 */
	private int x2;
	/** Y position of grid swap position 2 */
	private int y2;
	/** Has a move been undone? Because if so, redo the move */
	private boolean moveUndone;
	
	// REMOVE
	Random generator;
	int possibleXs[];
	int possibleYs[];
	int possibleMoves;
	
	/** Standard constructor. Takes in an existing grid as a parameter */
	public Grid(int d_mode, int d_grid[][], int d_x, int d_y, int mouseX, int mouseY)
	{
		mode = d_mode;
		grid = d_grid;
		highlights = new boolean[grid.length][grid[0].length];
		
		selectedX = -1;
		selectedY = -1;
		
		x = d_x;
		y = d_y;
		
		active = false;
		needsCompleteRedraw = true;
		usingKeyboard = false;
		focused = true;
		
		moves = 0;
		uncheckedMoves = 0;
		
		changedStatusBar = false;
		statusBar = "";
		statusBarNature = 0;
		statusBarDelay = false;
		defaultStatusBar = "";
		defaultStatusBarNature = 0;
		
		// finally, update and select the current highlighted piece
		updateHighlight(mouseX, mouseY);
		
		x1 = -1;
		y1 = -1;
		x2 = -1;
		y2 = -1;
		moveUndone = false;
		
		generator = new Random();
		possibleXs = new int[50];
		possibleYs = new int[50];
		possibleMoves = 0;
	}
	
	/** Create a new grid randomly, based on difficulty and game type settings. */
	public Grid(int d_mode, int difficulty, int d_x, int d_y, int mouseX, int mouseY)
	{
		this(d_mode,new int[difficulty][difficulty],d_x,d_y,mouseX,mouseY);
		
		int numbers[] = new int[difficulty]; // stores a count of how many numbers of each type are on the grid
		Random generator = new Random();
		
		if (d_mode != NumerateGame.SCATTER_MODE)
		{
			// design a grid with as much randomness as possible...
			// meant for non-scatter games
			for (int i = 0; i < grid.length; i++)
			{
				for (int j = 0; j < grid[i].length; j++)
				{
					int attempts = 0;
					do
					{
						// keep repeating the number selection process until we either get a non-repeated number or give up
						do
						{
							// keep selecting a number until we get a number that is not already full
							grid[i][j] = generator.nextInt(difficulty)+1;
						} while(numbers[grid[i][j]-1] >= difficulty);
						
					} while (((i > 0 && grid[i-1][j] == grid[i][j]) || (j > 0 && grid[i][j-1] == grid[i][j])) && attempts++ < PLACEMENT_RETRY_ATTEMPTS);
					numbers[grid[i][j]-1]++;
				}
			}
		}
		else
		{
			// create a grid for scatter mode
			// this will intentionally group stuff together more than a normal grid
			double repeatChance = REPEAT_TILE_CHANCE;
			for (int i = 0; i < grid.length; i++)
			{
				for (int j = 0; j < grid[i].length; j++)
				{
					// first, see if we want to copy an adjacent tile
					if ((i > 0 || j > 0) && generator.nextDouble() < repeatChance)
					{
						if (generator.nextDouble() < 0.5 && j > 0) // 50% chance we try the left tile first, then up tile
						{
							int number = grid[i][j-1]-1;
							if (numbers[number] >= difficulty && i > 0) // now try going up
							{
								number = grid[i-1][j]-1;
								if (numbers[number] < difficulty) // success! up works
								{
									grid[i][j] = number+1;
									numbers[number]++;
									repeatChance *= REPEAT_TILE_MULTIPLIER;
									continue;
								}
							}
							else // success! left works
							{
								grid[i][j] = number+1;
								numbers[number]++;
								repeatChance *= REPEAT_TILE_MULTIPLIER;
								continue;
							}
						}
						else if (i > 0) // try going up instead
						{
							int number = grid[i-1][j]-1;
							if (numbers[number] >= difficulty && j > 0) // now try going up
							{
								number = grid[i][j-1]-1;
								if (numbers[number] < difficulty) // success! up works
								{
									grid[i][j] = number+1;
									numbers[number]++;
									repeatChance *= REPEAT_TILE_MULTIPLIER;
									continue;
								}
							}
							else // success! left works
							{
								grid[i][j] = number+1;
								numbers[number]++;
								repeatChance *= REPEAT_TILE_MULTIPLIER;
								continue;
							}
						}
					}
					
					do
					{
						grid[i][j] = generator.nextInt(difficulty)+1;
					} while (numbers[grid[i][j]-1] >= difficulty);
					repeatChance = REPEAT_TILE_CHANCE; // reset the repeat tile chance
					numbers[grid[i][j]-1]++;
				}
			}
		}
		
		setHighestNumber();
	}
	
	/** Call this function to find out the highest number in the grid. */
	public void setHighestNumber()
	{
		highestNumber = 1;
		// now find the highest piece in the grid
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[i].length; j++)
			{
				if (highestNumber < grid[i][j])
				{
					highestNumber = grid[i][j];
				}
			}
		}
	}
	
	/** Updates the highlighted piece based on mouse x and y information */
	public void updateHighlight(int mouseX, int mouseY)
	{
		int oldX = highlightedX, oldY = highlightedY;
		
		boolean beforeGrid = (mouseX >= x && mouseY >= y);
		
		mouseX -= x+1;
		mouseY -= y+1;
		
		highlightedX = mouseX/NumerateGame.GRID_TILE_SIZE;
		highlightedY = mouseY/NumerateGame.GRID_TILE_SIZE;
		
		if (highlightedX < 0 || highlightedY < 0 || mouseX < 0 || mouseY < 0 || highlightedX >= grid[0].length || highlightedY >= grid.length)
		{
			highlightedX = -1;
			highlightedY = -1;
		}
		
		if ((oldX != highlightedX || oldY != highlightedY) && active)
		{
			needsCompleteRedraw = true;
		}
		
		if ((highlightedX >= 0 && highlightedY >= 0 && beforeGrid) != focused)
		{
			needsCompleteRedraw = true;
		}
		
		focused = (highlightedX >= 0 && highlightedY >= 0 && beforeGrid);
		
		setUsingKeyboard(false);
	}
	
	/** Moves the cursor */
	public void moveHighlight(int direction, int amount)
	{
		if (direction == UP)
		{
			highlightedY = Math.max(0,highlightedY-amount);
		}
		else if (direction == DOWN)
		{
			highlightedY = Math.min(grid.length-1,highlightedY+amount);
		}
		else if (direction == LEFT)
		{
			highlightedX = Math.max(0,highlightedX-amount);
		}
		else if (direction == RIGHT)
		{
			highlightedX = Math.min(grid[0].length-1,highlightedX+amount);
		}
		
		setUsingKeyboard(true);
		needsCompleteRedraw = true;
	}
	
	/** Swap two pieces. */
	public void swap(int x1, int y1, int x2, int y2)
	{
		int temp = grid[y1][x1];
		grid[y1][x1] = grid[y2][x2];
		grid[y2][x2] = temp;
	}
	
	/**
	  * Attempt to deselect a piece, if one is selected
	  */
	public int deselectPiece()
	{
		if (active)
		{
			if (selectedX >= 0 && selectedY >= 0)
			{
				selectedX = -1;
				selectedY = -1;
				// now cycle through the grid and erase any highlights
				for (int i = 0; i < highlights.length; i++)
				{
					for (int j = 0; j < highlights[i].length; j++)
					{
						highlights[i][j] = false;
					}
				}
				
				changedStatusBar = true;
				statusBar = TextBank.SELECT_PIECE_PROMPT;
				statusBarNature = 0;
				return SUCCESS;
			}
			else
			{
				return IRRELEVANT;
			}
		}
		return IRRELEVANT;
	}
	
	/**
	  * Attempt to select a piece -- can be either used to select a piece to move, or to actually do a swap
	  * @return the success (or failure) of the attempt
	  */
	public int selectPiece()
	{
		if (active)
		{
			if (selectedX < 0 || selectedY < 0)
			{
				// no piece already selected... SO...
				if (highlightedX < 0 || highlightedY < 0)
				{
					// the mouse is outside of the grid, so this selection is irrelevant...
					return IRRELEVANT;
				}
				else if (grid[highlightedY][highlightedX] < 2)
				{
					// we have a 1, so we can't do anything with this
					changedStatusBar = true;
					statusBar = TextBank.ONE_PIECE_NOTIFICATION;
					statusBarNature = -1;
					statusBarDelay = true;
					defaultStatusBar = TextBank.SELECT_PIECE_PROMPT;
					defaultStatusBarNature = 0;
					return FAILURE;
				}
				
				boolean movable = false; // assume a piece is unmovable initially
				
				if (grid[highlightedY][highlightedX] % 2 == 0)
				{
					// even number... go straight
					int currentX = highlightedX;
					int currentY = highlightedY;
					
					// start by going left
					while (--currentX >= 0)
					{
						if (grid[currentY][currentX] <= grid[highlightedY][highlightedX] && (grid[currentY][currentX] > 1 || grid[highlightedY][highlightedX] < highestNumber))
						{
							highlights[currentY][currentX] = true;
							movable = true;
						}
						else
						{
							// we've found something to break the chain, so stop
							break;
						}
					}
					currentX = highlightedX;
					
					// then right
					while (++currentX < grid[currentY].length)
					{
						if (grid[currentY][currentX] <= grid[highlightedY][highlightedX] && (grid[currentY][currentX] > 1 || grid[highlightedY][highlightedX] < highestNumber))
						{
							highlights[currentY][currentX] = true;
							movable = true;
						}
						else
						{
							// we've found something to break the chain, so stop
							break;
						}
					}
					currentX = highlightedX;
					
					// then up
					while (--currentY >= 0)
					{
						if (grid[currentY][currentX] <= grid[highlightedY][highlightedX] && (grid[currentY][currentX] > 1 || grid[highlightedY][highlightedX] < highestNumber))
						{
							highlights[currentY][currentX] = true;
							movable = true;
						}
						else
						{
							// we've found something to break the chain, so stop
							break;
						}
					}
					currentY = highlightedY;
					
					// then down
					while (++currentY < grid.length)
					{
						if (grid[currentY][currentX] <= grid[highlightedY][highlightedX] && (grid[currentY][currentX] > 1 || grid[highlightedY][highlightedX] < highestNumber))
						{
							highlights[currentY][currentX] = true;
							movable = true;
						}
						else
						{
							// we've found something to break the chain, so stop
							break;
						}
					}
				}
				else
				{
					// odd piece - move diagonally
					int currentX = highlightedX;
					int currentY = highlightedY;
					
					// start by going up-left
					while (--currentX >= 0 && --currentY >= 0)
					{
						if (grid[currentY][currentX] <= grid[highlightedY][highlightedX] && (grid[currentY][currentX] > 1 || grid[highlightedY][highlightedX] < highestNumber))
						{
							highlights[currentY][currentX] = true;
							movable = true;
						}
						else
						{
							// we've found something to break the chain, so stop
							break;
						}
					}
					currentX = highlightedX;
					currentY = highlightedY;
					
					// then up-right
					while (++currentX < grid[currentY].length && --currentY >= 0)
					{
						if (grid[currentY][currentX] <= grid[highlightedY][highlightedX] && (grid[currentY][currentX] > 1 || grid[highlightedY][highlightedX] < highestNumber))
						{
							highlights[currentY][currentX] = true;
							movable = true;
						}
						else
						{
							// we've found something to break the chain, so stop
							break;
						}
					}
					currentX = highlightedX;
					currentY = highlightedY;
					
					// then down-left
					while (--currentX >= 0 && ++currentY < grid.length)
					{
						if (grid[currentY][currentX] <= grid[highlightedY][highlightedX] && (grid[currentY][currentX] > 1 || grid[highlightedY][highlightedX] < highestNumber))
						{
							highlights[currentY][currentX] = true;
							movable = true;
						}
						else
						{
							// we've found something to break the chain, so stop
							break;
						}
					}
					currentX = highlightedX;
					currentY = highlightedY;
					
					// then down-right
					while (++currentX < grid[currentY].length && ++currentY < grid.length)
					{
						if (grid[currentY][currentX] <= grid[highlightedY][highlightedX] && (grid[currentY][currentX] > 1 || grid[highlightedY][highlightedX] < highestNumber))
						{
							highlights[currentY][currentX] = true;
							movable = true;
						}
						else
						{
							// we've found something to break the chain, so stop
							break;
						}
					}
					currentX = highlightedX;
					currentY = highlightedY;
				}
		
				if (movable)
				{
					selectedX = highlightedX;
					selectedY = highlightedY;
					
					changedStatusBar = true;
					statusBar = TextBank.MOVE_PIECE_PROMPT;
					statusBarNature = 0;
					defaultStatusBar = TextBank.MOVE_PIECE_PROMPT;
					defaultStatusBarNature = 0;
					
					return SUCCESS;
				}
				else
				{
					changedStatusBar = true;
					statusBar = TextBank.UNMOVABLE_NOTIFICATION;
					statusBarNature = -1;
					statusBarDelay = true;
					return FAILURE;
				}
			}
			else
			{
				// a piece is already selected... so see if we can swap
				
				if (highlightedY >= 0 && highlightedX >= 0 && highlights[highlightedY][highlightedX])
				{
					x1 = highlightedX;
					y1 = highlightedY;
					x2 = selectedX;
					y2 = selectedY;
					
					swap(highlightedX, highlightedY, selectedX, selectedY);
					deselectPiece();
					moves++;
					uncheckedMoves++;
					
					moveUndone = false;
					
					changedStatusBar = true;
					statusBar = TextBank.PIECE_MOVED_NOTIFICATION;
					statusBarNature = 1;
					statusBarDelay = true;
					defaultStatusBar = TextBank.SELECT_PIECE_PROMPT;
					defaultStatusBarNature = 0;
					
					return SUCCESS;
				}
				else if (highlightedY >= 0 && highlightedX >= 0)
				{
					changedStatusBar = true;
					statusBar = TextBank.INVALID_MOVE_NOTIFICATION;
					statusBarNature = -1;
					statusBarDelay = true;
					return FAILURE;
				}
				else
				{
					return IRRELEVANT;
				}
			}
		}
		return IRRELEVANT;
	}
	
	/** Do that whole victory check thing. */
	public boolean victorious()
	{
		if (mode == NumerateGame.LINEUP_MODE)
		{
			// ==================================================
			// CHECK LINE-UP MODE
			// ==================================================
			boolean victorious = true;
			// first, check horizontally, ascending
			for (int i = 0; i < grid.length; i++)
			{
				for (int j = 0; j < grid[i].length; j++)
				{
					if (grid[i][j] != i+1)
					{
						victorious = false;
						break;
					}
				}
				if (!victorious)
				{
					break;
				}
			}
			if (victorious)
			{
				return true;
			}
			victorious = true;
			
			// next, check horizontally, descending
			for (int i = 0; i < grid.length; i++)
			{
				for (int j = 0; j < grid[i].length; j++)
				{
					if (grid[i][j] != grid.length-i)
					{
						victorious = false;
						break;
					}
				}
				if (!victorious)
				{
					break;
				}
			}
			if (victorious)
			{
				return true;
			}
			
			// next, check vertically, ascending
			for (int i = 0; i < grid.length; i++)
			{
				for (int j = 0; j < grid[i].length; j++)
				{
					if (grid[i][j] != j+1)
					{
						victorious = false;
						break;
					}
				}
				if (!victorious)
				{
					break;
				}
			}
			if (victorious)
			{
				return true;
			}
			victorious = true;
			
			// next, check vertically, descending
			for (int i = 0; i < grid.length; i++)
			{
				for (int j = 0; j < grid[i].length; j++)
				{
					if (grid[i][j] != grid[i].length-j)
					{
						victorious = false;
						break;
					}
				}
				if (!victorious)
				{
					break;
				}
			}
			if (victorious)
			{
				return true;
			}
			return false;
		}
		else if (mode == NumerateGame.ALTERNATE_MODE)
		{
			// ==================================================
			// CHECK ALTERNATE MODE
			// ==================================================
			
			boolean victorious = true;
			// first, try row-by-row
			for (int i = 0; i < grid.length; i++)
			{
				int number1 = -1, number2 = -1;
				
				for (int j = 0; j < grid[i].length; j++)
				{
					if (j % 2 > 0) // odd number
					{
						if (number2 < 0)
						{
							// number isn't already set... so set it
							number2 = grid[i][j];
						}
						else if (number2 != grid[i][j])
						{
							victorious = false;
							break;
						}
					}
					else // even number
					{
						if (number1 < 0)
						{
							// number isn't already set... so set it
							number1 = grid[i][j];
						}
						else if (number1 != grid[i][j])
						{
							victorious = false;
							break;
						}
					}
				}
				
				if (!victorious)
				{
					break;
				}
			}
			
			if (victorious)
			{
				// we know the row-by-row worked... so return true here
				return true;
			}
			
			victorious = true;
			
			// now try column by column
			for (int j = 0; j < grid[0].length; j++)
			{
				int number1 = -1, number2 = -1;
				
				for (int i = 0; i < grid.length; i++)
				{
					if (i % 2 > 0) // odd number
					{
						if (number2 < 0)
						{
							// number isn't already set... so set it
							number2 = grid[i][j];
						}
						else if (number2 != grid[i][j])
						{
							victorious = false;
							break;
						}
					}
					else // even number
					{
						if (number1 < 0)
						{
							// number isn't already set... so set it
							number1 = grid[i][j];
						}
						else if (number1 != grid[i][j])
						{
							victorious = false;
							break;
						}
					}
				}
				if (!victorious)
				{
					break;
				}
				
			}
			
			// now we're at the mercy of whatever the column-by-column analysis was
			return victorious;
		}
		else if (mode == NumerateGame.SCATTER_MODE)
		{
			// ==================================================
			// CHECK SCATTER MODE
			// ==================================================
			for (int i = 0; i < grid.length; i++)
			{
				for (int j = 0; j < grid[i].length; j++)
				{
					if (j > 0 && grid[i][j-1] == grid[i][j])
					{
						return false;
					}
					if (i > 0 && grid[i-1][j] == grid[i][j])
					{
						return false;
					}
				}
			}
			return true;
		}
		else if (mode == NumerateGame.SUMMATION_MODE)
		{
			// ==================================================
			// CHECK SUMMATION MODE
			// ==================================================
			// first check all rows...
			for (int i = 0; i < grid.length; i++)
			{
				boolean numbers[] = new boolean[grid[i].length];
				for (int j = 0; j < grid[i].length; j++)
				{
					int number = grid[i][j];
					if (number <= numbers.length && number >= 0)
					{
						numbers[number-1] = true;
					}
				}
				
				// now check the numbers
				for (int j = 0; j < numbers.length; j++)
				{
					if (!numbers[j])
					{
						return false;
					}
				}
			}
			
			// next, check all columns...
			for (int j = 0; j < grid[0].length; j++)
			{
				boolean numbers[] = new boolean[grid.length];
				for (int i = 0; i < grid.length; i++)
				{
					int number = grid[i][j];
					if (number <= numbers.length && number >= 0)
					{
						numbers[number-1] = true;
					}
				}
				
				// now check the numbers
				for (int i = 0; i < numbers.length; i++)
				{
					if (!numbers[i])
					{
						return false;
					}
				}
			}
			
			// if we made it past all that, we have to say the player won
			return true;
		}
		
		return false;
	}
	
	/** Draw the grid on the screen */
	public void draw(GameGraphics graphics)
	{
		// first, draw the borders.
		graphics.g.setColor(NumerateGame.GRID_BORDER_COLOR);
		graphics.drawBorder(x,y,x + 1 + grid[0].length*NumerateGame.GRID_TILE_SIZE,y + 1 + grid.length*NumerateGame.GRID_TILE_SIZE);
		
		// next, fill in the box itself
		graphics.g.setColor(NumerateGame.GRID_BG_COLOR);
		graphics.g.fillRect(x+1,y+1, NumerateGame.GRID_TILE_SIZE*grid[0].length, NumerateGame.GRID_TILE_SIZE*grid.length);
		
		graphics.g.setFont(NumerateGame.GRID_FONT);
		graphics.g.setColor(NumerateGame.GRID_TEXT_COLOR);
		int textCursor = y + 1;
		boolean changedColor = false;
		
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[i].length; j++)
			{
				if (active && highlightedX == j && highlightedY == i && (selectedX < 0 || selectedY < 0 || highlights[i][j]))
				{
					// this is the currently highlighted piece... so give it a nice background
					graphics.g.setColor(NumerateGame.GRID_HIGHLIGHTED_BG_COLOR);
					graphics.g.fillRect(x + j*NumerateGame.GRID_TILE_SIZE + 1, y + i*NumerateGame.GRID_TILE_SIZE + 1,NumerateGame.GRID_TILE_SIZE-1,NumerateGame.GRID_TILE_SIZE+1);
					graphics.g.setColor(NumerateGame.GRID_TEXT_COLOR);
				}
				
				if (!active)
				{
					graphics.g.setColor(NumerateGame.INACTIVE_GRID_TEXT_COLOR);
				}
				else if (selectedX == j && selectedY == i)
				{
					graphics.g.setColor(NumerateGame.GRID_SELECTED_TEXT_COLOR);
				}
				else if (selectedX == -1 && selectedY == -1 && highlightedX == j && highlightedY == i && focused)
				{
					graphics.g.setColor(NumerateGame.GRID_CURSOR_TEXT_COLOR);
				}
				else if (selectedX != -1 && selectedY != -1 && highlightedX == j && highlightedY == i && highlights[highlightedY][highlightedX] && focused)
				{
					graphics.g.setColor(NumerateGame.GRID_CURSOR_TEXT_COLOR);
				}
				else if (selectedX != -1 && selectedY != -1 && highlightedX == j && highlightedY == i && usingKeyboard && focused)
				{
					graphics.g.setColor(NumerateGame.INACTIVE_GRID_TEXT_COLOR);
				}
				else if (highlights[i][j])
				{
					graphics.g.setColor(NumerateGame.GRID_HIGHLIGHTED_TEXT_COLOR);
				}
				
				graphics.g.drawString(Integer.toString(grid[i][j]),1+x+NumerateGame.GRID_TILE_SIZE*j+NumerateGame.GRID_TILE_SIZE/2-graphics.g.getFontMetrics().stringWidth(Integer.toString(grid[i][j]))/2,1+y+NumerateGame.GRID_TILE_SIZE*i+NumerateGame.GRID_TILE_SIZE/2+NumerateGame.GRID_FONT.getSize()/6);
				
				// now draw the vertical line
				if (i <= 0)
				{
					if (j > 0) // only draw the vertical line if we are after the first tile
					{
						graphics.g.setColor(NumerateGame.GRID_TILE_BORDER_COLOR);
						graphics.g.drawLine(x + NumerateGame.GRID_TILE_SIZE*j, y+1, x + NumerateGame.GRID_TILE_SIZE*j, y+1+NumerateGame.GRID_TILE_SIZE*grid.length);
						
					}
				}
				
				// now reset the color...
				graphics.g.setColor(NumerateGame.GRID_TEXT_COLOR);
			}
			// now draw the horizontal line
			if (i > 0) // only draw the horizontal line if we are after the first row
			{
				graphics.g.setColor(NumerateGame.GRID_TILE_BORDER_COLOR);
				graphics.g.drawLine(x+1, y+1+NumerateGame.GRID_TILE_SIZE*i, x+NumerateGame.GRID_TILE_SIZE*grid[0].length, y+1+NumerateGame.GRID_TILE_SIZE*i);
				graphics.g.setColor(NumerateGame.GRID_TEXT_COLOR);
			}
		}
	}
	
	/** Undo/redo a move */
	public void undoRedoMove()
	{
		if (moves > 0 && !moveUndone)
		{
			// undo the move
			swap(x1,y1,x2,y2);
			moves--;
			moveUndone = true;
			
			statusBar = TextBank.MOVE_UNDO_STATUS;
			
		}
		else if (moveUndone)
		{
			// redo the move
			swap(x1,y1,x2,y2);
			moves++;
			moveUndone = false;
			
			statusBar = TextBank.MOVE_REDO_STATUS;
		}
		
		changedStatusBar = true;
		statusBarNature = 0;
		statusBarDelay = true;
	}
	
	/** Gets the x position of the grid. */
	public int getX()
	{
		return x;
	}
	
	/** Gets the y position of the grid. */
	public int getY()
	{
		return y;
	}
	
	/** Gets the x size of the grid (in tiles). */
	public int getXSize()
	{
		return grid[0].length;
	}
	
	/** Gets the x size of the grid (in tiles). */
	public int getYSize()
	{
		return grid.length;
	}
	
	/** Gets the move count of the grid. */
	public int getMoves()
	{
		return moves;
	}
	
	/** Gets the number of unchecked moves on the grid. */
	public int getUncheckedMoves()
	{
		int unchecked = uncheckedMoves;
		uncheckedMoves = 0;
		return unchecked;
	}
	
	/** Returns whether or not the status has been changed. */
	public boolean changedStatusBar()
	{
		if (changedStatusBar)
		{
			// reset this, since obviously we don't need it after we've confirmed that the status bar has changed
			changedStatusBar = false;
			return true;
		}
		return changedStatusBar;
	}
	
	/** Returns the new status bar text. */
	public String getStatusBar()
	{
		return statusBar;
	}
	
	/** Returns the status bar nature. */
	public int getStatusBarNature()
	{
		return statusBarNature;
	}
	
	/** Returns the default status bar text. */
	public String getDefaultStatusBar()
	{
		return defaultStatusBar;
	}
	
	/** Returns the default status bar nature. */
	public int getDefaultStatusBarNature()
	{
		return defaultStatusBarNature;
	}
	
	/** Gets whether or not a move has been undone. */
	public boolean getMoveUndone()
	{
		return moveUndone;
	}
	
	/** Gets whether or not the grid is focused */
	public boolean focused()
	{
		return focused;
	}
	
	/** Is there a status ba delay? */
	public boolean statusBarDelay()
	{
		if (statusBarDelay)
		{
			statusBarDelay = false;
			return true;
		}
		return statusBarDelay;
	}
	
	/** Gets whether or not there is a piece currently selected. */
	public boolean pieceSelected()
	{
		return (selectedX >= 0 && selectedY >= 0);
	}
	
	/** Gets the highest number in the grid. */
	public int getHighestNumber()
	{
		return highestNumber;
	}
	
	/** Sets the active status of the grid. */
	public void setActive(boolean d_active)
	{
		if (d_active != active)
		{
			needsCompleteRedraw = true;
		}
		active = d_active;
		if (active)
		{
			changedStatusBar = true;
			statusBar = TextBank.SELECT_PIECE_PROMPT;
			statusBarNature = 0;
		}
	}
	
	/** Sets whether or not the keyboard is in use */
	public void setUsingKeyboard(boolean d_usingKeyboard)
	{
		usingKeyboard = d_usingKeyboard;
	}
	
	// MAKE A RANDOM MOVE -- REMOVE THIS SHIT!
	public void makeRandomMove()
	{
		int x = 0, y = 0;
		do
		{
			highlightedY = generator.nextInt(grid.length);
			highlightedX = generator.nextInt(grid[highlightedY].length);
			
			if (selectPiece() != SUCCESS)
			{
				continue;
			}
			
			possibleMoves = 0;
			if (grid[selectedY][selectedX] % 2 == 0)
			{
				// even piece
				x = selectedX;
				y = selectedY-1;
				while (y >= 0 && highlights[y][x])
				{
					possibleYs[possibleMoves] = y;
					possibleXs[possibleMoves] = x;
					y--;
					possibleMoves++;
				}
				x = selectedX;
				y = selectedY+1;
				while (y < highlights.length && highlights[y][x])
				{
					possibleYs[possibleMoves] = y;
					possibleXs[possibleMoves] = x;
					y++;
				}
				
				x = selectedX-1;
				y = selectedY;
				while (x >= 0 && highlights[y][x])
				{
					possibleYs[possibleMoves] = y;
					possibleXs[possibleMoves] = x;
					x--;
				}
				x = selectedX+1;
				y = selectedY;
				while (y < highlights.length && highlights[y][x])
				{
					possibleYs[possibleMoves] = y;
					possibleXs[possibleMoves] = x;
					x++;
				}
			}
			else
			{
				// odd
				while (y >= 0 && highlights[y][x])
				{
					possibleYs[possibleMoves] = y;
					possibleXs[possibleMoves] = x;
					y--;
				}
				x = selectedX;
				y = selectedY+1;
				while (y < highlights.length && highlights[y][x])
				{
					possibleYs[possibleMoves] = y;
					possibleXs[possibleMoves] = x;
					y++;
				}
				
				x = selectedX-1;
				y = selectedY;
				while (x >= 0 && highlights[y][x])
				{
					possibleYs[possibleMoves] = y;
					possibleXs[possibleMoves] = x;
					x--;
				}
				x = selectedX+1;
				y = selectedY;
				while (y < highlights.length && highlights[y][x])
				{
					possibleYs[possibleMoves] = y;
					possibleXs[possibleMoves] = x;
					x++;
				}
			}
		} while (false);
	}
}