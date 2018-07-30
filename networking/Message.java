package networking;


import java.io.Serializable;


/**Message from server to client*/
public abstract class Message implements Serializable{
	
	
	public final MessageType type;
	public final InitData initData;
	
	
	
	/**Constructor
	 * 
	 * @param type The type of message
	 * @param initData Initialisation data (null if this is not an initialisation message)
	 */
	public Message(MessageType type, InitData initData) {
		super();
		this.type = type;
		this.initData = initData;
	}
	
	
}
