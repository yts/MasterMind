import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

/**
 * The Peg class is used to display a peg (or empty hole) for a game of Mastermind.
 * @author Y. Stitzer
 * @ version
 */
public class Peg extends JComponent
{
	private Rectangle2D.Double square;
	private Color color;
	private int border;
	private final Color BORDER_COLOR = Color.BLACK;
	private final Color EMPTY_COLOR = Color.GRAY;

	/**
	 * Constructs a square Peg object with a specified width to display an empty peg hole.
	 * @param width the width (and height) of the peg
	 * @param inBorder the border thickness of the peg "hole"
	 */
	public Peg(int width, int inBorder)
	{
		color = EMPTY_COLOR;
		border = inBorder;
		square = new Rectangle2D.Double(border / 2, border / 2, width, width);
	}

	/**
	 * Paints a square to represent the peg.
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		//draw the peg hole
		g2.setColor(BORDER_COLOR);
		g2.setStroke(new BasicStroke(border));
		g2.draw(square);
		//draw the peg color
		g2.setColor(color);
		g2.fill(square);
	}

	/**
	 * Changes the color of the peg.
	 * @param inColor the color to make the peg.
	 */
	public void setColor(Color inColor)
	{
		color = inColor;
		repaint();
	}

	/**
	 * Gets the current color of the peg.
	 * @return the peg's color
	 */
	public Color getColor()
	{
		return color;
	}
	
	/**
	 * Sets the color of the peg to that of an empty peg hole.
	 */
	public void reset()
	{
		color = EMPTY_COLOR;
		repaint();
	}

}
