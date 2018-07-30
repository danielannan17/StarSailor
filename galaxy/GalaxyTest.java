package galaxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;

import galaxy.network.Message;
import main.State;
import messaging.PopUp;

public class GalaxyTest {
	
	Galaxy galaxy;
	
	@Before
	public void setUp(){
		galaxy = new Galaxy(100, true);
	}

	@Test
	public void testInitialiseSolarSystem() {
		galaxy.initialiseSolarSystem();
	}

	@Test
	public void testReinitialiseSolarSystem() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOwnership() {
		galaxy.getOwnership();
	}

	@Test
	public void testSetOwnership() {
		fail("Not yet implemented");
	}

	@Test
	public void testSendShips() {
		Galaxy.sendShips();
	}

	@Test
	public void testGetShips() {
		Galaxy.getShips(Message.createSetShips(1, 2, 3, 3, 3));
	}

	@Test
	public void testInitialisePlanet() {
		galaxy.initialisePlanet();
	}

	@Test
	public void testUpdateFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testDraw() {
		State.changeState(State.STATE.GAME_GALACTIC);
		galaxy.draw((new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB).getGraphics()));
		State.changeState(State.STATE.GAME_SOLAR);
		galaxy.draw((new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB).getGraphics()));
		State.changeState(State.STATE.GAME_PLANETARY);
		galaxy.draw((new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB).getGraphics()));
	}

	@Test
	public void testAddTradeRoute() {
		galaxy.addTradeRoute(1, 1, 2);
	}

	@Test
	public void testRemoveTradeRoute() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasTrade() {
		galaxy.hasTrade(1, 1);
	}

	@Test
	public void testSetTrading() {
		galaxy.setTrading(1, 1, 2);
	}

	@Test
	public void testFinishedTrading() {
		galaxy.finishedTrading();
	}

	@Test
	public void testIsOwned() {
		assertTrue(galaxy.isOwned(1, 1));
	}

	@Test
	public void testClickedGalactic() {
		fail("Not yet implemented");
	}

	@Test
	public void testClickedSolar() {
		fail("Not yet implemented");
	}

	@Test
	public void testClickedPlanetary() {
		fail("Not yet implemented");
	}

	@Test
	public void testMoveUD() {
		galaxy.moveUD(1);
		State.changeState(State.STATE.GAME_SOLAR);
		galaxy.moveUD(1);
		State.changeState(State.STATE.GAME_PLANETARY);
		galaxy.moveUD(1);
	}

	@Test
	public void testMoveLR() {
		galaxy.moveLR(1);
		State.changeState(State.STATE.GAME_SOLAR);
		galaxy.moveLR(1);
		State.changeState(State.STATE.GAME_PLANETARY);
		galaxy.moveLR(1);
	}

	@Test
	public void testGetPlayerSpeed() {
		galaxy.getPlayerSpeed();
	}

	@Test
	public void testSetPlayerMoving() {
		galaxy.setPlayerMoving(true);
	}

	@Test
	public void testZoomIn() {
		galaxy.zoomIn(0.2);
		State.changeState(State.STATE.GAME_SOLAR);
		galaxy.zoomIn(0.2);
		State.changeState(State.STATE.GAME_PLANETARY);
		galaxy.zoomIn(0.2);
	}

	@Test
	public void testZoomOut() {
		galaxy.zoomOut(0.2);
		State.changeState(State.STATE.GAME_SOLAR);
		galaxy.zoomOut(0.2);
		State.changeState(State.STATE.GAME_PLANETARY);
		galaxy.zoomOut(0.2);
	}

	@Test
	public void testIsCreated() {
		galaxy.setCreated(true);
		assertTrue(galaxy.isCreated());
	}

	@Test
	public void testSetCreated() {
		galaxy.setCreated(true);
		assertTrue(galaxy.isCreated());
	}

	@Test
	public void testGetNumOfFightersOnPlanet() {
		galaxy.getNumOfFightersOnPlanet();
	}

	@Test
	public void testGetNumOfCarriersOnPlanet() {
		galaxy.getNumOfCarriersOnPlanet();
	}

	@Test
	public void testGetNumOfCommandOnPlanet() {
		galaxy.getNumOfCommandOnPlanet();
	}

	@Test
	public void testGetCurrentPlanetName() {
		galaxy.getCurrentPlanetName();
	}

	@Test
	public void testGetCurrentStar() {
		galaxy.getCurrentStar();
	}

	@Test
	public void testSetCurrentStar() {
		galaxy.setCurrentStar(2);
	}

	@Test
	public void testGetCurrentPlanet() {
		galaxy.getCurrentPlanet();
	}

	@Test
	public void testGetPlanet() {
		galaxy.getPlanet(1, 2);
	}

	@Test
	public void testSetCurrentPlanet() {
		galaxy.setCurrentPlanet(1);
	}

	@Test
	public void testGetPrevPlanet() {
		galaxy.getPrevPlanet();
	}

	@Test
	public void testSetPrevPlanet() {
		galaxy.setPrevPlanet(1);
	}

	@Test
	public void testIsBattleStarted() {
		galaxy.setBattleStarted(true);
		assertTrue(galaxy.isBattleStarted());
	}

	@Test
	public void testSetBattleStarted() {
		galaxy.setBattleStarted(true);
		assertTrue(galaxy.isBattleStarted());
	}

	@Test
	public void testGetPrevOwnedPlanet() {
		galaxy.getPrevOwnedPlanet();
	}

	@Test
	public void testSetPrevOwnedPlanet() {
		galaxy.setPrevOwnedPlanet(2);
	}

	@Test
	public void testGetPopUp() {
		Galaxy.getPopUp();
	}

	@Test
	public void testSetPopUp() {
		Galaxy.setPopUp(new PopUp("hello test"));
	}

	@Test
	public void testGetPlayerNumber() {
		Galaxy.setPlayerNumber(1);
		assertEquals(1, Galaxy.getPlayerNumber());
	}

	@Test
	public void testSetPlayerNumber() {
		Galaxy.setPlayerNumber(1);
		assertEquals(1, Galaxy.getPlayerNumber());
	}


}
