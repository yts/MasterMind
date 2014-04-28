import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

/**
 * This JPanel contains the hint and clickable guess pegs, and supplies public methods to set the correct hint for the guess
 * @author Y. Stitzer
 * @version 12/20/2012
 */
public class AttemptRow extends JPanel
{
	private final int PEGS = 4;
	private CodePeg[] guess;
	private HintPeg[] hint;
	private int attempt;
	private MouseListener clickListener;
	
	/**
	 * Create an AttemptRow by adding the pegs to the panel. 
	 * @param number the row number
	 */
	public AttemptRow(int number)
	{
		attempt = number;
		
		JLabel numberLabel = createNumberLabel();
		JPanel guessPanel = createGuessPanel();
		JPanel hintPanel = createHintPanel();
		
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		add(numberLabel);
		add(guessPanel);
		add(hintPanel);		
	}
	
	/**
	 * Creates a JLabel to display the row number
	 * @return the JLabel with the row number
	 */
	private JLabel createNumberLabel()
	{
		//add a "0" to single-digit numbers so all rows are of equal width (purely for esthetics)
		String numberStr = Integer.toString(attempt);
		if (numberStr.length() < 2)
		{
			numberStr = "0" + attempt;
		}
		
		return new JLabel("<html><h3>" + numberStr + "</h3></html>");
	}
	
	/**
	 * Creates a JPanel containing the guess pegs in a row.
	 * @return the guess JPanel
	 */
	private JPanel createGuessPanel()
	{
		JPanel panel = new JPanel(new GridBagLayout());
		
		guess = new CodePeg[PEGS];
		final int PEG_SIZE = 30;
		final int PEG_BORDER = 4;
		
		GridBagConstraints constraint = new GridBagConstraints();
		//provide padding to display entire peg and add spacing between them
		final int HORIZONTAL_SPACING = 10;
		constraint.ipadx = PEG_SIZE + PEG_BORDER;
		constraint.ipady = PEG_SIZE + PEG_BORDER;
		constraint.insets = new Insets(0, HORIZONTAL_SPACING, 0, HORIZONTAL_SPACING);
		
		//initialize guess array with CodePeg objects and add each one to the panel
		for (int i = 0; i < PEGS; i++)
		{
			guess[i] = new CodePeg(PEG_SIZE, PEG_BORDER);
			constraint.gridx = i; //put in next column
			panel.add(guess[i], constraint);
		}
		
		return panel;
	}
	
	/**
	 * Creates a JPanel containing the 4 hint pegs in grid form.
	 * @return the hint JPanel
	 */
	private JPanel createHintPanel()
	{
			JPanel panel = new JPanel(new GridBagLayout());
			
			//initialize the hint array with hint pegs
			hint = new HintPeg[PEGS];
			final int PEG_SIZE = 11;
			final int PEG_BORDER = 4;
			for (int i = 0; i < PEGS; i++)
			{
				hint[i] = new HintPeg(PEG_SIZE, PEG_BORDER);
			}
			
			GridBagConstraints constraint = new GridBagConstraints();
			//provide padding to display entire peg
			constraint.ipadx = PEG_SIZE + PEG_BORDER; 
			constraint.ipady = PEG_SIZE + PEG_BORDER;
			constraint.insets = new Insets(0, 0, 0, 0);
			
			//place at top left
			constraint.gridx = 0;
			constraint.gridy = 0;
			constraint.insets.set(0, 1, 2, 2); //to space them out evenly
			panel.add(hint[0], constraint);
			
			//place at top right
			constraint.gridx = 1;
			constraint.insets.set(0, 1, 2, 1);
			panel.add(hint[1], constraint);
			
			//place at bottom left
			constraint.gridx = 0;
			constraint.gridy = 1;
			constraint.insets.set(1, 1, 0, 2);
			panel.add(hint[2], constraint); 
			
			//place at bottom right
			constraint.gridx = 1;
			constraint.insets.set(1, 1, 0, 1);
			panel.add(hint[3], constraint); 
			
			return panel;
	}
	
	/**
	 * Makes the guess pegs in this attempt row clickable or not clickable
	 * @param canEdit true if this row should be editable, false if not.
	 */
	public void setEditable(boolean canEdit)
	{
		if (canEdit)
		{
			createClickListener();
			for (Peg peg : guess)
			{
				peg.addMouseListener(clickListener);
				peg.setColor(getBackground()); //to show the user the pegs are now editable
			}
		}
		else
		{
			for (Peg peg : guess)
			{
				peg.removeMouseListener(clickListener);
			}
		}
	}

	/**
	 * creates the mouse listener to change the color of the guess pegs on clicks
	 */
	private void createClickListener()
	{
		class ClickListener implements MouseListener
		{
				public void mousePressed(MouseEvent event)
			{
			   CodePeg clickedPeg = (CodePeg) event.getSource();
			   
			   if (SwingUtilities.isLeftMouseButton(event))
			   {
			   	clickedPeg.setColor(clickedPeg.getValue() + 1); // sets peg as next color
			   }
			   else if (SwingUtilities.isRightMouseButton(event))
			   {
			   	clickedPeg.setColor(clickedPeg.getValue() - 1); // sets peg as previous color
			   }
			   
			}
			
			// Do-nothing methods
			public void mouseClicked(MouseEvent event) {}
			public void mouseReleased(MouseEvent event) {}
			public void mouseEntered(MouseEvent event) {}
			public void mouseExited(MouseEvent event) {}
		}
		
		clickListener = new ClickListener();
	}

	/**
	 * Checks whether or not all the guess pegs were used in the guess.
	 * @return true if the user made a complete guess, and false if not.
	 */
	public boolean isCompleteGuess()
	{
		for (CodePeg peg : guess)
		{
			if (!peg.wasSet())
			{
				return false;
			}
		}
		
		return true; //went through loop and didn't find an unset peg.
	}

	/**
	 * Gets the value of a guess peg
	 * @param index the index of the peg to check in the guess peg array
	 * @return the peg's value
	 */
	public int getGuessValue(int index)
	{
		return guess[index].getValue();
	}
	
	/**
	 * Sets a specific guess peg as checked or not checked
	 * @param index the index of the element in the guess peg array
	 * @param checked "true" to mark as checked, and "false" to mark as not checked
	 */
	public void setChecked(int index, boolean checked)
	{
		guess[index].setChecked(checked);
	}
	
	/**
	 * Checks whether or not a guess peg was already checked or not.
	 * @param index the index of the element in the guess peg array
	 * @return true if the peg was already checked, and false if not
	 */
	public boolean pegWasChecked(int index)
	{
		return guess[index].wasChecked();
	}
	
	/**
	 * Sets the colors of the hint pegs.
	 * @param correctPosition how many pegs of the guess code were of the correct color and position
	 * @param incorrectPosition how many pegs of the guess code were of the correct color but incorrect position
	 */
	public void setHint(int correctPosition, int incorrectPosition)
	{
		int index = 0;
		while (index < correctPosition)
		{
			hint[index].setAsCorrectPosition();
			index++;
		}
		
		incorrectPosition += index; 
		while (index < incorrectPosition)
		{
			hint[index].setAsIncorrectPosition();
			index++;
		}
	}
	
	/**
	 * Resets the AttemptRow to the state it was in upon instantiation.
	 */
	public void reset()
	{
		setEditable(false);
		
		for (CodePeg peg : guess)
		{
			peg.reset();
		}
		
		for (HintPeg peg : hint)
		{
			peg.reset();
		}
	}
	
	/**
	 * Gets a string describing the AttempRow and its pegs
	 * @return the description String
	 */
	public String toString()
	{
		return "Attempt=" + attempt + " " + "Guess=" + Arrays.toString(guess) + " Hint=" + Arrays.toString(hint);
	}
}
