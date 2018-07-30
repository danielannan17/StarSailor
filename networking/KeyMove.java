package networking;


/**Represents key presses in the game*/
public class KeyMove {
	

	public final KeyVal key;
	public final int playerID;
	
	
	/**Constructor
	 * 
	 * @param key The key pressed
	 * @param playerID The ID of the player who pressed it
	 */
	public KeyMove(KeyVal key, int playerID) {
		super();
		this.key = key;
		this.playerID = playerID;
	}	
	
	
}
