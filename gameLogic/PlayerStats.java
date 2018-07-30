package gameLogic;

import java.util.ArrayList;
import java.util.Arrays;

import galaxy.Galaxy;
import main.Main;
import messaging.PopUp;

public class PlayerStats {
	
	//The resources that a player has in total
	private long money;
	private long metal;
	private long uranium;
	private long aether;
	private long water;
	private long storage;
	private long population;
	private long crystal;
	private long points;
	
	//Amount required to upgrade Ship1, Ship2 and Ship3
	private int metalRequiredToUpgradeShip1 = 10, uraniumRequiredToUpgradeShip1 = 10, aetherRequiredToUpgradeShip1 = 10, populationRequiredToUpgradeShip1 = 10;
	private int metalRequiredToUpgradeShip2 = 12, uraniumRequiredToUpgradeShip2 = 12, aetherRequiredToUpgradeShip2 = 12, populationRequiredToUpgradeShip2 = 12;
	private int metalRequiredToUpgradeShip3 = 14, uraniumRequiredToUpgradeShip3 = 14, aetherRequiredToUpgradeShip3 = 14, populationRequiredToUpgradeShip3 = 14;
	
	//Initial storage level
	private int storageLevel = 1;
	
	//Variables used for keeping track of Storage, Population and Deposit Limit
	private int lastNumberOfStorage = 0,storageAllPlanets = 0, lastPopulation = 0, sumOfPopulationUsed = 0, totalDepositLimit = 0;
	
	//Number of Ships and Planets initially
	private int numbersOfShipsType1 = 0, numbersOfShipsType2 = 0, numbersOfShipsType3 = 0;
	private int  numberOfPlanets = 0;
	
	//Some booleans to check if a player received Crystal for something that he has done or to check if a type of ship has been already added
	private boolean checkedForCrystal1 = false, checkForCrystal2 = false, shipCounted = true, shipType2Counted = true, shipType3Counted = true;
	private boolean message1 = false, message2 = false;
	
	//Amounts required to upgrade Resources on Planet: Metal, Uranium, Aether, Storage, RecruitmentAgency(Population), Water, Bank
	private int[] metalRequiredToUpgradeMetal, uraniumRequiredToUpgradeMetal, aetherRequiredToUpgradeMetal, populationRequiredToUpgradeMetal;
	private int[] metalRequiredToUpgradeUranium, uraniumRequiredToUpgradeUranium, aetherRequiredToUpgradeUranium, populationRequiredToUpgradeUranium;
	private int[] metalRequiredToUpgradeAether, uraniumRequiredToUpgradeAether, aetherRequiredToUpgradeAether, populationRequiredToUpgradeAether;
	private int[] metalRequiredToUpgradeStorage, uraniumRequiredToUpgradeStorage, aetherRequiredToUpgradeStorage, populationRequiredToUpgradeStorage;
	private int[] metalRequiredToUpgradeRecruitmentAgency, uraniumRequiredToUpgradeRecruitmentAgency, aetherRequiredToUpgradeRecruitmentAgency, waterRequiredToUpgradeRecruitmentAgency;
	private int[] metalRequiredToUpgradeWater, uraniumRequiredToUpgradeWater, aetherRequiredToUpgradeWater;
	private int[] metalRequiredToUpgradeBank, uraniumRequiredToUpgradeBank, aetherRequiredToUpgradeBank, populationRequiredToUpgradeBank, depositLimitBankPerLevel;
	
	//Array used to keep track of playerShips
	private int[] playerShips;
	
	//An ArrayList of Resources which keep track of each resource a player has in order to easily upgrade the total amount
	private ArrayList<Resources> resources;
	
	//An ArrayList of long which is used to familiarizeSeed ( make it work in PlayerStats ) received from PlanetGenerator
	private ArrayList<Long> familiarizeSeed;
	
	//A [][] Array used to keep track of resource on each planet
	private long[][] resourcePerPlanet;
	
	//Values used to get the resource on planet, each has it's own ID and a special/unique seed ( received from PlanetGenerator and converted to work in PlayerStats)
	//Those Values and the Seed can be used from any other classes ( Galaxy, PlanetGenerator, Planet, etc ) in order to get the resource on that planet
	public static final int METALID = 0, URANIUMID = 1, AETHERID = 2, WATERID = 3, STORAGEID = 4, POPULATIONID = 5, SHIPTYPE1ID = 6, SHIPTYPE2ID = 7, SHIPTYPE3ID = 8;
	
	// Initial amount that a player is receiving in the beginning ( those are set in Main )
	public static final int MONEY = 90;
	public static final int METAL = 45;
	public static final int URANIUM = 45;
	public static final int AETHER = 45;
	public static final int WATER = 53;
	public static final int STORAGE = 1000;
	public static final int POPULATION = 240;
	public static final int SHIP1 = 0;
	public static final int CRYSTAL = 0;
	public static final int POINTS = 498;
	
	//Number of maximum planets/player, maximum resources/planet and maximum number of ships/ planet
	private static final int TOTAL_PLANETS = 1500;
	private static final int TOTAL_RESOURCES = 9;
	private static final int NUMBER_OF_TYPE_OF_SHIPS = 6;
	
	public PlayerStats(long _money,long _metal,long _uranium,long _aether,long _water,long _storage, long _population, int _numbersOfShipsType1, int _crystal, long _points){
		this.money = _money;
		this.metal = _metal;
		this.uranium = _uranium;
		this.aether = _aether;
		this.water = _water;
		this.storage = _storage;
		this.population = _population;
		this.numbersOfShipsType1 = _numbersOfShipsType1;
		this.crystal = _crystal;
		this.setPoints(_points);
		this.resources = new ArrayList<>();
		this.playerShips = new int[NUMBER_OF_TYPE_OF_SHIPS];
		
		// Initializing the values which are going to be used in this class
		initializeUpgradeValues();
		initializeDepositLimits();
		initializeResourcePerPlanet();
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
	
	private void initializeDepositLimits(){
		depositLimitBankPerLevel = new int[] {2500, 5000, 8000, 11000, 15000, 19000, 25000, 32000, 40000, 50000, 75000, 100000, 125000, 150000, 200000, 275000, 500000, 750000, 1000000, 2000000};
	}
	
	private void initializeResourcePerPlanet(){
		resourcePerPlanet = new long[TOTAL_PLANETS][TOTAL_RESOURCES];
		familiarizeSeed = new ArrayList<>();
		resourcePerPlanet[0][METALID] = 45;
		resourcePerPlanet[0][URANIUMID] = 45;
		resourcePerPlanet[0][AETHERID] = 45;
		resourcePerPlanet[0][WATERID] = 53;
		resourcePerPlanet[0][STORAGEID] = 1000;
		resourcePerPlanet[0][POPULATIONID] = 240;
		resourcePerPlanet[0][SHIPTYPE1ID] = 0;
		resourcePerPlanet[0][SHIPTYPE2ID] = 0;
		resourcePerPlanet[0][SHIPTYPE3ID] = 0;
	}
	
	/**
	 * Get the amount of Money a player has
	 * @return the amount of money a player has (long)
	 */
	public long getMoney(){
		return this.money;
	}
	
	/**
	 * Get the amount of Metal a player has
	 * @return the amount of metal a player has (long)
	 */
	public long getMetal(){
		return this.metal;
	}
	
	/**
	 * Get the amount of Uranium a player has
	 * @return the amount of Uranium a player has (long)
	 */
	public long getUranium(){
		return this.uranium;
	}
	
	/**
	 * Get the amount of Aether a player has
	 * @return the amount of Aether a player has (long)
	 */
	public long getAether(){
		return this.aether;
	}
	
	/**
	 * Get the amount of Water a player has
	 * @return the amount of Water a player has
	 */
	public long getWater(){
		return this.water;
	}
	
	/**
	 * Get the amount of Storage a player has available
	 * @return the amount of Storage a player has available
	 */
	public long getStorage(){
		return this.storage;
	}
	
	/**
	 * Get the level of the Storage
	 * @return the level of the Storage
	 */
	public int getStorageLevel(){
		return storageLevel;
	}
	
	/**
	 * Get the population a player has
	 * @return the number of population a player has
	 */
	public long getPopulation(){
		return this.population;
	}
	
	/**
	 * Get the population that the player still have 
	 * @return the population that the player still have
	 */
	public long getActivePopulation(){
		return (this.population - sumOfPopulationUsed);
	}
	
	public int getPopulationUsed(){
		return this.sumOfPopulationUsed;
	}
	
	/**
	 * Get the amount of metal required to upgrade metal to the next level
	 * @param level
	 * @return the metal required to upgrade metal for a certain level
	 */
	public int getMetalRequiredToUpgradeMetal(int level){
		return metalRequiredToUpgradeMetal[level];
	}
	
	/**
	 * Get the amount of uranium required to upgrade metal to the next level
	 * @param level 
	 * @return the uranium required to upgrade metal for a certain level
	 */
	public int getUraniumRequiredToUpgradeMetal(int level){
		return uraniumRequiredToUpgradeMetal[level];
	}
	
	/**
	 * Get the amount of aether required to upgrade metal to the next level
	 * @param level 
	 * @return the aether required to upgrade metal for a certain level
	 */
	public int getAetherRequiredToUpgradeMetal(int level){
		return aetherRequiredToUpgradeMetal[level];
	}
	
	/**
	 * Get the amount of population required to upgrade metal to the next level
	 * @param level 
	 * @return the population required to upgrade metal for a certain level
	 */
	public int getPopulationRequiredToUpgradeMetal(int level){
		return populationRequiredToUpgradeMetal[level];
	}
	
	/**
	 * Get the amount of metal required to upgrade uranium to the next level
	 * @param level 
	 * @return the metal required to upgrade uranium for a certain level
	 */
	public int getMetalRequiredToUpgradeUranium(int level){
		return metalRequiredToUpgradeUranium[level];
	}
	
	/**
	 * Get the amount of uranium required to upgrade uranium to the next level
	 * @param level 
	 * @return the uranium required to upgrade uranium for a certain level
	 */
	public int getUraniumRequiredToUpgradeUranium(int level){
		return uraniumRequiredToUpgradeUranium[level];
	}
	
	/**
	 * Get the amount of aether required to upgrade uranium to the next level
	 * @param level 
	 * @return the aether required to upgrade uranium for a certain level
	 */
	public int getAetherRequiredToUpgradeUranium(int level){
		return aetherRequiredToUpgradeUranium[level];
	}
	
	/**
	 * Get the amount of population required to upgrade uranium to the next level
	 * @param level 
	 * @return the population required to upgrade uranium for a certain level
	 */
	public int getPopulationRequiredToUpgradeUranium(int level){
		return populationRequiredToUpgradeUranium[level];
	}
	
	/**
	 * Get the amount of metal required to upgrade aether to the next level
	 * @param level 
	 * @return the metal required to upgrade aether for a certain level
	 */
	public int getMetalRequiredToUpgradeAether(int level){
		return metalRequiredToUpgradeAether[level];
	}
	
	/**
	 * Get the amount of uranium required to upgrade aether to the next level
	 * @param level 
	 * @return the uranium required to upgrade aether for a certain level
	 */
	public int getUraniumRequiredToUpgradeAether(int level){
		return uraniumRequiredToUpgradeAether[level];
	}
	
	/**
	 * Get the amount of aether required to upgrade aether to the next level
	 * @param level 
	 * @return the aether required to upgrade aether for a certain level
	 */
	public int getAetherRequiredToUpgradeAether(int level){
		return aetherRequiredToUpgradeAether[level];
	}
	
	/**
	 * Get the amount of population required to upgrade aether to the next level
	 * @param level 
	 * @return the population required to upgrade aether for a certain level
	 */
	public int getPopulationRequiredToUpgradeAether(int level){
		return populationRequiredToUpgradeAether[level];
	}
	
	/**
	 * Get the amount of metal required to upgrade storage to the next level
	 * @param level 
	 * @return the metal required to upgrade storage for a certain level
	 */
	public int getMetalRequiredToUpgradeStorage(int level){
		return metalRequiredToUpgradeStorage[level];
	}
	
	/**
	 * Get the amount of uranium required to upgrade storage to the next level
	 * @param level 
	 * @return the uranium required to upgrade storage for a certain level
	 */
	public int getUraniumRequiredToUpgradeStorage(int level){
		return uraniumRequiredToUpgradeStorage[level];
	}
	
	/**
	 * Get the amount of aether required to upgrade storage to the next level
	 * @param level 
	 * @return the aether required to upgrade storage for a certain level
	 */
	public int getAetherRequiredToUpgradeStorage(int level){
		return aetherRequiredToUpgradeStorage[level];
	}
	
	/**
	 * Get the amount of population required to upgrade storage to the next level
	 * @param level 
	 * @return the population required to upgrade storage for a certain level
	 */
	public int getPopulationRequiredToUpgradeStorage(int level){
		return populationRequiredToUpgradeStorage[level];
	}
	
	/**
	 * Get the amount of metal required to upgrade recruitment agency to the next level
	 * @param level 
	 * @return the metal required to upgrade recruitment agency for a certain level
	 */
	public int getMetalRequiredToUpgradeRecruitmentAgency(int level){
		return metalRequiredToUpgradeRecruitmentAgency[level];
	}
	
	/**
	 * Get the amount of uranium required to upgrade recruitment agency to the next level
	 * @param level 
	 * @return the uranium required to upgrade recruitment agency for a certain level
	 */
	public int getUraniumRequiredToUpgradeRecruitmentAgency(int level){
		return uraniumRequiredToUpgradeRecruitmentAgency[level];
	}
	
	/**
	 * Get the amount of aether required to upgrade recruitment agency to the next level
	 * @param level 
	 * @return the aether required to upgrade recruitment agency for a certain level
	 */
	public int getAetherRequiredToUpgradeRecruitmentAgency(int level){
		return aetherRequiredToUpgradeRecruitmentAgency[level];
	}
	
	/**
	 * Get the amount of water required to upgrade recruitment agency to the next level
	 * @param level 
	 * @return the water required to upgrade recruitment agency for a certain level
	 */
	public int getWaterRequiredToUpgradeRecruitmentAgency(int level){
		return waterRequiredToUpgradeRecruitmentAgency[level];
	}
	
	/**
	 * Get the amount of metal required to upgrade water to the next level
	 * @param level 
	 * @return the metal required to upgrade water for a certain level
	 */
	public int getMetalRequiredToUpgradeWater(int level){
		return metalRequiredToUpgradeWater[level];
	}
	
	/**
	 * Get the amount of uranium required to upgrade water to the next level
	 * @param level 
	 * @return the uranium required to upgrade water for a certain level
	 */
	public int getUraniumRequiredToUpgradeWater(int level){
		return uraniumRequiredToUpgradeWater[level];
	}
	
	/**
	 * Get the amount of aether required to upgrade water to the next level
	 * @param level 
	 * @return the aether required to upgrade water for a certain level
	 */
	public int getAetherRequiredToUpgradeWater(int level){
		return aetherRequiredToUpgradeWater[level];
	}
	
	/**
	 * Get the amount of metal required to buy one ship type 1
	 * @return the amount of metal required to buy one ship type 1
	 */
	public int getMetalRequiredToUpgradeShip1(){
		return metalRequiredToUpgradeShip1;
	}
	
	/**
	 * Get the amount of uranium required to buy one ship type 1
	 * @return the amount of uranium required to buy one ship type 1
	 */
	public int getUraniumRequiredToUpgradeShip1(){
		return uraniumRequiredToUpgradeShip1;
	}
	
	/**
	 * Get the amount of aether required to buy one ship type 1
	 * @return the amount of aether required to buy one ship type 1
	 */
	public int getAetherRequiredToUpgradeShip1(){
		return aetherRequiredToUpgradeShip1;
	}
	
	/**
	 * Get the amount of population required to buy one ship type 1
	 * @return the amount of population required to buy one ship type 1
	 */
	public int getPopulationRequiredToUpgradeShip1(){
		return populationRequiredToUpgradeShip1;
	}
	
	/**
	 * Get the amount of metal required to buy one ship type 2
	 * @return the amount of metal required to buy one ship type 2
	 */
	public int getMetalRequiredToUpgradeShip2(){
		return metalRequiredToUpgradeShip2;
	}
	
	/**
	 * Get the amount of uranium required to buy one ship type 2
	 * @return the amount of uranium required to buy one ship type 2
	 */
	public int getUraniumRequiredToUpgradeShip2(){
		return uraniumRequiredToUpgradeShip2;
	}
	
	/**
	 * Get the amount of aether required to buy one ship type 2
	 * @return the amount of aether required to buy one ship type 2
	 */
	public int getAetherRequiredToUpgradeShip2(){
		return aetherRequiredToUpgradeShip2;
	}
	
	/**
	 * Get the amount of population required to buy one ship type 2
	 * @return the amount of population required to buy one ship type 2
	 */
	public int getPopulationRequiredToUpgradeShip2(){
		return populationRequiredToUpgradeShip2;
	}
	
	/**
	 * Get the amount of metal required to buy one ship type 3
	 * @return the amount of metal required to buy one ship type 3
	 */
	public int getMetalRequiredToUpgradeShip3(){
		return metalRequiredToUpgradeShip3;
	}
	
	/**
	 * Get the amount of uranium required to buy one ship type 3
	 * @return the amount of uranium required to buy one ship type 3
	 */
	public int getUraniumRequiredToUpgradeShip3(){
		return uraniumRequiredToUpgradeShip3;
	}
	
	/**
	 * Get the amount of aether required to buy one ship type 3
	 * @return the amount of aether required to buy one ship type 3
	 */
	public int getAetherRequiredToUpgradeShip3(){
		return aetherRequiredToUpgradeShip3;
	}
	
	/**
	 * Get the amount of population required to buy one ship type 3
	 * @return the amount of uranium required to buy one ship type 3
	 */
	public int getPopulationRequiredToUpgradeShip3(){
		return populationRequiredToUpgradeShip3;
	}
	
	/**
	 * Get the amount of metal needed to upgrade the bank at a certain level
	 * @param level
	 * @return the amount of metal needed to upgrade the bank at a certain level
	 */
	public int getMetalRequiredToUpgradeBank(int level){
		return metalRequiredToUpgradeBank[level];
	}
	
	/**
	 * Get the amount of uranium needed to upgrade the bank at a certain level
	 * @param level
	 * @return the amount of uranium needed to upgrade the bank at a certain level
	 */
	public int getUraniumRequiredToUpgradeBank(int level){
		return uraniumRequiredToUpgradeBank[level];
	}
	
	/**
	 * Get the amount of aether needed to upgrade the bank at a certain level
	 * @param level
	 * @return the amount of aether needed to upgrade the bank at a certain level
	 */
	public int getAetherRequiredToUpgradeBank(int level){
		return aetherRequiredToUpgradeBank[level];
	}
	
	/**
	 * Get the amount of population needed to upgrade the bank at a certain level
	 * @param level
	 * @return the amount of population needed to upgrade the bank at a certain level
	 */
	public int getPopulationRequiredToUpgradeBank(int level){
		return populationRequiredToUpgradeBank[level];
	}
	
	/**
	 * Get the number of ships of type 1
	 * @return the number of ships of type 1
	 */
	public int getNumberOfShipsType1(){
		return numbersOfShipsType1;
	}
	
	/**
	 * Get the number of planets
	 * @return the number of planets
	 */
	public int getNumberOfPlanets(){
		return numberOfPlanets;
	}
	
	/**
	 * Get the amount of Crystal a player has
	 * @return the amount of Crystal a player has
	 */
	public long getCrystal(){
		return this.crystal;
	}
	
	/**
	 * Set the Money for the player
	 * @param _money the amount of Money you want to set
	 */
	public void setMoney(long _money){
		this.money = _money;
	}
	
	/**
	 * Set the Metal for the player
	 * @param _metal the amount of Metal you want to set
	 */
	public void setMetal(long _metal){
		this.metal = _metal;
	}
	
	/**
	 * Set the Uranium for the player
	 * @param _uranium the amount of Uranium you want to set
	 */
	public void setUranium(long _uranium){
		this.uranium = _uranium;
	}
	
	/**
	 * Set the Aether for the player
	 * @param _aether the amount of Aether you want to set
	 */
	public void setAether(long _aether){
		this.aether = _aether;
	}
	
	/**
	 * Set the Water for the player
	 * @param _water the amount of Water you want to set
	 */
	public void setWater(long _water){
		this.water = _water;
	}
	
	/**
	 * Set the storage for the player
	 * @param _storage the amount of storage you want to set
	 */
	public void setStorage(long _storage){
		this.storage = _storage;
	}
	
	/**
	 * Set the storage level for the player
	 * @param level the level you want to set
	 */
	public void setStorageLevel(int level){
		storageLevel = level;
	}
	
	/**
	 * Set the population for the player
	 * @param _population the amount of population you want to set
	 */
	public void setPopulation(long _population){
		this.population = _population;
	}
	
	/**
	 * Set the crystal for the player
	 * @param _crystal the number of crystals you want to set
	 */
	public void setCrystal(long _crystal){
		this.crystal = _crystal;
	}
	
	/**
	 * Set the number of planets a player own
	 * @param _numberOfPlanets the number of planets a player own
	 */
	public void setNumberOfPlanets(int _numberOfPlanets){
		this.numberOfPlanets = _numberOfPlanets;
	}
	
	public void setPopulationUsed(int _sumOfPopulationUsed){
		this.sumOfPopulationUsed = _sumOfPopulationUsed;
	}
	
	/**
	 * Add a new resource to the player
	 * @param resource the resource you want to add
	 */
	public void addResource(Resources resource){
		this.resources.add(resource);
	}
	
	/**
	 * Remove the resource from the player when he lost a planet
	 * @param resource the resource you want to remove
	 */
	public void removeResource(Resources resource){
		this.resources.remove(resource);
	}
	/**
	 * Verify if a player should receive a crystal or not
	 */
	public void checkCrystalAvailability(){
		int count = 0, level;
		for(Resources resource: this.resources){
			level = resource.getLevel();
			if(resource.getType() == "metal" && level == 20){
				count++;
			}else if(resource.getType() == "uranium" && level == 20){
				count++;
			}else if(resource.getType() == "aether" && level == 20){
				count++;
			}else if(resource.getType() == "water" && level == 20){
				count++;
			}else if(resource.getType() == "population" && level == 20){
				count++;
			}else if(resource.getType() == "storage" && level == 20){
				count++;
			}else if(resource.getType() == "money" && level == 20){
				count++;
			}// END IF-ELSE STATEMENT
		}
		
		if(count==7 && !checkedForCrystal1){
			Galaxy.setPopUp(new PopUp("Congratulations!\nYou've upgraded all types of resources\navailable to level 20.\nYou received +1 crystal for that.\nUse it with wisdom!"));
			setCrystal(getCrystal() + 1);
			if(Main.USEDB){
				Main.db.dbActions("UPDATE SAccounts SET Crystal = " + getCrystal() + " WHERE Name = '" + Main.PNAME + "';");
			}
			checkedForCrystal1 = true;
		}
		
		if(getNumberOfPlanets() == 3 && !checkForCrystal2){
			Galaxy.setPopUp(new PopUp("Congratulations!\nYou have now 3 planets.\nWe want to reward you for that!\nYou received +1 crystal. You can use it to spy\nother planets fleet.\nUse it with wisdom as it's very powerfull, but\nvery rare!"));
			setCrystal(getCrystal() + 1);
			if(Main.USEDB){
				Main.db.dbActions("UPDATE SAccounts SET Crystal = " + getCrystal() + " WHERE Name = '" + Main.PNAME + "';");
			}
			checkForCrystal2 = true;
		}
		
		if(getNumberOfPlanets() == 2 && !message1){
			Galaxy.setPopUp(new PopUp("Congratulations!\nYou conquered a new planet.\nYour resources are now growing faster, but those\nare still per planet.\nChanges in that can come later, just keep conquering\nnew planets to expand and grow faster.\n"));
			message1 = true;
		}
		
		if(getNumberOfPlanets() == 5 && !message2){
			Galaxy.setPopUp(new PopUp("Congratulations!\nYou have now 5 planets.\nWe want to reward your commitment and hard\nwork.\nFrom now, you can use all your resources\nanywhere you want(on every planet).\nInstead of having resource/planet, you'll have a\ntotal amount which you can use. "));
			message2 = true;
		}
	}
	
	/**
	 * Update the number of population a player has
	 */
	public void updateRecruitmentAgency(){
		boolean verify = true;
		if(storageAllPlanets==0){
			setPopulation(getPopulation());
			storageAllPlanets = 1;
			verify = true;
		}
		int numberOfPopulation = 0, s = 0;
		for(Resources resource: this.resources){
			if(resource.getType() == "population"){
				numberOfPopulation++;
				resourcePerPlanet[modifySeed(resource.getId())][POPULATIONID] = resource.getAmountPerHour();
				s += resource.getAmountPerHour();
				verify = false;
			}
		}
		if(s != getPopulation() && !verify){
			setPopulation(s);
			if(Main.USEDB){
				Main.db.dbActions("UPDATE SAccounts SET Population = " + getActivePopulation() + " WHERE Name = '" + Main.PNAME + "';");
			}
			lastPopulation = numberOfPopulation;
		}
	}
	
	/**
	 * Update the amount of storage a player has after upgrading the level or conquering a planet
	 */
	public void updateStorage(){
		boolean verify = true;
		if(storageAllPlanets==0){
			setStorage(getStorage());
			storageAllPlanets = 1;
			verify = true;
		}
		int numberOfStorage = 0, s = 0, sDeposit = 0;
		for(Resources resource: this.resources){
			if(resource.getType() == "storage"){
				//numberOfStorage++;
				resourcePerPlanet[modifySeed(resource.getId())][STORAGEID] = resource.getAmountPerHour();
				s += resource.getAmountPerHour();
				verify = false;
			}else if(resource.getType() == "money"){
				sDeposit += depositLimitBankPerLevel[resource.getLevel() - 1];
			}
		}
		if(s != getStorage() && !verify){
			setStorage(s);
			lastNumberOfStorage = numberOfStorage;
		}
		if(sDeposit != totalDepositLimit){
			totalDepositLimit = sDeposit;
		}
	}
	
//	public void updateShips(){
//		for(Resources resource: this.resources){
//			if(resource.getType().equals("ship1")){
//				if(!shipCounted){
//					resourcePerPlanet[modifySeed(resource.getId())][SHIPTYPE1ID] += 1;
//					shipCounted = true;
//				}
//			}else if(resource.getType().equals("ship2")){
//				if(!shipType2Counted){
//					resourcePerPlanet[modifySeed(resource.getId())][SHIPTYPE2ID] += 1;
//					shipType2Counted = true;
//				}
//			}else if(resource.getType().equals("ship3")){
//				if(!shipType3Counted){
//					resourcePerPlanet[modifySeed(resource.getId())][SHIPTYPE3ID] += 1;
//					shipType3Counted = true;
//				}
//			}
//		}
//	}
	
	
	//----------------DB CODE STARTED---------------------//
	public void updateDbOnClose(){
		Main.db.dbActions("UPDATE SAccounts SET Points = " + getPoints() + " WHERE Name = '" + Main.PNAME + "';");
		for(Resources resource: this.resources){
			switch(resource.getType()){
			case "metal":
				Main.db.dbActions("UPDATE Planets SET Metal = " + resourcePerPlanet[modifySeed(resource.getId())][METALID] + " WHERE PSeed = " + resource.getId() + ";");
			case "uranium":
				Main.db.dbActions("UPDATE Planets SET Uran = " + resourcePerPlanet[modifySeed(resource.getId())][URANIUMID] + " WHERE PSeed = " + resource.getId() + ";");
			case "aether":
				Main.db.dbActions("UPDATE Planets SET Aether = " + resourcePerPlanet[modifySeed(resource.getId())][AETHERID] + " WHERE PSeed = " + resource.getId() + ";");
			case "water":
				Main.db.dbActions("UPDATE Planets SET Water = " + resourcePerPlanet[modifySeed(resource.getId())][WATERID] + " WHERE PSeed = " + resource.getId() + ";");
			case "money":
				Main.db.dbActions("UPDATE Planets SET Bank = " + getMoney() + " WHERE PSeed = " + resource.getId() + ";");
			case "storage":
				Main.db.dbActions("UPDATE Planets SET Storage = " + resourcePerPlanet[modifySeed(resource.getId())][STORAGEID] + " WHERE PSeed = " + resource.getId() + ";");
			}
		}
	}
	//----------------DB CODE ENDED-----------------------//
	
	
	/**
	 * An update method which update the amount of resources received by the player
	 */
	public void update(){
		if(getNumberOfPlanets() < 5){
			for(Resources resource: this.resources){
				switch(resource.getType()){
				case "metal": 
					if(resourcePerPlanet[modifySeed(resource.getId())][METALID] + resource.getAmountPerHour() > resourcePerPlanet[modifySeed(resource.getId())][STORAGEID]){
						long difference = (resourcePerPlanet[modifySeed(resource.getId())][STORAGEID] - resourcePerPlanet[modifySeed(resource.getId())][METALID]);
						resourcePerPlanet[modifySeed(resource.getId())][METALID] = resourcePerPlanet[modifySeed(resource.getId())][STORAGEID];
						setMetal(getMetal() + difference);
						break;
					}else{
						resourcePerPlanet[modifySeed(resource.getId())][METALID] += resource.getAmountPerHour();
						setMetal(getMetal() + resource.getAmountPerHour()); break;
					} // END OF IF-ELSE STATEMENT
				case "uranium": 
					if(resourcePerPlanet[modifySeed(resource.getId())][URANIUMID] + resource.getAmountPerHour() > resourcePerPlanet[modifySeed(resource.getId())][STORAGEID]){
						long difference = (resourcePerPlanet[modifySeed(resource.getId())][STORAGEID] - resourcePerPlanet[modifySeed(resource.getId())][URANIUMID]);
						resourcePerPlanet[modifySeed(resource.getId())][URANIUMID] = resourcePerPlanet[modifySeed(resource.getId())][STORAGEID];
						setUranium(getUranium() + difference);
						break;
					}else{
						resourcePerPlanet[modifySeed(resource.getId())][URANIUMID] += resource.getAmountPerHour();
						setUranium(getUranium() + resource.getAmountPerHour()); break;
					} // END OF IF-ELSE STATEMENT
				case "aether": 
					if(resourcePerPlanet[modifySeed(resource.getId())][AETHERID] + resource.getAmountPerHour() > resourcePerPlanet[modifySeed(resource.getId())][STORAGEID]){
						long difference = (resourcePerPlanet[modifySeed(resource.getId())][STORAGEID] - resourcePerPlanet[modifySeed(resource.getId())][AETHERID]);
						resourcePerPlanet[modifySeed(resource.getId())][AETHERID] = resourcePerPlanet[modifySeed(resource.getId())][STORAGEID];
						setAether(getAether() + difference);
						break;
					}else{
						resourcePerPlanet[modifySeed(resource.getId())][AETHERID] += resource.getAmountPerHour();
					    setAether(getAether() + resource.getAmountPerHour()); break;
					} // END OF IF-ELSE STATEMENT
				case "water": 
					if(resourcePerPlanet[modifySeed(resource.getId())][WATERID] + resource.getAmountPerHour() > resourcePerPlanet[modifySeed(resource.getId())][STORAGEID]){
						long difference = (resourcePerPlanet[modifySeed(resource.getId())][STORAGEID] - resourcePerPlanet[modifySeed(resource.getId())][WATERID]);
						resourcePerPlanet[modifySeed(resource.getId())][WATERID] = resourcePerPlanet[modifySeed(resource.getId())][STORAGEID];
						setWater(getWater() + difference);
						break;
					}else{
						resourcePerPlanet[modifySeed(resource.getId())][WATERID] += resource.getAmountPerHour();
						setWater(getWater() + resource.getAmountPerHour()); break;
					} // END OF IF-ELSE STATEMENT
				case "money":
					if(getMoney() + resource.getAmountPerHour() > totalDepositLimit){
						setMoney(totalDepositLimit); break;
					}else{
					setMoney(getMoney() + resource.getAmountPerHour());
					break;
					} // END OF IF-ELSE STATEMENT
				}
			}
		}else{
			for(Resources resource: this.resources){
				switch(resource.getType()){
				case "metal":
					if((getMetal() + resource.getAmountPerHour()) > getStorage()) {
						setMetal(getStorage());
						break;
					}else{
						setMetal(getMetal() + resource.getAmountPerHour()); 
						break;
					} // END OF IF-ELSE STATEMENT
				case "uranium":
					if((getUranium() + resource.getAmountPerHour()) > getStorage() ){
						setUranium(getStorage());
						break;
					}else{
						setUranium(getUranium() + resource.getAmountPerHour());
						break;
					} // END OF IF-ELSE STATEMENT
				case "aether":
					if((getAether() + resource.getAmountPerHour()) > getStorage()){
						setAether(getStorage()); 
						break;
					}else{
						setAether(getAether() + resource.getAmountPerHour());
						break;
					} // END OF IF-ELSE STATEMENT
				case "water":
					if((getWater() + resource.getAmountPerHour()) > getStorage()){
						setWater(getStorage()); 
						break;
					}else{
						setWater(getWater() + resource.getAmountPerHour());
						break;
					} // END OF IF-ELSE STATEMENT
				case "money":
					if(getMoney() + resource.getAmountPerHour() > totalDepositLimit){
						setMoney(totalDepositLimit); 
						break;
					}else{
						setMoney(getMoney() + resource.getAmountPerHour());
						break;
					} // END OF IF-ELSE STATEMENT
				}
			}
		} // END OF IF-ELSE STATEMENT ( THE FIRST ONE ) 
	}
	
	/**
	 * A method which return a specific resource
	 * @param _type the type of resource needed. Ex: "metal", "uranium", etc.
	 * @param seed the seed of the resource
	 * @return the resource needed
	 */
	public Resources getResource(String _type, long seed){
		for(Resources resource: this.resources){
			if(resource.getType().equals(_type) && resource.getId() == seed)
				return resource;
		}
		return null;
	}
	
	/**
	 * Verify if a player can upgrade his buildings: resources, ship yard, storage, water, etc.
	 * @param _type the type of the resource
	 * @param level the level of the resource + 1
	 * @return true if the player can upgrade that resource or false otherwise
	 */
	public boolean verifyAvailableResources(String _type,int level,long seed){
		// to be updated
		if(getNumberOfPlanets() < 5){
			switch(_type){
			case "metal": 
				if(getResourceOnPlanet(METALID,seed) >= metalRequiredToUpgradeMetal[level] && getResourceOnPlanet(URANIUMID,seed) >= uraniumRequiredToUpgradeMetal[level] && getResourceOnPlanet(AETHERID,seed) >= aetherRequiredToUpgradeMetal[level] && getActivePopulation() >= populationRequiredToUpgradeMetal[level]){
					sumOfPopulationUsed += populationRequiredToUpgradeMetal[level];
					if(Main.USEDB){
						Main.db.dbActions("UPDATE Resources SET Metal = " + (level+1) + " WHERE PSeed = " + seed + ";");
						Main.db.dbActions("UPDATE SAccounts SET Population = " + getActivePopulation() + " WHERE Name = '" + Main.PNAME + "';");
					}
					return true;}
				return false;
			case "uranium":
				if(getResourceOnPlanet(METALID,seed) >= metalRequiredToUpgradeUranium[level] && getResourceOnPlanet(URANIUMID,seed) >= uraniumRequiredToUpgradeUranium[level] && getResourceOnPlanet(AETHERID,seed) >= aetherRequiredToUpgradeUranium[level] && getActivePopulation() >= populationRequiredToUpgradeUranium[level]){
					sumOfPopulationUsed += populationRequiredToUpgradeUranium[level];
					if(Main.USEDB){
						Main.db.dbActions("UPDATE Resources SET Uran = " + (level+1) + " WHERE PSeed = " + seed + ";");
						Main.db.dbActions("UPDATE SAccounts SET Population = " + getActivePopulation() + " WHERE Name = '" + Main.PNAME + "';");
					}
					return true;}
				return false;
			case "aether":
				if(getResourceOnPlanet(METALID,seed) >= metalRequiredToUpgradeAether[level] && getResourceOnPlanet(URANIUMID,seed) >= uraniumRequiredToUpgradeAether[level] && getResourceOnPlanet(AETHERID,seed) >= aetherRequiredToUpgradeAether[level] && getActivePopulation() >= populationRequiredToUpgradeAether[level]){
					sumOfPopulationUsed += populationRequiredToUpgradeAether[level];
					if(Main.USEDB){
						Main.db.dbActions("UPDATE Resources SET Aether = " + (level+1) + " WHERE PSeed = " + seed + ";");
						Main.db.dbActions("UPDATE SAccounts SET Population = " + getActivePopulation() + " WHERE Name = '" + Main.PNAME + "';");
					}
					return true;}
				return false;
			case "water":
				if(getResourceOnPlanet(METALID,seed) >= metalRequiredToUpgradeWater[level] && getResourceOnPlanet(URANIUMID,seed) >= uraniumRequiredToUpgradeWater[level] && getResourceOnPlanet(AETHERID,seed) >= aetherRequiredToUpgradeWater[level]){
					if(Main.USEDB){
						Main.db.dbActions("UPDATE Resources SET Water = " + (level+1) + " WHERE PSeed = " + seed + ";");
					}
					return true;
				}
				return false;
			case "storage":
				if(getResourceOnPlanet(METALID,seed) >= metalRequiredToUpgradeStorage[level] && getResourceOnPlanet(URANIUMID,seed) >= uraniumRequiredToUpgradeStorage[level] && getResourceOnPlanet(AETHERID,seed) >= aetherRequiredToUpgradeStorage[level] && getActivePopulation() >= populationRequiredToUpgradeStorage[level]){
					sumOfPopulationUsed += populationRequiredToUpgradeStorage[level];
					if(Main.USEDB){
						Main.db.dbActions("UPDATE Resources SET Storage = " + (level+1) + " WHERE PSeed = " + seed + ";");
						Main.db.dbActions("UPDATE SAccounts SET Population = " + getActivePopulation() + " WHERE Name = '" + Main.PNAME + "';");
					}
					return true;
					}
				return false;
			case "population":
				if(getResourceOnPlanet(METALID,seed) >= metalRequiredToUpgradeRecruitmentAgency[level] && getResourceOnPlanet(URANIUMID,seed) >= uraniumRequiredToUpgradeRecruitmentAgency[level] && getResourceOnPlanet(AETHERID,seed) >= aetherRequiredToUpgradeRecruitmentAgency[level] && getResourceOnPlanet(WATERID,seed) >= waterRequiredToUpgradeRecruitmentAgency[level]){
					if(Main.USEDB){
						Main.db.dbActions("UPDATE Resources SET Pop = " + (level+1) + " WHERE PSeed = " + seed + ";");
					}
					return true;
				}
				return false;
			case "ship1":
				if(getResourceOnPlanet(METALID,seed) >= metalRequiredToUpgradeShip1 && getResourceOnPlanet(URANIUMID,seed) >= uraniumRequiredToUpgradeShip1 && getResourceOnPlanet(AETHERID,seed) >= aetherRequiredToUpgradeShip1 && getActivePopulation() >= populationRequiredToUpgradeShip1){
					shipCounted = false;
					resourcePerPlanet[modifySeed(seed)][SHIPTYPE1ID] += 1;
					numbersOfShipsType1 += 1;
					playerShips[1] += 1;
					sumOfPopulationUsed += populationRequiredToUpgradeShip1;
					if(Main.USEDB){
						Main.db.dbActions("UPDATE Planets SET Ship1 = " + resourcePerPlanet[modifySeed(seed)][SHIPTYPE1ID] + " WHERE PSeed = " + seed + ";");
						Main.db.dbActions("UPDATE SAccounts SET Population = " + getActivePopulation() + " WHERE Name = '" + Main.PNAME + "';");
					}
					return true;
				}
				return false;
			case "ship2":
				if(getResourceOnPlanet(METALID,seed) >= metalRequiredToUpgradeShip2 && getResourceOnPlanet(URANIUMID,seed) >= uraniumRequiredToUpgradeShip2 && getResourceOnPlanet(AETHERID,seed) >= aetherRequiredToUpgradeShip2 && getActivePopulation() >= populationRequiredToUpgradeShip2){
					shipType2Counted = false;
					resourcePerPlanet[modifySeed(seed)][SHIPTYPE2ID] += 1;
					setNumbersOfShipsType2(getNumbersOfShipsType2() + 1);
					playerShips[2] += 1;
					sumOfPopulationUsed += populationRequiredToUpgradeShip2;
					if(Main.USEDB){
						Main.db.dbActions("UPDATE Planets SET Ship2 = " + resourcePerPlanet[modifySeed(seed)][SHIPTYPE2ID] + " WHERE PSeed = " + seed + ";");
						Main.db.dbActions("UPDATE SAccounts SET Population = " + getActivePopulation() + " WHERE Name = '" + Main.PNAME + "';");
					}
					return true;
				}
				return false;
			case "ship3":
				if(getResourceOnPlanet(METALID,seed) >= metalRequiredToUpgradeShip3 && getResourceOnPlanet(URANIUMID,seed) >= uraniumRequiredToUpgradeShip3 && getResourceOnPlanet(AETHERID,seed) >= aetherRequiredToUpgradeShip3 && getActivePopulation() >= populationRequiredToUpgradeShip3){
					shipType3Counted = false;
					resourcePerPlanet[modifySeed(seed)][SHIPTYPE3ID] += 1;
					setNumbersOfShipsType3(getNumbersOfShipsType3() + 1);
					playerShips[3] += 1;
					sumOfPopulationUsed += populationRequiredToUpgradeShip3;
					if(Main.USEDB){
						Main.db.dbActions("UPDATE Planets SET Ship3 = " + resourcePerPlanet[modifySeed(seed)][SHIPTYPE3ID] + " WHERE PSeed = " + seed + ";");
						Main.db.dbActions("UPDATE SAccounts SET Population = " + getActivePopulation() + " WHERE Name = '" + Main.PNAME + "';");
					}
					return true;
				}
				return false;
			case "money":
				if(getResourceOnPlanet(METALID,seed) >= metalRequiredToUpgradeBank[level] && getResourceOnPlanet(URANIUMID,seed) >= uraniumRequiredToUpgradeBank[level] && getResourceOnPlanet(AETHERID,seed) >= aetherRequiredToUpgradeBank[level] && getActivePopulation() >= populationRequiredToUpgradeBank[level]){
					sumOfPopulationUsed += populationRequiredToUpgradeBank[level];
					if(Main.USEDB){
						Main.db.dbActions("UPDATE Resources SET Bank = " + (level+1) + " WHERE Pseed = " + seed + ";");
						Main.db.dbActions("UPDATE SAccounts SET Population = " + getActivePopulation() + " WHERE Name = '" + Main.PNAME + "';");
					}
					return true;
				}
				return false;
			default: return false;
			}
		}else{
			switch(_type){
			case "metal": 
				if(getMetal() >= metalRequiredToUpgradeMetal[level] && getUranium() >= uraniumRequiredToUpgradeMetal[level] && getAether() >= aetherRequiredToUpgradeMetal[level] && getActivePopulation() >= populationRequiredToUpgradeMetal[level]){
					sumOfPopulationUsed += populationRequiredToUpgradeMetal[level];
					return true;}
				return false;
			case "uranium":
				if(getMetal() >= metalRequiredToUpgradeUranium[level] && getUranium() >= uraniumRequiredToUpgradeUranium[level] && getAether() >= aetherRequiredToUpgradeUranium[level] && getActivePopulation() >= populationRequiredToUpgradeUranium[level]){
					sumOfPopulationUsed += populationRequiredToUpgradeUranium[level];
					return true;}
				return false;
			case "aether":
				if(getMetal() >= metalRequiredToUpgradeAether[level] && getUranium() >= uraniumRequiredToUpgradeAether[level] && getAether() >= aetherRequiredToUpgradeAether[level] && getActivePopulation() >= populationRequiredToUpgradeAether[level]){
					sumOfPopulationUsed += populationRequiredToUpgradeAether[level];
					return true;}
				return false;
			case "water":
				if(getMetal() >= metalRequiredToUpgradeWater[level] && getUranium() >= uraniumRequiredToUpgradeWater[level] && getAether() >= aetherRequiredToUpgradeWater[level])
					return true;
				return false;
			case "storage":
				if(getMetal() >= metalRequiredToUpgradeStorage[level] && getUranium() >= uraniumRequiredToUpgradeStorage[level] && getAether() >= aetherRequiredToUpgradeStorage[level] && getActivePopulation() >= populationRequiredToUpgradeStorage[level]){
					sumOfPopulationUsed += populationRequiredToUpgradeStorage[level];
					return true;}
				return false;
			case "population":
				if(getMetal() >= metalRequiredToUpgradeRecruitmentAgency[level] && getUranium() >= uraniumRequiredToUpgradeRecruitmentAgency[level] && getAether() >= aetherRequiredToUpgradeRecruitmentAgency[level] && getWater() >= waterRequiredToUpgradeRecruitmentAgency[level])
					return true;
				return false;
			case "ship1":
				if(getMetal() >= metalRequiredToUpgradeShip1 && getUranium() >= uraniumRequiredToUpgradeShip1 && getAether() >= aetherRequiredToUpgradeShip1 && getActivePopulation() >= populationRequiredToUpgradeShip1){
					shipCounted = false;
					resourcePerPlanet[modifySeed(seed)][SHIPTYPE1ID] += 1;
					numbersOfShipsType1 += 1;
					playerShips[1] += 1;
					sumOfPopulationUsed += populationRequiredToUpgradeShip1;
					return true;
				}
				return false;
			case "ship2":
				if(getMetal() >= metalRequiredToUpgradeShip2 && getUranium() >= uraniumRequiredToUpgradeShip2 && getAether() >= aetherRequiredToUpgradeShip2 && getActivePopulation() >= populationRequiredToUpgradeShip2){
					shipType2Counted = false;
					resourcePerPlanet[modifySeed(seed)][SHIPTYPE2ID] += 1;
					setNumbersOfShipsType2(getNumbersOfShipsType2() + 1);
					playerShips[2] += 1;
					sumOfPopulationUsed += populationRequiredToUpgradeShip2;
					return true;
				}
				return false;
			case "ship3":
				if(getMetal() >= metalRequiredToUpgradeShip3 && getUranium() >= uraniumRequiredToUpgradeShip3 && getAether() >= aetherRequiredToUpgradeShip3 && getActivePopulation() >= populationRequiredToUpgradeShip3){
					shipType3Counted = false;
					resourcePerPlanet[modifySeed(seed)][SHIPTYPE3ID] += 1;
					setNumbersOfShipsType3(getNumbersOfShipsType3() + 1);
					playerShips[3] += 1;
					sumOfPopulationUsed += populationRequiredToUpgradeShip3;
					return true;
				}
				return false;
			case "money":
				if(getMetal() >= metalRequiredToUpgradeBank[level] && getUranium() >= uraniumRequiredToUpgradeBank[level] && getAether() >= aetherRequiredToUpgradeBank[level] && getActivePopulation() >= populationRequiredToUpgradeBank[level]){
					sumOfPopulationUsed += populationRequiredToUpgradeBank[level];
					return true;}
				return false;
			default: return false;
			}
		} // END OF IF-ELSE STATEMENT
	}

	/**
	 * Get the points a player has
	 * @return the number of points a player has
	 */
	public long getPoints() {
		return points;
	}

	/**
	 * Set the points a player has
	 * @param points the number of points you want to set
	 */
	public void setPoints(long points) {
		this.points = points;
	}
	
	/**
	 * Update the number of points a player has
	 */
	public void updatePoints(){
		long s = 0;
		for(Resources resource: resources){
			s += resource.getLevel() * 83;
		}
		setPoints(s);
	}
	
	/**
	 * Modify the seed of the planet in order to work correctly in PlayerStats class
	 * @param seed the seed of the planet
	 * @return a new seed/id which can be used in PlayerStats class
	 */
	private int modifySeed(long seed){
		if(familiarizeSeed.size() == 0){
			familiarizeSeed.add(seed);
			return 0;
		}
		for(int i=0; i<familiarizeSeed.size(); i++){
			if(seed == familiarizeSeed.get(i)){
				return i;
			}
		}
		familiarizeSeed.add(seed);
		return (familiarizeSeed.size() - 1);
	}
	
	/**
	 * Get the amount of resource a player has on a specific planet
	 * @param id the id of the resource ( METALID, URANIUMID, etc. )
	 * @param seed the seed of the planet
	 * @return the resource a player has on a specific planet
	 */
	public long getResourceOnPlanet(int id, long seed){
		return resourcePerPlanet[modifySeed(seed)][id];
	}
	
	/**
	 * Set the amount of resource a player has on a specific planet
	 * @param id the id of the resource ( METALID, URANIUMID, AETHERID, etc. )
	 * @param seed the seed of the planet
	 * @param value the value that you want to set
	 */
	public void setResourceOnPlanet(int id, long seed, long value){
		this.resourcePerPlanet[modifySeed(seed)][id] = value;
	}
	
	/**
	 * Update useful methods for PlayerStats
	 */
	public void updateStatics(){
		updatePoints();
		checkCrystalAvailability();
		updateRecruitmentAgency();
		updateStorage();
//		updateShips();
	}
	
	/**
	 * Get the deposit limit for a certain level
	 * @param level
	 * @return the deposit limit for a certain level
	 */
	public int getDepositLimit(int level){
		return depositLimitBankPerLevel[level];
	}
	
	/**
	 * Set the number of ships type 1 per total
	 * @param number the number that you want to set
	 */
	public void setNumberOfShipsType1(int number){
		numbersOfShipsType1 = number;
	}
	
	/**
	 * Get an array of player ships ( index 0 -> first type, inde 1 -> second type, .... )
	 * @return an array of player ships
	 */
	public int[] getPlayerShips(){
		return playerShips;
	}
	
	public long getAmountNecessaryForTrading(long amountToBuy, long difference){
		return 40*(amountToBuy + difference);
	}
	
	public long getAmountToPayDebt(long valueResource1, long valueResource2, long valueResource3){
		if(valueResource1 < 0) valueResource1 *= (-1);
		if(valueResource2 < 0) valueResource2 *= (-1);
		if(valueResource3 < 0) valueResource3 *= (-1);
		long[] valueResources = new long[] {valueResource1, valueResource2, valueResource3};
		Arrays.sort(valueResources);
		long difference0 = valueResources[2] - valueResources[0];
		long difference1 = valueResources[2] - valueResources[1];
		long difference2 = 0;
		long valueInMoney = getAmountNecessaryForTrading(valueResources[0],difference0) + getAmountNecessaryForTrading(valueResources[1],difference1) + getAmountNecessaryForTrading(valueResources[2],difference2);
		if(valueInMoney == 0) return 0;
		return (long)(valueInMoney + (0.15)*valueInMoney) + 1;
	}

	/**
	 * Get the number of ships type 2 per total
	 * @return the number of ships type 2 per total
	 */
	public int getNumbersOfShipsType2() {
		return numbersOfShipsType2;
	}

	/**
	 * Set the number of ships type 2 per total
	 * @param numbersOfShipsType2 the number you want to set
	 */
	public void setNumbersOfShipsType2(int numbersOfShipsType2) {
		this.numbersOfShipsType2 = numbersOfShipsType2;
	}

	/**
	 * Get the number of ships type 3 per total
	 * @return the number of ships type 3 per total
	 */
	public int getNumbersOfShipsType3() {
		return numbersOfShipsType3;
	}

	/**
	 * Set the number of ships type 3 per total
	 * @param numbersOfShipsType3 the number you want to set
	 */
	public void setNumbersOfShipsType3(int numbersOfShipsType3) {
		this.numbersOfShipsType3 = numbersOfShipsType3;
	}

	/**
	 * Get the resources
	 * @return an arrayList of resources
	 */
	public ArrayList<Resources> getResources() {
		return resources;
	}
	
	/**
	 * Set the sstorage for allplanets
	 * @param _value
	 */
	public void setStorageAllPlanets(int _value){
		this.storageAllPlanets = _value;
	}
}
