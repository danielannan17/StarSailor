package networking;


/**Message giving the client initialisation data*/
public class InitMessage extends Message {

	
	/**Constructor
	 * 
	 * @param data Initialisation data
	 */
	public InitMessage(InitData data){
		super(MessageType.INITDATA, data);
	}
	
	
}
