package menu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import handlers.InputHandler;
import handlers.ResourceHandler;
import main.State;

/**
 * the Main menu
 * 
 * 
 */
public class MainMenu extends Menu {

	private static final long serialVersionUID = 1L;
	private Label title;
	private Button play, host, connect, instant, options, exit;

	/**
	 * creates a new Main menu
	 * 
	 * @param bg
	 *            the background of this menu
	 */
	public MainMenu(String bg) {
		super(bg);
		this.setLayout(null);
		int width = 3 * InputHandler.screenSize.width / 4, height = InputHandler.screenSize.height / 8;
		int x = (int) (InputHandler.midPoint.x - (width / 2)), y = (int) (InputHandler.midPoint.y / 2 - (height / 2));
		title = new Label("", "URW Chancery L", Font.PLAIN, InputHandler.screenSize.width / 15, Color.white, x, y,
				width, height, JLabel.CENTER);
		title.addImage(ResourceHandler.getImage("gui/title"), width, height);
		width = InputHandler.screenSize.width / 10;
		height = InputHandler.screenSize.height / 14;
		x = (int) (InputHandler.midPoint.x - (width / 2));
		y = (int) ((InputHandler.midPoint.y / 5) * 5 - (height / 2));
		play = new Button("Play", x, y, width, height);
		y = (int) ((InputHandler.midPoint.y / 5) * 5 - (height / 2));
		host = new Button("Host", x, y, width, height);
		y = (int) ((InputHandler.midPoint.y / 5) * 6 - (height / 2));
		connect = new Button("Connect", x, y, width, height);
		y = (int) ((InputHandler.midPoint.y / 5) * 6 - (height / 2));
		instant = new Button("Instant Battle", x, y, width, height);
		y = (int) ((InputHandler.midPoint.y / 5) * 7 - (height / 2));
		options = new Button("Options", x, y, width, height);
		y = (int) ((InputHandler.midPoint.y / 5) * 8 - (height / 2));
		exit = new Button("Exit", x, y, width, height);
		this.add(title);
		this.add(play);
		// this.add(host);
		// this.add(connect);
		this.add(instant);
		this.add(options);
		this.add(exit);
	}

	@Override
	public int update(float time) {
		if (play.buttonPressed()) {
			return State.PLAY;
		}
		if (host.buttonPressed()) {
			return State.HOST;
		}
		if (connect.buttonPressed()) {
			return State.CONNECT;
		}
		if (instant.buttonPressed()) {
			return State.INSTANT;
		}
		if (options.buttonPressed()) {
			return State.OPTIONS;
		}
		if (exit.buttonPressed()) {
			return State.EXIT;
		}
		return 0;
	}

}
