import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;

/**
  * High scores list. <br /><br />
  * Keeps track of a list of all high scores. Updates and manages them dynamically in a specified binary file.
  */
public class ScoresList
{
	/** The class representing a single score in the scores list. */
	public class Score
	{
		/** The name associated with the score. */
		private String name;
		
		/** The score associated with the score. */
		private int score;
		
		/** The code associated with the score. Used for verifying codes in memory. */
		private long codeA;
		
		/** Standard constructor. */
		public Score(String d_name, int d_score)
		{
			name = d_name;
			score = d_score;
			codeA = codeA(d_score);
		}
		
		/** Accessor method for name */
		public String getName()
		{
			return name;
		}
		
		/** Acessor method for score. */
		public int getScore()
		{
			return score;
		}
		
		/** Verify the save game score by checking the code */
		public boolean verify()
		{
			return codeA(score) == codeA;
		}
	}
	
	/** Code convertor A for scores */
	public static long codeA(int score)
	{
		long code = score*score;
	 	code -= score*2;
	 	code += 4937;
	 	code /= 2;
	 	code += 23391;
	 	return code;
	}
	
	/** Code convertor B for scores */
	public static long codeB(int score)
	{
		long code = (score + 3)*(score + 3)*(score + 3);
		code -= 55393;
		code *= 3;
		code -= 2323;
		code /= 2;
		code -= score;
		return code;
	}
	
	/** Code convertor for a string - used for names */
	public static long code(String name)
	{
		long code = 298483231;
		for (int i = 0; i < name.length(); i++)
		{
			code += name.charAt(i)*name.charAt(i)*name.charAt(i)-name.charAt(i);
			if (i > 0)
			{
				code -= (name.charAt(i-1)%23)*2;
			}
		}
		return code;
	}
	
	/** The default filename for scores to be stored in. */
	public static final String SCORES_FILENAME = "scores.nmd";
	
	/** The number of high scores listed for each difficulty */
	public static final int SCORES_PER_LIST = 5;
	
	/** The INT value expected at the beginning of the scores file. */
	private static final int INITIAL_INT = 2437;
	/** The LONG value expected after the INT value. */
	private static final long INITIAL_LONG = 393249234;
	
	/** The max length of a string in the scores list */
	public static final int MAX_NAME_LENGTH = 15;
	
	/** The binary file scores are stored in. */
	private RandomAccessFile file;
	
	/** Array of scores - one linked list for every difficulty */
	private ArrayList<LinkedList<Score>> scores;
	
	/** Standard constructor. */
	public ScoresList(String filename) throws Exception
	{
		try
		{
			file = new RandomAccessFile(filename, "rw");
		}
		catch (FileNotFoundException X)
		{
			throw new Exception (TextBank.FILE_NOT_FOUND_ERROR);
		}
		
		scores = new ArrayList<LinkedList<Score>>(TextBank.DIFFICULTIES_SHORT.length);
		for (int i = 0; i < TextBank.DIFFICULTIES_SHORT.length; i++)
		{
			scores.add(new LinkedList<Score>());
		}
		
		scores.get(0).add(new Score("Jesus",1000000));
	}
	
	/** Constructor without a filename - uses the default filename. */
	public ScoresList() throws Exception
	{
		this(SCORES_FILENAME);
	}
	
	/** See if a new score will fit in the scores list. If true, returns the placement. If false, returns 0. */
	public int scoreFits(int difficulty, int score)
	{
		LinkedList<Score> list = scores.get(difficulty);
		Iterator it = list.iterator();
		int i = 1;
		while (it.hasNext() && i < SCORES_PER_LIST)
		{
			Score currentScore = (Score)it.next();
			if (currentScore.getScore() < score)
			{
				return i;
			}
			i++;
		}
		
		if (i < SCORES_PER_LIST)
		{
			return i;
		}
		
		return 0;
	}
	
	/** Place a new score on the scores list. */
	public void addScore(String name, int difficulty, int score, int placement)
	{
		LinkedList<Score> list = scores.get(difficulty);
		Score newScore = new Score(name, score);
		if (placement > 0)
		{
			list.add(placement-1,newScore);
		}
		else
		{
			// placement either 0 or negative, so tack it on to the end of the list
			list.add(newScore);
		}
		if (list.size() > SCORES_PER_LIST)
		{
			// we have one too many items on the list... so remove dat shit
			list.remove(SCORES_PER_LIST);
		}
	}
	
	// Files with scores are in this format:
	// initial int + initial long, then scores
	// boolean TRUE = new score for this difficulty
	// boolean FALSE = switch difficulty up
	// scores are in this format:
	// LONG name hash
	// STRING[21] name
	// LONG code A
	// LONG code B
	// INT score
	
	/** Read the file. */
	public void read() throws Exception
	{
		if (file != null)
		{
			try
			{
				if (file.length() >= NumerateGame.LONG_FIELD_SIZE + NumerateGame.INT_FIELD_SIZE)
				{
					if (file.readInt() != INITIAL_INT || file.readLong() != INITIAL_LONG)
					{
						throw new Exception(TextBank.CORRUPT_FILE_ERROR);
					}
					
					int difficulty = 0;
					while (file.getFilePointer() < file.length())
					{
						if (file.readBoolean())
						{
							if (file.length() >= file.getFilePointer() + NumerateGame.CHAR_FIELD_SIZE*(MAX_NAME_LENGTH+1) + NumerateGame.LONG_FIELD_SIZE*3 + NumerateGame.INT_FIELD_SIZE)
							{
								long nameCode = file.readLong();
								String name = "";
								for (int i = 0; i < MAX_NAME_LENGTH+1; i++)
								{
									char c = file.readChar();
									if (c > 0)
									{
										name += c;
									}
									else
									{
										// found a 0-bit... so quit out and adjust the pointer
										file.seek(file.getFilePointer()+(MAX_NAME_LENGTH-i)*NumerateGame.CHAR_FIELD_SIZE);
										break;
									}
								}
								long codeA = file.readLong();
								long codeB = file.readLong();
								int score = file.readInt();
								if (codeA(score) == codeA && codeB(score) == codeB)
								{
									// score checks out
									Score newScore = new Score(name, score);
									scores.get(difficulty).add(newScore);
								}
							}
							else
							{
								// nothing more to read... so break out of the loop
								break;
							}
						}
						else
						{
							difficulty++;
							if (difficulty >= scores.size())
							{
								// we've run out of difficulties to fill up
								break;
							}
						}
					}
				}
			}
			catch (IOException X)
			{
				throw new Exception(TextBank.UNREADABLE_FILE_ERROR);
				
			}
		}
	}
	
	/** Save the file. */
	public void save() throws Exception
	{
		try
		{
			file.setLength(0);
			file.seek(0);
			file.writeInt(INITIAL_INT);
			file.writeLong(INITIAL_LONG);
			
			for (int i = 0; i < scores.size(); i++)
			{
				LinkedList<Score> currentList = scores.get(i);
				Iterator it = currentList.iterator();
				while (it.hasNext())
				{
					Score score = (Score)it.next();
					if (score.verify())
					{
						file.writeBoolean(true);
						
						file.writeLong(code(score.getName()));
						for (int j = 0; j < (MAX_NAME_LENGTH+1); j++)
						{
							if (score.getName().length() > j)
							{
								file.writeChar(score.getName().charAt(j));
							}
							else
							{
								file.writeChar(0);
							}
						}
						file.writeLong(codeA(score.getScore()));
						file.writeLong(codeB(score.getScore()));
						file.writeInt(score.getScore());
					}
				}
				file.writeBoolean(false);
			}
		}
		catch (IOException X)
		{
			throw new Exception(TextBank.UNWRITABLE_FILE_ERROR);
		}
	}
	
	public static void main(String args[])
	{
		try
		{
			ScoresList s = new ScoresList();
			s.read();
		}
		catch (FileNotFoundException X)
		{
			System.out.println("FILE NOT FOUND: "+X.toString());
		}
		catch (Exception X)
		{
			System.out.println("ERROR: "+X.toString());
		}
		
	}
}