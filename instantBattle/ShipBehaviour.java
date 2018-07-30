package instantBattle;
/***
 * A class to manage the behaviour of the ships in instant battle.
 * 
 */

import java.util.concurrent.CopyOnWriteArrayList;

import entities.Entity;
import entities.Ship;
import entities.Vector2D;
import entities.projectiles.BasicProjectile;
import entities.projectiles.Projectile;

public class ShipBehaviour {
	private CopyOnWriteArrayList<Entity> entities;
	
	//READ ME :: //////////////////////////////////////
	/*
	 * 
	 * changing these values will make the junit test 'testStrengthDistGets()'
	 * fail unless updated in ShipBehaviourTest
	 * 
	 */
	private int seperationDistance = 10;
	private float seperationStrength = 0.3f;
	
	private int alignDistance = 20;
	private float alignStrength = 0.5f;
	
	private int projectileSeperationDistance = 10;
	private float projectileSeperationStrength = 0.6f;
	
	/**
	 * create an instance of ship behaviour. Should be one per instant battle.
	 * @param entities 
	 * 				the list of entities to manage the behaviour of
	 */
	public ShipBehaviour(CopyOnWriteArrayList<Entity> entities) {
		this.entities = new CopyOnWriteArrayList<Entity>(entities);
	}
	
	/**
	 * update the ships
	 */
	public void update() {
		float distToOther = 0;
		Vector2D averageNeighbourVelocity = new Vector2D();
		
		for (Entity e:entities) {
			boolean e1isShip = Ship.class.isInstance(e);
			
			if (!e1isShip) continue; //don't do anything to projectiles
			
			Ship s1 = (Ship) e;
			if (s1.isPlayer) continue;
			
			for (Entity e2:entities) {
				boolean e2isShip = Ship.class.isInstance(e2);
				
				if (e2 != e) {
					
					if (e2isShip) {
						//both are ships 
						Ship s2 = (Ship) e2;
						
						distToOther = distBetween(e,e2);
						
						if (distToOther <= seperationDistance) {
							//Separation
							applySeperation(e,e2,distToOther,seperationStrength,seperationDistance);
						}
					
						if (distToOther <= alignDistance && !s1.isInFlock && (s2.isInFlock || s2.isLeader)) {
							//alignment
							//need to take "stickyness" into account, from ship
							System.out.println("applying alignment");
							averageNeighbourVelocity.add(e2.getVelocity());
						}
					} else {
						//'other' is a projectile - avoid it?
						distToOther = distBetween(e,e2);
						Projectile p = (Projectile) e2;
						if (distToOther <= projectileSeperationDistance && p.getTargetTeam() == s1.getTeam()) {
							
							applySeperation(e,e2,distToOther,projectileSeperationStrength,projectileSeperationDistance);
						}
					}
				}
			}
			
			averageNeighbourVelocity.setMagnitude(alignStrength);
			e.applyForce(averageNeighbourVelocity);
			
			averageNeighbourVelocity = new Vector2D();
		}
	}
	
	/**
	 * apply a seperation between 2 entities
	 * @param e the entity to apply to force TO
	 * @param e2 the entity to apply the force away from
	 * @param distToOther the distance between the entities
	 */
	public void applySeperation(Entity e,Entity e2,float distToOther,float strength,int sepDist) {
		float mult = 1.0f - (distToOther/(float)sepDist);
		
		Vector2D awayFromOther = (e.getPosition().copy()).sub(e2.getPosition());
		awayFromOther.setMagnitude(mult * strength);
		e.applyForce(awayFromOther);
	}
	
	/**
	 * add an entity to the simulation
	 * @param e the entity to be added
	 */
	public boolean addEntity(Entity e) {
		if (entities.contains(e)) {
			return false;
		} else {
			this.entities.add(e);
			return true;
		}
	}
	
	/**
	 * add multiple entities to the simulation
	 * @param es the entites to be added
	 * @return 
	 */
	public boolean addEntities(CopyOnWriteArrayList<Entity> es) {
		boolean success = true;
		for (Entity e:es) {
			if (!addEntity(e)) {
				success = false;
			}
		}
		return success;
	}
	
	/**
	 * remove an entity from the simulation
	 * @param e the entity to be removed
	 */
	public void removeEntity(Entity e) {
		this.entities.remove(e);
	}
	
	/**
	 * returns the distance between two entities
	 * @param e1 entity 1
	 * @param e2 entity 2
	 * @return the distance between the entities
	 */
	static float distBetween(Entity e1,Entity e2) {
		int dx = e1.getxLocation() - e2.getxLocation();
		int dy = e1.getyLocation() - e2.getyLocation();
		float toReturn = (float)Math.sqrt(dx*dx + dy*dy);
		return toReturn;
	}

	/**
	 * does the ship behaviour simulation contain a certain entity?
	 * @param e the entity to check for 
	 * @return in simulation?
	 */
	public boolean containsEntity(Entity e) {
		return entities.contains(e);
	}
	
	/**
	 * get the seperation strength between ships
	 * @return the seperation strength between ships
	 */
	public float getSepStrength() {
		return seperationStrength;
	}
	
	/**
	 * get the align strength of ships - how much they want to align with their neighbours
	 * @return the alignment strength between ships
	 */
	public float getAlignStrength() {
		return alignStrength;
	}
	
	/**
	 * get the strength at which ships will try to avoid projectiles
	 * @return the projectile separation strength
	 */
	public float getProjSepStrength() {
		return projectileSeperationStrength;
	}
	
	/**
	 * get the distance that ships would like to stay away from each other
	 * @return the ship separation distance
	 */
	public int getSepDist() {
		return seperationDistance;
	}
	
	/**
	 * get the distance within which ships will attempt to align
	 * @return the align distance
	 */
	public int getAlignDist() {
		return alignDistance;
	}
	
	/**
	 * get the distance ships will try to stay from projectiles that will hurt them
	 * @return the projectile separation strength
	 */
	public int getProjSepDist() {
		return projectileSeperationDistance;
	}
	
}
