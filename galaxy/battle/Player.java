package galaxy.battle;

import handlers.InputHandler;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * the player
 * 
 * 
 * 
 *
 */
public class Player extends Entity {
	
	
	/**
	 * creates a new player
	 * 
	 * @param path
	 *            the path to the sprites to use for the player
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param animationTime
	 *            the animation time
	 */
	public Player(String path, float x, float y, int width, int height, long animationTime) {
		super(path, x, y, width, height, animationTime, 0, 0, 0, 6, 0, 0, 0);
		this.setTarget(new Point2D.Double(InputHandler.getMousePositionOnScreen().x,
				InputHandler.getMousePositionOnScreen().y));
		totalHealth = 500;
		currentHealth = totalHealth;
		damage = 30;
		speed = 5.0f;
		shotInterval = 500;
		prevShoot = System.currentTimeMillis();
	}

	@Override
	public boolean update(float time, int width, int height, List<Entity> team, Battle battle) {
		super.update(time, width, height, team, battle);
		this.setTarget(new Point2D.Double(InputHandler.getMousePositionOnScreen().x,
				InputHandler.getMousePositionOnScreen().y));
		return false;
	}

	protected void executeAGGRESSIVEBehaviour() {
	}

	protected void executeDEFENSIVEBehaviour() {
	}

	protected void executeTACTICALBehaviour() {
	}

	protected void executeALTERNATIVEBehaviour() {
	}

	protected void executeKINGCRUSHERBehaviour() {
	}

	@Override
	protected void executeBUGFIXERBehaviour() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void executeSURVIVORBehaviour(List<Entity> team, Battle battle, String shipImage) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return "Player"
	 */
	
	@Override
	public String getShipType(){
		return "Player";
	}
}
