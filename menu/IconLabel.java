package menu;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class IconLabel extends JLabel {
	protected long prevTime, animationTime;
	Image[] sprites;
	int width, height;
	int frame;
	
	IconLabel(Image[] frames, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		setSprites(frames);
		animationTime = 500;
		prevTime = System.currentTimeMillis();
		this.setBounds(x, y, width, height);
		this.setSize(width, height);
		this.setIcon(new ImageIcon(sprites[0]));
		frame = 0;
	}
	
	IconLabel(Image sprite, int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		setSprite(sprite);
		animationTime = 500;
		prevTime = System.currentTimeMillis();
		this.setBounds(x, y, width, height);
		this.setSize(width, height);
		
		this.setIcon(new ImageIcon(sprites[0]));
		frame = 0;
	}
	
	public void setSprites(Image[] sprites) {
		for (Image sprite : sprites) {
			sprite = sprite.getScaledInstance(width, height, 1);
		}
		this.sprites = sprites;
		frame = 0;
	}
	
	public void setSprite(Image sprite) {
		Image spritex = sprite.getScaledInstance(width, height, 1);
		this.sprites = new Image[] {spritex};
		frame = 0;
	}
	
	public boolean update() {
		if (System.currentTimeMillis() - prevTime >= animationTime) {
			incrementFrame();
			prevTime = System.currentTimeMillis();
		}
		return false;
	}
	
	public Image[] getSprite() {
		return sprites;
	}
	
	public void incrementFrame() {
		if (frame < sprites.length-1) {
			frame++;
		} else {
			frame= 0;
		}
		setIcon(new ImageIcon(sprites[frame]));
	}

	
	
}