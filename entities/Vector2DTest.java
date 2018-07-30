package entities;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Vector2DTest {

	Vector2D v1,v2;
	
	@Before
	public void setUp() throws Exception {
		v1 = new Vector2D();
		v2 = new Vector2D(10,22);
	}

	@Test
	public void testConstructor() {
		assertEquals(0,v1.getMagnitude(),0);
		assertEquals(10,v2.getX(),0);
		assertEquals(22,v2.getY(),0);
	}
	
	@Test
	public void testAdd() {
		Vector2D v3 = new Vector2D(4,20);
		v3.add(v2);
		assertEquals(14,v3.getX(),0);
		assertEquals(42,v3.getY(),0);
		
		v3.add(v1);
		assertEquals(14,v3.getX(),0);
		assertEquals(42,v3.getY(),0);
	}
	
	@Test
	public void testSub() {
		Vector2D v3 = new Vector2D(15,20);
		
		v3.sub(v1);

		assertEquals(15,v3.getX(),0);
		assertEquals(20,v3.getY(),0);
		
		v3.sub(v2);
		
		assertEquals(5,v3.getX(),0);
		assertEquals(-2,v3.getY(),0);
	}
	
	@Test
	public void testMult() {
		Vector2D v3 = new Vector2D(5,10);
		v3.mult(3);
		
		assertEquals(15,v3.getX(),0);
		assertEquals(30,v3.getY(),0);
	}
	
	@Test
	public void testDiv() {
		Vector2D v3 = new Vector2D(25,45);
		
		v3.div(5);
		
		assertEquals(5,v3.getX(),0);
		assertEquals(9,v3.getY(),0);
		
		//division by 0 should return the original vector - our choice.
		v3.div(0);
		
		assertEquals(5,v3.getX(),0);
		assertEquals(9,v3.getY(),0);
	}
	
	@Test
	public void testNormalize() {
		Vector2D v3 = new Vector2D(4,3);
		v3.normalize();
		
		assertEquals(1,v3.getMagnitude(),0);
		assertEquals(0.8,v3.getX(),0.001);
		assertEquals(0.6,v3.getY(),0.001);
	}
	
	@Test
	public void testCopy() {
		
		Vector2D v3 = v1;
		
		assertTrue(v3 == v1);
		
		v3 = v1.copy();
		
		assertFalse(v3 == v1);
	}
	
	@Test
	public void testGetXY() {
		assertEquals(10,v2.getX(),0);
		assertEquals(22,v2.getY(),0);
	}
	
	@Test
	public void testConvertToDirection() {
		assertEquals(Math.PI,Vector2D.convertToDirection(0,0,1,0),0.001);
		assertEquals(0,Vector2D.convertToDirection(0,0,-1,0),0.001);
		assertEquals(-Math.PI / 2,Vector2D.convertToDirection(0,0,0,1), 0.001);
		assertEquals(Math.PI / 2,Vector2D.convertToDirection(0, 0, 0, -1),0.001);
	}
	
	@Test
	public void testGetMagnitude() {
		assertEquals(0,v1.getMagnitude(),0);
		assertEquals(24.166,v2.getMagnitude(),0.001);
	}
	
	@Test
	public void testSetMagnitude() {
		Vector2D v3 = v2.copy();
		
		v3.setMagnitude(1);
		
		assertEquals(1,v3.getMagnitude(),0.001);

		v3.setMagnitude(12);
		
		assertEquals(12,v3.getMagnitude(),0.001);
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
