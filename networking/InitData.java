package networking;


import java.awt.Color;
import java.io.Serializable;


/**Initialisation data in the game*/
public class InitData implements Serializable {

	
	public final int type;
	public final Color colour;
	
	
	/**
	 * 
	 * @param type Player's ship type
	 * @param colour Player's colour
	 */
	public InitData(int type, Color colour){
		this.type = type;
		this.colour = colour;
	}
	
	
	
	
	
}
