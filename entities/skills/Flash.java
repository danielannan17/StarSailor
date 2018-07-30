package entities.skills;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import entities.AvailableSkill;
import entities.Ship;
import entities.Vector2D;
import entities.FlashMessage;
import handlers.ResourceHandler;
import instantBattle.BattleController;

/***
 * class to represent a flash skill
 *
 */
public class Flash implements Skill{
	int distance = 200;
	long cooldown = 8000, lastUse = 0;
	final static BufferedImage skillIcon = ResourceHandler.getBufferedImage("skill/flashIcon");
	public static final Image[] startFrames = ResourceHandler.getBlockSprites("skill/flash", 57, 57), 
			endFrames = ResourceHandler.getBlockSprites("skill/end_flash", 57, 57);

	
	public Flash() {
	}
	
	@Override
	public boolean equals(Object obj) {
		return Flash.class.isInstance(obj);
	}
	/**
	 * use the skill
	 * @param ship the ship to use the skill for
	 */
	@Override
	public synchronized void doSkill(Ship ship) {
		if (canDoSkill()) {
	
			lastUse = System.currentTimeMillis();
		int x = ship.getxLocation() + (int) (distance * Math.cos(ship.getDirection()));
		if (x < 0) {
			x = 0;
		} else if (x > BattleController.width) {
			x = BattleController.width;
		}
		int y = ship.getyLocation() + (int) (distance * Math.sin(ship.getDirection()));
		if (y < 0) {
			y = 0;
		} else if (y > BattleController.height) {
			y = BattleController.height;
		}
		BattleController.toAdd.add(new FlashMessage(AvailableSkill.FlashSkill.id,
				ship.getxLocation(),ship.getyLocation(),x,y));
				ship.setLocationTeleport(x, y);

		}
	}

	/**
	 * can the skill be used right now?
	 * @return usable?
	 */
	@Override
	public boolean canDoSkill() {
		return lastUse + cooldown < System.currentTimeMillis();
	}


	/**
	 * get the buffered image associated with this skill
	 * @return the buffered image
	 */
	public static BufferedImage getSkillIcon() {
		return skillIcon;
	}
	
}
