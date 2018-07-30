package gameAI.battle;

import entities.Vector2D;

/**
 * Contains the description of a projectile which exists on the map
 * Once a projectile object has been created it should be independent
 * of any other entity(except when a collision collision occurs).
 * Thus the BattleAI doesn't need to explicitly control the projectile
 */
public class ProjectileDescription extends ObjectDescription{

    private entities.projectiles.Projectile fProjectile;

    public ProjectileDescription(entities.projectiles.Projectile _fProjectile){
    	super("projectile");
        this.fProjectile = _fProjectile;
    }

    /**
     * @return the current x coordinate of the projectile
     */
    public int getXLocation(){
        return this.fProjectile.getxLocation();
    }

    /**
     * @return the current y coordinate of the projectile
     */
    public int getYLocation(){
        return this.fProjectile.getyLocation();
    }

    /**
     * @return the Vector2D of the projectile
     */
    public entities.Vector2D getProjectileVelocity() {
        return this.fProjectile.getVelocity();
    }

    /**
     * @return the attack power of the projectile
     */
    public Float getProjectileAttack(){
        return this.fProjectile.getProjectileDamage();
    }

    /**
     * @return I don't have a clue what this actually is
     */
    public Integer getProjectileEffect(){
        return this.fProjectile.getProjectileEffect();
    }
    
    /**
     * @return the velocity of the given projectile
     */
    
	@Override
	public Vector2D getVelocity() {
		return this.fProjectile.getVelocity();
	}
}
