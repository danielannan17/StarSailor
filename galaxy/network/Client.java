package galaxy.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

import galaxy.Galaxy;

/**
 * contains code for the client of the games campaign mode
 * 
 * 
 *
 */
public class Client extends Thread {

	private Socket socket;
	private PrintStream toServer;
	private InputStream fromServer;
	private ClientSender sender;
	private ClientReceiver receiver;

	/**
	 * creates a new Client
	 * 
	 * @param ip
	 *            the ip address to connect to
	 * @param port
	 *            the port to connect to
	 */
	public Client(String ip, int port) {
		try {
			socket = new Socket(ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			toServer = new PrintStream(socket.getOutputStream());
			fromServer = socket.getInputStream();
		} catch (IOException e) {
			System.err.println("Couldn't get server communication streams: " + e.getMessage());
			try {
				socket.close();
			} catch (IOException f) {
			}
			System.exit(1);
		}
		sender = new ClientSender(toServer);
		receiver = new ClientReceiver(fromServer);
		receiver.start();
	}

	/**
	 * sends a message over the network
	 * 
	 * @param m
	 *            the message object to send
	 */
	public void sendMessage(Message m) {
		sender.sendBytes(m.serialize());
	}

	/**
	 * gets a message from the network
	 * 
	 * @return a message from the network
	 */
	public Message receiveMessage() {
		return receiver.getMessage();
	}

	@Override
	public void run() {
		while (true) {
			Message m = receiveMessage();
			if (m != null) {
				if (m.getMessageID() == Message.PLANET_OWNERSHIP) {
					Galaxy.setOwnership(m);
				} else if(m.getMessageID() == Message.SET_SHIPS) {
					Galaxy.getShips(m);
				}
			}
			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
