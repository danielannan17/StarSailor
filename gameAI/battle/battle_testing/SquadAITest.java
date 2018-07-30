package gameAI.battle.battle_testing;

import entities.Player;
import entities.Ship;
import entities.Vector2D;
import entities.skills.Flash;
import gameAI.battle.BattleAI;
import gameAI.battle.BattleOrder;
import gameAI.battle.SquadAI;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SquadAITest {

	private SquadAI sq;

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
		
		for (SquadAI sq: ai.getAlliedSquadsOfTheEnvironment()){
			for (int i = 0; i < sq.getShips().size(); i++) {
				if (sq.getShips().get(i).getHealth() <= 0){
					sq.getShips().get(i).setHealth(100);
				}
			}
			assertTrue(sq.getfSquadFormation().isSquadAlive());
		}
		
		for (SquadAI sq: ai.getAlliedSquadsOfTheEnvironment()){
			sq.start();
			sq.setfCurrentOrder(new BattleOrder());
		}
		assertTrue(ai.getAlliedSquadsOfTheEnvironment().stream().allMatch(sq -> sq.getfSquadFormation().isSquadAlive()));
		ai.stopAI();
	}

	@Test
	public void testSquadAI() {
		assertFalse(ai.getAlliedSquadsOfTheEnvironment().get(0) == null);
	}

	@Test
	public void testSetfCurrentOrder() {

		ai.getAlliedSquadsOfTheEnvironment().get(0).setfCurrentOrder(new BattleOrder());
		assertTrue(ai.getAlliedSquadsOfTheEnvironment().get(0).getfCurrentOrder() != null);
	}

	@Test
	public void testGetfCurrentOrder() {

		ai.getAlliedSquadsOfTheEnvironment().get(0).setfCurrentOrder(new BattleOrder());
		assertTrue(ai.getAlliedSquadsOfTheEnvironment().get(0).getfCurrentOrder() != null);
	}

	@Test
	public void testIsPerformingAnyAIGivenOrder() {

		ai.getAlliedSquadsOfTheEnvironment().get(0).setfCurrentOrder(new BattleOrder());
		assertTrue(ai.getAlliedSquadsOfTheEnvironment().get(0).isPerformingAnyAIGivenOrder());
	}

	@Test
	public void testSetfRunFlag() throws InterruptedException {

		ai.getAlliedSquadsOfTheEnvironment().get(0).start();
		ai.getAlliedSquadsOfTheEnvironment().get(0).setfRunFlag(true);
		TimeUnit.SECONDS.sleep(4);
		assertFalse(ai.getAlliedSquadsOfTheEnvironment().get(0).isAlive());
	}

	@Test
	public void testGetShipDescriptions() {
		assertFalse(ai.getAlliedSquadsOfTheEnvironment().get(0).getShipDescriptions().isEmpty());
	}

	@Test
	public void testHasSquadBeenDestroyed() {
		ai.getAlliedSquadsOfTheEnvironment().get(0).getLeader().setHealth(0);
		assertFalse(ai.getAlliedSquadsOfTheEnvironment().get(0).isAlive());
	}

	@Test
	public void testGetSquadSize() {
		assertEquals(ai.getAlliedSquadsOfTheEnvironment().get(0).hasSquadBeenDestroyed(), false);
		assertEquals(ai.getAlliedSquadsOfTheEnvironment().get(0).getSquadSize(), 1);
		assertEquals(ai.getAlliedSquadsOfTheEnvironment().get(0).getShips().size(), 1);
	}

	@Test
	public void testGetLeader() {
		assertTrue(ai.getAlliedSquadsOfTheEnvironment().get(0).getLeader() != null);
	}

	@Test
	public void testGetShips() {
		assertEquals(ai.getAlliedSquadsOfTheEnvironment().get(0).getShips().size(), 1);
	}

	@Test
	public void testCompareTo() {
		assertTrue(ai.getAlliedSquadsOfTheEnvironment().get(0).compareTo(ai.getAlliedSquadsOfTheEnvironment().get(0)));
	}
}
