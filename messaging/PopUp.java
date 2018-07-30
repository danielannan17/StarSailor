package messaging;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import handlers.InputHandler;
import main.Main;

/**
 * the pop up messages that act as a tutorial for the game
 * 
 * 
 *
 */
public class PopUp {

	private String text, closeText = "close";
	private Rectangle bounds, close;

	/**
	 * creates a new pop up
	 * 
	 * @param text
	 *            the text this pop up displays
	 */
	public PopUp(String text) {
		this.text = text;
		int width = InputHandler.screenSize.width / 4;
		int height = InputHandler.screenSize.height / 4;
		bounds = new Rectangle(InputHandler.screenSize.width - width, InputHandler.screenSize.height - height, width,
				height);
		close = new Rectangle(bounds.x + 3 * width / 4, bounds.y + 3 * height / 4, width / 5, height / 5);
	}

	/**
	 * checks if the close button was clicked
	 * 
	 * @param mousePos
	 *            the mouse position
	 */
	public boolean clicked(Point mousePos) {
		if (close.contains(mousePos) && Main.input.isMouseDown(MouseEvent.BUTTON1)) {
			text = "";
			bounds = new Rectangle(0, 0, 0, 0);
			close = bounds;
			return true;
		}
		return false;
	}

	/**
	 * draws this popup to the screen
	 * 
	 * @param g2d
	 *            the graphics context to use
	 */
	public void draw(Graphics2D g2d) {
		g2d.setColor(new Color(0, 0, 0, 150));
		g2d.fill(bounds);
		g2d.setColor(Color.white);
		int x = bounds.x + bounds.width / 20, y = bounds.y + bounds.height / 12;
		for (String line : text.split("\n")) {
			g2d.drawString(line, x, y += g2d.getFontMetrics().getHeight());
		}
		g2d.setColor(new Color(0, 0, 255, 150));
		g2d.fill(close);
		g2d.setColor(Color.white);
		g2d.drawString(closeText, close.x + close.width / 4, close.y + close.height / 2);
	}

}
