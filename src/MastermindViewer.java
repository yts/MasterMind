import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Displays the Mastermind game
 * @author Y. Stitzer
 * @version 12/20/2012
 */
public class MastermindViewer
{

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, ClassNotFoundException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // thought this was cool
		int FRAME_WIDTH = 350;
	   int FRAME_HEIGHT = 685;
		MastermindFrame frame = new MastermindFrame();
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
