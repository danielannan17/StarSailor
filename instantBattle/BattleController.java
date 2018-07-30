package instantBattle;
import java.awt.Polygon;
import java.awt.RenderingHints.Key;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import entities.AvailableSkill;
import entities.BattleMessage;
import entities.CollisionHandler;
import entities.Entity;
import entities.FlashMessage;
import entities.Player;
import entities.ProjectileMessage;
import entities.Selector;
import entities.Ship;
import entities.ShipMessage;
import entities.Vector2D;
import entities.projectiles.Projectile;
import entities.skills.Skill;
import gameAI.battle.BattleAI;
import networking.BattleModeServer;
import networking.KeyMove;
import networking.KeyVal;
import networking.ServerState;

/***
 * battle controller class to manage instant battles
 *
 */
public class BattleController implements Runnable {
	public LinkedList<Entity> team1fleet;
	public LinkedList<Entity> team2fleet;
	public LinkedList<Entity> others;
	public PhysicsEngine physicsEngine;
	int count = 0;
	long last = System.currentTimeMillis();
	private LinkedList <Entity> allEntities;
	public static ArrayList<BattleMessage> toAdd;
	private BattleAI ai1;
	public static Player player1;
	public static Player player2;
	private BattleAI ai2;
	private final int ticksPerSecond = 60;
	public static int width = 1920;
	public static int height = 1080;
	
	/**
	 * add player to the battle
	 * @param player the player's information
	 */
	public void addPlayer(int[] player) {
		if (player != null) {
				Skill skill1 = AvailableSkill.getSkill(player[1]);
				Skill skill2 = AvailableSkill.getSkill(player[2]);
				Skill skill3 = AvailableSkill.getSkill(player[3]);
				int team = player[4];
				Player playerObj = new Player("Player" + team,player[0], team,100,100, Selector.getShipStats(player[0]),
						skill1, skill2, skill3);
			if (team == 1) {
				player1 = playerObj;
				team1fleet.add(playerObj);
			} else {
				player2 = playerObj;
				playerObj.setLocation(width-100, height-100);
				playerObj.setPosition(new Vector2D(width-100, height-100));
				team2fleet.add(playerObj);
			}
		}
	}
	
	/**
	 * start the game
	 */
	public void startGame() {
		BattleModeServer.changeState(ServerState.PLAYING);
		new Thread(physicsEngine).start();
		this.ai1.start();
		this.ai2.start();
	}
	
	/**
	 * create a new battle controller object
	 * @param player1 the first player
	 * @param fleet1 the first player's fleet
	 * @param player2 the second player
	 * @param fleet2 the second player's fleet
	 */
	public BattleController(int[] player1, int[] fleet1, int[] player2, int[] fleet2) {
		team1fleet = createArmy(1, fleet1);
		team2fleet = createArmy(2, fleet2);
		
		LinkedList<Entity> t1 = (LinkedList<Entity>) team1fleet.clone();
		LinkedList<Entity> t2 = (LinkedList<Entity>) team2fleet.clone();
		addPlayer(player1);
		addPlayer(player2);
		this.ai1 = new BattleAI(1,height,width,"MEDIUM", this.player1, t1, team2fleet, others, 1,1000/ticksPerSecond);
		this.ai2 = new BattleAI(2,height,width,"MEDIUM", this.player2, t2, team1fleet, others, 1,1000/ticksPerSecond);
		
		toAdd = new ArrayList<BattleMessage>();
		
		others = new LinkedList<Entity>();
		allEntities = new LinkedList<>();
		allEntities.addAll(team1fleet);
		allEntities.addAll(team2fleet);
		allEntities.addAll(others);
		physicsEngine = new PhysicsEngine(ticksPerSecond,allEntities,width,height);										
	
	}
	
	/**
	 * create an army/fleet
	 * @param team the team of the fleet
	 * @param army the army information
	 * @return
	 */
	private LinkedList<Entity> createArmy(int team, int[] army) {
		int count = 1;
		LinkedList<Entity> wholeArmy = new LinkedList<Entity>();
		for (int i = 0; i < army.length; i++) {
			for (int j = 0; j < army[i];j++) {
			Ship ship = Selector.getShip(i+1);
			ship.setId(team +"ship"+count);
			ship.setTeam(team);
			int y = count*100;
			int x = 100;
			if (team == 2) {
				x = width-100;
			} else {
				x = 100;
			}
			ship.setLocation(x , y);
			ship.setPosition(new Vector2D(x, y));
			wholeArmy.add(ship);
			count++;
			}
			
		}
		return wholeArmy;
	}
	
	/**
	 * remove a ship from the game
	 * @param e the ship to be removed
	 */
	private void removeShip(Ship e) {
		if (e.getTeam() == 1) {
			team1fleet.remove(e);
		} else {
			team2fleet.remove(e);
		}
		allEntities.remove(e);
		physicsEngine.removeEntity(e);
	}
	
	/**
	 * remove other entities from the simulation, like projectiles
	 * @param e the entity to be removed
	 */
	private void removeOtherEntity(Entity e) {
		others.remove(e);
		allEntities.remove(e);
		physicsEngine.removeEntity(e);
	}
	
	/**
	 * add entities to the game
	 * @param list
	 */
	private void addEntities(LinkedList<Entity> list) {
		for (Entity e : list)
			if (e != null)
				addEntity(e);
	}
	
	/**
	 * add an entity to the game
	 * @param e
	 */
	private void addEntity(Entity e) {
		others.add(e);
		allEntities.add(e);
		physicsEngine.addEntity(e);
		this.ai1.addEntity(e);
		this.ai2.addEntity(e);
	}
	

	/**
	 * handle the input of the player
	 * @param k the key value of the input
	 * @param i the player
	 */
	public static void handleInput(KeyVal k, int i) {
		if (i == 1) {
			switch (k) {
			case W:
				player1.moveUp();
				break;
			case A:
				player1.moveLeft();
				break;
			case S:
				player1.moveDown();
				break;
			case D:
				player1.moveRight();
				break;
			case BTN:
				player1.useSkill(0);
				break;
			case Q:
				player1.useSkill(1);
				break;
			case E:
				player1.useSkill(2);
				break;
			case SHIFT:
				player1.useSkill(3);
				break;
			}
		} else {
			switch (k) {
			case W:
				player2.moveUp();
				break;
			case A:
				player2.moveLeft();
				break;
			case S:
				player2.moveDown();
				break;
			case D:
				player2.moveRight();
				break;
			case BTN:
				player2.useSkill(0);
				break;
			case Q:
				player2.useSkill(1);
				break;
			case E:
				player2.useSkill(2);
				break;
			case SHIFT:
				player2.useSkill(3);
				break;
			}
		}
	}
	
	/**
	 * update the game
	 */
	public void update() {
		try {
		KeyVal[] moves1 = BattleModeServer.getPlayer1Move();
		for (KeyVal m : moves1) {
			handleInput(m,1);
		}
		}catch (Exception ex) {
			
		}
		try {
		KeyVal[] moves2 = BattleModeServer.getPlayer2Move();
		if (moves2 != null)

		for (KeyVal m : moves2) {
			handleInput(m,2);
		}
		} catch (Exception e) {
			
		}
		LinkedList<Entity> temp = new LinkedList<Entity>();
		temp.addAll(team1fleet);
		temp.addAll(team2fleet);
		for (Entity s : temp) {
			if (!((Ship) s).isAlive()) {
				removeShip((Ship) s);
			} else {
				((Ship)s).deactiveSkills();
			}
		}
		
		temp.clear();
		temp.addAll(others);
		for (Entity s : temp) {
			if (((Projectile) s).isRemove() || 
					((Projectile) s).getxLocation()+s.getSize().getWidth()/2 <= 0|| 
					((Projectile) s).getxLocation()-s.getSize().getWidth()/2 >= width ||
					((Projectile) s).getyLocation()+s.getSize().getHeight()/2 <= 0 || 
					((Projectile) s).getyLocation()-s.getSize().getHeight()/2 >= height) {
				removeOtherEntity(s);
			}
		}
		
		for (Entity e: team1fleet) {
			addEntities(((Ship) e).getMyEntities());
			}
		for (Entity e: team2fleet) {
			addEntities(((Ship) e).getMyEntities());
		}
		ArrayList<BattleMessage> toSend = new ArrayList<BattleMessage>();
		ArrayList<BattleMessage> clone = (ArrayList<BattleMessage>) toAdd.clone();
		for (BattleMessage msg : clone) {
				switch (msg.getMessageType()) {
				case 2:
					((FlashMessage) msg).update();
					if (msg.getFrame() < 0){
						toAdd.remove(msg);
					}else{
						
						toSend.add(msg);
					}
					break;
				}
						
		}
		for (Entity e: allEntities) {
			e.update();
			toSend.add(convertToBattleMessage(e));
		}
		
		BattleModeServer.sendBattleState(toSend);
			
	}
	
	/**
	 * convert the entity to a battle message for networking
	 * @param e the entity to convert
	 * @return the battle message version of the entity
	 */
	public BattleMessage convertToBattleMessage(Entity e) {
		if (Ship.class.isInstance(e)) {
			ShipMessage message = new ShipMessage((Ship) e);
			return message;
		} else if (Projectile.class.isInstance(e)) {
			ProjectileMessage message = new ProjectileMessage((Projectile) e);
			return message;
		}
		return null;
	}
	
	/**
	 * check the collisions in game
	 */
	private void  checkCollision() {
		LinkedList<Entity> team1fleet = new LinkedList<Entity>();
		LinkedList<Entity> team2fleet = new LinkedList<Entity>();
		LinkedList<Entity> others = new LinkedList<Entity>();
		team1fleet.addAll(this.team1fleet);
		team2fleet.addAll(this.team2fleet);
		others.addAll(this.others);
		for (Entity e1 : team1fleet) {
			for (Entity e2 : team2fleet) {
				if (e1.getHitBox().intersects(e2.getHitBox())) {
					CollisionHandler.handleCollision(e1, e2);
				}
		}
			
			for (Entity e3 : others) {
				if (e1.getHitBox().intersects(e3.getHitBox())) {
					CollisionHandler.handleCollision(e1, e3);
				}
			}	
		}
		for (Entity e2 : team2fleet) {
			for (Entity e3 : others) {
				if (e2.getHitBox().intersects(e3.getHitBox())) {
					CollisionHandler.handleCollision(e2, e3);
				}
			}
			
		}	
	}
	
	/**
	 * check if 2 polygons intersect
	 * @param polygon1 the first polygon
	 * @param polygon2 the second polygon
	 * @return do they intersect?
	 */
	private boolean polygonsIntersect(Polygon polygon1, Polygon polygon2) {
		Area x = new Area(polygon1);
		x.intersect(new Area(polygon2));
		return !x.isEmpty();
	}
	
	/**
	 * is the game over?
	 * @return game is over?
	 */
	public boolean isGameOver() {
		if (team1fleet.isEmpty() || team2fleet.isEmpty())
			return true;
		return false;
	}
	
	/**
	 * end the battle
	 */
	public void endBattle() {
		physicsEngine.stop();
		System.out.println("Player " + battleWinner() + " Wins");
		stopAIs();
	}
	
	/**
	 * who is the battle winner?
	 * @return the battle winner
	 */
	public int battleWinner() {
		if (team1fleet.isEmpty() && team2fleet.isEmpty()) {
			return 3;
	} else if (team1fleet.isEmpty()){
			return 2;
		} else if (team2fleet.isEmpty()) {
			return 1;
		} else {
			return 0;
		}
	}
	/**
	 * run the battle controller
	 */
	@Override
	public void run() {
		startGame();
		while (!isGameOver()) {
			update();
			checkCollision();
			sleep();
		}
		endBattle();
		BattleModeServer.sendEnd(battleWinner());
	}
	
	/**
	 * sleep the thread
	 */
	private void sleep() {
		try {
			Thread.sleep(1000/ticksPerSecond);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * stop the AIs
	 */
	public void stopAIs(){
		this.ai1.stopAI();
		this.ai2.stopAI();
		System.gc();
	}
}