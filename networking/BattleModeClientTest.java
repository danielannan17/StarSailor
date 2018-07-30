package networking;


import static org.junit.Assert.*;
import java.awt.Color;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


//Also tests ClientSender and ServerReceiver, which cannot be tested on their own
public class BattleModeClientTest {
	
	
	private BattleModeServer server;
	private BattleModeClient c1, c2;

	
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
	public void testColorToString() {
		Color red = Color.RED;
		Color red2 = BattleModeClient.stringToColor(BattleModeClient.colorToString(red));
		assertEquals(red, red2);
		Color blue = Color.BLUE;
		Color blue2 = BattleModeClient.stringToColor(BattleModeClient.colorToString(blue));
		assertEquals(blue, blue2);
	}
	
	
	@Test
	public void testLeave() {
		c2.leave();
		assertEquals(server.getState(), ServerState.WAITING);
	}
	
	
	@Test
	public void testGetState() {
		BattleModeClient.setState(ServerState.READY);
		assertEquals(BattleModeClient.getState(), ServerState.READY);
		BattleModeClient.setState(ServerState.WAITING);
		assertEquals(BattleModeClient.getState(), ServerState.WAITING);
	}

	
	@Test
	public void testSetState() {
		BattleModeClient.setState(ServerState.READY);
		assertEquals(BattleModeClient.getState(), ServerState.READY);
		BattleModeClient.setState(ServerState.WAITING);
		assertEquals(BattleModeClient.getState(), ServerState.WAITING);
	}

	
	@Test
	public void testStringToColor() {
		assertEquals(1,1);
		Color red = Color.RED;
		Color red2 = BattleModeClient.stringToColor(BattleModeClient.colorToString(red));
		assertEquals(red, red2);
		Color blue = Color.BLUE;
		Color blue2 = BattleModeClient.stringToColor(BattleModeClient.colorToString(blue));
		assertEquals(blue, blue2);
	}

	
	@Test
	public void testSendKeys() throws InterruptedException {
		Object[] keys = (Object[]) new KeyVal[]{KeyVal.A};
		c1.sendKeys(keys);
		Thread.sleep(100);
		assertArrayEquals((Object[]) BattleModeServer.getPlayer1Move(), keys);
		BattleModeServer.changeState(ServerState.WAITING);
	}

	
	@Test
	public void testSendReady() throws InterruptedException {
		c2.sendReady(new int[]{1, 2}, new int[]{2,3}, Color.RED);
		Thread.sleep(100);
		assertArrayEquals(BattleModeServer.getPlayer2Ships(), new int[]{1,2});
		assertArrayEquals(BattleModeServer.getPlayer2Skills(), new int[]{2,3});
		assertEquals(BattleModeServer.getPlayer2Colour(), Color.RED);
		BattleModeServer.changeState(ServerState.WAITING);
	}

	

}
