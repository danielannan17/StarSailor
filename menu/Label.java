package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * a generic label used to speed up the creation of menus
 * 
 * 
 */
public class Label extends JLabel {

	private static final long serialVersionUID = 1L;

	/**
	 * creates a new label
	 * 
	 * @param text
	 *            the text in this label
	 * @param fontName
	 *            the font name
	 * @param fontType
	 *            the font type
	 * @param fontSize
	 *            the font size
	 * @param fontColor
	 *            the font color
	 * @param x
	 *            the x coord of this label
	 * @param y
	 *            the y coord of this label
	 * @param width
	 *            the width of this label
	 * @param height
	 *            the height of this label
	 * @param alignment
	 *            this label's alignment
	 */
	public Label(String text, String fontName, int fontType, int fontSize, Color fontColor, int x, int y, int width,
			int height, int alignment) {
		this.setFont(new Font(fontName, fontType, fontSize));
		this.setText(text);
		this.setForeground(fontColor);
		this.setBounds(x, y, width, height);
		this.setHorizontalAlignment(alignment);
	}

	/**
	 * adds an image to this label
	 * 
	 * @param img
	 *            the image to add
	 * @param width
	 *            the width of the image
	 * @param height
	 *            the height of the image
	 */
	public void addImage(Image img, int width, int height) {
		this.setIcon(new ImageIcon(img.getScaledInstance(width, height, 0)));
	}

}
