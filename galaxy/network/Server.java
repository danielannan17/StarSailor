package galaxy.network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import galaxy.Galaxy;

/**
 * the Server for the campaign mode of the game
 * 
 * 
 *
 */
public class Server extends Thread {

	private ServerSocket server;
	private int port = 49152;
	private int playerCount = 0;
	public static final int MAXPORT = 65535;
	private ServerConnect connect;
	private ArrayList<Socket> players;
	private ArrayList<ServerSender> senders;
	private ArrayList<ServerReceiver> receivers;
	private long seed;
	private int[][] planetOwnership = new int[Galaxy.numOfStars][Galaxy.maxPlanets],
			planetFighters = new int[Galaxy.numOfStars][Galaxy.maxPlanets],
			planetCarriers = new int[Galaxy.numOfStars][Galaxy.maxPlanets],
			planetCommand = new int[Galaxy.numOfStars][Galaxy.maxPlanets];
	private Random random;

	/**
	 * creates a new Server
	 * 
	 * @param seed
	 *            the seed of the current game
	 */
	public Server(long seed, int maxPlayers) {
		this.seed = seed;
		random = new Random(seed);
		boolean portSet = false;
		while (!portSet && port <= MAXPORT) {
			try {
				server = new ServerSocket(port);
				portSet = true;
			} catch (IOException e) {
				port++;
			}
		}
		for (int i = 0; i < Galaxy.numOfStars; i++) {
			for (int j = 0; j < Galaxy.maxPlanets; j++) {
				planetFighters[i][j] = random.nextInt(30);
				planetCarriers[i][j] = random.nextInt(15);
				planetCommand[i][j] = random.nextInt(7);
			}
		}
		players = new ArrayList<Socket>();
		senders = new ArrayList<ServerSender>();
		receivers = new ArrayList<ServerReceiver>();
		connect = new ServerConnect(server, maxPlayers);
		connect.start();
	}

	/**
	 * Get the IP address of the server
	 * 
	 * @return The IP
	 */
	public String getIP() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface n = interfaces.nextElement();
				Enumeration<InetAddress> ips = n.getInetAddresses();
				while (ips.hasMoreElements()) {
					InetAddress address = ips.nextElement();
					if (!address.isLoopbackAddress() && !address.isLinkLocalAddress()) {
						return address.getHostAddress();
					}
				}
			}
			return Inet4Address.getLocalHost().getHostAddress();
		} catch (SocketException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} catch (UnknownHostException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return null; // but I don't think we should ever get here
	}

	/**
	 * gets the port of this server
	 * 
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	@Override
	public void run() {
		while (true) {
			Socket s = connect.getClient();
			if (s != null) {
				players.add(s);
				System.out.println("player connected");
				playerCount++;
				try {
					senders.add(new ServerSender(s.getOutputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					receivers.add(new ServerReceiver(s.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				receivers.get(playerCount - 1).start();
				senders.get(playerCount - 1).sendBytes(Message.createSeed(seed).serialize());
				senders.get(playerCount - 1).sendBytes(Message.createPlayerNumber(playerCount).serialize());
				for (ServerSender sender : senders) {
					sender.sendBytes(Message.createPlayerNumber(players.size()).serialize());
				}
//				for (int i = 0; i < planetOwnership.length; i++) {
//					boolean ownedPlanet = false;
//					for (int j = 0; j < planetOwnership[i].length; i++) {
//						if (planetOwnership[i][j] != 0) {
//							ownedPlanet = true;
//						}
//					}
//					if (ownedPlanet) {
//						senders.get(playerCount - 1).sendMessage(Message.createPlanetOwnership(i, planetOwnership[i]));
//					}
//				}
			}
			int i = 0;
			for (ServerReceiver receiver : receivers) {
				Message m = receiver.getMessage();
				if (m != null) {
					if (m.getMessageID() == Message.SET_OWNERSHIP) {
						planetOwnership[m.getStarID()][m.getPlanetID()] = m.getOwnedBy();
						for (ServerSender sender : senders) {
							sender.sendMessage(
									Message.createPlanetOwnership(m.getStarID(), planetOwnership[m.getStarID()]));
						}
					} else if (m.getMessageID() == Message.REQUEST_OWNERSHIP) {
						senders.get(m.getPlayerNumber() - 1).sendMessage(
								Message.createPlanetOwnership(m.getStarID(), planetOwnership[m.getStarID()]));
					} else if (m.getMessageID() == Message.PLAYER_NUMBER) {
						senders.get(i).sendBytes(Message.createSeed(seed).serialize());
					} else if (m.getMessageID() == Message.GET_SHIPS) {
						senders.get(i)
								.sendBytes(Message.createSetShips(m.getStarID(), m.getPlanetID(),
										planetFighters[m.getStarID()][m.getPlanetID()],
										planetCarriers[m.getStarID()][m.getPlanetID()],
										planetCommand[m.getStarID()][m.getPlanetID()]).serialize());
					} else if (m.getMessageID() == Message.SET_SHIPS) {
						planetFighters[m.getStarID()][m.getPlanetID()] = m.getNumOfFighters();
						planetCarriers[m.getStarID()][m.getPlanetID()] = m.getNumOfCarriers();
						planetCommand[m.getStarID()][m.getPlanetID()] = m.getNumOfCommand();
						for (ServerSender sender : senders) {
							sender.sendMessage(Message.createSetShips(m.getStarID(), m.getPlanetID(),
									m.getNumOfFighters(), m.getNumOfCarriers(), m.getNumOfCommand()));
						}
					}
				}
				i++;
			}
		}
	}

}
