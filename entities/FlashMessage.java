package entities;

import entities.skills.Flash;

/***
 * a class to represent flash messages
 *
 */

public class FlashMessage extends BattleMessage {
	
	int id,x1,y1,x2,y2;
	
	/**
	 * create a new flash message
	 * @param id the id of the message
	 * @param x1 the first x value
	 * @param y1 the first y value
	 * @param x2 the second x value
	 * @param y2 the second y value
	 */
	public FlashMessage(int id, int x1, int y1, int x2, int y2) {
		messageType = 2;
		this.id = id;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.frame = 0;
		this.animation = 0;
	}

	/**
	 * get the id of the message
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * get the first x value of the message
	 * @return the x value
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * get the first y value of the message
	 * @return the first y value
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * get the second x value of the message
	 * @return the second x value
	 */
	public int getX2() {
		return x2;
	}

	/**
	 * get the second y value of the message
	 * @return the second y value
	 */
	public int getY2() {
		return y2;
	}
	
	/**
	 * update the message
	 * @return success?
	 */
	public boolean update() {
		if (System.currentTimeMillis() - prevTime >= animationTime) {
			incrementFrame();
			prevTime = System.currentTimeMillis();
		}
		return false;
	}
	
	/**
	 * increment the frame
	 */
	public void incrementFrame() {
		if (frame < Flash.startFrames.length-1) {
			frame++;
		} else {
			frame= -1;
		}
	}

}
