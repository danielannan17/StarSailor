package galaxy.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import main.Main;

/**
 * the receiver for the client
 * 
 * 
 *
 */
public class ClientReceiver extends Thread {

	private InputStream server;
	private LinkedList<Message> messageQueue;
	private long seed;
	private int playerNumber;
	private boolean received = false;

	/**
	 * creates a new client receiver
	 * 
	 * @param server
	 *            the inputstream used to get messages from the server
	 */
	public ClientReceiver(InputStream server) {
		this.server = server;
		messageQueue = new LinkedList<Message>();
	}

	/**
	 * gets a message from the server
	 * 
	 * @return a message object
	 */
	public Message getMessage() {
		try {
			return messageQueue.poll();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public void run() {
		while (true) {
			byte[] b = new byte[1];
			byte[] b1;
			byte[] b2;
			try {
				server.read(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (b[0] == Message.PLANET_OWNERSHIP) {
				b1 = new byte[12];
				try {
					server.read(b1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				b2 = new byte[b.length + b1.length];
				System.arraycopy(b, 0, b2, 0, b.length);
				System.arraycopy(b1, 0, b2, b.length, b1.length);
				messageQueue.add(Message.deSerialize(b2));
			} else if (b[0] == Message.SET_OWNERSHIP) {
				b1 = new byte[4];
				try {
					server.read(b1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				b2 = new byte[b.length + b1.length];
				System.arraycopy(b, 0, b2, 0, b.length);
				System.arraycopy(b1, 0, b2, b.length, b1.length);
				messageQueue.add(Message.deSerialize(b2));
			} else if (b[0] == Message.SEED) {
				b1 = new byte[8];
				try {
					server.read(b1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				b2 = new byte[b.length + b1.length];
				System.arraycopy(b, 0, b2, 0, b.length);
				System.arraycopy(b1, 0, b2, b.length, b1.length);
				messageQueue.add(Message.deSerialize(b2));
			} else if (b[0] == Message.PLAYER_NUMBER) {
				b1 = new byte[1];
				try {
					server.read(b1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				b2 = new byte[b.length + b1.length];
				System.arraycopy(b, 0, b2, 0, b.length);
				System.arraycopy(b1, 0, b2, b.length, b1.length);
				Message m = Message.deSerialize(b2);
				Main.numOfPlayers = m.getPlayerNumber();
				messageQueue.add(m);
				received = true;
			} else if (b[0] == Message.SET_SHIPS){
				b1 = new byte[5];
				try {
					server.read(b1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				b2 = new byte[b.length + b1.length];
				System.arraycopy(b, 0, b2, 0, b.length);
				System.arraycopy(b1, 0, b2, b.length, b1.length);
				messageQueue.add(Message.deSerialize(b2));
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * gets the seed
	 * 
	 * @return the seed
	 */
	public long getSeed() {
		return seed;
	}

	/**
	 * gets the player number
	 * 
	 * @return the players number
	 */
	public int getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * checks if the initial messages have been received
	 * 
	 * @return whether the initial message were received
	 */
	public boolean isReceived() {
		return received;
	}

}
