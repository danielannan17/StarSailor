package entities.skills;

import java.awt.Image;
import java.awt.image.BufferedImage;

import entities.AvailableSkill;
import entities.Ship;
import entities.Vector2D;
import entities.projectiles.PenetratingProjectile;
import handlers.ResourceHandler;

/***
 * class to represent the penetrating skill
 *
 */
public class Penetrate extends AttackSkill {
final BufferedImage[][] projectileSprite, enemyProjectileSprite;
long cooldown = 4000, lastUse = 0;
public static final int penetration = 5;
final static BufferedImage skillIcon = ResourceHandler.getBufferedImage("skill/penetrateIcon");




	/**
	 * create a new penetrating skill
	 */
	public Penetrate() {
		projectileSprite = new BufferedImage[1][1]; 
		enemyProjectileSprite = new BufferedImage[1][1];
		projectileSprite[0][0] = ResourceHandler.getBufferedImage("skill/penetrate");
		enemyProjectileSprite[0][0] = ResourceHandler.getBufferedImage("skill/enemy_penetrate");
	
	}
	
	@Override
	public boolean equals(Object obj) {
		return Penetrate.class.isInstance(obj);
	}
	
	/**
	 * get the buffered image associated with this skill's icon
	 * @return the buffered image
	 */
	public static BufferedImage getSkillIcon() {
		return skillIcon;
	}
	
	/**
	 * can this skill be used right now?
	 */
	public boolean canDoSkill() {
		return lastUse + cooldown < System.currentTimeMillis();
	}
	
	/**
	 * use the skill
	 * @param ship the ship to use the skill for
	 */
	public void doSkill(Ship ship) {
		if (canDoSkill()) {
			int target;
			if (ship.getTeam() == 1)
				target = 2;
			else 
				target = 1;
			PenetratingProjectile projectile = new PenetratingProjectile(AvailableSkill.PenetrateSkill.id,ship.getxLocation(), ship.getyLocation(), -ship.getDirection(),
				multiplier*ship.getAttack(),penetration, target, new Vector2D(ship.getDirection(), 10), projectileSprite);
			ship.addEntity(projectile);
			lastUse = System.currentTimeMillis();
		}
	}
	
	/**
	 * get the in game buffered image of this projectile
	 * @param isTargetingMyTeam is the projectile targeting my team?
	 * @return the buffered image
	 */
	public BufferedImage[][] getProjectileIcon(boolean isTargetingMyTeam) {
		if (isTargetingMyTeam)
			return enemyProjectileSprite;
		else
			return projectileSprite;
	}
}
