package instantBattle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.AvailableSkill;
import entities.BattleMessage;
import entities.FlashMessage;
import entities.ProjectileMessage;
import entities.Selector;
import entities.ShipMessage;
import entities.Vector2D;
import entities.skills.Flash;
import handlers.InputHandler;
import handlers.ResourceHandler;
import main.Main;
import menu.InstantMenu;
import networking.KeyVal;

/***
 * Frame for the battle
 *
 */
public class Battle /*extends JFrame*/{
	private static final long serialVersionUID = 1L;
	private BufferedImage map;
	private int width, height, x, y;
	public static int myTeam;
	public Point myPosition =  new Point(0,0);
	public float scale;
	public static int winner;
	ArrayList<BattleMessage> currentState;
	
	private boolean keyLast = true;
	private ArrayList<KeyVal> keys;
	private Image bg;
	
	
	/**
	 * get the current state
	 * @param currentState
	 */
	public void setCurrentState(ArrayList<BattleMessage> currentState) {
		this.currentState = currentState;
	}
	
	/**
	 * create a new battle
	 * @param myShip the image of the ship
	 * @param enemyType the type of enemy
	 * @param enemyColour the colour of the enemy
	 */
	public Battle(Image[] myShip, int enemyType, Color enemyColour) {
		winner = 0;
		keys = new ArrayList<KeyVal>();
		bg = ResourceHandler.getImage("backgrounds/space");
		scale = (float) Math.min(InputHandler.screenSize.getWidth()/BattleController.width, InputHandler.screenSize.getHeight()/BattleController.height);
		if (scale == 0)
			scale = 1;
		Main.cache.put("myShip", myShip);
		Main.cache.put("enemyShip", makeSprites(enemyType, enemyColour));
		this.width = BattleController.width;
		this.height = BattleController.height;
		map = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Send the inputs of the player
	 */
	public void sendPlayerInputs() {
		
		
		if (Main.input.isKeyDown(KeyEvent.VK_W)) {
			keys.add(KeyVal.W);
		}
		if (Main.input.isKeyDown(KeyEvent.VK_A)) {
			keys.add(KeyVal.A);
		}
		if (Main.input.isKeyDown(KeyEvent.VK_S)) {
			keys.add(KeyVal.S);
		}
		if (Main.input.isKeyDown(KeyEvent.VK_D)) {
			keys.add(KeyVal.D);
		}
		
		if (Main.input.isKeyDown(KeyEvent.VK_Q)) {
			keys.add(KeyVal.Q);
		}
		if (Main.input.isKeyDown(KeyEvent.VK_E)) {
			keys.add(KeyVal.E);

		}
		if (Main.input.isKeyDown(KeyEvent.VK_SHIFT)) {
			keys.add(KeyVal.SHIFT);
			Main.input.artificialKeyReleased(KeyEvent.VK_SHIFT);
		}
		
		if (Main.input.isMouseDown(MouseEvent.BUTTON1)) {
			keys.add(KeyVal.BTN);
			Main.input.artificialMouseReleased(MouseEvent.BUTTON1);
		}
		
		if (keyLast) {
			keyLast = false;
			double direction = Vector2D.convertToDirection((int) InputHandler.getMousePositionOnScreen().getX(),(int) InputHandler.getMousePositionOnScreen().getY(), (int) myPosition.getX(), (int) myPosition.getY());
			InstantMenu.getClient().sendMouseDirection((float)-direction);
			
		} else {
			keyLast = true;
			if (!keys.isEmpty()) {	
				InstantMenu.getClient().sendKeys(keys.toArray());
				keys.clear();
			}
		}
		
		
	}


	/**
	 * draw a ship
	 * @param xg the graphics object
	 * @param ship the ship message to convert into a ship
	 */
	public void drawShip(Graphics g, ShipMessage ship) {
		int x = (int) (scale*ship.getX());
		int y = (int) (scale*ship.getY());
		Graphics xg = g.create();
		Graphics2D g2d = (Graphics2D) xg;
		g2d.rotate(ship.getDirection(), x, y);
		if (ship.getTeam() == myTeam) {
			if (ship.getType() < 0) {
				Image img = Main.cache.get("myShip")[ship.getFrame()].getScaledInstance((int) (Main.cache.get("myShip")[ship.getFrame()].getWidth(null)*scale),
						(int) (Main.cache.get("myShip")[ship.getFrame()].getHeight(null)*scale), 0);
				myPosition = new Point(x,y);
				xg.drawImage(img,  (int) (x-(scale*Selector.getShipSize(ship.getType()).width/2)), (int)(y -(scale*Selector.getShipSize(ship.getType()).height/2)), null);
			} else {
				Image img = Selector.getShipImage(ship.getType(),true)[ship.getFrame()];
				img = img.getScaledInstance((int) (img.getWidth(null)*scale), (int) (img.getHeight(null)*scale), 0);
				xg.drawImage(img, (int) (x-(scale*Selector.getShipSize(ship.getType()).width/2)), (int)(y -(scale*Selector.getShipSize(ship.getType()).height/2)), null);
			}
		} else {
			if (ship.getType() < 0) {		
				Image img = Main.cache.get("enemyShip")[ship.getFrame()].getScaledInstance((int) (Main.cache.get("enemyShip")[ship.getFrame()].getWidth(null)*scale),
						(int) (Main.cache.get("enemyShip")[ship.getFrame()].getHeight(null)*scale), 0);
				xg.drawImage(img,  (int) (x-(scale*Selector.getShipSize(ship.getType()).width/2)), (int)(y -(scale*Selector.getShipSize(ship.getType()).height/2)), null);
			} else {
				Image img = Selector.getShipImage(ship.getType(),false)[ship.getFrame()];
				img = img.getScaledInstance((int) (img.getWidth(null)*scale), (int) (img.getHeight(null)*scale), 0);
				xg.drawImage(img, (int) (x-(scale*Selector.getShipSize(ship.getType()).width/2)), (int)(y -(scale*Selector.getShipSize(ship.getType()).height/2)), null);
			}
		}
		drawHealthBar(g, ship,x,y);
		
		
	}
	

	/**
	 * draw the health bar of the ship
	 * @param g the graphics object
	 * @param ship the ship message to convert to a health bar
	 * @param x the x coord to draw at
	 * @param y the y coord to draw at
	 */
	private void drawHealthBar(Graphics g, ShipMessage ship, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		if (ship.getType() < 0) {
			if (ship.getTeam() == myTeam) {
				g2d.setColor(Color.GREEN);
			} else {
				g2d.setColor(Color.BLUE);
			}
		} else {
			g2d.setColor(Color.red);
		}
		//TODO Deleteg2d.draw(Selector.getHitbox(ship.getType(), x, y));
		Dimension size = Selector.getShipSize(ship.getType());
		size.setSize(size.getWidth()*scale, size.getHeight()*scale);
		int maxHealth = (int) (Selector.getShipStats(ship.getType())[0] + ship.getArmor());
		float currentHealth = ship.getHealth();
		int width =(int) (((currentHealth+ship.getArmor())/maxHealth)*50*scale);
		int height = (int) (5*scale);
		Point corner = new Point(x - width/2,(int) ( y-(Selector.getShipSize(ship.getType()).height/2)-height-(5*scale)));
		Rectangle healthbar = new Rectangle((int) corner.getX(),(int) corner.getY(), width, height);
		g2d.fill(healthbar);
		g2d.draw(healthbar);
		if (ship.getArmor() > 0) {
			g2d.setColor(Color.GRAY);
			int armorWidth = (int) ((ship.getArmor()/maxHealth)*50*scale);
			Rectangle armorBar = new Rectangle((int) corner.getX() + width,(int) corner.getY(), armorWidth, height);
			g2d.fill(armorBar);
			g2d.draw(armorBar);
		}
		
	}

	/**
	 * draw a projectile
	 * @param g the graphics object
	 * @param projectile the projectile message to convert to a projectile
	 */
	private void drawProjectile(Graphics g, ProjectileMessage projectile) {
		boolean targetingMe = projectile.getTargetTeam() == myTeam;
		if (targetingMe) {
			Image img = AvailableSkill.getSkillProjectile(projectile.getId(), true)[projectile.getFrame()][projectile.getAnimation()];
			img = img.getScaledInstance((int)(scale*img.getWidth(null)), (int)(scale*img.getHeight(null)), 0);
			g.drawImage(img,(int)(scale*projectile.getX() - img.getWidth(null)/2), (int)(scale*projectile.getY() - img.getHeight(null)/2), null);
		} else {
			Image img = AvailableSkill.getSkillProjectile(projectile.getId(), false)[projectile.getFrame()][projectile.getAnimation()];
			img = img.getScaledInstance((int)(scale*img.getWidth(null)), (int)(scale*img.getHeight(null)), 0);
			g.drawImage(img,(int)(scale*projectile.getX() - img.getWidth(null)/2 ), (int)(scale*projectile.getY() - img.getHeight(null)/2), null);
		}
	}



	
	/**
	 * draw the current state
	 * @param g the graphics object
	 */
	public void draw(Graphics g) {
		if (currentState != null) {
			ArrayList<BattleMessage> temp = (ArrayList<BattleMessage>) currentState.clone();
			Graphics g2 = (Graphics2D) map.getGraphics();
			Graphics2D g2d = (Graphics2D) g2;
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, (int) (width*scale), (int) (height*scale));
		
			g2.drawImage(bg, x,y, (int)InputHandler.screenSize.getWidth(), (int) InputHandler.screenSize.getHeight(), null);	
			for (BattleMessage msg : temp) {
				switch (msg.getMessageType()) {
				case 0 :
					ShipMessage ship = (ShipMessage) msg;
					drawShip(g2,ship);
					break;
				case 1: 
					ProjectileMessage projectile = (ProjectileMessage) msg;
					drawProjectile(g2,projectile);
					break;
				case 2:
					BattleMessage skill = (BattleMessage) msg;
					drawFlash(g2,(FlashMessage) skill);
					break;
				}
			}
			g.drawImage(map,  (int)(scale*x),  (int)(scale*y), null);	
		}
	}
	
	

	/**
	 * draw a flash message
	 * @param g the graphics object
	 * @param skill the message to draw
	 */
	private void drawFlash(Graphics g, FlashMessage skill) {
		Image startImg = Flash.startFrames[skill.getFrame()];
		Image endImg = Flash.endFrames[skill.getFrame()];
		g.drawImage(startImg,skill.getX1()-(startImg.getWidth(null)/2),skill.getY1()-(startImg.getHeight(null)/2),null);
		g.drawImage(startImg,skill.getX2()-(endImg.getWidth(null)/2),skill.getY2()-(endImg.getHeight(null)/2),null);
			
	}

	/**
	 * create a sprite for the ship
	 * @param type the type of ship
	 * @param colour the colour of the ship
	 * @return the image of the ship
	 */
	public static Image[] makeSprites(int type, Color colour) {
		BufferedImage base = Selector.getShipSpriteSheet(type);
		BufferedImage overlay = Selector.getShipOverlay(type);
		BufferedImage player = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Color playerColour = colour, prevColour = colour;
		Graphics g = player.getGraphics();
		for (int i = 0; i < overlay.getWidth(); i++) {
			for (int j = 0; j < overlay.getHeight(); j++) {
				if (overlay.getRGB(i, j) == prevColour.getRGB() || overlay.getRGB(i, j) == Color.WHITE.getRGB()) {
					overlay.setRGB(i, j, playerColour.getRGB());
				}
			}
		}
		prevColour = playerColour;
		g.drawImage(base, 0, 0, null);
		g.drawImage(overlay, 0, 0, null);
		Dimension size = Selector.getShipSize(type);
		return ResourceHandler.convertSpriteSheet(player, size.width, size.height);
	
    
	}
	
	
	
}