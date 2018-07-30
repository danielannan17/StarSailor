package entities;

/***
 * a class to represent projectile messages
 */
import entities.projectiles.Projectile;

public class ProjectileMessage extends BattleMessage {
	int id,x,y,targetTeam;
	double direction;
	
	/**
	 * create a projectile message
	 * @param projectile the projectile to make the message for
	 */
	public ProjectileMessage(Projectile projectile) {
		messageType = 1;
		frame = projectile.getFrame();
		animation = projectile.getAnimation();
		id = projectile.getProjectileId();
		x = projectile.getxLocation();
		y = projectile.getyLocation();
		targetTeam = projectile.getTargetTeam();
		direction = projectile.getDirection();
	}

	/**
	 * get the id of the message
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * get the x value of the message
	 * @return the x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * get the y value of the message
	 * @return the y value
	 */
	public int getY() {
		return y;
	}

	/**
	 * get the target team of the message
	 * @return the target team
	 */
	public int getTargetTeam() {
		return targetTeam;
	}

	/**
	 * get the direction of the message
	 * @return the direction
	 */
	public double getDirection() {
		return direction;
	}

	/**
	 * get the frame of the message
	 * @return the frame
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * get the animation of the message
	 * @return the animation
	 */
	public int getAnimation() {
		return animation;
	}
	
}
