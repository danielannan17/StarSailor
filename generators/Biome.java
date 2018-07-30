package generators;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * contains code for making custom biomes for the planets
 * 
 * 
 */
public class Biome {

	private static HashMap<Integer, Biome> idMap = new HashMap<Integer, Biome>();

	// very cold biomes
	public static Biome polar_desert = new Biome("polar desert", 0, 0.05f, Block.snow);
	public static Biome ice_spikes = new Biome("ice spikes", 1, 0.05f, Block.snow);
	public static Biome frozen_lakes = new Biome("frozen lakes", 2, 0.1f, Block.snow);
	public static Biome ice_sheet = new Biome("ice sheet", 3, 0.1f, Block.ice);
	public static Biome ice_bergs = new Biome("ice bergs", 4, 0.1f, Block.water_ocean);

	// cold biomes
	public static Biome tundra = new Biome("tundra", 5, 0.15f, Block.grass_tundra);
	public static Biome mountain = new Biome("mountain", 6, 0.2f, Block.grass_mountains);
	public static Biome taiga = new Biome("taiga", 7, 0.2f, Block.grass_snowy);
	public static Biome mountain_forest = new Biome("mountain forest", 8, 0.2f, Block.grass_mountains);
	public static Biome ocean = new Biome("ocean", 9, 0.2f, Block.water_ocean);

	// medium biomes
	public static Biome steppe = new Biome("steppe", 10, 0.25f, Block.grass_steppe);
	public static Biome plains = new Biome("plains", 11, 0.3f, Block.grass_plains);
	public static Biome forest = new Biome("forest", 12, 0.3f, Block.grass_forest);
	public static Biome lakes = new Biome("lakes", 13, 0.3f, Block.grass_plains);
	public static Biome islands = new Biome("islands", 14, 0.25f, Block.water_ocean);

	// hot biomes
	public static Biome desert_plains = new Biome("desert plains", 15, 0.2f, Block.sand_cracked);
	public static Biome canyon = new Biome("canyon", 16, 0.2f, Block.clay);
	public static Biome savannah = new Biome("savannah", 17, 0.2f, Block.grass_savannah);
	public static Biome jungle = new Biome("jungle", 18, 0.2f, Block.grass_jungle);
	public static Biome rainforest = new Biome("rainforest", 19, 0.15f, Block.grass_rainforest);

	// very hot biomes
	public static Biome lava_ocean = new Biome("lava ocean", 20, 0.1f, Block.lava);
	public static Biome lava_islands = new Biome("lava islands", 21, 0.1f, Block.lava);
	public static Biome lava_lakes = new Biome("lava lakes", 22, 0.1f, Block.rock);
	public static Biome igneous_desert = new Biome("igneous desert", 23, 0.05f, Block.rock);
	public static Biome volcanic_mountains = new Biome("volcanic_mountains", 24, 0.05f, Block.rock);

	private ArrayList<BiomePart> biomeParts = new ArrayList<BiomePart>(), decoParts = new ArrayList<BiomePart>();
	private String name;
	private int id;
	private float temperature, precipitation, lifeChance;

	/**
	 * creates a new Biome
	 * 
	 * @param name
	 *            the name of the biome
	 * @param id
	 *            the id of the biome
	 * @param lifeChance
	 *            the chance of life
	 * @param base
	 *            the base Block
	 */
	public Biome(String name, int id, float lifeChance, Block base) {
		setName(name);
		setId(id);
		setLifeChance(lifeChance);
		biomeParts.add(new BiomePart(base, 0, 1, 1));
	}

	/**
	 * adds a new part to this biome
	 * 
	 * @param part
	 *            the BiomePart to add
	 */
	public void addBiomePart(BiomePart part) {
		biomeParts.add(part);
	}

	/**
	 * gets the list of biome parts in this biome
	 * 
	 * @return a list of biome parts
	 */
	public ArrayList<BiomePart> getBiomeParts() {
		return biomeParts;
	}

	/**
	 * adds a decoration part to this biome
	 * 
	 * @param part
	 *            the decoration part to add
	 */
	public void addDecoPart(BiomePart part) {
		decoParts.add(part);
	}

	/**
	 * gets the decoration parts of this planet
	 * 
	 * @return a list of decoration parts
	 */
	public ArrayList<BiomePart> getDecoParts() {
		return decoParts;
	}

	/**
	 * gets the name of this biome
	 * 
	 * @return the name of this biome
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of this biome
	 * 
	 * @param name
	 *            the new biome name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets the id of this biome
	 * 
	 * @return the id of the biome
	 */
	public int getId() {
		return id;
	}

	/**
	 * sets the id of this biome
	 * 
	 * @param id
	 *            the new id of this biome
	 */
	public void setId(int id) {
		this.id = id;
		idMap.put(id, this);
	}

	/**
	 * gets the biomes temperature
	 * 
	 * @return the temperature of the biome
	 */
	public float getTemperature() {
		return temperature;
	}

	/**
	 * sets the temperature of this biome
	 * 
	 * @param temperature
	 *            the new temperature of this biome
	 */
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	/**
	 * gets the precipitation of this biome
	 * 
	 * @return the precipitation of this biome
	 */
	public float getPrecipitation() {
		return precipitation;
	}

	/**
	 * sets the precipitation of this biome
	 * 
	 * @param precipitation
	 *            the new value for the precipitation
	 */
	public void setPrecipitation(float precipitation) {
		this.precipitation = precipitation;
	}

	/**
	 * gets a biome based on an id
	 * 
	 * @param id
	 *            the id
	 * @return the biome with that id
	 */
	public static Biome getBiome(int id) {
		return idMap.get(id);
	}

	/**
	 * gets the chance of life of a biome
	 * 
	 * @return the chance of life
	 */
	public float getLifeChance() {
		return lifeChance;
	}

	/**
	 * sets the life chance
	 * 
	 * @param lifeChance
	 *            the new life chance
	 */
	public void setLifeChance(float lifeChance) {
		this.lifeChance = lifeChance;
	}

	/**
	 * creates the default biomes
	 */
	public static void createDefaultBiomes() {
		// very cold
		polar_desert.addBiomePart(new BiomePart(Block.ice, 0.4f, 0.5f, 1f));

		ice_spikes.addBiomePart(new BiomePart(Block.ice, 0f, 0.4f, 1f));
		ice_spikes.addDecoPart(new BiomePart(Block.ice_spikes, 0.3f, 0.7f, 0.01f));

		frozen_lakes.addBiomePart(new BiomePart(Block.ice, 0.7f, 1f, 1f));

		ice_sheet.addBiomePart(new BiomePart(Block.water_river, 0.35f, 0.5f, 1f));

		ice_bergs.addBiomePart(new BiomePart(Block.ice, 0.6f, 1f, 1f));

		// cold
		tundra.addBiomePart(new BiomePart(Block.water_murky, 0.7f, 1f, 1f));

		mountain.addBiomePart(new BiomePart(Block.water_ocean, 0f, 0.4f, 1f));
		mountain.addBiomePart(new BiomePart(Block.sand, 0.4f, 0.45f, 1f));
		mountain.addBiomePart(new BiomePart(Block.stone_solid, 0.7f, 1f, 1f));
		mountain.addBiomePart(new BiomePart(Block.stone_snowy, 0.8f, 1f, 1f));
		mountain.addBiomePart(new BiomePart(Block.snow, 0.9f, 1f, 1f));
		mountain.addDecoPart(new BiomePart(Block.rocks, 0.65f, 0.75f, 0.1f));
		mountain.addDecoPart(new BiomePart(Block.tree_pine, 0.45f, 0.7f, 0.1f));

		taiga.addBiomePart(new BiomePart(Block.water_river, 0.4f, 0.5f, 1f));
		taiga.addDecoPart(new BiomePart(Block.tree_pine, 0f, 0.4f, 0.1f));
		taiga.addDecoPart(new BiomePart(Block.tree_pine, 0.5f, 1f, 0.1f));

		mountain_forest.addBiomePart(new BiomePart(Block.water_ocean, 0f, 0.4f, 1f));
		mountain_forest.addBiomePart(new BiomePart(Block.sand, 0.4f, 0.45f, 1f));
		mountain_forest.addBiomePart(new BiomePart(Block.stone_solid, 0.7f, 1f, 1f));
		mountain_forest.addBiomePart(new BiomePart(Block.stone_snowy, 0.8f, 1f, 1f));
		mountain_forest.addBiomePart(new BiomePart(Block.snow, 0.9f, 1f, 1f));
		mountain_forest.addDecoPart(new BiomePart(Block.rocks, 0.65f, 0.75f, 0.1f));
		mountain_forest.addDecoPart(new BiomePart(Block.tree_pine, 0.45f, 0.7f, 0.2f));
		mountain_forest.addDecoPart(new BiomePart(Block.tree_spruce, 0.45f, 0.7f, 0.2f));

		ocean.addBiomePart(new BiomePart(Block.rock, 0f, 0.1f, 1f));
		ocean.addDecoPart(new BiomePart(Block.rocks, 0.05f, 0.25f, 0.1f));
		ocean.addDecoPart(new BiomePart(Block.rocks, 0.5f, 1f, 0.05f));

		// medium
		steppe.addDecoPart(new BiomePart(Block.tree_shrub, 0f, 1f, 0.05f));
		steppe.addDecoPart(new BiomePart(Block.tree_shrub_1, 0.5f, 1f, 0.1f));

		plains.addDecoPart(new BiomePart(Block.flowers, 0.3f, 0.5f, 0.2f));
		plains.addDecoPart(new BiomePart(Block.flower, 0.8f, 1f, 0.2f));
		plains.addDecoPart(new BiomePart(Block.tree_oak, 0f, 0.4f, 0.001f));
		plains.addDecoPart(new BiomePart(Block.tree_shrub, 0f, 1f, 0.03f));

		forest.addBiomePart(new BiomePart(Block.water_river, 0.9f, 1f, 1f));
		forest.addBiomePart(new BiomePart(Block.sand, 0.83f, 0.9f, 1f));
		forest.addDecoPart(new BiomePart(Block.tree_birch, 0f, 0.8f, 0.2f));
		forest.addDecoPart(new BiomePart(Block.tree_oak_1, 0f, 0.8f, 0.2f));
		forest.addDecoPart(new BiomePart(Block.tree_pine, 0f, 0.8f, 0.1f));

		lakes.addBiomePart(new BiomePart(Block.water_river, 0.7f, 1f, 1f));
		lakes.addDecoPart(new BiomePart(Block.tree_shrub_1, 0f, 0.7f, 0.01f));

		islands.addBiomePart(new BiomePart(Block.sand, 0.8f, 0.9f, 1f));
		islands.addBiomePart(new BiomePart(Block.grass_jungle, 0.9f, 1f, 1f));
		islands.addDecoPart(new BiomePart(Block.tree_palm, 0.9f, 1f, 0.03f));
		islands.addDecoPart(new BiomePart(Block.rocks, 0f, 0.3f, 0.05f));

		// hot
		desert_plains.addBiomePart(new BiomePart(Block.sand_arid, 0.8f, 1f, 1f));
		desert_plains.addDecoPart(new BiomePart(Block.tree_shrub_1, 0f, 1f, 0.05f));
		desert_plains.addDecoPart(new BiomePart(Block.tree_cactus, 0.4f, 1f, 0.05f));
		desert_plains.addDecoPart(new BiomePart(Block.rocks, 0f, 0.6f, 0.05f));

		canyon.addBiomePart(new BiomePart(Block.sand_cracked, 0f, 0.2f, 1f));
		canyon.addDecoPart(new BiomePart(Block.tree_shrub_1, 0f, 1f, 0.01f));

		savannah.addDecoPart(new BiomePart(Block.tree_baobab, 0f, 1f, 0.01f));
		savannah.addDecoPart(new BiomePart(Block.tree_shrub_1, 0f, 1f, 0.02f));

		jungle.addDecoPart(new BiomePart(Block.tree_palm, 0f, 0.7f, 0.3f));
		jungle.addDecoPart(new BiomePart(Block.tree_rubber, 0.3f, 1f, 0.3f));
		jungle.addDecoPart(new BiomePart(Block.tree_shrub, 0f, 1f, 0.1f));

		rainforest.addBiomePart(new BiomePart(Block.water_river, 0.42f, 0.48f, 1f));
		rainforest.addDecoPart(new BiomePart(Block.tree_palm, 0f, 0.42f, 0.3f));
		rainforest.addDecoPart(new BiomePart(Block.tree_rubber, 0f, 0.42f, 0.3f));
		rainforest.addDecoPart(new BiomePart(Block.tree_shrub, 0f, 0.42f, 0.1f));
		rainforest.addDecoPart(new BiomePart(Block.tree_palm, 0.48f, 1f, 0.3f));
		rainforest.addDecoPart(new BiomePart(Block.tree_rubber, 0.48f, 1f, 0.3f));
		rainforest.addDecoPart(new BiomePart(Block.tree_shrub, 0.48f, 1f, 0.1f));

		// very hot
		lava_ocean.addDecoPart(new BiomePart(Block.rocks, 0f, 1f, 0.01f));

		lava_islands.addBiomePart(new BiomePart(Block.rock, 0.7f, 1f, 1f));
		lava_islands.addDecoPart(new BiomePart(Block.rocks, 0f, 1f, 0.05f));

		lava_lakes.addBiomePart(new BiomePart(Block.lava, 0.6f, 1f, 1f));

		igneous_desert.addBiomePart(new BiomePart(Block.stone_solid, 0.8f, 1f, 1f));

		volcanic_mountains.addBiomePart(new BiomePart(Block.lava, 0f, 0.3f, 1f));
		volcanic_mountains.addBiomePart(new BiomePart(Block.stone_solid, 0.8f, 1f, 1f));
		volcanic_mountains.addDecoPart(new BiomePart(Block.rocks, 0.2f, 0.9f, 0.1f));
	}

}
