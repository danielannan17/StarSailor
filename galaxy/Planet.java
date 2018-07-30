package galaxy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import generators.NameGenerator;
import generators.PerlinNoiseGenerator;
import generators.PlanetGenerator;
import handlers.InputHandler;
import handlers.MathHandler;
import main.Main;
import main.State;

/**
 * contains code for generating, updating and drawing planets
 * 
 * 
 */
public class Planet {

	private PlanetGenerator generator;
	private PerlinNoiseGenerator perlin;
	private Random random;
	private int size, maxSize = 6, minSize = 4, scale = 6, owned = 0;
	private long seed, prevTime;
	private float angle, distance, increment, circle = 360.0f, xDif = 0, yDif = 0, xPos, yPos;
	private boolean selected = false, generated = false, drawn = false, trading = false;
	private String name;
	private int[] values = { 10, 20, 30, 50, 70, 100, 150, 200, 255, 255, 200, 180, 150, 120, 100, 85, 70, 60, 50, 40,
			30, 25, 20, 10, 0 };
	private int index = 0;
	private Point2D.Double position;
	private Rectangle2D.Double bounds;

	/**
	 * creates a new planet based on a seed and the size of the star
	 * 
	 * @param seed
	 *            the seed for this planet, used to generate its terrain
	 * @param starSize
	 *            the size of the star this planet belongs to
	 */
	public Planet(long seed, int starSize, boolean firstEntered) {
		this.seed = seed;
		random = new Random(seed);
		size = random.nextInt(maxSize - minSize) + minSize;
		xPos = size * scale / 2;
		yPos = size * scale / 2;
		angle = random.nextFloat() * circle;
		increment = 2.0f * (random.nextFloat() - 0.5f) / 1000.0f;
		distance = (float) (random.nextFloat() * (InputHandler.screenSize.getHeight() - starSize * scale)
				+ starSize * scale);
		position = MathHandler.convertPolarToCartesian(angle, distance, InputHandler.midPoint.x,
				InputHandler.midPoint.y);
		updateBounds();
		//----------------DB CODE STARTED---------------------//
		generator = new PlanetGenerator(size * scale, size * scale, seed, firstEntered);
		if(Main.USEDB){
			if(!firstEntered){
				generator.addResources();
			}
		}
		//----------------DB CODE ENDED-----------------------//
		name = NameGenerator.generateName(random.nextInt(3) + 2);
		name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
		prevTime = System.currentTimeMillis();
	}

	/**
	 * updates the position of the planet on screen
	 * 
	 * @param time
	 */
	public void update(float time) {
		switch (State.state) {
		case GAME_SOLAR:
			incrementAngle();
			position = MathHandler.convertPolarToCartesian(angle, distance, InputHandler.midPoint.x,
					InputHandler.midPoint.y);
			updateBounds();
			break;
		case GAME_PLANETARY:
			generator.update();
			break;
		default:
			break;
		}
	}

	/**
	 * draws the planet to the screen depending on the current game state
	 * 
	 * @param g
	 *            the graphics context used to draw this planet
	 */
	public void draw(Graphics g, int playerNumber) {
		switch (State.state) {
		case GAME_SOLAR:
			if (owned == playerNumber) {
				g.setColor(new Color(0, 0, 255, 80));
				g.fillOval((int) bounds.x - 10, (int) bounds.y - 10, (int) bounds.width + 20, (int) bounds.height + 20);
			} else if (owned != 0) {
				g.setColor(new Color(255, 0, 0, 80));
				g.fillOval((int) bounds.x - 10, (int) bounds.y - 10, (int) bounds.width + 20, (int) bounds.height + 20);
			}
			if (selected) {
				g.setColor(Color.green);
				g.drawRect((int) bounds.x, (int) bounds.y, (int) bounds.width, (int) bounds.height);
			}
			g.setColor(generator.getColor());
			g.fillOval((int) bounds.x, (int) bounds.y, (int) bounds.width, (int) bounds.height);
			break;
		case GAME_PLANETARY:
			if (System.currentTimeMillis() > prevTime + 200 && drawn == false) {
				if (index < values.length - 1) {
					index++;
				} else {
					drawn = true;
				}
				prevTime = System.currentTimeMillis();
			}
			generator.draw(g, xPos, yPos, Main.ratio);
			if (drawn == false) {
				g.setColor(new Color(0, 0, 0, values[index]));
				g.setFont(new Font("Verdana", Font.PLAIN, 100));
				g.drawString(name, (int) InputHandler.midPoint.x - InputHandler.screenSize.width / 8,
						(int) InputHandler.midPoint.y - InputHandler.screenSize.height / 4);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * draws the orbits of the planets in a solar system
	 * 
	 * @param g
	 *            the graphics context to draw too
	 */
	public void drawOrbits(Graphics g) {
		switch (State.state) {
		case GAME_SOLAR:
			g.setColor(generator.getColor());
			int widthHeight = (int) (distance * 2);
			g.drawOval((int) (InputHandler.midPoint.x + xDif - distance),
					(int) (InputHandler.midPoint.y + yDif - distance), widthHeight, widthHeight);
			break;
		default:
			break;
		}
	}

	/**
	 * checks if this planet has been clicked on
	 * 
	 * @param p
	 *            the point thats been clicked
	 * @param at
	 *            the transformation that is currently being applied to this
	 *            object
	 * @return a boolean representing whether this planet has been clicked
	 */
	public boolean clicked(Point p, AffineTransform at) {
		Shape rect = at.createTransformedShape(bounds);
		return rect.contains(p);
	}

	/**
	 * checks if a resource on a planet was clicked on
	 * 
	 * @param p
	 *            the point thats been clicked
	 */
	public void resourceClicked(Point p) {
		p.x /= Main.ratio;
		p.y /= Main.ratio;
		p.x += xPos;
		p.y += yPos;
		generator.clicked(p);
	}

	/**
	 * sets whether this planet has been selected
	 * 
	 * @param selected
	 *            the new value for whether this planet is selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * increments the angle the planet is at around the star
	 */
	public void incrementAngle() {
		if (angle < 360) {
			angle += increment;
		} else {
			angle = 0;
		}
	}

	/**
	 * moves the planet up or down
	 * 
	 * @param amount
	 *            the amount to move by
	 */
	public void moveUD(double amount) {
		yDif += amount;
		updateBounds();
	}

	/**
	 * moves the planet left or right
	 * 
	 * @param amount
	 *            the amount to move by
	 */
	public void moveLR(double amount) {
		xDif += amount;
		updateBounds();
	}

	/**
	 * updates the bounding rectangle of this planet
	 */
	public void updateBounds() {
		bounds = new Rectangle2D.Double((position.x - (size * scale / 2) + xDif),
				(position.y - (size * scale / 2) + yDif), size * scale, size * scale);
	}

	/**
	 * gets the center of the planet
	 * 
	 * @return the center point of the planet
	 */
	public Point2D.Double getCenter() {
		return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
	}

	/**
	 * generates this planets terrain
	 */
	public void generatePlanet(int playerNumber, int currentStar) {
		perlin = new PerlinNoiseGenerator(seed);
		generator.generatePlanet(perlin.getPerlinNoise(size * scale, size * scale, 4, 5), currentStar);
		generated = true;
		int previousOwner = getOwned();
		setOwned(playerNumber);
		//----------------DB CODE STARTED---------------------//
		if(Main.USEDB){
			if(previousOwner != 0){
				String nameOwner = Main.db.executeQueryS("SELECT Name FROM SAccounts WHERE pNumber = " + previousOwner + ";");
				String myName = Main.db.executeQueryS("SELECT Name FROM SAccounts WHERE pNumber = " + playerNumber + ";");
				Main.db.dbActions("UPDATE Planets SET Name = '" + myName + "' WHERE Name = '" + nameOwner + "';");
				Main.db.dbActions("UPDATE Resources SET Name = '" + myName + "' WHERE Name = '" + nameOwner + "';");
			}
		}
		//----------------DB CODE ENDED-----------------------//
	}

	/**
	 * moves the planet surface
	 * 
	 * @param xAmount
	 *            the amount to move in the x direction
	 * @param yAmount
	 *            the amount to move in the y direction
	 */
	public void movePlanet(float xAmount, float yAmount) {
		float divider = (float) Math.sqrt(Math.pow(xAmount, 2) + Math.pow(yAmount, 2));
		xPos += xAmount / divider;
		yPos += yAmount / divider;
	}

	/**
	 * checks whether this planet has been generated
	 * 
	 * @return a boolean representing whether this planet has been generated
	 */
	public boolean isGenerated() {
		return generated;
	}

	/**
	 * sets whether this planet has been generated
	 * 
	 * @param generated
	 *            the new value for generated
	 */
	public void setGenerated(boolean generated) {
		this.generated = generated;
	}

	/**
	 * gets the bounds of this planet
	 * 
	 * @return the bounds of the planet
	 */
	public Rectangle2D.Double getBounds() {
		return bounds;
	}

	/**
	 * sets the bounds of the planet
	 * 
	 * @param bounds
	 *            the new planet bounds
	 */
	public void setBounds(Rectangle2D.Double bounds) {
		this.bounds = bounds;
	}

	/**
	 * sets who owns this planet
	 * 
	 * @param owned
	 *            a player number that represents who now owns the planet
	 */
	public void setOwned(int owned) {
		this.owned = owned;
	}

	/**
	 * gets who owns this planet
	 * 
	 * @return the player number that owns this planet
	 */
	public int getOwned() {
		return owned;
	}

	/**
	 * gets the number of fighters on this planet
	 * 
	 * @return the number of fighters
	 */
	public int getNumOfFighters() {
		return generator.getNumOfFighters();
	}

	/**
	 * sets the number of fighters on this planet
	 * 
	 * @param numOfFighters
	 *            the new number of fighters
	 */
	public void setNumOfFighters(int numOfFighters) {
		generator.setNumOfFighters(numOfFighters);
	}

	/**
	 * gets the number of carriers on this planet
	 * 
	 * @return the number of carriers
	 */
	public int getNumOfCarriers() {
		return generator.getNumOfCarriers();
	}

	/**
	 * sets the number of carriers on this planet
	 * 
	 * @param numOfCarriers
	 *            the new number of carriers
	 */
	public void setNumOfCarriers(int numOfCarriers) {
		generator.setNumOfCarriers(numOfCarriers);
	}

	/**
	 * gets the number of command ships on this planet
	 * 
	 * @return the number of command ships
	 */
	public int getNumOfCommand() {
		return generator.getNumOfCommand();
	}

	/**
	 * sets the number of command ships on this planet
	 * 
	 * @param numOfCommand
	 *            the new number of command ships
	 */
	public void setNumOfCommand(int numOfCommand) {
		generator.setNumOfCommand(numOfCommand);
	}

	/**
	 * checks if the planet has finished drawing its name
	 * 
	 * @return a boolean representing whether the name has finished drawing
	 */
	public boolean isDrawn() {
		return drawn;
	}

	/**
	 * sets whether this planet has finished drawing its name, also used for
	 * resetting it so that the name will display on return
	 * 
	 * @param drawn
	 *            the new value for drawn
	 */
	public void setDrawn(boolean drawn) {
		this.drawn = drawn;
		index = 0;
	}

	/**
	 * gets the name of this planet
	 * 
	 * @return the planet name as a string
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of this planet
	 * 
	 * @param name
	 *            the new planet name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets this planet's seed
	 * 
	 * @return this planet's seed
	 */
	public long getSeed() {
		return seed;
	}

	/**
	 * sets this planet's seed
	 * 
	 * @param seed
	 *            the new seed
	 */
	public void setSeed(long seed) {
		this.seed = seed;
	}

	/**
	 * gets the angle this planet is at around its star
	 * 
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * sets the angle this planet is at around its star
	 * 
	 * @param angle
	 *            the new angle
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

	/**
	 * checks if this planet is trading
	 * 
	 * @return whether this planet is trading
	 */
	public boolean isTrading() {
		return trading;
	}

	/**
	 * sets whether this planet is trading
	 * 
	 * @param trading
	 *            the new value for trading
	 */
	public void setTrading(boolean trading) {
		this.trading = trading;
	}

	/**
	 * gets the xDif
	 * 
	 * @return the xDif
	 */
	public float getxDif() {
		return xDif;
	}

	/**
	 * gets the yDif
	 * 
	 * @return the yDif
	 */
	public float getyDif() {
		return yDif;
	}

	public float getIncrement() {
		return increment;
	}

	public void setIncrement(float increment) {
		this.increment = increment;
	}

	public float getxPos() {
		return xPos;
	}

	public void setxPos(float xPos) {
		this.xPos = xPos;
	}

	public float getyPos() {
		return yPos;
	}

	public void setyPos(float yPos) {
		this.yPos = yPos;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setxDif(float xDif) {
		this.xDif = xDif;
	}

	public void setyDif(float yDif) {
		this.yDif = yDif;
	}

}
