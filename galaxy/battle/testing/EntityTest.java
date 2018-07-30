package galaxy.battle.testing;

import galaxy.battle.Battle;
import galaxy.battle.Entity;
import handlers.InputHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * JUnit testing for the Entity Abstract Class
 * 
 */

public class EntityTest {

	private Battle battle;
	private Entity entity;


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

		entity = this.battle.getTeam(1).get(0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEntity() {
		assertTrue(entity != null);
	}

	@Test
	public void testUpdate() {
		assertTrue(entity.update(System.currentTimeMillis(),battle.getWidth(),battle.getHeight(),battle.getTeam(1),battle));
	}

	@Test
	public void testDraw() {
		fail("Not yet implemented");
	}

	@Test
	public void testIncrementFrame() {
		fail("Not yet implemented");
	}

	@Test
	public void testIncrementPFrame() {
		fail("Not yet implemented");
	}

	@Test
	public void testMoveEntity() {
		Point2D.Double centre = entity.getCentre();

		System.out.println(centre.y);
		entity.moveEntity(10,10);
		System.out.println(entity.getY() - centre.y);
		assertTrue(centre.x  == entity.getX() && centre.y == entity.getY());
	}

	@Test
	public void testGetRandomType() {
		boolean check = false;
		for (Entity.TYPE t: Entity.TYPE.values()){
			if (t.equals(entity.getType()))
				check = true;
		}
		assertTrue(check);
	}

	@Test
	public void testDamage() {
		entity.damage(10);
		assertTrue(entity.getTotalHealth() - entity.getCurrentHealth() == 10);
	}

	@Test
	public void testFindTargetDoublePlayerArrayListOfEntityInt() {

		Entity enemy = this.battle.getTeam(2).get(0);
		enemy.setY(0);
		enemy.setX(0);

		this.entity.setX(0);
		this.entity.setY(0);

		entity.findTarget(null,null,this.battle.getTeam(2),this.battle.getWidth());

		assertTrue(this.entity.getTargetEntity().equals(enemy));

	}

	@Test
	public void testFindTargetArrayListOfEntityInt() {

		Entity enemy = this.battle.getTeam(2).get(0);
		enemy.setY(0);
		enemy.setX(0);

		this.entity.setX(0);
		this.entity.setY(0);

		entity.findTarget(null,null,this.battle.getTeam(2),this.battle.getWidth());

		assertTrue(this.entity.getTargetEntity().equals(enemy));
	}

	@Test
	public void testTargetPlayer() {

		entity.targetPlayer(null);
		assertTrue(entity.getTargetEntity() == null);
	}

	@Test
	public void testFindClosestLaser() {

		this.entity.setY(0);
		this.entity.setX(0);
		this.battle.getLasersOfTeam(2).add(new Line2D.Double(0,0,1,1));

		entity.findClosestLaser(entity.getCentre(),this.battle.getLasersOfTeam(2),this.battle.getWidth());
		assertTrue(entity.getTarget().getX() == 0 && entity.getTarget().getY() == 0);
	}

	@Test
	public void testGetX() {

		this.entity.setX(0);

		assertTrue(this.entity.getX() == 0);
	}

	@Test
	public void testSetX() {

		this.entity.setX(0);

		assertTrue(this.entity.getX() == 0);
	}

	@Test
	public void testGetY() {

		this.entity.setY(0);

		assertTrue(this.entity.getY() == 0);
	}

	@Test
	public void testSetY() {

		this.entity.setY(0);
		assertTrue(this.entity.getY() == 0);
	}

	@Test
	public void testGetWidth() {

		this.entity.setWidth(0);
		assertTrue(this.entity.getWidth() == 0);
	}

	@Test
	public void testSetWidth() {

		this.entity.setWidth(0);
		assertTrue(this.entity.getWidth() == 0);
	}

	@Test
	public void testGetHeight() {

		this.entity.setHeight(0);
		assertTrue(this.entity.getWidth() == 0);
	}

	@Test
	public void testSetHeight() {

		this.entity.setWidth(0);
		assertTrue(this.entity.getWidth() == 0);
	}

	@Test
	public void testGetCentre() {

		Point2D.Double p = new Point2D.Double(0,0);
		entity.setCentre(p);
		assertTrue(this.entity.getCentre().equals(p));
	}

	@Test
	public void testGetPreviousCentre() {

		Point2D.Double p = new Point2D.Double(0,0);
		entity.setPreviousCentre(p);
		assertTrue(this.entity.getPreviousCentre().equals(p));
	}

	@Test
	public void testSetCentre() {
		Point2D.Double p = new Point2D.Double(0,0);
		entity.setCentre(p);
		assertTrue(this.entity.getCentre().equals(p));
	}

	@Test
	public void testIsMoving() {
		assertFalse(entity.isMoving());
	}

	@Test
	public void testSetMoving() {
		this.entity.setMoving(true);
		assertTrue(entity.isMoving());
	}

	@Test
	public void testGetTarget() {

		Point2D.Double p = new Point2D.Double(0,0);
		entity.setTarget(p);
		assertTrue(entity.getTarget().equals(p));
	}

	@Test
	public void testSetTarget() {

		Point2D.Double p = new Point2D.Double(0,0);
		entity.setTarget(p);
		assertTrue(entity.getTarget().equals(p));
	}

	@Test
	public void testGetTargetEntity() {

		Entity enemy = this.battle.getTeam(2).get(0);
		this.entity.setTargetEntity(enemy);
		assertTrue(this.entity.getTargetEntity().equals(enemy));
	}

	@Test
	public void testSetTargetEntity() {

		Entity enemy = this.battle.getTeam(2).get(0);
		this.entity.setTargetEntity(enemy);
		assertTrue(this.entity.getTargetEntity().equals(enemy));
	}

	@Test
	public void testGetFrame() {
		entity.setFrame(1);
		assertTrue(entity.getFrame() == 1);
	}

	@Test
	public void testSetFrame() {
		entity.setFrame(1);
		assertTrue(entity.getFrame() == 1);
	}

	@Test
	public void testGetTotalHealth() {

		this.entity.setTotalHealth(30);
		assertTrue(this.entity.getTotalHealth() == 30);
	}

	@Test
	public void testSetTotalHealth() {

		this.entity.setTotalHealth(30);
		assertTrue(this.entity.getTotalHealth() == 30);
	}

	@Test
	public void testGetCurrentHealth() {

		this.entity.setCurrentHealth(30);
		assertTrue(this.entity.getCurrentHealth() == 30);
	}

	@Test
	public void testSetCurrentHealth() {

		this.entity.setCurrentHealth(30);
		assertTrue(this.entity.getCurrentHealth() == 30);
	}

	@Test
	public void testGetDamage() {

		this.entity.setDamage(10);
		assertTrue(this.entity.getDamage() == 10);
	}

	@Test
	public void testSetDamage() {

		this.entity.setDamage(10);
		assertTrue(this.entity.getDamage() == 10);
	}

	@Test
	public void testGetSpeed() {

		this.entity.setSpeed(10);
		assertTrue(this.entity.getSpeed() == 10);
	}

	@Test
	public void testSetSpeed() {

		this.entity.setSpeed(10);
		assertTrue(this.entity.getSpeed() == 10);
	}

	@Test
	public void testGetType() {

		this.entity.setType(Entity.TYPE.AGGRESSIVE);
		assertTrue(this.entity.getType() == Entity.TYPE.AGGRESSIVE);
	}

	@Test
	public void testSetType() {
		this.entity.setType(Entity.TYPE.AGGRESSIVE);
		assertTrue(this.entity.getType() == Entity.TYPE.AGGRESSIVE);
	}

	@Test
	public void testGetPrevShoot() {
		long time = System.currentTimeMillis();
		entity.setPrevShoot(time);
		assertTrue(entity.getPrevShoot() == time);
	}

	@Test
	public void testSetPrevShoot() {
		long time = System.currentTimeMillis();
		entity.setPrevShoot(time);
		assertTrue(entity.getPrevShoot() == time);
	}

	@Test
	public void testGetShotInterval() {

		this.entity.setShotInterval(100);
		assertTrue(this.entity.getShotInterval() == 100);
	}

	@Test
	public void testSetShotInterval() {

		this.entity.setShotInterval(100);
		assertTrue(this.entity.getShotInterval() == 100);
	}

	@Test
	public void testGetShipid() {

		this.entity.setShipid(1);
		assertTrue(this.entity.getShipid() == 1);
	}

	@Test
	public void testSetParalysed() {
		this.entity.setParalysed(true);
		assertTrue(this.entity.isParalysed());
	}

	@Test
	public void testIsParalysed() {
		assertFalse(this.entity.isParalysed());
	}

	@Test
	public void testSetParalysationLength() {
		this.entity.setParalysationLength(100);
		assertTrue(this.entity.getParalysationLength() == 100);
	}

	@Test
	public void testGetParalysationTime() {
		long time = System.currentTimeMillis();
		this.entity.setParalysed(true);
		assertTrue(entity.getParalysationTime() >= time);
	}

	@Test
	public void testGetParalysationLength() {
		this.entity.setParalysationLength(100);
		assertTrue(this.entity.getParalysationLength() == 100);
	}

	@Test
	public void testSetShipid() {
		this.entity.setShipid(1);
		assertTrue(this.entity.getShipid() == 1);
	}

	@Test
	public void testExecuteAGGRESSIVEBehaviour() {
		Point2D.Double centre = this.entity.getCentre();

		this.entity.setX(0);
		this.entity.setY(0);

		for (Entity e : this.battle.getTeam(2)) {
			e.setX(600);
			e.setY(600);
		}

		entity.setTargetEntity(this.battle.getTeam(2).get(0));
		entity.setTarget(this.battle.getTeam(2).get(0).getCentre());
		entity.setType(Entity.TYPE.AGGRESSIVE);

		entity.findTarget(this.battle.getTeam(2),this.battle.getWidth());
		entity.update(System.currentTimeMillis(),this.battle.getWidth(),this.battle.getHeight(),this.battle.getTeam(1),this.battle);


		assertFalse(this.entity.getCentre().getX() == 0 && this.entity.getY() == 0);
	}

	@Test
	public void testExecuteDEFENSIVEBehaviour() {

		Point2D.Double centre = this.entity.getCentre();

		this.entity.setX(0);
		this.entity.setY(0);

		for (Entity e : this.battle.getTeam(2)) {
			e.setX(600);
			e.setY(600);
		}

		entity.setTargetEntity(this.battle.getTeam(2).get(0));
		entity.setTarget(this.battle.getTeam(2).get(0).getCentre());
		entity.setType(Entity.TYPE.DEFENSIVE);

		entity.findTarget(this.battle.getTeam(2),this.battle.getWidth());
		entity.update(System.currentTimeMillis(),this.battle.getWidth(),this.battle.getHeight(),this.battle.getTeam(1),this.battle);

		assertFalse(this.entity.getX() == 0 && this.entity.getY() == 0);

	}

	@Test
	public void testExecuteTACTICALBehaviour() {

		Point2D.Double centre = this.entity.getCentre();

		this.entity.setX(0);
		this.entity.setY(0);

		for (Entity e : this.battle.getTeam(2)) {
			e.setX(600);
			e.setY(600);
		}

		entity.setTargetEntity(this.battle.getTeam(2).get(0));
		entity.setTarget(this.battle.getTeam(2).get(0).getCentre());
		entity.setType(Entity.TYPE.TACTICAL);

		entity.findTarget(this.battle.getTeam(2),this.battle.getWidth());
		entity.update(System.currentTimeMillis(),this.battle.getWidth(),this.battle.getHeight(),this.battle.getTeam(1),this.battle);

		assertFalse(this.entity.getX() == 0 && this.entity.getY() == 0);


	}

	@Test
	public void testExecuteALTERNATIVEBehaviour() {

		Point2D.Double centre = this.entity.getCentre();

		this.entity.setX(0);
		this.entity.setY(0);

		for (Entity e : this.battle.getTeam(2)) {
			e.setX(600);
			e.setY(600);
		}


		entity.setTargetEntity(this.battle.getTeam(2).get(0));
		entity.setTarget(this.battle.getTeam(2).get(0).getCentre());
		entity.setType(Entity.TYPE.ALTERNATIVE);

		entity.findTarget(this.battle.getTeam(2),this.battle.getWidth());
		entity.update(System.currentTimeMillis(),this.battle.getWidth(),this.battle.getHeight(),this.battle.getTeam(1),this.battle);

		assertFalse(this.entity.getX() == 0 && this.entity.getY() == 0);


	}

	@Test
	public void testExecuteKINGCRUSHERBehaviour() {
		Point2D.Double centre = this.entity.getCentre();

		this.entity.setX(0);
		this.entity.setY(0);

		for (Entity e : this.battle.getTeam(2)) {
			e.setX(600);
			e.setY(600);
		}


		entity.setTargetEntity(this.battle.getTeam(2).get(0));
		entity.setTarget(this.battle.getTeam(2).get(0).getCentre());
		entity.setType(Entity.TYPE.KINGCRUSHER);

		entity.findTarget(this.battle.getTeam(2),this.battle.getWidth());
		entity.update(System.currentTimeMillis(),this.battle.getWidth(),this.battle.getHeight(),this.battle.getTeam(1),this.battle);

		assertFalse(this.entity.getX() == 0 && this.entity.getY() == 0);

	}

	@Test
	public void testExecuteBUGFIXERBehaviour() {

		Point2D.Double centre = this.entity.getCentre();

		this.entity.setX(0);
		this.entity.setY(0);

		for (Entity e : this.battle.getTeam(2)) {
			e.setX(600);
			e.setY(600);
		}


		entity.setTargetEntity(this.battle.getTeam(2).get(0));
		entity.setTarget(this.battle.getTeam(2).get(0).getCentre());
		entity.setType(Entity.TYPE.SURVIVOR);

		entity.findTarget(this.battle.getTeam(2),this.battle.getWidth());
		entity.update(System.currentTimeMillis(),this.battle.getWidth(),this.battle.getHeight(),this.battle.getTeam(1),this.battle);

		assertFalse(this.entity.getX() == 0 && this.entity.getY() == 0);

	}

	@Test
	public void testExecuteSURVIVORBehaviour() {

		Point2D.Double centre = this.entity.getCentre();

		this.entity.setX(0);
		this.entity.setY(0);

		for (Entity e : this.battle.getTeam(2)) {
			e.setX(600);
			e.setY(600);
		}


		entity.setTargetEntity(this.battle.getTeam(2).get(0));
		entity.setTarget(this.battle.getTeam(2).get(0).getCentre());
		entity.setType(Entity.TYPE.SURVIVOR);

		entity.findTarget(this.battle.getTeam(2),this.battle.getWidth());
		entity.update(System.currentTimeMillis(),this.battle.getWidth(),this.battle.getHeight(),this.battle.getTeam(1),this.battle);

		assertFalse(this.entity.getX() == 0 && this.entity.getY() == 0);
	}

	@Test
	public void testGetNextPossibleLocation() {

		Point2D.Double p1 = new Point2D.Double(0,0);
		Point2D.Double p2 = new Point2D.Double(0,1);

		assertTrue(entity.getNextPossibleLocation(p1,p2).getX() == 0.0 && entity.getNextPossibleLocation(p1,p2).getY() == 2.0);
	}

	@Test
	public void testGetLongRangeLaserCount() {

		switch (this.entity.getShipType()){
			case "Command":
				assertTrue(this.entity.getLongRangeLaserCount() == this.battle.getStandardCommandShipLongRangeLaserCount());
				break;
			case "Carrier":
				assertTrue(this.entity.getLongRangeLaserCount() == this.battle.getStandardCarrierShipLongRangeLaserCount());
				break;
			default:
				assertTrue(this.entity.getLongRangeLaserCount() == this.battle.getStandardFighterShiplongRangeLaserCount());
				break;
		}

	}

	@Test
	public void testShootLongRangeLaser() {

	}

	@Test
	public void testGetPreviousHeatShotTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanShootLongRangeLaser() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMagneticShotCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testShootMagneticLaser() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPreviousMagneticShotTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanShootMagneticLaser() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetShipType() {
		fail("Not yet implemented");
	}


}
