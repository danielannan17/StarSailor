package galaxy.battle;

import handlers.MathHandler;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

/**
 * contains code for the creation and control of a fighter
 * 
 * 
 * 
 *
 */
public class Fighter extends Entity {

	private Entity leader;
	private int orbit;
	
	/**
	 * creates a new fighter
	 * 
	 * @param path
	 *            the path to its sprites
	 * @param x
	 *            the initial x coord
	 * @param y
	 *            the initial y coord
	 * @param type
	 *            the type of AI to use
	 * @param leader
	 *            the leader to follow
	 * @param shipid
	 *            the ship id
	 * @param longRangeLasersCount
	 * @param heatShotInterval
	 */
	public Fighter(String path, int x, int y, Entity.TYPE type, Entity leader, int shipid, int longRangeLasersCount,
			int heatShotInterval, int collisionDamage, int magneticLaserCount, int magneticShotInterval, int paralysationLength) {
		super(path, x, y, 16, 16, 500, shipid, longRangeLasersCount, heatShotInterval, collisionDamage, magneticLaserCount, magneticShotInterval, paralysationLength);

		this.type = type;
		this.leader = leader;

		totalHealth = 30;
		currentHealth = totalHealth;

		damage = 5;
		speed = 4.0f;

		orbit = MathHandler.random.nextInt(2) * 2 - 1;

		shotInterval = (long) (speed * animationTime * 4);

		prevShoot = System.currentTimeMillis();

	}

	@Override
	public boolean update(float time, int width, int height, List<Entity> team, Battle battle) {
		super.update(time, width, height, team, battle);
		Point2D.Double centre = new Point2D.Double(this.centre.x + leader.getCentre().x,
				this.centre.y + leader.getCentre().y);
		switch (this.type) {
			case AGGRESSIVE:
				executeAGGRESSIVEBehaviour();
				break;
			case DEFENSIVE:
				executeDEFENSIVEBehaviour();
				break;
			case ALTERNATIVE:
				executeALTERNATIVEBehaviour();
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
			default:
				break;
		}
		if (centre.x < 0) {
			this.centre.x = 0;
		} else if (centre.x > width) {
			this.centre.x = 0;
		}
		if (centre.y < 0) {
			this.centre.y = 0;
		} else if (centre.y > height) {
			this.centre.y = 0;
		}
		if (System.currentTimeMillis() - prevShoot >= shotInterval) {
			prevShoot = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void draw(Graphics g, float scale) {
		Point2D.Double centre = new Point2D.Double(this.centre.x + leader.getCentre().x,
				this.centre.y + leader.getCentre().y);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform t = g2d.getTransform();
		AffineTransform at = new AffineTransform();
		at.rotate(MathHandler.getAngle(centre, target), centre.x, centre.y);
		if (currentHealth > 0) {
			g2d.setColor(Color.blue);
			g2d.fillRect((int) (centre.x - width / 2.0), (int) centre.y - (int) (height), (int) width,
					(int) height / 8);
			g2d.setColor(Color.green);
			float progress = currentHealth * (width / totalHealth);
			g2d.fillRect((int) (centre.x - width / 2.0f), (int) centre.y - (int) (height), (int) progress,
					(int) height / 8);
		}
		g2d.setTransform(at);
		g2d.drawImage(sprites[frame], (int) (this.centre.x - width / 2.0f + leader.getCentre().x),
				(int) (this.centre.y - height / 2.0f + leader.getCentre().y), null);
		g2d.setTransform(t);
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
	public Point2D.Double getCentre() {
		return new Point2D.Double(centre.x + leader.getX(), centre.y + leader.getY());
	}

	/**
	 * gets the leader
	 * 
	 * @return the leader
	 */
	public Entity getLeader() {
		return this.leader;
	}

	@Override
	protected void executeAGGRESSIVEBehaviour() {
		Point2D.Double centre = new Point2D.Double(this.centre.x + leader.getCentre().x,
				this.centre.y + leader.getCentre().y);
		if (leader == null || leader.getCurrentHealth() <= 0) {
			if (centre.distance(target) > 150) {
				Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else if (centre.distance(target) < 100) {
				Point2D.Double vector = MathHandler.getPoint2D(target, centre, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			}
		} else {
			if (centre.distance(leader.getCentre()) > 60) {
				Point2D.Double vector = MathHandler.getPoint2D(centre, leader.getCentre(), speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else if (centre.distance(leader.getCentre()) < 30) {
				Point2D.Double vector = MathHandler.getPoint2D(leader.getCentre(), centre, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else {
				Point2D.Double vector = MathHandler.getPoint2D(centre, leader.getCentre(), speed, 0);
				this.centre.x -= vector.y * orbit;
				this.centre.y += vector.x * orbit;
			}
		}

	}

	@Override
	protected void executeDEFENSIVEBehaviour() {
		Point2D.Double centre = new Point2D.Double(this.centre.x + leader.getCentre().x,
				this.centre.y + leader.getCentre().y);
		if (leader == null || leader.getCurrentHealth() <= 0) {
			if (centre.distance(target) < 500) {
				Point2D.Double vector = MathHandler.getPoint2D(target, centre, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else if (centre.distance(target) > 550) {
				Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			}
		} else {
			if (centre.distance(leader.getCentre()) > 60) {
				Point2D.Double vector = MathHandler.getPoint2D(centre, leader.getCentre(), speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else if (centre.distance(leader.getCentre()) < 30) {
				Point2D.Double vector = MathHandler.getPoint2D(leader.getCentre(), centre, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else {
				Point2D.Double vector = MathHandler.getPoint2D(centre, leader.getCentre(), speed, 0);
				this.centre.x -= vector.y * orbit;
				this.centre.y += vector.x * orbit;
			}
		}
	}

	@Override
	protected void executeTACTICALBehaviour() {
		Point2D.Double centre = new Point2D.Double(this.centre.x + leader.getCentre().x,
				this.centre.y + leader.getCentre().y);
		if (leader == null || leader.getCurrentHealth() <= 0) {
			if (centre.distance(target) > 600) {
				Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else if (centre.distance(target) < 300) {
				Point2D.Double vector = MathHandler.getPoint2D(target, centre, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else {
				Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
				this.centre.x -= vector.y * orbit;
				this.centre.y += vector.x * orbit;
			}
		} else {
			if (centre.distance(leader.getCentre()) > 60) {
				Point2D.Double vector = MathHandler.getPoint2D(centre, leader.getCentre(), speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else if (centre.distance(leader.getCentre()) < 30) {
				Point2D.Double vector = MathHandler.getPoint2D(leader.getCentre(), centre, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else {
				Point2D.Double vector = MathHandler.getPoint2D(centre, leader.getCentre(), speed, 0);
				this.centre.x -= vector.y * orbit;
				this.centre.y += vector.x * orbit;
			}
		}
	}

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

	@Override
	protected void executeKINGCRUSHERBehaviour() {
		Point2D.Double centre = new Point2D.Double(this.centre.x + leader.getCentre().x,
				this.centre.y + leader.getCentre().y);
		if (leader == null || leader.getCurrentHealth() <= 0) {
			if (centre.distance(target) > 300) {
				Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else if (centre.distance(target) < 100) {
				Point2D.Double vector = MathHandler.getPoint2D(target, centre, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else {
				Point2D.Double vector = MathHandler.getPoint2D(centre, target, speed, 0);
				this.centre.x -= vector.y * orbit;
				this.centre.y += vector.x * orbit;
			}
		} else {
			if (centre.distance(leader.getCentre()) > 60) {
				Point2D.Double vector = MathHandler.getPoint2D(centre, leader.getCentre(), speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else if (centre.distance(leader.getCentre()) < 30) {
				Point2D.Double vector = MathHandler.getPoint2D(leader.getCentre(), centre, speed, 0);
				this.centre.x += vector.x;
				this.centre.y += vector.y;
			} else {
				Point2D.Double vector = MathHandler.getPoint2D(centre, leader.getCentre(), speed, 0);
				this.centre.x -= vector.y * orbit;
				this.centre.y += vector.x * orbit;
			}
		}
	}
	
	/**
	 * Execute the BUGFIXER Behaviour for a fighter
	 */
	
	@Override
	protected void executeBUGFIXERBehaviour() {
		
		if(this.bugFixingChances >= 0 ){
			currentHealth = Math.min(currentHealth + totalHealth/6, totalHealth);
			this.bugFixingChances--;
		}
		executeALTERNATIVEBehaviour();
	}
	
	/**
	 * Execute the SURVIVOR Behaviour for a fighter
	 */
	
	@Override
	protected void executeSURVIVORBehaviour(List<Entity> team, Battle battle, String shipImage) {
		
		if(this.currentHealth <= 0 && this.bugFixingChances >0){
			this.currentHealth = this.totalHealth/10;
			this.bugFixingChances -= 3;
		}
		
		executeTACTICALBehaviour();
	}
	
	/**
	 * @return Fighter
	 */
	
	@Override
	public String getShipType(){
		return "Fighter";
	}
	
}