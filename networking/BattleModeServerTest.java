package networking;


import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


//Also tests ServerSender and ClientReceiver, which cannot be tested on their own
public class BattleModeServerTest {

	
	BattleModeServer server;
	BattleModeClient c1, c2;
	
	
	@Before
	public void setUp() throws Exception {
		server = new BattleModeServer();
		(new Thread(server)).start();
		
		c1 = new BattleModeClient(1, "c1", server.getIP(), server.getPort());
		c2 = new BattleModeClient(2, "c2", server.getIP(), server.getPort());
	}

	
	@After
	public void tearDown() throws Exception {
		c1.kill();
		c2.kill();
		server.kill();
	}

	
	@Test
	public void testAvailableID() {
		assertEquals(BattleModeServer.availableID(), -1);
	}

	
	@Test
	public void testChangeState() {
		ServerState s = server.getState();
		BattleModeServer.changeState(ServerState.INITIALISING);
		assertEquals(ServerState.INITIALISING, server.getState());
		BattleModeServer.changeState(s);
		assertEquals(s, server.getState());
	}

	
	@Test
	public void testGetPlayer1Move() {
		BattleModeServer.setPlayer1Move(new KeyVal[]{KeyVal.A, KeyVal.Q});
		assertArrayEquals(BattleModeServer.getPlayer1Move(), new KeyVal[]{KeyVal.A, KeyVal.Q});
		BattleModeServer.changeState(ServerState.WAITING);
	}

	
	@Test
	public void testGetPlayer1X() {
		BattleModeServer.setPlayer1X(100);
		assertEquals(100, BattleModeServer.getPlayer1X());
	}

	
	@Test
	public void testGetPlayer1Y() {
		BattleModeServer.setPlayer1Y(200);
		assertEquals(200, BattleModeServer.getPlayer1Y());
	}

	
	@Test
	public void testGetPlayer2Move() {
		BattleModeServer.setPlayer2Move(new KeyVal[]{KeyVal.W, KeyVal.SHIFT});
		assertArrayEquals(BattleModeServer.getPlayer2Move(), new KeyVal[]{KeyVal.W, KeyVal.SHIFT});
	}

	
	@Test
	public void testGetPlayer2Ships() throws InterruptedException {
		c2.sendReady(new int[]{1,2}, new int[]{3,4}, Color.BLUE);
		Thread.sleep(100);
		assertArrayEquals(new int[]{1,2}, BattleModeServer.getPlayer2Ships());
		BattleModeServer.changeState(ServerState.WAITING);
	}

	
	@Test
	public void testGetPlayer2Skills() throws InterruptedException {
		c2.sendReady(new int[]{1,2}, new int[]{3,4}, Color.BLUE);
		Thread.sleep(100);
		assertArrayEquals(new int[]{3,4}, BattleModeServer.getPlayer2Skills());
		BattleModeServer.changeState(ServerState.WAITING);
	}

	
	@Test
	public void testGetPlayer2X() {
		BattleModeServer.setPlayer2X(300);
		assertEquals(300, BattleModeServer.getPlayer2X());
	}

	
	@Test
	public void testGetPlayer2Y() {
		BattleModeServer.setPlayer2Y(500);
		assertEquals(500, BattleModeServer.getPlayer2Y());
	}

	
	@Test
	public void testMakeReady() {
		BattleModeServer.makeReady(2, new int[]{3,1,4}, new int[]{1,5,2}, Color.ORANGE);
		assertArrayEquals(BattleModeServer.getPlayer2Ships(), new int[]{3,1,4});
		assertArrayEquals(BattleModeServer.getPlayer2Skills(), new int[]{1,5,2});
		assertEquals(BattleModeServer.getPlayer2Colour(), Color.ORANGE);
		BattleModeServer.changeState(ServerState.WAITING);
	}

	
	@Test
	public void testSetPlayer1Move() {
		BattleModeServer.setPlayer1Move(new KeyVal[]{KeyVal.A, KeyVal.Q});
		assertArrayEquals(BattleModeServer.getPlayer1Move(), new KeyVal[]{KeyVal.A, KeyVal.Q});
		BattleModeServer.changeState(ServerState.WAITING);
	}

	
	@Test
	public void testSetPlayer1X() {
		BattleModeServer.setPlayer1X(150);
		assertEquals(150, BattleModeServer.getPlayer1X());
	}

	
	@Test
	public void testSetPlayer1Y() {
		BattleModeServer.setPlayer1Y(250);
		assertEquals(250, BattleModeServer.getPlayer1Y());
	}

	
	@Test
	public void testSetPlayer2Move() {
		BattleModeServer.setPlayer2Move(new KeyVal[]{KeyVal.D, KeyVal.S});
		assertArrayEquals(BattleModeServer.getPlayer2Move(), new KeyVal[]{KeyVal.D, KeyVal.S});
		BattleModeServer.changeState(ServerState.WAITING);
	}

	
	@Test
	public void testSetPlayer2X() {
		BattleModeServer.setPlayer2X(350);
		assertEquals(350, BattleModeServer.getPlayer2X());
	}

	
	@Test
	public void testSetPlayer2Y() {
		BattleModeServer.setPlayer2Y(550);
		assertEquals(550, BattleModeServer.getPlayer2Y());
	}

	
	@Test
	public void testGetState() {
		ServerState s = server.getState();
		BattleModeServer.changeState(ServerState.INITIALISING);
		assertEquals(ServerState.INITIALISING, server.getState());
		BattleModeServer.changeState(s);
		assertEquals(s, server.getState());
	}

	
}
