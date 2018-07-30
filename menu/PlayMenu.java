package menu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import generators.NameGenerator;
import handlers.InputHandler;
import handlers.MathHandler;
import main.State;

/**
 * the menu that allows users to start the campaign or connect to another's
 * campaign
 * 
 * 
 */
public class PlayMenu extends Menu {

	private static final long serialVersionUID = 1L;
	private Label name, seed, ip, port;
	private TextBox nameBox, seedBox, ipBox, portBox;
	private Button play, back, connect;
	private JCheckBox offline;

	/**
	 * creates a new play menu
	 * 
	 * @param bg
	 *            the background of this menu
	 */
	public PlayMenu(String bg) {
		super(bg);
		this.setLayout(null);
		int width = InputHandler.screenSize.width / 10, height = InputHandler.screenSize.height / 14;
		int x = (int) (InputHandler.midPoint.x / 2 - (width / 2)),
				y = (int) (InputHandler.midPoint.y / 2 - (height / 2));
		name = new Label("Player Name: ", "Verdana", Font.BOLD, 20, Color.white, x, y, width, height, JLabel.LEFT);
		x = x + width;
		width = width * 4;
		height = InputHandler.screenSize.height / 20;
		y = (int) (InputHandler.midPoint.y / 2 - (height / 2));
		nameBox = new TextBox(NameGenerator.generateName(3), x, y, width, height);
		width = InputHandler.screenSize.width / 10;
		height = InputHandler.screenSize.height / 14;
		x = (int) (InputHandler.midPoint.x / 2 - (width / 2));
		y = y + height;
		seed = new Label("Seed: ", "Verdana", Font.BOLD, 20, Color.white, x, y, width, height, JLabel.LEFT);
		x = x + width;
		width = width * 4;
		height = InputHandler.screenSize.height / 20;
		seedBox = new TextBox("" + MathHandler.random.nextLong(), x, y, width, height);
		width = InputHandler.screenSize.width / 10;
		height = InputHandler.screenSize.height / 14;
		x = (int) (InputHandler.midPoint.x / 2 - (width / 2));
		y = y + height;
		ip = new Label("IP: ", "Verdana", Font.BOLD, 20, Color.white, x, y, width, height, JLabel.LEFT);
		x = x + width;
		width = width * 4;
		height = InputHandler.screenSize.height / 20;
		ipBox = new TextBox("localhost", x, y, width, height);
		width = InputHandler.screenSize.width / 10;
		height = InputHandler.screenSize.height / 14;
		x = (int) (InputHandler.midPoint.x / 2 - (width / 2));
		y = y + height;
		port = new Label("Port: ", "Verdana", Font.BOLD, 20, Color.white, x, y, width, height, JLabel.LEFT);
		x = x + width;
		width = width * 4;
		height = InputHandler.screenSize.height / 20;
		portBox = new TextBox("49152", x, y, width, height);
		y = (int) (y + height * 1.5);
		width = width / 8;
		offline = new JCheckBox();
		offline.setText("Offline?");
		offline.setForeground(Color.white);
		offline.setBackground(Color.BLACK);
		offline.setBounds(x, y, width, height);
		width = InputHandler.screenSize.width / 10;
		height = InputHandler.screenSize.height / 14;
		x = (int) (InputHandler.midPoint.x / 2 - (width / 2));
		y = (int) ((InputHandler.midPoint.y / 2) * 3 - (height / 2));
		back = new Button("Back", x, y, width, height);
		x = (int) ((InputHandler.midPoint.x / 2) * 3 - (width / 2));
		play = new Button("Play", x, y, width, height);
		y = (int) (y - play.getBounds().getHeight() * 2);
		connect = new Button("Connect", x, y, width, height);
		this.add(name);
		this.add(nameBox);
		this.add(seed);
		this.add(seedBox);
		this.add(ip);
		this.add(ipBox);
		this.add(port);
		this.add(portBox);
		this.add(offline);
		this.add(back);
		this.add(play);
		this.add(connect);
	}

	@Override
	public int update(float time) {
		if (play.buttonPressed()) {
			return State.PLAY;
		}
		if (connect.buttonPressed()) {
			return State.CONNECT;
		}
		if (back.buttonPressed()) {
			return State.MAIN;
		}
		return 0;
	}

	/**
	 * gets the name entered by the player
	 */
	public String getName() {
		return nameBox.getText();
	}

	/**
	 * gets the seed
	 * 
	 * @return the seed
	 */
	public long getSeed() {
		String s1 = seedBox.getText();
		long s2 = 0;
		for (int i = 0; i < s1.length(); i++) {
			s2 += (int) s1.charAt(i);
		}
		return s2 * 15485863;
	}

	/**
	 * gets the ip from this menu
	 * 
	 * @return the ip
	 */
	public String getIP() {
		return ipBox.getText();
	}

	/**
	 * gets the port from this menu
	 * 
	 * @return the port
	 */
	public int getPort() {
		return Integer.parseInt(portBox.getText());
	}

	/**
	 * checks whether this game is offline
	 * 
	 * @return whether this game is offline
	 */
	public boolean getOffline() {
		return offline.isSelected();
	}

}
