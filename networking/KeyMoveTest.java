package networking;


import static org.junit.Assert.*;
import org.junit.Test;


public class KeyMoveTest {

	
	@Test
	public void testKeyMove() {
		KeyMove kmv = new KeyMove(KeyVal.A, 1);
		assertEquals(kmv.key, KeyVal.A);
		assertEquals(kmv.playerID, 1);
	}

	
}
