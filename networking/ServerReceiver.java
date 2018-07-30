package networking;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import entities.Vector2D;
import instantBattle.BattleController;
import main.Main;


/**
 * 
 * 
 * 
 * Listens for messages from the client and passes them on to the server
 *
 */
public class ServerReceiver extends Thread{


	private BufferedReader client;
	private boolean running;
	private BlockingQueue<KeyMove> moveQueue;
	private int id;
	
	
	/**
	 * Constructor method
	 * 
	 * @param client BufferedReader for receiving messages
	 * @param messageQueue A queue for processing messages
	 */
	public ServerReceiver(int id, BufferedReader client, BlockingQueue<KeyMove> moveQueue){
		 running = true;
		this.id = id;
		this.client = client;
		this.moveQueue = moveQueue;
	}

	
	/**Stop the thread from running*/
	public void kill(){
		running = false;
	}
	
	
	@Override
	public void run(){
		while (running){
			try {
				Thread.sleep(1000/60);
				String data = null;
		
				if (client.ready()) {
					data = client.readLine();
					
				}
				if(data != null){
					
					if (data.startsWith("k,")){
						String[] d = data.split(",");
						KeyVal[] moves = new KeyVal[d.length - 2];
				
						for (int i = 1; i < d.length - 1; i++){
							moves[i - 1] = KeyVal.fromString(d[i]);
							switch(d[d.length - 1]){
							case "1": BattleModeServer.setPlayer1Move(moves); break;
							case "2": BattleModeServer.setPlayer2Move(moves); break;
							}
							moveQueue.add(new KeyMove(KeyVal.fromString(d[i]), Integer.parseInt(d[d.length - 1])));
						}
					}
					else if (data.startsWith("m,")){
						String[] d = data.split(",");
						switch (d[2]){
						case "1":

							BattleController.player1.setDirection(Double.parseDouble(d[1]));
							
	
							break;
						case "2":
							BattleController.player2.setDirection(Double.parseDouble(d[1]));
							

							break;
						}
					}
					else if (data.startsWith("READY")){
						
						String[] msg = data.substring(6).split(",");
						String[] sships = msg[0].split(":");
						int[] ships = new int[sships.length];
						for (int i = 0; i < sships.length; i++){
							ships[i] = Integer.parseInt(sships[i]);
						}
					
						String[] sSkills = msg[1].split(":");
						int[] playerShip = new int[sSkills.length];
						for (int i = 0; i < sSkills.length; i++){
							playerShip[i] = Integer.parseInt(sSkills[i]);
						}
						Color colour = BattleModeClient.stringToColor(msg[2]);
						BattleModeServer.makeReady(id, ships, playerShip, colour);
					}
					else if (data.equals("UNREADY")){
						BattleModeServer.changeState(ServerState.CONNECTED);
					}
					else if (data.equals("START")){
						BattleModeServer.changeState(ServerState.PLAYING);
					}
					else if (data.startsWith("leaving,")){
						int id = Integer.parseInt(data.split(",")[0]);
						BattleModeServer.changeState(ServerState.WAITING);
					}
				}
			}
			catch (IOException e) {
				System.err.println("Failed to get message from client: " + e.getMessage());
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}
		return;
	}
	
	
}
