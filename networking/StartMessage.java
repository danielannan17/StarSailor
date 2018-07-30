package networking;


import networking.Message;


/**Message to let the client know it's time to start the game*/
public class StartMessage extends Message {

	
	/**Constructor method*/
	public StartMessage(){
		super(MessageType.START, null);
	}
	
	
}
