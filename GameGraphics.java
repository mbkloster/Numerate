import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
  * Game graphics class. <br /><br />
  *
  * In effect, this class acts as a container for all graphics-related
  * things passed to game states. Includes graphics and strategy objects.
  */
public class GameGraphics
{
	/** Graphics object. */
	public Graphics2D g;
	/** Stategy object. */
	public BufferStrategy s;
	
	/** Help lines */
	private LinkedList<String> helpLines;
	/** Help line height */
	private int helpLineHeight;
	/** Help line width */
	private int helpLineWidth;
	/** Help box refreshed? Set this to false when switching between different menu contexts */
	private boolean helpBoxRefreshed;
	
	/** Saved color. */
	private Color savedColor;
	/** Saved font. */
	private Font savedFont;
	
	/** Needs complete redraw? */
	public boolean needsCompleteRedraw = false;
	
	/** Just a constructor. */
	public GameGraphics(Graphics2D d_g, BufferStrategy d_s)
	{
		g = d_g;
		s = d_s;
		
		helpLines = new LinkedList<String>();
		helpBoxRefreshed = false;
	}
	
	/** Saves the existing color into a reference variable. */
	public void saveColor()
	{
		savedColor = g.getColor();
	}
	
	/** Saves the existing font into a reference variable. */
	public void saveFont()
	{
		savedFont = g.getFont();
	}
	
	/** Saves the color AND font at the same time */
	public void saveColorAndFont()
	{
		saveColor();
		saveFont();
	}
	
	/** Reverts to the saved color. */
	public void revertColor()
	{
		if (savedColor != null)
		{
			g.setColor(savedColor);
		}
	}
	
	/** Reverts to the saved font. */
	public void revertFont()
	{
		if (savedFont != null)
		{
			g.setFont(savedFont);
		}
	}
	
	/** Reverts the color AND font at the same time */
	public void revertColorAndFont()
	{
		revertColor();
		revertFont();
	}
	
	/** Clears the list of help lines. */
	public void clearHelpLines()
	{
		helpLines.clear();
	}
	
	/** Adds a help line. */
	public void addHelpLine(String line)
	{
		helpLines.add(line);
	}
	
	/** Set the help box refreshed status */
	public void setHelpBoxRefreshed(boolean d_helpBoxRefreshed)
	{
		helpBoxRefreshed = d_helpBoxRefreshed;
	}
	
	/** Accessor method for the help box refreshed status */
	public boolean isHelpBoxRefreshed()
	{
		return helpBoxRefreshed;
	}
	
	/** Updates the dimensions of the help box. */
	public void updateHelpBoxDimensions()
	{
		saveFont();
		g.setFont(NumerateGame.CAPTION_FONT);
		helpLineHeight = helpLines.size()*g.getFontMetrics().getMaxDescent();
		helpLineHeight += Math.max(helpLines.size()-1,0)*NumerateGame.HELP_BOX_INNER_TEXT_PADDING;
		
		helpLineWidth = 0;
		Iterator it = helpLines.iterator();
		while (it.hasNext())
		{
			String current = (String)it.next();
			if (g.getFontMetrics().stringWidth(current) > helpLineWidth)
			{
				helpLineWidth = g.getFontMetrics().stringWidth(current);
			}
		}
		
		revertFont();
	}
	
	/** Draws the help box. */
	public void drawHelpBox()
	{
		if (helpLines.size() > 0)
		{
			int x = NumerateGame.WINDOW_X-NumerateGame.HELP_BOX_PADDING_X-NumerateGame.HELP_BOX_INNER_PADDING_X*2-helpLineWidth-2;
			int y = NumerateGame.WINDOW_Y-NumerateGame.HELP_BOX_PADDING_Y-NumerateGame.HELP_BOX_INNER_PADDING_Y*2-helpLineHeight-2;
			
			saveColorAndFont();
			g.setColor(NumerateGame.CAPTION_COLOR);
			g.setFont(NumerateGame.CAPTION_FONT);
			
			// first, draw border...
			drawBorder(x,y,x+2+helpLineWidth+NumerateGame.HELP_BOX_INNER_PADDING_X*2,y+2+helpLineHeight+NumerateGame.HELP_BOX_INNER_PADDING_Y*2);
			
			x += 1+NumerateGame.HELP_BOX_INNER_PADDING_X;
			y += 1+NumerateGame.HELP_BOX_INNER_PADDING_Y*7/4;
			
			Iterator it = helpLines.iterator();
			while (it.hasNext())
			{
				String currentLine = (String)it.next();
				g.drawString(currentLine,x,y);
				//System.out.println("old y: "+y);
				y += g.getFontMetrics().getMaxDescent()+NumerateGame.HELP_BOX_INNER_TEXT_PADDING;
				//System.out.println("new y: "+y);
			}
		}
	}
	
	/** Do a partial redraw of the screen */
	public void partialRedraw()
	{
		g.clearRect(0,0,NumerateGame.WINDOW_X,NumerateGame.WINDOW_Y);
	}
	
	/** Do a complete redraw of the screen */
	public void completeRedraw()
	{
		g = (Graphics2D) s.getDrawGraphics();
	}
	
	/** Just do a redraw -- figure out if it's partial or complete based on the context */
	public void redraw()
	{
		if (needsCompleteRedraw)
		{
			completeRedraw();
			needsCompleteRedraw = false;
		}
		else
		{
			partialRedraw();
		}
	}
	
	/** Render a caption for the bottom of the screen. */
	public void drawCaption(String caption)
	{
		Font previousFont = g.getFont();
		Color previousColor = g.getColor();
		g.setFont(NumerateGame.CAPTION_FONT);
		g.setColor(NumerateGame.CAPTION_COLOR);
		g.drawString(caption,NumerateGame.CAPTION_PADDING_X,NumerateGame.WINDOW_Y-g.getFont().getSize()/2-NumerateGame.CAPTION_PADDING_Y);
		g.setFont(previousFont); // revert the font
		g.setColor(previousColor); // revert the color
	}
	
	/** Render a border around a rectangular area. */
	public void drawBorder(int x1, int y1, int x2, int y2)
	{
		g.drawLine(x1, y1, x1, y2);
		g.drawLine(x1, y1, x2, y1);
		g.drawLine(x1, y2, x2, y2);
		g.drawLine(x2, y1, x2, y2);
	}
}