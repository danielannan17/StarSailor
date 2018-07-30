package main;

/**
 * holds the state of the current game
 * 
 * 
 */
public class State {

	public static STATE state = STATE.MENU_MAIN;

	public static final int MAIN = 1, PLAY = 2, HOST = 3, CONNECT = 4, INSTANT = 5, OPTIONS = 6, EXIT = 7,
			SPEND_CRYSTAL = 8, BRIBE = 10, TRADE = 11, PURCHASE = 12;

	public static enum STATE {
		MENU_MAIN, MENU_PLAY, MENU_HOST, MENU_CONNECT, MENU_INSTANT, MENU_OPTIONS, MENU_BATTLE, MENU_PAUSE, GAME_GALACTIC, GAME_SOLAR, GAME_PLANETARY, GAME_BATTLE, INSTANT_BATTLE
	};

	/**
	 * changes the games state
	 * 
	 * @param s
	 *            the new state
	 */
	public static void changeState(STATE s) {
		state = s;
	}

}
