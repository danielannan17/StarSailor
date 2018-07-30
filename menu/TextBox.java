package menu;

import javax.swing.JTextField;

/**
 * a generic text box
 * 
 * 
 */
public class TextBox extends JTextField {

	private static final long serialVersionUID = 1L;

	/**
	 * creates a text box
	 * 
	 * @param defaultText
	 *            the default text
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public TextBox(String defaultText, int x, int y, int width, int height) {
		this.setText(defaultText);
		this.setBounds(x, y, width, height);
	}

}
