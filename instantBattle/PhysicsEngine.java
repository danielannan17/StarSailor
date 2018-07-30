package instantBattle;
/***
 * thread to simulate the positions, velocities and accelerations of entities
 * 
 */

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import entities.Entity;
import entities.Ship;
import entities.Vector2D;
import entities.projectiles.Projectile;

public class PhysicsEngine implements Runnable {
	
	private CopyOnWriteArrayList<Entity> entities;
	private boolean gameOver;
	private int millisPerTick;
	private int width;
	private int height;
	private float airResistance = 0.1f;
	private ShipBehaviour shipBehaviour;
	
	/**
	 * create a new physics engine simulation
	 * @param ticksPerSecond the number of updates per second
	 * @param entities the entities to add to the simulation
	 * @param width the width of the world
	 * @param height the height of the world
	 */
	public PhysicsEngine(int ticksPerSecond,Collection<Entity> entities,int width,int height){
		this.entities = new CopyOnWriteArrayList<Entity>(entities);
		gameOver = false;
		millisPerTick = 1000/ticksPerSecond;
		this.height = height;
		this.width = width;
		shipBehaviour = new ShipBehaviour(this.entities);
	}
	
	/**
	 * add an entity to the simulation
	 * @param entity the entity to be added
	 * @return was the entity added?
	 */
	public boolean addEntity(Entity entity){
		if (!entities.contains(entity)){
			entities.add(entity);
			shipBehaviour.addEntity(entity);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * add multiple entities to the simulation
	 * @param allEntities the entities to be added
	 * @return 
	 */
	public boolean addEntities(LinkedList<Entity> allEntities) {
		boolean success = true;
		for (Entity e : allEntities) {
			if (!addEntity(e)) success = false;
		}
		return success;
	}
	
	/**
	 * remove an entity from the simulation
	 * @param entity the entity to be removed
	 */
	public void removeEntity(Entity entity){
		entities.remove(entity);
		shipBehaviour.removeEntity(entity);
	}
	
	/**
	 * does the physics engine contain a certain entity? - for testing
	 * @param e the entity to check
	 * @return entity in physics engine?
	 */
	public boolean containsEntity(Entity e) {
		return entities.contains(e);
	}
	
	/**
	 * move an entity based on it's current velocity
	 * @param e the entity to be moved
	 */
	private synchronized void move(Entity e) {
		e.setPosition(e.getPosition().copy().add(e.getVelocity()));

		int x = Math.round(e.getPosition().getX()); 
		int y = Math.round(e.getPosition().getY());
		
		e.setLocation(x, y);
	}
	
	/**
	 * gets the number of entities currently in the simulation - for testing
	 * @return the number of entities
	 */
	public int getNumEntities() {
		return entities.size();
	}

	/**
	 * run the simulation
	 */
	public void run() {
		while(!gameOver){
			shipBehaviour.update();
			updateEntities();
			sleep();
		}
	}
	
	/**
	 * update the entities - one tick
	 */
	public void updateEntities() {
		for (Entity e : entities) {
			
			Vector2D finalAcc = new Vector2D();
			Vector2D newAcc = getAverageAcceleration(e.getAccelerationQueue());
			e.clearAccelerationQueue();
			//acceleration from acc queue
			finalAcc.add(newAcc);
			
			//air resistance acceleration
			finalAcc.add(getAirResistance(e));
			
			//add direct accelerations from entity.applyForce()
			e.setAcceleration(finalAcc.add(e.getAcceleration()));
			
			Vector2D newVel = e.getVelocity().copy();
			newVel.add(e.getAcceleration());
			e.setVelocity(newVel);
			move(e);
			
			if (inMap(e) != 0) handleOutOfMapEntity(e,inMap(e));
			
			//clear the acceleration
			e.setAcceleration(new Vector2D());
		}
	}
	
	/**
	 * get the value of the air resistance that an entity should be receiving
	 * @param e the entity to calculate the air resistance of
	 * @return the air resistance for the entity
	 */
	public Vector2D getAirResistance(Entity e) {
		Vector2D airRes = new Vector2D();
		
		if (e.hasAirResistance()) {
			airRes = new Vector2D(-e.getVelocity().getX(),-e.getVelocity().getY());
			airRes.mult(airResistance);
		}
		
		return airRes;
	}
	
	/**
	 * decide what to do when an entity is out of the map
	 * @param e the entity that is our of the map
	 * @param side the side is passed over
	 */
	public void handleOutOfMapEntity(Entity e,int side) {
		//if it's a projectile, delete it
		if (Projectile.class.isInstance(e)) {
			((Projectile)e).remove();
			entities.remove(e);
			return;
		}
		
		int halfShipSize = (int)(e.getSize().width * 0.5);
		
		//clear the velocity - no bounce
		e.setVelocity(new Vector2D());
		
		switch(inMap(e)) {
		case 1:
			//right
			e.setxLocation(width-halfShipSize);
			e.setPosition(new Vector2D(width - halfShipSize,e.getyLocation()));
			break;
		case 2:
			//down
			e.setyLocation(height-halfShipSize);
			e.setPosition(new Vector2D(e.getxLocation(),height - halfShipSize));
			break;
		case 3:
			//left
			e.setxLocation(halfShipSize);
			e.setPosition(new Vector2D(halfShipSize,e.getyLocation()));
			break;
		case 4:
			//up
			e.setyLocation(halfShipSize);
			e.setPosition(new Vector2D(e.getxLocation(),halfShipSize));
			break;
		default:
			//another class must have moved entity back into map very fast
			// -- our work here is done.
			break;
		}
		return;
	}
	
	/**
	 * check if an entity is still in the map
	 * @param e - the entity to check
	 * @return - 0 -> in map, 1 - 4 is the edge that has been crossed. 1 = right, clockwise increasing
	 */
	public int inMap(Entity e) {
		int halfEntitySize = (int)(e.getSize().width * 0.5);
		
		int x = e.getxLocation(), y = e.getyLocation();
		if (Projectile.class.isInstance(e)) {
			halfEntitySize = (int)(e.getSize().width/2);
			if (x - halfEntitySize > width) return 1;
			if (x + halfEntitySize < 0) return 3;
			if (y - halfEntitySize > height) return 2;
			if (y + halfEntitySize < 0) return 4;
		}
		if (x + halfEntitySize > width) return 1;
		if (x - halfEntitySize < 0) return 3;
		if (y + halfEntitySize > height) return 2;
		if (y - halfEntitySize < 0) return 4;
		return 0;
	}

	/**
	 * stop the physics simulation
	 */
	public void stop() {
		gameOver = true;
	}
	
	/**
	 * Get the average acceleration of an acceleration queue
	 * @param accQueue the acceleration queue to find the mean of
	 * @return the mean acceleration
	 */
	private Vector2D getAverageAcceleration(CopyOnWriteArrayList<Vector2D> accQueue)
	{
		Vector2D average = new Vector2D();
		int n = accQueue.size();
		for (Vector2D acc:accQueue) {
			average.add(acc);
		}
		if (n > 0){
			Vector2D result = average.div(n);
			return result;
		}
		else {
			return average;
		}
	}
	
	/**
	 * apply an instant, 1 tick force to an entity
	 * @param e the entity to apply the force to
	 * @param force the force to be applied
	 */
	public void applyForce(Entity e,Vector2D force) {
		float xAcc = e.getAcceleration().getX();
		float yAcc = e.getAcceleration().getY();
		
		e.setAcceleration(new Vector2D(xAcc+force.getX(),yAcc+force.getY()));
	}
	
	private void sleep() {
		try {
			Thread.sleep(millisPerTick);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
