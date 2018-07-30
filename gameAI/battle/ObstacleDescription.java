package gameAI.battle;

import entities.Vector2D;

public class ObstacleDescription extends ObjectDescription{

	entities.Obstacle fObstacle;

	/**
	 * Constructs a description for the AI to use from a given obstacle
	 * @param _obstacle 	= the given obstacle
	 */

	public ObstacleDescription(entities.Obstacle _obstacle){
		super("obstacle");
		this.fObstacle = _obstacle;
	}

	/**
	 * @return the x coord of the given obstacle
	 */

	@Override
	public int getXLocation() {
		return this.fObstacle.getxLocation();
	}

	/**
	 * @return the y coord of the given obstacle
	 */
	
	@Override
	public int getYLocation() {
		return this.fObstacle.getyLocation();
	}
	
	/**
	 * @return velocity of the given obstacle
	 */
	
	@Override
	public Vector2D getVelocity() {
		return this.fObstacle.getVelocity();
	}
}
