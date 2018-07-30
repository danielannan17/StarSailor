package networking;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class KeyValTest {

	
	@Test
	public void test() {
		//	W, A, S, D, Q, E, SHIFT, BTN;
		assertEquals(KeyVal.fromString(KeyVal.toString(KeyVal.W)), KeyVal.W);
		assertEquals(KeyVal.fromString(KeyVal.toString(KeyVal.A)), KeyVal.A);
		assertEquals(KeyVal.fromString(KeyVal.toString(KeyVal.S)), KeyVal.S);
		assertEquals(KeyVal.fromString(KeyVal.toString(KeyVal.D)), KeyVal.D);
		assertEquals(KeyVal.fromString(KeyVal.toString(KeyVal.Q)), KeyVal.Q);
		assertEquals(KeyVal.fromString(KeyVal.toString(KeyVal.E)), KeyVal.E);
		assertEquals(KeyVal.fromString(KeyVal.toString(KeyVal.SHIFT)), KeyVal.SHIFT);
		assertEquals(KeyVal.fromString(KeyVal.toString(KeyVal.BTN)), KeyVal.BTN);

		assertEquals(KeyVal.toString(KeyVal.fromString("W")), "W");
		assertEquals(KeyVal.toString(KeyVal.fromString("A")), "A");
		assertEquals(KeyVal.toString(KeyVal.fromString("S")), "S");
		assertEquals(KeyVal.toString(KeyVal.fromString("D")), "D");
		assertEquals(KeyVal.toString(KeyVal.fromString("Q")), "Q");
		assertEquals(KeyVal.toString(KeyVal.fromString("E")), "E");
		assertEquals(KeyVal.toString(KeyVal.fromString("SHIFT")), "SHIFT");
		assertEquals(KeyVal.toString(KeyVal.fromString("BTN")), "BTN");	
		}

	
}