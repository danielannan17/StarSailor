package instantBattle;
import entities.Entity;
import entities.Ship;
import entities.Vector2D;
import entities.projectiles.BasicProjectile;
import instantBattle.PhysicsEngine;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.LinkedList;

public class PhysicsEngineTest {
	
	PhysicsEngine engine;
	PhysicsEngine engine2;
	BasicProjectile p;
	
	@Before
	public void setUp() throws Exception {
		engine = new PhysicsEngine(30,new ArrayList<Entity>(),100,100);
		engine2 = new PhysicsEngine(30,new ArrayList<Entity>(),100,100);
		p = new BasicProjectile(0,0,new Vector2D(),0);
		engine.addEntity(p);
		(new Thread(engine)).start();
	}
	
	@Test
	public void testAddEntity() {
		BasicProjectile p = new BasicProjectile();
		engine.addEntity(p);
		assertTrue(!engine.addEntity(p));
		engine.removeEntity(p);
	}
	
	@Test
	public void testAddEntities() {
		BasicProjectile p = new BasicProjectile();
		BasicProjectile q = new BasicProjectile();
		LinkedList<Entity> es = new LinkedList<Entity>();
		es.add(p);
		es.add(q);
		engine.addEntities(es);
		assertFalse(engine.addEntities(es));
		assertTrue(engine.containsEntity(p));
		assertTrue(engine.containsEntity(q));
		engine.removeEntity(p);
		engine.removeEntity(q);
	}
	
	@Test
	public void testRemoveEntity() {
		BasicProjectile p = new BasicProjectile();
		engine.addEntity(p);
		assertTrue(engine.containsEntity(p));
		engine.removeEntity(p);
		assertFalse(engine.containsEntity(p));
	}
	
	@Test
	public void testMovement() {
		//make sure that things that shouldn't move, don't
		assertTrue(p.getxLocation() == 0);
		assertTrue(p.getyLocation() == 0);
		
		Vector2D testVelocity = new Vector2D(1,0);
		BasicProjectile q = new BasicProjectile(20,20,testVelocity,22);
		
		engine2.addEntity(q);
		engine2.updateEntities();
		
		assertEquals(q.getyLocation(),20);
		assertEquals(q.getxLocation(),21);
		
		engine.removeEntity(p);
		engine2.removeEntity(q);
	}
	
	@Test
	public void testInMap() {
		Vector2D testVelocity = new Vector2D();
		
		BasicProjectile a = new BasicProjectile(96,50,testVelocity,22);
		BasicProjectile b = new BasicProjectile(50,96,testVelocity,22);
		BasicProjectile c = new BasicProjectile(4,50,testVelocity,22);
		BasicProjectile d = new BasicProjectile(50,4,testVelocity,22);
		
		BasicProjectile e = new BasicProjectile(95,50,testVelocity,22);
		BasicProjectile f = new BasicProjectile(50,95,testVelocity,22);
		BasicProjectile g = new BasicProjectile(5,50,testVelocity,22);
		BasicProjectile h = new BasicProjectile(50,5,testVelocity,22);
		
		BasicProjectile i = new BasicProjectile(50,50,testVelocity,22);
		
		assertEquals(engine.inMap(a),1);
		assertEquals(engine.inMap(b),2);
		assertEquals(engine.inMap(c),3);
		assertEquals(engine.inMap(d),4);
		
		assertEquals(engine.inMap(e),0);
		assertEquals(engine.inMap(f),0);
		assertEquals(engine.inMap(g),0);
		assertEquals(engine.inMap(h),0);
		
		assertEquals(engine.inMap(i),0);
	}
	
	@Test
	public void testHandleOutOfMap() {
		Vector2D testVelocity = new Vector2D(0,5);

		BasicProjectile a = new BasicProjectile(96,50,testVelocity,22);
		BasicProjectile b = new BasicProjectile(50,96,testVelocity,22);
		BasicProjectile c = new BasicProjectile(4,50,testVelocity,22);
		BasicProjectile d = new BasicProjectile(50,4,testVelocity,22);
		
		BasicProjectile e = new BasicProjectile(95,50,testVelocity,22);
		BasicProjectile f = new BasicProjectile(50,95,testVelocity,22);
		BasicProjectile g = new BasicProjectile(5,50,testVelocity,22);
		BasicProjectile h = new BasicProjectile(50,5,testVelocity,22);
		
		BasicProjectile i = new BasicProjectile(50,50,testVelocity,22);

		Ship j = new Ship(96,50,testVelocity,"j");
		Ship k = new Ship(50,96,testVelocity,"k");
		Ship l = new Ship(4,50,testVelocity,"l");
		Ship m = new Ship(50,4,testVelocity,"m");

		Ship n = new Ship(95,50,testVelocity,"n");
		Ship o = new Ship(50,95,testVelocity,"o");
		Ship p = new Ship(5,50,testVelocity,"p");
		Ship q = new Ship(50,5,testVelocity,"q");
		
		Ship r = new Ship(50,50,testVelocity,"r");

		engine.handleOutOfMapEntity(a, engine.inMap(a));
		engine.handleOutOfMapEntity(b, engine.inMap(b));
		engine.handleOutOfMapEntity(c, engine.inMap(c));
		engine.handleOutOfMapEntity(d, engine.inMap(d));

		engine.handleOutOfMapEntity(e, engine.inMap(e));
		engine.handleOutOfMapEntity(f, engine.inMap(f));
		engine.handleOutOfMapEntity(g, engine.inMap(g));
		engine.handleOutOfMapEntity(h, engine.inMap(h));

		engine.handleOutOfMapEntity(i, engine.inMap(i));
		
		engine.handleOutOfMapEntity(j, engine.inMap(j));
		engine.handleOutOfMapEntity(k, engine.inMap(k));
		engine.handleOutOfMapEntity(l, engine.inMap(l));
		engine.handleOutOfMapEntity(m, engine.inMap(m));
	
		engine.handleOutOfMapEntity(n, engine.inMap(n));
		engine.handleOutOfMapEntity(o, engine.inMap(o));
		engine.handleOutOfMapEntity(p, engine.inMap(p));
		engine.handleOutOfMapEntity(q, engine.inMap(q));

		engine.handleOutOfMapEntity(r, engine.inMap(r));
		
		assertTrue(a.isRemove());
		assertTrue(b.isRemove());
		assertTrue(c.isRemove());
		assertTrue(d.isRemove());
		
		assertEquals(e.getxLocation(),95);
		assertEquals(f.getyLocation(),95);
		assertEquals(g.getxLocation(),5);
		assertEquals(h.getyLocation(),5);
		assertEquals(5,e.getVelocity().getMagnitude(),0.001);
		assertEquals(5,f.getVelocity().getMagnitude(),0.001);
		assertEquals(5,g.getVelocity().getMagnitude(),0.001);
		assertEquals(5,h.getVelocity().getMagnitude(),0.001);
		
		assertEquals(i.getxLocation(),50);
		assertEquals(i.getyLocation(),50);
		assertEquals(5,i.getVelocity().getMagnitude(),0.001);
		
		assertEquals(j.getxLocation(),95);
		assertEquals(k.getyLocation(),95);
		assertEquals(l.getxLocation(),5);
		assertEquals(m.getyLocation(),5);
		
		assertEquals(n.getxLocation(),95);
		assertEquals(o.getyLocation(),95);
		assertEquals(p.getxLocation(),5);
		assertEquals(q.getyLocation(),5);
		
		assertEquals(r.getxLocation(),50);
		assertEquals(r.getyLocation(),50);
	}
	
	@Test
	public void testAirResistance() {
		Vector2D testVelocity = new Vector2D(10,0);
		
		BasicProjectile p = new BasicProjectile(0,0,testVelocity,1);
		Ship s = new Ship(0,0,testVelocity,"s");
		
		assertEquals(0,engine.getAirResistance(p).getMagnitude(),0.001);
		
		//note - this test will fail if air resistance is ever modified in PhysicsEngine
		assertEquals(1,engine.getAirResistance(s).getMagnitude(),0.001);
	}
	
	@Test
	public void testApplyForce() {
		Vector2D force = new Vector2D(10,10);
		Ship s = new Ship(0,0,new Vector2D(),"s");
		engine.applyForce(s, force);

		assertEquals(s.getMaxAcceleration(),s.getAcceleration().getMagnitude(),0.001);
	}
	
	@Test
	public void testContains() {
		assertTrue(engine.containsEntity(p));
		
		BasicProjectile r = new BasicProjectile();
		
		assertFalse(engine.containsEntity(r));
	}
	
	@After
	public void tearDown() throws Exception {
		engine.stop();
	}

}
