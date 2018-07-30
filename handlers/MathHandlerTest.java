package handlers;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.junit.Test;

public class MathHandlerTest {

	@Test
	public void testSummation() {
		MathHandler.summation(3);
	}

	@Test
	public void testDrawLine() {
		MathHandler.drawLine(new Point(0, 0), new Point(1, 2));
	}

	@Test
	public void testGetDistanceDoubleDouble() {
		MathHandler.getDistance(new Point2D.Double(0, 0), new Point2D.Double(5, 5));
	}

	@Test
	public void testGetDistancePointPoint() {
		MathHandler.getDistance(new Point(0, 0), new Point(10,20));
	}

	@Test
	public void testGetAngleDoubleDouble() {
		MathHandler.getAngle(new Point2D.Double(0, 0), new Point2D.Double(10, -10));
	}

	@Test
	public void testGetAnglePointPoint() {
		MathHandler.getAngle(new Point(5, -5), new Point(-10, 20));
	}

	@Test
	public void testGetPoint2D() {
		MathHandler.getPoint2D(new Point2D.Double(0, 0), new Point2D.Double(5, 5), 1, Math.PI/6);
	}

	@Test
	public void testGetPoint() {
		MathHandler.getPoint(new Point(5, 5), new Point(-10, 6), 4, 0);
	}

	@Test
	public void testAverageVector() {
		MathHandler.averageVector(new Point2D.Double(4, 5), new Point2D.Double(6, 7));
	}

	@Test
	public void testConvertPolarToCartesian() {
		MathHandler.convertPolarToCartesian(Math.PI, 20, 0, 0);
	}

	@Test
	public void testLinearInterpolate() {
		MathHandler.linearInterpolate(0.5, 0.65, 0.2);
	}

	@Test
	public void testCosineInterpolate() {
		MathHandler.cosineInterpolate(0.5, 0.65, 0.2);
	}

	@Test
	public void testCubicInterpolate() {
		MathHandler.cubicInterpolate(0.2, 0.4, 0.55, 0.45, 0.6);
	}

	@Test
	public void testSmooth2() {
		MathHandler.smooth2(0.2, 0.6);
	}

	@Test
	public void testSmooth3() {
		MathHandler.smooth3(0.2, 0.4, 0.6);
	}

	@Test
	public void testSmooth4() {
		MathHandler.smooth4(0.2, 0.4, 0.6, 0.8);
	}

	@Test
	public void testSmooth5() {
		MathHandler.smooth5(0.1, 0.3, 0.5, 0.7, 0.9);
	}

	@Test
	public void testSmooth6() {
		MathHandler.smooth6(0, 0.2, 0.4, 0.6, 0.8, 1);
	}

	@Test
	public void testSmooth7() {
		MathHandler.smooth7(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7);
	}

	@Test
	public void testSmooth8() {
		MathHandler.smooth8(0, 0.2, 0.3, 0.5, 0.6, 0.8, 0.9, 1);
	}

	@Test
	public void testSmooth9() {
		MathHandler.smooth9(0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8);
	}

	@Test
	public void testCeiling() {
		MathHandler.ceiling(4, 6);
	}

	@Test
	public void testSetRandomSeed() {
		MathHandler.setRandomSeed(209374);
	}

}
