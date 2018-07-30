package entities;

import java.io.Serializable;

public class StopMessage implements Serializable {
	int type = 10, winner;
	
	public StopMessage(int winner) {
		this.winner = winner;
	}
	
	public int getWinner() {
		return winner;
	}
	

}
