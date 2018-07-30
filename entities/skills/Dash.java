package entities.skills;

import java.awt.image.BufferedImage;

/***
 * class to represent a dashing skill - increased speed
 */

import entities.Ship;
import entities.Vector2D;
import handlers.ResourceHandler;

public class Dash implements TimedSkill {
	boolean isActive = false;
	long duration = 1000;
	long cooldown = 4000, lastUse = 0;
	final static BufferedImage skillIcon = ResourceHandler.getBufferedImage("skill/dashIcon");

	
	
	public Dash() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		return Dash.class.isInstance(obj);
	}
	
	/**
	 * get the buffered image icon of this skill
	 * @return the buffered image
	 */
	public static BufferedImage getSkillIcon() {
		return skillIcon;
	}
	
	/**
	 * use this skill
	 * @param ship the ship to use the skill for
	 */
	@Override
	public void doSkill(Ship ship) {
		if (canDoSkill()) {
			ship.setVelocity(20);
			Vector2D acc = ship.getAcceleration().copy();
			acc.setMagnitude(1);
			ship.setAcceleration(acc);
			ship.setState(1);
			isActive = true;
			this.lastUse = System.currentTimeMillis();
		}
	}

	/**
	 * deactivate this skill
	 * @param ship the ship to deactivate the skill for
	 */
	@Override
	public void deactivateSkill(Ship ship) {
		if (isActive && lastUse + duration < System.currentTimeMillis()) {
			if (ship.getVelocity().getMagnitude() > ship.getMaxVelocity()) {
				ship.setVelocity((int) ship.getMaxVelocity());
			}
			isActive = false;
			ship.setState(0);
		}
	}
	
	/**
	 * is the skill usable at the moment?
	 * @return usable?
	 */
	@Override
	public boolean canDoSkill() {
		return lastUse + cooldown < System.currentTimeMillis();
	}
	
}
