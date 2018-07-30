package generators;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class PerlinNoiseGeneratorTest {

	PerlinNoiseGenerator generator1, generator2;

	@Before
	public void setUp() {
		generator1 = new PerlinNoiseGenerator(0);
		generator2 = new PerlinNoiseGenerator(0);
	}

	@Test
	public void testGetPerlinNoise() {
		float[][] noise1 = generator1.getPerlinNoise(100, 100, 4, 5);
		float[][] noise2 = generator2.getPerlinNoise(100, 100, 4, 5);
		// testing that it gets the same noise for the same seed, height, width
		// and octaves
		assertArrayEquals(noise1, noise2);
	}

}
