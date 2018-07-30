package generators;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

public class PlanetGeneratorTest {

	PlanetGenerator planet;
	PerlinNoiseGenerator gen;

	@Before
	public void setUp() {
		planet = new PlanetGenerator(100, 100, 0, false);
		gen = new PerlinNoiseGenerator(0);
	}

	@Test
	public void testChooseBiome() {
		for (float i = 0; i <= 1; i += 0.1f) {
			for (float j = 0; j <= 1; j += 0.1f) {
				planet.chooseBiome(i, j);
			}
		}
	}

	@Test
	public void testGeneratePlanet() {
		planet.generatePlanet(gen.getPerlinNoise(100, 100, 4, 5), 1);
	}
	
	@Test
	public void testUpdate(){
		planet.update();
	}
	
	@Test
	public void testDraw(){
		planet.draw((new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)).getGraphics(), 5, 5, 1);
	}

	@Test
	public void testGetBiome() {
		planet.setBiome(Biome.canyon);
		assertEquals(Biome.canyon, planet.getBiome());
	}

	@Test
	public void testSetBiome() {
		planet.setBiome(Biome.canyon);
		assertEquals(Biome.canyon, planet.getBiome());
	}

	@Test
	public void testGetColor() {
		planet.setColor(Color.blue);
		assertEquals(Color.blue, planet.getColor());
	}

	@Test
	public void testSetColor() {
		planet.setColor(Color.blue);
		assertEquals(Color.blue, planet.getColor());
	}

	@Test
	public void testClicked() {
		planet.clicked(new Point(4, 5));
	}

	@Test
	public void testGetNumOfFighters() {
		planet.setNumOfFighters(50);
		assertEquals(50, planet.getNumOfFighters());
	}

	@Test
	public void testSetNumOfFighters() {
		planet.setNumOfFighters(50);
		assertEquals(50, planet.getNumOfFighters());
	}

	@Test
	public void testGetNumOfCarriers() {
		planet.setNumOfCarriers(50);
		assertEquals(50, planet.getNumOfCarriers());
	}

	@Test
	public void testSetNumOfCarriers() {
		planet.setNumOfCarriers(50);
		assertEquals(50, planet.getNumOfCarriers());
	}

	@Test
	public void testGetNumOfCommand() {
		planet.setNumOfCommand(50);
		assertEquals(50, planet.getNumOfCommand());
	}

	@Test
	public void testSetNumOfCommand() {
		planet.setNumOfCommand(50);
		assertEquals(50, planet.getNumOfCommand());
	}

	@Test
	public void testAddResources() {
		planet.addResources();
	}

}
