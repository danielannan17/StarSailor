package galaxy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import handlers.InputHandler;
import handlers.MathHandler;
import messaging.PopUp;

/**
 * contains code for the creation, updating and drawing of trade routes
 * 
 * 
 *
 */
public class TradeRoute {

	private Planet p1, p2;
	private ArrayList<Point2D.Double> points;
	private ArrayList<Boolean> direction;
	private long totalTime = 100000, timeLeft, prevTime;
	private boolean finished = false;

	/**
	 * creates a new trade route between two planets
	 * 
	 * @param p1
	 *            the first planet
	 * @param p2
	 *            the second planet
	 */
	public TradeRoute(Planet p1, Planet p2) {
		points = new ArrayList<Point2D.Double>();
		direction = new ArrayList<Boolean>();
		this.p1 = p1;
		this.p2 = p2;
		prevTime = System.currentTimeMillis();
		timeLeft = totalTime;
		Galaxy.setPopUp(new PopUp(
				"Congratulations!\nYou've created your first trade route, once it's\nfinished you'll be able to purchase this planet."));
	}

	/**
	 * updates the trade routes
	 */
	public void update() {
		if (MathHandler.random.nextFloat() > 0.99) {
			if (MathHandler.random.nextBoolean()) {
				points.add(p2.getCenter());
				direction.add(true);
			} else {
				points.add(p1.getCenter());
				direction.add(false);
			}
		}
		for (int i = 0; i < points.size(); i++) {
			if (direction.get(i)) {
				if (points.get(i).distance(p1.getCenter()) < 3) {
					points.remove(i);
					direction.remove(i);
					i--;
					continue;
				}
				Point2D.Double vector = MathHandler.getPoint2D(points.get(i), p1.getCenter(), 4, 0);
				points.get(i).setLocation(points.get(i).x + vector.x, points.get(i).y + vector.y);
			} else {
				if (points.get(i).distance(p2.getCenter()) < 3) {
					points.remove(i);
					direction.remove(i);
					i--;
					continue;
				}
				Point2D.Double vector = MathHandler.getPoint2D(points.get(i), p2.getCenter(), 4, 0);
				points.get(i).setLocation(points.get(i).x + vector.x, points.get(i).y + vector.y);
			}
		}
		if (timeLeft > 0) {
			timeLeft -= System.currentTimeMillis() - prevTime;
			prevTime = System.currentTimeMillis();
		} else {
			if (!finished) {
				Galaxy.setPopUp(new PopUp("Your trade route between the planets \n" + p1.getName() + " and "
						+ p2.getName() + "has finished! \nHead to " + p2.getName() + " to purchase it."));
			}
			setFinished(true);
		}
	}

	/**
	 * draws the trade routes
	 * 
	 * @param g
	 *            the graphics context used for drawing
	 */
	public void draw(Graphics g) {
		g.setColor(Color.CYAN);
		g.drawLine((int) p1.getCenter().x, (int) p1.getCenter().y, (int) p2.getCenter().x, (int) p2.getCenter().y);
		g.setColor(Color.LIGHT_GRAY);
		for (Point2D.Double p : points) {
			g.fillRect((int) p.x, (int) p.y, 4, 4);
		}
		g.setColor(Color.blue);
		float width = InputHandler.screenSize.width / 30.0f;
		float height = InputHandler.screenSize.height / 80.0f;
		g.fillRect((int) ((int) p2.getCenter().x - width / 2.0), (int) p2.getCenter().y - (int) (2.5 * height),
				(int) width, (int) height);
		g.setColor(Color.green);
		float progress = ((totalTime - timeLeft) * (width / totalTime));
		g.fillRect((int) (p2.getCenter().x - width / 2.0f), (int) p2.getCenter().y - (int) (2.5 * height),
				(int) progress, (int) height);
	}

	/**
	 * moves the trade routes by a vector
	 * 
	 * @param vector
	 *            the vector to use
	 */
	public void moveTradeRoutes(Point2D.Double vector) {
		for (Point2D.Double p : points) {
			p.setLocation(p.x + vector.x, p.y + vector.y);
		}
	}

	/**
	 * checks if this trade route is finished
	 * 
	 * @return whether this trade route is finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * sets whether this trade route is finished
	 * 
	 * @param finished
	 *            boolean whether its finished
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * gets the first planet in this trade route
	 * 
	 * @return the first planet
	 */
	public Planet getP1() {
		return p1;
	}

	/**
	 * sets the first planet in this trade route
	 * 
	 * @param p1
	 *            the new first planet
	 */
	public void setP1(Planet p1) {
		this.p1 = p1;
	}

	/**
	 * gets the second planet in this trade route
	 * 
	 * @return the second planet
	 */
	public Planet getP2() {
		return p2;
	}

	/**
	 * sets the second planet in this trade route
	 * 
	 * @param p2
	 *            the new second planet
	 */
	public void setP2(Planet p2) {
		this.p2 = p2;
	}

}
