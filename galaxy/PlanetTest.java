package galaxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;

import main.Main;

public class PlanetTest {

	Planet p;

	@Before
	public void setUp() {
		p = new Planet(0, 5, Main.USEDB);
	}

	@Test
	public void testUpdate() {
		p.update(0);
	}

	@Test
	public void testDraw() {
		p.draw((new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)).getGraphics(), 1);
	}

	@Test
	public void testClicked() {
		p.setBounds(new Rectangle2D.Double(0, 0, 100, 100));
		assertTrue(p.clicked(new Point(50, 50), new AffineTransform()));
		assertFalse(p.clicked(new Point(101, 101), new AffineTransform()));
	}

	@Test
	public void testIncrementAngle() {
		float angle = p.getAngle();
		p.incrementAngle();
		assertEquals(angle + p.getIncrement(), p.getAngle(), 0);
		p.setAngle(360);
		p.incrementAngle();
		assertEquals(p.getAngle(), 0, 0);
	}

	@Test
	public void testMoveUD() {
		Rectangle2D.Double bounds = p.getBounds();
		p.moveUD(1);
		assertEquals(bounds.y + 1, p.getBounds().y, 0);
	}

	@Test
	public void testMoveLR() {
		Rectangle2D.Double bounds = p.getBounds();
		p.moveLR(1);
		assertEquals(bounds.x + 1, p.getBounds().x, 0);
	}

	@Test
	public void testGetCenter() {
		assertTrue(p.getCenter().equals(new Point2D.Double(p.getBounds().getCenterX(), p.getBounds().getCenterY())));
	}

	@Test
	public void testMovePlanet() {
		float x = p.getxPos();
		p.movePlanet(1, 0);
		assertEquals(x + 1, p.getxPos(), 0);
		float y = p.getyPos();
		p.movePlanet(0, 1);
		assertEquals(y + 1, p.getyPos(), 0);
	}

	@Test
	public void testIsGenerated() {
		p.setGenerated(true);
		assertTrue(p.isGenerated());
		p.setGenerated(false);
		assertFalse(p.isGenerated());
	}

	@Test
	public void testSetGenerated() {
		p.setGenerated(true);
		assertTrue(p.isGenerated());
		p.setGenerated(false);
		assertFalse(p.isGenerated());
	}

	@Test
	public void testGetBounds() {
		p.setBounds(new Rectangle2D.Double(0, 0, 100, 100));
		assertTrue(p.getBounds().equals(new Rectangle2D.Double(0, 0, 100, 100)));
	}

	@Test
	public void testSetBounds() {
		p.setBounds(new Rectangle2D.Double(0, 0, 50, 50));
		Rectangle2D.Double r = p.getBounds();
		assertEquals(r, p.getBounds());
	}

	@Test
	public void testSetOwned() {
		p.setOwned(0);
		int i = p.getOwned();
		p.setOwned(1);
		assertNotEquals((long) i, (long) p.getOwned());
	}

	@Test
	public void testGetOwned() {
		p.setOwned(0);
		assertEquals(0, p.getOwned());
		p.setOwned(1);
		assertEquals(1, p.getOwned());
	}

	@Test
	public void testGetNumOfFighters() {
		p.setNumOfFighters(10);
		assertEquals(10, p.getNumOfFighters());

	}

	@Test
	public void testSetNumOfFighters() {
		p.setNumOfFighters(0);
		int num = p.getNumOfFighters();
		assertEquals(0, num);
		p.setNumOfFighters(10);
		assertEquals(10, p.getNumOfFighters());
		assertNotEquals(num, p.getNumOfFighters());
	}

	@Test
	public void testGetNumOfCarriers() {
		p.setNumOfCarriers(10);
		assertEquals(p.getNumOfCarriers(), 10);
	}

	@Test
	public void testSetNumOfCarriers() {
		p.setNumOfCarriers(0);
		int num = p.getNumOfCarriers();
		assertEquals(num, 0);
		p.setNumOfCarriers(10);
		assertEquals(p.getNumOfCarriers(), 10);
		assertNotEquals(num, p.getNumOfCarriers());
	}

	@Test
	public void testGetNumOfCommand() {
		p.setNumOfCommand(10);
		assertEquals(p.getNumOfCommand(), 10);
	}

	@Test
	public void testSetNumOfCommand() {
		p.setNumOfCommand(0);
		int num = p.getNumOfCommand();
		assertEquals(num, 0);
		p.setNumOfCommand(10);
		assertEquals(p.getNumOfCommand(), 10);
		assertNotEquals(num, p.getNumOfCommand());
	}

	@Test
	public void testIsDrawn() {
		p.setDrawn(true);
		assertTrue(p.isDrawn());
		p.setDrawn(false);
		assertFalse(p.isDrawn());
	}

	@Test
	public void testSetDrawn() {
		boolean drawn = true;
		p.setDrawn(drawn);
		assertTrue(drawn = p.isDrawn());
		p.setDrawn(false);
		assertFalse(p.isDrawn());
	}

	@Test
	public void testGetName() {
		p.setName("hello");
		assertTrue("hello".equals(p.getName()));
		p.setName("something else");
		assertNotEquals("hello", p.getName());
		assertTrue(p.getName().equals("something else"));
	}

	@Test
	public void testSetName() {
		p.setName("hello");
		assertTrue("hello".equals(p.getName()));
		p.setName("something else");
		assertNotEquals("hello", p.getName());
		assertTrue(p.getName().equals("something else"));
	}

	@Test
	public void testGetSeed() {
		p.setSeed(1000);
		assertEquals(1000, p.getSeed());
	}

	@Test
	public void testSetSeed() {
		p.setSeed(1000);
		assertEquals(1000, p.getSeed());
	}

	@Test
	public void testGetAngle() {
		p.setAngle(0.5f);
		assertEquals(0.5f, p.getAngle(), 0);
	}

	@Test
	public void testSetAngle() {
		p.setAngle(0.5f);
		assertEquals(0.5f, p.getAngle(), 0);
	}

	@Test
	public void testIsTrading() {
		p.setTrading(true);
		assertTrue(p.isTrading());
		p.setTrading(false);
		assertFalse(p.isTrading());
	}

	@Test
	public void testSetTrading() {
		p.setTrading(true);
		assertTrue(p.isTrading());
		p.setTrading(false);
		assertFalse(p.isTrading());
	}

	@Test
	public void testGetxDif() {
		p.setxDif(0);
		assertEquals(0, p.getxDif(), 0);
	}

	@Test
	public void testGetyDif() {
		p.setyDif(0);
		assertEquals(0, p.getyDif(), 0);
	}

}
