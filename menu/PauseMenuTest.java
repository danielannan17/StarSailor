package menu;

import org.junit.Before;
import org.junit.Test;

public class PauseMenuTest {
	
	PauseMenu p;
	
	@Before
	public void setUp(){
		p = new PauseMenu("backgrounds/space");
	}

	@Test
	public void testUpdateFloat() {
		p.update(0);
	}

}
