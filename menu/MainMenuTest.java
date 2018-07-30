package menu;

import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {
	
	MainMenu main;
	
	@Before
	public void setUp(){
		main = new MainMenu("backgrounds/space");
	}

	@Test
	public void testUpdateFloat() {
		main.update(0);
	}

}
