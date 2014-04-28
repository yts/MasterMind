import java.awt.Color;
import java.io.Serializable;

/**
 * Objects of the CodePeg class can be used to display and test one peg of the guessed or answer code.
 * @author Y. Stitzer
 * @version 12/20/2012
 */
public class CodePeg extends Peg
{
	private final int PRE_CYCLE = -1;
	private final int NOT_SET = 0;
	private final int BLUE = 1;
	private final int PINK = 2;
	private final int GREEN = 3;
	private final int MAGENTA = 4;
	private final int CYAN = 5;
	private final int ORANGE = 6;
	private final int POST_CYCLE = 7;

	private int value;
	
	private boolean checked;
	
	/**
	 * construct an empty CodePeg object with a given diameter and border size.
	 * @param diameter the diameter of the peg
	 * @param border the border of the peg
	 */
	public CodePeg(int diameter, int border)
	{
		super(diameter, border);
		checked = false;
		value = NOT_SET;
	}

	/**
	 * Sets the the value and displays the color representation of a given integer corresponding to a color.
	 * @param inValue the color (in integer form) to set this CodePeg as
	 */
	public void setColor(int inValue)
	{	
		if (inValue == NOT_SET ||
				inValue == POST_CYCLE)
	   {
			setColor(getBackground());
			value = NOT_SET;
	   }
	   else if (inValue == BLUE)
		{
			setColor(Color.BLUE);
			value = BLUE;
		}
		else if (inValue == PINK)
		{
			setColor(Color.PINK);
			value = PINK;
		}
		else if (inValue == GREEN)
		{
			setColor(Color.GREEN);
			value = GREEN;
		}
		else if (inValue == MAGENTA)
		{
			setColor(Color.MAGENTA);
			value = MAGENTA;
		}
		else if (inValue == CYAN)
		{
			setColor(Color.CYAN);
			value = CYAN;
		}
		else if (inValue == ORANGE ||
				inValue == PRE_CYCLE)
		{
			setColor(Color.ORANGE);
			value = ORANGE;
		}
	   
	}
	
	/**
	 * gets the current integer value of the peg
	 * @return the peg's integer value
	 */
	public int getValue()
	{
		return value;
	}
	
	/**
	 * Checks whether or not the value of this peg has been set or not by the user
	 * @return true if it was set, and false if not.
	 */
	public boolean wasSet()
	{
		return value != NOT_SET;
	}
	
	/**
	 * Sets this peg as already checked to avoid making too many hint pegs, or as unchecked to reuse peg later. 
	 * @param inChecked "true" if peg shouldn't be checked again, and "false" if should be used
	 */
	public void setChecked(boolean inChecked)
	{
		checked = inChecked;
	}
	
	/**
	 * Checks whether this peg was already checked or not.
	 * @return true if it wasn't checked, and false if it was.
	 */
	public boolean wasChecked()
	{
		return checked;
	}
	
	/**
	 * Gets a string describing the current color of this peg
	 * @return the string with the peg's color
	 */
	public String toString()
	{
		if (value == BLUE)
		{
			return "Blue";
		}
		else if (value == PINK)
		{
			return "Pink";
		}
		else if (value == GREEN)
		{
			return "Green";
		}
		else if (value == MAGENTA)
		{
			return "Magenta";
		}
		else if (value == CYAN)
		{
			return "Cyan";
		}
		else if (value == ORANGE)
		{
			return "Orange";
		}
		else
		{
			return "Blank";
		}
	}
	
	/**
	 * Resets the values of this CodePeg to those of a new CodePeg object
	 */
	public void reset()
	{
		checked = false;
		value = NOT_SET;
		super.reset();
	}

}
