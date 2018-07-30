package galaxy.battle;

import gameLogic.PlayerStats;
import handlers.MathHandler;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

/**
 * contains code for the creation and control of a command ship
 * 
 * 
 * 
 *
 */
public class Command extends Entity {

	private boolean hasSingularityPointWeapon;

	/**
	 * creates a new command ship
	 * 
	 * @param path
	 *            the path to its sprites
	 * @param x
	 *            its initial x coord
	 * @param y
	 *            its initial y coord
	 * @param type
	 *            the type of AI to use
	 * @param shipid
	 *            its shipid
	 * @param heatPojectilesCount
	 *            its heat projectile count
	 * @param heatShotInterval
	 *            its heat projectile interval
	 */
	public Command(String path, int x, int y, Entity.TYPE type, int shipid, int heatPojectilesCount,
			int heatShotInterval, int collisionDamage, int magneticLaserCount, int magneticShotInterval,
			int paralysationLength) {
		super(path, x, y, 128, 32, 500, shipid, heatPojectilesCount, heatShotInterval, collisionDamage,
				magneticLaserCount, magneticShotInterval, paralysationLength);
		this.type = type;
		System.out.println(this.type);
		totalHealth = 200;
		currentHealth = totalHealth;
		damage = 45;
		speed = 1.0f;
		shotInterval = (long) (speed * animationTime * 4);
		prevShoot = System.currentTimeMillis();
	}

	@Override
	public boolean update(float time, int width, int height, List<Entity> team, Battle battle) {
		super.update(time, width, height, team, battle);
		switch (type) {
		case AGGRESSIVE:
			executeAGGRESSIVEBehaviour();
			break;
		case ALTERNATIVE:
			executeALTERNATIVEBehaviour();
			break;
		case DEFENSIVE:
			executeDEFENSIVEBehaviour();
			break;
		case TACTICAL:
			executeTACTICALBehaviour();
			break;
		case SURVIVOR:
			executeSURVIVORBehaviour(team, battle,"1");
			break;
		case KINGCRUSHER:
			executeKINGCRUSHERBehaviour();
			break;
		
		default:
			break;
		}
		if (centre.x < 0) {
			centre.x = 0;
		} else if (centre.x > width) {
			centre.x = width;
		}
		if (centre.y < 0) {
			centre.y = 0;
		} else if (centre.y > height) {
			centre.y = height;
		}
		if (System.currentTimeMillis() - prevShoot >= shotInterval) {
			prevShoot = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void incrementFrame() {
		if (getFrame() == 0) {
			setFrame(1);
		} else {
			setFrame(0);
		}
	}
	
	/**
	 * Execute the distance adjustment towards the target according to the AGGRESIVE type standard
	 */
	
	@Override
	protected void executeAGGRESSIVEBehaviour() {

		if (centre.distance(target) > 150) {
			Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		} else if (centre.distance(target) < 100) {
			Point2D.Double vector = MathHandler.getPoint2D(target, centre, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		}
	}
	
	/**
	 * Execute the distance adjustment towards the target according to the DEFENSIVE type standard
	 */
	
	@Override
	protected void executeDEFENSIVEBehaviour() {

		if (centre.distance(target) < 500) {
			Point2D.Double vector = MathHandler.getPoint2D(target, centre, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		} else if (centre.distance(target) > 550) {
			Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		}
	}
	
	/**
	 * Execute the distance adjustment towards the target according to the TACTICAL type standard
	 */
	@Override
	protected void executeTACTICALBehaviour() {

		if (centre.distance(target) > 200) {
			Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		} else if (centre.distance(target) < 100) {
			Point2D.Double vector = MathHandler.getPoint2D(target, centre, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		}
	}
	/**
	 * Switch between different type standards
	 */
	@Override
	protected void executeALTERNATIVEBehaviour() {

		if(System.currentTimeMillis() - this.lastSwitchTime > this.alternativeAISwitchInterval){
			
			Random rand = new Random();
			switch (rand.nextInt() % behaviourCount) {
				case 0:
					executeAGGRESSIVEBehaviour();
					currentTYPE = TYPE.AGGRESSIVE;
					break;
				case 1:
					executeDEFENSIVEBehaviour();
					currentTYPE = TYPE.DEFENSIVE;
					break;
				case 2:
					executeTACTICALBehaviour();
					currentTYPE = TYPE.TACTICAL;
				case 3:
					executeKINGCRUSHERBehaviour();
					currentTYPE = TYPE.KINGCRUSHER;
			}
			this.lastSwitchTime = System.currentTimeMillis();
			
		}else {
			switch (this.currentTYPE){
				case AGGRESSIVE:
					executeAGGRESSIVEBehaviour();
				case DEFENSIVE:
					executeDEFENSIVEBehaviour();
				case TACTICAL:
					executeTACTICALBehaviour();
				case KINGCRUSHER:
					executeKINGCRUSHERBehaviour();
				default:
					executeAGGRESSIVEBehaviour();
			}
		}

	}
	
	/**
	 * Execute the distance adjustment towards the target according to the KINGCRUSHER type standard
	 */
	protected void executeKINGCRUSHERBehaviour() {

		if (centre.distance(target) < 450) {
			Point2D.Double vector = MathHandler.getPoint2D(target, centre, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		} else if (centre.distance(target) > 400) {
			Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		}
	}

	public void setHasSingularityPointWeapon(boolean value) {
		hasSingularityPointWeapon = value;
	}

	public boolean getHasSingularityPointWeapon() {
		return hasSingularityPointWeapon;
	}
	
	/**
	 * Execute the distance adjustment towards the target according to the BUGFIXER type standard
	 * do small ship reparations
	 */
	
	@Override
	protected void executeBUGFIXERBehaviour() {

		if (this.bugFixingChances >= 0) {
			currentHealth = Math.min(currentHealth + totalHealth / 4, totalHealth);
			this.bugFixingChances--;
		}
		executeALTERNATIVEBehaviour();
	}

	/**
	 * Execute the distance adjustment towards the target according to the SURVIVOR type standard
	 * if entity gets destroyed replace it with weaker entities
	 */
	
	@Override
	protected void executeSURVIVORBehaviour(List<Entity> team, Battle battle, String shipImage) {
		
		System.out.println("CALLED" + " " + shipImage);
		
		if (this.currentHealth <= 0) {
			
			Random random = new Random();
			for (int i = 0; i < 7; i++) {
				team.add(new Fighter(shipImage,
						(int) (random.nextInt(30) * (random.nextInt() % 2 == 0 ? 1 : -1)),
						(int) (random.nextInt(30) * (random.nextInt() % 2 == 0 ? 1 : -1)),
						Entity.getRandomType(random, battle.getShipBehaviourRange()), this, PlayerStats.SHIPTYPE1ID,
						battle.getStandardFighterShipMagneticProjectileCount(),
						battle.getStandardFighterShipMagneticShotInterval(),
						battle.getStandardFighterShipCollisionDamage(),
						battle.getStandardFighterShipMagneticProjectileCount(),
						battle.getStandardFighterShipMagneticShotInterval(),
						battle.getStandardFighterShipParalysationLenght()));
			}
		} else {
			this.executeAGGRESSIVEBehaviour();
		}

	}
	
	/**
	 * @return Command
	 */
	@Override
	public String getShipType(){
		return "Command";
	}
	
}
