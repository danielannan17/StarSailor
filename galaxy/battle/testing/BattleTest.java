package galaxy.battle.testing;

import galaxy.battle.Battle;
import galaxy.battle.Entity;
import galaxy.battle.LongRangeLaser;
import handlers.InputHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class BattleTest {
	
	private Battle battle;

	private Random rand;

	private float xDif, yDif;
	private int width = InputHandler.screenSize.width, height = InputHandler.screenSize.height;

	private int numOfFighters1;
	private int numOfCarriers1;
	private int numOfCommand1;

	private int numOfFighters2;
	private int numOfCarriers2;
	private int numOfCommand2;

	@Before
	public void setUp() throws Exception {
		
		this.rand = new Random();

		this.numOfFighters1 = rand.nextInt(30);
		if (this.numOfFighters1 == 0){
			this.numOfFighters1 = 1;
		}

		this.numOfCarriers1 = rand.nextInt(03);
		if (this.numOfCarriers1 == 0){
			this.numOfCarriers1 = 1;
		}

		this.numOfCommand1 = rand.nextInt(30);
		if (this.numOfCommand1 == 0){
			this.numOfCommand1 = 1;
		}

		this.numOfFighters2 = rand.nextInt(30);
		if (this.numOfFighters2 == 0){
			this.numOfFighters2 = 1;
		}

		this.numOfCarriers2 = rand.nextInt(30);
		if (this.numOfCarriers2 == 0){
			this.numOfCarriers2 = 1;
		}

		this.numOfCommand2 = rand.nextInt(30);
		if (this.numOfCommand2 == 0){
			this.numOfCommand2 = 1;
		}

	
		this.battle = new Battle(this.numOfFighters1, this.numOfCarriers1, this.numOfCommand1, 
				this.numOfFighters2, this.numOfCarriers2, this.numOfCommand2, this.width, this.height, 0, null);
		
	}
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBattle() {
		
		
		assertTrue(!battle.getTeam(1).isEmpty());
		assertTrue(!battle.getTeam(2).isEmpty());
		assertTrue(battle.getShipBehaviourRange() == 6);
		System.out.println("Passed!");
		
	}
	
	public boolean isAngle0(double x1,double y1, double x2, double y2, double x3, double y3){
		return (x1 * y2 + x2 * y3 + x3 * y1 - x3 * y2 - x2 * y1 - x1 * y3) == 0;
	}
	
	@Test
	public void testLasersUpdate() {
		
		battle.update(100);
		
		// test if lasers are updated for the team that's shooting them
		
		ArrayList<Line2D.Double> lasers = new ArrayList<>();	
		ArrayList<Line2D.Double> lasers1 = new ArrayList<>();
		
		// copy the lasers and their positions
		for (Line2D.Double l: battle.getLasersOfTeam(1)){
			lasers.add(
					new Line2D.Double(l.x1, l.y1, l.x2, l.y2)
			);
		}		
		
		// update the original lasers
		battle.lasersUpdate(lasers1, 1, null, false);
		
		boolean areLasersListsTheSame = true;
		// assuming the lasers were just created they couldn't have left the map imediately
		if (lasers.size() != lasers1.size()){
			areLasersListsTheSame = false;
		} else{
			for (int i = 0; i < lasers.size() && areLasersListsTheSame == true; i++) {
				
				if (lasers.get(i).intersectsLine(lasers1.get(i)) && !this.isAngle0(lasers.get(i).x1, lasers.get(i).y1, lasers.get(i).x2, lasers.get(i).y2, lasers1.get(i).x2, lasers1.get(i).y2)){
					areLasersListsTheSame = false;
				}
				
			}
		}
		
		assertTrue(areLasersListsTheSame);
		
	}

	@Test
	public void testLongRangeLasersUpdate() throws InterruptedException {
		
		battle.update(100);
		
		// test if lasers are updated for the team that's shooting them
		
		ArrayList<LongRangeLaser> lasers = new ArrayList<>();	
		ArrayList<LongRangeLaser> lasers1 = new ArrayList<>();
		
		// copy the lasers and their positions
		for (LongRangeLaser l: battle.getLongRangeLaserOfTeam(1)){
			lasers.add(
					new LongRangeLaser(l.getProjectile(),l.getTarget())
			);
			lasers.get(lasers.size()-1).setShotTime(l.getShotTime());
		}	
		
		//lifespan of lasers should be less than 2 seconds
		TimeUnit.SECONDS.sleep(2);
		
		battle.longRangeLasersUpdate(lasers, 1, null);
		
		assertTrue(lasers.isEmpty());
	}

	@Test
	public void testCheckLasersHitsOnEntity() {
		 
		 Entity e1 = battle.getTeam(1).get(0);
		 Entity e2 = battle.getTeam(2).get(0);
		 
		 battle.getLasersOfTeam(1).add(new Line2D.Double(e1.getCentre(),e2.getCentre()));
		 battle.getMagneticLasersOfTeam(1).add(new Line2D.Double(e1.getCentre(),e2.getCentre()));
		 
		 float life = e2.getCurrentHealth();
		 
		 battle.checkLasersHitsOnEntity(e2,  battle.getLasersOfTeam(1), false);
		 battle.checkLasersHitsOnEntity(e2,  battle.getMagneticLasersOfTeam(1), true);
		 
		 
		 assertTrue(life - e2.getCurrentHealth() == battle.getLaserDamage() && e2.isParalysed());
	}

	@Test
	public void testCheckLongRangeLasersHits() {
		
		Entity e1 = battle.getTeam(1).get(0);
		Entity e2 = battle.getTeam(2).get(0);
		
		battle.getLongRangeLaserOfTeam(1).add(new LongRangeLaser(new Line2D.Double(e1.getCentre(),e2.getCentre()), e2));
		
		float life = e2.getCurrentHealth();
		
		battle.checkLongRangeLasersHits(e2, battle.getLongRangeLaserOfTeam(1));
		
		assertTrue(life - e2.getCurrentHealth() == battle.getLongRangeLaserDamage());
	}

	@Test
	public void testUpdateEntity(){

		battle.update(100);

		Entity e = this.battle.getTeam(1).get(0);
		int i = 0;
		//while((e = this.battle.getTeam(1).get(0)).getTargetEntity() != null && i < this.battle.getTeam(1).size());

		e.setTargetEntity(this.battle.getTeam(2).get(0));
		e.setTarget(this.battle.getTeam(2).get(0).getCentre());

		int lasersCount1 = this.battle.getLasersOfTeam(1).size();
		int lasersCount2 = this.battle.getLasersOfTeam(2).size();

		int magneticLasersCount1 = this.battle.getMagneticLasersOfTeam(1).size();
		int magneticLasersCount2 = this.battle.getMagneticLasersOfTeam(2).size();

		int longRangeLasersCount1 = this.battle.getLongRangeLaserOfTeam(1).size();
		int longRangeLasersCount2 = this.battle.getLongRangeLaserOfTeam(2).size();

		e.setPreviousTime(0);
		this.battle.updateEntity(e,battle.getTeam(1),System.currentTimeMillis(),battle.getLasersOfTeam(1),battle.getMagneticLasersOfTeam(2),battle.getLongRangeLaserOfTeam(2));

		assertTrue(
				(	lasersCount1 != this.battle.getLasersOfTeam(1).size()
					|| lasersCount2 != this.battle.getLasersOfTeam(2).size()
					|| magneticLasersCount1 != this.battle.getMagneticLasersOfTeam(1).size()
					|| magneticLasersCount2 != this.battle.getMagneticLasersOfTeam(2).size()
					|| longRangeLasersCount1 != this.battle.getLongRangeLaserOfTeam(1).size()
					|| longRangeLasersCount2 != this.battle.getLongRangeLaserOfTeam(2).size()
				)
		);

	}

	@Test
	public void testUpdate() {

		// test the update for the entity by getting its previous location
		// and comparing its new location and checking if it now has a target
		Entity e;
		int i = 0;
		while((e = this.battle.getTeam(1).get(i++)).getTargetEntity() != null );

		Point2D.Double location = e.getCentre();

		int lasersCount1 = this.battle.getLasersOfTeam(1).size();
		int lasersCount2 = this.battle.getLasersOfTeam(2).size();

		int magneticLasersCount1 = this.battle.getMagneticLasersOfTeam(1).size();
		int magneticLasersCount2 = this.battle.getMagneticLasersOfTeam(2).size();

		int longRangeLasersCount1 = this.battle.getLongRangeLaserOfTeam(1).size();
		int longRangeLasersCount2 = this.battle.getLongRangeLaserOfTeam(2).size();

		battle.update(100);

		//assertTrue(location != e.getCentre());
		assertTrue(this.battle.getTeam(2).contains(e.getTargetEntity()));
		assertTrue ( (lasersCount1 != this.battle.getLasersOfTeam(1).size()
					|| lasersCount2 != this.battle.getLasersOfTeam(2).size()
					|| magneticLasersCount1 != this.battle.getMagneticLasersOfTeam(1).size()
					|| magneticLasersCount2 != this.battle.getMagneticLasersOfTeam(2).size()
					|| longRangeLasersCount1 != this.battle.getLongRangeLaserOfTeam(1).size()
					|| longRangeLasersCount2 != this.battle.getLongRangeLaserOfTeam(2).size()
					)
		);
	}

	@Test
	public void testMoveBattle() {

		float initialX = this.battle.getxDif();
		float initialY = this.battle.getyDif();

		float divider = (float) Math.sqrt(Math.pow(0, 2) + Math.pow(0, 2));

		this.battle.moveBattle((float) 0,(float) 0);

		xDif = this.battle.getxDif() ;
		yDif = this.battle.getyDif() ;

		assertTrue(this.battle.getxDif() == xDif && this.battle.getyDif() == yDif);
	}

	@Test
	public void testGetVictory() {

		this.battle.getTeam(1).removeAll(this.battle.getTeam(1));
		this.battle.getTeam(2).removeAll(this.battle.getTeam(2));

		this.battle.getLasersOfTeam(1).removeAll(this.battle.getLasersOfTeam(1));
		this.battle.getLasersOfTeam(2).removeAll(this.battle.getLasersOfTeam(2));

		this.battle.getMagneticLasersOfTeam(1).removeAll(this.battle.getMagneticLasersOfTeam(1));
		this.battle.getMagneticLasersOfTeam(2).removeAll(this.battle.getMagneticLasersOfTeam(2));

		this.battle.getLongRangeLaserOfTeam(1).removeAll(this.battle.getLongRangeLaserOfTeam(1));
		this.battle.getLongRangeLaserOfTeam(2).removeAll(this.battle.getLongRangeLaserOfTeam(2));
		System.out.println(this.battle.getTeam(2));

		this.battle.update(System.currentTimeMillis());
		assertTrue(this.battle.getVictory());
	}

	@Test
	public void testGetDefeat() {
		this.battle.getTeam(1).removeAll(this.battle.getTeam(1));
		this.battle.getTeam(2).removeAll(this.battle.getTeam(2));

		this.battle.getLasersOfTeam(1).removeAll(this.battle.getLasersOfTeam(1));
		this.battle.getLasersOfTeam(2).removeAll(this.battle.getLasersOfTeam(2));

		this.battle.getMagneticLasersOfTeam(1).removeAll(this.battle.getMagneticLasersOfTeam(1));
		this.battle.getMagneticLasersOfTeam(2).removeAll(this.battle.getMagneticLasersOfTeam(2));

		this.battle.getLongRangeLaserOfTeam(1).removeAll(this.battle.getLongRangeLaserOfTeam(1));
		this.battle.getLongRangeLaserOfTeam(2).removeAll(this.battle.getLongRangeLaserOfTeam(2));
		System.out.println(this.battle.getTeam(2));

		this.battle.update(System.currentTimeMillis());
		assertTrue(this.battle.getDefeat());
	}

	@Test
	public void testFindClosestEnemyShipToGivenPoint() {

		this.battle.getTeam(1).get(0).setX(1);
		this.battle.getTeam(1).get(0).setY(1);

		Entity e = this.battle.findClosestEnemyShipToGivenPoint(new Point2D.Double(0,0),this.battle.getTeam(1));

		assertTrue(e.getX() == 1 && e.getY() == 1);
	}

}
