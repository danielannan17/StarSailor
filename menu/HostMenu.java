package menu;

import handlers.InputHandler;
import main.State;

/**
 * the host menu for the game
 * 
 * 
 */
public class HostMenu extends Menu {

	private static final long serialVersionUID = 1L;
	private Button start, back;

	/**
	 * creates a new host menu
	 * 
	 * @param bg
	 *            the background for this menu
	 */
	public HostMenu(String bg) {
		super(bg);
		this.setLayout(null);
		int width = InputHandler.screenSize.width / 10, height = InputHandler.screenSize.height / 14;
		int x = (int) (InputHandler.midPoint.x / 2 - (width / 2)),
				y = (int) (InputHandler.midPoint.y / 2 - (height / 2));
		width = InputHandler.screenSize.width / 10;
		height = InputHandler.screenSize.height / 14;
		x = (int) (InputHandler.midPoint.x / 2 - (width / 2));
		y = (int) ((InputHandler.midPoint.y / 2) * 3 - (height / 2));
		back = new Button("Back", x, y, width, height);
		x = (int) ((InputHandler.midPoint.x / 2) * 3 - (width / 2));
		start = new Button("Host", x, y, width, height);
		this.add(back);
		this.add(start);
	}

	@Override
	public int update(float time) {
		if (start.buttonPressed()) {
			return State.HOST;
		}
		if (back.buttonPressed()) {
			return State.MAIN;
		}
		return 0;
	}

}
