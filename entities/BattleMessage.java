package entities;
/***
 * an abstract class to represent battle messages
 */

import java.io.Serializable;

public abstract class BattleMessage implements Serializable {
	int messageType;
	protected int frame;
	protected int animation;
	protected transient long prevTime;
	protected transient long animationTime = 200;

	/**
	 * get the type of the message
	 * @return the type
	 */
	public int getMessageType() {
		return messageType;
	}

	/**
	 * Returns the frame number
	 * @return frame
	 */
	public int getFrame() {
		return frame;
	}
	
	/**
	 * Returns the animation numer
	 * @return animation
	 */
	public int getAnimation() {
		return animation;
	}

}
