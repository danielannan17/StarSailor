package networking;


import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import instantBattle.Battle;
import main.Main;
import menu.InstantMenu;


/**
 * 
 * 
 * 
 * Client for playing the game in instant battle mode
 *
 */
public class BattleModeClient implements Runnable{

	
	private String name;
	private int id;
	private Socket serverSocket;
	private PrintStream toServer;
	private InputStream fromServer;
	private ClientReceiver receiver;
	private boolean running;
	private static ClientSender sender;
	private static ServerState state;
	
	
	/**
	 * Constructor method
	 * 
	 * @param id Numerical id of the client
	 * @param name The username of the player
	 * @param host The IP address of the server
	 * @param port The port number that the server is listening on
	 * 
	 * @throws IOException If it can't connect to the server
	 */
	public BattleModeClient(int id, String name, String host, int port) throws IOException, UnknownHostException{
		this.id = id;
		this.name = name;
		state = ServerState.WAITING;
		running = true;
		serverSocket = new Socket(host, port);
		
		toServer = new PrintStream(serverSocket.getOutputStream());
		fromServer = serverSocket.getInputStream();

		sender = new ClientSender(toServer);
		receiver = new ClientReceiver(fromServer);
		
	}


	/**A helper for representing colours as strings*/
	public static String colorToString(Color colour){
		return String.valueOf(colour.getRGB());
	}

	
	/**Get the state of the game
	 * 
	 * @return Game state
	 */
	public static ServerState getState() {
		return state;
	}
	

	/**Initialise the game with relevant date
	 * 
	 * @param type Enemy type
	 * @param tColour Enemy colour
	 */
	public static void initialise(int type, Color tColour){
		Main.battle = new Battle(InstantMenu.getMyShipSprites(), type, tColour);
	}
	
	
	/**Set the state of the game
	 * 
	 * @param state New state
	 */
	public static void setState(ServerState state) {
		BattleModeClient.state = state;
	}
	
	
	/**Helper to get a colour from a string
	 * 
	 * @param s String representing a colour
	 * @return Corresponding colour
	 */
	public static Color stringToColor(String s){
		return new Color(Integer.parseInt(s));
	}
	
	
	/**Helper string[] join method
	 * 
	 * @param delimiter Delimiter for string
	 * @param s Array of strings to be joined
	 * @return Joined string
	 */
	private String join(String delimiter, int[] s){
		String res = "";
		for (int i = 0; i < s.length; i++){
			res += s[i] + (i == s.length - 1 ? "" : delimiter);
		}
		return res;
	}
	
	
	/**Stop the client running*/
	public void kill(){
		sender.kill();
		receiver.kill();
		running = false;
	}
	

	/**Let the server know we're leaving the game*/
	public void leave(){
		sender.sendString("leaving," + id);
	}
	
	
	@Override
	public void run() {

		while (running) {
			sender.start();
			sender.sendString(name);
			receiver.start();

			while (state != ServerState.PLAYING) {
				if (state == ServerState.END) {
					kill();
				}
				try {
					Thread.sleep(BattleModeServer.TICKRATE);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("game started");
			
			
			while (running){
			
			}
		}
	}
	
	
	/**Pass key presses on to the server
	 * 
	 * @param ks The keys
	 */

	public void sendKeys(Object[] ks){
		sender.sendKeys(ks, id);
	}
	
	
	/**Send the mouse direction to the server
	 * 
	 * @param d The mouse direction
	 */
	public void sendMouseDirection(float d){
		sender.sendString("m,"+ -d +","+ id);

	}
	

	/**Let the server know we're ready to play
	 * 
	 * @param ships Player's ship types
	 * @param skills Player's skill types
	 * @param colour Player's colour
	 */
	public void sendReady(int[] ships, int[] skills, Color colour){
		switch (state){
		case CONNECTED: state = ServerState.READY; break;
		case READY: state = ServerState.INITIALISING; break;
		default: System.err.println("Unexpected state");
		}
		sender.sendString("READY," + join(":", ships) + "," + join(":", skills) + "," + colorToString(colour));
	}
	
	
	/**
	 * Send a message to the server
	 * 
	 * @param s The message
	 */
	public void sendString(String s){
		sender.sendString(s);
	}
	
	
	/**Let the server know we're not ready to play any more*/
	public void unready(){
		sender.sendString("UNREADY");
	}
	
	
}
