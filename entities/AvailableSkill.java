package entities;

import entities.skills.*;
import main.Main;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public enum AvailableSkill {
	
	
	
	DashSkill(1,3), LightningOrbSkill(2,1), PenetrateSkill(3,1), BasicAttackSkill(4,1), FlashSkill(5,3), ArmourSkill(6,2);
	
	// 1 = Attack, 2 = Defence, 3 = Utility
	public final int id;
	public final int type;
	public static int noOfSkills = 6;
	
	
	
	/**
	 * Assigns an id and type to each available skill
	 * @param id The id for the skill
	 * @param type The type of the skill. 1 = Attack, 2 = Defence, 3 = Utility
	 */
	AvailableSkill(int id,int type) {
		this.id = id;
		this.type = type;
	}
	
	public static void initialise() { 
            Main.projectileCache.put("basic", new BasicAttack().getProjectileIcon(false));
            Main.projectileCache.put("eBasic", new BasicAttack().getProjectileIcon(true));
            Main.projectileCache.put("lightningOrb", new LightningOrb().getProjectileIcon(false));
            Main.projectileCache.put("eLightningOrb", new LightningOrb().getProjectileIcon(true));
            Main.projectileCache.put("penetrate", new Penetrate().getProjectileIcon(false));
            Main.projectileCache.put("ePenetrate", new Penetrate().getProjectileIcon(true));

	}
	
	/**
	 * Returns an array containing the skills based on the paramters given.
	 * @param attack Whether to include attack type skills
	 * @param defence Whether to include defence type skills
	 * @param utility Whether to include utility type skills
	 * @return Array of Skills
	 */
	public static AvailableSkill[] getSkills(boolean attack, boolean defence, boolean utility) {
		LinkedList<AvailableSkill> skills = new LinkedList<AvailableSkill>();
		for (AvailableSkill skill : AvailableSkill.values()) {
			if (attack && skill.type == 1 || defence && skill.type == 2 || utility && skill.type == 3) {
				skills.add(skill);
			}
		}
		
		AvailableSkill[] returning = new AvailableSkill[skills.size()];
		for (int i = 0; i < returning.length;i++) {
			returning[i] = skills.get(i);
		}
		return returning;
	}
	
	
	/**
	 * Returns the Skill Icon for the skill
	 * @return skill icon
	 */
	public BufferedImage getSkillIcon() {
		switch (this) {
			case DashSkill:
				return Dash.getSkillIcon();
			
			case LightningOrbSkill:
				return LightningOrb.getSkillIcon();
			
			case PenetrateSkill:
				return Penetrate.getSkillIcon();
			
			case BasicAttackSkill:
				return BasicAttack.getSkillIcon();
			case FlashSkill:
				return Flash.getSkillIcon();
			case ArmourSkill:
				return Armour.getSkillIcon();
		}
		return null;
	}
	
	/**
	 * Returns the Skill corresponding to the ID
	 * @param id The id of the skill to be returned
	 * @return Skill to be used
	 */
	public static Skill getSkill(int id) {
		switch (id) {
			case 1:
				return new Dash();
			
			case 2:
				return new LightningOrb();
			
			case 3:
				return new Penetrate();
			
			case 4:
				return new BasicAttack();
			case 5:
				return new Flash();
			case 6:
				return new Armour();
		}
		return null;	
	}
	
	/**
	 * If the skill of the given id has a projectile it returns the 
	 * projectile sprite sheet else it returns null.
	 * @param id the Id of the skill
	 * @return Sprite sheet of skill projectile
	 */
	public static BufferedImage[][] getSkillProjectile(int id, boolean isTargetingMyTeam) {
		switch (id) {
		case 2: 
			if (isTargetingMyTeam) {
				return Main.projectileCache.get("eLightningOrb");
			} else {
				return Main.projectileCache.get("lightningOrb");
			}
		case 3:
			if (isTargetingMyTeam) {
				return Main.projectileCache.get("ePenetrate");
			} else {
				return Main.projectileCache.get("penetrate");
			}
		case 4:
			if (isTargetingMyTeam) {
				return Main.projectileCache.get("eBasic");
			} else {
				return Main.projectileCache.get("basic");
			}
		default:
			return null;
		}
	}
	
	/**
	 * Returns the description of the skill corresponding to the id given or null
	 * if the skill does not exist.
	 * @param id The id of the skill
	 * @return Skill description or null
	 */
	
	public static String getSkillDescription(int id) {
		switch (id) {
		case 1:
			return "Gives a speed boost in the direction of movement.";
		
		case 2:
			return "Shoots a lightning orb that deals damage overtime to enemys touching it";
		
		case 3:
			return "Shoots a projectile that passes through " + Penetrate.penetration + " enemies.";
		
		case 4:
			return "Shoots a basic projectile.";
		case 5:
			return "Teleports you a set distance in the direction you are facing.";
		case 6:
			return "Gives you " + (int) Armour.value + " armour."; 
		}
		return null;
	}
	
	
	/**
	 * Returns the skill id of the AvailableSkill
	 * @return Id of the skill
	 */
	public int getSkillID() {
		switch (this) {
			case DashSkill:
				return DashSkill.id;
			
			case LightningOrbSkill:
				return LightningOrbSkill.id;
			
			case PenetrateSkill:
				return PenetrateSkill.id;
			
			case BasicAttackSkill:
				return BasicAttackSkill.id;
			case FlashSkill:
				return FlashSkill.id;
			case ArmourSkill:
				return ArmourSkill.id;
		}
		return 0;	
	}

	
	/**
	 * Returns the skill of the AvailableSkill
	 * @return The skill
	 */
	public Skill getSkill() {
		switch (this) {
			case DashSkill:
				return new Dash();
			
			case LightningOrbSkill:
				return new LightningOrb();
			
			case PenetrateSkill:
				return new Penetrate();
			
			case BasicAttackSkill:
				return new BasicAttack();
			case FlashSkill:
				return new Flash();
			case ArmourSkill:
				return new Armour();
		}
		return null;	
	}
}
	
	

