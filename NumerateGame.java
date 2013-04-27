import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
  * The entire central Numerate game class. Represents what will run by default when the user wishes to run Numerate.
  */
public class NumerateGame extends Canvas
{
	// ==================================================
	// GAME CONSTANTS
	// ==================================================
	
	/** Window x-size. */
	public static final int WINDOW_X = 640;
	/** Window y-size. */
	public static final int WINDOW_Y = 480;
	
	/** Standard font for game menu items */
	public static final Font MENU_FONT = new GameFont("Arial", Font.BOLD, 12);
	/** Standard font for game grid tiles */
	public static final Font GRID_FONT = new GameFont("Arial", Font.BOLD, 14);
	/** Standard font for game logo */
	public static final Font LOGO_FONT = new GameFont("Courier New", Font.BOLD, 24);
	/** Standard font for game headings */
	public static final Font HEADING_FONT = new GameFont("Arial", Font.BOLD, 24);
	/** Standard font for game credits */
	public static final Font CREDITS_FONT = new GameFont("Arial", Font.PLAIN, 10);
	/** Standard font for menu captions */
	public static final Font CAPTION_FONT = new GameFont("Arial", Font.PLAIN, 10);
	/** Font for in-game headings (for time count, move count etc) */
	public static final Font GAME_HEADING_FONT = new GameFont("Arial",Font.BOLD, 16);
	/** Font for counter headings */
	public static final Font COUNTER_HEADING_FONT = new GameFont("Arial",Font.BOLD,12);
	/** Font for in-game headings (for time count, move count etc) */
	public static final Font COUNTER_TEXT_FONT = new GameFont("Arial",Font.BOLD, 16);
	/** Font for the status bar */
	public static final Font STATUS_BAR_FONT = new GameFont("Arial",Font.PLAIN,12);
	/** Font for game scores. */
	public static final Font SCORE_FONT = new GameFont("Arial",Font.BOLD,24);
	/** Small font for victory stats. */
	public static final Font SMALL_VICTORY_FONT = new GameFont("Arial",Font.BOLD,18);
	/** Large font for victory stats. */
	public static final Font LARGE_VICTORY_FONT = new GameFont("Arial",Font.BOLD,24);
	/** Button font. */
	public static final Font BUTTON_FONT = new GameFont("Arial",Font.BOLD, 12);
	
	/** Color for game menu items */
	public static final Color MENU_TEXT_COLOR = Color.WHITE;
	/** Color for selected game menu items */
	public static final Color SELECTED_MENU_TEXT_COLOR = Color.YELLOW;
	/** Color for game captions */
	public static final Color CAPTION_COLOR = Color.WHITE;
	/** Color for standard grid items */
	public static final Color GRID_TEXT_COLOR = Color.WHITE;
	/** Color for inactive grid items */
	public static final Color INACTIVE_GRID_TEXT_COLOR = Color.GRAY;
	/** Color for cursor-selected grid items */
	public static final Color GRID_CURSOR_TEXT_COLOR = Color.YELLOW;
	/** Color for selected grid items */
	public static final Color GRID_SELECTED_TEXT_COLOR = Color.BLUE;
	/** Color for highlighted grid items */
	public static final Color GRID_HIGHLIGHTED_TEXT_COLOR = Color.GREEN;
	/** Color for menu background */
	public static final Color MENU_BG_COLOR = new Color(0,0,32);
	/** Color for game background */
	public static final Color GAME_BG_COLOR = new Color(0,0,16);
	/** Color for grid background */
	public static final Color GRID_BG_COLOR = Color.BLACK;
	/** Color for highlighted grid background */
	public static final Color GRID_HIGHLIGHTED_BG_COLOR = new Color(0,8,64);
	/** Color for grid border */
	public static final Color GRID_BORDER_COLOR = new Color(0,0,128);
	/** Color for grid tile borders */
	public static final Color GRID_TILE_BORDER_COLOR = new Color(128,128,128);
	/** Color for counter borders */
	public static final Color COUNTER_BORDER_COLOR = new Color(64,64,64);
	/** Color for counter background */
	public static final Color COUNTER_BACKGROUND_COLOR = Color.BLACK;
	/** Color for counter heading */
	public static final Color COUNTER_HEADING_COLOR = new Color(255,64,0);
	/** Color for counter text */
	public static final Color COUNTER_TEXT_COLOR = Color.WHITE;
	/** Color for status bar border */
	public static final Color STATUS_BAR_BORDER_COLOR = new Color(192,192,192);
	/** Color for status bar background */
	public static final Color STATUS_BAR_BACKGROUND_COLOR = Color.BLACK;
	/** Color for status bar text */
	public static final Color STATUS_BAR_TEXT_COLOR = Color.WHITE;
	/** Color for positive status bar text */
	public static final Color STATUS_BAR_POSITIVE_TEXT_COLOR = Color.GREEN;
	/** Color for negative status bar text */
	public static final Color STATUS_BAR_NEGATIVE_TEXT_COLOR = Color.RED;
	/** Border color for buttons */
	public static final Color BUTTON_BORDER_COLOR = new Color(96,96,255);
	/** Background color for buttons */
	public static final Color BUTTON_BACKGROUND_COLOR = new Color(0,0,192);
	/** Background color for pressed buttons */
	public static final Color BUTTON_PRESSED_BACKGROUND_COLOR = new Color(0,0,255);
	/** Text color for buttons */
	public static final Color BUTTON_TEXT_COLOR = new Color(224,224,255);
	/** Text color for unenabled buttons */
	public static final Color BUTTON_UNENABLED_TEXT_COLOR = new Color(102,102,118);
	/** Text color for highlighted buttons */
	public static final Color BUTTON_HIGHLIGHTED_TEXT_COLOR = new Color(255,255,0);
	
	/** Padding below captions (in px). */
	public static final int CAPTION_PADDING_Y = 5;
	/** Padding to the left of captions (in px). */
	public static final int CAPTION_PADDING_X = 5;
	/** Padding between captions and help boxes (in px) */
	public static final int HELP_BOX_PADDING_Y = 22;
	/** Padding to the right of help boxes (in px) */
	public static final int HELP_BOX_PADDING_X = 10;
	/** Inner X-padding for help boxes (in px) */
	public static final int HELP_BOX_INNER_PADDING_Y = 7;
	/** Inner Y-padding for help boxes (in px) */
	public static final int HELP_BOX_INNER_PADDING_X = 10;
	/** Inner text padding for help boxes (in px) */
	public static final int HELP_BOX_INNER_TEXT_PADDING = 10;
	
	/** Constant for no game mode. */
	public static final int NO_MODE = -1;
	/** Constant for Line-Up mode. */
	public static final int LINEUP_MODE = 0;
	/** Constant for Alternate mode. */
	public static final int ALTERNATE_MODE = 1;
	/** Constant for Scatter mode. */
	public static final int SCATTER_MODE = 2;
	/** Constant for Summation mode. */
	public static final int SUMMATION_MODE = 3;
	
	/** Size of grid tiles. */
	public static final int GRID_TILE_SIZE = 32;
	/** Standard x padding for grid. */
	public static final int GRID_X_PADDING = 10;
	/** Standard y padding for grid. */
	public static final int GRID_Y_PADDING = 10;
	
	/** Game title. */
	public static final String GAME_TITLE = "Numerate";
	/** Game version. */
	public static final String GAME_VERSION = "2.00";
	
	/** Target game framerate. */
	public static final double FRAMERATE = 35.0;
	/** Number of cycles per second to work off of. */
	public static final int CYCLES_PER_SEC = 100;
	
	/** General game tasks branch constant */
	public static final int B_GENERAL_GAME_TASKS = 0;
	
	/** Game refresh timer constant */
	public static final int T_GAME_REFRESH = 0;
	
	// Field sizes:
	/** Long field size. */
	public static final int LONG_FIELD_SIZE     = 8;
	/** Integer field size. */
	public static final int INT_FIELD_SIZE      = 4;
	/** Short field size. */
	public static final int SHORT_FIELD_SIZE    = 2;
	/** Character field size. */
	public static final int CHAR_FIELD_SIZE     = 2;
	/** Byte field size. */
	public static final int BYTE_FIELD_SIZE     = 1;
	/** Boolean field size. */
	public static final int BOOLEAN_FIELD_SIZE  = 1;
	/** Double field size. */
	public static final int DOUBLE_FIELD_SIZE   = 8;
	/** Float field size. */
	public static final int FLOAT_FIELD_SIZE    = 4;
	
	/** Integer number formatter */
	public static NumberFormat INTEGER_FORMAT = NumberFormat.getIntegerInstance(Locale.US);

	
	// ==================================================
	// GAME VARIABLES
	// ==================================================
	
	/** Container for the window. */
	private JFrame container;
	
	/** Game render buffer strategy. */
	private BufferStrategy strategy;
	
	/** The current on-screen game */
	private GameState currentState;
	
	/** In debug mode? */
	private boolean debugMode;
	
	/** Game timer list. */
	private TimerList timers;
	
	/** Game mouse listener */
	private MouseManager mouseListener;
	/** Game key listener */
	private KeyManager keyListener;
	
	/** Games high score list. */
	private ScoresList scores;
	
	public class MouseManager implements MouseListener, MouseMotionListener
	{
		
		public void mouseDragged(MouseEvent e)
		{
			currentState.mouseDragged(e.getX(),e.getY(),e.getButton() == MouseEvent.BUTTON3);
		}
		
		public void mouseMoved(MouseEvent e)
		{
			currentState.mouseMoved(e.getX(),e.getY());
		}
		
		public void mouseClicked(MouseEvent e) 
		{
			currentState.mouseClicked(e.getButton() == MouseEvent.BUTTON3);
		}
		
		public void mouseEntered(MouseEvent e) 
		{
			currentState.mouseEntered(e.getX(),e.getY());
		}
		
		public void mouseExited(MouseEvent e) 
		{
			// mouse exited the component, so reset the x/y components
			currentState.mouseExited();
		}
		
		public void mousePressed(MouseEvent e) 
		{
			currentState.mousePressed(e.getButton() == MouseEvent.BUTTON3);
		}
		
		public void mouseReleased(MouseEvent e) 
		{
			currentState.mouseReleased(e.getButton() == MouseEvent.BUTTON3);
		}
	}
	
	public class KeyManager implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			currentState.keyPressed(e.getKeyCode());
		}
		
		public void keyReleased(KeyEvent e)
		{
			currentState.keyReleased(e.getKeyCode());
		}
		
		public void keyTyped(KeyEvent e)
		{
			currentState.keyTyped(e.getKeyChar());
		}
	} 
	
	// ==================================================
	// UTILITY METHODS
	// ==================================================
	
	/** Static function for returning the current system time (in seconds). Recommended ONLY for time comparison! */
	public static double sysTime()
	{
		return (double)System.nanoTime()/1000000000.0;
	}
	
	/** Static function for a string array with one parameter. */
	public static String[] a(String a1)
	{
		String[] a = {a1};
		return a;
	}
	
	/** Static function for a string array with two parameters. */
	public static String[] a(String a1, String a2)
	{
		String[] a = {a1,a2};
		return a;
	}
	
	/** Static function for a string array with three parameters. */
	public static String[] a(String a1, String a2, String a3)
	{
		String[] a = {a1,a2,a3};
		return a;
	}
	
	/** Static function for a string array with four parameters. */
	public static String[] a(String a1, String a2, String a3, String a4)
	{
		String[] a = {a1,a2,a3,a4};
		return a;
	}
	
	/** Determine the number of digits in a base 10 integer. */
	public static int getDigits(int number)
	{
		int digits = 1;
		while ((number /= 10) != 0)
		{
			digits++;
		}
		return digits;
	}
	
	// ==================================================
	// MAIN CONSTRUCTOR METHOD
	// ==================================================
	
	/** Standard constructor. */
	public NumerateGame(boolean d_debugMode)
	{
		
		// create a frame to contain our game
		container = new JFrame(GAME_TITLE);
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = new JPanel(new GridLayout(2,1));
		panel.setPreferredSize(new Dimension(WINDOW_X,WINDOW_Y));
		panel.setLayout(null);
		
		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,WINDOW_X,WINDOW_Y);
		
		// now add this canvas to the main panel
		panel.add(this);
		
		// now add the panel to the frame
		container.add(panel);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// finally make the window visible 
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// request the focus so key events come to us
		requestFocus(true);

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// now create our timers...
		timers = new TimerList(2);
		timers.addTimer(B_GENERAL_GAME_TASKS,T_GAME_REFRESH, new Timer("refresh",1/FRAMERATE,0,false));
		
		// now set up the scores list
		try
		{
			scores = new ScoresList();
			scores.read();
		}
		catch (Exception X)
		{
			System.out.println(X.getMessage());
		}
		
		// initiate the menu...
		currentState = new GameMenu(scores);
		
		// now add a mouse listener
		mouseListener = new MouseManager();
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		
		// now, a keyboard listener
		keyListener = new KeyManager();
		addKeyListener(keyListener);
		
		// now! set debug mode (if necessary)
		debugMode = d_debugMode;
	}
	
	// ==================================================
	// MAIN GAME LOOP
	// ==================================================
	
	/** Game loop. */
	public void gameLoop()
	{
		GameGraphics graphics = new GameGraphics((Graphics2D) strategy.getDrawGraphics(),strategy);
		double time = sysTime(), dtime = 0.0;
		boolean needsCompleteRedraw = false; // set this to TRUE when we lose focus of our window and need to redraw it
		
		while (true)
		{
			dtime = sysTime()-time;
			time += dtime;
			timers.increment(dtime);
			
			currentState = currentState.nextState;
			
			if (currentState == null)
			{
				// we have a null state... so quit out please
				removeMouseListener(mouseListener);
				removeMouseMotionListener(mouseListener);
				removeKeyListener(keyListener);
				break;
			}
			
			currentState.elapseTime(dtime);
			
			if (!timers.isActive(B_GENERAL_GAME_TASKS, T_GAME_REFRESH) || timers.triggered(B_GENERAL_GAME_TASKS, T_GAME_REFRESH))
			{
				if (!timers.isActive(B_GENERAL_GAME_TASKS, T_GAME_REFRESH))
				{
					// if the screen refresh timer is not already enabled, enable it (usually done at startup)
					timers.setIsActive(B_GENERAL_GAME_TASKS, T_GAME_REFRESH,true);
				}
				
				if (!container.hasFocus() && !hasFocus())
				{
					graphics.needsCompleteRedraw = true;
				}
				
				currentState.draw(graphics);
				
				graphics.g.dispose();
				graphics.s.show();
				
			}
			
			try { Thread.sleep(1000/CYCLES_PER_SEC); } catch (Exception e) {}
			
		}
		
		setVisible(false);
		container.setVisible(false);
		System.exit(0);
	}
	
	/** Main method. */
	public static void main(String args[])
	{
		boolean debugMode = false;
		
		NumerateGame game = new NumerateGame(debugMode);
		game.gameLoop();
	}
}