package entities;

/***
 * An abstract class to represent an entity which is something that is in the instant battle "world" and physics engine.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Entity implements Serializable{
	private static final long serialVersionUID = -370180323968977475L;
	protected long prevTime, animationTime = 500;
	protected int xLocation = 20, yLocation = 20, frame, animation;
	protected transient Vector2D position;
	protected transient Vector2D velocity;
	protected transient Vector2D acceleration;
	protected transient float maxVelocity;
	protected transient float maxAcceleration;
	protected Rectangle hitbox;
	protected transient BufferedImage[][] sprite;
	protected String id;
	protected Dimension size;
	protected double direction;
	
	/**
	 * get the size of the entity
	 * @return the size
	 */
	public Dimension getSize() {
		return size;
	}
	
	/**
	 * set the size of the entity
	 * @param size the size of the entity
	 */
	public void setSize(Dimension size) {
		this.size = size;
	}
	
	/**
	 * set the direction of the entity
	 * @param direction the direction in radians
	 */
	public void setDirection(double direction) {
		if (hitbox != null) {
			//rotatePolygon(hitbox, direction);
		}
		this.direction = direction;
	}
	protected CopyOnWriteArrayList<Vector2D> accelerationQueue;
	protected boolean hasAirResistance;
	//every physics tick, the average acceleration from queue is added to velocity and the queue is cleared.
	
	/**
	 * Construct for entity, used to construct obstacle.
	 * @param id The id of the object
	 * @param xLocation The x location of the object
	 * @param yLocation The y location of the object
	 * @param sprite the sprite used for the object
	*/
	public Entity(String id, int xLocation, int yLocation, BufferedImage[][] sprite) {
		this.id = id;
		
		setLocation(xLocation,yLocation);
		setPosition(new Vector2D(xLocation,yLocation));
		setSprite(sprite);
		this.velocity = new Vector2D(); //(0,0)
		this.maxVelocity = 1;
		this.acceleration = new Vector2D(); //(0,0)
		this.maxAcceleration = 0.5f;
		this.accelerationQueue = new CopyOnWriteArrayList<Vector2D>();
		
		this.hasAirResistance = true;
	}
	
	
	/**
	 * Constructor. Used to create a ship
	 * @param maxVelocity max velocity of the entity
	 * @param maxAcceleration acceleration of the entity
	 * @param sprite sprite used for the entity
	 */
	public Entity(float maxVelocity, float maxAcceleration, BufferedImage[][] sprite) {
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
		this.position = new Vector2D();
		this.velocity = new Vector2D();
		this.acceleration = new Vector2D();
		this.accelerationQueue = new CopyOnWriteArrayList<Vector2D>();
		setSprite(sprite);
		this.hasAirResistance = true;
	}
	
	/**
	 * used to create an entity
	 * @param maxVelocity the max velocity allowed by the physics engine
	 * @param maxAcceleration the max acceleration allowed by the physics engine
	 */
	public Entity(float maxVelocity, float maxAcceleration) {
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
		this.position = new Vector2D();
		this.velocity = new Vector2D();
		this.acceleration = new Vector2D();
		this.accelerationQueue = new CopyOnWriteArrayList<Vector2D>();
		this.hasAirResistance = true;
	}
	
	/**
	 * create an entity
	 * @param xLocation the x position
	 * @param yLocation the y position
	 */
	public Entity(int xLocation, int yLocation) {
		setxLocation(xLocation);
		setyLocation(yLocation);
		setPosition(new Vector2D(xLocation,yLocation));
		this.velocity = new Vector2D(); //(0,0)
		this.maxVelocity = 10;
		this.acceleration = new Vector2D(); //(0,0)
		this.maxAcceleration = 0.5f;
		this.accelerationQueue = new CopyOnWriteArrayList<Vector2D>();
		this.hasAirResistance = false;
	}
	
	/**
	 * Adds an acceleration to an entity. entities do not have a mass so this is equivelant to adding a force.
	 * 
	 * every physics tick, the average acceleration of the queue is found and applied to the entity.
	 * this is to allow multiple classes to apply an acceleration in a short space of time
	 * 
	 * @param acc the acceleration to apply to the entity.
	 */
	public void addAccelerationToQueue(Vector2D acc) {
		accelerationQueue.add(acc);
	}
	
	/**
	 * apply a single, instant force to an entity.
	 * @param force the force to be applied
	 */
	public void applyForce(Vector2D force) {
		acceleration.add(force);
		if (acceleration.getMagnitude() >= maxAcceleration) {
			acceleration.setMagnitude(maxAcceleration);
		}
	}
	
	/**
	 * get the maximum velocity of the entity
	 * @return the max velocity
	 */
	public float getMaxVelocity() {
		return maxVelocity;
	}

	/**
	 * clear the acceleration queue of the entity
	 */
	public void clearAccelerationQueue() {
		accelerationQueue.clear();
	}
	
	/**
	 * get the acceleration queue of the entity
	 * @return the acceleration queue
	 */
	public CopyOnWriteArrayList<Vector2D> getAccelerationQueue(){
		return accelerationQueue;
	}
	
	/**
	 * get the x location of the entity
	 * @return the x location
	 */
	public int getxLocation() {
		return xLocation;
	}
	
	/**
	 * set the x location of the entity
	 * @param xLocation the new x location
	 */
	public void setxLocation(int xLocation) {
		if (hitbox != null) 
			this.hitbox.translate(xLocation - this.xLocation, yLocation - this.yLocation);
		this.xLocation = xLocation;
	}
	
	/**
	 * get the y location of the entity
	 * @return the y location
	 */
	public int getyLocation() {
		return yLocation;
	}
	
	/**
	 * get the direction of the entity
	 * @return the direction
	 */
	public double getDirection() {
		return direction;
	}
	
	/**
	 * sets the location - for use by teleports
	 * @param x the new x value
	 * @param y the new y value
	 */
	public void setLocationTeleport(int x,int y) {
		this.setPosition(new Vector2D(x,y));
		this.setLocation(x,y);
	}
	
	/**
	 * set the y location of the entity
	 * @param yLocation the new y location
	 */
	public void setyLocation(int yLocation) {
		if (hitbox != null) 
			this.hitbox.translate(xLocation - this.xLocation, yLocation - this.yLocation);
		this.yLocation = yLocation;
	}
	
	/**
	 * set the location of the entity
	 * @param xLocation the new x location
	 * @param yLocation the new y location
	 */
	public void setLocation(int xLocation, int yLocation) {
		setxLocation(xLocation);
		setyLocation(yLocation);
		if (hitbox != null)
		this.hitbox.setLocation((int) (xLocation-size.getWidth()/2), (int) (yLocation-size.getHeight()/2));
		
	}
	
	/**
	 * does this entity experience air resistance?
	 * @return has air resistance?
	 */
	public boolean hasAirResistance()
	{
		return hasAirResistance;
	}
	
	/**
	 * set the magnitude of the velocity of the entity
	 * @param mag the new magnitude - direction stays the same
	 */
	public void setVelocity(int mag) {
		this.velocity.setMagnitude(mag);
	}
	
	/**
	 * set the velocity of the entity
	 * @param velocity the new velocity
	 */
	public void setVelocity(Vector2D velocity){
		
		if (velocity.getMagnitude() >= maxVelocity)
		{
			this.velocity = velocity;
			this.velocity.setMagnitude(maxVelocity);
		}
		else
		{
			this.velocity = velocity;
		}
	}
	
	/**
	 * get the velocity of the entity
	 * @return the velocity
	 */
	public Vector2D getVelocity() {
		return this.velocity;
	}
	
	/**
	 * set the acceleration of the entity
	 * @param acceleration the new acceleration
	 */
	public void setAcceleration(Vector2D acceleration){
		if (acceleration.getMagnitude() >= 1)
		{
			this.acceleration = acceleration;
			this.acceleration.setMagnitude(maxAcceleration);
		}
		else
		{
			float mult = acceleration.getMagnitude();
			float newMag = mult * maxAcceleration;
			this.acceleration = acceleration;
			this.acceleration.setMagnitude(newMag);
		}
	}
	
	/**
	 * get the max acceleration
	 * @return the max acceleration
	 */
	public float getMaxAcceleration() {
		return this.maxAcceleration;
	}
	
	/**
	 * get the acceleration of the entity
	 * @return the acceleration
	 */
	public Vector2D getAcceleration(){
		return this.acceleration;
	}
	
	/**
	 * get the id of the entity
	 * @return the id of the entity
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * set the id of the entity
	 * @param id the new ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * get the hitbox of the entity
	 * @return
	 */
	public Rectangle getHitBox() {
		return hitbox;
	}

	/**
	 * get the buffered image of the entity
	 * @return the buffered image
	 */
	public BufferedImage[][] getSprite() {
		return sprite;
	}

	/**
	 * set the buffered image of the entity
	 * @param sprite the buffered image
	 */
	public void setSprite(BufferedImage[][] sprite) {
		this.sprite = sprite;
		frame = 0;
		animation = 0;
		this.size = new Dimension(sprite[0][0].getWidth(), sprite[0][0].getHeight());
		this.hitbox = new Rectangle(new Point((int) (xLocation - (this.size.getWidth()/2)),(int) (yLocation - (this.size.getHeight()/2))), this.size);
		
		
	}
	
	/**
	 * update the entity
	 * @return 
	 */
	public boolean update() {
		if (System.currentTimeMillis() - prevTime >= animationTime) {
			incrementFrame();
			prevTime = System.currentTimeMillis();
		}
		return false;
	}
	
	/**
	 * progress to the next frame
	 */
	public void incrementFrame() {
		if (frame < sprite.length-1) {
			frame++;
		} else {
			frame= 0;
		}
	}
	

	public int getFrame() {
		return frame;
	}

	public int getAnimation() {
		return animation;
	}

	public Vector2D getPosition() {
		return position;
	}
	
	public void setPosition(Vector2D pos) {
		this.position = pos;
	}


}
