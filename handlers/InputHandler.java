package handlers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

/**
 * Handles input for a given Component
 * 
 * 
 */
public class InputHandler implements KeyListener, MouseListener, MouseWheelListener {

	private boolean[] keyArray = new boolean[256];
	private boolean[] mouseArray = new boolean[MouseInfo.getNumberOfButtons()];
	private boolean overComp, mouseWheelMoved = false;
	private String typedAcum = "";
	private static Point lastMousePosition = new Point(0, 0);
	private static Component c;
	private double mouseWheelRotation = 0;
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static Point2D.Double midPoint = new Point2D.Double((double) screenSize.width / 2,
			(double) screenSize.height / 2);

	/**
	 * adds this class as a listener for a given Component
	 * 
	 * @param c
	 *            the Component being listened to
	 */
	public InputHandler(Component c) {
		c.addKeyListener(this);
		c.addMouseListener(this);
		c.addMouseWheelListener(this);
		InputHandler.c = c;

	}

	/**
	 * returns the position of the mouse on the screen
	 * 
	 * @return the position of the mouse on the screen as a Point
	 */
	public static Point getMousePositionOnScreen() {
		try {
			Point p = MouseInfo.getPointerInfo().getLocation();
			if (p == null) {
				return lastMousePosition;
			}
			lastMousePosition = p;
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			return getMousePositionRelativeToComponent();
		}
	}

	/**
	 * gets the position of the mouse on the component this class is listening
	 * to
	 * 
	 * @return the position of the mouse on the component
	 */
	public static Point getMousePositionRelativeToComponent() {
		try {
			Point p = c.getMousePosition();
			if (p == null) {
				return lastMousePosition;
			}
			lastMousePosition = p;
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			return getMousePositionOnScreen();
		}
	}

	/**
	 * checks whether the key at a position in he array of keys is being pressed
	 * 
	 * @param keyCode
	 *            the id of the key being checked
	 * @return a true or false value indicating whether the key has been pressed
	 */
	public boolean isKeyDown(int keyCode) {
		return keyArray[keyCode];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		try {
			keyArray[e.getKeyCode()] = true;
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * sets the specified key as pressed
	 * 
	 * @param keyCode
	 *            the code of the key being pressed
	 */
	public void artificialKeyPressed(int keyCode) {
		keyArray[keyCode] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		try {
			keyArray[e.getKeyCode()] = false;
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * sets the specified key as released
	 * 
	 * @param keyCode
	 *            the code of the key being released
	 */
	public void artificialKeyReleased(int keyCode) {
		keyArray[keyCode] = false;
	}

	/**
	 * gets the accumulator of typed letters
	 * 
	 * @return a string of typed letters
	 */
	public String getTypedAcum() {
		return typedAcum;
	}

	/**
	 * clears the accumulator of typed letters
	 */
	public void clearTypedAcum() {
		typedAcum = "";
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
			if (typedAcum.length() == 0) {
				return;
			}
			typedAcum = typedAcum.substring(0, typedAcum.length() - 1);
			return;
		}
		typedAcum += e.getKeyChar();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * checks if the mouse over the component this handler is on
	 * 
	 * @return a boolean of whether the mouse is over the component
	 */
	public boolean isMouseOverComp() {
		return overComp;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		overComp = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		overComp = false;
	}

	/**
	 * checks if the specified mouse button is down
	 * 
	 * @param mouseButton
	 *            the mouse button being checked
	 * @return a boolean representing whether the mouse button is down or not
	 */
	public boolean isMouseDown(int mouseButton) {
		return mouseArray[mouseButton];
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseArray[e.getButton()] = true;
	}

	/**
	 * artificially presses the specified mouse button
	 * 
	 * @param mouseButton
	 *            the mouse button target
	 */
	public void artificialMousePressed(int mouseButton) {
		mouseArray[mouseButton] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseArray[e.getButton()] = false;
	}

	/**
	 * artificially releases the specified mouse button
	 * 
	 * @param mouseButton
	 *            the mouse button target
	 */
	public void artificialMouseReleased(int mouseButton) {
		mouseArray[mouseButton] = false;
	}

	/**
	 * checks whether the mouse wheel is being scrolled up
	 * 
	 * @return whether the mouse wheel is scrolling up
	 */
	public boolean getMouseWheelUp() {
		return mouseWheelRotation < 0;
	}

	/**
	 * checks whether the mouse wheel is being scrolled down
	 * 
	 * @return whether the mouse wheel is scrolling down
	 */
	public boolean getMouseWheelDown() {
		return mouseWheelRotation > 0;
	}

	/**
	 * checks if the mouse wheel has moved
	 * 
	 * @return a boolean representing whether the mouse wheel has moved
	 */
	public boolean hasMouseWheelMoved() {
		return mouseWheelMoved;
	}

	@Override
	/**
	 * positive value is down/towards user negative value is up/away from user
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheelRotation = e.getPreciseWheelRotation();
		mouseWheelMoved = true;
	}

	/**
	 * get the rotation amount of the mouse wheel
	 * 
	 * @return rotation amount as a double
	 */
	public double getMouseWheelRotation() {
		mouseWheelMoved = false;
		return mouseWheelRotation;
	}

	/**
	 * stops the mouse wheel
	 */
	public void stopMouseWheel() {
		mouseWheelMoved = false;
		mouseWheelRotation = 0;
	}

}
