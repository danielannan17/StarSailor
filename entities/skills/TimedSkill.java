package entities.skills;

import entities.Ship;

/***
 * interface for timed skills
 *
 */

public interface TimedSkill extends Skill {
	/**
	 * Deactivates the skill for the ship given
	 * @param ship the ship to deactivate skill
	 */
	public void deactivateSkill(Ship ship);
}
