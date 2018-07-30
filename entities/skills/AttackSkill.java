package entities.skills;
/***
 * abstract class to represent an attacking skill
 *
 */

public abstract class AttackSkill implements Skill {
	
	protected float multiplier = 1;
	protected long lastUse = 0, cooldown = 1000;
	protected int effect = 0;
	
}
