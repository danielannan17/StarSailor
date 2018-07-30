package generators;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import handlers.ResourceHandler;

/**
 * a block used for planet generation
 * 
 * 
 *
 */
public class Block {

	private Image[] textures;
	private long time, animationWaitTime;
	private int index, width, height, id;
	private boolean solid, liquid = false;
	private static ArrayList<Block> blocks = new ArrayList<Block>();
	private static HashMap<Integer, Block> idMap = new HashMap<Integer, Block>();

	/**
	 * creates a new block
	 * 
	 * @param path
	 *            the path to the image of this block
	 * @param id
	 *            the id of this block
	 * @param animationWaitTime
	 *            the wait time for animated blocks
	 * @param solid
	 *            whether this block is solid
	 * @param liquid
	 *            whether this block is a liquid
	 * @param width
	 *            the width of this block
	 * @param height
	 *            the height of this block
	 */
	public Block(String path, int id, long animationWaitTime, boolean solid, boolean liquid, int width, int height) {
		textures = ResourceHandler.getBlockSprites(path, width, height);
		this.animationWaitTime = animationWaitTime;
		time = System.currentTimeMillis();
		setSolid(solid);
		setLiquid(liquid);
		setWidth(width);
		setHeight(height);
		blocks.add(this);
		setId(id);
	}

	/**
	 * updates all the blocks
	 */
	public static void updateAll() {
		for (Block b : blocks) {
			b.update();
		}
	}

	/**
	 * updates this block
	 */
	public void update() {
		long newTime = System.currentTimeMillis();
		if (newTime > time + animationWaitTime) {
			time = newTime;
			if (index < textures.length - 1) {
				index++;
			} else {
				index = 0;
			}
		}
	}

	/**
	 * draws this block
	 * 
	 * @param g
	 *            the graphics context to draw to
	 * @param x
	 *            the x coordinate to draw it at
	 * @param y
	 *            the y coordinate to draw it at
	 */
	public void draw(Graphics g, int x, int y) {
		g.drawImage(textures[index], x, y, width, height, null);
	}

	/**
	 * draws this block with a scale
	 * 
	 * @param g
	 *            the graphics context to draw to
	 * @param x
	 *            the x coordinate to draw it at
	 * @param y
	 *            the y coordinate to draw it at
	 * @param multiplier
	 *            the value to scale this block by
	 */
	public void draw(Graphics g, int x, int y, int multiplier) {
		g.drawImage(textures[index], x, y, width * multiplier, height * multiplier, null);
	}

	/**
	 * gets a block based on an id
	 * 
	 * @param id
	 *            the id of a block
	 * @return the block this id belongs to
	 */
	public static Block getBlock(int id) {
		return idMap.get(id);
	}

	/**
	 * checks if this block is solid
	 * 
	 * @return whether this block is solid
	 */
	public boolean isSolid() {
		return solid;
	}

	/**
	 * sets whether this block is solid
	 * 
	 * @param solid
	 *            the new value for solid
	 */
	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	/**
	 * checks if this block is liquid
	 * 
	 * @return whether this block is liquid
	 */
	public boolean isLiquid() {
		return liquid;
	}

	/**
	 * sets whether this block is liquid
	 * 
	 * @param liquid
	 *            the new value of liquid
	 */
	public void setLiquid(boolean liquid) {
		this.liquid = liquid;
	}

	/**
	 * gets the width of this block
	 * 
	 * @return the width of this block
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * sets the width of this block
	 * 
	 * @param width
	 *            the new width of this block
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * gets the height of this block
	 * 
	 * @return the height of this block
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * sets the height of this block
	 * 
	 * @param height
	 *            the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * gets the id of this block
	 * 
	 * @return this blocks id
	 */
	public int getId() {
		return id;
	}

	/**
	 * sets the id of this block
	 * 
	 * @param id
	 *            the new block id
	 */
	public void setId(int id) {
		this.id = id;
		idMap.put(id, this);
	}

	/**
	 * gets the array of Images that this uses to be animated
	 * 
	 * @return an array of images
	 */
	public Image[] getTextures() {
		return textures;
	}

	/**
	 * sets the textures used by this block
	 * 
	 * @param textures
	 *            the new textures
	 */
	public void setTextures(Image[] textures) {
		this.textures = textures;
	}

	// grass blocks, ids 0-10
	public static final Block grass_forest = new Block("grass/grass_forest", 0, 0, false, false, 16, 16);
	public static final Block grass_jungle = new Block("grass/grass_jungle", 1, 0, false, false, 16, 16);
	public static final Block grass_mountains = new Block("grass/grass_mountains", 2, 0, false, false, 16, 16);
	public static final Block grass_plains = new Block("grass/grass_plains", 3, 0, false, false, 16, 16);
	public static final Block grass_rainforest = new Block("grass/grass_rainforest", 4, 0, false, false, 16, 16);
	public static final Block grass_savannah = new Block("grass/grass_savannah", 5, 0, false, false, 16, 16);
	public static final Block grass_snowy = new Block("grass/grass_snowy", 6, 0, false, false, 16, 16);
	public static final Block grass_steppe = new Block("grass/grass_steppe", 7, 0, false, false, 16, 16);
	public static final Block grass_tundra = new Block("grass/grass_tundra", 8, 0, false, false, 16, 16);

	// ice, ids 10-15
	public static final Block ice_spikes = new Block("ice/ice_spikes_1", 10, 0, true, false, 16, 16);
	public static final Block ice = new Block("ice/ice_1", 11, 0, false, false, 16, 16);
	public static final Block snow = new Block("ice/snow_1", 12, 0, false, false, 16, 16);

	// sand blocks ids 15-25
	public static final Block sand_arid = new Block("sand/sand_arid", 15, 0, false, false, 16, 16);
	public static final Block sand_beach = new Block("sand/sand_beach", 16, 0, false, false, 16, 16);
	public static final Block sand_cracked = new Block("sand/sand_cracked", 17, 0, false, false, 16, 16);
	public static final Block sand_dunes = new Block("sand/sand_dunes", 18, 0, false, false, 16, 16);
	public static final Block sand = new Block("sand/sand", 19, 0, false, false, 16, 16);

	// stone ids 25-40
	public static final Block clay = new Block("stone/clay", 25, 0, false, false, 16, 16);
	public static final Block rock = new Block("stone/rock", 26, 0, false, false, 16, 16);
	public static final Block rocks = new Block("stone/rocks", 27, 0, false, false, 16, 16);
	public static final Block stone_mossy = new Block("stone/stone_mossy", 28, 0, false, false, 16, 16);
	public static final Block stone_snowy = new Block("stone/stone_snowy", 29, 0, false, false, 16, 16);
	public static final Block stone_solid = new Block("stone/stone_solid", 30, 0, false, false, 16, 16);
	public static final Block stone_volcanic = new Block("stone/stone_volcanic", 31, 0, false, false, 16, 16);
	public static final Block stone = new Block("stone/stone", 32, 0, false, false, 16, 16);

	// tree blocks, ids 40-60
	public static final Block tree_baobab = new Block("trees/tree_baobab", 40, 0, true, false, 16, 16);
	public static final Block tree_birch_1 = new Block("trees/tree_birch_1", 41, 0, true, false, 16, 16);
	public static final Block tree_birch = new Block("trees/tree_birch", 42, 0, true, false, 16, 16);
	public static final Block tree_cactus = new Block("trees/tree_cactus", 43, 0, true, false, 16, 16);
	public static final Block tree_oak_1 = new Block("trees/tree_oak_1", 44, 0, true, false, 16, 16);
	public static final Block tree_oak = new Block("trees/tree_oak", 45, 0, true, false, 16, 16);
	public static final Block tree_palm = new Block("trees/tree_palm", 46, 0, true, false, 16, 16);
	public static final Block tree_pine = new Block("trees/tree_pine", 47, 0, true, false, 16, 16);
	public static final Block tree_rubber = new Block("trees/tree_rubber", 48, 0, true, false, 16, 16);
	public static final Block tree_sequoia = new Block("trees/tree_sequoia", 49, 0, true, false, 16, 16);
	public static final Block tree_shrub_1 = new Block("trees/tree_shrub_1", 50, 0, false, false, 16, 16);
	public static final Block tree_shrub = new Block("trees/tree_shrub", 51, 0, false, false, 16, 16);
	public static final Block tree_spruce = new Block("trees/tree_spruce", 52, 0, true, false, 16, 16);

	// liquids, ids 60-70
	public static final Block lava = new Block("lava/lava", 60, 500, true, true, 16, 16);
	public static final Block water_murky = new Block("water/water_murky", 61, 500, true, true, 16, 16);
	public static final Block water_ocean = new Block("water/water_ocean", 62, 500, true, true, 16, 16);
	public static final Block water_river = new Block("water/water_river", 63, 500, true, true, 16, 16);

	// decoration, ids 70-80
	public static final Block crates = new Block("decoration/crates", 70, 0, true, false, 16, 16);
	public static final Block flower = new Block("decoration/flower", 71, 0, false, false, 16, 16);
	public static final Block flowers = new Block("decoration/flowers", 72, 0, false, false, 16, 16);

	/*
	 * misc public static final Block iron = new Block("ship_interior/iron", 0,
	 * false, 16, 16); public static final Block engine_fire = new
	 * Block("ship_interior/engine_fire_sprites", 200, false, 64, 192); public
	 * static final Block engine = new Block("ship_interior/engine", 0, true,
	 * 16, 16); public static final Block ship_walls = new
	 * Block("ship_interior/ship_walls", 0, true, 16, 16); public static final
	 * Block glass = new Block("ship_interior/glass", 0, true, 16, 16); public
	 * static final Block ship_lights = new Block("ship_interior/ship_lights",
	 * 0, true, 16, 16); public static final Block control_desk = new
	 * Block("ship_interior/control_desk", 0, false, 16, 16); public static
	 * final Block wood = new Block("wood/wood", 0, false, 16, 16); public
	 * static final Block counter = new Block("wood/counter", 0, true, 16, 16);
	 * public static final Block smallship = new
	 * Block("spaceship/smallShipParked", 0, true, 16, 16); public static final
	 * Block bigship = new Block("spaceship/EnemyCommandShip2", 0, true, 16,
	 * 16); public static final Block smallshipf = new
	 * Block("spaceship/smallShipParkedFlip", 0, true, 16, 16); public static
	 * final Block bigshipf = new Block("spaceship/EnemyCommandShip2Flip", 0,
	 * true, 16, 16); public static final Block smallfighter = new
	 * Block("spaceship/smallFighterParked", 0, true, 16, 16); public static
	 * final Block smallfighterf = new Block("spaceship/smallFighterParkedFlip",
	 * 0, true, 16, 16); public static final Block entrance = new
	 * Block("entrance", 0, true, 16, 16);
	 */

}
