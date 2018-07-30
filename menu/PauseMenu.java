package menu;

import handlers.InputHandler;
import main.State;

/**
 * the in game pause menu
 * 
 * 
 *
 */
public class PauseMenu extends Menu {

	private static final long serialVersionUID = 1L;
	private Button resume, options, exit;

	/**
	 * creates a new pause menu
	 * 
	 * @param background
	 *            the path to the background image to use
	 */
	public PauseMenu(String background) {
		super(background);
		this.setLayout(null);
		int width = InputHandler.screenSize.width / 5, height = InputHandler.screenSize.height / 12;
		int x = (int) InputHandler.midPoint.x - width / 2, y = (int) (2 * InputHandler.midPoint.y / 3 - height / 2);
		resume = new Button("Resume", x, y, width, height);
		y = (int) (y + height * 1.5);
		options = new Button("Settings", x, y, width, height);
		y = (int) (y + height * 1.5);
		exit = new Button("Main Menu", x, y, width, height);
		this.add(resume);
		this.add(options);
		this.add(exit);
	}

	@Override
	public int update(float time) {
		if (resume.buttonPressed()) {
			return State.PLAY;
		} else if (options.buttonPressed()) {
			options.releaseButton();
			return State.OPTIONS;
		} else if (exit.buttonPressed()) {
			return State.MAIN;
		}
		return 0;
	}

}
