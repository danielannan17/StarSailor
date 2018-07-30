package entities.projectiles;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import entities.Entity;
import entities.Ship;
import entities.Vector2D;
/***
 * class to represent dot projectiles
 *
 */
public class DotProjectile extends Projectile {
		int ticksPerSecond = 1;
		HashMap<Entity,Long> cantHit;
	/**
	 * create a dot projectile
	 * @param id the id
	 * @param xLocation the x location
	 * @param yLocation the y location
	 * @param direction the direction of the projectile
	 * @param damage the damage of the projectile
	 * @param ticksPerSecond the number of ticks per second of the simulation
	 * @param targetTeam the target team of the projectile
	 * @param velocity the velocity of the projectile
	 * @param sprite the buffered image of the projectile
	 */
	public DotProjectile(int id, int xLocation, int yLocation,double direction,float damage, int ticksPerSecond, int targetTeam,
			Vector2D velocity, BufferedImage[][] sprite) {
		super(id, xLocation, yLocation, velocity, targetTeam, damage);
		this.ticksPerSecond=ticksPerSecond;
		this.maxVelocity = 10;
		setSprite(sprite);
		System.out.println("First " + sprite.length);
		System.out.println("Sec " + sprite[0].length);
		setDirection(direction);
		cantHit = new HashMap<Entity,Long>();
		
	}
	
	/**
	 * deal damage
	 * @param e the entity to damage
	 * @return the damage dealt
	 */
	public float dealDamage(Entity e) {
		if (cantHit.get(e) != null) {
			if (cantHit.get(e) + (1000/ticksPerSecond) > System.currentTimeMillis())
				return 0;
		}
		cantHit.put(e, System.currentTimeMillis());
		System.out.println(((Ship) e).getId() + "  " + damage);
		return damage;
	}
}
