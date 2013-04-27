import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
  * Game font class. This class is identical to the Font class built into
  * Java, except it can take it an array of font names to determine the first
  * one that the system can use.
  */
public class GameFont extends Font
{
	/** List of available font family names. */
	private static ArrayList<LinkedList<String>> availableFonts = null;
	/** Number of array indeces available for the available fonts array */
	private static final int FONT_ARRAY_INDECES = 25;
	/** Number of characters to hash in array. */
	private static final int HASH_LENGTH = 4;
	
	/** Grabs a string array and uses the first available font face */
	private static String firstAvailableFontFace(String[] faces)
	{
		if (availableFonts == null)
		{
			// null font array - set it up
			availableFonts = new ArrayList<LinkedList<String>>(FONT_ARRAY_INDECES);
			for (int i = 0; i < FONT_ARRAY_INDECES; i++)
			{
				availableFonts.add(new LinkedList<String>());
			}
			
			String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			for (int i = 0; i < fonts.length; i++)
			{
				int hash = Math.abs((int)HashFunctions.RSHash(fonts[i],HASH_LENGTH));
				availableFonts.get(hash%FONT_ARRAY_INDECES).add(fonts[i]);
			}
		}
		
		for (int i = 0; i < faces.length; i++)
		{
			int hash = Math.abs((int)HashFunctions.RSHash(faces[i],HASH_LENGTH));
			Iterator it = availableFonts.get(hash%FONT_ARRAY_INDECES).iterator();
			while (it.hasNext())
			{
				// now cycle through the linked list
				String face = (String)it.next();
				if (face.equals(faces[i]))
				{
					return face;
				}
			}
		}
		return faces[faces.length-1];
	}
	
	/** Standard, array-less constructor. */
	public GameFont(String face, int style, int size)
	{
		super(face,style,size);
	}
	
	/** Standard, array-filled constructor */
	public GameFont(String faces[], int style, int size)
	{
		super(firstAvailableFontFace(faces),style,size);
	}
}