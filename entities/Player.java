package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import entities.skills.BasicAttack;
import entities.skills.Skill;


public class Player extends Ship {

	
	/**
	 * Constructor method used to create the player
	 * @param id id of the player
	 * @param type type of the ship
	 * @param team the team the player is on
	 * @param xLocation x location of the player
	 * @param yLocation y location of the player
	 * @param stats the stats of the ship
	 * @param skill1 the first skill chosen
	 * @param skill2 the second skill chosen
	 * @param skill3 the third skill chosen
	 */
	public Player(String id,int type, int team, int xLocation, int yLocation, int[] stats, Skill skill1, Skill skill2,
			Skill skill3) {
		super(-type,stats[0],stats[1],stats[2],stats[3],
				stats[4],new BasicAttack(),skill1,skill2,skill3);
		setSprite(Selector.getShipSprite(type,true));
		setTeam(team);
		setLocation(xLocation,yLocation);
		setPosition(new Vector2D(xLocation,yLocation));		
		setId(id);
		isPlayer = true;
	}

	/**
	 * move the ship up
	 */
	public void moveUp() {
		Vector2D move = new Vector2D(0,-1);
		move.setMagnitude(2);
		this.addAccelerationToQueue(move);
	}
	
	/**
	 * move the ship down
	 */
	public void moveDown() {
		Vector2D move = new Vector2D(0,1);
		move.setMagnitude(2);
		this.addAccelerationToQueue(move);
	}
	
	/**
	 * move the ship left
	 */
	public void moveLeft() {
		Vector2D move = new Vector2D(-1,0);
		move.setMagnitude(2);
		this.addAccelerationToQueue(move);
	}

	/**
	 * move the ship right
	 */
	public void moveRight() {
		Vector2D move = new Vector2D(1,0);
		move.setMagnitude(2);
		this.addAccelerationToQueue(move);
	}

	
}
