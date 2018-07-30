package menu;

import org.junit.Before;
import org.junit.Test;

public class BattleStartTest {
	
	BattleStart b;
	
	@Before
	public void setUp(){
		b = new BattleStart("backgrounds/space", "test", false);
	}
	
	@Test
	public void testBattleStart(){
		b = new BattleStart("backgrounds/space", "test", true);
	}

	@Test
	public void testUpdateFloat() {
		b.update(0);
	}

	@Test
	public void testShowUnitCount() {
		b.setShowing(true);
		b.showUnitCount(0);
		b.setShowing(false);
		b.showUnitCount(0);
	}

	@Test
	public void testNoCrystal() {
		b.setShowing(true);
		b.noCrystal();
		b.setShowing(false);
		b.noCrystal();
	}

	@Test
	public void testGetShowing() {
		b.getShowing();
	}

	@Test
	public void testSetShowing() {
		b.setShowing(true);
	}

	@Test
	public void testShowTrading() {
		b.showTrading(0);
		b.showTrading(1);
		b.showTrading(2);
	}

	@Test
	public void testShowBribe() {
		b.showBribe(true, 0);
		b.showBribe(false, 0);
	}

	@Test
	public void testGetBribed() {
		b.getBribed();
	}

}
