package menu;

import java.awt.Color;

import javax.swing.JSlider;

/**
 * a generic slider
 * 
 * 
 */
public class Slider extends JSlider {

	private static final long serialVersionUID = 1L;

	/**
	 * creates a slider
	 * 
	 * @param x
	 *            the x coord
	 * @param y
	 *            the y coord
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param bg
	 *            the background color
	 * @param fg
	 *            the foreground color
	 * @param minimum
	 *            the minimum
	 * @param maximum
	 *            the maximum
	 * @param majorTicks
	 *            the major tick spacing
	 * @param minorTicks
	 *            the minor tick spacing
	 * @param paintTicks
	 *            whether to paint the ticks
	 * @param paintLabels
	 *            whether to paint the labels
	 * @param value
	 *            the start value
	 */
	public Slider(int x, int y, int width, int height, Color bg, Color fg, int minimum, int maximum, int majorTicks,
			int minorTicks, boolean paintTicks, boolean paintLabels, int value) {
		this.setBounds(x, y, width, height);
		this.setBackground(bg);
		this.setForeground(fg);
		this.setMinimum(minimum);
		this.setMaximum(maximum);
		this.setMajorTickSpacing(majorTicks);
		this.setMinorTickSpacing(minorTicks);
		this.setPaintTicks(paintTicks);
		this.setPaintLabels(paintLabels);
		this.setValue(value);
	}

}
