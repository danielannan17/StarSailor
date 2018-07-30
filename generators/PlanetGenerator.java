package generators;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import galaxy.Galaxy;
import gameLogic.PlayerStats;
import gameLogic.Resources;
import handlers.InputHandler;
import handlers.ResourceHandler;
import main.Main;
import messaging.PopUp;

/**
 * generates a planet
 * 
 * 
 *
 */
public class PlanetGenerator {

	private Biome biome;
	private Block[][] planet, liquid, decoration;
	private BufferedImage planetImage, liquidImage, decorationImage, resourceImage, guiImage;
	private Image storageImage, waterImage, houseImage, shipImage, moneyImage, upgradeArrow;
	private Image[] factoryImages, nuclearPlantImages, generatorImages;
	private Rectangle metalRect, uranRect, aetherRect, waterRect, storageRect, popRect, ship1Rect, ship2Rect, ship3Rect,
			moneyRect, metalUpgrade, uranUpgrade, aetherUpgrade, waterUpgrade, storageUpgrade, popUpgrade, ship1Upgrade,
			ship2Upgrade, ship3Upgrade, moneyUpgrade;
	private Random random;
	private boolean clickedMetal, clickedUran, clickedAether, clickedWater, clickedStorage, clickedPop, clickedShip1,
			clickedShip2, clickedShip3, clickedMoney;
	private int metalLevel = 1, uranLevel = 1, aetherLevel = 1, waterLevel = 1, storageLevel = 1, popLevel = 1,
			bankLevel = 1, numOfFighters, numOfCarriers, numOfCommand, maxLevel = 20, resourceSize = 64,
			currentFrame = 0, width, height;
	private Color color;
	private long seed, timer = System.currentTimeMillis();
	private Resources metal, uranium, aether, water, storage, population, money, ship1, ship2, ship3;
	private boolean metalMessage = false, uraniumMessage = false, aetherMessage = false, metalUpgraded = false,
			uraniumUpgraded = false, aetherUpgraded = false;
	private boolean storageUpgraded = false, populationUpgraded = false, waterUpgraded = false, ship1Upgraded = false,
			ship2Upgraded = false, ship3Upgraded = false, moneyUpgraded = false;
	private boolean message1 = false;
	
	//DB Variable
	private boolean newPlanet;

	/**
	 * creates a new planet generator
	 * 
	 * @param width
	 *            the width of the planet
	 * @param height
	 *            the height of the planet
	 * @param seed
	 *            the seed to use
	 */
	public PlanetGenerator(int width, int height, long seed, boolean newPlanet) {
		this.seed = seed;
		this.width = width;
		this.height = height;
		this.newPlanet = newPlanet;
		random = new Random(seed);
		float temp = random.nextFloat(), precip = random.nextFloat();
		setBiome(chooseBiome(temp, precip));
		chooseColor();
		metal = new Resources("metal", 1, seed, PlayerStats.METAL);
		uranium = new Resources("uranium", 1, seed, PlayerStats.URANIUM);
		aether = new Resources("aether", 1, seed, PlayerStats.AETHER);
		water = new Resources("water", 1, seed, PlayerStats.WATER);
		storage = new Resources("storage", 1, seed, PlayerStats.STORAGE);
		population = new Resources("population", 1, seed, PlayerStats.POPULATION);
		money = new Resources("money", 1, seed, PlayerStats.MONEY);
		ship1 = new Resources("ship1", 1, seed, 0);
		ship2 = new Resources("ship2", 1, seed, 0);
		ship3 = new Resources("ship3", 1, seed, 0);
		numOfFighters = random.nextInt(20);
		numOfCarriers = random.nextInt(10);
		numOfCommand = random.nextInt(5);
	}

	/**
	 * chooses the biome this planet will use
	 * 
	 * @param temperature
	 *            the temperature of this planet
	 * @param precipitation
	 *            the precipitation of this planet
	 * @return the biome used by this planet
	 */
	public Biome chooseBiome(float temperature, float precipitation) {
		Biome b = Biome.forest;
		if (temperature >= 0 && temperature < 0.2) {
			if (precipitation >= 0 && precipitation < 0.2) {
				b = Biome.polar_desert;
			} else if (precipitation >= 0.2 && precipitation < 0.4) {
				b = Biome.ice_spikes;
			} else if (precipitation >= 0.4 && precipitation < 0.6) {
				b = Biome.frozen_lakes;
			} else if (precipitation >= 0.6 && precipitation < 0.8) {
				b = Biome.ice_sheet;
			} else if (precipitation >= 0.8 && precipitation <= 1.0) {
				b = Biome.ice_bergs;
			}
		} else if (temperature >= 0.2 && temperature < 0.4) {
			if (precipitation >= 0 && precipitation < 0.2) {
				b = Biome.tundra;
			} else if (precipitation >= 0.2 && precipitation < 0.4) {
				b = Biome.mountain;
			} else if (precipitation >= 0.4 && precipitation < 0.6) {
				b = Biome.taiga;
			} else if (precipitation >= 0.6 && precipitation < 0.8) {
				b = Biome.mountain_forest;
			} else if (precipitation >= 0.8 && precipitation <= 1.0) {
				b = Biome.ocean;
			}
		} else if (temperature >= 0.4 && temperature < 0.6) {
			if (precipitation >= 0 && precipitation < 0.2) {
				b = Biome.steppe;
			} else if (precipitation >= 0.2 && precipitation < 0.4) {
				b = Biome.plains;
			} else if (precipitation >= 0.4 && precipitation < 0.6) {
				b = Biome.forest;
			} else if (precipitation >= 0.6 && precipitation < 0.8) {
				b = Biome.lakes;
			} else if (precipitation >= 0.8 && precipitation <= 1.0) {
				b = Biome.islands;
			}
		} else if (temperature >= 0.6 && temperature < 0.8) {
			if (precipitation >= 0 && precipitation < 0.2) {
				b = Biome.desert_plains;
			} else if (precipitation >= 0.2 && precipitation < 0.4) {
				b = Biome.canyon;
			} else if (precipitation >= 0.4 && precipitation < 0.6) {
				b = Biome.savannah;
			} else if (precipitation >= 0.6 && precipitation < 0.8) {
				b = Biome.jungle;
			} else if (precipitation >= 0.8 && precipitation <= 1.0) {
				b = Biome.rainforest;
			}
		} else if (temperature >= 0.8 && temperature <= 1.0) {
			if (precipitation >= 0 && precipitation < 0.2) {
				b = Biome.lava_ocean;
			} else if (precipitation >= 0.2 && precipitation < 0.4) {
				b = Biome.lava_islands;
			} else if (precipitation >= 0.4 && precipitation < 0.6) {
				b = Biome.lava_lakes;
			} else if (precipitation >= 0.6 && precipitation < 0.8) {
				b = Biome.igneous_desert;
			} else if (precipitation >= 0.8 && precipitation <= 1.0) {
				b = Biome.volcanic_mountains;
			}
		}
		return b;
	}

	/**
	 * choose the colour this planet will display as
	 */
	public void chooseColor() {
		BufferedImage img = (BufferedImage) biome.getBiomeParts().get(0).getBlock().getTextures()[0];
		int r = 0, g = 0, b = 0;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				r += new Color(img.getRGB(i, j)).getRed();
				g += new Color(img.getRGB(i, j)).getGreen();
				b += new Color(img.getRGB(i, j)).getBlue();
			}
		}
		int numOfPixels = (img.getWidth() * img.getHeight());
		r /= numOfPixels;
		g /= numOfPixels;
		b /= numOfPixels;
		this.setColor(new Color(r, g, b));
	}

	/**
	 * generates this planet using some noise
	 * 
	 * @param noise
	 */
	public void generatePlanet(float[][] noise, int currentStar) {
		
		//----------------DB CODE STARTED---------------------//
		if(Main.USEDB){
			if(Main.db.executeQueryInt("SELECT PSeed FROM Planets WHERE PSeed = " + seed + ";", "PSeed")!=0 && newPlanet){
				addResources();
			}
			if(newPlanet && Main.db.executeQueryInt("SELECT PSeed FROM Planets WHERE PSeed = " + seed + ";", "PSeed") == 0){
				Main.pStats.addResource(metal);
				Main.pStats.addResource(uranium);
				Main.pStats.addResource(aether);
				Main.pStats.addResource(water);
				Main.pStats.addResource(storage);
				Main.pStats.addResource(population);
				Main.pStats.addResource(money);
				Main.pStats.addResource(ship1);
				Main.pStats.addResource(ship2);
				Main.pStats.addResource(ship3);
				Main.db.dbActions("INSERT INTO Planets Values (" + this.seed + "," + currentStar + ",'" + Main.PNAME + "'," + PlayerStats.METAL + "," + PlayerStats.URANIUM + "," + PlayerStats.AETHER + "," + PlayerStats.WATER + "," + PlayerStats.STORAGE + "," + PlayerStats.MONEY + "," + "0,0,0);");
				Main.db.dbActions("INSERT INTO Resources Values (" + this.seed + "," + currentStar + ",'" + Main.PNAME + "',1,1,1,1,1,1,1);");
				this.newPlanet = false;
			}
		}else{
			Main.pStats.addResource(metal);
			Main.pStats.addResource(uranium);
			Main.pStats.addResource(aether);
			Main.pStats.addResource(water);
			Main.pStats.addResource(storage);
			Main.pStats.addResource(population);
			Main.pStats.addResource(money);
			Main.pStats.addResource(ship1);
			Main.pStats.addResource(ship2);
			Main.pStats.addResource(ship3);
		}
		//----------------DB CODE ENDED-----------------------//
		
		this.setNumOfFighters(0);
		this.setNumOfCarriers(0);
		this.setNumOfCommand(0);
		Galaxy.sendShips();
		storageImage = ResourceHandler.getImage("decoration/crates_trans");
		waterImage = ResourceHandler.getImage("decoration/water_tower");
		houseImage = ResourceHandler.getImage("decoration/house");
		shipImage = ResourceHandler.getImage("decoration/ship_dock");
		moneyImage = ResourceHandler.getImage("decoration/nasa");
		upgradeArrow = ResourceHandler.getImage("gui/upgrade_arrow");
		factoryImages = ResourceHandler.getBlockSprites("decoration/factory_animated", 32, 32);
		nuclearPlantImages = ResourceHandler.getBlockSprites("decoration/nuclear_plant_animated", 32, 32);
		generatorImages = ResourceHandler.getBlockSprites("decoration/generator_animated", 32, 32);
		planet = new Block[width][height];
		decoration = new Block[width][height];
		liquid = new Block[width][height];
		planetImage = new BufferedImage(width * 16, height * 16, BufferedImage.TYPE_INT_ARGB);
		liquidImage = new BufferedImage(width * 16, height * 16, BufferedImage.TYPE_INT_ARGB);
		decorationImage = new BufferedImage(width * 16, height * 16, BufferedImage.TYPE_INT_ARGB);
		resourceImage = new BufferedImage(width * 16, height * 16, BufferedImage.TYPE_INT_ARGB);
		Graphics planetGraphics = planetImage.getGraphics();
		Graphics decorationGraphics = decorationImage.getGraphics();
		for (int i = 0; i < noise.length; i++) {
			for (int j = 0; j < noise[0].length; j++) {
				for (BiomePart part : biome.getBiomeParts()) {
					if (part.getStart() <= noise[i][j] && part.getEnd() >= noise[i][j]
							&& part.getChance() >= random.nextFloat()) {
						planet[i][j] = part.getBlock();
						if (part.getBlock().isLiquid()) {
							liquid[i][j] = part.getBlock();
						}else{
							liquid[i][j] = null;
						}
					}
				}
				for (BiomePart part : biome.getDecoParts()) {
					if (part.getStart() <= noise[i][j] && part.getEnd() >= noise[i][j]
							&& part.getChance() >= random.nextFloat()) {
						decoration[i][j] = part.getBlock();
					}
				}
				if (planet[i][j] != null) {
					planet[i][j].draw(planetGraphics, i * planet[i][j].getWidth(), j * planet[i][j].getHeight());
				}
				if (decoration[i][j] != null) {
					decoration[i][j].draw(decorationGraphics, i * decoration[i][j].getWidth(),
							j * decoration[i][j].getHeight());
				}
			}
		}
		ship1Rect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize * 3),
				random.nextInt(noise[0].length * 16 - resourceSize * 3), resourceSize, resourceSize);
		ship1Upgrade = new Rectangle(ship1Rect.x + ((2 * ship1Rect.width) / 3),
				ship1Rect.y + (2 * ship1Rect.height / 5), ship1Rect.width / 4, ship1Rect.height / 4);
		ship2Rect = new Rectangle(ship1Rect.x + ship1Rect.width, ship1Rect.y, resourceSize, resourceSize);
		ship2Upgrade = new Rectangle(ship2Rect.x + ((2 * ship2Rect.width) / 3),
				ship2Rect.y + (2 * ship2Rect.height / 5), ship2Rect.width / 4, ship2Rect.height / 4);
		ship3Rect = new Rectangle(ship2Rect.x + resourceSize, ship2Rect.y, resourceSize, resourceSize);
		ship3Upgrade = new Rectangle(ship3Rect.x + ((2 * ship3Rect.width) / 3),
				ship3Rect.y + (2 * ship3Rect.height / 5), ship3Rect.width / 4, ship3Rect.height / 4);
		metalRect = new Rectangle(random.nextInt((noise.length * 16) - resourceSize),
				random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		while (metalRect.intersects(ship1Rect) || metalRect.intersects(ship2Rect) || metalRect.intersects(ship3Rect)) {
			metalRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
					random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		}
		metalUpgrade = new Rectangle(metalRect.x + ((2 * metalRect.width) / 3),
				metalRect.y + (2 * metalRect.height / 5), metalRect.width / 4, metalRect.height / 4);
		uranRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
				random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		while (uranRect.intersects(metalRect) || uranRect.intersects(ship1Rect) || uranRect.intersects(ship2Rect)
				|| uranRect.intersects(ship3Rect)) {
			uranRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
					random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		}
		uranUpgrade = new Rectangle(uranRect.x + ((2 * uranRect.width) / 3), uranRect.y + (2 * uranRect.height / 5),
				uranRect.width / 4, uranRect.height / 4);
		aetherRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
				random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		while (aetherRect.intersects(metalRect) || aetherRect.intersects(uranRect) || aetherRect.intersects(ship1Rect)
				|| aetherRect.intersects(ship2Rect) || aetherRect.intersects(ship3Rect)) {
			aetherRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
					random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		}
		aetherUpgrade = new Rectangle(aetherRect.x + ((2 * aetherRect.width) / 3),
				aetherRect.y + (2 * aetherRect.height / 5), aetherRect.width / 4, aetherRect.height / 4);
		storageRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
				random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		while (storageRect.intersects(metalRect) || storageRect.intersects(uranRect)
				|| storageRect.intersects(aetherRect) || storageRect.intersects(ship1Rect)
				|| storageRect.intersects(ship2Rect) || storageRect.intersects(ship3Rect)) {
			storageRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
					random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		}
		storageUpgrade = new Rectangle(storageRect.x + ((2 * storageRect.width) / 3),
				storageRect.y + (2 * storageRect.height / 5), storageRect.width / 4, storageRect.height / 4);
		popRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
				random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		while (popRect.intersects(metalRect) || popRect.intersects(uranRect) || popRect.intersects(aetherRect)
				|| popRect.intersects(storageRect) || popRect.intersects(ship1Rect) || popRect.intersects(ship2Rect)
				|| popRect.intersects(ship3Rect)) {
			popRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
					random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		}
		popUpgrade = new Rectangle(popRect.x + ((2 * popRect.width) / 3), popRect.y + (2 * popRect.height / 5),
				popRect.width / 4, popRect.height / 4);
		waterRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
				random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		while (waterRect.intersects(metalRect) || waterRect.intersects(uranRect) || waterRect.intersects(aetherRect)
				|| waterRect.intersects(storageRect) || waterRect.intersects(popRect) || waterRect.intersects(ship1Rect)
				|| waterRect.intersects(ship2Rect) || waterRect.intersects(ship3Rect)) {
			waterRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
					random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		}
		waterUpgrade = new Rectangle(waterRect.x + ((2 * waterRect.width) / 3),
				waterRect.y + (2 * waterRect.height / 5), waterRect.width / 4, waterRect.height / 4);
		moneyRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
				random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		while (moneyRect.intersects(metalRect) || moneyRect.intersects(uranRect) || moneyRect.intersects(aetherRect)
				|| moneyRect.intersects(storageRect) || moneyRect.intersects(popRect) || moneyRect.intersects(waterRect)
				|| moneyRect.intersects(ship1Rect) || moneyRect.intersects(ship1Rect) || moneyRect.intersects(ship2Rect)
				|| moneyRect.intersects(ship3Rect)) {
			moneyRect = new Rectangle(random.nextInt(noise.length * 16 - resourceSize),
					random.nextInt(noise[0].length * 16 - resourceSize), resourceSize, resourceSize);
		}
		moneyUpgrade = new Rectangle(moneyRect.x + ((2 * moneyRect.width) / 3),
				moneyRect.y + (2 * moneyRect.height / 5), moneyRect.width / 4, moneyRect.height / 4);
	}

	/**
	 * updates this planet
	 */
	public void update() {
		if (upgradedChecker1() && Main.pStats.getNumberOfPlanets() == 1 && !message1) {
			Galaxy.setPopUp(new PopUp(
					"Congratulations!\nSeems like you found all the resources available:\nmetal, uranium, aether, storage, water, bank,\npopulation and you upgraded them to next level.\nYou also have ships of each type. Good job!\nRemember: maximum level for each resource is\n20, while ships are limited by population!"));
			message1 = true;
		} else if (upgradedChecker2() && Main.pStats.getNumberOfPlanets() == 1 && !message1) {
			Galaxy.setPopUp(new PopUp(
					"Congratulations!\nSeems like you found all the resources available:\nmetal, uranium, aether, storage, water, bank,\npopulation and you upgraded them to next level.\nYou also have ships, but not fighters. Build them!\nRemember: maximum level for each resource is\n20, while ships are limited by population!"));
			message1 = true;
		} else if (upgradedChecker3() && Main.pStats.getNumberOfPlanets() == 1 && !message1) {
			Galaxy.setPopUp(new PopUp(
					"Congratulations!\nSeems like you found all the resources available:\nmetal, uranium, aether, storage, water, bank,\npopulation and you upgraded them to next level.\nYou also have ships, but not carriers. Build them!\nRemember: maximum level for each resource is\n20, while ships are limited by population!"));
			message1 = true;
		} else if (upgradedChecker4() && Main.pStats.getNumberOfPlanets() == 1 && !message1) {
			Galaxy.setPopUp(new PopUp(
					"Congratulations!\nSeems like you found all the resources available:\nmetal, uranium, aether, storage, water, bank,\npopulation and you upgraded them to next level.\nYou also have ships, but not commands. Build them!\nRemember: maximum level for each resource is\n20, while ships are limited by population!"));
			message1 = true;
		} else if (upgradedChecker5() && Main.pStats.getNumberOfPlanets() == 1 && !message1) {
			Galaxy.setPopUp(new PopUp(
					"Congratulations!\nSeems like you found all the resources available:\nmetal, uranium, aether, storage, water, bank,\npopulation and you upgraded them to next level.\nYou also have ships, but only commands. Try all types!\nRemember: maximum level for each resource is\n20, while ships are limited by population!"));
			message1 = true;
		} else if (upgradedChecker6() && Main.pStats.getNumberOfPlanets() == 1 && !message1) {
			Galaxy.setPopUp(new PopUp(
					"Congratulations!\nSeems like you found all the resources available:\nmetal, uranium, aether, storage, water, bank,\npopulation and you upgraded them to next level.\nYou also have ships, but only carriers. Try all types!\nRemember: maximum level for each resource is\n20, while ships are limited by population!"));
			message1 = true;
		} else if (upgradedChecker7() && Main.pStats.getNumberOfPlanets() == 1 && !message1) {
			Galaxy.setPopUp(new PopUp(
					"Congratulations!\nSeems like you found all the resources available:\nmetal, uranium, aether, storage, water, bank,\npopulation and you upgraded them to next level.\nYou also have ships, but only fighters. Try all types!\nRemember: maximum level for each resource is\n20, while ships are limited by population!"));
			message1 = true;
		} else if (upgradedChecker8() && Main.pStats.getNumberOfPlanets() == 1 && !message1) {
			Galaxy.setPopUp(new PopUp(
					"Congratulations!\nSeems like you found all the resources available:\nmetal, uranium, aether, storage, water, bank,\npopulation and you upgraded them to next level.\nYou don't have any ships yet.Build some!\nRemember: maximum level for each resource is\n20, while ships are limited by population!"));
			message1 = true;
		}
		Graphics2D liquidGraphics = (Graphics2D) liquidImage.getGraphics();
		for (int i = 0; i < liquid.length; i++) {
			for (int j = 0; j < liquid[0].length; j++) {
				if (liquid[i][j] != null) {
					liquid[i][j].draw(liquidGraphics, i * liquid[i][j].getWidth(), j * liquid[i][j].getHeight());
				}
			}
		}
		if (System.currentTimeMillis() > timer) {
			currentFrame = ++currentFrame % 3;
			timer = System.currentTimeMillis() + 1000;
		}
		resourceImage = new BufferedImage(width * 16, height * 16, BufferedImage.TYPE_INT_ARGB);
		Graphics resourceGraphics = resourceImage.getGraphics();
		resourceGraphics.drawImage(factoryImages[currentFrame], metalRect.x, metalRect.y, metalRect.width,
				metalRect.height, null);
		resourceGraphics.drawImage(nuclearPlantImages[currentFrame], uranRect.x, uranRect.y, uranRect.width,
				uranRect.height, null);
		resourceGraphics.drawImage(generatorImages[currentFrame], aetherRect.x, aetherRect.y, aetherRect.width,
				aetherRect.height, null);
		resourceGraphics.drawImage(waterImage, waterRect.x, waterRect.y, waterRect.width, waterRect.height, null);
		resourceGraphics.drawImage(storageImage, storageRect.x, storageRect.y, storageRect.width, storageRect.height,
				null);
		resourceGraphics.drawImage(houseImage, popRect.x, popRect.y, popRect.width, popRect.height, null);
		resourceGraphics.drawImage(shipImage, ship1Rect.x, ship1Rect.y, ship1Rect.width, ship1Rect.height, null);
		resourceGraphics.drawImage(shipImage, ship2Rect.x, ship2Rect.y, ship2Rect.width, ship2Rect.height, null);
		resourceGraphics.drawImage(shipImage, ship3Rect.x, ship3Rect.y, ship3Rect.width, ship3Rect.height, null);
		resourceGraphics.drawImage(moneyImage, moneyRect.x, moneyRect.y, moneyRect.width, moneyRect.height, null);
	}

	/**
	 * draws this planet to the screen
	 * 
	 * @param g
	 *            the graphics context to use
	 * @param x
	 *            the x coord
	 * @param y
	 *            the y coord
	 * @param ratio
	 *            the ratio
	 */
	public void draw(Graphics g, float x, float y, float ratio) {
		int width = InputHandler.screenSize.width;
		int height = InputHandler.screenSize.height;
		g.drawImage(planetImage, 0, 0, (int) (width), (int) (height), (int) x, (int) y, (int) ((width / ratio) + x),
				(int) ((height / ratio) + y), null);
		g.drawImage(liquidImage, 0, 0, (int) (width), (int) (height), (int) x, (int) y, (int) ((width / ratio) + x),
				(int) ((height / ratio) + y), null);
		g.drawImage(decorationImage, 0, 0, (int) (width), (int) (height), (int) x, (int) y, (int) ((width / ratio) + x),
				(int) ((height / ratio) + y), null);
		g.drawImage(resourceImage, 0, 0, (int) (width), (int) (height), (int) x, (int) y, (int) ((width / ratio) + x),
				(int) ((height / ratio) + y), null);
		g.drawImage(guiImage, 0, 0, (int) (width), (int) (height), (int) x, (int) y, (int) ((width / ratio) + x),
				(int) ((height / ratio) + y), null);
		// g.setColor(new Color(0, 0, 0, 150));
		// int xPos = 7 * InputHandler.screenSize.width / 8, yPos =
		// InputHandler.screenSize.height / 20;
		// width = InputHandler.screenSize.width / 6;
		// height = InputHandler.screenSize.height / 3;
		// long metalDebt = Main.pStats.getResourceOnPlanet(PlayerStats.METALID,
		// seed) > 0 ? 0
		// : (-1) * Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed);
		// long uraniumDebt =
		// Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed) > 0 ? 0
		// : (-1) * Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID,
		// seed);
		// long aetherDebt =
		// Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed) > 0 ? 0
		// : (-1) * Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed);
		// g.fillRect(xPos, yPos, width, height);
		// g.setColor(Color.white);
		// g.setFont(new Font("Verdana", Font.PLAIN,
		// InputHandler.screenSize.width / 100));
		// yPos += height / 10;
		// g.drawString("Metal: " + metal.getAmountPerHour() + "/update", xPos,
		// yPos);
		// yPos += height / 10;
		// g.drawString("Uranium: " + uranium.getAmountPerHour() + "/update",
		// xPos, yPos);
		// yPos += height / 10;
		// g.drawString("Aether: " + aether.getAmountPerHour() + "/update",
		// xPos, yPos);
		// yPos += height / 10;
		// g.drawString("DEBTS: ", xPos, yPos);
		// yPos += height / 10;
		// g.drawString("Metal: " + metalDebt, xPos, yPos);
		// yPos += height / 10;
		// g.drawString("Uranium: " + uraniumDebt, xPos, yPos);
		// yPos += height / 10;
		// g.drawString("Aether: " + aetherDebt, xPos, yPos);
		// yPos += height / 10;
		// g.drawString("Pay Debts?", xPos, yPos);
		// yPos += height / 10;
		// g.drawString("(Money - " + Main.pStats.getAmountToPayDebt(metalDebt,
		// uraniumDebt, aetherDebt) + " )", xPos,
		// yPos);
		// yPos -= height / 8;
		// payDebts = new Rectangle(xPos + width / 2, yPos, width / 5, height /
		// 6);
		// g.setColor(Color.cyan);
		// g.fillRect(payDebts.x, payDebts.y, payDebts.width, payDebts.height);
	}

	/**
	 * gets the biome
	 * 
	 * @return the biome
	 */
	public Biome getBiome() {
		return biome;
	}

	/**
	 * sets the biome
	 * 
	 * @param biome
	 *            the new biome
	 */
	public void setBiome(Biome biome) {
		this.biome = biome;
	}

	/**
	 * gets the color
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * sets the color
	 * 
	 * @param color
	 *            the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * checks if anything on this planet was clicked on
	 * 
	 * @param p
	 *            the point the mouse is at
	 */
	public void clicked(Point p) {
		guiImage = new BufferedImage(width * 16, height * 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) guiImage.getGraphics();
		g2d.setColor(new Color(0, 0, 0, 150));
		if (metalRect.contains(p)) {
			if (!metalMessage && !uraniumMessage && !aetherMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("You found the metal! \nClick on the arrow to upgrade the resource.\n"));
				metalMessage = true;
			} else if (!metalMessage && !uraniumMessage && aetherMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp(
						"Congratulation! You also found the metal! \nClick on the arrow to upgrade the resource.\n"));
				metalMessage = true;
			} else if (!metalMessage && uraniumMessage && !aetherMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp(
						"Congratulation! You also found the metal! \nClick on the arrow to upgrade the resource.\n"));
				metalMessage = true;
			} else if (!metalMessage && uraniumMessage && aetherMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp(
						"Congratulation! You found the last resource, metal! \nClick on the arrow to upgrade the resource.\n"));
				metalMessage = true;
			}

			if (clickedMetal && metalUpgrade.contains(p) && !metalUpgraded && !uraniumMessage && !aetherMessage
					&& Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded metal to level " + (metalLevel + 1)
						+ ".\nNow you should go and find Uranium and Aether!"));
				metalUpgraded = true;
			} else if (clickedMetal && metalUpgrade.contains(p) && !metalUpgraded && !uraniumMessage && aetherMessage
					&& Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded metal to level " + (metalLevel + 1)
						+ ".\nNow you should go and find Uranium mine!"));
				metalUpgraded = true;
			} else if (clickedMetal && metalUpgrade.contains(p) && !metalUpgraded && uraniumMessage && !aetherMessage
					&& Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded metal to level " + (metalLevel + 1)
						+ ".\nNow you should go and find Aether mine!"));
				metalUpgraded = true;
			} else if (clickedMetal && metalUpgrade.contains(p) && !metalUpgraded && uraniumMessage && aetherMessage
					&& uraniumUpgraded && aetherUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded metal to level " + (metalLevel + 1)
						+ ".\nNow you have metal,uranium and aether upgraded!\nYou got it now!\nIt's time for you to explore all the planet\nYou'll find more amazing things!"));
				metalUpgraded = true;
			} else if (clickedMetal && metalUpgrade.contains(p) && !metalUpgraded && uraniumMessage && aetherMessage
					&& !uraniumUpgraded && aetherUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded metal to level " + (metalLevel + 1)
						+ ".\nNow you have metal and aether upgraded!\nDon't forget to upgrade uranium!\nGo to the uranium mine and click the upgrade arrow.\n"));
				metalUpgraded = true;
			} else if (clickedMetal && metalUpgrade.contains(p) && !metalUpgraded && uraniumMessage && aetherMessage
					&& uraniumUpgraded && !aetherUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded metal to level " + (metalLevel + 1)
						+ ".\nNow you have metal and uranium upgraded!\nDon't forget to upgrade aether!\nGo to the aether mine and click the upgrade arrow.\n"));
				metalUpgraded = true;
			} else if (clickedMetal && metalUpgrade.contains(p) && !metalUpgraded && uraniumMessage && aetherMessage
					&& !uraniumUpgraded && !aetherUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded metal to level " + (metalLevel + 1)
						+ ".\nNow you have metal upgraded!\nDon't forget to upgrade uranium and aether!\nGo to their mine and click the upgrade arrow.\n"));
				metalUpgraded = true;
			}
			if (clickedMetal) {
				if (metalUpgrade.contains(p) && metalLevel < maxLevel
						&& Main.pStats.verifyAvailableResources("metal", metalLevel, seed)) {
					Main.pStats.setResourceOnPlanet(PlayerStats.METALID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed)
									- Main.pStats.getMetalRequiredToUpgradeMetal(metalLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.URANIUMID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed)
									- Main.pStats.getUraniumRequiredToUpgradeMetal(metalLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.AETHERID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed)
									- Main.pStats.getAetherRequiredToUpgradeMetal(metalLevel));
					Main.pStats
							.setMetal(Main.pStats.getMetal() - Main.pStats.getMetalRequiredToUpgradeMetal(metalLevel));

					Main.pStats.setUranium(
							Main.pStats.getUranium() - Main.pStats.getUraniumRequiredToUpgradeMetal(metalLevel));
					Main.pStats.setAether(
							Main.pStats.getAether() - Main.pStats.getAetherRequiredToUpgradeMetal(metalLevel));
					metalLevel++;
					Main.pStats.getResource("metal", seed).upgradeResources(metalLevel);
				}
			}
			clickedMetal = true;
			g2d.fill(metalRect);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Verdana", Font.PLAIN, 8));
			g2d.drawString("METAL", metalRect.x + metalRect.width / 20, metalRect.y + 2 * metalRect.height / 16);
			g2d.drawString("Level: " + metalLevel, metalRect.x + metalRect.width / 20,
					metalRect.y + 4 * metalRect.height / 16);
			g2d.drawString("Amount: " + metal.getAmountPerHour(), metalRect.x + metalRect.width / 20,
					metalRect.y + 6 * metalRect.height / 16);
			if (metalLevel < maxLevel) {
				g2d.drawString("Upgrade: ", metalRect.x + metalRect.width / 20,
						metalRect.y + 8 * metalRect.height / 16);
				g2d.drawImage(upgradeArrow, metalUpgrade.x, metalUpgrade.y, metalUpgrade.width, metalUpgrade.height,
						null);
				g2d.drawString("Cost: ", metalRect.x + metalRect.width / 20, metalRect.y + 10 * metalRect.height / 16);
				g2d.drawString(
						"M:" + Main.pStats.getMetalRequiredToUpgradeMetal(metalLevel) + " U:"
								+ Main.pStats.getUraniumRequiredToUpgradeMetal(metalLevel),
						metalRect.x + metalRect.width / 20, metalRect.y + 12 * metalRect.height / 16);
				g2d.drawString(
						"A:" + Main.pStats.getAetherRequiredToUpgradeMetal(metalLevel) + " P:"
								+ Main.pStats.getPopulationRequiredToUpgradeMetal(metalLevel),
						metalRect.x + metalRect.width / 20, metalRect.y + 14 * metalRect.height / 16);
			}
			if (Main.pStats.getNumberOfPlanets() < 5) {
				g2d.drawString("Total:" + Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed),
						metalRect.x + metalRect.width / 20, metalRect.y + 16 * metalRect.height / 16);
			}
		} else {
			clickedMetal = false;
		}
		if (uranRect.contains(p)) {
			if (!uraniumMessage && !metalMessage && !aetherMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("You found the uranium! \nClick on the arrow to upgrade the resource.\n"));
				uraniumMessage = true;
			} else if (!uraniumMessage && !metalMessage && aetherMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp(
						"Congratulation! You also found the uranium! \nClick on the arrow to upgrade the resource.\n"));
				uraniumMessage = true;
			} else if (!uraniumMessage && metalMessage && !aetherMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp(
						"Congratulation! You also found the uranium! \nClick on the arrow to upgrade the resource.\n"));
				uraniumMessage = true;
			} else if (!uraniumMessage && metalMessage && aetherMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp(
						"Congratulation! You found the last resource, uranium! \nClick on the arrow to upgrade the resource.\n"));
				uraniumMessage = true;
			}

			if (clickedUran && uranUpgrade.contains(p) && !uraniumUpgraded && !metalMessage && !aetherMessage
					&& Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded uranium to level " + (uranLevel + 1)
						+ ".\nNow you should go and find Metal and Aether!"));
				uraniumUpgraded = true;
			} else if (clickedUran && uranUpgrade.contains(p) && !uraniumUpgraded && !metalMessage && aetherMessage
					&& Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded uranium to level " + (uranLevel + 1)
						+ ".\nNow you should go and find Metal mine!"));
				uraniumUpgraded = true;
			} else if (clickedUran && uranUpgrade.contains(p) && !uraniumUpgraded && metalMessage && !aetherMessage
					&& Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded uranium to level " + (uranLevel + 1)
						+ ".\nNow you should go and find Aether mine!"));
				uraniumUpgraded = true;
			} else if (clickedUran && uranUpgrade.contains(p) && !uraniumUpgraded && metalMessage && aetherMessage
					&& metalUpgraded && aetherUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded uranium to level " + (uranLevel + 1)
						+ ".\nNow you have metal,uranium and aether upgraded!\nYou got it now!\nIt's time for you to explore all the planet\nYou'll find more amazing things!"));
				uraniumUpgraded = true;
			} else if (clickedUran && uranUpgrade.contains(p) && !uraniumUpgraded && metalMessage && aetherMessage
					&& !metalUpgraded && aetherUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded uranium to level " + (uranLevel + 1)
						+ ".\nNow you have uranium and aether upgraded!\nDon't forget to upgrade metal!\nGo to the metal mine and click the upgrade arrow.\n"));
				uraniumUpgraded = true;
			} else if (clickedUran && uranUpgrade.contains(p) && !uraniumUpgraded && metalMessage && aetherMessage
					&& metalUpgraded && !aetherUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded uranium to level " + (uranLevel + 1)
						+ ".\nNow you have uranium and metal upgraded!\nDon't forget to upgrade aether!\nGo to the aether mine and click the upgrade arrow.\n"));
				uraniumUpgraded = true;
			} else if (clickedUran && uranUpgrade.contains(p) && !uraniumUpgraded && metalMessage && aetherMessage
					&& !metalUpgraded && !aetherUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded uranium to level " + (uranLevel + 1)
						+ ".\nNow you have uranium upgraded!\nDon't forget to upgrade metal and aether!\nGo to their mine and click the upgrade arrow.\n"));
				uraniumUpgraded = true;
			}
			if (clickedUran) {
				if (uranUpgrade.contains(p) && uranLevel < maxLevel
						&& Main.pStats.verifyAvailableResources("uranium", uranLevel, seed)) {
					Main.pStats.setResourceOnPlanet(PlayerStats.METALID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed)
									- Main.pStats.getMetalRequiredToUpgradeUranium(uranLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.URANIUMID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed)
									- Main.pStats.getUraniumRequiredToUpgradeUranium(uranLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.AETHERID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed)
									- Main.pStats.getAetherRequiredToUpgradeUranium(uranLevel));
					Main.pStats
							.setMetal(Main.pStats.getMetal() - Main.pStats.getMetalRequiredToUpgradeUranium(uranLevel));
					Main.pStats.setUranium(
							Main.pStats.getUranium() - Main.pStats.getUraniumRequiredToUpgradeUranium(uranLevel));
					Main.pStats.setAether(
							Main.pStats.getAether() - Main.pStats.getAetherRequiredToUpgradeUranium(uranLevel));
					uranLevel++;
					Main.pStats.getResource("uranium", seed).upgradeResources(uranLevel);
				}
			}
			clickedUran = true;
			g2d.fill(uranRect);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Verdana", Font.PLAIN, 8));
			g2d.drawString("URANIUM", uranRect.x + uranRect.width / 20, uranRect.y + 2 * uranRect.height / 16);
			g2d.drawString("Level: " + uranLevel, uranRect.x + uranRect.width / 20,
					uranRect.y + 4 * uranRect.height / 16);
			g2d.drawString("Amount: " + uranium.getAmountPerHour(), uranRect.x + uranRect.width / 20,
					uranRect.y + 6 * uranRect.height / 16);
			if (uranLevel < maxLevel) {
				g2d.drawString("Upgrade: ", uranRect.x + uranRect.width / 20, uranRect.y + 8 * uranRect.height / 16);
				g2d.drawImage(upgradeArrow, uranUpgrade.x, uranUpgrade.y, uranUpgrade.width, uranUpgrade.height, null);
				g2d.drawString("Cost: ", uranRect.x + uranRect.width / 20, uranRect.y + 10 * uranRect.height / 16);
				g2d.drawString(
						"M:" + Main.pStats.getMetalRequiredToUpgradeUranium(uranLevel) + " U:"
								+ Main.pStats.getUraniumRequiredToUpgradeUranium(uranLevel),
						uranRect.x + uranRect.width / 20, uranRect.y + 12 * uranRect.height / 16);
				g2d.drawString(
						"A:" + Main.pStats.getAetherRequiredToUpgradeUranium(uranLevel) + " P:"
								+ Main.pStats.getPopulationRequiredToUpgradeUranium(uranLevel),
						uranRect.x + uranRect.width / 20, uranRect.y + 14 * uranRect.height / 16);
			}
			if (Main.pStats.getNumberOfPlanets() < 5) {
				g2d.drawString("Total:" + Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed),
						uranRect.x + uranRect.width / 20, uranRect.y + 16 * uranRect.height / 16);
			}
		} else {
			clickedUran = false;
		}
		if (aetherRect.contains(p)) {
			if (!aetherMessage && !metalMessage && !uraniumMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("You found the aether! \nClick on the arrow to upgrade the resource.\n"));
				aetherMessage = true;
			} else if (!aetherMessage && !metalMessage && uraniumMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp(
						"Congratulation! You also found the aether! \nClick on the arrow to upgrade the resource.\n"));
				aetherMessage = true;
			} else if (!aetherMessage && metalMessage && !uraniumMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp(
						"Congratulation! You also found the aether! \nClick on the arrow to upgrade the resource.\n"));
				aetherMessage = true;
			} else if (!aetherMessage && metalMessage && uraniumMessage && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp(
						"Congratulation! You found the last resource, aether! \nClick on the arrow to upgrade the resource."));
				aetherMessage = true;
			}

			if (clickedAether && aetherUpgrade.contains(p) && !aetherUpgraded && !metalMessage && !uraniumMessage
					&& Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded aether to level " + (aetherLevel + 1)
						+ ".\nNow you should go and find Metal and Uranium!"));
				aetherUpgraded = true;
			} else if (clickedAether && aetherUpgrade.contains(p) && !aetherUpgraded && !metalMessage && uraniumMessage
					&& Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded aether to level " + (aetherLevel + 1)
						+ ".\nNow you should go and find Metal mine!"));
				aetherUpgraded = true;
			} else if (clickedAether && aetherUpgrade.contains(p) && !aetherUpgraded && metalMessage && !uraniumMessage
					&& Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded aether to level " + (aetherLevel + 1)
						+ ".\nNow you should go and find Uranium mine!"));
				aetherUpgraded = true;
			} else if (clickedAether && aetherUpgrade.contains(p) && !aetherUpgraded && metalMessage && uraniumMessage
					&& metalUpgraded && uraniumUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded aether to level " + (aetherLevel + 1)
						+ ".\nNow you have metal,uranium and aether upgraded!\nYou got it now!\nIt's time for you to explore all the planet\nYou'll find more amazing things!"));
				aetherUpgraded = true;
			} else if (clickedAether && aetherUpgrade.contains(p) && !aetherUpgraded && metalMessage && uraniumMessage
					&& !metalUpgraded && uraniumUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded aether to level " + (aetherLevel + 1)
						+ ".\nNow you have uranium and aether upgraded!\nDon't forget to upgrade metal!\nGo to the metal mine and click the upgrade arrow.\n"));
				aetherUpgraded = true;
			} else if (clickedAether && aetherUpgrade.contains(p) && !aetherUpgraded && metalMessage && uraniumMessage
					&& metalUpgraded && !uraniumUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded aether to level " + (aetherLevel + 1)
						+ ".\nNow you have metal and aether upgraded!\nDon't forget to upgrade uranium!\nGo to the uranium mine and click the upgrade arrow.\n"));
				aetherUpgraded = true;
			} else if (clickedAether && aetherUpgrade.contains(p) && !aetherUpgraded && metalMessage && uraniumMessage
					&& !metalUpgraded && !uraniumUpgraded && Main.pStats.getNumberOfPlanets() == 1) {
				Galaxy.setPopUp(new PopUp("Congratulation!\n\nYou upgraded aether to level " + (aetherLevel + 1)
						+ ".\nNow you have aether upgraded!\nDon't forget to upgrade metal and uranium!\nGo to their mine and click the upgrade arrow.\n"));
				aetherUpgraded = true;
			}
			if (clickedAether) {
				if (aetherUpgrade.contains(p) && aetherLevel < maxLevel
						&& Main.pStats.verifyAvailableResources("aether", aetherLevel, seed)) {
					Main.pStats.setResourceOnPlanet(PlayerStats.METALID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed)
									- Main.pStats.getMetalRequiredToUpgradeAether(aetherLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.URANIUMID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed)
									- Main.pStats.getUraniumRequiredToUpgradeAether(aetherLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.AETHERID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed)
									- Main.pStats.getAetherRequiredToUpgradeAether(aetherLevel));
					Main.pStats.setMetal(
							Main.pStats.getMetal() - Main.pStats.getMetalRequiredToUpgradeAether(aetherLevel));
					Main.pStats.setUranium(
							Main.pStats.getUranium() - Main.pStats.getUraniumRequiredToUpgradeAether(aetherLevel));
					Main.pStats.setAether(
							Main.pStats.getAether() - Main.pStats.getAetherRequiredToUpgradeAether(aetherLevel));
					aetherLevel++;
					Main.pStats.getResource("aether", seed).upgradeResources(aetherLevel);
				}
			}
			clickedAether = true;
			g2d.fill(aetherRect);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Verdana", Font.PLAIN, 8));
			g2d.drawString("AETHER", aetherRect.x + aetherRect.width / 16, aetherRect.y + 2 * aetherRect.height / 16);
			g2d.drawString("Level: " + aetherLevel, aetherRect.x + aetherRect.width / 16,
					aetherRect.y + 4 * aetherRect.height / 16);
			g2d.drawString("Amount: " + aether.getAmountPerHour(), aetherRect.x + aetherRect.width / 16,
					aetherRect.y + 6 * aetherRect.height / 16);
			if (aetherLevel < maxLevel) {
				g2d.drawString("Upgrade: ", aetherRect.x + aetherRect.width / 16,
						aetherRect.y + 8 * aetherRect.height / 16);
				g2d.drawImage(upgradeArrow, aetherUpgrade.x, aetherUpgrade.y, aetherUpgrade.width, aetherUpgrade.height,
						null);
				g2d.drawString("Cost: ", aetherRect.x + aetherRect.width / 20,
						aetherRect.y + 10 * aetherRect.height / 16);
				g2d.drawString(
						"M:" + Main.pStats.getMetalRequiredToUpgradeAether(aetherLevel) + " U:"
								+ Main.pStats.getUraniumRequiredToUpgradeAether(aetherLevel),
						aetherRect.x + aetherRect.width / 20, aetherRect.y + 12 * aetherRect.height / 16);
				g2d.drawString(
						"A:" + Main.pStats.getAetherRequiredToUpgradeAether(aetherLevel) + " P:"
								+ Main.pStats.getPopulationRequiredToUpgradeAether(aetherLevel),
						aetherRect.x + aetherRect.width / 20, aetherRect.y + 14 * aetherRect.height / 16);
			}
			if (Main.pStats.getNumberOfPlanets() < 5) {
				g2d.drawString("Total:" + Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed),
						aetherRect.x + aetherRect.width / 20, aetherRect.y + 16 * aetherRect.height / 16);
			}
		} else {
			clickedAether = false;
		}
		if (storageRect.contains(p)) {
			if (clickedStorage) {
				if (storageUpgrade.contains(p) && storageLevel < maxLevel
						&& Main.pStats.verifyAvailableResources("storage", storageLevel, seed)) {
					storageUpgraded = true;
					Main.pStats.setResourceOnPlanet(PlayerStats.METALID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed)
									- Main.pStats.getMetalRequiredToUpgradeStorage(storageLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.URANIUMID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed)
									- Main.pStats.getUraniumRequiredToUpgradeStorage(storageLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.AETHERID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed)
									- Main.pStats.getAetherRequiredToUpgradeStorage(storageLevel));
					Main.pStats.setMetal(
							Main.pStats.getMetal() - Main.pStats.getMetalRequiredToUpgradeStorage(storageLevel));
					Main.pStats.setUranium(
							Main.pStats.getUranium() - Main.pStats.getUraniumRequiredToUpgradeStorage(storageLevel));
					Main.pStats.setAether(
							Main.pStats.getAether() - Main.pStats.getAetherRequiredToUpgradeStorage(storageLevel));
					storageLevel++;
					Main.pStats.getResource("storage", seed).upgradeResources(storageLevel);
				}
			}
			clickedStorage = true;
			g2d.fill(storageRect);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Verdana", Font.PLAIN, 8));
			g2d.drawString("STORAGE", storageRect.x + storageRect.width / 20,
					storageRect.y + 2 * storageRect.height / 16);
			g2d.drawString("Level: " + storageLevel, storageRect.x + storageRect.width / 20,
					storageRect.y + 4 * storageRect.height / 16);
			g2d.drawString("Amount: " + storage.getAmountPerHour(), storageRect.x + storageRect.width / 20,
					storageRect.y + 6 * storageRect.height / 16);
			if (storageLevel < maxLevel) {
				g2d.drawString("Upgrade: ", storageRect.x + storageRect.width / 20,
						storageRect.y + 8 * storageRect.height / 16);
				g2d.drawImage(upgradeArrow, storageUpgrade.x, storageUpgrade.y, storageUpgrade.width,
						storageUpgrade.height, null);
				g2d.drawString("Cost: ", storageRect.x + storageRect.width / 20,
						storageRect.y + 10 * storageRect.height / 16);
				g2d.drawString(
						"M:" + Main.pStats.getMetalRequiredToUpgradeStorage(storageLevel) + " U:"
								+ Main.pStats.getUraniumRequiredToUpgradeStorage(storageLevel),
						storageRect.x + storageRect.width / 20, storageRect.y + 12 * storageRect.height / 16);
				g2d.drawString(
						"A:" + Main.pStats.getAetherRequiredToUpgradeStorage(storageLevel) + " P:"
								+ Main.pStats.getPopulationRequiredToUpgradeStorage(storageLevel),
						storageRect.x + storageRect.width / 20, storageRect.y + 14 * storageRect.height / 16);
			}
			if (Main.pStats.getNumberOfPlanets() < 5) {
				g2d.drawString("Total:" + Main.pStats.getResourceOnPlanet(PlayerStats.STORAGEID, seed),
						storageRect.x + storageRect.width / 20, storageRect.y + 16 * storageRect.height / 16);
			}
		} else {
			clickedStorage = false;
		}
		if (popRect.contains(p)) {
			if (clickedPop) {
				if (popUpgrade.contains(p) && popLevel < maxLevel
						&& Main.pStats.verifyAvailableResources("population", popLevel, seed)) {
					populationUpgraded = true;
					Main.pStats.setResourceOnPlanet(PlayerStats.METALID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed)
									- Main.pStats.getMetalRequiredToUpgradeRecruitmentAgency(popLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.URANIUMID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed)
									- Main.pStats.getUraniumRequiredToUpgradeRecruitmentAgency(popLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.AETHERID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed)
									- Main.pStats.getAetherRequiredToUpgradeRecruitmentAgency(popLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.WATERID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.WATERID, seed)
									- Main.pStats.getWaterRequiredToUpgradeRecruitmentAgency(popLevel));
					Main.pStats.setMetal(
							Main.pStats.getMetal() - Main.pStats.getMetalRequiredToUpgradeRecruitmentAgency(popLevel));
					Main.pStats.setUranium(Main.pStats.getUranium()
							- Main.pStats.getUraniumRequiredToUpgradeRecruitmentAgency(popLevel));
					Main.pStats.setAether(Main.pStats.getAether()
							- Main.pStats.getAetherRequiredToUpgradeRecruitmentAgency(popLevel));
					Main.pStats.setWater(
							Main.pStats.getWater() - Main.pStats.getWaterRequiredToUpgradeRecruitmentAgency(popLevel));
					popLevel++;
					Main.pStats.getResource("population", seed).upgradeResources(popLevel);
				}
			}
			clickedPop = true;
			g2d.fill(popRect);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Verdana", Font.PLAIN, 8));
			g2d.drawString("POPULATION", popRect.x + popRect.width / 20, popRect.y + 2 * popRect.height / 16);
			g2d.drawString("Level: " + popLevel, popRect.x + popRect.width / 20, popRect.y + 4 * popRect.height / 16);
			g2d.drawString("Amount: " + population.getAmountPerHour(), popRect.x + popRect.width / 20,
					popRect.y + 6 * popRect.height / 16);
			if (popLevel < maxLevel) {
				g2d.drawString("Upgrade: ", popRect.x + popRect.width / 20, popRect.y + 8 * popRect.height / 16);
				g2d.drawImage(upgradeArrow, popUpgrade.x, popUpgrade.y, popUpgrade.width, popUpgrade.height, null);
				g2d.drawString("Cost: ", popRect.x + popRect.width / 20, popRect.y + 10 * popRect.height / 16);
				g2d.drawString(
						"M:" + Main.pStats.getMetalRequiredToUpgradeRecruitmentAgency(popLevel) + " U:"
								+ Main.pStats.getUraniumRequiredToUpgradeRecruitmentAgency(popLevel),
						popRect.x + popRect.width / 20, popRect.y + 12 * popRect.height / 16);
				g2d.drawString(
						"A:" + Main.pStats.getAetherRequiredToUpgradeRecruitmentAgency(popLevel) + " W:"
								+ Main.pStats.getWaterRequiredToUpgradeRecruitmentAgency(popLevel),
						popRect.x + popRect.width / 20, popRect.y + 14 * popRect.height / 16);
			}
		} else {
			clickedPop = false;
		}
		if (waterRect.contains(p)) {
			if (clickedWater) {
				if (waterUpgrade.contains(p) && waterLevel < maxLevel
						&& Main.pStats.verifyAvailableResources("water", waterLevel, seed)) {
					waterUpgraded = true;
					Main.pStats.setResourceOnPlanet(PlayerStats.METALID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed)
									- Main.pStats.getMetalRequiredToUpgradeWater(waterLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.URANIUMID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed)
									- Main.pStats.getUraniumRequiredToUpgradeWater(waterLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.AETHERID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed)
									- Main.pStats.getAetherRequiredToUpgradeWater(waterLevel));
					Main.pStats
							.setMetal(Main.pStats.getMetal() - Main.pStats.getMetalRequiredToUpgradeWater(waterLevel));
					Main.pStats.setUranium(
							Main.pStats.getUranium() - Main.pStats.getUraniumRequiredToUpgradeWater(waterLevel));
					Main.pStats.setAether(
							Main.pStats.getAether() - Main.pStats.getAetherRequiredToUpgradeWater(waterLevel));
					waterLevel++;
					Main.pStats.getResource("water", seed).upgradeResources(waterLevel);
				}
			}
			clickedWater = true;
			g2d.fill(waterRect);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Verdana", Font.PLAIN, 8));
			g2d.drawString("WATER", waterRect.x + waterRect.width / 20, waterRect.y + 2 * waterRect.height / 16);
			g2d.drawString("Level: " + waterLevel, waterRect.x + waterRect.width / 20,
					waterRect.y + 4 * waterRect.height / 16);
			g2d.drawString("Amount: " + water.getAmountPerHour(), waterRect.x + waterRect.width / 20,
					waterRect.y + 6 * waterRect.height / 16);
			if (waterLevel < maxLevel) {
				g2d.drawString("Upgrade: ", waterRect.x + waterRect.width / 20,
						waterRect.y + 8 * waterRect.height / 16);
				g2d.drawImage(upgradeArrow, waterUpgrade.x, waterUpgrade.y, waterUpgrade.width, waterUpgrade.height,
						null);
				g2d.drawString("Cost: ", waterRect.x + waterRect.width / 20, waterRect.y + 10 * waterRect.height / 16);
				g2d.drawString(
						"M:" + Main.pStats.getMetalRequiredToUpgradeWater(waterLevel) + " U:"
								+ Main.pStats.getUraniumRequiredToUpgradeWater(waterLevel),
						waterRect.x + waterRect.width / 20, waterRect.y + 12 * waterRect.height / 16);
				g2d.drawString("A:" + Main.pStats.getAetherRequiredToUpgradeWater(waterLevel),
						waterRect.x + waterRect.width / 20, waterRect.y + 14 * waterRect.height / 16);
			}
		} else {
			clickedWater = false;
		}
		if (ship1Rect.contains(p)) {
			if (clickedShip1) {
				if (ship1Upgrade.contains(p) && Main.pStats.verifyAvailableResources("ship1", 1, seed)) {
					ship1Upgraded = true;
					Main.pStats.setResourceOnPlanet(PlayerStats.METALID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed)
									- Main.pStats.getMetalRequiredToUpgradeShip1());
					Main.pStats.setResourceOnPlanet(PlayerStats.URANIUMID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed)
									- Main.pStats.getUraniumRequiredToUpgradeShip1());
					Main.pStats.setResourceOnPlanet(PlayerStats.AETHERID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed)
									- Main.pStats.getAetherRequiredToUpgradeShip1());
					Main.pStats.setMetal(Main.pStats.getMetal() - Main.pStats.getMetalRequiredToUpgradeShip1());
					Main.pStats.setUranium(Main.pStats.getUranium() - Main.pStats.getUraniumRequiredToUpgradeShip1());
					Main.pStats.setAether(Main.pStats.getAether() - Main.pStats.getAetherRequiredToUpgradeShip1());
					numOfFighters++;
					Galaxy.sendShips();
				}
			}
			clickedShip1 = true;
			g2d.fill(ship1Rect);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Verdana", Font.PLAIN, 8));
			g2d.drawString("FIGHTERS", ship1Rect.x + ship1Rect.width / 20, ship1Rect.y + 2 * ship1Rect.height / 16);
			g2d.drawString("Amount: " + Main.pStats.getResourceOnPlanet(PlayerStats.SHIPTYPE1ID, seed),
					ship1Rect.x + ship1Rect.width / 20, ship1Rect.y + 4 * ship1Rect.height / 16);
			g2d.drawString("Purchase: ", ship1Rect.x + ship1Rect.width / 20, ship1Rect.y + 6 * ship1Rect.height / 16);
			g2d.drawImage(upgradeArrow, ship1Upgrade.x, ship1Upgrade.y, ship1Upgrade.width, ship1Upgrade.height, null);
			g2d.drawString("Cost: ", ship1Rect.x + ship1Rect.width / 20, ship1Rect.y + 8 * ship1Rect.height / 16);
			g2d.drawString(
					"M:" + Main.pStats.getMetalRequiredToUpgradeShip1() + " U:"
							+ Main.pStats.getUraniumRequiredToUpgradeShip1(),
					ship1Rect.x + ship1Rect.width / 20, ship1Rect.y + 10 * ship1Rect.height / 16);
			g2d.drawString(
					"A:" + Main.pStats.getAetherRequiredToUpgradeShip1() + " P:"
							+ Main.pStats.getPopulationRequiredToUpgradeShip1(),
					ship1Rect.x + ship1Rect.width / 20, ship1Rect.y + 12 * ship1Rect.height / 16);

		} else {
			clickedShip1 = false;
		}
		if (ship2Rect.contains(p)) {
			if (clickedShip2) {
				if (ship2Upgrade.contains(p) && Main.pStats.verifyAvailableResources("ship2", 1, seed)) {
					ship2Upgraded = true;
					Main.pStats.setResourceOnPlanet(PlayerStats.METALID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed)
									- Main.pStats.getMetalRequiredToUpgradeShip2());
					Main.pStats.setResourceOnPlanet(PlayerStats.URANIUMID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed)
									- Main.pStats.getUraniumRequiredToUpgradeShip2());
					Main.pStats.setResourceOnPlanet(PlayerStats.AETHERID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed)
									- Main.pStats.getAetherRequiredToUpgradeShip2());
					Main.pStats.setMetal(Main.pStats.getMetal() - Main.pStats.getMetalRequiredToUpgradeShip2());
					Main.pStats.setUranium(Main.pStats.getUranium() - Main.pStats.getUraniumRequiredToUpgradeShip2());
					Main.pStats.setAether(Main.pStats.getAether() - Main.pStats.getAetherRequiredToUpgradeShip2());
					numOfCarriers++;
					Galaxy.sendShips();
				}
			}
			clickedShip2 = true;
			g2d.fill(ship2Rect);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Verdana", Font.PLAIN, 8));
			g2d.drawString("CARRIERS", ship2Rect.x + ship2Rect.width / 20, ship2Rect.y + 2 * ship2Rect.height / 16);
			g2d.drawString("Amount: " + Main.pStats.getResourceOnPlanet(PlayerStats.SHIPTYPE2ID, seed),
					ship2Rect.x + ship2Rect.width / 20, ship2Rect.y + 4 * ship2Rect.height / 16);
			g2d.drawString("Purchase: ", ship2Rect.x + ship2Rect.width / 20, ship2Rect.y + 6 * ship2Rect.height / 16);
			g2d.drawImage(upgradeArrow, ship2Upgrade.x, ship2Upgrade.y, ship2Upgrade.width, ship2Upgrade.height, null);
			g2d.drawString("Cost: ", ship2Rect.x + ship2Rect.width / 20, ship2Rect.y + 8 * ship2Rect.height / 16);
			g2d.drawString(
					"M:" + Main.pStats.getMetalRequiredToUpgradeShip2() + " U:"
							+ Main.pStats.getUraniumRequiredToUpgradeShip2(),
					ship2Rect.x + ship2Rect.width / 20, ship2Rect.y + 10 * ship2Rect.height / 16);
			g2d.drawString(
					"A:" + Main.pStats.getAetherRequiredToUpgradeShip2() + " P:"
							+ Main.pStats.getPopulationRequiredToUpgradeShip2(),
					ship2Rect.x + ship2Rect.width / 20, ship2Rect.y + 12 * ship2Rect.height / 16);
		} else {
			clickedShip2 = false;
		}
		if (ship3Rect.contains(p)) {
			if (clickedShip3) {
				if (ship3Upgrade.contains(p) && Main.pStats.verifyAvailableResources("ship3", 1, seed)) {
					ship3Upgraded = true;
					Main.pStats.setResourceOnPlanet(PlayerStats.METALID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed)
									- Main.pStats.getMetalRequiredToUpgradeShip3());
					Main.pStats.setResourceOnPlanet(PlayerStats.URANIUMID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed)
									- Main.pStats.getUraniumRequiredToUpgradeShip3());
					Main.pStats.setResourceOnPlanet(PlayerStats.AETHERID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed)
									- Main.pStats.getAetherRequiredToUpgradeShip3());
					Main.pStats.setMetal(Main.pStats.getMetal() - Main.pStats.getMetalRequiredToUpgradeShip3());
					Main.pStats.setUranium(Main.pStats.getUranium() - Main.pStats.getUraniumRequiredToUpgradeShip3());
					Main.pStats.setAether(Main.pStats.getAether() - Main.pStats.getAetherRequiredToUpgradeShip3());
					numOfCommand++;
					Galaxy.sendShips();
				}
			}
			clickedShip3 = true;
			g2d.fill(ship3Rect);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Verdana", Font.PLAIN, 8));
			g2d.drawString("COMMAND SHIPS", ship3Rect.x + ship3Rect.width / 20,
					ship3Rect.y + 2 * ship3Rect.height / 16);
			g2d.drawString("Amount: " + Main.pStats.getResourceOnPlanet(PlayerStats.SHIPTYPE3ID, seed),
					ship3Rect.x + ship3Rect.width / 20, ship3Rect.y + 4 * ship3Rect.height / 16);
			g2d.drawString("Purchase: ", ship3Rect.x + ship3Rect.width / 20, ship3Rect.y + 6 * ship3Rect.height / 16);
			g2d.drawImage(upgradeArrow, ship3Upgrade.x, ship3Upgrade.y, ship3Upgrade.width, ship3Upgrade.height, null);
			g2d.drawString("Cost: ", ship3Rect.x + ship3Rect.width / 20, ship3Rect.y + 8 * ship3Rect.height / 16);
			g2d.drawString(
					"M:" + Main.pStats.getMetalRequiredToUpgradeShip3() + " U:"
							+ Main.pStats.getUraniumRequiredToUpgradeShip3(),
					ship3Rect.x + ship3Rect.width / 20, ship3Rect.y + 10 * ship3Rect.height / 16);
			g2d.drawString(
					"A:" + Main.pStats.getAetherRequiredToUpgradeShip3() + " P:"
							+ Main.pStats.getPopulationRequiredToUpgradeShip3(),
					ship3Rect.x + ship3Rect.width / 20, ship3Rect.y + 12 * ship3Rect.height / 16);
		} else {
			clickedShip3 = false;
		}
		if (moneyRect.contains(p)) {
			if (clickedMoney) {
				if (moneyUpgrade.contains(p) && bankLevel < maxLevel
						&& Main.pStats.verifyAvailableResources("money", bankLevel, seed)) {
					moneyUpgraded = true;
					Main.pStats.setResourceOnPlanet(PlayerStats.METALID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.METALID, seed)
									- Main.pStats.getMetalRequiredToUpgradeBank(bankLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.URANIUMID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID, seed)
									- Main.pStats.getUraniumRequiredToUpgradeBank(bankLevel));
					Main.pStats.setResourceOnPlanet(PlayerStats.AETHERID, seed,
							Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID, seed)
									- Main.pStats.getAetherRequiredToUpgradeBank(bankLevel));
					Main.pStats.setMetal(Main.pStats.getMetal() - Main.pStats.getMetalRequiredToUpgradeBank(bankLevel));
					Main.pStats.setUranium(
							Main.pStats.getUranium() - Main.pStats.getUraniumRequiredToUpgradeBank(bankLevel));
					Main.pStats
							.setAether(Main.pStats.getAether() - Main.pStats.getAetherRequiredToUpgradeBank(bankLevel));
					bankLevel++;
					Main.pStats.getResource("money", seed).upgradeResources(bankLevel);
				}
			}
			clickedMoney = true;
			g2d.fill(moneyRect);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Verdana", Font.PLAIN, 8));
			g2d.drawString("BANK", moneyRect.x + moneyRect.width / 20, moneyRect.y + 2 * moneyRect.height / 16);
			g2d.drawString("Level: " + bankLevel, moneyRect.x + moneyRect.width / 20,
					moneyRect.y + 4 * moneyRect.height / 16);
			g2d.drawString("D. Limit: " + Main.pStats.getDepositLimit(bankLevel - 1),
					moneyRect.x + moneyRect.width / 20, moneyRect.y + 6 * moneyRect.height / 16);
			if (bankLevel < maxLevel) {
				g2d.drawString("Upgrade: ", moneyRect.x + moneyRect.width / 20,
						moneyRect.y + 8 * moneyRect.height / 16);
				g2d.drawImage(upgradeArrow, moneyUpgrade.x, moneyUpgrade.y, moneyUpgrade.width, moneyUpgrade.height,
						null);
				g2d.drawString("Cost: ", moneyRect.x + moneyRect.width / 20, moneyRect.y + 10 * moneyRect.height / 16);
				g2d.drawString(
						"M:" + Main.pStats.getMetalRequiredToUpgradeBank(bankLevel) + " U:"
								+ Main.pStats.getUraniumRequiredToUpgradeBank(bankLevel),
						moneyRect.x + moneyRect.width / 20, moneyRect.y + 12 * moneyRect.height / 16);
				g2d.drawString("A:" + Main.pStats.getAetherRequiredToUpgradeBank(bankLevel),
						moneyRect.x + moneyRect.width / 20, moneyRect.y + 14 * moneyRect.height / 16);
			}
		} else {
			clickedMoney = false;
		}
	}

	/**
	 * gets the number of fighters on this planet
	 * 
	 * @return the number of fighters
	 */
	public int getNumOfFighters() {
		return numOfFighters;
	}

	/**
	 * sets the number of fighters on this planet
	 * 
	 * @param numOfFighters
	 *            the new number of fighters
	 */
	public void setNumOfFighters(int numOfFighters) {
		this.numOfFighters = numOfFighters;
	}

	/**
	 * gets the number of carriers on this planet
	 * 
	 * @return the number of carriers
	 */
	public int getNumOfCarriers() {
		return numOfCarriers;
	}

	/**
	 * sets the number of carriers on this planet
	 * 
	 * @param numOfCarriers
	 *            the new number of carriers
	 */
	public void setNumOfCarriers(int numOfCarriers) {
		this.numOfCarriers = numOfCarriers;
	}

	/**
	 * gets the number of command ships on this planet
	 * 
	 * @return the number of command ships
	 */
	public int getNumOfCommand() {
		return numOfCommand;
	}

	/**
	 * sets the number of command ships on this planet
	 * 
	 * @param numOfCommand
	 *            the new number of command ships
	 */
	public void setNumOfCommand(int numOfCommand) {
		this.numOfCommand = numOfCommand;
	}

	private boolean upgradedChecker1() {
		if (metalUpgraded && uraniumUpgraded && aetherUpgraded && storageUpgraded && populationUpgraded && waterUpgraded
				&& moneyUpgraded && ship1Upgraded && ship2Upgraded && ship3Upgraded)
			return true;
		return false;
	}

	private boolean upgradedChecker2() {
		if (metalUpgraded && uraniumUpgraded && aetherUpgraded && storageUpgraded && populationUpgraded && waterUpgraded
				&& moneyUpgraded && !ship1Upgraded && ship2Upgraded && ship3Upgraded)
			return true;
		return false;
	}

	private boolean upgradedChecker3() {
		if (metalUpgraded && uraniumUpgraded && aetherUpgraded && storageUpgraded && populationUpgraded && waterUpgraded
				&& moneyUpgraded && ship1Upgraded && !ship2Upgraded && ship3Upgraded)
			return true;
		return false;
	}

	private boolean upgradedChecker4() {
		if (metalUpgraded && uraniumUpgraded && aetherUpgraded && storageUpgraded && populationUpgraded && waterUpgraded
				&& moneyUpgraded && ship1Upgraded && ship2Upgraded && !ship3Upgraded)
			return true;
		return false;
	}

	private boolean upgradedChecker5() {
		if (metalUpgraded && uraniumUpgraded && aetherUpgraded && storageUpgraded && populationUpgraded && waterUpgraded
				&& moneyUpgraded && !ship1Upgraded && !ship2Upgraded && ship3Upgraded)
			return true;
		return false;
	}

	private boolean upgradedChecker6() {
		if (metalUpgraded && uraniumUpgraded && aetherUpgraded && storageUpgraded && populationUpgraded && waterUpgraded
				&& moneyUpgraded && !ship1Upgraded && ship2Upgraded && !ship3Upgraded)
			return true;
		return false;
	}

	private boolean upgradedChecker7() {
		if (metalUpgraded && uraniumUpgraded && aetherUpgraded && storageUpgraded && populationUpgraded && waterUpgraded
				&& moneyUpgraded && ship1Upgraded && !ship2Upgraded && !ship3Upgraded)
			return true;
		return false;
	}

	private boolean upgradedChecker8() {
		if (metalUpgraded && uraniumUpgraded && aetherUpgraded && storageUpgraded && populationUpgraded && waterUpgraded
				&& moneyUpgraded && !ship1Upgraded && !ship2Upgraded && !ship3Upgraded)
			return true;
		return false;
	}
	
	public void addResources() {
		Main.pStats.addResource(metal);
		Main.pStats.addResource(uranium);
		Main.pStats.addResource(aether);
		Main.pStats.addResource(water);
		Main.pStats.addResource(storage);
		Main.pStats.addResource(population);
		Main.pStats.addResource(money);
		Main.pStats.addResource(ship1);
		Main.pStats.addResource(ship2);
		Main.pStats.addResource(ship3);
		
		Main.pStats.setResourceOnPlanet(PlayerStats.METALID, seed, Main.db.executeQueryInt("SELECT Metal FROM Planets WHERE PSeed = " + seed + ";", "Metal"));
		Main.pStats.setResourceOnPlanet(PlayerStats.URANIUMID, seed, Main.db.executeQueryInt("SELECT Uran FROM Planets WHERE PSeed = " + seed + ";", "Uran"));
		Main.pStats.setResourceOnPlanet(PlayerStats.AETHERID, seed, Main.db.executeQueryInt("SELECT Aether FROM Planets WHERE PSeed = " + seed + ";", "Aether"));
		Main.pStats.setResourceOnPlanet(PlayerStats.WATERID, seed, Main.db.executeQueryInt("SELECT Water FROM Planets WHERE PSeed = " + seed + ";", "Water"));
		Main.pStats.setResourceOnPlanet(PlayerStats.STORAGEID, seed, Main.db.executeQueryInt("SELECT Storage FROM Planets WHERE PSeed = " + seed + ";", "Storage"));
		Main.pStats.setResourceOnPlanet(PlayerStats.SHIPTYPE1ID, seed, Main.db.executeQueryInt("SELECT Ship1 FROM Planets WHERE PSeed = " + seed + ";", "Ship1"));
		Main.pStats.setResourceOnPlanet(PlayerStats.SHIPTYPE2ID, seed, Main.db.executeQueryInt("SELECT Ship2 FROM Planets WHERE PSeed = " + seed + ";", "Ship2"));
		Main.pStats.setResourceOnPlanet(PlayerStats.SHIPTYPE3ID, seed, Main.db.executeQueryInt("SELECT Ship3 FROM Planets WHERE PSeed = " + seed + ";", "Ship3"));
		
		Main.pStats.setMetal(Main.pStats.getMetal() + Main.db.executeQueryInt("SELECT Metal FROM Planets WHERE PSeed = " + seed + ";", "Metal"));
		Main.pStats.setUranium(Main.pStats.getUranium() + Main.db.executeQueryInt("SELECT Uran FROM Planets WHERE PSeed = " + seed + ";", "Uran"));
		Main.pStats.setAether(Main.pStats.getAether() + Main.db.executeQueryInt("SELECT Aether FROM Planets WHERE PSeed = " + seed + ";", "Aether"));
		Main.pStats.setWater(Main.pStats.getWater() + Main.db.executeQueryInt("SELECT Water FROM Planets WHERE PSeed = " + seed + ";", "Water"));
		Main.pStats.setStorage(Main.pStats.getStorage() + Main.db.executeQueryInt("SELECT Metal FROM Planets WHERE PSeed = " + seed + ";", "Metal"));
		Main.pStats.setMoney(Main.db.executeQueryInt("SELECT Bank FROM Planets WHERE PSeed = " + seed + ";", "Bank"));
		Main.pStats.setNumberOfShipsType1(Main.pStats.getNumberOfShipsType1() + (int)Main.db.executeQueryInt("SELECT Ship1 FROM Planets WHERE PSeed = " + seed + ";", "Ship1"));
		Main.pStats.setNumbersOfShipsType2(Main.pStats.getNumbersOfShipsType2() + (int)Main.db.executeQueryInt("SELECT Ship2 FROM Planets WHERE PSeed = " + seed + ";", "Ship2"));
		Main.pStats.setNumbersOfShipsType3(Main.pStats.getNumbersOfShipsType3() + (int)Main.db.executeQueryInt("SELECT Ship3 FROM Planets WHERE PSeed = " + seed + ";", "Ship3"));

		
		metalLevel = (int)Main.db.executeQueryInt("SELECT Metal FROM Resources WHERE PSeed = " + seed + ";", "Metal");
		uranLevel = (int)Main.db.executeQueryInt("SELECT Uran FROM Resources WHERE PSeed = " + seed + ";", "Uran");
		aetherLevel = (int)Main.db.executeQueryInt("SELECT Aether FROM Resources WHERE PSeed = " + seed + ";", "Aether");
		popLevel = (int)Main.db.executeQueryInt("SELECT Pop FROM Resources WHERE PSeed = " + seed + ";", "Pop");
		waterLevel = (int)Main.db.executeQueryInt("SELECT Water FROM Resources WHERE PSeed = " + seed + ";", "Water");
		storageLevel = (int)Main.db.executeQueryInt("SELECT Storage FROM Resources WHERE PSeed = " + seed + ";", "Storage");
		bankLevel = (int)Main.db.executeQueryInt("SELECT Bank FROM Resources WHERE PSeed = " + seed + ";", "Bank");
		
		metal.setLevel(metalLevel);
		uranium.setLevel(uranLevel);
		aether.setLevel(aetherLevel);
		population.setLevel(popLevel);
		water.setLevel(waterLevel);
		storage.setLevel(storageLevel);
		money.setLevel(bankLevel);
		
		metal.setAmountPerHour(""+metalLevel);
		uranium.setAmountPerHour(""+uranLevel);
		aether.setAmountPerHour(""+aetherLevel);
		population.setAmountPerHour(""+popLevel);
		water.setAmountPerHour(""+waterLevel);
		storage.setAmountPerHour(""+storageLevel);
		money.setAmountPerHour(""+bankLevel);
		Main.pStats.setPopulation(Main.pStats.getPopulation() + population.getAmountPerHour());
	//	Main.pStats.setPopulationUsed(Main.pStats.getPopulationUsed() + (int) (Main.pStats.getPopulation() - Main.db.executeQueryInt("SELECT Population FROM SAccounts WHERE Name = '" + Main.PNAME + "';", "Population")));
	}

}