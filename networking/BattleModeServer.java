package networking;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import entities.BattleMessage;
import instantBattle.BattleController;
/**
 * 
 * Server for the game in instant battle mode
 */
public class BattleModeServer implements Runnable{
	
	
	private int port;
	private ServerSocket serverSocket;
	private int numOfPlayers;
	private static boolean running;
	public boolean playersConnected;
	private BattleController controller;
	public static final int MAXPORT = 65535;
	public static final int TICKRATE = 20;
	private static Socket[] players;
	private static int maxPlayer = 2;
	private static BlockingQueue<KeyMove> moveQueue;
	private static ServerSender[] senders;
	private static ServerState state;
	public static boolean stateChanged;
	private static ServerReceiver[] receivers;;
	private static int[] player1Ships;
	private static int[] player1Ship;
	private static Color player1Colour;
	private static int[] player2Ships;
	private static int[] player2Ship;
	private static Color player2Colour;
	private static int player1X, player1Y, player2X, player2Y;
	private static KeyVal[] player1Move, player2Move;

	
	/**
	 * Constructor method
	 */
	public BattleModeServer(){
		 running = true;
		 numOfPlayers = 0;
		 port = 49152;
		moveQueue = new LinkedBlockingQueue<KeyMove>();
		receivers = new ServerReceiver[maxPlayer];
		stateChanged = false;
		state = ServerState.WAITING;
		boolean portSet = false;
		playersConnected = false;
		while (!portSet && port <= MAXPORT){
			try{
				serverSocket = new ServerSocket(port);
				portSet = true;
			}
			catch(IOException e){
				port++;
			}
		}
		if (!portSet){
			System.err.println("Couldn't listen on any ports");
			System.exit(1);
		}
		players = new Socket[maxPlayer];
		senders = new ServerSender[maxPlayer];
		for (int i = 0; i < maxPlayer; i++){
			players[i] = null;
		}
	}

	
	/**Get index of an available cell in the player array (-1 if none)
	 * 
	 * @return Available index
	 */
	public static int availableID(){
		for (int i = 0; i < maxPlayer; i++){
			if (players[i] == null){
				return i;
			}
		}
		return -1;
	}

	
	/**Change the state of the server
	 * 
	 * @param newState The new state
	 */
	public static void changeState(ServerState newState) {
		System.out.println("Changing state from " + state + " to " + newState);
		state = newState;
		stateChanged = true;
	}
	
	
	/**Get player 1's colour
	 * 
	 * @return Player 1 colour
	 */
	public static Color getPlayer1Colour() {
		return player1Colour;
	}
	
	
	/**Get player 1's most recent move
	 * 
	 * @return Most recent move
	 */
	public static KeyVal[] getPlayer1Move() {
		
		KeyVal[] x = player1Move.clone();
		player1Move = null;
		return x;
	}
	
	
	/**Get the x coordinate of player 1's mouse
	 * 
	 * @return Player 1 x
	 */
	public static int getPlayer1X() {
		return player1X;
	}
	
	
	/**Get the y coordinate of player 1's mouse
	 * 
	 * @return Player 1 y
	 */
	public static int getPlayer1Y() {
		return player1Y;
	}
	
	
	/**Get player 2's colour
	 * 
	 * @return Player 2 colour
	 */
	public static Color getPlayer2Colour() {
		return player2Colour;
	}
	
	
	/**Get player 2's most recent move
	 * 
	 * @return Player 2 move
	 */
	public static KeyVal[] getPlayer2Move() {
		KeyVal[] x = player2Move;
		player2Move = null;
		return x;
	}
	
	
	/**Get player 2's ships types
	 * 
	 * @return Ship types
	 */
	public static int[] getPlayer2Ships() {
		return player2Ships;
	}
	
	
	/**Get player 2's skills
	 * 
	 * @return Skills
	 */
	public static int[] getPlayer2Skills() {
		return player2Ship;
	}
	
	
	/**Get the x coordinate of player 2's mouse
	 * 
	 * @return Player 2 x
	 */
	public static int getPlayer2X() {
		return player2X;
	}


	/**Get the y coordinate of player 2's mouse
	 * 
	 * @return Player 2 y
	 */
	public static int getPlayer2Y() {
		return player2Y;
	}
	
	
	/**
	 * Get the queue of actions from the client
	 * 
	 * @return The action queue
	 */
	public static KeyMove[] getQueue() {
		KeyMove s[] = new KeyMove[moveQueue.size()];
		for (int i = 0; i < s.length; i++) {
			s[i] = moveQueue.poll();
		}
		moveQueue.clear();
		return s;
	}
	
	
	/**Prepare to play a game
	 * 
	 * @param id Player's ID
	 * @param ships Player's ship types
	 * @param playerShip Player's skill types
	 * @param colour Player's colour
	 */
	public static void makeReady(int id, int[] ships, int[] playerShip, Color colour){
		switch (id){
		case 1:
			player1Ships = ships;
			player1Ship = playerShip;
			player1Colour = colour;
			changeState(ServerState.INITIALISING);
			break;
		case 2:
			player2Ships = ships;
			player2Ship = playerShip;
			player2Colour = colour;
			changeState(ServerState.READY);
			break;
		}
	}
	
	public static void sendEnd(int winner) {
		System.out.println("Sending end");
		senders[0].sendEnd(winner);
		senders[1].sendEnd(winner);
	}
	
	/**Remove a player from the game and wait for a new one
	 * 
	 * @param i ID of the player
	 */
	public static void removePlayer(int i){
		players[i] = null;
		senders[i] = null;
		receivers[i] = null;
		changeState(ServerState.WAITING);
	}
	
	
	/**Send the state of the battle to both clients
	 * 
	 * @param Es Battle state
	 */
	public static void sendBattleState(ArrayList<BattleMessage> Es) {
			senders[0].sendBattleState(Es);
			senders[1].sendBattleState(Es);
		
	}
	
	
	/**Set player 1's most recent move
	 * 
	 * @param player1Move Most recent move
	 */
	public static void setPlayer1Move(KeyVal[] player1Move) {
		BattleModeServer.player1Move = player1Move;
	}
	
	
	/**Set the x coordinate of player 1's mouse
	 * 
	 * @param player1x Player 1 x
	 */
	public static void setPlayer1X(int player1x) {
		player1X = player1x;
	}
	
	
	/**Set the y coordinate of player 1's mouse
	 * 
	 * @param player1y Player 1 y
	 */
	public static void setPlayer1Y(int player1y) {
		player1Y = player1y;
	}
	
	
	/**Set player 2's most recent move
	 * 
	 * @param player2Move Player 2 move
	 */
	public static void setPlayer2Move(KeyVal[] player2Move) {
		BattleModeServer.player2Move = player2Move;
	}
	
	
	/**Set the x coordinate of player 2's mouse
	 * 
	 * @param player2x Player 2 x
	 */
	public static void setPlayer2X(int player2x) {
		player2X = player2x;
	}
	
	
	/**Set the y coordinate of player 2's mouse
	 * 
	 * @param player2y Player 2 y
	 */
	public static void setPlayer2Y(int player2y) {
		player2Y = player2y;
	}
	
	
	/**Run the receiver threads for all the clients*/
	public void addReceivers() {
		for (int i = 0; i < players.length; i++){
			try{
				receivers[i] = new ServerReceiver(i + 1, new BufferedReader(new InputStreamReader(players[i].getInputStream())), moveQueue);
				receivers[i].start();
			}
			catch (IOException e){
				System.err.println("Couldn't make ServerReceiver " + i + ": " + e.getMessage());
				System.exit(1);
			}
		}
	}
	
	
	/**
	 * Get the IP address of the server
	 * 
	 * @return The IP
	 */
	public String getIP(){
		//COMMENT THIS TRY OUT TO RUN ON WIFI
		/*
		try {
			return Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		*/
		//COMMENT THIS TRY OUT TO RUN IN THE LAB
		try{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()){
				NetworkInterface n = interfaces.nextElement();
				Enumeration<InetAddress> ips = n.getInetAddresses();
				while (ips.hasMoreElements()){
					InetAddress address = ips.nextElement();
					if (!address.isLoopbackAddress() && !address.isLinkLocalAddress()){
						return address.getHostAddress();
					}
				}
			}
			return Inet4Address.getLocalHost().getHostAddress();
		}
		catch (SocketException e){
			System.err.println(e.getMessage());
			System.exit(1);
		} catch (UnknownHostException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return null; //but I don't think we should ever get here
		
	}
	
	
	/**Get player 1's ship types
	 * 
	 * @return Ship types
	 */
	public int[] getPlayer1Ships(){
		return player1Ships;
	}
	
	
	/**Get player 1's skill types
	 * 
	 * @return Skill types
	 */
	public int[] getPlayer1Skills(){
		return player1Ship;
	}
	
	
	/**
	 * Get the port number that the server is listening on
	 * 
	 * @return The port
	 */
	public int getPort(){
		return port;
	}
	
	
	/**Get the state of the game
	 * 
	 * @return The state
	 */
	public ServerState getState(){
		stateChanged = false;
		return state;
	}
	
	
	/**Stop the server from running*/
	public static void kill(){
		try {
		for (ServerReceiver receiver: receivers){
			if (receiver != null){
				receiver.kill();
			}
		}
		for (ServerSender sender: senders){
			if (sender != null){
				sender.kill();
			}
		}}
		catch (Exception e) {
			
		}
		running = false;
	}
	
	@Override
	public void run() {
		while (running) {
			switch (state) {
			case WAITING:
				waitForPlayers();
				sleep(TICKRATE);
				break;
			case CONNECTED:
				sleep(TICKRATE);
				break;	
			case READY:
				sleep(TICKRATE);
				break;	
			case INITIALISING:
				senders[0].sendInitData(player2Ship[0], player2Colour);
				senders[1].sendInitData(player1Ship[0], player1Colour);
				player1Ship[4] = 1;
				player2Ship[4] = 2;
				controller = new BattleController(player1Ship,player1Ships,player2Ship,player2Ships);
				senders[0].sendStart();
				senders[1].sendStart();
				Thread thread = new Thread(controller);
				thread.start();
				sleep(1000/60);
				try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case PLAYING:
				break;
			}
		
		}
	}
	
	
	/**Sleep
	 * 
	 * @param time Number of milliseconds to sleep
	 */
	private void sleep(long time) {
		try{
			Thread.sleep(time);
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Block until enough players have connected for a game
	 */
	private void waitForPlayers(){
		if (numOfPlayers < maxPlayer){
			try {
				Socket client = serverSocket.accept();
				PrintStream toClient = new PrintStream(client.getOutputStream());
				senders[numOfPlayers] = new ServerSender(toClient);
				senders[numOfPlayers].start();
				players[numOfPlayers] = client;
				numOfPlayers++;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			addReceivers();
			changeState(ServerState.CONNECTED);
		}
		playersConnected = true;
	}
	
}