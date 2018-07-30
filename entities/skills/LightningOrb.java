package entities.skills;

import java.awt.Image;
import java.awt.image.BufferedImage;

import entities.AvailableSkill;
import entities.Ship;
import entities.Vector2D;
import entities.projectiles.DotProjectile;
import handlers.ResourceHandler;

/***
 * class to represent lightening orb skills
 *
 */
public class LightningOrb extends AttackSkill {
	final int ticksPerSecond;
	final int speed;
	final BufferedImage[][] projectileSprite;
	final BufferedImage[][] enemyProjectileSprite;
	long cooldown = 4000, lastUse = 0;
	final static BufferedImage skillIcon = ResourceHandler.getBufferedImage("skill/lightningOrbIcon");
	private BufferedImage[][] s,x;
	
	/**
	 * create a lightening orb
	 */
	public LightningOrb() {
		speed = 2;
		this.multiplier = 0.5f;
		this.ticksPerSecond = 5;
		s = ResourceHandler.getPlayerSprites("skill/lightning-orb", 130, 128);
		x = ResourceHandler.getPlayerSprites("skill/enemy_lightning-orb", 130, 128);	
		projectileSprite = new BufferedImage[s.length][s[0].length];
		enemyProjectileSprite = new BufferedImage[s.length][s[0].length];
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[i].length;j++) {
			enemyProjectileSprite[i][j] = x[i][j];
			projectileSprite[i][j] = s[i][j];
			}
		}
		
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		return LightningOrb.class.isInstance(obj);
	}



	/**
	 * get the buffered image associated with this skill
	 * @return the buffered image
	 */
	public static BufferedImage getSkillIcon() {
		return skillIcon;
	}
	
	/**
	 * can the skill be used right now?
	 * @return usable?
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
		DotProjectile orb = new DotProjectile(AvailableSkill.LightningOrbSkill.id,ship.getxLocation(), ship.getyLocation(), ship.getDirection(),
				multiplier*ship.getAttack(),ticksPerSecond, target, new Vector2D(ship.getDirection(),speed), projectileSprite);
		ship.addEntity(orb);
		lastUse = System.currentTimeMillis();
		}
	}

	/**
	 * get the in gaem buffered image of this projectile/skill
	 * @param isTargetingMyTeam is the projectile targeting this team?
	 * @return the buffered image
	 */
	public BufferedImage[][] getProjectileIcon(boolean isTargetingMyTeam) {
		if (isTargetingMyTeam)
			return enemyProjectileSprite;
		else
			return projectileSprite;
	}
	

	
	

}
