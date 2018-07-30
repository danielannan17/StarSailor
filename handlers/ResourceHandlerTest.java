package handlers;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class ResourceHandlerTest {

	@Test
	public void testGetImage() {
		ResourceHandler.getImage("backgrounds/space");
	}

	@Test
	public void testGetBufferedImage() {
		ResourceHandler.getBufferedImage("backgrounds/space");
	}

	@Test
	public void testGetPlayerSprites() {
		ResourceHandler.getPlayerSprites("backgrounds/space", 16, 16);
	}

	@Test
	public void testGetBlockSprites() {
		ResourceHandler.getBlockSprites("water/water_river", 16, 16);
	}

	@Test
	public void testConvertToBuffered() {
		ResourceHandler.convertToBuffered(ResourceHandler.getBlockSprites("water/water_river", 16, 16));
	}

	@Test
	public void testConvertSpriteSheet() {
		ResourceHandler.convertSpriteSheet(ResourceHandler.getBufferedImage("backgrounds/space"), 16, 16);
	}

	@Test
	public void testGetSound() {
		ResourceHandler.getSound("sound/laser/laser.wav");
	}

	@Test
	public void testOpenCloseInputStream() {
		try {
			ResourceHandler.openInputStream("test.txt");
			ResourceHandler.getLineOfTextFromFile();
			ResourceHandler.getAllTextFromFile();
			ResourceHandler.closeInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
