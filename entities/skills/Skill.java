package entities.skills;

/***
 * interface for skills
 */
import java.awt.image.BufferedImage;

import entities.Ship;

public interface Skill {
	
	
	/**
	 * Activates the skill for the given ship
	 * @param ship ship to activate skill
	 */
	public abstract void doSkill(Ship ship);
	
	/**
	 * Checks if the skill is available
	 * @return is available
	 */
	public abstract boolean canDoSkill();

}
