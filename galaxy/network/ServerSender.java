package galaxy.network;

import java.io.IOException;
import java.io.OutputStream;

/**
 * the server sender which handles sending messages to clients
 * 
 * 
 *
 */
public class ServerSender {

	private OutputStream client;

	/**
	 * creates a new server sender
	 * 
	 * @param client
	 *            the outputstream to use to send messages to a client
	 */
	public ServerSender(OutputStream client) {
		this.client = client;
	}

	/**
	 * sends a message to the client
	 * 
	 * @param m
	 *            the message to send
	 */
	public void sendMessage(Message m) {
		try {
			client.write(m.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * sends an array of bytes to the client
	 * 
	 * @param b
	 *            the byte array to send
	 */
	public void sendBytes(byte[] b) {
		try {
			client.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
