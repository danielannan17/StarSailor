package networking;


import java.io.PrintStream;


/**
 * 
 * 
 * 
 * Sends messages from the client to the server
 *
 */
public class ClientSender extends Thread{

	
	private PrintStream server;
	private boolean running;
	

	/**
	 * Constructor method
	 * 
	 * @param server A stream for comms with the server
	 */
	public ClientSender(PrintStream server){
		this.server = server;
		 running = true;
	}
	
	
	/**
	 * Pass a string on to the server
	 * 
	 * @param s The string
	 */
	public void sendString(String s){
		server.println(s);
	}

	
	/**Send an array of keys to the server
	 * 
	 * @param ks The keypresses
	 * @param id Client's ID
	 */
	public void sendKeys(Object[] ks, int id){
		String sks = "";
		
		for (Object k: ks){
			
			sks += KeyVal.toString((KeyVal)k) + ",";
		}
			
		server.println("k," + sks + id);
		server.flush(); 	
	}
	
	
	/**Stop this thread running*/
	public void kill(){
		running = false;
	}
	
	
	@Override
	public void run(){
		try{
			while (running){

				Thread.sleep(1000/60);

				// Just sit and wait to be told to do things
			}
		}
		catch (InterruptedException e){
			System.err.println(e.getMessage());
		}
		return;
	}
	
	
}
