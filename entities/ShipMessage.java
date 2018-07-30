package entities;
/**
 * a class to represent ship messages
 */
import java.util.ArrayList;

public class ShipMessage extends BattleMessage{
	
	/**
	 * get the type of the message
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * get the x value
	 * @return the x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * get the y value
	 * @return the y value
	 */
	public int getY() {
		return y;
	}

	/**
	 * get the team
	 * @return the team
	 */
	public int getTeam() {
		return team;
	}

	/**
	 * get the direction
	 * @return the direction
	 */
	public double getDirection() {
		return direction;
	}

	/**
	 * get the health
	 * @return the health
	 */
	public float getHealth() {
		return health;
	}

	/**
	 * get the armour
	 * @return the armour
	 */
	public float getArmor() {
		return armor;
	}

	/**
	 * get the buffs
	 * @return the buffs
	 */
	public ArrayList<Integer> getBuffs() {
		return buffs;
	}

	int type,x,y;
	int team;
	double direction;
	float health,armor;
	ArrayList<Integer> buffs;
	
	/**
	 * create a new ship message
	 * @param ship the ship to create for
	 */
	public ShipMessage(Ship ship) {
		messageType = 0;
		frame = ship.getFrame();
		animation = ship.getAnimation();
		type = ship.getType();
		team = ship.getTeam();
		direction = ship.getDirection();
		x = ship.getxLocation();
		y = ship.getyLocation();
		health = ship.getHealth();
		armor = ship.getArmour();
	
	}

	/**
	 * get the current frame
	 * @return the frame
	 */
	public int getFrame() {
		return frame;
	}
	
	/**
	 * get the animation
	 * @return the animation
	 */
	public int getAnimation() {
		return animation;
	}
	

}
