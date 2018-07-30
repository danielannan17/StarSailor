package instantBattle;

import static org.junit.Assert.*;

import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Before;
import org.junit.Test;

import entities.Entity;
import entities.Ship;
import entities.Vector2D;
import entities.projectiles.BasicProjectile;

public class ShipBehaviourTest {

	ShipBehaviour sb;
	Ship s1,s2;
	BasicProjectile p;
	
	@Before
	public void setUp() throws Exception {
		CopyOnWriteArrayList<Entity> es = new CopyOnWriteArrayList<>();
		s1 = new Ship(45,0,new Vector2D(),"");
		s2 = new Ship(50,0,new Vector2D(),"");
		p = new BasicProjectile(100,100,new Vector2D(),1);
		
		es.add(s1);
		es.add(s2);
		es.add(p);
		
		sb = new ShipBehaviour(es);
	}

	@Test
	public void testSeperationBetweenShips() {
		assertEquals(0,s1.getAcceleration().getMagnitude(),0);
		assertEquals(0,s2.getAcceleration().getMagnitude(),0);
		
		sb.applySeperation(s1,s2,ShipBehaviour.distBetween(s1,s2),sb.getSepStrength(),sb.getSepDist());
		sb.applySeperation(s2,s1,ShipBehaviour.distBetween(s1,s2),sb.getSepStrength(),sb.getSepDist());

		assertNotEquals(0,s1.getAcceleration().getMagnitude());
		assertNotEquals(0,s2.getAcceleration().getMagnitude());
		
		double s1dir = Vector2D.convertToDirection(0, 0, s1.getAcceleration().getX(),s1.getAcceleration().getY());
		double s2dir = Vector2D.convertToDirection(0, 0, s2.getAcceleration().getX(),s2.getAcceleration().getY());
		
		assertEquals(s1dir,s2dir-Math.PI,0.001);
	}
	
	@Test
	public void testAddEntity() {
		BasicProjectile p = new BasicProjectile();
		sb.addEntity(p);
		assertTrue(!sb.addEntity(p));
		sb.removeEntity(p);
	}
	
	@Test
	public void testAddEntities() {
		BasicProjectile p = new BasicProjectile();
		BasicProjectile q = new BasicProjectile();
		CopyOnWriteArrayList<Entity> es = new CopyOnWriteArrayList<>();
		es.add(p);
		es.add(q);
		sb.addEntities(es);
		assertFalse(sb.addEntities(es));
		assertTrue(sb.containsEntity(p));
		assertTrue(sb.containsEntity(q));
		sb.removeEntity(p);
		sb.removeEntity(q);
	}
	
	@Test
	public void testRemoveEntity() {
		BasicProjectile p = new BasicProjectile();
		sb.addEntity(p);
		assertTrue(sb.containsEntity(p));
		sb.removeEntity(p);
		assertFalse(sb.containsEntity(p));
	}
	
	@Test
	public void testDistBetween() {
		assertEquals(5,ShipBehaviour.distBetween(s1, s2),0.001);
		Ship s3 = new Ship(63,97,new Vector2D(),"");
		assertEquals(98.656,ShipBehaviour.distBetween(s1, s3),0.001);
	}
	
	@Test
	public void testStrengthDistGets() {
		assertEquals(10,sb.getSepDist());
		assertEquals(10,sb.getProjSepDist());
		assertEquals(20,sb.getAlignDist());

		assertEquals(0.3,sb.getSepStrength(),0.001);
		assertEquals(0.5,sb.getAlignStrength(),0.001);
		assertEquals(0.6,sb.getProjSepStrength(),0.001);
	}
	
	@Test
	public void testContains() {
		assertTrue(sb.containsEntity(p));
		BasicProjectile q = new BasicProjectile();
		
		assertFalse(sb.containsEntity(q));
	}
}
