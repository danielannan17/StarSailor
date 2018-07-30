package menu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextArea;

public class HoverPopUp extends JTextArea {
	
	public HoverPopUp() {
		setEditable(false);
		setLineWrap(true);
		setWrapStyleWord(true);
		setBackground(new Color(0,0,0,123));
		setFont(new Font("Verdana",Font.BOLD,14));
		setForeground(Color.WHITE);
		setVisible(false);
	}

}
