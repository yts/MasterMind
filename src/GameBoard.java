import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * This JPanel contains the main elements of the Mastermind game, and allows the user to make a guesses and test them out. 
 * @author Y. Stitzer
 * @version 12/20/2012
 */
public class GameBoard extends JPanel implements Serializable
{
	private final int ATTEMPTS = 10;
	private final int CODE_LENGTH = 4;
	private final int PEG_WIDTH = 30;
	private final int PEG_BORDER = 4;
	private AttemptRow[] attempts;
	private CodePeg[] code;
	private int attempted;
   private GameState state;
   private StatsCounter stats;
	
	private enum GameState
	{
		WON, LOST, IN_PROGRESS
	}
	
	/**
	 * Constructs a new GameBoard object to start a new game of Mastermind
	 */
	public GameBoard()
	{
		super(new BorderLayout());
		
		state = GameState.IN_PROGRESS;
		stats = new StatsCounter();
		
		setCode();
		
      JPanel attemptsPanel = createAttemptsPanel();
      
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(createGuessButton());

		add(attemptsPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		attempted = 0;
		attempts[attempted].setEditable(true); //prepares for first round
	}

	/**
	 * Sets the answer code with random values
	 */
	private void setCode()
	{
		code = new CodePeg[CODE_LENGTH];
		final int COLORS = 6;
		Random rand = new Random();

		for (int i = 0; i < code.length; i++)
		{
			code[i] = new CodePeg(PEG_WIDTH, PEG_BORDER);
			code[i].setColor(rand.nextInt(COLORS) + 1);
		}
		
//		System.out.println(Arrays.toString(code)); //for testing purposes
		
	}

	/**
	 * Initializes "attempts" and creates the panel containing all of the AttemptRow objects.
	 * @return the JPanel with the ArremptRows
	 */
	private JPanel createAttemptsPanel()
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 0)); //specialized layout to minimize vertical gap

		attempts = new AttemptRow[ATTEMPTS];
		
		for (int i = ATTEMPTS - 1; i >= 0; i--) //add each attempt row, starting from the bottom
		{
			attempts[i] = new AttemptRow(i + 1);
			panel.add(attempts[i]);
		}
		
		return panel;
	}
	
	/**
	 * Creates the guess button and adds the action listener to it.
	 * @return the guess button
	 */
	public JButton createGuessButton()
	{
		JButton button = new JButton("Guess It!");
		
		class ButtonListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{	
				if (state == GameState.IN_PROGRESS) //only test guess if game hasn't ended yet
				{
					if (attempts[attempted].isCompleteGuess()) //make sure all pegs are selected
					{
						attempts[attempted].setEditable(false);
						testGuess();
					}
					else
					{
						showIncompleteError();
					}
				}
				else if (state == GameState.WON)
				{
					showWonMessage();
				}
				else // state == lost
				{
					showLostMessage();
				}
			}
		}
		
		button.addActionListener(new ButtonListener());
		
		return button;
	}

	/**
	 * Tests the user's last guess.
	 */
	public void testGuess()
	{
		AttemptRow attempt = attempts[attempted]; //the attempt being worked with
		int correctPosition = 0;
		int incorrectPosition = 0;
		
		for (int i = 0; i < CODE_LENGTH; i++) //go through guessed code
		{
			if (!attempt.pegWasChecked(i)) //to avoid multiple hint pegs if matching color is in the guess code multiple times
			{
				if (attempt.getGuessValue(i) == code[i].getValue()) //correct position
				{
					correctPosition++;
					attempt.setChecked(i, true); //don't use guess peg again
					code[i].setChecked(true); //don't use answer peg again
				}
				else 
				{
					for (int j = 0; j < CODE_LENGTH; j++) //find incorrect position pegs
					{
					   //first condition is to avoid multiple hint pegs if matching color is in answer code multiple times
						if (!(code[j].wasChecked() || attempt.pegWasChecked(i)) && attempt.getGuessValue(i) == code[j].getValue())
						{
							if (!(attempt.getGuessValue(j) == code[j].getValue())) //correct position hints take precedence
							{
								incorrectPosition++;
								attempt.setChecked(i, true);
								code[j].setChecked(true);
							}
							else
							{
								correctPosition++;
								attempt.setChecked(j, true);
								code[j].setChecked(true);
							}
						}
					}
				}
			}
		}
		
		attempt.setHint(correctPosition, incorrectPosition);
		
		attempted++; //attempt was made
		
		setNextState(correctPosition);	
	}

	/**
	 * Determines if game was won, lost, or neither, and takes the appropriate action for each
	 * @param correctPosition the number of guessed pegs which were the correct color and position
	 */
	private void setNextState(int correctPosition)
	{
		if (correctPosition == CODE_LENGTH) //all guess pegs were correct
		{
			state = GameState.WON;
			try
			{
				stats.addWin();
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Unable to update win count due to file error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			showWonMessage();
		}
		else if (attempted == ATTEMPTS) //user made last allowed attempt
		{
			try
			{
				stats.addLoss();
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Unable to update loss count due to file error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			showLostMessage();
		}
		else
		{
		   //prepare answer code for testing of the next guess
			for (int i = 0; i < 4; i++)
			{
				code[i].setChecked(false);
			}
			//let user guess again
			attempts[attempted].setEditable(true);
		}
		
	}

	/**
	 * Displays a message notifying the user of a lost game and revealing the answer code.
	 * The user is also offered the option to start a new game.
	 */
	public void showLostMessage()
	{
		state = GameState.LOST;
		
		JPanel message = new JPanel(new GridLayout(0, 1)); //to be displayed in JOptionPane
		
		JLabel statement = new JLabel("You didn't guess the code. The correct code was:");
		statement.setHorizontalAlignment(JLabel.CENTER);
		message.add(statement);
		
		//creates panel containing the answer code
		JPanel codePanel = new JPanel(new GridBagLayout());
      GridBagConstraints constraint = new GridBagConstraints();
		final int SPACING = 5;
		constraint = new GridBagConstraints();
		constraint.ipadx = PEG_WIDTH + PEG_BORDER;
		constraint.ipady = PEG_WIDTH + PEG_BORDER;
		constraint.insets = new Insets(0, SPACING, 0, SPACING);
		for (int i = 0; i < code.length; i++)
		{
			constraint.gridx = i;
			codePanel.add(code[i], constraint); //place them in a row
		}
		message.add(codePanel);
		
		JLabel question = new JLabel("Would you like to play again?");
		question.setHorizontalAlignment(JLabel.CENTER);
		message.add(question);
		
		int selection = JOptionPane.showOptionDialog(this, message, "Game Over", 
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
		
		if (selection == JOptionPane.YES_OPTION)
		{
			reset();
		}
	}
	
	/**
	 * Displays a congratulatory dialog and offers the user to start a new game 
	 */
	private void showWonMessage()
	{
		JLabel message = new JLabel("You guessed the code! Would you like to start a new game?");
		message.setHorizontalAlignment(JLabel.CENTER);
		
		int selection = JOptionPane.showOptionDialog(this, message, "Congratulations!", 
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
		
		if (selection == JOptionPane.YES_OPTION)
		{
			reset();
		}
	}

	/**
	 * Displays a dialog window notifying the user of an incomplete guess
	 */
	private void showIncompleteError()
	{
		JOptionPane.showMessageDialog(this, "Please choose a color for each peg.",
	    "Incomplete Guess", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Checks if the user made an attempt yet
	 * @return true if an attempt was made, false if not
	 */
	public boolean attemptWasMade()
	{
		return attempted > 0;
	}
	
	/**
	 * Resets the GameBoard to new game position
	 */
	public void reset()
	{	
		for (AttemptRow attempt : attempts)
		{
			attempt.reset();
		}
		
		state = GameState.IN_PROGRESS;
		
		setCode();
		
		attempted = 0;
		attempts[attempted].setEditable(true);
	}
	

	
	
}
