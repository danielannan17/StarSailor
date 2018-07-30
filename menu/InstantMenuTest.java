package menu;

import static org.junit.Assert.assertEquals;

import java.awt.Image;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entities.AvailableSkill;
import entities.Selector;
import handlers.InputHandler;
import instantBattle.Battle;
import main.Main;

public class InstantMenuTest {

	static JFrame frame;
	static InstantMenu instant;
	static Battle battle;
	private static Robot r;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Main.projectileCache = new HashMap<String,BufferedImage[][]>();
		Main.cache = new HashMap<String,Image[]>();
		Selector.initialise();
		AvailableSkill.initialise();
		frame = new JFrame();
		frame.setBounds(0,0,InputHandler.screenSize.width,InputHandler.screenSize.height);
		frame.setVisible(true);
		instant = new InstantMenu("backgrounds/space");
		frame.add(instant);
		instant.setSize(InputHandler.screenSize.width, InputHandler.screenSize.height);	
		r = new Robot();  
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		Thread.sleep(1000);
	}

	@Test
	public void testMenuDrawn() {
		instant.setSize(InputHandler.screenSize.width, InputHandler.screenSize.height);	
	}
	
	@Test
	public void testChangeShipButtons() {
		assertEquals(1,instant.ship);
		Thread thread = new Thread() {
			public void run() {
				r.mouseMove(instant.playerSelectorPanel.getX()+instant.nextShip.getX()+30,
						instant.playerSelectorPanel.getY()+instant.nextShip.getY()+30);
				r.mousePress(1024);
				r.mouseRelease(1024);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		};
		try {
			thread.start();
			thread.join();
		} catch (InterruptedException e) {
		}
		
		assertEquals(2,instant.ship);
		
		Thread threadx = new Thread() {
			public void run() {
				r.mouseMove(instant.playerSelectorPanel.getX()+instant.prevShip.getX()+30,
						instant.playerSelectorPanel.getY()+instant.prevShip.getY()+30);
				r.mousePress(1024);
				r.mouseRelease(1024);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		};
		try {
			threadx.start();
			threadx.join();
		} catch (InterruptedException e) {
		}
		assertEquals(1,instant.ship);
	}
	
	@Test
	public void testChangeSkill1ButtonsChangeSkill1Value() {
		assertEquals(0,instant.skill1);
		Thread thread = new Thread() {
			public void run() {
				r.mouseMove(instant.playerSelectorPanel.getX()+instant.nextSkill1.getX()+100,
						instant.playerSelectorPanel.getY()+instant.nextSkill1.getY()+30);
				r.mousePress(1024);
				r.mouseRelease(1024);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		};
		try {
			thread.start();
			thread.join();
		} catch (InterruptedException e) {
		}
		
		assertEquals(1,instant.skill1);
		
		Thread threadx = new Thread() {
			public void run() {
				r.mouseMove(instant.playerSelectorPanel.getX()+instant.prevSkill1.getX()+100,
						instant.playerSelectorPanel.getY()+instant.prevSkill1.getY()+30);
				r.mousePress(1024);
				r.mouseRelease(1024);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		};
		try {
			threadx.start();
			threadx.join();
		} catch (InterruptedException e) {
		}
		assertEquals(0,instant.skill1);
	}
	
	@Test
	public void testChangeSkill2ButtonsChangeSkill2Value() {
		assertEquals(0,instant.skill2);
		Thread thread = new Thread() {
			public void run() {
				r.mouseMove(instant.playerSelectorPanel.getX()+instant.nextskill2.getX()+170,
						instant.playerSelectorPanel.getY()+instant.nextskill2.getY()+30);
				r.mousePress(1024);
				r.mouseRelease(1024);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		};
		try {
			thread.start();
			thread.join();
		} catch (InterruptedException e) {
		}
		
		assertEquals(1,instant.skill2);
		
		Thread threadx = new Thread() {
			public void run() {
				r.mouseMove(instant.playerSelectorPanel.getX()+instant.prevskill2.getX()+170,
						instant.playerSelectorPanel.getY()+instant.prevskill2.getY()+30);
				r.mousePress(1024);
				r.mouseRelease(1024);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		};
		try {
			threadx.start();
			threadx.join();
		} catch (InterruptedException e) {
		}
		assertEquals(0,instant.skill2);
	}
	
	@Test
	public void testChangeSkill3ButtonsChangeSkill3Value() {
		assertEquals(0,instant.skill3);
		Thread thread = new Thread() {
			public void run() {
				r.mouseMove(instant.playerSelectorPanel.getX()+instant.nextskill3.getX()+230,
						instant.playerSelectorPanel.getY()+instant.nextskill3.getY()+30);
				r.mousePress(1024);
				r.mouseRelease(1024);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		};
		try {
			thread.start();
			thread.join();
		} catch (InterruptedException e) {
		}
		
		assertEquals(1,instant.skill3);
		
		Thread threadx = new Thread() {
			public void run() {
				r.mouseMove(instant.playerSelectorPanel.getX()+instant.prevskill3.getX()+230,
						instant.playerSelectorPanel.getY()+instant.prevskill3.getY()+30);
				r.mousePress(1024);
				r.mouseRelease(1024);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		};
		try {
			threadx.start();
			threadx.join();
		} catch (InterruptedException e) {
		}
		assertEquals(0,instant.skill3);
	}

}
