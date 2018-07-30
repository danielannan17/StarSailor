package entities.skills;

/***
 * class to represent a basic attack
 */
import java.awt.image.BufferedImage;

import entities.AvailableSkill;
import entities.Ship;
import entities.Vector2D;
import entities.projectiles.BasicProjectile;
import handlers.ResourceHandler;

public class BasicAttack extends AttackSkill {
	final BufferedImage[][] projectileSprite,enemyProjectileSprite;
	final static BufferedImage	skillIcon = ResourceHandler.getBufferedImage("skill/basicIcon");;
	
	/**
	 * create a new basic attack
	 */
	public BasicAttack() {
		enemyProjectileSprite = new BufferedImage[1][1];
		projectileSprite = new BufferedImage[1][1];
		enemyProjectileSprite[0][0] = ResourceHandler.getBufferedImage("skill/enemy_basicAttack");
		projectileSprite[0][0] = ResourceHandler.getBufferedImage("skill/basicAttack");
	}
	
	@Override
	public boolean equals(Object obj) {
		return BasicAttack.class.isInstance(obj);
	}


	
	/**
	 * get the buffered image of the skills icon
	 * @return the buffered image
	 */
	public static BufferedImage getSkillIcon() {
		return skillIcon;
	}
	
	/**
	 * can the skill be used?
	 * return usable?
	 */
	public boolean canDoSkill() {
		return lastUse + cooldown < System.currentTimeMillis();
	}
	
	/**
	 * use the skill
	 * @param ship the ship the use the skill for
	 */
	public void doSkill(Ship ship) {
		if (canDoSkill()) {
		int target;
		if (ship.getTeam() == 1)
			target = 2;
		else 
			target = 1;
		BasicProjectile projectile = new BasicProjectile(AvailableSkill.BasicAttackSkill.id, ship.getxLocation(), ship.getyLocation(), ship.getDirection(),
				multiplier*ship.getAttack(), target, new Vector2D(ship.getDirection(), 10), projectileSprite);
		ship.addEntity(projectile);
		lastUse = System.currentTimeMillis();
		}
	}
	
	/**
	 * get the buffered image of the projectile
	 * @param isTargetingMyTeam is this projectile targeting the ship's team?
	 * @return the buffered image
	 */
	public BufferedImage[][] getProjectileIcon(boolean isTargetingMyTeam) {
		if (isTargetingMyTeam)
			return enemyProjectileSprite;
		else
			return projectileSprite;
	}
	

}
