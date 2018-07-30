package networking;


/**Represents values of key presses*/
public enum KeyVal {
	
	
	W, A, S, D, Q, E, SHIFT, BTN;

	
	/**Represent as string
	 * 
	 * @param key The key
	 * @return String representation
	 */
	public static String toString(KeyVal key){
		switch(key){
		case W: return "W";
		case A: return "A";
		case S: return "S";
		case D: return "D";
		case Q: return "Q";
		case E: return "E";
		case SHIFT: return "SHIFT";
		case BTN: return "BTN";
		default: return ""; //But this will never happen
		}
	}
	
	
	/**Convert from string
	 * 
	 * @param s A string
	 * @return Corresponding key value
	 */
	public static KeyVal fromString(String s){
		switch(s){
		case "W": return W;
		case "A": return A;
		case "S": return S;
		case "D": return D;
		case "Q": return Q;
		case "E": return E;
		case "SHIFT": 
			System.out.println("Shift");
			return SHIFT;
		case "BTN": return BTN;
		default: throw new RuntimeException("Unexpected key: " + s);
		}
	}
	
	
}
