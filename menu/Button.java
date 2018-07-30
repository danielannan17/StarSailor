package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import handlers.InputHandler;
import handlers.ResourceHandler;

/**
 * a generic Button class, used to speed up the creation of menus
 * 
 * 
 */
public class Button extends JButton {

	private static final long serialVersionUID = 1L;
	private InputHandler input;
	private Image buttonImage = ResourceHandler.getImage("gui/button");
	private BufferedImage buffer;

	/**
	 * creates a new button
	 * 
	 * @param title
	 *            the buttons text
	 * @param x
	 *            the buttons x coord
	 * @param y
	 *            the buttons y coord
	 * @param width
	 *            the buttons width
	 * @param height
	 *            the buttons height
	 */
	public Button(String title, int x, int y, int width, int height) {
		this.setText(title);
		this.setBounds(x, y, width, height);
		input = new InputHandler(this);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
	}

	/**
	 * checks if this button has been pressed
	 * 
	 * @return whether the button has been pressed
	 */
	public boolean buttonPressed() {
		if (input.isMouseDown(MouseEvent.BUTTON1)) {
			// SoundHandler.playSound(SoundHandler.select);
		}
		return (input.isMouseDown(MouseEvent.BUTTON1));
	}

	/**
	 * artificially releases the mouse
	 */
	public void releaseButton() {
		input.artificialMouseReleased(MouseEvent.BUTTON1);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics gbuffer = g.create();
		gbuffer.drawImage(buttonImage, 0, 0, this.getBounds().width, this.getBounds().height, null);
		gbuffer.setFont(new Font("Verdana", Font.PLAIN, 20));
		gbuffer.setColor(Color.blue);
		FontMetrics met = gbuffer.getFontMetrics();
		gbuffer.drawString(this.getText(), this.getBounds().width / 2 - met.stringWidth(this.getText()) / 2,
				this.getBounds().height / 2 + met.getAscent() / 2);
		g.drawImage(buffer, this.getBounds().x, this.getBounds().y, null);
		gbuffer.dispose();
	}

}
