package galaxy.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * the serverReceiver which handles messages coming to the server
 * 
 * 
 *
 */
public class ServerReceiver extends Thread {

	private InputStream client;
	private LinkedList<Message> messageQueue;

	/**
	 * creates a new server receiver
	 * 
	 * @param client
	 *            the inputstream to listen for messages on
	 */
	public ServerReceiver(InputStream client) {
		this.client = client;
		messageQueue = new LinkedList<Message>();
	}

	/**
	 * gets the next message from a queue of messages
	 * 
	 * @return the next message in the queue
	 */
	public Message getMessage() {
		try {
			return messageQueue.removeFirst();
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
				client.read(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (b[0] == Message.SET_OWNERSHIP) {
				b1 = new byte[4];
				try {
					client.read(b1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				b2 = new byte[b.length + b1.length];
				System.arraycopy(b, 0, b2, 0, b.length);
				System.arraycopy(b1, 0, b2, b.length, b1.length);
				messageQueue.add(Message.deSerialize(b2));
			} else if (b[0] == Message.REQUEST_OWNERSHIP) {
				b1 = new byte[3];
				try {
					client.read(b1);
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
					client.read(b1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				b2 = new byte[b.length + b1.length];
				System.arraycopy(b, 0, b2, 0, b.length);
				System.arraycopy(b1, 0, b2, b.length, b1.length);
				messageQueue.add(Message.deSerialize(b2));
			} else if(b[0] == Message.GET_SHIPS) {
				b1 = new byte[3];
				try {
					client.read(b1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				b2 = new byte[b.length + b1.length];
				System.arraycopy(b, 0, b2, 0, b.length);
				System.arraycopy(b1, 0, b2, b.length, b1.length);
				messageQueue.add(Message.deSerialize(b2));
			} else if(b[0] == Message.SET_SHIPS) {
				b1 = new byte[5];
				try {
					client.read(b1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				b2 = new byte[b.length + b1.length];
				System.arraycopy(b, 0, b2, 0, b.length);
				System.arraycopy(b1, 0, b2, b.length, b1.length);
				messageQueue.add(Message.deSerialize(b2));
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
