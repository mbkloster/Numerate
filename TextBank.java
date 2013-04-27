/**
  * Numerate text file. This should contain every text string in the game.<br />
  * Game language: English (US)
  */
public class TextBank
{
	public static final String MAIN_MENU_OPTIONS[] = {
		"Play Quick Game",
		"Play Challenge",
		"How to Play",
		"Settings",
		"Scores and Progress",
		"Credits",
		"Quit"
	};
	public static final String MAIN_MENU_CAPTIONS[] = {
		"Play a simple, quick game of Numerate.",
		"Play a challenge game. Recommended for intermediate to advanced players.",
		"A simple guide to the basic rules of the game.",
		"Change or tweak the game settings.",
		"View high scores and progress on challenges.",
		"View a list of outstanding and fabulous people.",
		"Quit the game (you probably shouldn't select this)."
	};
	
	public static final String SELECT_GAME_MODE = "Select a Game Mode";
	
	public static final String MODES[] = {"Line-Up","Alternation","Scatter","Summation"};
	public static final String MODE_DESCRIPTIONS[] = {
		"Line up grid tiles so either every row or every column has one number, horizontal or vertical, ascending or descending.",
		"Line up grid tiles so either every row or every column alternates between two numbers.",
		"Arrange the grid tiles so no number touches the same number in any direction.",
		"Line up grid tiles so every row and column has every single number in the grid."
	};
	
	public static final String SELECT_DIFFICULTY = "Select Difficulty";
	
	public static final String DIFFICULTIES[] = {"Easy (4x4 grid)","Medium (6x6 grid)","Hard (8x8 grid)","Master (10x10 grid)"};
	public static final String DIFFICULTIES_SHORT[] = {"Easy","Medium","Hard","Master"};
	
	public static final String CREDITS_LINES[] = {
		"Created, Programmed and Designed by Matthew Kloster",
		"(c) 2008-2009"};
	public static final String CREDITS_RETURN = "Press any key to return to main menu.";
	
	public static final String HOWTOPLAY_INTRO[] = {
		"Numerate is a game that combines tactical skill and thinking speed to give you a unique puzzle challenge. Initially,"};
	public static final String HOWTOPLAY_CYCLEFIRST = "RIGHT-CLICK or ESC to return to main menu, LEFT CLICK or ENTER to proceed to next page.";
	public static final String HOWTOPLAY_CYCLE = "RIGHT-CLICK or ESC to go back to last page, LEFT CLICK or ENTER to proceed to next page.";
	public static final String HOWTOPLAY_CYCLELAST = "RIGHT-CLICK or ESC to go back to last page, LEFT CLICK or ENTER to finish tutorial.";
	
	public static final String BACK_TO_MENU = "Back to Menu";
	public static final String BACK_TO_MENU_CAPTION = "Return to the main menu.";
	
	public static final String BACK_TO_MODE_SELECT = "Back";
	public static final String BACK_TO_MODE_SELECT_CAPTION = "Return to the mode selection screen.";
	
	public static final String TIME_COUNTER_HEADING = "Time";
	public static final String MOVE_COUNTER_HEADING = "Moves";
	
	public static final String GAME_BEGINNING_WAIT = "Waiting for game to begin...";
	public static final String SELECT_PIECE_PROMPT = "Move over a piece and select it to move it.";
	public static final String MOVE_PIECE_PROMPT = "Move over a highlighted piece and select it to move the currently selected piece.";
	public static final String PIECE_MOVED_NOTIFICATION = "Piece moved!";
	public static final String ONE_PIECE_NOTIFICATION = "You cannot select a 1 to move.";
	public static final String UNMOVABLE_NOTIFICATION = "You cannot move this piece - every surrounding piece is larger.";
	public static final String INVALID_MOVE_NOTIFICATION = "You cannot move outside of the highlighted pieces.";
	public static final String MOVE_UNDO_STATUS = "Previous move undone.";
	public static final String MOVE_REDO_STATUS = "Previous move redone.";
	public static final String VICTORY_NOTIFICATION = "You win!";
	
	public static final String FINAL_MOVES = "Total Moves:";
	public static final String FINAL_TIME = "Total Time:";
	public static final String FINAL_SCORE = "Final Score:";
	
	public static final String FILE_NOT_FOUND_ERROR = "This file could not be found. Make sure the file exists and, if necessary, you have the correct permission to create and modify files.";
	public static final String CORRUPT_FILE_ERROR = "This file appears to be corrupt or not in the proper format. Please either delete it or make sure it is in the proper format and try again.";
	public static final String UNREADABLE_FILE_ERROR = "Could not read the file. Try again in a few moments.";
	public static final String UNWRITABLE_FILE_ERROR = "Could not write to the file. Try again in a few moments.";
	
	public static final String UNDO_BUTTON = "Undo";
	public static final String REDO_BUTTON = "Redo";
	public static final String HINT_BUTTON = "Hint";
	
	public static final String UNDO_BUTTON_CAPTION = "Undo your previous move.";
	public static final String REDO_BUTTON_CAPTION = "Redo your previous move.";
	public static final String CANT_UNDO_BUTTON_CAPTION = "You must make a move before you can undo one.";
	public static final String MOVING_BUTTON_CAPTION = "You cannot use this button while you are in the process of making a move.";
	public static final String UNDO_BUTTON_VICTORY_CAPTION = "You can't undo a move after the game is over. What would be the point of that?";
	public static final String HINT_BUTTON_VICTORY_CAPTION = "...Seriously? Still?";
	
	public static final String NEW_1_SCORE[] = {"Hot dang! New #1 high score.","You beat your old record and got a new #1 high score.","And I thought the old high score was untoppable. New #1 score."};
	public static final String NEW_SCORE[] = {"Nice one. New #` high score.","Impressive... very impressive. New #` high score.","You just earned yourself a new #` high score!","It appears I underestimated you. Nice new #` score you got there."};
	public static final String NO_NEW_SCORE[] = {"You did it.","Not bad at all.","Quite good, quite good."};
	
	public static final String HELP_MENU_SCROLL = "UP, DOWN: Scroll menu";
	public static final String HELP_MENU_SELECT = "LEFT-CLICK or ENTER: Select";
	public static final String HELP_MENU_BACK = "RIGHT-CLICK or ESC: Back";
	public static final String HELP_GAME_SCROLL = "UP, DOWN, LEFT, RIGHT: Scroll grid";
	public static final String HELP_GAME_SELECT = "LEFT-CLICK or ENTER: Select";
	public static final String HELP_GAME_MOVE = "LEFT-CLICK or ENTER: Move piece";
	public static final String HELP_GAME_UNDOREDO = "D: Undo/Redo move";
	public static final String HELP_GAME_HINT = "H: Give hint";
	public static final String HELP_MENU_QUIT = "RIGHT-CLICK or ESC: Quit to menu";
	public static final String HELP_MENU_DESELECT = "RIGHT-CLICK or ESC: Deselect piece";
	
}