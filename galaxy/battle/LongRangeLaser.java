package galaxy.battle;

import java.awt.geom.Line2D;

/***
 * class to represent a heat projectile
 * 
 */

public class LongRangeLaser {
	
	private Line2D.Double projectile;
	private Entity target;
	private long lifeTime;
	private long shotTime;
	
	/**
	 * create a new heat projectile
	 * @param projectile = the projectiles path
	 * @param target the = targetted entity
	 */
	public LongRangeLaser(Line2D.Double projectile, Entity target){
		this.projectile = projectile;
		this.target 	= target;
		this.lifeTime	= 1500;
		this.shotTime 	= System.currentTimeMillis();
	}
	
	/**
	 * get the projectile's line
	 * @return the projectile line/
	 */
	public Line2D.Double getProjectile(){
		return projectile;
	}
	
	/**
	 * get the target team of the projectile
	 * @return the target team
	 */
	public Entity getTarget(){
		return target;
	}
	
	/**
	 * set the projectile path/line
	 * @param _projectile the new line
	 */
	public void setProjectile(Line2D.Double _projectile){
		this.projectile = _projectile;
	}
	
	/**
	 * set the target of this projectile
	 * @param _target the target
	 */
	public void setTarget(Entity _target){
		this.target = _target;
	}
	
	
	/**
	 * set the time at which this was shot
	 * @param _time = the new value 
	 */
	public void setShotTime(long _time){
		this.shotTime = _time;
	}
	
	/**
	 * get the time at which this was shot
	 * @return the time it was shot
	 */
	public long getShotTime(){
		return this.shotTime;
	}
	
	/**
	 * get the life span of this projectile
	 * @return the life span
	 */
	public long getLifeTime(){
		return this.lifeTime;
	}
	
	
	
	
}
