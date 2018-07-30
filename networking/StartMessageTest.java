package networking;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class StartMessageTest {

	
	@Test
	public void testStartMessage() {
		Message msg = new StartMessage();
		assertEquals(msg.type, MessageType.START);
		assertEquals(msg.initData, null);
	}

	
}
