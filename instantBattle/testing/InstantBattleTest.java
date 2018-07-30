package instantBattle.testing;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entities.AvailableSkill;
import entities.BattleMessage;
import entities.FlashMessage;
import entities.Player;
import entities.ProjectileMessage;
import entities.Selector;
import entities.Ship;
import entities.ShipMessage;
import entities.projectiles.BasicProjectile;
import entities.projectiles.PenetratingProjectile;
import handlers.InputHandler;
import instantBattle.Battle;
import main.Main;
import menu.InstantMenu;

public class InstantBattleTest {


	static JFrame frame;
	static InstantMenu instant;
	static Battle battle;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Main.projectileCache = new HashMap<String,BufferedImage[][]>();
		Main.cache = new HashMap<String,Image[]>();
		Selector.initialise();
		AvailableSkill.initialise();
		frame = new JFrame();
		frame.setBounds(0,0,InputHandler.screenSize.width,InputHandler.screenSize.height);
		frame.setVisible(true);
		
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		frame.dispose();
		Main.projectileCache = null;
		Main.cache = null;
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		Thread.sleep(1000);
		
	}

	@Test
	public void testPlayersType1DrawnAndMakeSprites() {
		battle = new Battle(Battle.makeSprites(1, Color.WHITE),0, Color.RED);
		Battle.myTeam = 1;
		Player player1 = new Player("",1, 1, 100, 100, Selector.getShipStats(1), AvailableSkill.getSkill(2), AvailableSkill.getSkill(2),
				AvailableSkill.getSkill(2));
		Player player2 = new Player("",1, 2, 200, 200, Selector.getShipStats(1), AvailableSkill.getSkill(2), AvailableSkill.getSkill(2),
				AvailableSkill.getSkill(2));
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		state.add(new ShipMessage(player1));
		state.add(new ShipMessage(player2));
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());
	
	}
	
	@Test
	public void testPlayersType2Drawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		Player player1 = new Player("",2, 1, 100, 100, Selector.getShipStats(1), AvailableSkill.getSkill(2), AvailableSkill.getSkill(2),
				AvailableSkill.getSkill(2));
		Player player2 = new Player("",2, 2, 200, 200, Selector.getShipStats(1), AvailableSkill.getSkill(2), AvailableSkill.getSkill(2),
				AvailableSkill.getSkill(2));
		
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		state.add(new ShipMessage(player1));
		state.add(new ShipMessage(player2));
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());
	}
	
	@Test
	public void testShipType2Drawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		Ship ship1 = Selector.getShip(2);
		ship1.setLocation(100, 100);
		ship1.setTeam(1);
		Ship ship2 = Selector.getShip(2);
		ship2.setLocation(200, 200);
		ship2.setTeam(2);
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		state.add(new ShipMessage(ship1));
		state.add(new ShipMessage(ship2));
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());
	}
	
	@Test
	public void testShipType1Drawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		Ship ship1 = Selector.getShip(1);
		ship1.setLocation(100, 100);
		ship1.setTeam(1);
		Ship ship2 = Selector.getShip(1);
		ship2.setLocation(200, 200);
		ship2.setTeam(2);
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		state.add(new ShipMessage(ship1));
		state.add(new ShipMessage(ship2));
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());
	}
	
	
	@Test
	public void testBasicProjectileDrawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		BasicProjectile projectile1 = new BasicProjectile();
		projectile1.id = 4;
		projectile1.setxLocation(100);
		projectile1.setyLocation(100);
		projectile1.setTargetTeam(1);
		BasicProjectile projectile2 = new BasicProjectile();
		projectile2.id = 4;
		projectile2.setxLocation(200);
		projectile2.setyLocation(200);
		projectile2.setTargetTeam(2);
		
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		state.add(new ProjectileMessage(projectile1));
		state.add(new ProjectileMessage(projectile2));
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());	
	}
	
	@Test
	public void testPenetrateProjectileDrawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		BasicProjectile projectile1 = new BasicProjectile();
		projectile1.id = 3;
		projectile1.setxLocation(100);
		projectile1.setyLocation(100);
		projectile1.setTargetTeam(1);
		BasicProjectile projectile2 = new BasicProjectile();
		projectile2.id = 3;
		projectile2.setxLocation(200);
		projectile2.setyLocation(200);
		projectile2.setTargetTeam(2);
		
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		state.add(new ProjectileMessage(projectile1));
		state.add(new ProjectileMessage(projectile2));
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());	
	}
	
	@Test
	public void testLightningOrbProjectileDrawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		BasicProjectile projectile1 = new BasicProjectile();
		projectile1.id = 2;
		projectile1.setxLocation(100);
		projectile1.setyLocation(100);
		projectile1.setTargetTeam(1);
		BasicProjectile projectile2 = new BasicProjectile();
		projectile2.id = 2;
		projectile2.setxLocation(200);
		projectile2.setyLocation(200);
		projectile2.setTargetTeam(2);
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		state.add(new ProjectileMessage(projectile1));
		state.add(new ProjectileMessage(projectile2));
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());	
	}
	
	@Test
	public void testFlashFrame1Drawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		FlashMessage flash =new FlashMessage(1,100,100,200,200);
		state.add(flash);
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());	
	}
	
	@Test
	public void testFlashFrame2Drawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		FlashMessage flash =new FlashMessage(1,100,100,200,200);
		flash.incrementFrame();
		state.add(flash);
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());	
	}
	
	@Test
	public void testFlashFrame3Drawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		FlashMessage flash =new FlashMessage(1,100,100,200,200);
		for (int i = 0; i < 2;i++) {
			flash.incrementFrame();
		}
		state.add(flash);
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());	
	}
	
	@Test
	public void testFlashFrame4Drawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		FlashMessage flash =new FlashMessage(1,100,100,200,200);
		for (int i = 0; i < 3;i++) {
			flash.incrementFrame();
		}
		state.add(flash);
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());	
	}
	
	@Test
	public void testFlashFrame5Drawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		FlashMessage flash =new FlashMessage(1,100,100,200,200);
		for (int i = 0; i < 4;i++) {
			flash.incrementFrame();	}
		state.add(flash);
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());	
	}
	@Test
	public void testFlashFrame6Drawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		FlashMessage flash =new FlashMessage(1,100,100,200,200);
		for (int i = 0; i < 5;i++) {
			flash.incrementFrame();	}
		state.add(flash);
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());	
	}
	@Test
	public void testFlashFrame7Drawn() {
		battle = new Battle(Battle.makeSprites(2, Color.WHITE),2, Color.RED);
		Battle.myTeam = 1;
		ArrayList<BattleMessage> state = new ArrayList<BattleMessage>();
		FlashMessage flash =new FlashMessage(1,100,100,200,200);
		for (int i = 0; i < 6;i++) {
			flash.incrementFrame();	}
		state.add(flash);
		battle.setCurrentState(state);
		battle.draw(frame.getGraphics());	
	}

}
