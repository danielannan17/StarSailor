package handlers;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * handles math
 * 
 * 
 *
 */
public class MathHandler {

	public static final double root2 = Math.sqrt(2.0);
	public static Random random = new Random();

	public static int summation(int n) {
		int sum = 1;
		for (int i = 1; i <= n; i++) {
			sum += i;
		}
		return sum;
	}

	/**
	 * A function to calculate all the points on a line between two points
	 * 
	 * @param start
	 *            The first point
	 * @param end
	 *            The second point
	 * @return An ArrayList of points between the two points
	 */
	public static ArrayList<Point> drawLine(Point start, Point end) {
		ArrayList<Point> updates = new ArrayList<Point>();

		// Creates a new point that represents the line from start to end
		Point line = new Point(end.x - start.x, end.y - start.y);

		// Flags for x, y inversion and coordinate swapping
		int xMultiplier = 1;
		int yMultiplier = 1;
		boolean swapCoords = false;

		// Get the gradient of the line
		float grad = (float) line.y / (float) line.x;

		// If the line is below the x-axis flip the y coordinate
		if (line.y < 0) {
			yMultiplier = -1;
			line.setLocation(line.x, -line.y);
		}
		// If the line is behind the y-axis flip the x coordinate
		if (line.x < 0) {
			xMultiplier = -1;
			line.setLocation(-line.x, line.y);
		}
		// If the gradient of the line is greater than one flip the coordinates
		if (Math.abs(grad) > 1) {
			swapCoords = true;
			line.setLocation(line.y, line.x);
		}

		// Get the new gradient
		grad = (float) line.y / (float) line.x;

		/**
		 * The line should now conform to the assumptions 1. Both the x and y
		 * coordinates are positive 2. the gradient is less than one
		 */

		// For each x Coord on the line
		for (int x = 0; x <= line.x; x++) {

			// Calculate the y Coord
			int y = Math.round(x * grad);
			// Add the point to the ArrayList and undo the transformation of the
			// line for the point
			if (swapCoords) {
				updates.add(new Point(start.x + y * xMultiplier, start.y + x * yMultiplier));
			} else {
				updates.add(new Point(start.x + x * xMultiplier, start.y + y * yMultiplier));
			}

		}

		return updates;

	}

	/**
	 * gets the distance between two points
	 * 
	 * @param p1
	 *            the first point
	 * @param p2
	 *            the second point
	 * @return the distance
	 */
	public static double getDistance(Point2D.Double p1, Point2D.Double p2) {
		double xDif = p1.x - p2.x;
		double yDif = p1.y - p2.y;
		return Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));
	}

	public static double getDistance(Point p1, Point p2) {
		double xDif = p1.x - p2.x;
		double yDif = p1.y - p2.y;
		return Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));
	}

	/**
	 * get's the angle from east between two points
	 * 
	 * @param p1
	 *            the first point
	 * @param p2
	 *            the second point
	 * @return the angle between the two points
	 */
	public static double getAngle(Point2D.Double p1, Point2D.Double p2) {
		if (p1.x != p2.x && p1.y != p2.y) {
			double xdif = (p2.getX() - p1.getX());
			double ydif = (p2.getY() - p1.getY());
			double angle = 0; // in radians
			angle = -Math.atan(ydif / xdif);
			if (xdif < 0) {
				if (ydif < 0) {
					angle += Math.PI;
				} else {
					angle -= Math.PI;
				}
			}
			return -angle;
		} else if (p1.x > p2.x) {
			return Math.PI;
		} else if (p1.x < p2.x) {
			return 0.0;
		} else if (p1.y > p2.y) {
			return -Math.PI / 2.0;
		} else if (p1.y < p2.y) {
			return Math.PI / 2.0;
		}
		return 0.0;
	}

	/**
	 * gets the angle between two points
	 * 
	 * @param p1
	 *            the first point
	 * @param p2
	 *            the second point
	 * @return the angle
	 */
	public static double getAngle(Point p1, Point p2) {
		if (p1.x != p2.x && p1.y != p2.y) {
			double xdif = (p2.getX() - p1.getX());
			double ydif = (p2.getY() - p1.getY());
			double angle = 0; // in radians
			angle = -Math.atan(ydif / xdif);
			if (xdif < 0) {
				if (ydif < 0) {
					angle += Math.PI;
				} else {
					angle -= Math.PI;
				}
			}
			return -angle;
		} else if (p1.x > p2.x) {
			return Math.PI;
		} else if (p1.x < p2.x) {
			return 0.0;
		} else if (p1.y > p2.y) {
			return -Math.PI / 2.0;
		} else if (p1.y < p2.y) {
			return Math.PI / 2.0;
		}
		return 0.0;
	}

	/**
	 * gets the next point along a line
	 * 
	 * @param p1
	 *            the first point
	 * @param p2
	 *            the second point
	 * @param speed
	 *            the speed at which to go along the line
	 * @param accuracy
	 *            how closely you stay on this line, set to 0 for perfect
	 *            accuracy
	 * @return the next point along a line
	 */
	public static Point2D.Double getPoint2D(Point2D.Double p1, Point2D.Double p2, double speed, double accuracy) {
		double angle = 0;
		if (p1.x != p2.x && p1.y != p2.y) {
			double xdif = (p2.getX() - p1.getX());
			double ydif = (p2.getY() - p1.getY());
			angle = 0; // in radians

			angle = -Math.atan(ydif / xdif);
			if (xdif < 0) {
				if (ydif < 0) {
					angle += Math.PI;
				} else {
					angle -= +Math.PI;
				}
			}
			if (accuracy != 0) {
				angle += (random.nextGaussian() * accuracy);
			}
			double xgain = 0;
			double ygain = 0;
			xgain = Math.cos(angle) * speed;
			ygain = -Math.sin(angle) * speed;

			return new Point2D.Double(xgain, ygain);
		} else if (p1.x > p2.x) {
			angle = Math.PI;
		} else if (p1.x < p2.x) {
			angle = 0;
		} else if (p1.y > p2.y) {
			angle = Math.PI / 2;
		} else if (p1.y < p2.y) {
			angle = -Math.PI / 2;
		}
		if (accuracy != 0) {
			angle += (random.nextGaussian() * accuracy);
		}
		double xgain = 0;
		double ygain = 0;
		xgain = Math.cos(angle) * speed;
		ygain = -Math.sin(angle) * speed;

		return new Point2D.Double(xgain, ygain);
	}

	/**
	 * gets the next point along a line
	 * 
	 * @param p1
	 *            the first point
	 * @param p2
	 *            the second point
	 * @param speed
	 *            the speed at which to go along the line
	 * @param accuracy
	 *            how closely you stay on this line, set to 0 for perfect
	 *            accuracy
	 * @return the next point along a line
	 */
	public static Point getPoint(Point p1, Point p2, double speed, double accuracy) {
		double angle = 0;
		if (p1.x != p2.x && p1.y != p2.y) {
			double xdif = (p2.getX() - p1.getX());
			double ydif = (p2.getY() - p1.getY());
			angle = 0; // in radians

			angle = -Math.atan(ydif / xdif);
			if (xdif < 0) {
				if (ydif < 0) {
					angle += Math.PI;
				} else {
					angle -= +Math.PI;
				}
			}
			if (accuracy != 0) {
				angle += (random.nextGaussian() * accuracy);
			}
			double xgain = 0;
			double ygain = 0;
			xgain = Math.cos(angle) * speed;
			ygain = -Math.sin(angle) * speed;

			return new Point((int) xgain, (int) ygain);
		} else if (p1.x > p2.x) {
			angle = Math.PI;
		} else if (p1.x < p2.x) {
			angle = 0;
		} else if (p1.y > p2.y) {
			angle = Math.PI / 2;
		} else if (p1.y < p2.y) {
			angle = -Math.PI / 2;
		}
		if (accuracy != 0) {
			angle += (random.nextGaussian() * accuracy);
		}
		double xgain = 0;
		double ygain = 0;
		xgain = Math.cos(angle) * speed;
		ygain = -Math.sin(angle) * speed;

		return new Point((int) xgain, (int) ygain);
	}

	/**
	 * gets the average of two vectors
	 * 
	 * @param p1
	 *            the first vector
	 * @param p2
	 *            the second vector
	 * @return the average of the two vectors
	 */
	public static Point2D.Double averageVector(Point2D.Double p1, Point2D.Double p2) {
		return new Point2D.Double((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}

	/**
	 * converts an angle and distance from the given point into an x and y
	 * coordinate
	 * 
	 * @param angle
	 *            the angle from the point
	 * @param distance
	 *            the distance from the point
	 * @param xCentre
	 *            the xCoord of the point
	 * @param yCentre
	 *            the yCoord of the point
	 * @return the x and y position
	 */
	public static Point2D.Double convertPolarToCartesian(double angle, double distance, double xCentre,
			double yCentre) {
		double x = 0, y = 0;
		while (angle >= 360) {
			angle -= 360;
		}
		while (angle < 0) {
			angle += 360;
		}
		if (angle >= 0 && angle < 90) {
			x = xCentre + distance * Math.cos(angle);
			y = yCentre - distance * Math.sin(angle);
		} else if (angle >= 90 && angle < 180) {
			x = xCentre - distance * Math.sin(angle - 90);
			y = yCentre - distance * Math.cos(angle - 90);
		} else if (angle >= 180 && angle < 270) {
			x = xCentre - distance * Math.cos(angle - 180);
			y = yCentre + distance * Math.sin(angle - 180);
		} else if (angle >= 270 && angle < 360) {
			x = xCentre + distance * Math.sin(angle - 270);
			y = yCentre + distance * Math.cos(angle - 270);
		} else {
			System.out.println("something went wrong while converting polar coordinates to cartesian!");
		}
		return new Point2D.Double(x, y);
	}

	/**
	 * linearly interpolates between 2 doubles
	 * 
	 * @param x0
	 *            the first double
	 * @param x1
	 *            the second double
	 * @param alpha
	 *            the alpha value
	 * @return the interpolated double
	 */
	public static double linearInterpolate(double x0, double x1, double alpha) {
		return x0 * (1 - alpha) + alpha * x1;
	}

	/**
	 * cosine interpolation function
	 * 
	 * @param x0
	 *            the first double
	 * @param x1
	 *            the second double
	 * @param mu
	 *            mu value
	 * @return the interpolated double
	 */
	public static double cosineInterpolate(double x0, double x1, double mu) {
		double mu2;
		mu2 = (1 - Math.cos(mu * Math.PI)) / 2;
		return (x0 * (1 - mu2) + x1 * mu2);
	}

	/**
	 * cubic interpolation function
	 * 
	 * @param x0
	 *            the first double
	 * @param x1
	 *            the second double
	 * @param x2
	 *            the third double
	 * @param x3
	 *            the fourth double
	 * @param mu
	 *            the mu value
	 * @return the interpolated double
	 */
	public static double cubicInterpolate(double x0, double x1, double x2, double x3, double mu) {
		double a0, a1, a2, a3, mu2;
		mu2 = mu * mu;
		a0 = x3 - x2 - x0 + x1;
		a1 = x0 - x1 - a0;
		a2 = x2 - x0;
		a3 = x1;
		return (a0 * mu * mu2 + a1 * mu2 + a2 * mu + a3);
	}

	/**
	 * averages two doubles
	 * 
	 * @param x0
	 *            the first double
	 * @param x1
	 *            the second double
	 * @return the average of the two doubles
	 */
	public static double smooth2(double x0, double x1) {
		return (x0 + x1) / 2;
	}

	/**
	 * gets the average of 3 doubles
	 * 
	 * @param x0
	 * @param x1
	 * @param x2
	 * @return the average
	 */
	public static double smooth3(double x0, double x1, double x2) {
		return (smooth2(x0, x1) + x2) / 2;
	}

	/**
	 * gets the average of 4 doubles
	 * 
	 * @param x0
	 * @param x1
	 * @param x2
	 * @param x3
	 * @return the average
	 */
	public static double smooth4(double x0, double x1, double x2, double x3) {
		return (smooth2(x0, x1) + smooth2(x2, x3)) / 2;
	}

	/**
	 * gets the average of 5 doubles
	 * 
	 * @param x0
	 * @param x1
	 * @param x2
	 * @param x3
	 * @param x4
	 * @return the average
	 */
	public static double smooth5(double x0, double x1, double x2, double x3, double x4) {
		return (smooth3(x0, x1, x2) + smooth2(x3, x4)) / 2;
	}

	/**
	 * gets the average of 6 points
	 * 
	 * @param x0
	 * @param x1
	 * @param x2
	 * @param x3
	 * @param x4
	 * @param x5
	 * @return the average of 6 points
	 */
	public static double smooth6(double x0, double x1, double x2, double x3, double x4, double x5) {
		return (smooth3(x0, x1, x2) + smooth3(x3, x4, x5)) / 2;
	}

	/**
	 * gets the average of 7 points
	 * 
	 * @param x0
	 * @param x1
	 * @param x2
	 * @param x3
	 * @param x4
	 * @param x5
	 * @param x6
	 * @return the average of 7 points
	 */
	public static double smooth7(double x0, double x1, double x2, double x3, double x4, double x5, double x6) {
		return (smooth3(x0, x1, x2) + smooth4(x3, x4, x5, x6)) / 2;
	}

	/**
	 * gets the average of 8 points
	 * 
	 * @param x0
	 * @param x1
	 * @param x2
	 * @param x3
	 * @param x4
	 * @param x5
	 * @param x6
	 * @param x7
	 * @return the average of 8 points
	 */
	public static double smooth8(double x0, double x1, double x2, double x3, double x4, double x5, double x6,
			double x7) {
		return (smooth3(x0, x1, x2) + smooth3(x3, x4, x5) + smooth2(x6, x7)) / 3;
	}

	/**
	 * gets the average of 9 points
	 * 
	 * @param x0
	 * @param x1
	 * @param x2
	 * @param x3
	 * @param x4
	 * @param x5
	 * @param x6
	 * @param x7
	 * @param x8
	 * @return the average of the 9 points
	 */
	public static double smooth9(double x0, double x1, double x2, double x3, double x4, double x5, double x6, double x7,
			double x8) {
		return (smooth3(x0, x1, x2) + smooth3(x3, x4, x5) + smooth3(x6, x7, x8)) / 3;
	}

	/**
	 * gets the smallest int that is higher than the result of a division
	 * 
	 * @param a1
	 *            the first number being divided
	 * @param b1
	 *            the second number being divided
	 * @return rounded integer from the division of a1 and b1
	 */
	public static int ceiling(int a1, int b1) {

		double a2 = a1;
		double b2 = b1;

		double result = a2 / b2;
		if (result % 1 == 0) {
			return (int) result;
		}

		return (int) (result) + 1;

	}

	/**
	 * sets the seed of this classes random
	 * 
	 * @param seed
	 *            the new seed
	 */
	public static void setRandomSeed(long seed) {
		random = new Random(seed);
	}

}
