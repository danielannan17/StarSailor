package networking;


import static org.junit.Assert.*;
import java.awt.Color;
import org.junit.Test;


public class InitMessageTest {


	@Test
	public void testInitMessage() {
		Message msg = new InitMessage(new InitData(1, Color.RED));
		assertEquals(msg.type, MessageType.INITDATA);
		assertEquals(msg.initData.colour, Color.RED);
		assertEquals(msg.initData.type, 1);
				
	}

}
