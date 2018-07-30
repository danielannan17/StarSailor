package entities;
import java.awt.Dimension;
/***
 * a class to represent a ship
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import entities.skills.Skill;
import entities.skills.TimedSkill;

public class Ship extends Entity implements Serializable {
	
	transient public int type;
	float health,armour;
	Rectangle healthbar, armorbar;
	transient int attack, team, defence, maxHealth;
	int state;
	transient public boolean isDashing,isLeader,isInFlock,isPlayer;
	float flockStickyStrength = 1;
	Ship currentLeader;
	transient Skill basic, skill1, skill2, utility;
	boolean[] activated = new boolean[]{false,false,false,false};
	long last = 0;
	transient LinkedList<TimedSkill> activeSkills;
	LinkedList<Integer> buffs;
	LinkedList<Entity> myEntities;
	boolean usingSkill;
	
	/**
	 * get the type of the ship
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * set the type of the ship
	 * @param type the new type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * set the basic skill of the ship
	 * @param basic the basic skill
	 */
	public void setBasic(Skill basic) {
		if (TimedSkill.class.isInstance(basic)) {
			activeSkills.add((TimedSkill)basic);
		}
		this.basic = basic;
	}

	/**
	 * Gets the basic skill for the ship
	 * @return the skill
	 */
	public Skill getBasic() {
		return basic;
	}

	/**
	 * set the special weapon 1 of the ship
	 * @param special1 the special weapon
	 */
	public void setSpecial1(Skill special1) {
		if (TimedSkill.class.isInstance(special1)) {
			activeSkills.add((TimedSkill)special1);
		}
		this.skill1 = special1;
	}

	/**
	 * set the special weapon 2 of the ship
	 * @param special2 the special weapon
	 */
	public void setSpecial2(Skill special2) {
		if (TimedSkill.class.isInstance(special2)) {
			activeSkills.add((TimedSkill)special2);
		}
		this.skill2 = special2;
	}
	
	/**
	 * get the special skill 1 of the ship
	 * @return the special skill
	 */
	public Skill getSkill1() {
		return skill1;
	}

	/**
	 * get the special skill 2 of the ship
	 * @return the special skill
	 */
	public Skill getSkill2() {
		return skill2;
	}

	/**
	 * get the utility skill of the ship
	 * @return the utility skill of the ship
	 */
	public Skill getUtility() {
		return utility;
	}

	/**
	 * set the utility skill of the ship
	 * @param utility the new utility skill
	 */
	public void setUtility(Skill utility) {
		if (TimedSkill.class.isInstance(utility)) {
			activeSkills.add((TimedSkill)utility);
		}
		this.utility = utility;
	}

	/**
	 * set the team of the ship
	 * @param team the team
	 */
	public void setTeam(int team) {
		this.team = team;
	}

	/**
	 * deactivate the ships skills
	 */
	public void deactiveSkills() {
		for (TimedSkill s : activeSkills) {
			s.deactivateSkill(this);
		}
	}
	
	/**
	 * get a list of the ships skills
	 * @return a list of skills
	 */
	public LinkedList<TimedSkill> getActiveSkills() {
		return activeSkills;
	}
	
	/**
	 * add a new skill to a ship
	 * @param skill the skill to be added
	 */
	public void addActiveSkill(TimedSkill skill) {
		activeSkills.add(skill);
	}

	/**
	 * take health from the ship
	 * @param damage the amount of health to take off
	 */
	public void takeDamage(float damage) {
		System.out.println(armour);
		if (armour > 0) {
			armour -= damage;
			if (armour < 0) {
				health += armour;
				armour = 0;
			}
		} else {
			health -= damage;
		}
		
	}

	
	/**
	 * create a ship
	 * @param id the id of the ship
	 * @param xLocation the x position
	 * @param yLocation the y position
	 */
	public Ship(String id, int xLocation, int yLocation) {
		super(xLocation,yLocation);
		this.id = id;
	}
	
	/**
	 * create a ship
	 * @param type the type of the ship
	 * @param maxHealth the maximum health of the ship
	 * @param attack the magnitude of the attack
	 * @param defence the magnitude of the defence
	 * @param maxVelocity the maximum velocity the ship can travel at
	 * @param maxAcceleration the maximum acceleration the ship may travel at
	 * @param basic the basic skill of the ship
	 * @param special1 the 1st special skill of the ship
	 * @param special2 the 2nd special skill of the ship
	 * @param utilitythe utility skill of the ship
	 */
	public Ship(int type, int maxHealth, int attack, int defence, float maxVelocity, float maxAcceleration, Skill basic,
			Skill special1, Skill special2, Skill utility) {
		super(maxVelocity,maxAcceleration);
		this.accelerationQueue = new CopyOnWriteArrayList<Vector2D>();
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
		this.type = type; 
		armour = 0;
		setSprite(Selector.getShipSprite(type,true));
		this.maxHealth = maxHealth;
		myEntities = new LinkedList<Entity>();
		this.health = maxHealth;
		this.attack = attack;
		this.defence = defence;
		buffs = new LinkedList<Integer>();
		this.activeSkills = new LinkedList<TimedSkill>();
		setBasic(basic);
		setSpecial1(special1);
		setSpecial2(special2);
		setUtility(utility);

	}
	
	/**
	 * create a ship
	 * @param type the type of the ship
	 * @param maxHealth the max health of the ship
	 * @param attack the magnitude of the attack of the ship
	 * @param defence the magnitude of the defense of the ship
	 * @param maxVelocity the maximum velocity of the ship
	 * @param maxAcceleration the maximum acceleration of the ship
	 * @param sprite the buffered image of the ship
	 */
	public Ship(int type, int maxHealth, int attack, int defence, float maxVelocity, float maxAcceleration, BufferedImage[][] sprite) {
		super(maxVelocity, maxAcceleration, sprite);
		this.type = type;
		this.maxHealth = maxHealth;
		armour = 0;
		setSprite(Selector.getShipSprite(type,true));
		myEntities = new LinkedList<Entity>();
		buffs = new LinkedList<Integer>();
		this.health = maxHealth;
		this.attack = attack;
		this.defence = defence;
		this.activeSkills = new LinkedList<TimedSkill>();
	}
	
	/**
	 * create a ship - testing only
	 * @param x the x value
	 * @param y the y value
	 * @param vel the velocity of the ship
	 * @param id the id of the ship
	 */
	public Ship(int x,int y,Vector2D vel,String id) {
		super(x,y);
		position = new Vector2D(x,y);
		armour = 0;
		velocity = vel;
		this.id = id;
		setSize(new Dimension(10,10));
		this.hasAirResistance = true;
		acceleration = new Vector2D();
		maxAcceleration = 1;
	}
	
	

	/**
	 * get the current health of the ship
	 * @return the current health
	 */
	public float getHealth() {
		return health;
	}
	
	/**
	 * set the health of the ship
	 * @param health the new health
	 */
	public void setHealth(float health) {
		this.health = health;
	}

	/**
	 * get the attack value of the ship
	 * @return the attack value
	 */
	public int getAttack() {
		return attack;
	}

	/**
	 * set the attack value of the ship
	 * @param attack the value of the attack
	 */
	public void setAttack(int attack) {
		this.attack = attack;
	}

	/**
	 * get the defence of the ship
	 * @return the defence
	 */
	public int getDefence() {
		return defence;
	}
	
	/**
	 * set the defence of the ship
	 * @param defence the new defence of the ship
	 */
	public void setDefence(int defence) {
		this.defence = defence;
	}
	
	/**
	 * get the maximum health of the ship
	 * @return the maximum health of the ship
	 */
	public int getMaxHealth() {
		return maxHealth;
	}
	
	/**
	 * set the maximum health of the ship
	 * @param maxHealth the maximum health
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	/**
	 * get the state of the ship
	 * @return the state
	 */
	public int getState() {
		return state;
	}
	
	/**
	 * set the state of the ship
	 * @param state the new state of the ship
	 */
	public void setState(int state) {
		this.state = state;
	}
	
	/**
	 * get the amount of armour left on the ship
	 * @return the amount of armour
	 */
	public float getArmour() {
		return armour;
	}

	/**
	 * set the armour of the ship
	 * @param armour the new armour value
	 */
	public void setArmour(float armour) {
		this.armour = armour;
	}

	/**
	 * is the ship alive?
	 * @return alive or not
	 */
	public boolean isAlive() {
		return health > 0;
	}
	
	/**
	 * return the entities associated with the ship, then clear them entities
	 * @return the entities of the ship (projectiles usually)
	 */
	public LinkedList<Entity> getMyEntities() {
		LinkedList<Entity> toReturn = new LinkedList<Entity>();
		if (!myEntities.isEmpty())
			toReturn.addAll(myEntities);
		myEntities.clear();
		return toReturn;
	}
	
	/**
	 * use a skill
	 * @param skill the skill to be used
	 */
	public void useSkill(int skill) {
		switch (skill) {
		case 0:
			basic.doSkill(this);
			break;
		case 1:
			skill1.doSkill(this);
			break;
		case 2:
			skill2.doSkill(this);
			break;
		case 3:
			utility.doSkill(this);
			break;
		default :
			basic.doSkill(this);
			break;
		}
		
	
	}

	/**
	 * set the velocity of the ship
	 * @param velocity the new velocity of the ship
	 */
	public void setVelocity(Vector2D velocity){
		if (state != 1 && velocity.getMagnitude() >= maxVelocity)
		{
			this.velocity = velocity;
			this.velocity.setMagnitude(maxVelocity);
		}
		else
		{
			this.velocity = velocity;
		}
	}
	
	/**
	 * get the team of the ship
	 * @return the team
	 */
	public int getTeam() {
		return team;
	}

	/**
	 * add an entity to 'myEntities'
	 * @param entity the entity to be added
	 */
	public void addEntity(Entity entity) {
		myEntities.add(entity);
	}
	
	/**
	 * move the ship up
	 */
	public void moveUp() {
		Vector2D move = new Vector2D(0,-1);
		move.setMagnitude(1);
		this.addAccelerationToQueue(move);
	}
	
	/**
	 * move the ship down
	 */
	public void moveDown() {
		Vector2D move = new Vector2D(0,1);
		move.setMagnitude(1);
		this.addAccelerationToQueue(move);
	}
	
	/**
	 * move the ship left
	 */
	public void moveLeft() {
		Vector2D move = new Vector2D(-1,0);
		move.setMagnitude(1);
		this.addAccelerationToQueue(move);
	}

	/**
	 * move the ship right
	 */
	public void moveRight() {
		Vector2D move = new Vector2D(1,0);
		move.setMagnitude(1);
		this.addAccelerationToQueue(move);
	}
}
