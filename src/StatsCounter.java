import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * This class keeps the score of played games
 * @author Y. Stitzer
 * @version 12/20/2012
 */
public class StatsCounter
{
	private final String FILENAME = "resources/Statistics.txt";
	private Scanner reader;
	private PrintWriter writer;
	private int[] stats;
	public static final int VALUES = 3;
	public static final int WINS = 0;
	public static final int LOSSES = 1;
	public static final int INCOMPLETES = 2;
	
	public StatsCounter()
	{
		stats = new int[VALUES];
	}
	
	/**
	 * gets the values from the file
	 * @throws IOException
	 */
	private void readFile() throws IOException
	{
		reader = new Scanner(new File(FILENAME));

		try
		{
			try
			{
				stats[WINS] = reader.nextInt();
				stats[LOSSES] = reader.nextInt();
				stats[INCOMPLETES] = reader.nextInt();
			}
			catch (Exception ex)
			{
				throw new InvalidFileException("File doesn't contain " + VALUES + " integer values.");
			}
				
			if (reader.hasNext())
			{
				throw new InvalidFileException("File has more than " + VALUES + " values.");
			}
		}
		finally
		{
			if (reader != null)
			{
				reader.close();
			}
		}

	}
	
	/**
	 * Gets the statistics
	 * @return the array with the statistics
	 * @throws IOException
	 */
	public int[] getStats() throws IOException
	{
		readFile();
		return stats;
	}
	
	/**
	 * adds a win count to the file
	 * @throws IOException
	 */
	public void addWin() throws IOException
	{
		readFile();
		stats[WINS]++;
		updateFile();
	}
	
	/**
	 * adds a loss count to the file
	 * @throws IOException
	 */
	public void addLoss() throws IOException
	{
		readFile();
		stats[LOSSES]++;
		updateFile();
	}
	
	/**
	 * adds an incomplete count to the file
	 * @throws IOException
	 */
	public void addIncomplete() throws IOException
	{
		readFile();
		stats[INCOMPLETES]++;
		updateFile();
	}

	/**
	 * rewrites the statistics
	 * @throws FileNotFoundException
	 */
	private void updateFile() throws FileNotFoundException
	{
		writer = new PrintWriter(new File(FILENAME));
		writer.println(stats[WINS]);
		writer.println(stats[LOSSES]);
		writer.println(stats[INCOMPLETES]);
		writer.close();
	}
}
