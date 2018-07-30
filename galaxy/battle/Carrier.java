package galaxy.battle;

import gameLogic.PlayerStats;
import handlers.MathHandler;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

/**
 * contains code for the creation and control of a carrier ship
 * 
 * 
 * 
 *
 */
public class Carrier extends Entity {

	private int orbit;

	/**
	 * creates a new carrier
	 * 
	 * @param path
	 *            the path to the image to use
	 * @param x
	 *            the x coordinate to start it at
	 * @param y
	 *            the y coordinate to start it at
	 * @param type
	 *            the type of AI to use
	 * @param shipid
	 *            the shipid
	 * @param heatPojectilesCount
	 *            the number of heat projectiles
	 * @param heatShotInterval
	 *            the heat shot interval
	 */
	public Carrier(String path, float x, float y, Entity.TYPE type, int shipid, int heatPojectilesCount,
			int heatShotInterval, int collisionDamage, int magneticLaserCount, int magneticShotInterval, int paralysationLength) {
		super(path, x, y, 32, 32, 500, shipid, heatPojectilesCount, heatShotInterval, collisionDamage,magneticLaserCount, magneticShotInterval, paralysationLength);
		this.type = type;
		totalHealth = 100;
		currentHealth = totalHealth;
		damage = 20;
		speed = 1.5f;
		orbit = MathHandler.random.nextInt(2) * 2 - 1;
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
			case DEFENSIVE:
				executeDEFENSIVEBehaviour();
				break;
			case ALTERNATIVE:
				this.executeALTERNATIVEBehaviour();
				break;
			case TACTICAL:
				executeTACTICALBehaviour();
				break;
			case SURVIVOR:
				executeSURVIVORBehaviour(team, battle,"");
				break;
			case KINGCRUSHER:
				executeKINGCRUSHERBehaviour();
				break;
			case BUGFIXER:
				executeBUGFIXERBehaviour();
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

	@Override
	protected void executeTACTICALBehaviour() {

		if (centre.distance(target) > 600) {
			Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		} else if (centre.distance(target) < 300) {
			Point2D.Double vector = MathHandler.getPoint2D(target, centre, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		} else {
			Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
			centre.x -= vector.y * orbit;
			centre.y += vector.x * orbit;
		}
	}

	@Override
	protected void executeALTERNATIVEBehaviour() {

		Random rand = new Random();
		switch (rand.nextInt() % (behaviourCount - 1)) {
		case 0:
			this.executeAGGRESSIVEBehaviour();
			break;
		case 1:
			this.executeDEFENSIVEBehaviour();
			break;
		case 2:
			this.executeTACTICALBehaviour();
		case 3:
			this.executeKINGCRUSHERBehaviour();
		}
	}

	@Override
	protected void executeKINGCRUSHERBehaviour() {

		if (centre.distance(target) > 400) {
			Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		} else if (centre.distance(target) < 200) {
			Point2D.Double vector = MathHandler.getPoint2D(target, centre, speed, 0);
			centre.x += vector.x;
			centre.y += vector.y;
		} else {
			Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
			centre.x -= vector.y * orbit;
			centre.y += vector.x * orbit;
		}
	}

	@Override
	protected void executeBUGFIXERBehaviour() {
		if(this.bugFixingChances >= 0 ){
			currentHealth = Math.min(currentHealth + totalHealth/5, totalHealth);
			this.bugFixingChances--;
		}
		executeALTERNATIVEBehaviour();
	}

	@Override
	protected void executeSURVIVORBehaviour(List<Entity> team, Battle battle,String shipImage) {
		
		if (this.currentHealth <= 0){
			
			Random random = new Random();
			for (int i = 0; i < 3; i ++){
				team.add(
						new Fighter(
								shipImage, 
								(int) (random.nextInt(30) * (random.nextInt() % 2 == 0 ? 1 : -1)),
								(int) (random.nextInt(30) * (random.nextInt() % 2 == 0 ? 1 : -1)), 
								Entity.getRandomType(random, battle.getShipBehaviourRange()), 
								this,
								PlayerStats.SHIPTYPE1ID, 
								battle.getStandardFighterShipMagneticProjectileCount(),
								battle.getStandardFighterShipMagneticShotInterval(), 
								battle.getStandardFighterShipCollisionDamage(),
								battle.getStandardFighterShipMagneticProjectileCount(), 
								battle.getStandardFighterShipMagneticShotInterval(),
								battle.getStandardFighterShipParalysationLenght()
						)
			    );
			}
		} else{
			this.executeAGGRESSIVEBehaviour();
		}
		
		
	}

	/**
	 * @return Carrier
	 */
	
	@Override
	public String getShipType(){
		return "Carrier";
	}

}
