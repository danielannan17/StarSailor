package entities.skills;
/***
 * class to represent the armour of a ship
 */

import java.awt.image.BufferedImage;

import entities.Ship;
import handlers.ResourceHandler;

public class Armour implements TimedSkill {
	public static final float value = 100;
	boolean isActive = false;
	long cooldown = 12000, lastUse = 0, duration = 8000;
	final static BufferedImage skillIcon = ResourceHandler.getBufferedImage("skill/armourIcon");
	

	
	public Armour() {
	}
	
	@Override
	public boolean equals(Object obj) {
		return Armour.class.isInstance(obj);
	}
	
	/**
	 * use the skill
	 * @param ship the ship to use it for
	 */
	@Override
	public void doSkill(Ship ship) {
		if (canDoSkill() && ship.getArmour() <= 10) {
			ship.setArmour(value);
			isActive = true;
			lastUse = System.currentTimeMillis();
		}
	}

	/**
	 * can the skill be used?
	 * @return usable?
	 */
	@Override
	public boolean canDoSkill() {
		return lastUse + cooldown < System.currentTimeMillis();
	}

	/**
	 * deactivate this skill
	 * @param ship the ship to deactivate it for
	 */
	@Override
	public void deactivateSkill(Ship ship) {
		if (isActive && lastUse + duration < System.currentTimeMillis()) {
			ship.setArmour(0);	
			isActive = false;
		} 
	}

	/**
	 * get the buffered image of the skill icon
	 * @return the buffered image
	 */
	public static BufferedImage getSkillIcon() {
		return skillIcon;
	}

}
