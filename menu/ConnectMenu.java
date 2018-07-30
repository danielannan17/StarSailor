package menu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import handlers.InputHandler;
import main.State;

/**
 * the menu for connecting to another game
 * 
 * 
 */
public class ConnectMenu extends Menu {

	private static final long serialVersionUID = 1L;
	private Label ipAddress, port;
	private Button connect, back;
	private TextBox ipBox, portBox;

	/**
	 * creates a new connect menu
	 * 
	 * @param bg
	 *            the background of the connect menu
	 */
	public ConnectMenu(String bg) {
		super(bg);
		this.setLayout(null);
		int width = InputHandler.screenSize.width / 10, height = InputHandler.screenSize.height / 14;
		int x = (int) (InputHandler.midPoint.x / 2 - (width / 2)),
				y = (int) (InputHandler.midPoint.y / 2 - (height / 2));
		ipAddress = new Label("IP: ", "Verdana", Font.BOLD, 20, Color.white, x, y, width, height, JLabel.LEFT);
		x = x + width;
		width = width * 4;
		height = InputHandler.screenSize.height / 20;
		y = (int) (InputHandler.midPoint.y / 2 - (height / 2));
		ipBox = new TextBox("", x, y, width, height);
		width = InputHandler.screenSize.width / 10;
		height = InputHandler.screenSize.height / 14;
		x = (int) (InputHandler.midPoint.x / 2 - (width / 2));
		y = y + height;
		port = new Label("Port: ", "Verdana", Font.BOLD, 20, Color.white, x, y, width, height, JLabel.LEFT);
		x = x + width;
		width = width * 4;
		height = InputHandler.screenSize.height / 20;
		portBox = new TextBox("", x, y, width, height);
		width = InputHandler.screenSize.width / 10;
		height = InputHandler.screenSize.height / 14;
		x = (int) (InputHandler.midPoint.x / 2 - (width / 2));
		y = (int) ((InputHandler.midPoint.y / 2) * 3 - (height / 2));
		back = new Button("Back", x, y, width, height);
		x = (int) ((InputHandler.midPoint.x / 2) * 3 - (width / 2));
		connect = new Button("Connect", x, y, width, height);
		this.add(ipAddress);
		this.add(port);
		this.add(ipBox);
		this.add(portBox);
		this.add(back);
		this.add(connect);
	}

	@Override
	public int update(float time) {
		if (back.buttonPressed()) {
			return State.MAIN;
		}
		if (connect.buttonPressed()) {
			return State.CONNECT;
		}
		return 0;
	}

	/**
	 * gets the ip
	 * 
	 * @return the ip
	 */
	public String getIP() {
		return ipBox.getText();
	}

	/**
	 * gets the port
	 * 
	 * @return the port
	 */
	public int getPort() {
		return Integer.parseInt(portBox.getText());
	}

}
