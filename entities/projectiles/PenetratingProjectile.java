package entities.projectiles;

/***
 * class to represent projectiles with penetrating power
 */
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.AvailableSkill;
import entities.Entity;
import entities.Vector2D;

public class PenetratingProjectile extends Projectile {
	int penetration;
	ArrayList<Entity> cantHit;
	
	/**
	 * create a new penetrating projectile
	 * @param id the id
	 * @param xLocation the x location
	 * @param yLocation the y location
	 * @param direction the direction
	 * @param damage the damage of the projectile
	 * @param penetration the number of entities that will be penetrated
	 * @param targetTeam the target team of the projectile
	 * @param velocity the velocity of the projectile
	 * @param sprite the buffered image of the projectile
	 */
	public PenetratingProjectile(int id, int xLocation, int yLocation,double direction,float damage, int penetration, int targetTeam, Vector2D velocity, BufferedImage[][] sprite) {
		super(id, xLocation, yLocation, velocity, targetTeam, damage);
		this.maxVelocity = 10;
		cantHit = new ArrayList<Entity>();
		this.penetration = penetration;
		this.setSprite(sprite);
		this.damage = damage;
		setDirection(direction);
	}
	
	/**
	 * deal damage to an entity
	 * @param e the entity to damage
	 * @return the damage dealt
	 */
	public float dealDamage(Entity e){
		if (cantHit.contains(e)) {
			return 0;
		} else {
			cantHit.add(e);
			penetration--;
			if (penetration <= 0) { 
				remove = true;
			}
			return this.damage;
		}
		
	}
	
	
}
