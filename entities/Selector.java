package entities;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import entities.skills.BasicAttack;
import handlers.ResourceHandler;
import main.Main;

public class Selector {
	public static final int noOfShips = 2;
	
	
	/**
	 * Method called on start to initialise the memory storing sprites
	 */
	public static void initialise() {
		Main.cache.put("ship_type_2", ResourceHandler.getBlockSprites("spaceship/ship_type_2", 32, 32));
		Main.cache.put("enemy_type_2", ResourceHandler.getBlockSprites("spaceship/enemy_type_2", 32, 32));
		Main.cache.put("ship_type_1", ResourceHandler.getBlockSprites("spaceship/ship_type_1", 32, 32));
		Main.cache.put("enemy_type_1", ResourceHandler.getBlockSprites("spaceship/enemy_type_1", 32, 32));
	}
	
	
	/**
	 * Contains all the different types of ships that can be created.
	 * Ships made do not have an id, location or direction
	 * Constructor Used = Ship(type, maxHealth, attack, defence, maxVelocity, maxAcceleration, sprite, basic, special1, special2, utility);
	 * @param type An integer representing the type of ship that will be created.
	 * @return The ship for the type
	 */
	public static Ship getShip(int type) {
		AvailableSkill[] skills = getSkills(type);
		switch (type) {
		
			default:
				int[] stats = getShipStats(type);
				Ship ship = new Ship(type, stats[0], stats[1], stats[2], stats[3], stats[4], getShipSprite(type,true));
				ship.setBasic(new BasicAttack());
				ship.setSpecial1(skills[0].getSkill());
				ship.setSpecial2(skills[1].getSkill());
				ship.setUtility(skills[2].getSkill());
				return ship;
		}
	}
	
	
	/**
	 * Returns the stats of the ship based on the type received as
	 * an array.Values correspond to max health, attack, defence,
	 * max velocity and max acceleration.
	 * @param type The type of ship to get the stats for.
	 * @return Array of ship information
	 */
	public static int[] getShipStats(int type) {
		switch (type) {
		case 2:
			return new int[] {100, 10, 10, 10, 2};
		default:
			return new int[] {100, 15, 10, 7, 1};
			
		}
	}
	
	
	/**
	 * Returns the sprite for the given ship type as an image array
	 * @param type The type of ship to get the image for
	 * @return The sprite related to the ship
	 */
	public static Image[] getShipImage(int type, boolean myTeam) {
		switch (type) {
		case 2:				
			if (myTeam)
				return Main.cache.get("ship_type_2");
			else
				return Main.cache.get("enemy_type_2");
			default:				
				if (myTeam)
					return Main.cache.get("ship_type_1");
				else
					return Main.cache.get("enemy_type_1");
				
			}
	}
	
	/**
	 * Returns the whole sprite sheet for the type of ship
	 * Used for creating the player sprites
	 * @param type the type of ship the player chose
	 * @return
	 */
	public static BufferedImage getShipSpriteSheet(int type) {
		switch (type) {
			case 2:
				return ResourceHandler.getBufferedImage("spaceship/ship_type_2");
			default:				
				return ResourceHandler.getBufferedImage("spaceship/ship_type_1");
			}
	}
	
	
	/**
	 * Gets the overlay image for the ship. Used for customisation
	 * @param type the type of ship to get the overlay for
	 * @return the image
	 */
	public static BufferedImage getShipOverlay(int type) {
		switch (type) {
			case 2:
				return ResourceHandler.getBufferedImage("spaceship/overlay_type_2");
		
			default:				
				return ResourceHandler.getBufferedImage("spaceship/overlay_type_1");
			}
	}
	
	/**
	 * Returns the size of the ship
	 * @param type the type of ship to get the dimensions for
	 * @return
	 */
	public static Dimension getShipSize(int type) {
		switch (type) {
		case 2:
			return new Dimension(32,32);
		default:				
			return new Dimension(32,32);
		}
	}
	
	/**
	 * Returns the base skills for the ship of a given type
	 * @param type The type of ship to get the skills for
	 * @return array of Available skills the ship can use
	 */
	public static AvailableSkill[] getSkills(int type) {
		AvailableSkill[] skills = new AvailableSkill[3];
				
		switch (type) {
		case 2: 
			skills[0] = AvailableSkill.LightningOrbSkill;
			skills[1] =  AvailableSkill.PenetrateSkill;
			skills[2] =  AvailableSkill.FlashSkill;
			break;
		default : 
			skills[0] =  AvailableSkill.PenetrateSkill;
			skills[1] =  AvailableSkill.ArmourSkill;
			skills[2] =  AvailableSkill.DashSkill;
			break;
		}
		return skills;
	}
	
	/**
	 * Returns the sprite for the given ship type 2D Buffered Image
	 * array.
	 * @param type The type of ship to get the array for
	 * @return The sprite related to the ship
	 */
	public static BufferedImage[][] getShipSprite(int type,boolean myTeam) {
		switch (type) {
			case 2:
				if (myTeam)
					return ResourceHandler.getPlayerSprites("spaceship/ship_type_2", 32, 32);
				else
					return ResourceHandler.getPlayerSprites("spaceship/enemy_type_2", 32, 32);
			
			default:
				if (myTeam)
					return ResourceHandler.getPlayerSprites("spaceship/ship_type_1", 32, 32);
				else
					return ResourceHandler.getPlayerSprites("spaceship/enemy_type_1", 32, 32);
			}
	}

	
	
}
