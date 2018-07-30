package menu;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import handlers.InputHandler;
import handlers.ResourceHandler;

public class BattleButton extends JButton {
	
	BattleButton(int type, int x, int y, int width, int height) {
		Image img = null;
		switch (type) {
		case 1:
			img = ResourceHandler.getImage("gui/up_arrow");
			break;
		case 2:
			img = ResourceHandler.getImage("gui/down_arrow");
			break;
		case 3:
			img = ResourceHandler.getImage("gui/plus");
			break;
		case 4:
			img = ResourceHandler.getImage("gui/minus");
			break;
		}
		img = img.getScaledInstance(width, height, 1);
		ImageIcon ico = new ImageIcon(img);
		setIcon(ico);
		this.setBounds(x, y, width, height);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
	}
	
	

}
