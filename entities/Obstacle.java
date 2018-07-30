package entities;

import java.awt.image.BufferedImage;

/**
 * Created by adrian_radulescu1997 on 09/02/2017.
 * Represents an obstacle that appears on the map
 */
public class Obstacle extends Entity{

	/**
	 * create an obstacle
	 * @param id the id of the obstacle
	 * @param xLocation the x location of the obstacle
	 * @param yLocation the y location of the obstacle
	 * @param sprite the buffered image of the obstacle
	 */
    public Obstacle(String id, int xLocation, int yLocation, BufferedImage[][] sprite) {
        super(id, xLocation, yLocation, sprite);
    }
}
