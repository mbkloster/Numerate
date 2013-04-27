public class GameBoard
{
	/** Board difficulty/size. 4 = easy, 6 = medium, 8 = hard, 10 = expert. */
	private int difficulty;
	
	/** Game board. */
	private int board[][];
	
	/** x-coordinant of currently selected piece */
	private int selectedX;
	/** y-coordinant of currently selected piece */
	private int selectedY;
	
	/** Standard constructor. */
	public GameBoard(int d_difficulty)
	{
		difficulty = d_difficulty;
		board = new int[difficulty][difficulty];
	}
	
	/** Render the game board */
	public void render()
	{
	}
}