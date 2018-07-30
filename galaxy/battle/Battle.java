package galaxy.battle;

import gameLogic.PlayerStats;
import handlers.InputHandler;
import handlers.MathHandler;
import handlers.ResourceHandler;
import handlers.SoundHandler;
import main.Main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * The campaign battle
 * 
 * 
 * 
 *
 */
public class Battle {

	private ArrayList<Entity> team1, team2;
	private ArrayList<Line2D.Double> lasersTeam1, lasersTeam2;
	private ArrayList<LongRangeLaser> longRangeLasersTeam1, longRangeLasersTeam2;
	private ArrayList<Line2D.Double> magneticLasersTeam1, magneticLasersTeam2;
	private Random random;
	private BufferedImage battleImage;
	private float xDif, yDif;
	private int width, height;
	private Image background = ResourceHandler.getImage("backgrounds/space");
	private Player player;
	private boolean victory = false, defeat = false;
	private long prevPlanetSeed;
	private int shipBehaviourRange = 6;

	private int standardCommandShipCollisionDamage = 3;
	private int standardCarrierShipCollisionDamage = 5;
	private int standardFighterShipCollisionDamage = 7;

	private int standardCommandShipLongRangeLaserCount = 2;
	private int standardCarrierShipLongRangeLaserCount = 2;
	private int standardFighterShipLongRangeLaserCount = 0;

	private int standardCommandShipLongRangeLaserInterval = 27000;
	private int standardCarrierShipLongRangeLaserInterval = 24000;
	private int standardFighterShipLongRangeLaserInterval = 25000;

	private int standardCommandShipMagneticProjectileCount = 7;
	private int standardCommandShipMagneticShotInterval = 20000;
	private int standardCommandShipParalysationLenght = 5000;

	private int standardCarrierShipMagneticProjectileCount = 6;
	private int standardCarrierShipMagneticShotInterval = 9000;
	private int standardCarrierShipParalysationLenght = 4000;

	private int standardFighterShipMagneticProjectileCount = 5;
	private int standardFighterShipMagneticShotInterval = 15000;
	private int standardFighterShipParalysationLenght = 5000;

	private int standardPlayerSHipParalysationLenght = 5000;

	private int laserDamage = 10;
	private int longRangeLaserDamage = 25;

	private Point2D.Double previousPPosition;
	
	private Rectangle battleZone;
	
	/**
	 * creates a new Battle
	 * 
	 * @param numOfFighters1
	 *            the number of fighters in team 1
	 * @param numOfCarriers1
	 *            the number of carriers in team 1
	 * @param numOfCommand1
	 *            the number of command ships in team 1
	 * @param numOfFighters2
	 *            the number of fighters in team 2
	 * @param numOfCarriers2
	 *            the number of carriers in team 2
	 * @param numOfCommand2
	 *            the number of command ships in team 2
	 * @param width
	 *            the width of the battle
	 * @param height
	 *            the height of the battle
	 * @param seed
	 *            the seed for this battle
	 * @param player
	 *            the player
	 */
	public Battle(int numOfFighters1, int numOfCarriers1, int numOfCommand1, int numOfFighters2, int numOfCarriers2,
			int numOfCommand2, int width, int height, long seed, Player player) {
		this.width = width;
		this.height = height;

		this.player = player;

		prevPlanetSeed = seed;
		battleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		xDif = width / 4;
		yDif = height / 4;

		random = new Random(seed);
		
		if (player !=null)
			player.setParalysationLength(standardPlayerSHipParalysationLenght);

		lasersTeam1 = new ArrayList<Line2D.Double>();
		lasersTeam2 = new ArrayList<Line2D.Double>();

		magneticLasersTeam1 = new ArrayList<Line2D.Double>();
		magneticLasersTeam2 = new ArrayList<Line2D.Double>();

		longRangeLasersTeam1 = new ArrayList<>();
		longRangeLasersTeam2 = new ArrayList<>();

		team1 = new ArrayList<Entity>();
		team2 = new ArrayList<Entity>();
		
		battleZone = new Rectangle(0, 0, width, height);
		
		for (int i = 0; i < numOfCommand1; i++) {

			team1.add(new Command("ship_types/large_command_blue", random.nextInt(width), random.nextInt(height),
					Entity.getRandomType(random, shipBehaviourRange - 1), PlayerStats.SHIPTYPE3ID,
					standardCarrierShipLongRangeLaserCount, standardCommandShipLongRangeLaserInterval,
					standardCommandShipCollisionDamage, standardCommandShipMagneticProjectileCount,
					standardCommandShipMagneticShotInterval, standardCommandShipParalysationLenght));
		}

		for (int i = 0; i < numOfCarriers1; i++) {

			team1.add(new Carrier("ship_types/medium_carrier_blue", random.nextInt(width), random.nextInt(height),
					Entity.getRandomType(random, shipBehaviourRange - 1), PlayerStats.SHIPTYPE2ID,
					standardCarrierShipLongRangeLaserCount, standardCarrierShipLongRangeLaserInterval,
					standardCarrierShipCollisionDamage, standardCarrierShipMagneticProjectileCount,
					standardCarrierShipMagneticShotInterval, standardCarrierShipParalysationLenght));
		}

		for (int i = 0; i < numOfFighters1; i++) {

			Entity e;
			if (numOfCarriers1 + numOfCommand1 > 0) {
				e = team1.get(random.nextInt(numOfCarriers1 + numOfCommand1));
			} else {
				e = player;
			}
			team1.add(new Fighter("ship_types/small_fighter_blue", (int) (random.nextInt(100) - 50),
					(int) (random.nextInt(100) - 50), Entity.getRandomType(random, shipBehaviourRange), e,
					PlayerStats.SHIPTYPE1ID, standardFighterShipLongRangeLaserCount,
					standardFighterShipLongRangeLaserInterval, standardFighterShipCollisionDamage,
					standardFighterShipMagneticProjectileCount, standardFighterShipMagneticShotInterval,
					standardFighterShipParalysationLenght));
		}

		for (int i = 0; i < numOfCommand2; i++) {

			team2.add(new Command("ship_types/large_command_red", random.nextInt(width), random.nextInt(height),
					Entity.getRandomType(random, shipBehaviourRange - 1), PlayerStats.SHIPTYPE3ID,
					standardCommandShipLongRangeLaserCount, standardCommandShipLongRangeLaserInterval,
					standardCommandShipCollisionDamage, standardCommandShipMagneticProjectileCount,
					standardCommandShipMagneticShotInterval, standardCommandShipParalysationLenght));
			System.out.println(team2.get(team2.size()-1).getShipType());
			System.out.println(team2.get(team2.size()-1).getType());
		}

		for (int i = 0; i < numOfCarriers2; i++) {

			team2.add(new Carrier("ship_types/medium_carrier_red", random.nextInt(width), random.nextInt(height),
					Entity.getRandomType(random, shipBehaviourRange - 1), PlayerStats.SHIPTYPE2ID,
					standardCarrierShipLongRangeLaserCount, standardCarrierShipLongRangeLaserInterval,
					standardCarrierShipCollisionDamage, standardCarrierShipMagneticProjectileCount,
					standardCarrierShipMagneticShotInterval, standardCarrierShipParalysationLenght));
		}

		for (int i = 0; i < numOfFighters2; i++) {

			Entity e = team2.get(random.nextInt(numOfCarriers2 + numOfCommand2));
			team2.add(new Fighter("ship_types/small_fighter_red", (int) (random.nextInt(100) - 50),
					(int) (random.nextInt(100) - 50), Entity.getRandomType(random, shipBehaviourRange), e,
					PlayerStats.SHIPTYPE1ID, standardFighterShipLongRangeLaserCount,
					standardFighterShipLongRangeLaserInterval, standardFighterShipCollisionDamage,
					standardFighterShipMagneticProjectileCount, standardFighterShipMagneticShotInterval,
					standardFighterShipParalysationLenght));
		}
	}
	
	
	/**
	 * update the locations of the given lasers 
	 * if the lasers belong to the second team then check if they hit the player
	 * @param lasers
	 * @param team
	 * @param pPosition
	 */
	
	public void lasersUpdate(ArrayList<Line2D.Double> lasers, int team, Point2D.Double pPosition, boolean magnetic){
		
		for (int i = 0; i < lasers.size(); i++) {
			Line2D.Double l = lasers.get(i);
			Point2D.Double p = new Point2D.Double(l.getX2() - l.getX1(), l.getY2() - l.getY1());
			l.setLine(l.getP2(), new Point2D.Double(p.x + l.getP2().getX(), p.y + l.getP2().getY()));
			
			if (team == 2 && pPosition != null){
				//check if the lasers hit the player
				if (new Rectangle((int) (pPosition.getX() - player.getWidth() / 2),
						(int) (pPosition.getY() - player.getHeight() / 2), player.getWidth(), player.getHeight())
								.intersectsLine(lasers.get(i))) {
					
					if (!magnetic){
						player.damage(laserDamage);
					} else {
						player.setParalysed(true);
					}
					lasers.remove(i);
					i--;
				}
			}
			
			if (l != null && !(battleZone.contains(l.getP2()))) {
				lasers.remove(i);
				i--;
			}
		}
		
	}
	
	/**
	 * update the locations of the given long range lasers 
	 * if they are from the second team check if they hit the player 
	 * @param longRangeLasers
	 * @param team
	 * @param pPosition
	 */
	
	public void longRangeLasersUpdate(ArrayList<LongRangeLaser> longRangeLasers, int team, Point2D.Double pPosition) {
		
		for (int i = 0; i < longRangeLasersTeam2.size(); i++) {
			if (longRangeLasersTeam2.get(i).getLifeTime() < System.currentTimeMillis()
					- longRangeLasersTeam2.get(i).getShotTime()) {
				longRangeLasersTeam2.remove(i);
			}
			// if team = 2 then check if they hit the player
			if (team == 2 && player != null && pPosition != null ) {
				if (new Rectangle((int) (pPosition.getX() - player.getWidth() / 2),
						(int) (pPosition.getY() - player.getHeight() / 2), player.getWidth(), player.getHeight())
								.intersectsLine(longRangeLasers.get(i).getProjectile())) {
					player.damage(longRangeLaserDamage);
					longRangeLasers.remove(i);
					i--;
				}
			}
		}
	}
	
	/**
	 * Check if the given entity was hit by any enemy laser and apply corresponding effect
	 * @param e			= the given entity
	 * @param lasers	= the enemy lasers 
	 * @param magnetic	= true if checking for magnetic lasers false otherwise
	 */
	
	public void checkLasersHitsOnEntity(Entity e, ArrayList<Line2D.Double> lasers, boolean magnetic){
		
		for (int j = 0; j < lasers.size(); j++) {
			if (new Rectangle(
					(int) e.getCentre().getX() - e.getWidth() / 2,
					(int) e.getCentre().getY() - e.getHeight() / 2, 
					e.getWidth(), 
					e.getHeight()
				).intersectsLine(lasers.get(j))) {
				lasers.remove(j);
				j--;
				
				if (!magnetic){
					e.damage(laserDamage);
				} else {
					e.setParalysed(true);
				}
			}
		}
	}
	
	/**
	 * Check if the given entity was hit by any enemy long range laser and apply corresponding effect
	 * @param e
	 * @param longRangeLasers
	 */
	public void checkLongRangeLasersHits(Entity e, ArrayList<LongRangeLaser> longRangeLasers){
		
		for (int j = 0; j < longRangeLasers.size(); j++) {
			if (new Rectangle((int) e.getCentre().getX() - e.getWidth() / 2,
					(int) e.getCentre().getY() - e.getHeight() / 2, e.getWidth(), e.getHeight())
							.intersectsLine(longRangeLasers.get(j).getProjectile())) {
				longRangeLasers.remove(j);
				j--;
				e.damage(this.longRangeLaserDamage);
			}
		}
		
	}
	
	/**
	 * Update the given entity
	 * @param e					= the given entity
	 * @param team				= the team of the given entity
	 * @param time				= the time of the update
	 * @param lasers			= the lasers from the team of the given entity
	 * @param magneticLasers	= the magnetic lasers from the team of the given entity
	 * @param longRangeLasers	= the long range lasers from the team of the given entity
	 */
	
	public void updateEntity(Entity e,ArrayList<Entity> team,float time, ArrayList<Line2D.Double> lasers,
							 ArrayList<Line2D.Double> magneticLasers, ArrayList<LongRangeLaser> longRangeLasers){
		//System.out.println(e.getShipType());
		if (e != null && !e.isParalysed() && e.update(time, this.width, this.height, team, this)) {
			// shoot towards the current location of the target

			Point2D.Double p = MathHandler.getPoint2D(e.getCentre(), e.getTarget(), 10.0, Math.PI / 64);

			// shoot a heat projectile
			if (e.canShootLongRangeLaser()) {
				p = new Point2D.Double(e.getTarget().x, e.getTarget().y);
				longRangeLasers.add(new LongRangeLaser(new Line2D.Double(e.getCentre().x, e.getCentre().y,
						p.x + (p.x - e.getCentre().x), p.y + (p.y - e.getCentre().y)), e.getTargetEntity()));
				e.shootLongRangeLaser();
				SoundHandler.playSound(SoundHandler.rail_gun);
			} else if (e.canShootMagneticLaser()) {
				magneticLasers.add(new Line2D.Double(e.getCentre().x, e.getCentre().y, e.getCentre().x + p.x,
						e.getCentre().y + p.y));
				e.shootMagneticLaser();
			} else {
				lasers.add(new Line2D.Double(e.getCentre().x, e.getCentre().y, e.getCentre().x + p.x,
						e.getCentre().y + p.y));
				SoundHandler.playSound(SoundHandler.laser);
			}
		}
	}
	
	/**
	 * updates the positions of all the entities and checks for laser hits
	 * 
	 * @param time
	 *            the time between frames
	 */
	public void update(float time) {
		
		Point2D.Double pPosition;
		
		if (player != null){
			pPosition = new Point2D.Double(InputHandler.midPoint.x + xDif, InputHandler.midPoint.y + yDif);
		} else {
			pPosition = null;
		}
		// get the players actions
		if (player != null && !player.isParalysed()) {
			if (Main.input.isMouseDown(MouseEvent.BUTTON1)
					&& System.currentTimeMillis() - player.getPrevShoot() > player.getShotInterval()
					&& player.getCurrentHealth() > 0) {
				player.setPrevShoot(System.currentTimeMillis());
				Point2D.Double p = MathHandler.getPoint2D(pPosition,
						new Point2D.Double(InputHandler.getMousePositionOnScreen().x + xDif,
								InputHandler.getMousePositionOnScreen().y + yDif),
						10.0, Math.PI / 64);
				lasersTeam1.add(new Line2D.Double(pPosition.x, pPosition.y, pPosition.x + p.x, pPosition.y + p.y));
				SoundHandler.playSound(SoundHandler.laser);
			}
			if (Main.input.isMouseDown(MouseEvent.BUTTON2)
					&& System.currentTimeMillis() - player.getPrevShoot() > player.getShotInterval()
					&& player.getCurrentHealth() > 0) {

				player.setPrevShoot(System.currentTimeMillis());
				Point2D.Double p = new Point2D.Double(InputHandler.getMousePositionOnScreen().x,
						InputHandler.getMousePositionOnScreen().y);

				Entity playerTarget = this.findClosestEnemyShipToGivenPoint(p, this.team2);
				longRangeLasersTeam1
						.add(new LongRangeLaser(new Line2D.Double(pPosition, playerTarget.getCentre()), playerTarget));

			}
			if (Main.input.isMouseDown(MouseEvent.BUTTON3)
					&& System.currentTimeMillis() - player.getPrevShoot() > player.getShotInterval()
					&& player.getCurrentHealth() > 0) {

				player.setPrevShoot(System.currentTimeMillis());
				Point2D.Double p = MathHandler.getPoint2D(pPosition,
						new Point2D.Double(InputHandler.getMousePositionOnScreen().x + xDif,
								InputHandler.getMousePositionOnScreen().y + yDif),
						10.0, Math.PI / 64);
				magneticLasersTeam1
						.add(new Line2D.Double(pPosition.x, pPosition.y, pPosition.x + p.x, pPosition.y + p.y));
			}
		}
		
		//update the teams according to their actions
		
		// team1

		// move lasers and check if any is outside the battle zone
		lasersUpdate(lasersTeam1, 1,pPosition, false);
		

		// move magnetic lasers and check if any is outside the battle zone
		lasersUpdate(magneticLasersTeam1, 1,pPosition, true);
		
		// move heat projectiles and check if any is outside the battle zone
		
		longRangeLasersUpdate(longRangeLasersTeam1, 1, pPosition);
		
		
		for (int i = 0; i < team1.size(); i++) {
			Entity e = team1.get(i);

			if (e.getType() == Entity.TYPE.KINGCRUSHER) {
				while (e.getType() == Entity.TYPE.KINGCRUSHER) {
					e.setType(Entity.getRandomType(random, shipBehaviourRange));
					e.findTarget(team2, width);
				}
			} else {
				e.findTarget(team2, width);
			}
			
			
			
			System.out.println(e.getTargetEntity());
			//update entity
			updateEntity(e, team1, time, lasersTeam1, magneticLasersTeam1, longRangeLasersTeam1);
		
			// check hits

			// check if any of the enemy lasers have hit this entity
			checkLasersHitsOnEntity(e, lasersTeam2, false);
			
			// check if any magnetic lasers have hit this entity
			checkLasersHitsOnEntity(e, magneticLasersTeam2, true);
			
			// check if any of the enemy long range lasers hit this entity
			checkLongRangeLasersHits(e, longRangeLasersTeam2);
			
			
			if (e.getCurrentHealth() <= 0) {

				if (e.getType() == Entity.TYPE.SURVIVOR) {
					System.out.println(e.getShipType());
					//e.executeSURVIVORBehaviour(team1, this, "ship_types/small_fighter_blue");
				}
				team1.remove(i);
				SoundHandler.playSound(SoundHandler.explosion);
				Main.pStats.setResourceOnPlanet(e.getShipid(), prevPlanetSeed,
						Main.pStats.getResourceOnPlanet(e.getShipid(), prevPlanetSeed) - 1);
				i--;
			}
			e.previousCentre = e.getCentre();
		}
		
		// team2
		
		//update lasers location
		lasersUpdate(lasersTeam2, 2, pPosition, false);
		
		//update magnetic lasers
		lasersUpdate(magneticLasersTeam2, 2, pPosition, true);
		// update heat projectiles
		longRangeLasersUpdate(longRangeLasersTeam2, 2, pPosition);
		
		for (int i = 0; i < team2.size(); i++) {
			Entity e = team2.get(i);

			if (e.getType() == Entity.TYPE.KINGCRUSHER) {
				e.setTarget(pPosition);
				e.setTargetEntity(player);
			} else {
				e.findTarget(team1, width);

			}

			if (player != null && player.getCurrentHealth() > 0) {
				e.findTarget(pPosition, player, team1, width);
			} else {
				e.findTarget(team1, width);
			}
			
			//update entity
			updateEntity(e, team2, time, lasersTeam2, magneticLasersTeam2, longRangeLasersTeam2);

			// check hits

			// check if any entity was hit by an enemy laser
			checkLasersHitsOnEntity(e, lasersTeam1, false);

			// check if any entity was hit by an enemy magnetic laser
			checkLasersHitsOnEntity(e, lasersTeam1, true);
			

			// check if any of the enemy heat projectiles hit this entity
			checkLongRangeLasersHits(e, longRangeLasersTeam1);

			if (e.getCurrentHealth() <= 0) {
				
				System.out.println(e.getShipType());
				if(e.getShipType() ==  "Command"){
					System.out.println(e.getType() == Entity.TYPE.SURVIVOR);
					System.out.println(e.getType());
				}
				if (e.getType() == Entity.TYPE.SURVIVOR) {
					System.out.println(e.getShipType());
					e.executeSURVIVORBehaviour(team2, this, "ship_types/small_fighter_red");
				}

				team2.remove(i);
				SoundHandler.playSound(SoundHandler.explosion);
				i--;
			}

			e.previousCentre = e.getCentre();
		}
		// victory / defeat conditions
		if (team2.size() == 0) {
			victory = true;
		}
		if (team1.size() == 0 && ((player != null && player.getCurrentHealth() <= 0)|| player == null)) {
			defeat = true;
		}

		previousPPosition = pPosition;
		System.gc();
		//System.out.println("t1 = " + team1.size());
		//System.out.println("t2 = " + team2.size());
	}

	/**
	 * draws all the entities to the screen
	 * 
	 * @param g
	 *            the graphics context to use
	 */
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) battleImage.getGraphics();
		g2d.drawImage(background, 0, 0, width / 2, height / 2, null);
		g2d.drawImage(background, width / 2, height / 2, width / 2, height / 2, null);
		g2d.drawImage(background, 0, height / 2, width / 2, height / 2, null);
		g2d.drawImage(background, width / 2, 0, width / 2, height / 2, null);
		for (Entity e : team1) {
			e.draw(g2d, 2);
		}
		for (Entity e : team2) {
			e.draw(g2d, 2);
		}
		g2d.setColor(Color.blue);
		g2d.setStroke(new BasicStroke(4));
		for (Line2D.Double l : lasersTeam1) {
			g2d.drawLine((int) l.x1, (int) l.y1, (int) l.x2, (int) l.y2);
		}
		g2d.setColor(Color.pink);
		for (Line2D.Double l : magneticLasersTeam1) {
			g2d.drawLine((int) l.x1, (int) l.y1, (int) l.x2, (int) l.y2);
		}

		g2d.setColor(Color.orange);
		for (LongRangeLaser hp : longRangeLasersTeam1) {
			g2d.drawLine((int) hp.getProjectile().x1, (int) hp.getProjectile().y1, (int) hp.getProjectile().x2,
					(int) hp.getProjectile().y2);
		}

		g2d.setColor(Color.red);
		for (Line2D.Double l : lasersTeam2) {
			g2d.drawLine((int) l.x1, (int) l.y1, (int) l.x2, (int) l.y2);
		}
		g2d.setColor(Color.green);
		for (Line2D.Double l : magneticLasersTeam2) {
			g2d.drawLine((int) l.x1, (int) l.y1, (int) l.x2, (int) l.y2);
		}

		g2d.setColor(Color.yellow);
		for (LongRangeLaser hp : longRangeLasersTeam2) {
			System.out.println("draw from : " + hp.getProjectile().x1 + " " + hp.getProjectile().y1 + " to "
					+ hp.getProjectile().x2 + " " + hp.getProjectile().y2);
			g2d.drawLine((int) hp.getProjectile().x1, (int) hp.getProjectile().y1, (int) hp.getProjectile().x2,
					(int) hp.getProjectile().y2);
		}

		if (victory) {
			g2d.setColor(Color.blue);
			g2d.setFont(new Font("Verdana", Font.BOLD, InputHandler.screenSize.width / 30));
			g2d.drawString("VICTORY", (int) 0, (int) 0);
		}
		if (defeat) {
			g2d.setColor(Color.red);
			g2d.setFont(new Font("Verdana", Font.BOLD, InputHandler.screenSize.width / 30));
			g2d.drawString("DEFEAT", (int) 0, (int) 0);
		}
		g.drawImage(battleImage, 0, 0, InputHandler.screenSize.width, InputHandler.screenSize.height, (int) xDif,
				(int) yDif, (int) (width / 2 + xDif), (int) (height / 2 + yDif), null);
	}

	/**
	 * moves the whole battle (used for player movement
	 * 
	 * @param xAmount
	 *            the xAmount
	 * @param yAmount
	 *            the yAmount
	 */
	public void moveBattle(float xAmount, float yAmount) {
		float divider = (float) Math.sqrt(Math.pow(xAmount, 2) + Math.pow(yAmount, 2));
		if (player != null) {
			xDif += (xAmount * player.getSpeed()) / divider;
			yDif += (yAmount * player.getSpeed()) / divider;
		}
	}

	/**
	 * checks if this battle results in a victory
	 * 
	 * @return
	 */
	public boolean getVictory() {
		return victory;
	}

	/**
	 * checks if this battle resulted in defeat
	 * 
	 * @return
	 */
	public boolean getDefeat() {
		return defeat;
	}

	public Entity findClosestEnemyShipToGivenPoint(Point2D.Double p, ArrayList<Entity> enemyTeam) {

		Entity closest = enemyTeam.get(0);

		for (Entity e : enemyTeam.subList(1, enemyTeam.size())) {
			if (MathHandler.getDistance(p, closest.getCentre()) > MathHandler.getDistance(p, e.getCentre())) {
				closest = e;
			}
		}
		return closest;
	}

	/**
	 * @return the standardFighterShipMagneticProjectileCount;
	 */
	public int getStandardFighterShipMagneticProjectileCount() {
		return this.standardFighterShipMagneticProjectileCount;
	}

	/**
	 * @return the standardFighterShipMagneticShotInterval
	 */
	public int getStandardFighterShipMagneticShotInterval() {
		return this.standardFighterShipMagneticShotInterval;
	}

	/**
	 * @return the standardFighterShipParalysationLenght
	 */
	public int getStandardFighterShipParalysationLenght() {
		return this.standardFighterShipParalysationLenght;
	}

	/**
	 * @return the standardFighterShiplongRangeLaserCount
	 */
	public int getStandardFighterShiplongRangeLaserCount() {
		return this.standardFighterShipLongRangeLaserCount;
	}

	/**
	 * @return the standardFighterShipHeatShotInterval
	 */
	public int getStandardFighterShipHeatShotInterval() {
		return this.standardFighterShipLongRangeLaserInterval;
	}

	/**
	 * @return the standardFighterShipCollisionDamage
	 */
	public int getStandardFighterShipCollisionDamage() {
		return this.standardFighterShipCollisionDamage;
	}


	/**
	 * @return the standardCommandShipLongRangeLaserInterval
	 */
	public int getStandardCommandShipLongRangeLaserInterval(){
		return this.standardCommandShipLongRangeLaserInterval;
	}

	/**
	 *
	 * @return the standardCommandShipMagneticProjectileCount
	 */
	public int getStandardCommandShipMagneticProjectileCount(){
		return this.standardCommandShipMagneticProjectileCount;
	}

	/**
	 * @return the standardCommandShipLongRangeLaserCount
	 */
	public int getStandardCarrierShipLongRangeLaserCount(){
		return this.standardCommandShipLongRangeLaserCount;
	}

	/**
	 *
	 * @return
	 */
	public int getStandardCarrierShipCollisionDamage(){
		return this.standardCommandShipCollisionDamage;
	}

	//-------------
	/**
	 * @return the standardCommandShipLongRangeLaserInterval
	 */
	public int getStandardCarrierShipLongRangeLaserInterval(){
		return this.standardCarrierShipLongRangeLaserInterval;
	}

	/**
	 *
	 * @return the standardCommandShipMagneticProjectileCount
	 */
	public int getStandardCarrierShipMagneticProjectileCount(){
		return this.standardCarrierShipMagneticProjectileCount;
	}

	/**
	 * @return the standardCommandShipLongRangeLaserCount
	 */
	public int getStandardCommandShipLongRangeLaserCount(){
		return this.standardCommandShipLongRangeLaserCount;
	}

	//-------------
	/**
	 * @return the shipBehavoiurRange
	 */
	public int getShipBehaviourRange() {
		return this.shipBehaviourRange;
	}
	
	/**
	 * Get the lasers belonging to the requested team
	 * @param team	= the team id
	 * @return
	 */
	
	public ArrayList<Line2D.Double> getLasersOfTeam(int team){
		if (team == 1){
			return lasersTeam1;
		} else {
			return lasersTeam2;
		}
	}
	
	/**
	 * Get the magnetic lasers belonging to the requested team
	 * @param team
	 * @return
	 */
	
	public ArrayList<Line2D.Double> getMagneticLasersOfTeam(int team){
		if (team == 1){
			return magneticLasersTeam1;
		} else {
			return magneticLasersTeam2;
		}
	}
	
	/**
	 * Get the long range lasers belonging to the requested team
	 * @param team
	 * @return
	 */
	
	public ArrayList<LongRangeLaser> getLongRangeLaserOfTeam(int team){
		if (team == 1){
			return longRangeLasersTeam1;
		} else {
			return longRangeLasersTeam2;
		}
	}
	
	/**
	 * Return the requested team's entities
	 * @param team
	 * @return
	 */
	public ArrayList<Entity> getTeam(int team){
		if (team == 1){
			return team1;
		} else {
			return team2;
		}
	}
	
	/**
	 * @return the amount damage delivered by a laser
	 */
	
	public int getLaserDamage(){
		return this.laserDamage;
	}
	
	/**
	 * @return the amount damage delivered by a laser
	 */
	
	public int getLongRangeLaserDamage(){
		return this.longRangeLaserDamage;
	}

	/**
	 * @return the amount by which to move the image of the battle on x axis
	 */
	public float getxDif(){
		return this.xDif;
	}

	/**
	 * @return the amount by which to move the image of the battle on y axis
	 */
	public float getyDif(){
		return this.yDif;
	}

	/**
	 * @return the width of the battle
	 */

	public int getWidth(){
		return this.width;
	}

	/**
	 * @return the height of the battle
	 */

	public int getHeight(){
		return this.height;
	}
}
