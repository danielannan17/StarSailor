package galaxy.battle;

import handlers.MathHandler;
import handlers.ResourceHandler;
import main.State;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * contains code for a generic Entity
 * 
 * 
 * 
 *
 */
public abstract class Entity {

	protected Image[] sprites;
	protected Image[] paralysis;

	protected int width, height, frame = 1, pFrame = 0;
	protected long previousTime, animationTime, prevShoot, shotInterval;

	private boolean moving = false;

	protected Point2D.Double previousCentre;
	protected Point2D.Double centre;
	protected Point2D.Double target;

	protected Entity targetEntity;

	protected float totalHealth, currentHealth, damage;
	protected float speed;
	protected Entity.TYPE type;
	protected int shipid;

	protected int longRangeLaserCount;
	protected long previousLongRangeLaserTime;
	protected long longRangeLaserInterval;

	protected static int behaviourCount = 6;
	protected int collisionDamage;
	
	protected int magneticShotInterval;
	protected int magneticShotCount;
	protected long previousMagneticShotTime;
	
	protected boolean paralysed = false;
	protected long paralysationTime;
	protected long paralysationLength;
	
	protected int repairs;
	
	protected long alternativeAISwitchInterval = 8000;
	protected long lastSwitchTime;
	protected TYPE currentTYPE;
	
	protected String name;
	
	protected int bugFixingChances = 5;
	
	/**
	 * enum for the type of AI this entity has
	 * 
 * 
	 *
	 */
	public static enum TYPE {
		AGGRESSIVE, DEFENSIVE, TACTICAL, BUGFIXER, SURVIVOR, SURPRISESURVIVOR, ALTERNATIVE, KINGCRUSHER
	}

	/**
	 * creates a new Entity
	 * 
	 * @param path
	 *            the path to the sprites
	 * @param x
	 *            its initial x coord
	 * @param y
	 *            its initial y coord
	 * @param width
	 *            its width
	 * @param height
	 *            its height
	 * @param animationTime
	 *            its animation time
	 * @param shipid
	 *            its id
	 * @param heatProjectilesCount
	 * @param heatShotInterval
	 */
	
	public Entity(String path, float x, float y, int width, int height, long animationTime, int shipid,
			int heatProjectilesCount, int heatShotInterval, int collisionDamage, int magneticLaserCount, int magneticShotInterval, int paralysationLength) {
		this.sprites = ResourceHandler.getBlockSprites(path, width, height);
		this.paralysis = ResourceHandler.getBlockSprites("ship_types/paralyse", 32, 32);
		centre = new Point2D.Double(x, y);
		previousCentre = centre;
		this.width = width;
		this.height = height;
		this.animationTime = animationTime;
		this.shipid = shipid;
		
		this.longRangeLaserCount = heatProjectilesCount;
		this.previousLongRangeLaserTime = System.currentTimeMillis();
		this.longRangeLaserInterval = longRangeLaserInterval;
		this.collisionDamage = collisionDamage;
		
		this.magneticShotInterval = magneticShotInterval;
		this.paralysationLength = paralysationLength;
		this.magneticShotCount = magneticLaserCount;
		this.previousMagneticShotTime = System.currentTimeMillis();
		this.lastSwitchTime = System.currentTimeMillis();
		
		this.currentTYPE = TYPE.AGGRESSIVE;
	}

	/**
	 * updates this entity
	 * 
	 * @param time
	 *            the time between frames
	 * @param width
	 *            the width of the battle
	 * @param height
	 *            the height of the battle
	 * @return whether to fire a laser
	 */
	
	public boolean update(float time, int width, int height, List<Entity> team, Battle battle) {
		if (System.currentTimeMillis() - previousTime >= animationTime) {
			incrementFrame();
			incrementPFrame();
			previousTime = System.currentTimeMillis();
			previousCentre = centre;
		}
		return false;
	}

	/**
	 * draws this entity
	 * 
	 * @param g
	 *            the graphics contrext to use
	 * @param scale
	 *            the scale to use
	 */
	public void draw(Graphics g, float scale) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform t = g2d.getTransform();
		AffineTransform at = new AffineTransform();
		//System.out.println("name" + this.name + "type" + this.type + " " + centre + " "+ target);
		
		at.rotate(MathHandler.getAngle(centre, target), centre.x, centre.y);
		if (currentHealth > 0 && State.state == State.STATE.GAME_BATTLE) {
			g2d.setColor(Color.blue);
			g2d.fillRect((int) (centre.x - width / 2.0), (int) centre.y - (int) (height), (int) width,
					(int) height / 8);
			g2d.setColor(Color.green);
			float progress = currentHealth * (width / totalHealth);
			g2d.fillRect((int) (centre.x - width / 2.0f), (int) centre.y - (int) (height), (int) progress,
					(int) height / 8);
			
		}
		g2d.setTransform(at);
		if (currentHealth > 0) {
			g2d.drawImage(sprites[frame], (int) (centre.x - width / 2.0f), (int) (centre.y - height / 2.0f), null);
			if(paralysed){
				g2d.drawImage(paralysis[pFrame], (int) (centre.x - width / 2.0f), (int) (centre.y - height / 2.0f), width, height, null);
			}
		}
		g2d.setTransform(t);
	}

	/**
	 * increments the frame used to draw this entity
	 */
	public void incrementFrame() {
		if (moving) {
			if (frame < 2) {
				frame++;
			} else {
				frame = 1;
			}
		} else {
			if (frame < 4) {
				frame++;
			} else {
				frame = 3;
			}
		}
	}
	
	public void incrementPFrame() {
		if(pFrame < 3){
			pFrame ++;
		}else{
			pFrame = 0;
		}
	}

	/**
	 * moves this entity
	 * 
	 * @param x
	 *            the x amount to move it by
	 * @param y
	 *            the y amount to move it by
	 */
	public void moveEntity(float x, float y) {
		centre.x += x;
		System.out.println(centre.x);
		centre.y += y;
	}

	/**
	 * gets a random AI type
	 * 
	 * @param random
	 *            the instance of Random to use
	 * @param range
	 *            the range
	 * @return an AI type
	 */
	public static TYPE getRandomType(Random random, int range) {
		int n = random.nextInt(range);
		
		
		switch (n) {
		case 0:
			return Entity.TYPE.AGGRESSIVE;
		case 1:
			return Entity.TYPE.DEFENSIVE;
		case 2:
			return Entity.TYPE.TACTICAL;
		case 3:
			return Entity.TYPE.ALTERNATIVE;
		case 4:
			return Entity.TYPE.KINGCRUSHER;
		case 5:
			return Entity.TYPE.BUGFIXER;
		case 6:
			return Entity.TYPE.AGGRESSIVE;
		}
		return Entity.TYPE.SURVIVOR;
	}

	/**
	 * If the entity has been hit then this method is called
	 * 
	 * @param amount
	 *            the amount of damage taken by the entity
	 */
	public void damage(int amount) {
		currentHealth -= amount;
	}

	/**
	 * Finds the closest enemy entity(from this entity) and sets it as its
	 * target This is called only by the team that opposes the player so it
	 * tries to check him as a target as well
	 * 
	 * @param pPosition
	 *            the position of the player
	 * @param enemyTeam
	 *            the enemy fleet
	 * @param width
	 *            the width of the screen(which acts like the initial closest
	 *            distance)
	 */
	public void findTarget(Point2D.Double pPosition, Player player, ArrayList<Entity> enemyTeam, int width) {
		double distance = width;
		for (Entity e : enemyTeam) {
			if (this.getCentre().distance(e.getCentre()) < distance) {
				target = e.getCentre();
				targetEntity = e;
				distance = this.getCentre().distance(e.getCentre());
			}
		}
		if (pPosition != null && this.getCentre().distance(pPosition) < distance) {
			target = pPosition;
			targetEntity = player;
		}
	}

	/**
	 * Finds the closest enemy entity(from this entity) and sets it as its
	 * target Called by an entity belonging to the player's team
	 * 
	 * @param enemyTeam
	 *            the enemy fleet
	 * @param width
	 *            the width of the screen(which acts like the initial closest
	 *            distance)
	 */
	public void findTarget(ArrayList<Entity> enemyTeam, int width) {
		
		double distance = width;
		for (Entity e : enemyTeam) {
			if (this.getCentre().distance(e.getCentre()) < distance) {
				target = e.getCentre();
				targetEntity = e;
				distance = this.getCentre().distance(e.getCentre());
			}
		}
	}

	/**
	 * Aim straight for the player
	 * 
	 * @param pPosition
	 */
	public void targetPlayer(Point2D.Double pPosition) {
		target = pPosition;
	}

	/**
	 * Finds the closest laser to the leader
	 * 
	 * @param leaderPosition
	 *            = the position of the leader
	 * @param enemyLasers
	 *            = the list of the enemy lasers
	 * @param width
	 *            = the width of the map where the battle is held
	 */
	public void findClosestLaser(Point2D.Double leaderPosition, ArrayList<Line2D.Double> enemyLasers, int width) {
		int distance = width;
		for (Line2D.Double l : enemyLasers) {
			if (this.getCentre().distance(l.getP1()) + leaderPosition.distance(l.getP1()) < distance)
				target = (Point2D.Double) l.getP1();
		}
	}

	/**
	 * gets the x coordinate
	 * 
	 * @return the x coord
	 */
	public float getX() {
		return (float) centre.x;
	}

	/**
	 * sets the x coordinate
	 * 
	 * @param x
	 *            the new x coord
	 */
	
	public void setX(float x) {
		centre.x = x;
	}

	/**
	 * gets the y coordinate
	 * 
	 * @return the y coord
	 */
	public float getY() {
		return (float) centre.y;
	}

	/**
	 * sets the y coordinate
	 * 
	 * @param y
	 *            the new y coord
	 */
	public void setY(float y) {
		centre.y = y;
	}

	/**
	 * gets the width
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * sets the width
	 * 
	 * @param width
	 *            the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * gets the height
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * sets the height
	 * 
	 * @param height
	 *            the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * gets the centre of this entity
	 * @return the centre point of this entity
	 */
	public Point2D.Double getCentre() {
		return centre;
	}

	public void setPreviousCentre(Point2D.Double p){
		this.previousCentre = p;
	}

	/**
	 * sets the centre of this entity
	 * @param centre the new centre
	 */
	public void setCentre(Point2D.Double centre) {
		this.centre = centre;
	}

	/**
	 * checks if this entity is moving
	 * @return whether this entity is moving
	 */
	public boolean isMoving() {
		return moving;
	}

	/**
	 * sets whether this entity is moving
	 * @param moving new value for whether its moving
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	/**
	 * gets the target
	 * @return the target
	 */
	public Point2D.Double getTarget() {
		return target;
	}

	/**
	 * sets the target
	 * @param target the new target
	 */
	public void setTarget(Point2D.Double target) {
		this.target = target;
	}

	/**
	 * gets the target entity
	 * @return the target entity
	 */
	public Entity getTargetEntity() {
		return targetEntity;
	}

	/**
	 * sets the target entity
	 * @param targetEntity the target entity
	 */
	public void setTargetEntity(Entity targetEntity) {
		this.targetEntity = targetEntity;
	}

	/**
	 * gets the current frame
	 * @return the current frame
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * sets the current frame
	 * @param frame the new frame
	 */
	public void setFrame(int frame) {
		this.frame = frame;
	}

	/**
	 * gets the total health
	 * @return the total health
	 */
	public float getTotalHealth() {
		return totalHealth;
	}

	/**
	 * sets the total health
	 * @param totalHealth the new total health
	 */
	public void setTotalHealth(float totalHealth) {
		this.totalHealth = totalHealth;
	}

	/**
	 * gets the current health
	 * @return the current health
	 */
	public float getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * sets the current health
	 * @param currentHealth the current health
	 */
	public void setCurrentHealth(float currentHealth) {
		this.currentHealth = currentHealth;
	}

	/**
	 * gets the damage
	 * @return the damage
	 */
	public float getDamage() {
		return damage;
	}

	/**
	 * sets the damage
	 * @param damage the new damage
	 */
	public void setDamage(float damage) {
		this.damage = damage;
	}

	/**
	 * gets the speed of this entity
	 * @return the speed of this entity
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * sets the speed of this entity
	 * @param speed the new speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * gets the type of AI being used
	 * @return the type of AI being used
	 */
	public Entity.TYPE getType() {
		return type;
	}

	/**
	 * sets this entities AI
	 * @param type the new AI to use
	 */
	public void setType(Entity.TYPE type) {
		this.type = type;
	}

	/**
	 * gets the time when the previous shot fired
	 * @return when the previous shot fired
	 */
	public long getPrevShoot() {
		return prevShoot;
	}

	/**
	 * sets the time when the previous shot fired
	 * @param prevShoot 
	 */
	public void setPrevShoot(long prevShoot) {
		this.prevShoot = prevShoot;
	}

	/**
	 * gets the time an entity has to wait to shoot
	 * @return the shot interval
	 */
	public long getShotInterval() {
		return shotInterval;
	}

	/**
	 * sets the shot interval
	 * @param shotInterval the new shot interval
	 */
	public void setShotInterval(long shotInterval) {
		this.shotInterval = shotInterval;
	}

	/**
	 * gets the ship id
	 * @return the ship id
	 */
	public int getShipid() {
		return shipid;
	}
	
	/**
	 * @return the previous centre of the entity before the last animation
	 */
	
	public Point2D.Double getPreviousCentre(){
		return previousCentre;
	}
	
	/**
	 * Sets the paralysed field to the given value
	 * @param value
	 */
	
	public void setParalysed(boolean value){
		paralysed = value;
		paralysationTime = System.currentTimeMillis();
	}
	
	/**
	 * Checks whether the entity can move or not
	 * @return
	 */

	public boolean isParalysed(){
		
		if(System.currentTimeMillis() - this.getParalysationTime() > this.getParalysationLength())
			paralysed = false;
		return paralysed;
	}
	
	public void setParalysationLength(long value){
		paralysationLength = value;
	}
	
	/**
	 * @return the time at which the entity was paralysed
	 */
	
	public long getParalysationTime(){
		return paralysationTime;
	}
	
	/**
	 * @return the length of the time this entity stays paralysed
	 */
	
	public long getParalysationLength(){
		return paralysationLength;
	}
	
	/**
	 * sets the ship id
	 * @param shipid the new ship id
	 */
	public void setShipid(int shipid) {
		this.shipid = shipid;
	}
	
	/**
	 * the aggressive ai
	 */
	protected abstract void executeAGGRESSIVEBehaviour();

	/**
	 * the defensive ai
	 */
	protected abstract void executeDEFENSIVEBehaviour();

	/**
	 * the tactical ai
	 */
	protected abstract void executeTACTICALBehaviour();

	/**
	 * the alternative ai
	 */
	protected abstract void executeALTERNATIVEBehaviour();

	/**
	 * the king crusher ai
	 */
	protected abstract void executeKINGCRUSHERBehaviour();
	
	/**
	 * the bug fixer behaviour
	 */
	protected abstract void executeBUGFIXERBehaviour();
	
	/**
	 * the survivor behaviour
	 */
	protected abstract void executeSURVIVORBehaviour(List<Entity> team, Battle battle, String shipImage);
	
	/**
	 * Use a basic idea to get a possible location to which a target is heading
	 * towards
	 * 
	 * @param previousLocation
	 *            = the previous location of the entity
	 * @param currentLocation
	 *            = the current location
	 * @return
	 * 
	 * Ideas : 
	 * 	use the animationTime to ge the vector and try to predict de location
	 * 	Separate the location using axis in order to get an approximate direction 
	 *  use multiple previous locations in order to predict the trajectory 
	 * 
	 */
	public Point2D.Double getNextPossibleLocation(Point2D.Double previousLocation, Point2D.Double currentLocation) {
		return new Point2D.Double(2 * currentLocation.getX() - previousLocation.getX(),
				2 * currentLocation.getY() - previousLocation.getY());
	}
	
	/**
	 * @return the available number of long range lasers
	 */
	
	public int getLongRangeLaserCount() {
		return longRangeLaserCount;
	}
	
	/**
	 * Shoots a long range laser and decreases the available shots
	 */
	
	public void shootLongRangeLaser() {
		longRangeLaserCount--;
		this.previousLongRangeLaserTime = System.currentTimeMillis();
	}
	
	/**
	 * @return the last time a shot was fired
	 */
	
	public long getPreviousLongRangeLaserTime() {
		return previousLongRangeLaserTime;
	}
	
	/**
	 * Check if the entity can launch a magnetic laser
	 * @return
	 */
	
	public boolean canShootLongRangeLaser() {
		return longRangeLaserCount > 0 && System.currentTimeMillis() - previousLongRangeLaserTime > longRangeLaserInterval;
	}
	
	/**
	 * Check how many magnetic shots are available
	 * @return
	 */
	public int getMagneticShotCount() {
		return longRangeLaserCount;
	}
	
	/**
	 *  Shoot a magnetic laser
	 */
	public void shootMagneticLaser() {
		magneticShotCount--;
		this.previousMagneticShotTime = System.currentTimeMillis();
	}
	
	/**
	 * @return the last time a magnetic shot has been fired
	 */

	public long getPreviousMagneticShotTime() {
		return previousMagneticShotTime;
	}
	
	
	/**
	 * Check if the entity can launch a magnetic laser
	 */
	
	public boolean canShootMagneticLaser(){
		return magneticShotCount > 0 && System.currentTimeMillis() - previousMagneticShotTime > magneticShotInterval;
	}

	/**
	 * @return the type of this entity
	 */
	public abstract String getShipType();

	/**
	 * @return returns the previous time at which a shot was fired
	 */
	public long getPreviousTime(){
		return this.previousTime;
	}


	/**
	 * set the previous update time
	 */
	public void setPreviousTime(long newValue){
		this.previousTime = newValue;
	}
}
