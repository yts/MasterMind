import java.awt.Color;
import java.io.Serializable;

/**
 * Objects of this class can be used to display hint pegs for the Mastermind game
 * @author Y. Stitzer
 * @version 12/20/2012
 */
public class HintPeg extends Peg
{
	private final Color CORRECT_POSITION = Color.BLACK;
   private final Color INCORRECT_POSITION = Color.WHITE;

   /**
    * Constructs an empty hint peg with a given width and border size
    * @param width the width of the peg
    * @param border the border size of the peg
    */
	public HintPeg(int width, int border)
	{
		super(width, border);
	}
	
	/**
	 * Sets the peg to display the correct position hint color
	 */
	public void setAsCorrectPosition()
	{
		setColor(CORRECT_POSITION);
	}
	
	/**
	 * Sets the peg to display the correct color, but incorrect position hint color
	 */
	public void setAsIncorrectPosition()
	{
		setColor(INCORRECT_POSITION);
	}
	
	/**
	 * Gets the current color of this HintPeg
	 * @return the color string
	 */
	public String toString()
	{
		if (getColor().equals(CORRECT_POSITION))
		{
			return "Black";
		}
		else if (getColor().equals(INCORRECT_POSITION))
		{
			return "White";
		}
		else
		{
			return "Blank";
		}
	}

}
