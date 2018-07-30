package messaging;

import static org.junit.Assert.assertFalse;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;

public class PopUpTest {

	PopUp popUp;

	@Before
	public void setUp() {
		popUp = new PopUp("hello");
	}

	@Test
	public void testClicked() {
		Point p = new Point(0, 0);
		assertFalse(popUp.clicked(p));
	}

	@Test
	public void testDraw() {
		popUp.draw((Graphics2D) (new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics()));
	}

}
