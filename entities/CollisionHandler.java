package entities;
import entities.projectiles.BasicProjectile;
import entities.projectiles.DotProjectile;
import entities.projectiles.PenetratingProjectile;
import entities.projectiles.Projectile;
public class CollisionHandler {
	
	
	/**
	 * Receives 2 entities and determines what action to take based on the 
	 * type of the entities. If it is 2 ships, both ships take damage equal
	 * to the others health. If it is 1 ship and 1 projectile then the ship
	 * takes damage equal to the damage of the projectile.
	 * @param e1 The first entity for which it is checking the collision
	 * @param e2 The second entity for which it is checking the collsion
	 */
	public static void handleCollision(Entity e1, Entity e2) {
		if (Ship.class.isInstance(e1) && Ship.class.isInstance(e2)) {
			float velocity = (e1.getVelocity().getMagnitude() + e2.getVelocity().getMagnitude())/2;
			float maxVelocity = (e1.getMaxVelocity() + e2.getMaxVelocity())/2;
			float health = (((Ship) e1).getHealth() + ((Ship) e2).getHealth())/2;
			float healthLoss = (velocity/maxVelocity) * health;
			((Ship) e1).takeDamage(healthLoss);
			((Ship) e2).takeDamage(healthLoss);
		} else if (Ship.class.isInstance(e1) && Projectile.class.isInstance(e2)) {
			if (((Ship) e1).getTeam() == ((Projectile) e2).getTargetTeam()) {
				if (DotProjectile.class.isInstance(e2)) {
					((Ship) e1).takeDamage(((DotProjectile) e2).dealDamage(e1));
				} else if (PenetratingProjectile.class.isInstance(e2)) {
					((Ship) e1).takeDamage(((PenetratingProjectile) e2).dealDamage(e1));
				} else {
					((Ship) e1).takeDamage(((BasicProjectile) e2).dealDamage());
				}
					
			}
		} 
	}
	
}