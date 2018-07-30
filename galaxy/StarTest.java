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
import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.Main;

public class StarTest {

	Star s;

	@Before
	public void setUp() {
		s = new Star(0, 16);
	}

	@After
	public void tearDown() {
		s = null;
	}
	
	@Test
	public void testUpdate(){
		s.update(0);
	}
	
	@Test
	public void testDraw(){
		s.draw((new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)).getGraphics());
	}

	@Test
	public void testMoveTradeRoutes() {

	}

	@Test
	public void testAddTradeRoute() {
		int numT = s.getRoutes().size();
		s.addTradeRoute(new Planet(0, 0, Main.USEDB), new Planet(0, 0, Main.USEDB));
		assertEquals(numT + 1, s.getRoutes().size());
	}

	@Test
	public void testRemoveTradeRoute() {
		Planet p = new Planet(0, 0, Main.USEDB);
		s.addTradeRoute(p, new Planet(2, 0, Main.USEDB));
		s.addTradeRoute(new Planet(4, 0, Main.USEDB), new Planet(3, 0, Main.USEDB));
		int numT = s.getRoutes().size();
		s.removeTradeRoute(p);
		assertNotEquals(numT, s.getRoutes().size());
		int num = s.getRoutes().size();
		s.removeTradeRoute(p);
		assertTrue(num == s.getRoutes().size());
	}

	@Test
	public void testMoveUD() {
		s.setBounds(new Rectangle2D.Double(0, 0, 100, 100));
		s.moveUD(1);
		assertEquals(s.getyDif(), 1, 0);
	}

	@Test
	public void testMoveLR() {
		s.setBounds(new Rectangle2D.Double(0, 0, 100, 100));
		s.moveLR(1);
		assertEquals(s.getxDif(), 1, 0);
	}

	@Test
	public void testUpdateBounds() {
		s.updateBounds();
	}

	@Test
	public void testClicked() {
		s.clicked(new Point(0, 0), new AffineTransform());
	}

	@Test
	public void testGetBounds() {
		s.setBounds(new Rectangle2D.Double(0, 0, 50, 50));
		assertEquals(new Rectangle2D.Double(0, 0, 50, 50), s.getBounds());
	}

	@Test
	public void testGetCenter() {
		s.getCenter();
	}

	@Test
	public void testSetBounds() {
		s.setBounds(new Rectangle2D.Double(0, 0, 50, 50));
		assertEquals(new Rectangle2D.Double(0, 0, 50, 50), s.getBounds());
	}

	@Test
	public void testSetSelected() {
		s.setSelected(true);
		assertTrue(s.isSelected());
		s.setSelected(false);
		assertFalse(s.isSelected());
	}

	@Test
	public void testIsSelected() {
		s.setSelected(true);
		assertTrue(s.isSelected());
		s.setSelected(false);
		assertFalse(s.isSelected());
	}

	@Test
	public void testGetxDif() {
		s.setxDif(1);
		assertEquals(1, s.getxDif(), 0);
	}

	@Test
	public void testSetxDif() {
		s.setxDif(1);
		assertEquals(s.getxDif(), 1, 0);
		s.setxDif(2);
		assertEquals(s.getxDif(), 2, 0);
	}

	@Test
	public void testGetyDif() {
		s.setyDif(1);
		assertEquals(s.getyDif(), 1, 0);
		s.setyDif(2);
		assertEquals(s.getyDif(), 2, 0);
	}

	@Test
	public void testSetyDif() {
		s.setyDif(1);
		assertEquals(s.getyDif(), 1, 0);
		s.setyDif(2);
		assertEquals(s.getyDif(), 2, 0);
	}

	@Test
	public void testGetNumOfPlanets() {
		s.setNumOfPlanets(0);
		assertEquals(s.getNumOfPlanets(), 0);
		s.setNumOfPlanets(1);
		assertNotEquals(s.getNumOfPlanets(), 0);
		assertEquals(s.getNumOfPlanets(), 1);
	}

	@Test
	public void testSetNumOfPlanets() {
		s.setNumOfPlanets(0);
		assertEquals(s.getNumOfPlanets(), 0);
		s.setNumOfPlanets(1);
		assertNotEquals(s.getNumOfPlanets(), 0);
		assertEquals(s.getNumOfPlanets(), 1);
	}

	@Test
	public void testIsGenerated() {
		s.setGenerated(true);
		assertTrue(s.isGenerated());
		s.setGenerated(false);
		assertFalse(s.isGenerated());
	}

	@Test
	public void testSetGenerated() {
		s.setGenerated(true);
		assertTrue(s.isGenerated());
		s.setGenerated(false);
		assertFalse(s.isGenerated());
	}

	@Test
	public void testGetSize() {
		s.setSize(50);
		assertEquals(s.getSize(), 50);
	}

	@Test
	public void testSetSize() {
		s.setSize(50);
		assertEquals(s.getSize(), 50);
	}

	@Test
	public void testGetSolarPos() {
		Point2D.Double p = new Point2D.Double(50, 50);
		s.setSolarPos(p);
		assertEquals(s.getSolarPos(), p);
	}

	@Test
	public void testSetSolarPos() {
		Point2D.Double p = new Point2D.Double(50, 50);
		s.setSolarPos(p);
		assertEquals(p, s.getSolarPos());
		s.setSolarPos(new Point2D.Double(100, 100));
		Assert.assertNotEquals(p, s.getSolarPos());
	}

	@Test
	public void testIncrementSolarPos() {
		s.incrementSolarPos(1, 1);
	}

	@Test
	public void testGetSeed() {
		s.setSeed(50);
		assertEquals(50, s.getSeed());
		s.setSeed(100);
		assertEquals(100, s.getSeed());
	}

	@Test
	public void testSetSeed() {
		s.setSeed(50);
		assertEquals(50, s.getSeed());
		s.setSeed(100);
		assertEquals(100, s.getSeed());
	}

	@Test
	public void testIsOwned() {
		s.setOwned(true);
		assertTrue(s.isOwned());
		s.setOwned(false);
		assertFalse(s.isOwned());
	}

	@Test
	public void testSetOwned() {
		s.setOwned(true);
		assertTrue(s.isOwned());
		s.setOwned(false);
		assertFalse(s.isOwned());
	}
	
	@Test
	public void testGetDistance() {
		s.setDistance(0);
		assertEquals(0, s.getDistance(), 0);
	}

	@Test
	public void setDistance() {
		s.setDistance(0);
		assertEquals(0, s.getDistance(), 0);
	}

	@Test
	public void testGetAngle() {
		s.setAngle(100);
		assertEquals(100, s.getAngle(), 0);
	}

	@Test
	public void testSetAngle() {
		s.setAngle(100);
		assertEquals(100, s.getAngle(), 0);
	}

	@Test
	public void testGetIncrement() {
		s.setIncrement(0.5f);
		assertEquals(0.5f, s.getIncrement(), 0);
	}

	@Test
	public void testSetIncrement() {
		s.setIncrement(0.5f);
		assertEquals(0.5f, s.getIncrement(), 0);
	}

	@Test
	public void testGetPosition() {
		s.setPosition(new Point2D.Double(50, 50));
		assertEquals(new Point2D.Double(50, 50), s.getPosition());
	}

	@Test
	public void testSetPosition() {
		s.setPosition(new Point2D.Double(50, 50));
		assertEquals(new Point2D.Double(50, 50), s.getPosition());
	}

	@Test
	public void testGetRoutes() {
		s.setRoutes(new ArrayList<TradeRoute>());
		assertEquals(new ArrayList<TradeRoute>(), s.getRoutes());
	}

	@Test
	public void testSetRoutes() {
		s.setRoutes(new ArrayList<TradeRoute>());
		assertEquals(new ArrayList<TradeRoute>(), s.getRoutes());
	}

}
