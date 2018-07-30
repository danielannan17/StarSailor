package menu;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import handlers.InputHandler;
import handlers.ResourceHandler;

/**
 * a generic menu used to speed up menu creation
 * 
 * 
 */
public abstract class Menu extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image bg;

	/**
	 * creates a new menu
	 * 
	 * @param background
	 *            the background of the menu
	 */
	public Menu(String background) {
		bg = ResourceHandler.getImage(background);
	}

	public Menu(Image bg) {
		this.bg = bg;
	}

	/**
	 * updates the menu
	 * 
	 * @param time
	 *            the time between frames
	 * @return an int representing what happeneed in this update
	 */
	public abstract int update(float time);

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bg, 0, 0, InputHandler.screenSize.width, InputHandler.screenSize.height, null);
	}

}