package networking;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

import entities.BattleMessage;
import entities.Entity;
import entities.StopMessage;
import instantBattle.Battle;
import main.Main;


/**
 * 
 * 
 * 
 * Receives messages from the server and passes them on to the client
 *
 */
public class ClientReceiver extends Thread{

	
	private ObjectInputStream server;
	private boolean running;
	
	
	/**
	 * Constructor method
	 * 
	 * @param server A stream for receiving messages from the server
	 */
	public ClientReceiver(InputStream server){
		
		try {
			this.server = new ObjectInputStream(new BufferedInputStream(server));
			running  = true;
		} catch (IOException e) {
			System.err.println("Couldn't get ClientReceiver: " + e.getMessage());
			System.exit(1);
		}
	}
	
	
	/**Stop this thread running*/
	public void kill(){
		running = false;
	}
	
	
	@Override
	public void run(){
		try{
			while (running){
				switch (BattleModeClient.getState()) {
				case PLAYING:
					Object msg = server.readObject();
					if (ArrayList.class.isInstance(msg)) {
						ArrayList<BattleMessage> battleState = (ArrayList<BattleMessage>) msg;
						Main.battle.setCurrentState(battleState);
					} else if (StopMessage.class.isInstance(msg)) {
						System.out.println("Got ENd");
						BattleModeServer.changeState(ServerState.END);
						Battle.winner = ((StopMessage) msg).getWinner();
					}
					break;
					
				default:
					@SuppressWarnings("unchecked")
					Message msg1 = (Message) server.readObject();
					switch (msg1.type){
						case INITDATA: 
							BattleModeClient.initialise(msg1.initData.type, msg1.initData.colour);
							break;
						case 
						START: 
							BattleModeClient.setState(ServerState.PLAYING);
							break;
					}
 				}
				Thread.sleep(1000/60);
			}
		}
		catch (IOException e){
			System.err.println("Server died: " + e.getMessage());
			kill();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		} 
		return;
	}
	
	
}
