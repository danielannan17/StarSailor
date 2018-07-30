package gameAI.battle;


import entities.Player;
import entities.Ship;
import entities.projectiles.Projectile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

//git-teaching.cs.bham.ac.uk/mod-team-proj-2016/a3.git


public class BattleAI extends java.lang.Thread {
	
	private int teamID;
	
	private Player fPlayer;
	
	private List<SquadAI> fAIShips;
    private volatile List<ShipDescription> fEnemyShips;
    
    private List<ObstacleDescription> fMapObstacles;
    private volatile List<ProjectileDescription> fExistingProjectilesOnTheMap;
    
    private int fTicksTimer;
    
    private int battleZoneWidth;
    private int battleZoneHeight;
    private float standardCheckingRadius;
    
    private volatile boolean fRunFlag;
    private String LEVEL;
    
    private int fSleepTimeCoefficient;
    
    /**
     * Constructs an AI object.
     * @param _teamID		 = the ID of the team the AI is fighting for
     * @param _height 		 = the height of the battle zone
     * @param _width		 = the width of the battle zone
     * @param _LEVEL         = the level of the AI
	 * @param _player		 = the player ship
     * @param _playerShips   = the fleet of the AI
     * @param _enemyShips    = the fleet of the enemy(AI/player)
     * @param _fMapObstacles = the obstacles that appear on the map
     * @param _groupSize     = the standard size of a ship squad
     */
   
    public BattleAI(int _teamID, int _height, int _width, String _LEVEL, Player _player, List<entities.Entity> _playerShips, List<entities.Entity> _enemyShips,List<entities.Entity> _fMapObstacles, int _groupSize, int _ticksTimer) {
    	
    	this.teamID = _teamID;
        this.LEVEL = _LEVEL;

        if(_player != null) {
			this.fPlayer = _player;
		} else {
        	this.fPlayer = new Player("0",1,1,100,450,null,null,null, null);
		}

        this.battleZoneWidth = _width;
        this.battleZoneHeight = _height;
        this.standardCheckingRadius = _width/10;
        
        System.out.println("za ===" + this.battleZoneWidth + " " + this.battleZoneHeight);
        
        if (_playerShips.size() <= 3){
        	this.fSleepTimeCoefficient = 1;
        } else if (_playerShips.size() < 5){
        	this.fSleepTimeCoefficient = 3;
        } else if (_playerShips.size() < 8){
        	this.fSleepTimeCoefficient = 4;
        } else {
        	this.fSleepTimeCoefficient = 6;
        }
        
        //creates the squads
        this.fAIShips = new ArrayList<>();

        try {

        	System.out.println("Player team size : " + _playerShips.size());
        	
            for (int i = 0; i < _playerShips.size(); i += _groupSize) {
                ArrayList<entities.Ship> group = new ArrayList<>();
                for (int j = 0; j < _groupSize; j++) {
                	if(i + j < _playerShips.size())
                		group.add( (entities.Ship) _playerShips.get(i + j));
                }
                this.fAIShips.add(new SquadAI(group, this, _ticksTimer));
            }
            
        }
        catch (NullPointerException e){
            System.out.print("AI fleet is NULL");
            e.printStackTrace();

        }

        //create the list of description objects of the enemy ships
        try {
            this.fEnemyShips = (ArrayList<ShipDescription>) _enemyShips.stream().map(eShip -> new ShipDescription((entities.Ship) eShip)).collect(Collectors.toList());
            this.fEnemyShips.stream().forEach(eShip -> eShip.setfShipTeam(1));
            System.out.println("Enemy team size : " + this.fEnemyShips.size());
        }
        catch (NullPointerException e){
            System.out.println("Enemy fleet is null");
            e.printStackTrace();
        }
        this.fRunFlag = true;

        if(_fMapObstacles == null)
            this.fMapObstacles = new ArrayList<>();
        else
            this.fMapObstacles = new ArrayList<>();

        this.fExistingProjectilesOnTheMap = new LinkedList<>();
        this.fTicksTimer = _ticksTimer;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
    	
    	Random rand = new Random();
    	
    	//start the threads

		for (int i = 0; i < this.fAIShips.size(); i++){
			this.fAIShips.get(i).start();
		}

		/*for (SquadAI squadAI: this.fAIShips){
            squadAI.start();
        }*/
    	
        synchronized (this) {
            switch (this.LEVEL) {
                case "EASY":
                    runEasyAI();
                case "MEDIUM":
                	
                	switch(rand.nextInt(3)){
                		case 0:
                			//this.runMediumBalancedAI();
                			this.runMediumDefendingAI();
                			break;
                		/*case 1:
                			this.runMediumDefendingAI();
                			break;
                		case 2:
                			this.runMediumAttackingAI();
                			break;
                		*/default:
                			//this.runMediumBalancedAI();
                			this.runMediumDefendingAI();
                			break;
                	}
                	
                    break;
                default:
                    runEasyAI();
            }
        }
        
        //make sure to stop all the threads
        for (SquadAI squadAI: this.fAIShips){
            squadAI.setfRunFlag(false);
        }
        System.gc();
    }

    /**
     *  Runs the easy version of the AI which makes the squads to randomly
     *  chose ships to shoot at from the enemy ships
     *  If an enemy ship was destroyed then randomly choose another one
     */

    private void runEasyAI(){

        // for each group choose a ship to shoot at and starts the threads

        while (this.fRunFlag){
            //for each ship check whether it is still alive and if it has destroyed its target or not
        	if (this.fAIShips.size() == 0){
        		System.out.println("DONE");
        		this.fRunFlag = false;
        	}
        	
        	try {
				Thread.sleep(this.fTicksTimer * this.fSleepTimeCoefficient);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    /**
     * Runs an AI that has a balanced strategy between attacking and defending
     * It chooses the closest two ships to the player to be its guards(stay close to him and attack the enemies that approach him)
     * The other entities serve as and aggressive support for the two guards checking for the enemies that approach them and shooting towards them
     */

   	private void runMediumBalancedAI(){
    	
    	List<Integer> guards = new ArrayList<>();
    	
    	
    	while (this.fRunFlag){
            //for each ship check whether it is still alive and if it has destroyed its target or not
        	if (this.fAIShips.size() == 0){
        		System.out.println("DONE");
        		this.fRunFlag = false;
        	}
        	
        	for (int i = 0; i < guards.size(); i++){
        		if (guards.get(i) > this.fAIShips.size() && this.fAIShips.get(guards.get(i)).getfCurrentOrder() == null){
        			//guard has been destroyed
        			guards.remove(i);
        			i--;
        		}
        	}
        	
        	//check if the guards are still alive 	
        	if (guards.size() < 2){
        		
        		// put the closest two squads to the player as its guardians
            	AILogistics.sortSquadsAfterTheDistanceToAGivenLocation(this.fPlayer.getxLocation(), this.fPlayer.getyLocation(), this.fAIShips);
            	
            	guards.clear();
            	
            	if (this.fAIShips.size() < 2){
            		guards.add(0);
            	} else {
            		guards.add(0);
            		guards.add(1);
            	}
            	
            	for (Integer g : guards){
            		this.fAIShips.get(g).setfCurrentOrder(new BattleOrder(this.fPlayer.getxLocation(),this.fPlayer.getyLocation(), null, this.fPlayer,0));
            	}
        	}
        	
        	// put the other entities to attack the enemies that are closing in on the guards
        	// and support the guards
        	for (int i = 0; i < this.fAIShips.size(); i++){
        		
        		
        		
        		int closestGuardIndex = 0;
        		for (Integer g: guards){
        			closestGuardIndex = 
        					(AILogistics.dist(
        							this.fAIShips.get(g).getLeader().getxLocation(), 
        							this.fAIShips.get(g).getLeader().getyLocation(),
        							this.fAIShips.get(i).getLeader().getxLocation(),
        							this.fAIShips.get(i).getLeader().getyLocation()
        					 ) < AILogistics.dist(
         							this.fAIShips.get(closestGuardIndex).getLeader().getxLocation(), 
         							this.fAIShips.get(closestGuardIndex).getLeader().getyLocation(),
         							this.fAIShips.get(i).getLeader().getxLocation(),
         							this.fAIShips.get(i).getLeader().getyLocation()
         					 )? g : closestGuardIndex );
        		}
        		// change the order of this squad
        		this.fAIShips.get(closestGuardIndex).setfCurrentOrder(
        				new BattleOrder(
        						this.fAIShips.get(closestGuardIndex).getLeader().getxLocation(),
        						this.fAIShips.get(closestGuardIndex).getLeader().getyLocation(),
        						AILogistics.getClosestSurroundingEnemyShipFromTwoLocations(
        								this.fAIShips.get(i).getLeader().getxLocation(), 
        								this.fAIShips.get(i).getLeader().getyLocation(), 
        								this.fAIShips.get(closestGuardIndex).getLeader().getxLocation(),
        								this.fAIShips.get(closestGuardIndex).getLeader().getyLocation(), 
        								-3, this
        						),
        						this.fAIShips.get(closestGuardIndex).getLeader(),
        						0
        				)
        		);
        	}
        	
        	
        	try {
				Thread.sleep(this.fTicksTimer * this.fSleepTimeCoefficient);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	
    	
    	
    }
    
    /**
     * Runs an AI that has a defensive strategy 
     * It commands all the ships to assist the player, thus entities serve as and aggressive support for player checking for the enemies that approach them and shooting towards them
     */
    
    private void runMediumDefendingAI(){
    	
    	//make all the ships guard the player and shoot to the closest enemy to the player and the squad
    	for (int i = 0; i < this.fAIShips.size(); i ++){
    		this.fAIShips.get(i).setfCurrentOrder(
    				new BattleOrder(
    						this.fPlayer.getxLocation(),
    						this.fPlayer.getyLocation(),
    						AILogistics.getClosestSurroundingEnemyShipFromTwoLocations(
    								this.fAIShips.get(i).getLeader().getxLocation(), 
    								this.fAIShips.get(i).getLeader().getyLocation(), 
    								this.fPlayer.getxLocation(),
    								this.fPlayer.getyLocation(), 
    								-3, this
    						),
    						this.fPlayer,
    						0
    				)
    		);
    	}
    	
    	while (this.fRunFlag){
            //for each ship check whether it is still alive and if it has destroyed its target or not
        	if (this.fAIShips.size() == 0){
        		System.out.println("DONE");
        		this.fRunFlag = false;
        	}
        
        	//check for each ship if it has executed its order(which should be null in so)
        	for (int i = 0; i < this.fAIShips.size(); i++){
        		if (this.fAIShips.get(i).getfCurrentOrder() == null){
        			
        			// previous order executed so reset it 			
        			this.fAIShips.get(i).setfCurrentOrder(
        					new BattleOrder(
            						this.fPlayer.getxLocation(),
            						this.fPlayer.getyLocation(),
            						AILogistics.getClosestSurroundingEnemyShipFromTwoLocations(
            								this.fAIShips.get(i).getLeader().getxLocation(), 
            								this.fAIShips.get(i).getLeader().getyLocation(), 
            								this.fPlayer.getxLocation(),
            								this.fPlayer.getyLocation(), 
            								-3, this
            						),
            						this.fPlayer,
            						1
            				)
        			);
        		}
        	}
        	
        	try {
				Thread.sleep(this.fTicksTimer * this.fSleepTimeCoefficient);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	
    	
    }
    
    /**
     * Runs the hard version of the AI which has the same strategy as the MediumDefending version 
     * but the ships are much stronger
     */

    private void runHardAI(){
    	
    	for (SquadAI squad : this.fAIShips){
    		for (Ship ship: squad.getShips()){
    			ship.setAttack(2 * ship.getAttack());
    			ship.setArmour(2 * ship.getArmour());
    		}
    	}
    	
    	this.runMediumDefendingAI();
    }

	/**
	 * Add an entity to the projectile list if the entity is a projectile
	 * @param _entity
	 */
	public void addEntity(entities.Entity _entity){
    	if(Projectile.class.isInstance(_entity)){
    		this.addProjectileToProjectileList((Projectile)_entity);
    	}
    }

    /**
     *
     * @param _projectile
     */
    public void addProjectileToProjectileList(Projectile _projectile){
        this.fExistingProjectilesOnTheMap.add(new ProjectileDescription(_projectile));
    }

    /**
     * @return a list of description objects for all the entities on the map that the AI is currently seeing
     */
    public List<ObjectDescription> getEntitiesOfTheEnvironment (){
        List<ObjectDescription> objList = new LinkedList<ObjectDescription>(fMapObstacles);
        objList.addAll(this.fExistingProjectilesOnTheMap);
        objList.addAll(this.fEnemyShips);

        for(SquadAI sq : this.fAIShips){
            objList.addAll(sq.getShipDescriptions());
        }
        return objList;
    }

    /**
     * @return a list comprising the obstacles the AI is seeing at the time the method is called
     */
    public List<ObstacleDescription> getTheObstaclesOfTheEnvironment(){
        return this.fMapObstacles;
    }

    /**
     * @return the enemy ships that the AI is seeing at the time the method is called
     */
    public List<ShipDescription> getTheEnemyShipsOfTheEnvironment(){
        return this.fEnemyShips;
    }
    
    /**
     * @return the allied squads that the AI is seeing at the time the method is called
     */
    public List<SquadAI> getAlliedSquadsOfTheEnvironment(){
    	return this.fAIShips;
    }

    /**
     * @return the projectiles that are currently on the map
     */
    public List<ProjectileDescription> getTheProjectilesOfTheEnvironment(){
        return this.fExistingProjectilesOnTheMap;
    }

    /**
     *  stops this AI thread and its associated SquadAI threads when called
     */
    public void stopAI(){
        for(SquadAI aiShip : this.fAIShips){
            if(aiShip.isAlive()) {
                aiShip.setfRunFlag(false);
            }
        }
    	this.fRunFlag = false;
    }
    
    /**
     * Removes the squads that were destroyed
     */
    
    public void removeDestroyedSquad(SquadAI _aiShip){
    	this.fAIShips.remove(_aiShip);
    }
    
    public void removedDestroyedEnemyShip(ShipDescription _enemyShip){
    	this.fEnemyShips.remove(_enemyShip);
    }
    
    /**
     * @return the standard checking radius
     */
    
    public float getStandardCheckingRadius(){
    	return this.standardCheckingRadius;
    }
    
    /** 
     * @return the width of the battle zone
     */

    public int getBattleWidth(){
    	return this.battleZoneWidth;
    }
    
    /** 
     * @return the height of the battle zone
     */

    public int getBattleHeight(){
    	return this.battleZoneWidth;
    }
    
    /**
     * @return the coefficient for the sleep period of the threads coordinated by this AI 
     */
    
    public int getSleepTimeCoefficient(){
    	return this.fSleepTimeCoefficient;
    }
}
