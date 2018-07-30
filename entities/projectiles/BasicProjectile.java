package entities.projectiles;

/***
 * a class to represent basic projectiles
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entities.Vector2D;
import handlers.ResourceHandler;

public class BasicProjectile extends Projectile {
	
	/**
	 * draw the projectile
	 * @param g the graphics object
	 */
	protected void drawHitbox(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(250, 0, 0, 150));
		g2d.fill(hitbox);
		g2d.draw(getHitBox());	
	}
	
	/**
	 * create a basic projectile
	 * @param id the id
	 * @param xLocation the x location
	 * @param yLocation the y location
	 * @param direction the direction
	 * @param damage the damage of the projectile
	 * @param targetTeam the team the projectile is targeting
	 * @param velocity the velocity of the projectile
	 * @param sprite the buffered image of the projectile
	 */
	public BasicProjectile(int id, int xLocation, int yLocation,double direction,float damage, int targetTeam, Vector2D velocity, BufferedImage[][] sprite) {
		super(id, xLocation, yLocation,velocity, targetTeam, damage);
		this.maxVelocity = 10;
		this.setSprite(sprite);
		setDirection(direction);
		this.damage = damage;
	}
	
	/**
	 * creates a basic projectile - for testing use only
	 */
	public BasicProjectile() {
		super(22,0,0,new Vector2D(0,0),0,0);
		setSize(new Dimension(10,10));
	}
	
	/**
	 * creates a basic projectile - for testing only
	 * @param x the x coord
	 * @param y the y coord
	 * @param vel the velocity of the basic projectile
	 * @param id the id
	 */
	public BasicProjectile(int x,int y,Vector2D vel,int id) {
		super(id,x,y,vel,0,0);
		setSize(new Dimension(10,10));
	}
	
	/**
	 * get the damage of the projectile - then flag it for removal
	 * @return the damage value of the projectile
	 */
	public float dealDamage(){
		remove = true;
		return this.damage;
	}

	


}
