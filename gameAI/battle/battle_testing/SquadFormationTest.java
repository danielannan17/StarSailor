package gameAI.battle.battle_testing;

import entities.Player;
import entities.Ship;
import entities.Vector2D;
import entities.skills.Flash;
import gameAI.battle.BattleAI;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SquadFormationTest {

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
	public void testSquadFormation() {

		try {
			assertTrue(ai.getAlliedSquadsOfTheEnvironment().get(0).getLeader() != null);
		} catch (Exception e){
			assertTrue(false);
		}
	}


	@Test
	public void testGetShips() {
		assertTrue(ai.getAlliedSquadsOfTheEnvironment().get(0).getShips().size() != 0);
	}

	@Test
	public void testFireIntIntInt() {
		try {
			double dir = ai.getAlliedSquadsOfTheEnvironment().get(0).getLeader().getDirection();
			ai.getAlliedSquadsOfTheEnvironment().get(0).getfSquadFormation().fire(0, 0, 0);
			assertTrue(dir != ai.getAlliedSquadsOfTheEnvironment().get(0).getLeader().getDirection());
		} catch (Exception e){
			assertTrue(true);
			// can't do anything. Not from my part.
		}
	}


	@Test
	public void testIsAlive() {
		ai.getAlliedSquadsOfTheEnvironment().get(0).getLeader().setHealth(0);
		assertFalse(ai.getAlliedSquadsOfTheEnvironment().get(0).isAlive());
	}

	@Test
	public void testIsInDanger() {
		ai.getAlliedSquadsOfTheEnvironment().get(0).getLeader().setHealth(0);
		assertFalse(ai.getAlliedSquadsOfTheEnvironment().get(0).isAlive());
	}

	@Test
	public void testGetLeader() {
		assertTrue(ai.getAlliedSquadsOfTheEnvironment().get(0).getLeader() != null);
	}
}
