package galaxy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import galaxy.battle.Battle;
import galaxy.battle.Player;
import galaxy.network.Client;
import galaxy.network.Message;
import galaxy.network.Server;
import gameLogic.PlayerStats;
import generators.Block;
import handlers.InputHandler;
import handlers.MathHandler;
import handlers.SoundHandler;
import main.Main;
import main.State;
import messaging.PopUp;

/**
 * contains all the code to update and draw the entire galaxy
 * 
 * 
 */
public class Galaxy extends JPanel {

	private static final long serialVersionUID = 1L;
	private long seed, previousTime, currentTime, timeInterval = 1500;
	public static int numOfStars = 255, maxPlanets = 12;
	private static int currentStar = 0, currentPlanet = 1, selectedStar = 1, selectedPlanet = 1, playerNumber, prevStar,
			prevPlanet = 1, prevOwnedPlanet = 1;
	private float zoom = 3.0f, zoomSolar = 3.0f;
	private Star[] galaxy = new Star[numOfStars];
	private static Planet[][] solarSystems = new Planet[numOfStars][maxPlanets];
	private boolean created = false, battleStarted = false, firstEnteredGalaxy = true, firstEnteredSolar = true,
			firstEnteredBattle = true, zoomInGalaxy = true, zoomInSolar = true;
	private BufferedImage galaxyImage;
	private AffineTransform galaxyTransform, solarTransform;
	private Player player;
	private static Client client;
	private Server server;
	private Battle battle;
	private static PopUp popUp;

	// DB Variable
	private boolean firstEntered = true;

	/**
	 * creates a new galaxy for a host computer
	 * 
	 * @param seed
	 *            the galaxy's seed
	 */
	public Galaxy(long seed, boolean firstEntered) {
		server = new Server(seed, numOfStars);
		server.start();
		client = new Client(server.getIP(), server.getPort());
		Message m = client.receiveMessage();
		while (m == null) {
			m = client.receiveMessage();
		}
		this.seed = m.getSeed();
		System.out.println("seed: " + this.seed);
		m = client.receiveMessage();
		while (m == null) {
			m = client.receiveMessage();
		}
		currentStar = m.getPlayerNumber();
		System.out.println("player number: " + currentStar);
		playerNumber = currentStar;
		prevStar = currentStar;

		// ----------------DB CODE STARTED---------------------//
		if (Main.USEDB) {
			if (firstEntered)
				Main.db.dbActions(
						"UPDATE SAccounts SET cStar = " + currentStar + " WHERE pNumber = " + currentStar + (";"));
			else {
				Main.pStats.setMetal(0);
				Main.pStats.setUranium(0);
				Main.pStats.setAether(0);
				Main.pStats.setPopulation(0);
				Main.pStats.setWater(0);
				Main.pStats.setMoney(0);
				playerNumber = (int) Main.db
						.executeQueryInt("SELECT SID FROM Planets WHERE Name = '" + Main.PNAME + "';", "SID");
				currentStar = playerNumber;
				prevStar = currentStar;
			}
		}
		// ----------------DB CODE ENDED-----------------------//

		galaxyImage = new BufferedImage(InputHandler.screenSize.width * 4, InputHandler.screenSize.height * 4,
				BufferedImage.TYPE_INT_ARGB);
		player = new Player("spaceship/ship_sprites", (int) InputHandler.midPoint.x, (int) InputHandler.midPoint.y, 32,
				32, 500);
		initialiseGalaxy();
		double x = -galaxy[currentStar].getBounds().x - galaxy[currentStar].getBounds().width / 2
				+ InputHandler.midPoint.x;
		double y = -galaxy[currentStar].getBounds().y - galaxy[currentStar].getBounds().height / 2
				+ InputHandler.midPoint.y;
		for (Star s : galaxy) {
			if (s != null) {
				s.moveLR(x);
				s.moveUD(y);
			}
		}

		// ----------------DB CODE STARTED---------------------//
		if (Main.USEDB) {
			if (firstEntered) {
				initialiseSolarSystem();
			} else {
				reinitialiseSolarSystem();
			}
		}
		// ----------------DB CODE ENDED-----------------------//

		x = -solarSystems[currentStar][currentPlanet].getBounds().x
				- solarSystems[currentStar][currentPlanet].getBounds().width / 2 + InputHandler.midPoint.x;
		y = -solarSystems[currentStar][currentPlanet].getBounds().y
				- solarSystems[currentStar][currentPlanet].getBounds().height / 2 + InputHandler.midPoint.y;
		for (Planet p : solarSystems[currentStar]) {
			if (p != null) {
				p.moveLR(x);
				p.moveUD(y);
			}
		}
		galaxy[currentStar].incrementSolarPos(x, y);
		this.previousTime = System.currentTimeMillis();
		client.start();
	}

	/**
	 * creates a new galaxy for a connecting player
	 * 
	 * @param seed
	 *            the galaxy's seed
	 */
	public Galaxy(String ip, int port, boolean firstEntered) {
		client = new Client(ip, port);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Message m = client.receiveMessage();
		while (m == null) {
			m = client.receiveMessage();
		}
		this.seed = m.getSeed();
		System.out.println("seed: " + seed);
		m = client.receiveMessage();
		while (m == null) {
			m = client.receiveMessage();
		}
		currentStar = m.getPlayerNumber();
		System.out.println("player number: " + currentStar);
		playerNumber = currentStar;
		prevStar = currentStar;

		// ----------------DB CODE STARTED---------------------//
		if (Main.USEDB) {
			if (firstEntered) {
				Main.db.dbActions(
						"UPDATE SAccounts SET cStar = " + currentStar + " WHERE Name = '" + Main.PNAME + "'" + (";"));
				Main.db.dbActions(
						"UPDATE SAccounts SET pNumber = " + playerNumber + " WHERE Name ='" + Main.PNAME + "';");
				Main.db.dbActions("UPDATE SAccounts SET GSeed = " + seed + " WHERE Name = '" + Main.PNAME + "';");
			} else {
				Main.pStats.setMetal(0);
				Main.pStats.setUranium(0);
				Main.pStats.setAether(0);
				Main.pStats.setPopulation(0);
				Main.pStats.setWater(0);
				Main.pStats.setMoney(0);
				Main.db.dbActions(
						"UPDATE SAccounts SET pNumber = " + playerNumber + " WHERE Name ='" + Main.PNAME + "';");
				currentStar = playerNumber;
				prevStar = currentStar;
			}
		}
		// ----------------DB CODE ENDED-----------------------//

		galaxyImage = new BufferedImage(InputHandler.screenSize.width * 4, InputHandler.screenSize.height * 4,
				BufferedImage.TYPE_INT_ARGB);
		player = new Player("spaceship/ship_sprites", (int) InputHandler.midPoint.x, (int) InputHandler.midPoint.y, 32,
				32, 500);
		initialiseGalaxy();
		double x = -galaxy[currentStar].getBounds().x - galaxy[currentStar].getBounds().width / 2
				+ InputHandler.midPoint.x;
		double y = -galaxy[currentStar].getBounds().y - galaxy[currentStar].getBounds().height / 2
				+ InputHandler.midPoint.y;
		for (Star s : galaxy) {
			if (s != null) {
				s.moveLR(x);
				s.moveUD(y);
			}
		}

		// ----------------DB CODE STARTED---------------------//
		if (Main.USEDB) {
			if (firstEntered) {
				initialiseSolarSystem();
			} else {
				reinitialiseSolarSystem();
			}
		}
		// ----------------DB CODE ENDED-----------------------//

		x = -solarSystems[currentStar][currentPlanet].getBounds().x
				- solarSystems[currentStar][currentPlanet].getBounds().width / 2 + InputHandler.midPoint.x;
		y = -solarSystems[currentStar][currentPlanet].getBounds().y
				- solarSystems[currentStar][currentPlanet].getBounds().height / 2 + InputHandler.midPoint.y;
		for (Planet p : solarSystems[currentStar]) {
			if (p != null) {
				p.moveLR(x);
				p.moveUD(y);
			}
		}
		galaxy[currentStar].incrementSolarPos(x, y);
		this.previousTime = System.currentTimeMillis();
		client.start();
	}

	/**
	 * initialises the galaxy
	 */
	public void initialiseGalaxy() {
		for (int i = 1; i < numOfStars; i++) {
			galaxy[i] = new Star(seed + (i * 15485863), maxPlanets);
			for (int j = 1; j < galaxy[i].getNumOfPlanets(); j++) {
				solarSystems[i][j] = new Planet(seed + galaxy[i].getSeed() + (j * 243011), galaxy[i].getSize(),
						this.firstEntered);
			}
			galaxy[i].setGenerated(true);
		}
		setCreated(true);
	}

	/**
	 * initialises the current solar system
	 */
	public void initialiseSolarSystem() {

	}

	// ----------------DB CODE STARTED---------------------//
	public void reinitialiseSolarSystem() {
		currentStar = (int) Main.db.executeQueryInt("SELECT SID FROM Planets WHERE Name = '" + Main.PNAME + "';",
				"SID");
		ArrayList<Long> playerPlanets = new ArrayList<>();
		ArrayList<Integer> playerStars = new ArrayList<>();
		playerPlanets = Main.db.executeQueryIntA("Select PSeed FROM Planets WHERE Name = '" + Main.PNAME + "';",
				"PSeed");
		playerStars = Main.db.executeQueryIntAI("Select SID FROM Planets WHERE Name = '" + Main.PNAME + "';", "SID");
		playerStars = removeDuplicates(playerStars);
		int counter = 0;
		System.out.println("SIZE IS:" + playerStars.size());
		while (counter < playerStars.size()) {
			currentStar = playerStars.get(counter);
			for (int i = 1; i < galaxy[currentStar].getNumOfPlanets(); i++) {
				if (checkPlanetSeed(playerPlanets, seed + galaxy[currentStar].getSeed() + (i * 243011))) {
					System.out.println("ENTERED HERE ONE TIME!!!!");
					solarSystems[currentStar][i] = new Planet(seed + galaxy[currentStar].getSeed() + (i * 243011),
							galaxy[currentStar].getSize(), false);
					client.sendMessage(Message.createSetOwnership(currentStar, i, playerNumber, playerNumber));
				} else
					solarSystems[currentStar][i] = new Planet(seed + galaxy[currentStar].getSeed() + (i * 243011),
							galaxy[currentStar].getSize(), true);
			}
			galaxy[currentStar].setGenerated(true);
			counter++;
		}
	}

	private boolean checkPlanetSeed(ArrayList<Long> planets, long seed) {
		for (int i = 0; i < planets.size(); i++) {
			if (planets.get(i) == seed)
				return true;
		}
		return false;
	}

	private ArrayList<Integer> removeDuplicates(ArrayList<Integer> pStars) {
		for (int i = 1; i < pStars.size(); i++) {
			if (pStars.subList(0, i).contains(pStars.get(i))) {
				pStars.remove(i);
				i--;
			}
		}
		return pStars;
	}
	// ----------------DB CODE ENDED-----------------------//

	/**
	 * gets the ownership of the planets in the current solar system
	 */
	public void getOwnership() {
		client.sendMessage(Message.createRequestOwnership(currentStar, playerNumber));
	}

	/**
	 * sets the ownership of planets
	 * 
	 * @param m
	 *            the message that contains the new ownership of the planets
	 */
	public static void setOwnership(Message m) {
		for (int i = 0; i < m.getPlanetOwnership().length; i++) {
			if (solarSystems[m.getStarID()][i] != null) {
				if (solarSystems[m.getStarID()][i].getOwned() == playerNumber
						&& m.getPlanetOwnership()[i] != playerNumber) {
					Main.pStats.setNumberOfPlanets(Main.pStats.getNumberOfPlanets() - 1);
					Main.pStats
							.removeResource(Main.pStats.getResource("metal", solarSystems[m.getStarID()][i].getSeed()));
					Main.pStats.removeResource(
							Main.pStats.getResource("uranium", solarSystems[m.getStarID()][i].getSeed()));
					Main.pStats.removeResource(
							Main.pStats.getResource("aether", solarSystems[m.getStarID()][i].getSeed()));
					Main.pStats
							.removeResource(Main.pStats.getResource("water", solarSystems[m.getStarID()][i].getSeed()));
					Main.pStats.removeResource(
							Main.pStats.getResource("population", solarSystems[m.getStarID()][i].getSeed()));
					Main.pStats.removeResource(
							Main.pStats.getResource("storage", solarSystems[m.getStarID()][i].getSeed()));
					Main.pStats
							.removeResource(Main.pStats.getResource("money", solarSystems[m.getStarID()][i].getSeed()));
					if (Main.pStats.getResourceOnPlanet(PlayerStats.METALID,
							solarSystems[m.getStarID()][i].getSeed()) > 0)
						Main.pStats.setMetal(Main.pStats.getMetal() - Main.pStats
								.getResourceOnPlanet(PlayerStats.METALID, solarSystems[m.getStarID()][i].getSeed()));

					if (Main.pStats.getResourceOnPlanet(PlayerStats.URANIUMID,
							solarSystems[m.getStarID()][i].getSeed()) > 0)
						Main.pStats.setUranium(Main.pStats.getUranium() - Main.pStats
								.getResourceOnPlanet(PlayerStats.URANIUMID, solarSystems[m.getStarID()][i].getSeed()));

					if (Main.pStats.getResourceOnPlanet(PlayerStats.AETHERID,
							solarSystems[m.getStarID()][i].getSeed()) > 0)
						Main.pStats.setAether(Main.pStats.getAether() - Main.pStats
								.getResourceOnPlanet(PlayerStats.AETHERID, solarSystems[m.getStarID()][i].getSeed()));

					if (Main.pStats.getResourceOnPlanet(PlayerStats.WATERID,
							solarSystems[m.getStarID()][i].getSeed()) > 0)
						Main.pStats.setWater(Main.pStats.getWater() - Main.pStats
								.getResourceOnPlanet(PlayerStats.WATERID, solarSystems[m.getStarID()][i].getSeed()));

					if (Main.pStats.getResourceOnPlanet(PlayerStats.POPULATIONID,
							solarSystems[m.getStarID()][i].getSeed()) > Main.pStats.getActivePopulation()) {
						Main.pStats.setPopulation(Main.pStats.getPopulation() - Main.pStats.getResourceOnPlanet(
								PlayerStats.POPULATIONID, solarSystems[m.getStarID()][i].getSeed()));
						Main.pStats.setPopulationUsed((int) Main.pStats.getPopulation());
					} else {
						Main.pStats.setPopulation(Main.pStats.getPopulation() - Main.pStats.getResourceOnPlanet(
								PlayerStats.POPULATIONID, solarSystems[m.getStarID()][i].getSeed()));
						/*
						 * Main.pStats.setPopulationUsed( (int)
						 * (Main.pStats.getPopulationUsed() +
						 * Main.pStats.getResourceOnPlanet(
						 * PlayerStats.POPULATIONID,
						 * solarSystems[currentStar][i].getSeed())));
						 */
					}

					if (Main.pStats.getResourceOnPlanet(PlayerStats.STORAGEID,
							solarSystems[m.getStarID()][i].getSeed()) > 0)
						Main.pStats.setStorage(Main.pStats.getStorage() - Main.pStats
								.getResourceOnPlanet(PlayerStats.STORAGEID, solarSystems[m.getStarID()][i].getSeed()));
				}
				System.out.println("recieved ownership message, setting ownership");
				solarSystems[m.getStarID()][i].setOwned(m.getPlanetOwnership()[i]);
			}
		}
	}

	/**
	 * sends the number of ships a planet has to the server
	 */
	public static void sendShips() {
		client.sendMessage(Message.createSetShips(currentStar, currentPlanet,
				solarSystems[currentStar][currentPlanet].getNumOfFighters(),
				solarSystems[currentStar][currentPlanet].getNumOfCarriers(),
				solarSystems[currentStar][currentPlanet].getNumOfCommand()));
	}

	/**
	 * method that is called when the client recieves a message about the ships
	 * on a planet
	 * 
	 * @param m
	 *            the message
	 */
	public static void getShips(Message m) {
		if (m.getPlanetID() < 15 && solarSystems[m.getStarID()][m.getPlanetID()] != null) {
			solarSystems[m.getStarID()][m.getPlanetID()].setNumOfFighters(m.getNumOfFighters());
			solarSystems[m.getStarID()][m.getPlanetID()].setNumOfCarriers(m.getNumOfCarriers());
			solarSystems[m.getStarID()][m.getPlanetID()].setNumOfCommand(m.getNumOfCommand());
		}
	}

	/**
	 * initialises the current planet
	 */
	public void initialisePlanet() {
		solarSystems[currentStar][currentPlanet].generatePlanet(playerNumber, currentStar);
		client.sendMessage(Message.createSetOwnership(currentStar, currentPlanet, playerNumber, playerNumber));
		galaxy[currentStar].setOwned(true);
		Main.pStats.setNumberOfPlanets(Main.pStats.getNumberOfPlanets() + 1);
	}

	/**
	 * updates the game based on which state its currently in
	 * 
	 * @param time
	 */
	public void update(float time) {
		switch (State.state) {
		case GAME_GALACTIC:
			if (selectedStar != 0) {
				moveToSelected();
			}
			for (Star s : galaxy) {
				if (s != null) {
					s.update(time);
				}
			}
			// player.update(time);
			break;
		case GAME_SOLAR:
			if (!galaxy[currentStar].isGenerated()) {
				initialiseSolarSystem();
			}
			galaxy[currentStar].update(time);
			for (Planet p : solarSystems[currentStar]) {
				if (p != null) {
					p.update(time);
				}
			}
			if (selectedPlanet != 0) {
				moveToSelected();
			}
			break;
		case GAME_PLANETARY:
			if (!galaxy[currentStar].isGenerated()) {
				initialiseSolarSystem();
			}
			if (!solarSystems[currentStar][currentPlanet].isGenerated()) {
				initialisePlanet();
			}
			Block.updateAll();
			solarSystems[currentStar][currentPlanet].update(time);
			break;
		case GAME_BATTLE:
			if (firstEnteredBattle) {
				setPopUp(new PopUp(
						"Destroy the enemy team!\nRed ships are the enemy, blue are allies.\nRed lasers will harm you\ngreen ones will paralyse."));
				firstEnteredBattle = false;
			}
			if (battleStarted) {
				battle = new Battle(
						(int) Main.pStats.getResourceOnPlanet(PlayerStats.SHIPTYPE1ID,
								solarSystems[prevStar][prevOwnedPlanet].getSeed()),
						(int) Main.pStats.getResourceOnPlanet(PlayerStats.SHIPTYPE2ID,
								solarSystems[prevStar][prevOwnedPlanet].getSeed()),
						(int) Main.pStats.getResourceOnPlanet(PlayerStats.SHIPTYPE3ID,
								solarSystems[prevStar][prevOwnedPlanet].getSeed()),
						solarSystems[currentStar][currentPlanet].getNumOfFighters(),
						solarSystems[currentStar][currentPlanet].getNumOfCarriers(),
						solarSystems[currentStar][currentPlanet].getNumOfCommand(), InputHandler.screenSize.width * 2,
						InputHandler.screenSize.height * 2, solarSystems[prevStar][prevOwnedPlanet].getSeed(), player);
				battle.moveBattle(-InputHandler.screenSize.width / 2, -InputHandler.screenSize.height / 2);
				battleStarted = false;
			}
			if (battle.getVictory()) {
				State.changeState(State.STATE.GAME_PLANETARY);
				SoundHandler.music.stopClip();
				SoundHandler.loopSong(SoundHandler.campaign_music);
				player.setCurrentHealth(player.getTotalHealth());
				battleStarted = true;
			}
			if (battle.getDefeat()) {
				State.changeState(State.STATE.GAME_SOLAR);
				SoundHandler.music.stopClip();
				SoundHandler.loopSong(SoundHandler.campaign_music);
				player.setCurrentHealth(player.getTotalHealth());
				zoomOut(0.2);
				battleStarted = true;
			}
			battle.update(time);
			break;
		default:
			break;
		}
		for (Star s : galaxy) {
			if (s != null) {
				s.updateTradeRoutes();
			}
		}
		Main.pStats.updateStatics();
		/*
		 * Main.pStats.updatePoints(); Main.pStats.checkCrystalAvailability();
		 * Main.pStats.updateRecruitmentAgency(); Main.pStats.updateStorage();
		 */
		currentTime = System.currentTimeMillis();
		if (currentTime - previousTime > timeInterval) {
			Main.pStats.update();
			// getOwnership();
			// for(int i = 0 ; i< galaxy[currentStar].getNumOfPlanets(); i++){
			// client.sendMessage(Message.createGetShips(currentStar, i));
			// }
			previousTime = currentTime;
		}
		player.update(time, 0, 0, null, null);
	}

	/**
	 * draws the galaxy to the given graphics context
	 * 
	 * @param g
	 *            the graphics context used to draw the galaxy
	 */
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) galaxyImage.getGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, galaxyImage.getWidth(), galaxyImage.getHeight());
		switch (State.state) {
		case GAME_GALACTIC:
			galaxyTransform = new AffineTransform();
			galaxyTransform.translate(InputHandler.midPoint.x, InputHandler.midPoint.y);
			galaxyTransform.scale(zoom, zoom);
			galaxyTransform.translate(-InputHandler.midPoint.x, -InputHandler.midPoint.y);
			g2d.setTransform(galaxyTransform);
			for (Star s : galaxy) {
				if (s != null) {
					s.draw(g2d);
				}
			}
			break;
		case GAME_SOLAR:
			solarTransform = new AffineTransform();
			solarTransform.translate(InputHandler.midPoint.x, InputHandler.midPoint.y);
			solarTransform.scale(zoomSolar, zoomSolar);
			solarTransform.translate(-InputHandler.midPoint.x, -InputHandler.midPoint.y);
			g2d.setTransform(solarTransform);
			for (Planet p : solarSystems[currentStar]) {
				if (p != null) {
					p.drawOrbits(g2d);
				}
			}
			for (Planet p : solarSystems[currentStar]) {
				if (p != null) {
					p.draw(g2d, playerNumber);
				}
			}
			galaxy[currentStar].draw(g2d);
			break;
		case GAME_PLANETARY:
			if (!galaxy[currentStar].isGenerated()) {
				initialiseSolarSystem();
			}
			if (!solarSystems[currentStar][currentPlanet].isGenerated()) {
				initialisePlanet();
			}
			solarSystems[currentStar][currentPlanet].draw(g2d, playerNumber);
			break;
		case GAME_BATTLE:
			battle.draw(g2d);
			break;
		default:
			break;
		}
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, galaxyImage.getWidth(), galaxyImage.getHeight());
		g.drawImage(galaxyImage, 0, 0, null);
		g.setFont(new Font("Verdana", Font.PLAIN, InputHandler.screenSize.width / 100));
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, InputHandler.screenSize.width, InputHandler.screenSize.height / 20);
		g.setColor(Color.white);
		String s = "Metal: " + Main.pStats.getMetal() + "    Uranium: " + Main.pStats.getUranium() + "    Aether: "
				+ Main.pStats.getAether() + "    Water: " + Main.pStats.getWater() + "    Storage: "
				+ Main.pStats.getStorage() + "    Population: " + Main.pStats.getActivePopulation() + " Online: "
				+ Main.numOfPlayers + "       Crystal:" + Main.pStats.getCrystal() + "           Planets Owned: "
				+ Main.pStats.getNumberOfPlanets() + "      Points: " + Main.pStats.getPoints() + "     Money: "
				+ Main.pStats.getMoney();
		g.drawString(s, 0, InputHandler.screenSize.height / 30);
		player.draw(g, 2);
		popUp.draw((Graphics2D) g);
	}

	/**
	 * adds a trade route between two planets
	 * 
	 * @param star
	 *            the id of the star
	 * @param p1
	 *            the first planet
	 * @param p2
	 *            the second planet
	 */
	public void addTradeRoute(int star, int p1, int p2) {
		galaxy[star].addTradeRoute(solarSystems[star][p1], solarSystems[star][p2]);
	}

	/**
	 * removes the trade route from the current planet
	 */
	public void removeTradeRoute() {
		galaxy[currentStar].removeTradeRoute(solarSystems[currentStar][currentPlanet]);
	}

	/**
	 * checks if a planet has a trade route
	 * 
	 * @param star
	 *            the id of the star
	 * @param p
	 *            the id of the planet being checked
	 * @return whether the planet is trading
	 */
	public boolean hasTrade(int star, int p) {
		if (solarSystems[star][p] != null) {
			return solarSystems[star][p].isTrading();
		} else {
			return false;
		}
	}

	/**
	 * sets that two planets are trading
	 * 
	 * @param star
	 *            the star
	 * @param p1
	 *            the first planet
	 * @param p2
	 *            the second planet
	 */
	public void setTrading(int star, int p1, int p2) {
		solarSystems[star][p1].setTrading(true);
		solarSystems[star][p2].setTrading(true);
	}

	/**
	 * checks if the current planet has finished trading (i.e. the progress bar
	 * is complete)
	 * 
	 * @return whether the planet is finished trading
	 */
	public boolean finishedTrading() {
		return galaxy[currentStar].getTradeFinished(solarSystems[currentStar][currentPlanet]);
	}

	/**
	 * checks who owns a planet
	 * 
	 * @param star
	 *            the star this planet orbits
	 * @param planet
	 *            the planet id
	 * @return whether this planet is owned by the player
	 */
	public boolean isOwned(int star, int planet) {
		if (solarSystems[star][planet] != null)
			return playerNumber == solarSystems[star][planet].getOwned();
		return false;
	}

	/**
	 * checks if a star has been clicked on
	 * 
	 * @param p
	 *            the point that has been clicked
	 */
	public void clickedGalactic(Point p) {
		for (int i = 1; i < galaxy.length; i++) {
			if (galaxy[i].clicked(p, galaxyTransform)) {
				selectedStar = i;
				galaxy[i].setSelected(true);
			} else {
				galaxy[i].setSelected(false);
			}
		}
	}

	/**
	 * checks if a planet has been clicked on
	 * 
	 * @param p
	 *            the point that has been clicked
	 */
	public void clickedSolar(Point p) {
		for (int i = 0; i < solarSystems[currentStar].length; i++) {
			if (solarSystems[currentStar][i] != null) {
				if (solarSystems[currentStar][i].clicked(p, solarTransform)) {
					prevPlanet = selectedPlanet;
					selectedPlanet = i;
					solarSystems[currentStar][i].setSelected(true);
				} else {
					solarSystems[currentStar][i].setSelected(false);
				}
			}
		}
	}

	/**
	 * checks if something on a planet has been clicked
	 * 
	 * @param p
	 *            the point the mouse clicked
	 */
	public void clickedPlanetary(Point p) {
		solarSystems[currentStar][currentPlanet].resourceClicked(p);
	}

	/**
	 * moves the player to the selected object (star/planet)
	 */
	private void moveToSelected() {
		switch (State.state) {
		case GAME_GALACTIC:
			if (galaxy[selectedStar].getCenter().distance(InputHandler.midPoint) < 3) {
				double xdif = galaxy[selectedStar].getCenter().getX() - InputHandler.midPoint.x;
				moveLR(-xdif);
				double ydif = galaxy[selectedStar].getCenter().getY() - InputHandler.midPoint.y;
				moveUD(-ydif);
				currentStar = selectedStar;
				player.setMoving(false);
			} else {
				currentStar = 0;
				Point2D.Double vector = MathHandler.getPoint2D(galaxy[selectedStar].getCenter(), InputHandler.midPoint,
						2, 0);
				moveUD(vector.y);
				moveLR(vector.x);
				player.setMoving(true);
			}
			break;
		case GAME_SOLAR:
			if (solarSystems[currentStar][selectedPlanet].getCenter().distance(InputHandler.midPoint) < 3) {
				double xdif = solarSystems[currentStar][selectedPlanet].getCenter().getX() - InputHandler.midPoint.x;
				moveLR(-xdif);
				double ydif = solarSystems[currentStar][selectedPlanet].getCenter().getY() - InputHandler.midPoint.y;
				moveUD(-ydif);
				currentPlanet = selectedPlanet;
				player.setMoving(false);
			} else {
				currentPlanet = 0;
				Point2D.Double vector = MathHandler.getPoint2D(solarSystems[currentStar][selectedPlanet].getCenter(),
						InputHandler.midPoint, 4, 0);
				galaxy[currentStar].moveTradeRoutes(vector);
				// Point2D.Double p = galaxy[currentStar].getSolarPos();
				// double distance = MathHandler.getDistance(p,
				// InputHandler.midPoint);
				// double distance2 = MathHandler.getDistance(p,
				// solarSystems[currentStar][selectedPlanet].getCenter());
				// double angle = Math.toDegrees(MathHandler.getAngle(p,
				// InputHandler.midPoint));
				// double angle2 = Math.toDegrees(MathHandler.getAngle(p,
				// solarSystems[currentStar][selectedPlanet].getCenter()));
				// System.out.println(distance + ", " + distance2 + ", " + angle
				// + ", " + angle2);
				// angle = angle - angle2;
				// if(angle > 180){
				// angle -= 360;
				// }else if(angle < -180){
				// angle += 360;
				// }
				// distance = (distance - distance2)/angle;
				//
				// Point2D.Double vector =
				// MathHandler.convertPolarToCartesian(1, distance, 0, 0);

				moveUD(vector.y);
				moveLR(vector.x);
				player.setMoving(true);
			}

			break;
		default:
			break;
		}
	}

	/**
	 * moves up or down by the given amount
	 * 
	 * @param amount
	 *            the amount to move by
	 */
	public void moveUD(double amount) {
		switch (State.state) {
		case GAME_GALACTIC:
			for (Star s : galaxy) {
				if (s != null) {
					s.moveUD(amount);
				}
			}
			break;
		case GAME_SOLAR:
			for (Planet p : solarSystems[currentStar]) {
				if (p != null) {
					p.moveUD(amount);
				}
			}
			galaxy[currentStar].incrementSolarPos(0, amount);
			break;
		case GAME_PLANETARY:
			solarSystems[currentStar][currentPlanet].movePlanet(0, (float) amount);
			break;
		case GAME_BATTLE:
			battle.moveBattle(0, (float) amount);
			break;
		default:
			break;
		}
	}

	/**
	 * moves left or right by the given amount
	 * 
	 * @param amount
	 *            the amount to move by
	 */
	public void moveLR(double amount) {
		switch (State.state) {
		case GAME_GALACTIC:
			for (Star s : galaxy) {
				if (s != null) {
					s.moveLR(amount);
				}
			}
			break;
		case GAME_SOLAR:
			for (Planet p : solarSystems[currentStar]) {
				if (p != null) {
					p.moveLR(amount);
				}
			}
			galaxy[currentStar].incrementSolarPos(amount, 0);
			break;
		case GAME_PLANETARY:
			solarSystems[currentStar][currentPlanet].movePlanet((float) amount, 0);
			break;
		case GAME_BATTLE:
			battle.moveBattle((float) amount, 0);
			break;
		default:
			break;
		}
	}

	/**
	 * gets the player speed
	 * 
	 * @return the player speed
	 */
	public float getPlayerSpeed() {
		return player.getSpeed();
	}

	/**
	 * sets that the player is moving
	 * 
	 * @param moving
	 *            the new value of moving
	 */
	public void setPlayerMoving(boolean moving) {
		player.setMoving(moving);
	}

	/**
	 * increases the zoom by the specified amount
	 * 
	 * @param amount
	 *            the amount to zoom in by
	 */
	public void zoomIn(double amount) {
		switch (State.state) {
		case GAME_GALACTIC:
			if (zoom < 3.0) {
				zoom += amount;
			} else {
				if (currentStar != 0) {
					System.out.println("entering");
					client.sendMessage(Message.createRequestOwnership(1, playerNumber));
					State.changeState(State.STATE.GAME_SOLAR);
					if (zoomInGalaxy) {
						setPopUp(new PopUp(
								"Seems like you've got it!\nYou entered back on a star.\nYou can click on any planet in order\nto travel to it.\nEnjoy and good luck in fights!\nHope to see you in the top with huge\nnumber of planets owned!"));
						zoomInGalaxy = false;
					}
				}
			}
			break;
		case GAME_SOLAR:
			if (zoomSolar < 3.0) {
				zoomSolar += amount;
			} else {
				if (currentPlanet != 0 && selectedPlanet != 0) {
					getOwnership();
					if (solarSystems[currentStar][currentPlanet].getOwned() == playerNumber) {
						selectedPlanet = 0;
						if (zoomInSolar) {
							setPopUp(new PopUp(
									"You've got it!\nYou entered back on a planet.\nYou can zoom out again to see the solar system."));
							zoomInSolar = false;
						}
						State.changeState(State.STATE.GAME_PLANETARY);
					} else {
						//client.sendMessage(Message.createGetShips(currentStar, currentPlanet));
						State.changeState(State.STATE.MENU_BATTLE);
					}
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * decreases the zoom by the specified amount
	 * 
	 * @param amount
	 *            the amount to zoom out by
	 */
	public void zoomOut(double amount) {
		switch (State.state) {
		case GAME_GALACTIC:
			if (firstEnteredSolar) {
				setPopUp(new PopUp(
						"This is the galaxy.\nThe blue circle near a star means that\nyou have at least one planet there!\nIf you zoom out more you'll see\nall the stars in the universe!\nYou can travel from one star to another by\nclicking on a particular star!\nYou can see what's on a star by\nzooming in."));
				firstEnteredSolar = false;
			}
			if (zoom > 0.2) {
				zoom -= amount;
			}
			break;
		case GAME_SOLAR:
			if (zoomSolar > 0.5) {
				zoomSolar -= amount;
			} else {
				State.changeState(State.STATE.GAME_GALACTIC);
				if (galaxy[currentStar].isOwned()) {
					prevStar = currentStar;
				}
				if (selectedPlanet != 0 && solarSystems[currentStar][currentPlanet] != null) {
					solarSystems[currentStar][currentPlanet].setSelected(false);
				}
			}
			break;
		case GAME_PLANETARY:
			if (solarSystems[currentStar][currentPlanet].getOwned() == playerNumber) {
				prevOwnedPlanet = currentPlanet;
			}
			if (firstEnteredGalaxy) {
				setPopUp(new PopUp(
						"This is a solar system.\nYou can travel from one planet to another by\nclicking on the planet you want to travel to!\nAlso, you can zoom in or out to see what's on a\nplanet!\nIf you zoom out more you'll see all\nthe stars in the galaxy!\nTry it."));
				firstEnteredGalaxy = false;
			}
			State.changeState(State.STATE.GAME_SOLAR);
			solarSystems[currentStar][currentPlanet].setDrawn(false);
			getOwnership();
		default:
			break;
		}
	}

	/**
	 * checks if the galaxy has been created
	 * 
	 * @return whether the galaxy is created
	 */
	public boolean isCreated() {
		return created;
	}

	/**
	 * sets whether the galaxy has been created
	 * 
	 * @param created
	 *            the new value of created
	 */
	public void setCreated(boolean created) {
		this.created = created;
	}

	/**
	 * gets the number of fighters on a planet
	 * 
	 * @return the number of fighters
	 */
	public int getNumOfFightersOnPlanet() {
		return solarSystems[currentStar][currentPlanet].getNumOfFighters();
	}

	/**
	 * gets the number of carriers on a planet
	 * 
	 * @return the number of carriers
	 */
	public int getNumOfCarriersOnPlanet() {
		return solarSystems[currentStar][currentPlanet].getNumOfCarriers();
	}

	/**
	 * gets the number of command ships on a planet
	 * 
	 * @return the number of command ships
	 */
	public int getNumOfCommandOnPlanet() {
		return solarSystems[currentStar][currentPlanet].getNumOfCommand();
	}

	/**
	 * gets the current planets name
	 * 
	 * @return the planets name
	 */
	public String getCurrentPlanetName() {
		return solarSystems[currentStar][currentPlanet].getName();
	}

	/**
	 * gets the current stars id
	 * 
	 * @return the star id
	 */
	public int getCurrentStar() {
		return currentStar;
	}

	/**
	 * sets what the current star is
	 * 
	 * @param currentStar
	 *            the new current star
	 */
	public void setCurrentStar(int star) {
		currentStar = star;
	}

	/**
	 * gets the current planets id
	 * 
	 * @return the current planets id
	 */
	public int getCurrentPlanet() {
		return currentPlanet;
	}

	/**
	 * gets a planet based on an id
	 * 
	 * @param starID
	 *            the star id
	 * @param planetID
	 *            the planets id
	 * @return the planet
	 */
	public Planet getPlanet(int starID, int planetID) {
		return solarSystems[starID][planetID];
	}

	/**
	 * sets the current planets
	 * 
	 * @param currentPlanet
	 *            the new current planet
	 */
	public void setCurrentPlanet(int planet) {
		currentPlanet = planet;
	}

	/**
	 * gets the previous planet visited
	 * 
	 * @return the previous planets id
	 */
	public int getPrevPlanet() {
		return prevPlanet;
	}

	/**
	 * sets what the previous planet was
	 * 
	 * @param prevPlanet
	 *            the new previous planet
	 */
	public void setPrevPlanet(int planet) {
		prevPlanet = planet;
	}

	/**
	 * checks if the battle has started
	 * 
	 * @return whether the battle has started
	 */
	public boolean isBattleStarted() {
		return battleStarted;
	}

	/**
	 * sets that a battle has started
	 * 
	 * @param battleStarted
	 */
	public void setBattleStarted(boolean battleStarted) {
		this.battleStarted = battleStarted;
	}

	/**
	 * gets the id of the previous owned planet
	 * 
	 * @return the id of the previous owned planet
	 */
	public int getPrevOwnedPlanet() {
		return prevOwnedPlanet;
	}

	/**
	 * sets the previous owned planet
	 * 
	 * @param prevOwnedPlanet
	 *            the new previous planet
	 */
	public void setPrevOwnedPlanet(int planet) {
		prevOwnedPlanet = planet;
	}

	/**
	 * gets the currently displayer pop up
	 * 
	 * @return the pop up
	 */
	public static PopUp getPopUp() {
		return popUp;
	}

	/**
	 * sets the pop up to a new instance of PopUp
	 * 
	 * @param newPopUp
	 *            the new PopUp
	 */
	public static void setPopUp(PopUp newPopUp) {
		popUp = newPopUp;
	}

	/**
	 * gets the player number
	 * 
	 * @return the player number
	 */
	public static int getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * sets the player number
	 * 
	 * @param playerNumber
	 *            the new player number
	 */
	public static void setPlayerNumber(int playerNumber) {
		Galaxy.playerNumber = playerNumber;
	}

}
