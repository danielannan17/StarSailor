package menu;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class PlayMenuTest {
	
	PlayMenu p;
	
	@Before
	public void setUp(){
		p = new PlayMenu("backgrounds/space");
	}

	@Test
	public void testUpdateFloat() {
		p.update(0);
	}

	@Test
	public void testGetName() {
		p.getName();
	}

	@Test
	public void testGetSeed() {
		p.getSeed();
	}

	@Test
	public void testGetIP() {
		p.getIP();
	}

	@Test
	public void testGetPort() {
		p.getPort();
	}

	@Test
	public void testGetOffline() {
		p.getOffline();
	}

}
