package gameLogic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import database.DBConnect;
import generators.NameGenerator;
import main.Main;

/**
 * 
 * 
 *
 */
public class PlayerStatsTest {
	private PlayerStats ps;
	private Random randomGenerator;
	private int rValue, nr, level, seed, initialValue, counter;
	private String name;
	private ArrayList<Resources> resources;
	private Resources metal, uranium, aether, water, storage, population, money, ship1, ship2, ship3;
	private int[] metalRequiredToUpgradeMetal, uraniumRequiredToUpgradeMetal, aetherRequiredToUpgradeMetal, populationRequiredToUpgradeMetal;
	private int[] metalRequiredToUpgradeUranium, uraniumRequiredToUpgradeUranium, aetherRequiredToUpgradeUranium, populationRequiredToUpgradeUranium;
	private int[] metalRequiredToUpgradeAether, uraniumRequiredToUpgradeAether, aetherRequiredToUpgradeAether, populationRequiredToUpgradeAether;
	private int[] metalRequiredToUpgradeStorage, uraniumRequiredToUpgradeStorage, aetherRequiredToUpgradeStorage, populationRequiredToUpgradeStorage;
	private int[] metalRequiredToUpgradeRecruitmentAgency, uraniumRequiredToUpgradeRecruitmentAgency, aetherRequiredToUpgradeRecruitmentAgency, waterRequiredToUpgradeRecruitmentAgency;
	private int[] metalRequiredToUpgradeWater, uraniumRequiredToUpgradeWater, aetherRequiredToUpgradeWater;
	private int[] metalRequiredToUpgradeBank, uraniumRequiredToUpgradeBank, aetherRequiredToUpgradeBank, populationRequiredToUpgradeBank, depositLimitBankPerLevel;

	@Test
	public void basicTest() {
		while(nr!=0){
			smallTest("metal");
			smallTest("uranium");
			smallTest("aether");
			smallTest("water");
			smallTest("storage");
			smallTest("storageLevel");
			smallTest("population");
			smallTest("crystal");
			smallTest("numberOfPlanets");
			smallTest("populationUsed");
			smallTest("money");
			smallTest("Nothing");
			ps.setPoints(nr);
			assertEquals(nr, ps.getPoints());
			nr--;
		}
	}
	
	@Test
	public void checkAddingRemovingResources(){
		counter = 0;
		for(int i=0;i<nr;i++){
			setARResourcesValues(); // AR comes from AddingRemoving
			ps.addResource(new Resources(name,level,seed,initialValue));
		}
		resources = ps.getResources();
		assertEquals(resources.size(), nr);
		for(int i=0;i<resources.size();i++){
			if(randomGenerator.nextBoolean() && randomGenerator.nextInt(100000) > 60000){
				ps.removeResource(resources.get(i));
				counter++;
			}
		}
		resources = ps.getResources();
		assertEquals(resources.size(), (nr-counter));
	}
	
	@Test
	public void checkCrystalAvailabilityTest(){
		counter = 0;
		
		//Adding resources
		ps.addResource(metal);
		ps.addResource(uranium);
		ps.addResource(aether);
		ps.addResource(water);
		ps.addResource(storage);
		ps.addResource(population);
		ps.addResource(money);
		ps.addResource(ship1);
		ps.addResource(ship2);
		ps.addResource(ship3);
		
		// Setting the level of each resource
		metal.setLevel(20);
		uranium.setLevel(20);
		aether.setLevel(20);
		water.setLevel(20);
		storage.setLevel(20);
		population.setLevel(20);
		money.setLevel(20);
		
		resources = ps.getResources();
		for(int i=0;i<resources.size();i++){
			if(resources.get(i).getLevel() == 20)
				counter++;
		}
		// Check if you receive only one time a crystal when you have all the buildings at level 20
		ps.checkCrystalAvailability();
		assertEquals(ps.getCrystal(), 1);
		ps.checkCrystalAvailability();
		assertEquals(ps.getCrystal(), 1);
		metal.setLevel(15);
		ps.checkCrystalAvailability();
		assertEquals(ps.getCrystal(), 1);
		
		//Check if the crystal is received only one time
		ps.setNumberOfPlanets(3);
		ps.checkCrystalAvailability();
		assertEquals(ps.getCrystal(), 2);
		ps.checkCrystalAvailability();
		assertEquals(ps.getCrystal(), 2);
		
		//Reset the crystal to 1 and check if you won't receive another one in this situation
		ps.setCrystal(1);
		ps.setNumberOfPlanets(2);
		ps.checkCrystalAvailability();
		assertEquals(ps.getCrystal(), 1);
		ps.checkCrystalAvailability();
		assertEquals(ps.getCrystal(), 1);
		
		//Modifying the level of each resource
		uranium.setLevel(randomGenerator.nextInt(19));
		aether.setLevel(randomGenerator.nextInt(19));
		water.setLevel(randomGenerator.nextInt(19));
		storage.setLevel(randomGenerator.nextInt(19));
		population.setLevel(randomGenerator.nextInt(19));
		money.setLevel(randomGenerator.nextInt(19));
		ps.checkCrystalAvailability();
		
		ps.setNumberOfPlanets(5);
		ps.checkCrystalAvailability();
		ps.checkCrystalAvailability();
	}
	
	@Test
	public void UpdatesTest(){
		resources = ps.getResources();
		assertEquals(resources.size(), 0);
		resources = ps.getResources();
		assertEquals(resources.size(), 0);
		ps.addResource(metal);
		ps.addResource(uranium);
		ps.addResource(aether);
		ps.addResource(water);
		ps.addResource(storage);
		ps.addResource(population);
		ps.addResource(money);
		ps.addResource(ship1);
		ps.addResource(ship2);
		ps.addResource(ship3);
		nr = randomGenerator.nextInt(20);
		assertEquals(ps.getNumberOfShipsType1(), 0);
		assertEquals(ps.getNumbersOfShipsType2(), 0);
		assertEquals(ps.getNumbersOfShipsType3(), 0);
	
		// Storage Update Testing
		storage.setLevel(20);
		storage.upgradeResources(20);
		ps.updateStorage();
		assertEquals(storage.getAmountPerHour(),ps.getStorage());
		ps.updateStorage();
		assertEquals(storage.getAmountPerHour(),ps.getStorage());
		storage.setLevel(10);
		storage.upgradeResources(10);
		ps.updateStorage();
		assertEquals(storage.getAmountPerHour(),ps.getStorage());
		ps.setStorage(1230);
		storage.setLevel(2);
		storage.upgradeResources(2);
		ps.updateStorage();
		
		// Recruitment Agency Update Testing
		ps.updateRecruitmentAgency();
		assertEquals(population.getAmountPerHour(),ps.getPopulation());
		ps.updateRecruitmentAgency();
		assertEquals(population.getAmountPerHour(),ps.getPopulation());
		ps.setStorageAllPlanets(0);
		ps.updateRecruitmentAgency();
		ps.setStorageAllPlanets(1);
		ps.updateRecruitmentAgency();
		population.setLevel(5);
		population.upgradeResources(5);
		ps.updateRecruitmentAgency();
		assertEquals(population.getAmountPerHour(),ps.getPopulation());
		
		// Update Method Testing
		long time = System.currentTimeMillis();
		long currentTime = time;
		while(currentTime - time < 750){
			ps.update();
			ps.updateStatics();
			currentTime = System.currentTimeMillis();
		}
		ps.setNumberOfPlanets(5);
		time = System.currentTimeMillis();
		currentTime = time;
		// Resseting the values for the second test
		ps.setMetal(0);
		ps.setUranium(0);
		ps.setAether(0);
		ps.setWater(0);
		ps.setMoney(0);
		while(currentTime - time < 750){
			ps.update();
			ps.updateStatics();
			currentTime = System.currentTimeMillis();
		}
		
	}
	
	@Test
	public void upgradeTest(){
		for(int i=0;i<20;i++){
			// Metal Upgrade Testing
			assertEquals(ps.getMetalRequiredToUpgradeMetal(i), metalRequiredToUpgradeMetal[i]);
			assertEquals(ps.getUraniumRequiredToUpgradeMetal(i), uraniumRequiredToUpgradeMetal[i]);
			assertEquals(ps.getAetherRequiredToUpgradeMetal(i), aetherRequiredToUpgradeMetal[i]);
			assertEquals(ps.getPopulationRequiredToUpgradeMetal(i), populationRequiredToUpgradeMetal[i]);
			
			// Uranium Upgrade Testing
			assertEquals(ps.getMetalRequiredToUpgradeUranium(i), metalRequiredToUpgradeUranium[i]);
			assertEquals(ps.getUraniumRequiredToUpgradeUranium(i), uraniumRequiredToUpgradeUranium[i]);
			assertEquals(ps.getAetherRequiredToUpgradeUranium(i), aetherRequiredToUpgradeUranium[i]);
			assertEquals(ps.getPopulationRequiredToUpgradeUranium(i), populationRequiredToUpgradeUranium[i]);
			
			// Aether Upgrade Testing
			assertEquals(ps.getMetalRequiredToUpgradeAether(i), metalRequiredToUpgradeAether[i]);
			assertEquals(ps.getUraniumRequiredToUpgradeAether(i), uraniumRequiredToUpgradeAether[i]);
			assertEquals(ps.getAetherRequiredToUpgradeAether(i), aetherRequiredToUpgradeAether[i]);
			assertEquals(ps.getPopulationRequiredToUpgradeAether(i), populationRequiredToUpgradeAether[i]);
			
			// Storage Upgrade Testing
			assertEquals(ps.getMetalRequiredToUpgradeStorage(i), metalRequiredToUpgradeStorage[i]);
			assertEquals(ps.getUraniumRequiredToUpgradeStorage(i), uraniumRequiredToUpgradeStorage[i]);
			assertEquals(ps.getAetherRequiredToUpgradeStorage(i), aetherRequiredToUpgradeStorage[i]);
			assertEquals(ps.getPopulationRequiredToUpgradeStorage(i), populationRequiredToUpgradeStorage[i]);
			
			// Population Upgrade Testing
			assertEquals(ps.getMetalRequiredToUpgradeRecruitmentAgency(i), metalRequiredToUpgradeRecruitmentAgency[i]);
			assertEquals(ps.getUraniumRequiredToUpgradeRecruitmentAgency(i), uraniumRequiredToUpgradeRecruitmentAgency[i]);
			assertEquals(ps.getAetherRequiredToUpgradeRecruitmentAgency(i), aetherRequiredToUpgradeRecruitmentAgency[i]);
			assertEquals(ps.getWaterRequiredToUpgradeRecruitmentAgency(i), waterRequiredToUpgradeRecruitmentAgency[i]);
			
			// Water Upgrade Testing
			assertEquals(ps.getMetalRequiredToUpgradeWater(i), metalRequiredToUpgradeWater[i]);
			assertEquals(ps.getUraniumRequiredToUpgradeWater(i), uraniumRequiredToUpgradeWater[i]);
			assertEquals(ps.getAetherRequiredToUpgradeWater(i), aetherRequiredToUpgradeWater[i]);
			
			// Bank Upgrade Testing
			assertEquals(ps.getMetalRequiredToUpgradeBank(i), metalRequiredToUpgradeBank[i]);
			assertEquals(ps.getUraniumRequiredToUpgradeBank(i), uraniumRequiredToUpgradeBank[i]);
			assertEquals(ps.getAetherRequiredToUpgradeBank(i), aetherRequiredToUpgradeBank[i]);
			assertEquals(ps.getPopulationRequiredToUpgradeBank(i), populationRequiredToUpgradeBank[i]);
			
			// Ship1 Increase Testing
			assertEquals(ps.getMetalRequiredToUpgradeShip1(), 10);
			assertEquals(ps.getUraniumRequiredToUpgradeShip1(), 10);
			assertEquals(ps.getAetherRequiredToUpgradeShip1(), 10);
			assertEquals(ps.getPopulationRequiredToUpgradeShip1(), 10);
			
			// Ship2 Increase Testing
			assertEquals(ps.getMetalRequiredToUpgradeShip2(), 12);
			assertEquals(ps.getUraniumRequiredToUpgradeShip2(), 12);
			assertEquals(ps.getAetherRequiredToUpgradeShip2(), 12);
			assertEquals(ps.getPopulationRequiredToUpgradeShip2(), 12);
			
			// Ship3 Increase Testing
			assertEquals(ps.getMetalRequiredToUpgradeShip3(), 14);
			assertEquals(ps.getUraniumRequiredToUpgradeShip3(), 14);
			assertEquals(ps.getAetherRequiredToUpgradeShip3(), 14);
			assertEquals(ps.getPopulationRequiredToUpgradeShip3(), 14);
			
		}
	}
	
	@Test
	public void verifyAvailableResourcesTest(){
		ps.addResource(metal);
		ps.addResource(uranium);
		ps.addResource(aether);
		ps.addResource(water);
		ps.addResource(storage);
		ps.addResource(population);
		ps.addResource(money);
		ps.addResource(ship1);
		ps.addResource(ship2);
		ps.addResource(ship3);
		
		resources = ps.getResources();
		nr = 10000;
		while(nr != 0){
			for(int i=0;i<20;i++){
				ps.verifyAvailableResources("metal", i, seed);
				ps.verifyAvailableResources("uranium", i, seed);
				ps.verifyAvailableResources("aether", i, seed);
				ps.verifyAvailableResources("water", i, seed);
				ps.verifyAvailableResources("storage", i, seed);
				ps.verifyAvailableResources("population", i, seed);
				ps.verifyAvailableResources("money", i, seed);
				ps.verifyAvailableResources("ship1", i, seed);
				ps.verifyAvailableResources("ship2", i, seed);
				ps.verifyAvailableResources("ship3", i, seed);
				ps.update();
				ps.setPopulation(ps.getPopulation() + 10);
				assertEquals(ps.getMetal(), ps.getResourceOnPlanet(PlayerStats.METALID, seed));
				assertEquals(ps.getUranium(), ps.getResourceOnPlanet(PlayerStats.URANIUMID, seed));
				assertEquals(ps.getAether(), ps.getResourceOnPlanet(PlayerStats.AETHERID, seed));
			}
			nr--;
		}
		
		// Setting the values for the next test
		ps.verifyAvailableResources("Nothing", randomGenerator.nextInt(19), seed);
		nr = 10000;
		ps.setNumberOfPlanets(7);
		
		// Reset Initial Values
		ps.setMetal(1000);
		ps.setUranium(1000);
		ps.setAether(1000);
		ps.setWater(1000);
		ps.setPopulation(1000);
		ps.setPopulationUsed(0);
		ps.verifyAvailableResources("ship1", randomGenerator.nextInt(19), seed);
		ps.verifyAvailableResources("ship2", randomGenerator.nextInt(19), seed);
		ps.verifyAvailableResources("ship3", randomGenerator.nextInt(19), seed);
		ps.verifyAvailableResources("Nothing", randomGenerator.nextInt(19), seed);
		while(nr != 0){
			for(int i=0;i<20;i++){
				ps.verifyAvailableResources("metal", i, seed);
				ps.verifyAvailableResources("uranium", i, seed);
				ps.verifyAvailableResources("aether", i, seed);
				ps.verifyAvailableResources("water", i, seed);
				ps.verifyAvailableResources("storage", i, seed);
				ps.verifyAvailableResources("population", i, seed);
				ps.verifyAvailableResources("money", i, seed);
				ps.verifyAvailableResources("ship1", i, seed);
				ps.verifyAvailableResources("ship2", i, seed);
				ps.verifyAvailableResources("ship3", i, seed);
				ps.update();
				ps.setPopulation(ps.getPopulation() + 10);
				assertEquals(ps.getMetal(), ps.getResourceOnPlanet(PlayerStats.METALID, seed));
				assertEquals(ps.getUranium(), ps.getResourceOnPlanet(PlayerStats.URANIUMID, seed));
				assertEquals(ps.getAether(), ps.getResourceOnPlanet(PlayerStats.AETHERID, seed));
			}
			nr--;
		}
	}
	
	@Test
	public void getResourcesTest(){
		ps.addResource(metal);
		assertEquals(ps.getResource("metal", seed), metal);
		
		ps.addResource(aether);
		assertEquals(ps.getResource("aether", seed), aether);
		
		ps.addResource(uranium);
		assertEquals(ps.getResource("URANIUM", seed), null);
	}
	
	@Test
	public void resourcesTest(){
		for(int i=0; i<=20; i++){
			money.upgradeResources(i);
			water.upgradeResources(i);
			population.upgradeResources(i);
			storage.upgradeResources(i);
			metal.upgradeResources(i);
			uranium.upgradeResources(i);
			aether.upgradeResources(i);
		}
	}
	
	@Test
	public void finalTests(){
		ps.addResource(metal);
		ps.addResource(uranium);
		ps.addResource(aether);
		ps.addResource(water);
		ps.addResource(storage);
		ps.addResource(population);
		ps.addResource(money);
		ps.addResource(ship1);
		ps.addResource(ship2);
		ps.addResource(ship3);
		ps.addResource(new Resources("metal",3,39204, PlayerStats.METAL));
		ps.setResourceOnPlanet(PlayerStats.METALID, 39204, 1500);
		assertEquals(ps.getResourceOnPlanet(PlayerStats.METALID, 39204), 1500);
		
		// Deposit Limit Test
		assertEquals(ps.getDepositLimit(0), 2500);
		assertEquals(ps.getDepositLimit(2), 8000);
		
		// Other Tests remained in PlayerStats
		ps.setNumberOfShipsType1(5);
		assertEquals(ps.getNumberOfShipsType1(), 5);
		
		int[] playerShips;
		playerShips = ps.getPlayerShips();
		assertEquals(playerShips[0], 0);
		
		while(nr!=0){
			assertEquals(ps.getAmountNecessaryForTrading(nr, nr-1), 80*nr - 40);
			ps.getAmountToPayDebt(nr, nr, nr-1);
			nr--;
		}
		ps.getAmountToPayDebt(-200, -1500, -200);
		ps.getAmountToPayDebt(0, 0, 0);
		
	}
	
	@Before
	public void setUp(){
		ps = new PlayerStats(PlayerStats.MONEY, PlayerStats.METAL, PlayerStats.URANIUM, PlayerStats.AETHER,
				PlayerStats.WATER, PlayerStats.STORAGE, PlayerStats.POPULATION, PlayerStats.SHIP1, PlayerStats.CRYSTAL,
				PlayerStats.POINTS);
		randomGenerator = new Random();
		resources = new ArrayList<>();
		nr = randomGenerator.nextInt(100000);
		metal = new Resources("metal", 1, seed, PlayerStats.METAL);
		uranium = new Resources("uranium", 1, seed, PlayerStats.URANIUM);
		aether = new Resources("aether", 1, seed, PlayerStats.AETHER);
		water = new Resources("water", 1, seed, PlayerStats.WATER);
		storage = new Resources("storage", 1, seed, PlayerStats.STORAGE);
		population = new Resources("population", 1, seed, PlayerStats.POPULATION);
		money = new Resources("money", 1, seed, PlayerStats.MONEY);
		ship1 = new Resources("ship1", 1, seed, 0);
		ship2 = new Resources("ship2", 1, seed, 0);
		ship3 = new Resources("ship3", 1, seed, 0);
		initializeUpgradeValues();
		
	}

	
	private void setRValue(){
		rValue = randomGenerator.nextInt(1000000);
	}
	
	private void smallTest(String test){
		switch(test){
		case "metal":
			setRValue();
			ps.setMetal(rValue);
			assertEquals(ps.getMetal(), rValue);
			break;
		case "uranium":
			setRValue();
			ps.setUranium(rValue);;
			assertEquals(ps.getUranium(), rValue);
			break;
		case "aether":
			setRValue();
			ps.setAether(rValue);
			assertEquals(ps.getAether(), rValue);
			break;
		case "water":
			setRValue();
			ps.setWater(rValue);
			assertEquals(ps.getWater(), rValue);
			break;
		case "storage":
			setRValue();
			ps.setStorage(rValue);
			assertEquals(ps.getStorage(), rValue);
			break;
		case "storageLevel":
			setRValue();
			ps.setStorageLevel(rValue);
			assertEquals(ps.getStorageLevel(), rValue);
			break;
		case "population":
			setRValue();
			ps.setPopulation(rValue);
			assertEquals(ps.getPopulation(), rValue);
			break;
		case "crystal":
			setRValue();
			ps.setCrystal(rValue);
			assertEquals(ps.getCrystal(), rValue);
			break;
		case "numberOfPlanets":
			setRValue();
			ps.setNumberOfPlanets(rValue);
			assertEquals(ps.getNumberOfPlanets(), rValue);
			break;
		case "populationUsed":
			setRValue();
			ps.setPopulationUsed(rValue);
			assertEquals(ps.getPopulationUsed(), rValue);
			assertEquals(ps.getActivePopulation(), ps.getPopulation() - ps.getPopulationUsed());
			break;
		case "money":
			setRValue();
			ps.setMoney(rValue);
			assertEquals(ps.getMoney(), rValue);
			break;
		default:
		}
	}
	
	private void setARResourcesValues(){
		name = NameGenerator.generateName(3);
		level = randomGenerator.nextInt(21);
		seed = randomGenerator.nextInt(100000000);
		initialValue = randomGenerator.nextInt(50000);
	}
	
	private void initializeUpgradeValues(){
		metalRequiredToUpgradeMetal = new int[] {50,63,78,98,122,153,191,238,298,373,466,582,728,909,1137,1421,1776,2220,2776,3469};
		uraniumRequiredToUpgradeMetal = new int[] {60,77,98,124,159,202,258,329,419,534,681,868,1107,1412,1800,2295,2926,3731,4757,6065};
		aetherRequiredToUpgradeMetal = new int[] {40,50,62,77,96,120,149,185,231,287,358,446,555,691,860,1071,1333,1659,2066,2572};
		populationRequiredToUpgradeMetal = new int[] {5,5,1,1,1,1,2,2,2,2,3,3,4,5,5,5,7,8,9,10};
		
		metalRequiredToUpgradeUranium = new int[] {65,83,105,133,169,215,273,346,440,559,709,901,1144,1453,1846,2344,2977,3781,4802,6098};
		uraniumRequiredToUpgradeUranium = new int[] {50,63,80,101,128,162,205,256,328,415,525,664,840,1062,1343,1700,2150,2720,3440,4352};
		aetherRequiredToUpgradeUranium = new int[] {40,50,62,76,95,117,145,180,224,277,344,426,529,655,813,1008,1250,1550,1922,2383};
		populationRequiredToUpgradeUranium = new int[] {10,10,1,2,2,2,3,3,4,4,4,5,6,7,8,8,10,12,13,15};
		
		metalRequiredToUpgradeAether = new int[] {75,94,118,147,184,231,289,362,453,567,710,889,1113,1393,1744,2183,2734,3422,4285,5365};
		uraniumRequiredToUpgradeAether = new int[] {65,83,106,135,172,219,279,356,454,579,738,941,1200,1529,1950,2486,3170,4042,5153,6571};
		aetherRequiredToUpgradeAether = new int[] {70,87,108,133,165,205,254,316,391,485,602,746,925,1147,1422,1764,2187,2712,3363,4170};
		populationRequiredToUpgradeAether = new int[] {10,10,2,2,3,3,4,4,5,6,7,8,10,11,13,15,18,21,25,28};
		
		metalRequiredToUpgradeStorage = new int[] {60,76,96,121,154,194,246,311,393,498,630,796,1007,1274,1612,2039,2580,3264,4128,5222}; 
		uraniumRequiredToUpgradeStorage = new int[] {50,64,81,102,130,165,210,266,338,430,546,693,880,1118,1420,1803,2290,2908,3693,4691}; 
		aetherRequiredToUpgradeStorage = new int[] {40,50,62,77,96,120,149,185,231,287,358,446,555,691,860,1071,1333,1659,2066,2572};
		populationRequiredToUpgradeStorage = new int[] {1,1,1,2,2,2,2,3,3,3,3,4,4,4,4,4,4,5,5,5};
		
		metalRequiredToUpgradeRecruitmentAgency = new int[] {45,59,76,99,129,167,217,282,367,477,620,806,1048,1363,1772,2303,2994,3893,5060,6579};
		uraniumRequiredToUpgradeRecruitmentAgency = new int[] {40,53,70,92,121,160,212,279,369,487,642,848,1119,1477,1950,2574,3398,4486,5921,7816};
		aetherRequiredToUpgradeRecruitmentAgency = new int[] {30,39,50,64,83,107,138,178,230,297,383,494,637,822,1060,1368,1764,2276,2936,3787}; 
		waterRequiredToUpgradeRecruitmentAgency = new int[] {65,83,106,135,172,219,279,356,454,579,738,941,1200,1529,1950,2486,3170,4042,5153,6571}; 
		
		metalRequiredToUpgradeWater = new int[] {65,83,105,133,169,215,273,346,440,559,709,901,1144,1453,1846,2344,2977,3781,4802,6098}; 
		uraniumRequiredToUpgradeWater = new int[] {65,83,106,135,172,219,279,356,454,579,738,941,1200,1529,1950,2486,3170,4042,5153,6571}; 
		aetherRequiredToUpgradeWater = new int[] {70,87,108,133,165,205,254,316,391,485,602,746,925,1147,1422,1764,2187,2712,3363,4170}; 
		
		metalRequiredToUpgradeBank = new int[] {75,94,118,147,184,231,289,362,453,567,710,889,1113,1393,1744,2183,2734,3422,4285,5365};
		uraniumRequiredToUpgradeBank = new int[] {50,64,81,102,130,165,210,266,338,430,546,693,880,1118,1420,1803,2290,2908,3693,4691};  
		aetherRequiredToUpgradeBank = new int[] {65,83,106,135,172,219,279,356,454,579,738,941,1200,1529,1950,2486,3170,4042,5153,6571};
		populationRequiredToUpgradeBank = new int[] {10,10,2,2,3,3,4,4,5,6,7,8,10,11,13,15,18,21,25,28}; 
	}

}
