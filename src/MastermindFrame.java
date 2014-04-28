import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.Document;

/**
 * The JFrame in which to load and display GameBoards
 * @author Y. Stitzer
 * @version 12/20/2012
 */
public class MastermindFrame extends JFrame
{
	private GameBoard game;
	private StatsCounter stats;
	
/**
 * Creates a new JFrame with the GameBoard and menu bar
 */
	public MastermindFrame()
	{
      game = new GameBoard();
      stats = new StatsCounter();

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createGameMenu());
		menuBar.add(createHelpMenu());
		
		setJMenuBar(menuBar);
		add(createHeaderPanel(), BorderLayout.NORTH);
		add(game, BorderLayout.CENTER);
		
		setTitle("Mastermind");
		ImageIcon image = new ImageIcon("resources/MastermindPicture.jpg");
		setIconImage(image.getImage());
	}

	/**
	 * Creates a "Game" menu to perform game related actions
	 * @return the JMenu
	 */
	private JMenu createGameMenu()
	{
		JMenu menu = new JMenu("Game");
		
		menu.add(createNewGameItem());
		menu.add(createShowStatisticsItem());
		menu.add(createRevealCodeItem());
		menu.add(createQuitItem());
		
		return menu;
	}
	
	/**
	 * Creates a menu item to start a new game when clicked
	 * @return the "Start New" menu item
	 */
	private JMenuItem createNewGameItem()
	{
		JMenuItem item = new JMenuItem("Start New"); 
		
		class NewGameAction implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				if (game.attemptWasMade())
				{
					try
					{
						stats.addIncomplete();
					} catch (IOException ex)
					{
						JOptionPane.showMessageDialog(game, "Unable to upjust statistics due to file error: " 
								+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				game.reset();
			}
			
		}
		
		item.addActionListener(new NewGameAction());
		
		return item;
	}

	/**
	 * Create a menu item to show previously played game statistics when clicked
	 * @return the "Show Statistics" menu item
	 */
	private JMenuItem createShowStatisticsItem()
	{
      JMenuItem item = new JMenuItem("Show Statistics"); 
		
		class StatisticsAction implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					JPanel message = new JPanel(new GridLayout(0, 1));
					
					int[] currentStats = stats.getStats();
					
					JLabel label = new JLabel("Games Won: " + currentStats[StatsCounter.WINS]);
					message.add(label);
					
					label = new JLabel("Games Lost: " + currentStats[StatsCounter.LOSSES]);
					message.add(label);
					
					label = new JLabel("Games Incomplete: " + currentStats[StatsCounter.INCOMPLETES]);
					message.add(label);
					
					JOptionPane.showMessageDialog(game, message, "Mastermind Statistics", JOptionPane.INFORMATION_MESSAGE);
					
				} catch (IOException ex)
				{
					JOptionPane.showMessageDialog(game, "Unable to retrieve the statistics due to file error: " 
							+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				};
			}
			
		}
		
		item.addActionListener(new StatisticsAction());
		
		return item;
	}
	
	/**
	 * Creates a menu item to display the answer code when clicked
	 * @return the "Reveal Code" menu item
	 */
	private JMenuItem createRevealCodeItem()
	{
      JMenuItem item = new JMenuItem("Reveal Code"); 
		
		class RevealCodeAction implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				int selection = JOptionPane.showConfirmDialog(game, "If you reveal the code, this game will be over. \n Do you wish to continue?", 
						"Confirm Reveal", JOptionPane.YES_NO_OPTION);
				
				if (selection == JOptionPane.YES_OPTION)
				{
					if (game.attemptWasMade())
					{
						try
						{
							stats.addIncomplete();
						} catch (IOException ex)
						{
							JOptionPane.showMessageDialog(game, "Unable to adjust the statistics due to file error: " 
									+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
					
					game.showLostMessage(); //displays the code
				}
			}
		}
		
		item.addActionListener(new RevealCodeAction());
		
		return item;
	}
	
	/**
	 * Creates a menu item to terminate the game when clicked
	 * @return the "Quit" menu item
	 */
	private JMenuItem createQuitItem()
	{
		JMenuItem item = new JMenuItem("Quit"); 
		
		class ExitAction implements ActionListener, Serializable
		{
			public void actionPerformed(ActionEvent event)
			{
				if (game.attemptWasMade())
				{
					try
					{
						stats.addIncomplete();
					} catch (IOException ex)
					{
						JOptionPane.showMessageDialog(game, "Unable to adjust the statistics due to file error: " 
								+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				dispose(); //close frame
			}
			
		}
		
		item.addActionListener(new ExitAction());
		
		return item;
	}

	/**
	 * Create a JMenu to display items to help the user
	 * @return the "Help" menu
	 */
	private JMenu createHelpMenu()
	{
		JMenu menu = new JMenu("Help");
		
		menu.add(createDirectionsItem());
		
		return menu;
	}

	/**
	 * Create a menu item to display the game directions to the user when clicked
	 * @return the "How to Play" menu item
	 */
	private JMenuItem createDirectionsItem()
	{
		JMenuItem item = new JMenuItem("How to Play");
		
		class DirectionsAction implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				File file = new File("resources/MastermindDirections.txt"); //file with directions
				
				InputStream in = null;
				String text = "";
				
				try
				{
					//read file to array of bytes and make a string out of them
					in = new FileInputStream(file);
					byte[] textBytes = new byte[(int) file.length()];
					in.read(textBytes);
					text = new String(textBytes);
				} catch (FileNotFoundException ex)
				{
					JOptionPane.showMessageDialog(game, "Unable to show game rules because file with rules wasn't found.", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (IOException ex)
				{
					JOptionPane.showMessageDialog(game, "Unable to show game rules due to error: " 
							+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				//close the stream
				if (in != null)
				{
					try
					{
						in.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				
				//display directions in JFrame
				final int WIDTH = 320;
				final int HEIGHT = 585;
				JLabel textLabel = new JLabel(text);
				JFrame frame = new JFrame();
				frame.add(textLabel);
				frame.setSize(WIDTH, HEIGHT);
				frame.setLocation(getLocationOnScreen());
				frame.setTitle("How to Play");
				frame.setVisible(true);
				frame.setResizable(false);
			}
		}
		
		item.addActionListener(new DirectionsAction());
		
		return item;
	}
	
	/**
	 * Creates a JPanel with the game name and short directions.
	 * @return the JPanel
	 */
	private JPanel createHeaderPanel()
	{
		JPanel panel = new JPanel(new GridLayout(0, 1));
		
		JLabel directions = new JLabel("Click the pegs to change the colors, then click \"Guess It!\"");
		directions.setHorizontalAlignment(JLabel.CENTER);
		
		panel.add(new JLabel(new ImageIcon("resources/MastermindTitle.jpg")));
		panel.add(directions);
		
		return panel;
	}
}
	
