package main;

import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

/**
 * an abstract main class that contains code for the main run loop that will
 * update and draw the whole game
 * 
 * 
 */
public abstract class AbstractMain extends JFrame {

	private static final long serialVersionUID = 1L;
	protected boolean running = false;
	public static int width, height;

	/**
	 * runs the game by initialising, then updating at a constant rate and
	 * drawing
	 */
	public void run() {
		initialise();
		long beforeTime, afterTime, deltaTime = 0;
		long counter = System.nanoTime() + 1000000000;
		int maxFPS = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayModes()[0]
				.getRefreshRate();
		if (maxFPS == 0) {
			maxFPS = 60;
		}
		int fps = 0;
		long minFrameTime = 1000000000l / maxFPS;
		deltaTime = 1;
		update(0);
		while (running) {
			beforeTime = System.nanoTime();
			draw();
			update(deltaTime / 1000000000f);
			afterTime = System.nanoTime();
			deltaTime = afterTime - beforeTime;
			fps++;
			if (deltaTime < minFrameTime) {
				try {
					Thread.sleep((minFrameTime - deltaTime) / 1000000l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (System.nanoTime() > counter) {
				counter += 1000000000;
				System.out.println(fps);
				fps = 0;
			}
		}
		close();
	}

	/**
	 * an abstract initialise method
	 */
	public abstract void initialise();

	/**
	 * updates the game
	 * 
	 * @param time
	 *            the time between frames
	 */
	public abstract void update(float time);

	/**
	 * draws the game
	 */
	public abstract void draw();

	/**
	 * closes the game
	 */
	public abstract void close();

}
