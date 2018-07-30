package galaxy.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import main.Main;

/**
 * the server connect class which handles players connecting to the server mid
 * game
 * 
 * 
 *
 */
public class ServerConnect extends Thread {

	private ServerSocket server;
	private LinkedList<Socket> queue;
	private int maxPlayers;

	/**
	 * creates a new server connect object
	 * 
	 * @param server
	 *            the server socket to listen for connection on
	 */
	public ServerConnect(ServerSocket server, int maxPlayers) {
		this.server = server;
		queue = new LinkedList<Socket>();
		this.maxPlayers = maxPlayers;
	}

	/**
	 * gets the next client trying to connect in the queue
	 * 
	 * @return a Socket object of a client that is trying to connect
	 */
	public Socket getClient() {
		try {
			return queue.poll();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (!Main.offline && Main.numOfPlayers < maxPlayers) {
					Socket client = server.accept();
					queue.add(client);
					Main.numOfPlayers++;
				} else {
					try {
						Thread.sleep(100000000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
