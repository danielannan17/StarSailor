package generators;

import org.junit.Before;
import org.junit.Test;

public class BiomeTest {
	
	Biome biome;

	@Before
	public void setUp(){
		biome = Biome.canyon;
	}
	
	@Test
	public void testAddBiomePart() {
		biome.addBiomePart(new BiomePart(Block.clay, 0, 0, 0));
	}

	@Test
	public void testGetBiomeParts() {
		biome.getBiomeParts();
	}

	@Test
	public void testAddDecoPart() {
		biome.addDecoPart(new BiomePart(Block.tree_baobab, 0, 0, 0));
	}

	@Test
	public void testGetDecoParts() {
		biome.getDecoParts();
	}

	@Test
	public void testGetName() {
		biome.getName();
	}

	@Test
	public void testSetName() {
		biome.setName("test");
	}

	@Test
	public void testGetId() {
		biome.getId();
	}

	@Test
	public void testSetId() {
		biome.setId(2374);
	}

	@Test
	public void testGetTemperature() {
		biome.getTemperature();
	}

	@Test
	public void testSetTemperature() {
		biome.setTemperature(100);
	}

	@Test
	public void testGetPrecipitation() {
		biome.getPrecipitation();
	}

	@Test
	public void testSetPrecipitation() {
		biome.setPrecipitation(213);
	}

	@Test
	public void testGetBiome() {
		Biome.getBiome(4);
	}

	@Test
	public void testGetLifeChance() {
		biome.getLifeChance();
	}

	@Test
	public void testSetLifeChance() {
		biome.setLifeChance(10);
	}

	@Test
	public void testCreateDefaultBiomes() {
		biome.createDefaultBiomes();
	}

}
