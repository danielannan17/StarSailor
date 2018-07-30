package gameAI.battle.battle_testing;

import entities.Player;
import entities.Ship;
import entities.Vector2D;
import entities.projectiles.BasicProjectile;
import entities.skills.Flash;
import gameAI.battle.BattleAI;
import gameAI.battle.SquadAI;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BattleAITest {

	private BattleAI ai;
	private int size;

	private LinkedList<entities.Entity> team1;
	private LinkedList<entities.Entity> team2;

	@Before
	public void setUp(){

		size = 4;
		Random rand = new Random();

		team1 = new LinkedList<>();
		team2 = new LinkedList<>();

		for (int i = 0; i < size; i++){

			team1.add(
					new Ship(
							rand.nextInt(2000),
							rand.nextInt(2000),
							new Vector2D(
									rand.nextInt(2000),
									rand.nextInt(2000)
							),
							i + ""
					)
			);

			team2.add(
					new Ship(
							rand.nextInt(2000),
							rand.nextInt(2000),
							new Vector2D(
									rand.nextInt(2000),
									rand.nextInt(2000)
							),
							i + ""
					)
			);
		}

		entities.Player player = new Player("0",1,1,100,450,new int[]{0,0,0,0,0,0,0,0,0,0},new Flash(),new Flash(), new Flash());
		ai = new BattleAI(1, 2000, 2000, "EASY", player, team1, team2, null, 1, 1000/60);
	}

	@Test
	public void testRun() {
		fail("Not yet implemented");
	}

	@Test
	public void testBattleAI() {

		assertTrue(this.ai != null);

	}

	@Test
	public void testRunEasyAI() throws InterruptedException {

		ai.start();
		TimeUnit.SECONDS.sleep(3);
		for (SquadAI sq: ai.getAlliedSquadsOfTheEnvironment()){
			assertTrue(sq.getfCurrentOrder() == null);
		}
		ai.stopAI();
	}

	@Test
	public void testAddEntity() {
		ai.addEntity(new BasicProjectile());
	}

	@Test
	public void testAddProjectileToProjectileList() {

		ai.addProjectileToProjectileList(new BasicProjectile());

		assertTrue(ai.getEntitiesOfTheEnvironment().size() == 9);
	}

	@Test
	public void testGetEntitiesOfTheEnvironment() {
		assertTrue(ai.getEntitiesOfTheEnvironment().size() == 8);
	}

	@Test
	public void testGetTheObstaclesOfTheEnvironment() {
		assertTrue(ai.getTheObstaclesOfTheEnvironment().size() == 0);
	}

	@Test
	public void testGetTheEnemyShipsOfTheEnvironment() {
		assertTrue(ai.getTheEnemyShipsOfTheEnvironment().size() == 4);
	}

	@Test
	public void testGetAlliedSquadsOfTheEnvironment() {
		assertTrue(ai.getAlliedSquadsOfTheEnvironment().size() == 4);
	}

	@Test
	public void testGetTheProjectilesOfTheEnvironment() {
		ai.addProjectileToProjectileList(new BasicProjectile());
		assertTrue(ai.getTheProjectilesOfTheEnvironment().size() == 1);
	}

	@Test
	public void testStopAI() {

		ai.start();
		ai.stopAI();

		//assertFalse(ai.isInterrupted());
	}

	@Test
	public void testRemoveDestroyedSquad() {
		int initialSize = ai.getAlliedSquadsOfTheEnvironment().size();
		ai.removeDestroyedSquad(ai.getAlliedSquadsOfTheEnvironment().get(0));
		assertTrue(initialSize - 1 == ai.getAlliedSquadsOfTheEnvironment().size());
	}

	@Test
	public void testRemovedDestroyedEnemyShip() {
		int initilaSize = ai.getTheEnemyShipsOfTheEnvironment().size();
		ai.removedDestroyedEnemyShip(ai.getTheEnemyShipsOfTheEnvironment().get(0));
		assertTrue(initilaSize - 1 == ai.getTheEnemyShipsOfTheEnvironment().size());
	}

	@Test
	public void testGetStandardCheckingRadius() {
		assertTrue(ai.getBattleWidth()/10 == ai.getStandardCheckingRadius());
	}

	@Test
	public void testGetBattleWidth() {
		assertTrue(ai.getBattleWidth() == 2000);
	}

	@Test
	public void testGetBattleHeight() {
		assertTrue(ai.getBattleHeight() == 2000);
	}

	@Test
	public void testGetSleepTimeCoefficient() {
		assertTrue(ai.getSleepTimeCoefficient() == 3);
	}
}
