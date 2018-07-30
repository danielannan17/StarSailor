package menu;

import org.junit.Before;
import org.junit.Test;

public class OptionsMenuTest {
	
	OptionsMenu menu;
	
	@Before
	public void setUp(){
		menu = new OptionsMenu("backgrounds/space", 50);
	}

	@Test
	public void testUpdateFloat() {
		menu.update(0);
	}

	@Test
	public void testGetVolume() {
		menu.getVolume();
	}

}
