package galaxy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import handlers.InputHandler;
import handlers.MathHandler;
import main.State;

/**
 * contains all the code for generating, updating and drawing a star
 * 
 * 
 */
public class Star {

	private int size, numOfPlanets, minPlanets = 4;
	private float distance, angle, increment, xDif = 0, yDif = 0;
	private long seed;
	private Point2D.Double position, solarPos;
	private Rectangle2D.Double bounds;
	private Random random;
	private Color varColor;
	private boolean selected = false, generated = false, owned = false;
	private Color white = new Color(255, 255, 255), yellow = new Color(220, 230, 0), orange = new Color(255, 130, 0),
			red = new Color(200, 15, 15), blue = new Color(100, 150, 245);
	private int maxR, minR, maxG, minG, maxB, minB;
	private int upR = 5, upG = 5, upB = 8;
	private ArrayList<TradeRoute> routes;

	/**
	 * creates a new star based on a seed and the max number of planets
	 * 
	 * @param seed
	 *            the seed for this star to use
	 * @param maxPlanets
	 *            the max number of planets this star can have in orbit
	 */
	public Star(long seed, int maxPlanets) {
		this.seed = seed;
		random = new Random(seed);
		size = random.nextInt(15) + 16;
		distance = random.nextFloat() * InputHandler.screenSize.width * 3;
		angle = random.nextFloat() * 360;
		increment = random.nextFloat() / 10000.0f;
		position = MathHandler.convertPolarToCartesian(angle, distance, InputHandler.midPoint.x,
				InputHandler.midPoint.y);
		solarPos = new Point2D.Double(0, 0);
		updateBounds();
		int change = 50;
		varColor = chooseRandomColor(random.nextInt(15));
		maxR = Math.min(255, varColor.getRed() + change);
		minR = Math.max(0, varColor.getRed() - change);
		maxG = Math.min(255, varColor.getGreen() + change);
		minG = Math.max(0, varColor.getGreen() - change);
		maxB = Math.min(255, varColor.getBlue() + change);
		minB = Math.max(0, varColor.getBlue() - change);
		setNumOfPlanets(random.nextInt(maxPlanets - minPlanets) + minPlanets);
		routes = new ArrayList<TradeRoute>();
	}

	/**
	 * updates the position of this star
	 * 
	 * @param time
	 */
	public void update(float time) {
		switch (State.state) {
		case GAME_GALACTIC:
			incrementAngle();
			position = MathHandler.convertPolarToCartesian(angle, distance, InputHandler.midPoint.x,
					InputHandler.midPoint.y);
			updateBounds();
			break;
		case GAME_SOLAR:

			break;
		default:
			break;
		}
		twinkle();
	}

	/**
	 * updates this star's trade routes
	 */
	public void updateTradeRoutes() {
		for (TradeRoute r : routes) {
			r.update();
		}
	}

	/**
	 * gets whether a planet's trade route has finished
	 * 
	 * @param p
	 *            the planet
	 * @return whether that planet's trade has finished
	 */
	public boolean getTradeFinished(Planet p) {
		for (TradeRoute r : routes) {
			if (r.getP2().equals(p)) {
				return r.isFinished();
			}
		}
		return false;
	}

	/**
	 * draws this star to the current graphics context
	 * 
	 * @param g
	 *            the graphics context used to draw this star
	 */
	public void draw(Graphics g) {
		switch (State.state) {
		case GAME_GALACTIC:
			if (owned) {
				g.setColor(new Color(0, 0, 255, 80));
				g.fillOval((int) bounds.x - 10, (int) bounds.y - 10, (int) bounds.width + 20, (int) bounds.height + 20);
			}
			if (selected) {
				g.setColor(Color.green);
				g.drawRect((int) bounds.x, (int) bounds.y, (int) bounds.width, (int) bounds.height);
			}
			g.setColor(varColor);
			g.fillOval((int) bounds.x, (int) bounds.y, (int) bounds.width, (int) bounds.height);
			break;
		case GAME_SOLAR:
			g.setColor(varColor);
			g.fillOval((int) ((InputHandler.midPoint.x - size * 5) + solarPos.x),
					(int) ((InputHandler.midPoint.y - size * 5) + solarPos.y), size * 10, size * 10);
			for (TradeRoute r : routes) {
				r.draw(g);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * moves the trade routes
	 * 
	 * @param vector
	 *            the vector to move them by
	 */
	public void moveTradeRoutes(Point2D.Double vector) {
		for (TradeRoute t : routes) {
			t.moveTradeRoutes(vector);
		}
	}

	/**
	 * adds a new trade route
	 * 
	 * @param p1
	 *            the first planet
	 * @param p2
	 *            the second planet
	 */
	public void addTradeRoute(Planet p1, Planet p2) {
		routes.add(new TradeRoute(p1, p2));
	}

	/**
	 * removes a planets trade route
	 * 
	 * @param p
	 *            the planet
	 */
	public void removeTradeRoute(Planet p) {
		for (TradeRoute r : routes) {
			if (r.getP2().equals(p)) {
				routes.remove(r);
				break;
			}
		}
	}

	/**
	 * increments the angle this star is at around the center of the galaxy
	 */
	private void incrementAngle() {
		if (angle < 360.0) {
			angle += increment;
		} else {
			angle = 0;
		}
	}

	/**
	 * moves this star up or down
	 * 
	 * @param amount
	 *            the amount to move by
	 */
	public void moveUD(double amount) {
		yDif += amount;
		updateBounds();
	}

	/**
	 * moves this star left or right
	 * 
	 * @param amount
	 *            the amount to move by
	 */
	public void moveLR(double amount) {
		xDif += amount;
		updateBounds();
	}

	/**
	 * updates the bounding rectangle of this star
	 */
	public void updateBounds() {
		bounds = new Rectangle2D.Double((position.x - (size / 2) + xDif), (position.y - (size / 2) + yDif), size, size);
	}

	/**
	 * checks if this star has been clicked on
	 * 
	 * @param p
	 *            the point that has been clicked
	 * @param at
	 *            the transformation that is currently applied to this star
	 * @return whether this star has been clicked
	 */
	public boolean clicked(Point p, AffineTransform at) {
		if (bounds != null && at != null) {
			Shape rect = at.createTransformedShape(bounds);
			return rect.contains(p);
		} else {
			return false;
		}
	}

	/**
	 * chooses a random colour for this star
	 * 
	 * @param c
	 *            an integer that decides the colour
	 * @return the colour of this star
	 */
	private Color chooseRandomColor(int c) {
		if (c >= 0 && c < 6) {
			return white;
		} else if (c >= 6 && c < 8) {
			return yellow;
		} else if (c >= 8 && c < 10) {
			return orange;
		} else if (c >= 10 && c < 14) {
			return red;
		} else {
			return blue;
		}
	}

	/**
	 * twinkles the color of the star
	 */
	private void twinkle() {
		int red = varColor.getRed() + upR;
		if (red >= maxR) {
			upR = -upR;
			red = maxR;
		} else if (red <= minR) {
			upR = -upR;
			red = minR;
		}
		int green = varColor.getGreen() + upG;
		if (green >= maxG) {
			upG = -upG;
			green = maxG;
		} else if (green <= minG) {
			upG = -upG;
			green = minG;
		}
		int blue = varColor.getBlue() + upB;
		if (blue >= maxB) {
			upB = -upB;
			blue = maxB;
		} else if (blue <= minB) {
			upB = -upB;
			blue = minB;
		}
		varColor = new Color(red, green, blue);
	}

	/**
	 * gets the bounding rectangle of this star
	 * 
	 * @return the bounds of this star
	 */
	public Rectangle2D.Double getBounds() {
		return bounds;
	}

	/**
	 * gets the center point of this star
	 * 
	 * @return the center of the star
	 */
	public Point2D.Double getCenter() {
		return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
	}

	/**
	 * sets the bounds of this star
	 * 
	 * @param bounds
	 *            the new bounds of the star
	 */
	public void setBounds(Rectangle2D.Double bounds) {
		this.bounds = bounds;
	}

	/**
	 * sets that this star has been selected
	 * 
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * checks if this star is selected
	 * 
	 * @return whether this star is selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * gets the x difference between the origin of this star and where it is now
	 * 
	 * @return the x difference
	 */
	public double getxDif() {
		return xDif;
	}

	/**
	 * sets the x difference of this star
	 * 
	 * @param xDif
	 *            the new xDif
	 */
	public void setxDif(float xDif) {
		this.xDif = xDif;
	}

	/**
	 * gets the y difference between the origin of this star and where it is now
	 * 
	 * @return the y difference
	 */
	public double getyDif() {
		return yDif;
	}

	/**
	 * sets the y difference of this star
	 * 
	 * @param yDif
	 *            the new yDif
	 */
	public void setyDif(float yDif) {
		this.yDif = yDif;
	}

	/**
	 * gets the number of planets this star has in orbit
	 * 
	 * @return the number of planets in orbit
	 */
	public int getNumOfPlanets() {
		return numOfPlanets;
	}

	/**
	 * sets the number of planets this star has in orbit
	 * 
	 * @param numOfPlanets
	 *            the new number of planets
	 */
	public void setNumOfPlanets(int numOfPlanets) {
		this.numOfPlanets = numOfPlanets;
	}

	/**
	 * checks if this star has been generated
	 * 
	 * @return whether this star has been generated
	 */
	public boolean isGenerated() {
		return generated;
	}

	/**
	 * sets whether this star has been generated
	 * 
	 * @param generated
	 *            the new value of generated
	 */
	public void setGenerated(boolean generated) {
		this.generated = generated;
	}

	/**
	 * gets the size of this star
	 * 
	 * @return the star size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * sets the size of this star
	 * 
	 * @param size
	 *            the new size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * gets the solar position of this star for use in the solar system view
	 * 
	 * @return the solar position of this star
	 */
	public Point2D.Double getSolarPos() {
		return solarPos;
	}

	/**
	 * sets the solar position of this star
	 * 
	 * @param solarPos
	 *            the new solar position
	 */
	public void setSolarPos(Point2D.Double solarPos) {
		this.solarPos = solarPos;
	}

	/**
	 * increments the solar position
	 * 
	 * @param amountx
	 *            the amount to increment along the x axis
	 * @param amounty
	 *            the amount to increment along the y axis
	 */
	public void incrementSolarPos(double amountx, double amounty) {
		solarPos.x += amountx;
		solarPos.y += amounty;
	}

	/**
	 * gets the seed of this star
	 * 
	 * @return the star's seed
	 */
	public long getSeed() {
		return seed;
	}

	/**
	 * sets the seed for this star
	 * 
	 * @param seed
	 *            the new seed
	 */
	public void setSeed(long seed) {
		this.seed = seed;
	}

	/**
	 * checks if this star is owned by the player
	 * 
	 * @return
	 */
	public boolean isOwned() {
		return owned;
	}

	/**
	 * sets the ownership of this star
	 * 
	 * @param owned
	 *            the new value for owned
	 */
	public void setOwned(boolean owned) {
		this.owned = owned;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getIncrement() {
		return increment;
	}

	public void setIncrement(float increment) {
		this.increment = increment;
	}

	public Point2D.Double getPosition() {
		return position;
	}

	public void setPosition(Point2D.Double position) {
		this.position = position;
	}

	public ArrayList<TradeRoute> getRoutes() {
		return routes;
	}

	public void setRoutes(ArrayList<TradeRoute> routes) {
		this.routes = routes;
	}

}
