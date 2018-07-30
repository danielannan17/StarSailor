package galaxy.network;

import java.io.IOException;
import java.io.OutputStream;

/**
 * the Client sender, sends messages to the server
 * 
 * 
 *
 */
public class ClientSender {

	private OutputStream server;

	/**
	 * creates a new client sender
	 * 
	 * @param server
	 *            the outputstream to use to communicate with the server
	 */
	public ClientSender(OutputStream server) {
		this.server = server;
	}

	/**
	 * sends a byte array over the network
	 * 
	 * @param b
	 *            the byte array to send
	 */
	public void sendBytes(byte[] b) {
		try {
			server.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
