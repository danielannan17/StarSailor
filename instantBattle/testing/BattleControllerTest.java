package instantBattle.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import entities.Entity;
import entities.Ship;
import entities.Vector2D;
import instantBattle.BattleController;
import networking.BattleModeClient;
import networking.BattleModeServer;
import networking.KeyVal;

public class BattleControllerTest {

	private BattleController controller;
	private Thread thread;
	private BattleModeClient client1;
	private BattleModeServer server;
	private BattleModeClient client2;
	private Thread serverThread;
	private Thread client2Thread;
	private Thread client1Thread;
	 	
	@Before
	public void setUp() throws Exception {
			server = new BattleModeServer();
			serverThread = new Thread(server);
			serverThread.start();
			client1 = new BattleModeClient(1, "server", server.getIP(), server.getPort());
			client2 = new BattleModeClient(1, "server", server.getIP(), server.getPort());
			client1Thread = new Thread(client1);
			client2Thread = new Thread(client1);
			client1Thread.start();
			client2Thread.start();
	}
	

	@After
	public void tearDown() throws Exception {
		server.kill();
		controller.team1fleet.clear();
		controller.team2fleet.clear();
		thread.stop();
		serverThread.stop();
		server = null;
		controller.endBattle();
		thread = null;
		client1.kill();
		client2.kill();
		client1 = null;
		client2 = null;
		client2Thread.stop();
		client1Thread.stop();
		client2Thread = null;
		client1Thread = null;
		
	}

	@Test
	public void testShipCollisions() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertEquals(controller.player1.getHealth(), controller.player1.getMaxHealth(),0.001);
		controller.player1.setPosition(new Vector2D(300, 300));
		controller.player2.setPosition(new Vector2D(300, 300));
		controller.player1.setLocation(300, 300);
		controller.player2.setLocation(300, 300);
		controller.player1.moveDown();
		assertEquals(controller.player1.getxLocation(), controller.player2.getxLocation());
		assertEquals(controller.player1.getyLocation(), controller.player2.getyLocation());
		assertTrue(controller.player1.getHitBox().intersects(controller.player2.getHitBox()));
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
				
		
			}
		}, 2000);
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
		System.out.println(controller.player1.getHealth());
		assertNotEquals(controller.player1.getMaxHealth(),controller.player1.getHealth(),0.001);
		assertNotEquals(controller.player2.getMaxHealth(),controller.player2.getHealth(),0.001);
	}
	
	@Test
	public void testShipBasicProjectileCollisions() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertEquals(controller.player1.getHealth(), controller.player1.getMaxHealth(),0.001);
		controller.player1.setPosition(new Vector2D(280, 280));
		controller.player2.setPosition(new Vector2D(300, 300));
		controller.player1.setLocation(280, 280);
		controller.player2.setLocation(300, 300);
		assertEquals(controller.player2.getMaxHealth(),controller.player2.getHealth(),0.001);
		controller.player1.useSkill(0);
		assertFalse(controller.player1.getBasic().canDoSkill());
	
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
		
			}
		}, 1000);
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
		assertEquals((float) (controller.player2.getMaxHealth()-controller.player1.getAttack()),controller.player2.getHealth(),0.001);
	}
	
	@Test
	public void testShipDotProjectileCollisions() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertEquals(controller.player1.getHealth(), controller.player1.getMaxHealth(),0.001);
		controller.player1.setPosition(new Vector2D(280, 280));
		controller.player2.setPosition(new Vector2D(300, 300));
		controller.player1.setLocation(280, 280);
		controller.player2.setLocation(300, 300);
		controller.player1.setDirection(-Vector2D.convertToDirection(controller.player1.getxLocation(), controller.player1.getyLocation(),
				controller.player1.getxLocation(), controller.player1.getyLocation()));
		assertEquals(controller.player2.getMaxHealth(),controller.player2.getHealth(),0.001);
		controller.player1.useSkill(1);
		assertFalse(controller.player1.getSkill1().canDoSkill());
		
		
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
		
			}
		}, 1000);
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
		assertNotEquals((float) controller.player2.getMaxHealth(),controller.player2.getHealth(),0.001);
	}
	
	@Test
	public void testShipPenetratingProjectileCollisions() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertEquals(controller.player1.getHealth(), controller.player1.getMaxHealth(),0.001);
		controller.player1.setPosition(new Vector2D(280, 280));
		controller.player2.setPosition(new Vector2D(300, 300));
		controller.player1.setLocation(280, 280);
		controller.player2.setLocation(300, 300);
		controller.player1.setDirection(-Vector2D.convertToDirection(controller.player1.getxLocation(), controller.player1.getyLocation(),
				controller.player1.getxLocation(), controller.player1.getyLocation()));
		assertEquals(controller.player2.getMaxHealth(),controller.player2.getHealth(),0.001);
		controller.player1.useSkill(2);
		assertFalse(controller.player1.getSkill2().canDoSkill());
		
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
				
		
			}
		}, 1000);
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
		assertNotEquals((float) controller.player2.getMaxHealth(),controller.player2.getHealth(),0.001);
	}
	
	@Test
	public void testGameOver() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertFalse(controller.isGameOver());
		controller.team1fleet.clear();
		assertTrue(controller.isGameOver());
	}
	@Test
	public void testDraw() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertEquals(0,controller.battleWinner());
		controller.team1fleet.clear();
		controller.team2fleet.clear();
		assertEquals(3,controller.battleWinner());
	}
	@Test
	public void testTeam1Win() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertEquals(0,controller.battleWinner());
		controller.team2fleet.clear();
		assertEquals(1,controller.battleWinner());
	}
	@Test
	public void testTeam2Win() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertEquals(0,controller.battleWinner());
		controller.team1fleet.clear();
		assertEquals(2,controller.battleWinner());
	}
	
	@Test
	public void testProjectileLeaveMapNorth() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		controller.player1.setPosition(new Vector2D(40, 40));
		controller.player1.setLocation(40, 40);
		controller.player1.setDirection(Math.toRadians(-90));
		controller.player1.useSkill(0);
		controller.update();
		assertTrue(!controller.others.isEmpty());
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
				
		
			}
		}, 2000);
		try {
			thread.join(5000);
		} catch (InterruptedException e) {
			System.out.println("Could not join");
		}
		assertTrue(controller.others.isEmpty());

	}
	@Test
	public void testProjectileLeaveMapWest() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		controller.player1.setPosition(new Vector2D(40, 40));
		controller.player1.setLocation(40, 40);
		controller.player1.setDirection(Math.toRadians(180));
		controller.player1.useSkill(0);
		controller.update();
		assertTrue(!controller.others.isEmpty());
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
				
		
			}
		}, 2000);
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.out.println("Could not join");
		}
		assertTrue(controller.others.isEmpty());

	}
	@Test
	public void testProjectileLeaveMapSouth() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		controller.player1.setPosition(new Vector2D(controller.width-40, controller.height-40));
		controller.player1.setLocation(controller.width-40, controller.height-40);
		controller.player1.setDirection(Math.toRadians(90));
		controller.player1.useSkill(0);
		controller.update();
		assertTrue(!controller.others.isEmpty());
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
				
		
			}
		}, 2000);
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.out.println("Could not join");
		}
		assertTrue(controller.others.isEmpty());

	}
	@Test
	public void testProjectileLeaveMapEast() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		controller.player1.setPosition(new Vector2D(controller.width-40, controller.height-40));
		controller.player1.setLocation(controller.width-40, controller.height-40);
		controller.player1.setDirection(Math.toRadians(0));
		controller.player1.useSkill(0);
		controller.update();
		assertTrue(!controller.others.isEmpty());
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
				
		
			}
		}, 2000);
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.out.println("Could not join");
		}
		assertTrue(controller.others.isEmpty());

	}
	
	@Test
	public void testFlash() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		controller.player2.setPosition(new Vector2D(40, 40));
		controller.player2.setLocation(40, 40);
		controller.player2.setDirection(Math.toRadians(0));
		assertEquals(40,controller.player2.getxLocation(),0.001);
		assertEquals(40,controller.player2.getyLocation(),0.001);
		controller.player2.useSkill(2);
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
				
		
			}
		}, 2000);
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(240,controller.player2.getxLocation(),0.001);
		assertEquals(40,controller.player2.getyLocation(),0.001);
		assertFalse(controller.player2.getSkill2().canDoSkill());
		
	}
	
	@Test
	public void testDash() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		controller.player2.setPosition(new Vector2D(40, 40));
		controller.player2.setLocation(40, 40);
		controller.player2.useSkill(1);
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
				
		
			}
		}, 2000);
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(controller.player2.getxLocation());
		assertFalse(controller.player2.getSkill1().canDoSkill());
	}
	
	@Test
	public void damageTest() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertEquals(0,controller.player2.getArmour(),0.001);
		controller.player2.useSkill(3);
		System.out.println(controller.player2.getMaxHealth());
		assertEquals(100,controller.player2.getArmour(),0.001);
		controller.player2.takeDamage(50);
		assertEquals(50,controller.player2.getArmour(),0.001);
		assertEquals(controller.player2.getMaxHealth(),controller.player2.getHealth(),0.001);
		controller.player2.takeDamage(70);
		assertEquals(controller.player2.getMaxHealth()-20,controller.player2.getHealth(),0.001);
		assertEquals(0,controller.player2.getArmour(),0.001);
		controller.player2.takeDamage(100);
		controller.update();
		assertEquals(-20,controller.player2.getHealth(),0.001);
		assertTrue(controller.team2fleet.isEmpty());
	}
	
	@Test
	public void deactivateArmourTest() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertEquals(0,controller.player2.getArmour(),0.001);
		try {
			Thread.sleep(13000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		controller.player2.useSkill(3);
		assertEquals(100,controller.player2.getArmour(),0.001);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		controller.player2.deactiveSkills();
		assertEquals(0,controller.player2.getArmour(),0);
	}
	
	@Test
	public void handlePlayer1and2UpInput() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		controller.player1.setPosition(new Vector2D(40, 40));
		controller.player1.setLocation(40, 40);
		controller.player2.setPosition(new Vector2D(100, 100));
		controller.player2.setLocation(100, 100);
		assertEquals(40,controller.player1.getxLocation(),0.001);
		assertEquals(40,controller.player1.getyLocation(),0.001);
		assertEquals(100,controller.player2.getxLocation(),0.001);
		assertEquals(100,controller.player2.getyLocation(),0.001);
		BattleModeServer.setPlayer1Move(new KeyVal[]{KeyVal.W});
		BattleModeServer.setPlayer2Move(new KeyVal[]{KeyVal.W});
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
		
			}
		}, 1000);
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
		assertEquals(40,controller.player1.getxLocation(),0.001);
		assertTrue(controller.player1.getyLocation() < 40);
		assertEquals(100,controller.player2.getxLocation(),0.001);
		assertTrue(controller.player2.getyLocation() < 100);
	}

	@Test
	public void handlePlayer1and2DownInput() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		controller.player1.setPosition(new Vector2D(40, 40));
		controller.player1.setLocation(40, 40);
		controller.player2.setPosition(new Vector2D(100, 100));
		controller.player2.setLocation(100, 100);
		assertEquals(40,controller.player1.getxLocation(),0.001);
		assertEquals(40,controller.player1.getyLocation(),0.001);
		assertEquals(100,controller.player2.getxLocation(),0.001);
		assertEquals(100,controller.player2.getyLocation(),0.001);
		BattleModeServer.setPlayer1Move(new KeyVal[]{KeyVal.S});
		BattleModeServer.setPlayer2Move(new KeyVal[]{KeyVal.S});
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
		
			}
		}, 1000);
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
		assertEquals(40,controller.player1.getxLocation(),0.001);
		assertTrue(controller.player1.getyLocation() > 40);
		assertEquals(100,controller.player2.getxLocation(),0.001);
		assertTrue(controller.player2.getyLocation() > 100);
	}
	
	@Test
	public void handlePlayer1and2RightInput() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		controller.player1.setPosition(new Vector2D(40, 40));
		controller.player1.setLocation(40, 40);
		controller.player2.setPosition(new Vector2D(100, 100));
		controller.player2.setLocation(100, 100);
		assertEquals(40,controller.player1.getxLocation(),0.001);
		assertEquals(40,controller.player1.getyLocation(),0.001);
		assertEquals(100,controller.player2.getxLocation(),0.001);
		assertEquals(100,controller.player2.getyLocation(),0.001);
		BattleModeServer.setPlayer1Move(new KeyVal[]{KeyVal.D});
		BattleModeServer.setPlayer2Move(new KeyVal[]{KeyVal.D});
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
		
			}
		}, 1000);
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
		assertEquals(40,controller.player1.getyLocation(),0.001);
		assertTrue(controller.player1.getxLocation() > 40);
		assertEquals(100,controller.player2.getyLocation(),0.001);
		assertTrue(controller.player2.getxLocation() > 100);
		
	}
	
	@Test
	public void handlePlayer1and2LeftInput() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		controller.player1.setPosition(new Vector2D(40, 40));
		controller.player1.setLocation(40, 40);
		controller.player2.setPosition(new Vector2D(100, 100));
		controller.player2.setLocation(100, 100);
		assertEquals(40,controller.player1.getxLocation(),0.001);
		assertEquals(40,controller.player1.getyLocation(),0.001);
		assertEquals(100,controller.player2.getxLocation(),0.001);
		assertEquals(100,controller.player2.getyLocation(),0.001);
		BattleModeServer.setPlayer1Move(new KeyVal[]{KeyVal.A});
		BattleModeServer.setPlayer2Move(new KeyVal[]{KeyVal.A});
		new Timer().schedule(new TimerTask() {
			public void run() {
				controller.endBattle();
				System.out.println("run");
				thread.stop();
		
			}
		}, 1000);
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
		assertEquals(40,controller.player1.getyLocation(),0.001);
		assertTrue(controller.player1.getxLocation() < 40);
		assertEquals(100,controller.player2.getyLocation(),0.001);
		assertTrue(controller.player2.getxLocation() < 100);	
	}
	
	@Test
	public void handlePlayer1and2BtnInput() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertTrue(controller.player1.getBasic().canDoSkill());
		assertTrue(controller.player2.getBasic().canDoSkill());
		BattleModeServer.setPlayer1Move(new KeyVal[]{KeyVal.BTN});
		BattleModeServer.setPlayer2Move(new KeyVal[]{KeyVal.BTN});
		controller.update();
		assertFalse(controller.player1.getBasic().canDoSkill());
		assertFalse(controller.player2.getBasic().canDoSkill());
	}
	
	@Test
	public void handlePlayer1and2QInput() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertTrue(controller.player1.getSkill1().canDoSkill());
		assertTrue(controller.player2.getSkill1().canDoSkill());
		BattleModeServer.setPlayer1Move(new KeyVal[]{KeyVal.Q});
		BattleModeServer.setPlayer2Move(new KeyVal[]{KeyVal.Q});
		controller.update();
		assertFalse(controller.player1.getSkill1().canDoSkill());
		assertFalse(controller.player2.getSkill1().canDoSkill());
	}
	
	@Test
	public void handlePlayer1and2EInput() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertTrue(controller.player1.getSkill1().canDoSkill());
		assertTrue(controller.player2.getSkill1().canDoSkill());
		BattleModeServer.setPlayer1Move(new KeyVal[]{KeyVal.E});
		BattleModeServer.setPlayer2Move(new KeyVal[]{KeyVal.E});
		controller.update();
		assertFalse(controller.player1.getSkill2().canDoSkill());
		assertFalse(controller.player2.getSkill2().canDoSkill());
	}
	
	@Test
	public void handlePlayer1and2ShiftInput() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertTrue(controller.player1.getUtility().canDoSkill());
		assertTrue(controller.player2.getUtility().canDoSkill());
		BattleModeServer.setPlayer1Move(new KeyVal[]{KeyVal.SHIFT});
		BattleModeServer.setPlayer2Move(new KeyVal[]{KeyVal.SHIFT});
		controller.update();
		assertFalse(controller.player1.getUtility().canDoSkill());
		assertFalse(controller.player2.getUtility().canDoSkill());
	}
	
	@Test
	public void testPlayer1Death() {
		int[] player1 = new int[]{1,2,3,4,1};
		int[] player2 = new int[]{2,1,5,6,2};
		int[] player1fleet = new int[]{0,0};
		int[] player2fleet = new int[]{0,0};
		controller = new BattleController(player1, player1fleet, player2, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		
		assertTrue(controller.team1fleet.contains(controller.player1));
		controller.player1.takeDamage(1000);
		controller.update();
		assertFalse(controller.team1fleet.contains(controller.player1));
	}
	
	@Test
	public void testFleetSetUp() {
		int[] player1fleet = new int[]{4,5};
		int[] player2fleet = new int[]{7,6};
		controller = new BattleController(null, player1fleet, null, player2fleet); 
		thread = new Thread(controller);
		thread.start();
		assertEquals(9, controller.team1fleet.size());
		assertEquals(13, controller.team2fleet.size());
		int team1type1count = 0;
		int team1type2count = 0;
		for (Entity ship: controller.team1fleet) {
			if (((Ship) ship).getType() == 1) {
				team1type1count++;
			} else if (((Ship) ship).getType() == 2) {
				team1type2count++;
			}
		}
		assertEquals(4,team1type1count);
		assertEquals(5,team1type2count);
	}
	
	
	
	
}
