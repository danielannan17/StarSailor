package gameAI.battle;

import entities.Vector2D;

/***
 * abstract class to represent descriptions of objects
 *
 */

public abstract class ObjectDescription {
	
	private String type;
	
	/**
	 * create a new object description
	 * @param _type the type of the object description
	 */
	public ObjectDescription(String _type){
		this.type = _type;
	}
	public abstract int getXLocation();
	public abstract int getYLocation();
	public abstract Vector2D getVelocity();
}
