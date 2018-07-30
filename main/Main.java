package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import database.DBConnect;
import entities.AvailableSkill;
import entities.Selector;
import galaxy.Galaxy;
import gameLogic.PlayerStats;
import generators.Biome;
import handlers.InputHandler;
import handlers.MathHandler;
import handlers.ResourceHandler;
import handlers.SoundHandler;
import instantBattle.Battle;
import menu.BattleStart;
import menu.ConnectMenu;
import menu.HostMenu;
import menu.InstantMenu;
import menu.MainMenu;
import menu.Menu;
import menu.OptionsMenu;
import menu.PauseMenu;
import menu.PlayMenu;
import messaging.PopUp;
import networking.BattleModeClient;
import networking.BattleModeServer;

/**
 * the main class where the game starts
 * 
 * 
 *
 */
public class Main extends AbstractMain {

	private static final long serialVersionUID = 1L;
	private static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	public static float ratio;
	private Menu mainMenu, hostMenu, instantMenu;
	private OptionsMenu optionsMenu;
	private BattleStart battleMenu;
	private PlayMenu playMenu;
	private ConnectMenu connectMenu;
	private PauseMenu pauseMenu;
	private Galaxy galaxy;
	public static Battle battle;
	public static InputHandler input;
	private BattleModeClient client;
	private BattleModeServer server;
	public static PlayerStats pStats;
	private boolean battleBool = false, pause = false, options = false;
	private boolean clickedFirst = false, clickedWSAD = false;
	public static boolean offline = false;
	private int volume = 100;
	public static int numOfPlayers = 0;
	private BufferedImage bImage;
	private State.STATE lastState;
	
	//Values for DB
	public static boolean USEDB = false;
	public static DBConnect db;
	public static String PNAME;
	private boolean newPlayer = true;
	public static HashMap<String, BufferedImage[][]> projectileCache = null;
	public static HashMap<String, Image[]> cache = null;
	
	public void initialise() {	
		running = true;
		projectileCache = new HashMap<String,BufferedImage[][]>();
		cache = new HashMap<String,Image[]>();
		AvailableSkill.initialise();
		Selector.initialise();
		this.setTitle("Star Sailor");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setUndecorated(true);
		device.setFullScreenWindow(this);
		//this.setSize(InputHandler.screenSize);
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(ResourceHandler.getImage("decoration/cross_hair"), new Point(16, 16), ""));
		ratio = Math.min(InputHandler.screenSize.width, InputHandler.screenSize.height) / 14f / 16f;
		Biome.createDefaultBiomes();
		mainMenu = new MainMenu("backgrounds/space");
		this.add(mainMenu);
		this.setVisible(running);
		input = new InputHandler(this);
		pStats = new PlayerStats(PlayerStats.MONEY, PlayerStats.METAL, PlayerStats.URANIUM, PlayerStats.AETHER,
				PlayerStats.WATER, PlayerStats.STORAGE, PlayerStats.POPULATION, PlayerStats.SHIP1, PlayerStats.CRYSTAL,
				PlayerStats.POINTS);
		SoundHandler.loopSong(SoundHandler.menu_music);
	}

	@Override
	public void update(float time) {
		int switchState;
		switch (State.state) {
		case MENU_MAIN:
			switchState = mainMenu.update(time);
			if (switchState == State.PLAY) {
				playMenu = new PlayMenu("backgrounds/space");
				State.changeState(State.STATE.MENU_PLAY);
				this.remove(mainMenu);
				this.add(playMenu);
				this.setVisible(running);
				repaint();
			} else if (switchState == State.HOST) {
				hostMenu = new HostMenu("backgrounds/space");
				State.changeState(State.STATE.MENU_HOST);
				this.remove(mainMenu);
				this.add(hostMenu);
				this.setVisible(running);
				repaint();
			} else if (switchState == State.CONNECT) {
				connectMenu = new ConnectMenu("backgrounds/space");
				State.changeState(State.STATE.MENU_CONNECT);
				this.remove(mainMenu);
				this.add(connectMenu);
				this.setVisible(running);
				repaint();
			} else if (switchState == State.INSTANT) {
				instantMenu = new InstantMenu("backgrounds/space");
				State.changeState(State.STATE.MENU_INSTANT);
				this.remove(mainMenu);
				this.add(instantMenu);
				this.setVisible(running);
				repaint();
			} else if (switchState == State.OPTIONS) {
				optionsMenu = new OptionsMenu("backgrounds/space", volume);
				State.changeState(State.STATE.MENU_OPTIONS);
				this.remove(mainMenu);
				this.add(optionsMenu);
				this.setVisible(running);
				repaint();
			} else if (switchState == State.EXIT) {
				close();
			}
			break;
		case MENU_PLAY:
			switchState = playMenu.update(time);
			long actualSeed; // for DB
			if (switchState == State.PLAY) {
				
				//----------------DB CODE STARTED---------------------//
				if(USEDB){
					if(db.executeQueryS("SELECT Name FROM Accounts WHERE Name='" + playMenu.getName() + "';") == ""){
						db.dbActions("INSERT INTO Accounts Values ('"+ playMenu.getName() + "','password');");
						db.dbActions("INSERT INTO SAccounts Values (1," + playMenu.getSeed() + ",'" + playMenu.getName() + "'," + pStats.getPoints() + ",1,0," + pStats.getActivePopulation() + ",0);");
						PNAME = playMenu.getName();
						actualSeed = playMenu.getSeed();
						newPlayer = true;
					}else{
						PNAME = playMenu.getName();
						actualSeed = db.executeQueryInt("SELECT GSeed FROM SAccounts WHERE Name = '" + playMenu.getName() + "';", "GSeed");
						newPlayer = false;
					}
				}else{
					actualSeed = playMenu.getSeed();
				}
				galaxy = new Galaxy(actualSeed, newPlayer);
				if(USEDB){
					pStats.setPopulationUsed((int) (pStats.getPopulation() - Main.db.executeQueryInt("SELECT Population FROM SAccounts WHERE Name = '" + Main.PNAME + "';", "Population")));
				}
				//----------------DB CODE ENDED----------------------//
				
				State.changeState(State.STATE.GAME_PLANETARY);
				SoundHandler.music.stopClip();
				SoundHandler.loopSong(SoundHandler.campaign_music);
				this.remove(playMenu);
				if (!clickedFirst) {
					Galaxy.setPopUp(
							new PopUp("Welcome to Stars Sailor Campaign Mode \nYou can use W,S,A,D to move around "));
					clickedFirst = true;
				}
				this.add(galaxy);
				this.setVisible(running);
				repaint();
				offline = playMenu.getOffline();
			} else if (switchState == State.MAIN) {
				mainMenu = new MainMenu("backgrounds/space");
				State.changeState(State.STATE.MENU_MAIN);
				this.remove(playMenu);
				this.add(mainMenu);
				this.setVisible(true);
				repaint();
			} else if (switchState == State.CONNECT) {
				
				//----------------DB CODE STARTED---------------------//
				if(USEDB){
					if (db.executeQueryS("SELECT Name FROM Accounts WHERE Name='" + playMenu.getName() + "';") == ""){
						db.dbActions("INSERT INTO Accounts Values ('"+ playMenu.getName() + "','password');");
						db.dbActions("INSERT INTO SAccounts Values (1,1,'" + playMenu.getName() + "'," + pStats.getPoints() + ",1,0," + pStats.getActivePopulation() + ",0);");
						PNAME = playMenu.getName();
						actualSeed = playMenu.getSeed();
						newPlayer = true;
					}else{
						PNAME = playMenu.getName();
						newPlayer = false;
					}
				}
				galaxy = new Galaxy(playMenu.getIP(), playMenu.getPort(), newPlayer);
				//----------------DB CODE ENDED-----------------------//
				
				State.changeState(State.STATE.GAME_PLANETARY);
				SoundHandler.music.stopClip();
				SoundHandler.loopSong(SoundHandler.campaign_music);
				this.remove(playMenu);
				if (!clickedFirst) {
					Galaxy.setPopUp(
							new PopUp("Welcome to Stars Sailor Campaign Mode \nYou can use W,S,A,D to move around "));
					clickedFirst = true;
				}
				this.add(galaxy);
				this.setVisible(running);
			}
			break;
		case MENU_HOST:
			switchState = hostMenu.update(time);
			if (switchState == State.HOST) {
				this.remove(hostMenu);
				this.setVisible(running);
				repaint();
			} else if (switchState == State.MAIN) {
				mainMenu = new MainMenu("backgrounds/space");
				State.changeState(State.STATE.MENU_MAIN);
				this.remove(hostMenu);
				this.add(mainMenu);
				this.setVisible(true);
				repaint();
			}
			break;
		case MENU_CONNECT:
			switchState = connectMenu.update(time);
			if (switchState == State.CONNECT) {
				State.changeState(State.STATE.GAME_BATTLE);
				this.remove(connectMenu);
				this.setVisible(running);
				repaint();
			} else if (switchState == State.MAIN) {
				mainMenu = new MainMenu("backgrounds/space");
				State.changeState(State.STATE.MENU_MAIN);
				this.remove(connectMenu);
				this.add(mainMenu);
				this.setVisible(true);
				repaint();
			}
			break;
		case MENU_INSTANT:
			switchState = instantMenu.update(time);
			if (switchState == State.MAIN) {
				mainMenu = new MainMenu("backgrounds/space");
				State.changeState(State.STATE.MENU_MAIN);
				this.remove(instantMenu);
				this.add(mainMenu);
				this.setVisible(true);
				repaint();
			} else if (switchState == State.INSTANT) {
				State.changeState(State.STATE.INSTANT_BATTLE);
				this.remove(instantMenu);
				this.setVisible(running);
				repaint();
			}

			break;
		case MENU_OPTIONS:
			switchState = optionsMenu.update(time);
			if (switchState == State.MAIN) {
				mainMenu = new MainMenu("backgrounds/space");
				State.changeState(State.STATE.MENU_MAIN);
				volume = optionsMenu.getVolume();
				this.remove(optionsMenu);
				this.add(mainMenu);
				this.setVisible(true);
				repaint();
			}
			break;
		case MENU_BATTLE:
			if (!battleBool) {
				this.remove(galaxy);
				battleMenu = new BattleStart("backgrounds/space", galaxy.getCurrentPlanetName(),
						galaxy.finishedTrading());
				this.add(battleMenu);
				this.setVisible(true);
				repaint();
				battleBool = true;
			}
			switchState = battleMenu.update(time);
			if (switchState == State.PLAY) {
				this.remove(battleMenu);
				State.changeState(State.STATE.GAME_BATTLE);
				SoundHandler.music.stopClip();
				SoundHandler.loopSong(SoundHandler.battle_music);
				galaxy.setBattleStarted(true);
				this.add(galaxy);
				this.setVisible(true);
				repaint();
				battleBool = false;
			} else if (switchState == State.EXIT) {
				this.remove(battleMenu);
				State.changeState(State.STATE.GAME_SOLAR);
				this.add(galaxy);
				this.setVisible(true);
				galaxy.zoomOut(0.2);
				battleBool = false;
			} else if (switchState == State.SPEND_CRYSTAL) {
				if (!battleMenu.getShowing()) {
					if (Main.pStats.getCrystal() > 0) {
						Main.pStats.setCrystal(Main.pStats.getCrystal() - 1);
						battleMenu.showUnitCount(galaxy.getNumOfFightersOnPlanet() + galaxy.getNumOfCarriersOnPlanet()
								+ galaxy.getNumOfCommandOnPlanet());
						repaint();
					} else {
						battleMenu.noCrystal();
						repaint();
					}
				}
			} else if (switchState == State.BRIBE) {
				if (Main.pStats.getMoney() > 500 && !battleMenu.getBribed()) {
					int bribed = MathHandler.random.nextInt(Math.min(20, galaxy.getNumOfFightersOnPlanet()));
					System.out.println("bribed " + bribed + " ships");
					galaxy.getPlanet(galaxy.getCurrentStar(), galaxy.getCurrentPlanet())
							.setNumOfFighters(galaxy.getNumOfFightersOnPlanet() - bribed);
					Main.pStats.setNumberOfShipsType1(Main.pStats.getNumberOfShipsType1() + bribed);
					Main.pStats.setMoney(Main.pStats.getMoney() - 500);
					battleMenu.showBribe(true, bribed);
				} else {
					battleMenu.showBribe(false, 0);
				}
			} else if (switchState == State.TRADE) {
				if (galaxy.isOwned(galaxy.getCurrentStar(), galaxy.getPrevPlanet())
						&& !galaxy.hasTrade(galaxy.getCurrentStar(), galaxy.getPrevPlanet())
						&& !galaxy.hasTrade(galaxy.getCurrentStar(), galaxy.getCurrentPlanet())
						&& Main.pStats.getMoney() >= 1000) {
					Main.pStats.setMoney(Main.pStats.getMoney() - 1000);
					galaxy.addTradeRoute(galaxy.getCurrentStar(), galaxy.getPrevPlanet(), galaxy.getCurrentPlanet());
					galaxy.setTrading(galaxy.getCurrentStar(), galaxy.getPrevPlanet(), galaxy.getCurrentPlanet());
					battleMenu.showTrading(0);
				} else if (galaxy.hasTrade(galaxy.getCurrentStar(), galaxy.getPrevPlanet())
						|| galaxy.hasTrade(galaxy.getCurrentStar(), galaxy.getCurrentPlanet())) {
					battleMenu.showTrading(2);
				} else if (Main.pStats.getMoney() < 1000) {
					battleMenu.showTrading(1);
				}
			} else if (switchState == State.PURCHASE) {
				if (Main.pStats.getMoney() > 50000 && galaxy.finishedTrading()) {
					this.remove(battleMenu);
					State.changeState(State.STATE.GAME_PLANETARY);
					Main.pStats.setMoney(Main.pStats.getMoney() - 50000);
					galaxy.removeTradeRoute();
					this.add(galaxy);
					this.setVisible(true);
					repaint();
				}
			}
			galaxy.update(time);
			break;
		case MENU_PAUSE:
			galaxy.update(time);
			if (!pause) {
				this.remove(galaxy);
				pauseMenu = new PauseMenu("backgrounds/space");
				this.add(pauseMenu);
				this.setVisible(true);
				this.repaint();
				pause = true;
			}
			if (!options) {
				switchState = pauseMenu.update(time);
				if (switchState == State.PLAY) {
					State.changeState(lastState);
					this.remove(pauseMenu);
					this.add(galaxy);
					setVisible(true);
					repaint();
					pause = false;
				} else if (switchState == State.OPTIONS) {
					options = true;
					this.remove(pauseMenu);
					optionsMenu = new OptionsMenu("backgrounds/space", volume);
					this.add(optionsMenu);
					setVisible(true);
					repaint();
				} else if (switchState == State.MAIN) {
					mainMenu = new MainMenu("backgrounds/space");
					State.changeState(State.STATE.MENU_MAIN);
					SoundHandler.music.stopClip();
					SoundHandler.loopSong(SoundHandler.menu_music);
					this.remove(pauseMenu);
					this.add(mainMenu);
					//----------------DB CODE STARTED---------------------//
					if(USEDB){
						pStats.updateDbOnClose();
					}
					//----------------DB CODE ENDED-----------------------//
					pStats = new PlayerStats(PlayerStats.MONEY, PlayerStats.METAL, PlayerStats.URANIUM,
							PlayerStats.AETHER, PlayerStats.WATER, PlayerStats.STORAGE, PlayerStats.POPULATION,
							PlayerStats.SHIP1, PlayerStats.CRYSTAL, PlayerStats.POINTS);
					this.setVisible(true);
					repaint();
					pause = false;
				}
			} else if (options) {
				if (optionsMenu.update(time) == State.MAIN) {
					this.volume = optionsMenu.getVolume();
					options = false;
					this.remove(optionsMenu);
					this.add(pauseMenu);
					setVisible(true);
					repaint();
				}
			}
			break;
		case GAME_GALACTIC:
			if (!this.hasFocus()) {
				this.requestFocus();
			}
			if (input.isMouseDown(MouseEvent.BUTTON1)) {
				galaxy.clickedGalactic(InputHandler.getMousePositionOnScreen());
				Galaxy.getPopUp().clicked(InputHandler.getMousePositionOnScreen());
				input.artificialMouseReleased(MouseEvent.BUTTON1);
			}
			if (input.getMouseWheelUp()) {
				galaxy.zoomIn(0.2);
				input.stopMouseWheel();
			} else if (input.getMouseWheelDown()) {
				galaxy.zoomOut(0.2);
				input.stopMouseWheel();
			}
			if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
				lastState = State.state;
				State.changeState(State.STATE.MENU_PAUSE);
				pause = false;
				input.artificialKeyReleased(KeyEvent.VK_ESCAPE);
			}
			galaxy.update(time);
			break;
		case GAME_SOLAR:
			if (!this.hasFocus()) {
				this.requestFocus();
			}
			if (input.isMouseDown(MouseEvent.BUTTON1)) {
				galaxy.clickedSolar(InputHandler.getMousePositionOnScreen());
				Galaxy.getPopUp().clicked(InputHandler.getMousePositionOnScreen());
				input.artificialMouseReleased(MouseEvent.BUTTON1);
			}
			if (input.getMouseWheelUp()) {
				galaxy.zoomIn(0.2);
				input.stopMouseWheel();
			} else if (input.getMouseWheelDown()) {
				galaxy.zoomOut(0.2);
				input.stopMouseWheel();
			}

			if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
				lastState = State.state;
				State.changeState(State.STATE.MENU_PAUSE);
				pause = false;
				input.artificialKeyReleased(KeyEvent.VK_ESCAPE);
			}
			galaxy.update(time);
			break;
		case GAME_PLANETARY:
			if (!this.hasFocus()) {
				this.requestFocus();
			}
			if (input.isKeyDown(KeyEvent.VK_W)) {
				if (!clickedWSAD) {
					Galaxy.setPopUp(new PopUp(
							"Congratulations!\nYou can now explore the planet \nGo and find the following resources:\n -Metal, Uranium, Aether \nClick on the buildings to display its informations "));
					clickedWSAD = true;
				}
				galaxy.moveUD(-1);
				galaxy.setPlayerMoving(true);
			}
			if (input.isKeyDown(KeyEvent.VK_A)) {
				if (!clickedWSAD) {
					Galaxy.setPopUp(new PopUp(
							"Congratulations!\nYou can now explore the planet \nGo and find the following resources:\n -Metal, Uranium, Aether \nClick on the buildings to display its informations "));
					clickedWSAD = true;
				}
				galaxy.moveLR(-1);
				galaxy.setPlayerMoving(true);
			}
			if (input.isKeyDown(KeyEvent.VK_S)) {
				if (!clickedWSAD) {
					Galaxy.setPopUp(new PopUp(
							"Congratulations!\nYou can now explore the planet \nGo and find the following resources:\n -Metal, Uranium, Aether \nClick on the buildings to display its informations "));
					clickedWSAD = true;
				}
				galaxy.moveUD(1);
				galaxy.setPlayerMoving(true);
			}
			if (input.isKeyDown(KeyEvent.VK_D)) {
				if (!clickedWSAD) {
					Galaxy.setPopUp(new PopUp(
							"Congratulations!\nYou can now explore the planet \nGo and find the following resources:\n -Metal, Uranium, Aether \nClick on the buildings to display its informations "));
					clickedWSAD = true;
				}
				galaxy.moveLR(1);
				galaxy.setPlayerMoving(true);
			}
			if (!input.isKeyDown(KeyEvent.VK_W) && !input.isKeyDown(KeyEvent.VK_A) && !input.isKeyDown(KeyEvent.VK_S)
					&& !input.isKeyDown(KeyEvent.VK_D)) {
				galaxy.setPlayerMoving(false);
			}
			if (input.getMouseWheelDown()) {
				galaxy.zoomOut(0);
				input.stopMouseWheel();
			}
			if (input.isMouseDown(MouseEvent.BUTTON1)) {
				galaxy.clickedPlanetary(InputHandler.getMousePositionOnScreen());
				Galaxy.getPopUp().clicked(InputHandler.getMousePositionOnScreen());
				input.artificialMouseReleased(MouseEvent.BUTTON1);
			}
			if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
				lastState = State.state;
				State.changeState(State.STATE.MENU_PAUSE);
				input.artificialKeyReleased(KeyEvent.VK_ESCAPE);
			}
			galaxy.update(time);
			break;
		case GAME_BATTLE:
			if (!this.hasFocus()) {
				this.requestFocus();
			}
			if (galaxy != null) {
			if (input.isKeyDown(KeyEvent.VK_W)) {
				galaxy.moveUD(-galaxy.getPlayerSpeed());
				galaxy.setPlayerMoving(true);
			}
			if (input.isKeyDown(KeyEvent.VK_A)) {
				galaxy.moveLR(-galaxy.getPlayerSpeed());
				galaxy.setPlayerMoving(true);
			}
			if (input.isKeyDown(KeyEvent.VK_S)) {
				galaxy.moveUD(galaxy.getPlayerSpeed());
				galaxy.setPlayerMoving(true);
			}
			if (input.isKeyDown(KeyEvent.VK_D)) {
				galaxy.moveLR(galaxy.getPlayerSpeed());
				galaxy.setPlayerMoving(true);
			}
			if (!input.isKeyDown(KeyEvent.VK_W) && !input.isKeyDown(KeyEvent.VK_A) && !input.isKeyDown(KeyEvent.VK_S)
					&& !input.isKeyDown(KeyEvent.VK_D)) {
				galaxy.setPlayerMoving(false);
			}
			galaxy.update(time);
			}
			break;
		case INSTANT_BATTLE:
			if (!this.hasFocus()) {
				this.requestFocus();
			}
			Main.battle.sendPlayerInputs();
			if (Main.battle.winner != 0) {
				if (Main.battle.winner == Main.battle.myTeam) {
					JOptionPane.showMessageDialog(null,
						    "Thats how it's done! You win!",
						    "Winner",
						    JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null,
						    "Try harder next time. You Lose!",
						    "Loser",
						    JOptionPane.PLAIN_MESSAGE);
				}
				mainMenu = new MainMenu("backgrounds/space");
				State.changeState(State.STATE.MENU_MAIN);
				BattleModeServer.kill();
				
				this.remove(instantMenu);
				this.add(mainMenu);
				this.setVisible(true);
				battle.winner = 0;
				repaint();
			}
			
			break;
		default:
			break;
		}
		if(input.isKeyDown(KeyEvent.VK_P)){
			BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D printout = image.createGraphics();
			printAll(printout);
			System.out.println("screenshot made!");
			printout.dispose();
			try { 
			    ImageIO.write(image, "png", new File("/resources/test.png")); 
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
	}

	@Override
	public void draw() {
		Graphics g = this.getGraphics();
		bImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) bImage.getGraphics();
		g2d.setColor(Color.black);
		switch (State.state) {
		case GAME_GALACTIC:
			g2d.fillRect(0, 0, bImage.getWidth(), bImage.getHeight());
			galaxy.draw(g2d);
			break;
		case GAME_SOLAR:
			g2d.fillRect(0, 0, bImage.getWidth(), bImage.getHeight());
			galaxy.draw(g2d);
			break;
		case GAME_PLANETARY:
			g2d.fillRect(0, 0, bImage.getWidth(), bImage.getHeight());
			galaxy.draw(g2d);
			break;
		case GAME_BATTLE:
			g2d.fillRect(0, 0, bImage.getWidth(), bImage.getHeight());
			galaxy.draw(g2d);
			break;
		case INSTANT_BATTLE:
			if (battle != null) {
				battle.draw(g);
			}
		default:
			break;
		}
		g.drawImage(bImage, 0, 0, null);
	}

	
	
	public static void main(String[] args) {

		
		Main main = new Main();
		//----------------DB CODE STARTED---------------------//
		if(USEDB){
			db = new DBConnect();
		}
		//----------------DB CODE ENDED-----------------------//
		main.run();
		
		
	}

	@Override
	public void close() {
		this.dispose();
		System.exit(0);
	}

}
