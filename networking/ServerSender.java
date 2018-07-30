package networking;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import entities.BattleMessage;
import entities.Entity;
import entities.Ship;
import entities.StopMessage;


/**
 * 
 * 
 * 
 * Sends messages from the server to the client
 *
 */
public class ServerSender extends Thread{

	
	private ObjectOutputStream client;
	private boolean running;
	
	
	/**
	 * Constructor method
	 * 
	 * @param client Stream to print objects to the client
	 */
	public ServerSender(OutputStream client){
		try {
			this.client = new ObjectOutputStream(client);
			 running = true;
		} catch (IOException e) {
			System.err.println("Couldn't create ServerSender: " + e.getMessage());
			System.exit(1);
		}
	}
	
	
	/**Send game initialisation data to the client
	 * 
	 * @param type Enemy's ship type
	 * @param colour Enemy's colour
	 */
	public synchronized void sendInitData(int type, Color colour){
		Message data = new InitMessage(new InitData(type, colour));
		try{
			client.writeObject(data);
			client.reset();
		}
		catch (IOException e){
			System.err.println("Couldn't communicate with client");
		}
	}
	
	
	/**Send a start message to let the client know it's time to start the game*/
	public synchronized void sendStart(){
		try{
			client.writeObject(new StartMessage());
			client.reset();
		}
		catch (IOException e){
			System.err.println("Couldn't communicate");
		}
	}

	
	/**Stop the thread from running*/
	public void kill(){
		running = false;
	}
	
	
	@Override
	public void run(){
		try{
			while (running){
				Thread.sleep(1000/60);
			}
		}
		catch (InterruptedException e){
			System.err.println(e.getMessage());
		}
		return;
	}

	
	/**Send the state of the battle to the client
	 * 
	 * @param e Battle state
	 */
	public void sendBattleState(ArrayList<BattleMessage>  e) {
		try {
			client.flush();
			client.writeObject(e);
			client.reset();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}


	public void sendEnd(int winner) {
		try {
			
			client.writeObject(new StopMessage(winner));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	
}
