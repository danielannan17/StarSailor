package entities.projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import entities.Entity;
import entities.Vector2D;

public abstract class Projectile extends Entity {
	/***
	 * abstract class to represent projectiles
	 */
	private static final long serialVersionUID = 9173976181415700271L;
	boolean remove = false;
	Vector2D velocity;
	protected int speed;
	protected int targetTeam;
	protected float damage = 0;
	int effect = 0;
	public int id = 0;
	
	/**
	 * draw the hitbox of the projectile
	 * @param g the graphics object
	 */
	protected void drawHitbox(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(250, 0, 0, 150));
		g2d.fill(hitbox);
		g2d.draw(getHitBox());	
	}
	
	/**
	 * create a projectile
	 * @param id the id
	 * @param xLocation the x location 
	 * @param yLocation the y location
	 * @param velocity the velocity of the projectile
	 * @param targetTeam the target team of the projectile
	 * @param damage the damage that the projectile will deal
	 */
	public Projectile(int id, int xLocation, int yLocation, Vector2D velocity,int targetTeam, float damage) {
		super(xLocation, yLocation);
		this.id = id;
		this.setVelocity(velocity);
		this.damage = damage;
		this.targetTeam = targetTeam;
		
	}
	/**
	 * should this projectile be removed?
	 * @return remove projectile?
	 */
	public boolean isRemove() {
		return remove;
	}
	
	/**
	 * get the target team of the projectile
	 * @return the target team
	 */
	public int getTargetTeam() {
		return targetTeam;
	}

	/**
	 * get the effect of the projectile
	 * @return the effect
	 */
	public int getProjectileEffect(){
		return this.effect;
	}
	
	/**
	 * get the damage that the projectile will deal
	 * @return
	 */
	public float getProjectileDamage(){
		return this.damage;
	}

	/**
	 * get the id of the projectile
	 * @return the id
	 */
	public int getProjectileId() {
		return id;
	}

	/**
	 * flag this projectile to be removed
	 */
	public void remove() {
		remove = true;
	}
	
	/**
	 * gets the ID of the projectile
	 * @return the id
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the targetTeam - used for testing
	 * @param team The team the projectile should target
	 */
	public void setTargetTeam(int team) {
		targetTeam = team;
	}
}


