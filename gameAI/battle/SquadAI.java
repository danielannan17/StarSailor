package gameAI.battle;

import entities.Ship;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * A squad AI is an object that handles the execution of the orders(in
 * the form of BattleOrder objects) it receives from the central AI system by
 * interpreting the order and passing it to the SquadFormation object which
 * in turn handles the actual procedures involved in the execution of the order 
 */
public class SquadAI extends Thread{

    private volatile boolean fRunFlag;
    private volatile BattleOrder fCurrentOrder;
    private SquadFormation fSquadFormation;
    private BattleAI fAI;
    private int fOrderTickTimer;
    
    private ShipDescription fTarget;
    
    private int fStandardDistance = 90;
    private int fStandardSpecialFireDistance = 450;
    
    private Random rand;
    
    /**
     * Creates the AI for the squad
     * @param _ships        = the ships in the squad
     * @param _ai            = the AI of the team the squad belongs to
     * @param _ticksTimer   = the time at which the order checking is performed in the squad
     */
    public SquadAI(ArrayList<Ship> _ships, BattleAI _ai, int _ticksTimer){

        this.fCurrentOrder = null;
        this.fRunFlag = true;
        this.fSquadFormation = new SquadFormation(_ships);
        this.fAI = _ai;
        this.fOrderTickTimer = _ticksTimer;
        
        this.rand = new Random();
    }
    
    /**
     * 	Runs the standard behaviour for a squad
     *  If there is no order from the central AI then pick the closest target and attack it
     *  otherwise execute the given order
     *  Always check if the ships in the squad are still alive or in danger and if their target was destroyed
     *  In case all the ships were destroyed then remove the squad from the list in the central AI
     *  if the  
     */
    @Override
    public void run() {

        while(fRunFlag){
        	
        	//check if the squad is still alive
            if(!this.fSquadFormation.isSquadAlive()){
            	this.setfRunFlag(false);
            	this.fAI.removeDestroyedSquad(this);
            	System.out.println("removed sq");
            	 //call the garbage collector from time to time
            	System.gc();
            }
            else{
	            if(this.fCurrentOrder == null){
	            	
	            	if(this.fTarget == null){	            		
	            		this.fTarget = this.findClosestTarget();
	            	} 
	            	
	            	this.fire();
	            	
	            	this.fSquadFormation.moveSquadTowards(
	                        this.fTarget.getXLocation(),
	                        this.fTarget.getYLocation(),
	                        this.fAI.getBattleWidth(),
	                        this.fAI.getBattleHeight(),
	                        this.fStandardDistance
	                );
	            	
	            	
	            } else{
		            
	            	//check whether the order involves supporting a squad/player
	            	if (this.fCurrentOrder.getAssist() != null){
	            		
	            		this.fSquadFormation.moveSquadTowards(
	            				this.fCurrentOrder.getAssist().getxLocation(),
	            				this.fCurrentOrder.getAssist().getyLocation(),
		                        this.fAI.getBattleWidth(),
		                        this.fAI.getBattleHeight(),
		                        this.fStandardDistance
		                );
	            		
	            		//Support the ship by attacking the closest enemies that might inflict damage 
	            		this.fTarget = this.findClosestTarget();
	            			
	            		// fire towards the target 
	            		this.fire();
	            		
	            	} else if (this.fCurrentOrder.getTarget() != null) {
	            		
	            		this.fTarget = this.fCurrentOrder.getTarget();
	            		
	            		this.fSquadFormation.moveSquadTowards(
		                        this.fTarget.getXLocation(),
		                        this.fTarget.getYLocation(),
		                        this.fAI.getBattleWidth(),
		                        this.fAI.getBattleHeight(),
		                        this.fStandardDistance
		                );		
	            		
	            		// fire towards the target 
	            		this.fire();
	            		
	            	} else {
	            		// bad order
	            		this.fCurrentOrder = null;
	            		
	            		if (this.fTarget == null){
	            			this.fTarget = this.findClosestTarget();
	            		} 
	            		
	            		this.fire(); 
		            	
		            	this.fSquadFormation.moveSquadTowards(
		                        this.fTarget.getXLocation(),
		                        this.fTarget.getYLocation(),
		                        this.fAI.getBattleWidth(),
		                        this.fAI.getBattleHeight(),
		                        this.fStandardDistance
		                );
	            		
	            	}
	            }
	        	
	            if(this.fTarget != null && this.fTarget.getHealth() <= 0){
	            	
	            	System.out.println(this.fSquadFormation.getLeader().getTeam() + " " + this.getLeader().getId() + " target x =  " + this.fTarget.getXLocation() + " target y = " + this.fTarget.getYLocation() + "\n");
	            	System.out.println("DESTROYED");
	            	
	            	//the ship doesn't exist anymore so the AI doesn't need to see it
	            	this.fAI.removedDestroyedEnemyShip(this.fTarget);
	            	this.fCurrentOrder = null;
	            	this.fTarget = null;
	            	System.gc();
	            	
	            	//see if the has variables need to be modified as well
	            	
	            } else {
	            	// check whether the squad is at its limits
	            	if (this.fSquadFormation.isInDanger()){
	            		
	            		int nextXLocation = rand.nextInt(this.fAI.getBattleWidth());
	            		int nextYLocation = rand.nextInt(this.fAI.getBattleWidth());
	            		
	            		while (nextXLocation < this.fStandardDistance || nextXLocation > this.fAI.getBattleWidth() - this.fStandardDistance) {
	            			nextXLocation = rand.nextInt((int)this.fAI.getBattleWidth());
	            		}
	            		
	            		while (nextYLocation < this.fStandardDistance || nextYLocation > this.fAI.getBattleHeight() - this.fStandardDistance) {
	            			nextYLocation = rand.nextInt((int)this.fAI.getBattleHeight());
	            		}
	            		this.fSquadFormation.teleportTo(nextXLocation, nextYLocation);
	            	}
	            }
	            
	            //check for surrounding objects and avoid them
            }
            

        	try {
                Thread.sleep(this.fOrderTickTimer * this.fAI.getSleepTimeCoefficient());
            } catch (InterruptedException e){
        	    //delete the squad somehow or make it safe for the game
                this.fRunFlag = false;
        	    break;
            }
        }
    }
    
    /**
     *  Fire a shot to the current target taking into account what type of shot the entity is ordered to fire
     */
    private void fire(){
    	
    	
    	if (this.fCurrentOrder == null || this.fCurrentOrder.getFireType() < 0){
    	
	    	if (AILogistics.dist(this.fTarget.getXLocation(), this.fTarget.getYLocation(), this.getLeader().getxLocation(),this.getLeader().getyLocation()) <= this.fStandardSpecialFireDistance){
	    		
	    		if (AILogistics.dist(this.fTarget.getXLocation(), this.fTarget.getYLocation(), this.getLeader().getxLocation(),this.getLeader().getyLocation()) <= this.fStandardSpecialFireDistance/2) {
		    		this.fSquadFormation.fire(
		        			this.fTarget.getXLocation(),
		                    this.fTarget.getYLocation(),
		                    2
		        	);
	    		} else {
	    			this.fSquadFormation.fire(
		        			this.fTarget.getXLocation(),
		                    this.fTarget.getYLocation(),
		                    1
		        	);
	    		}
	    		System.out.println("Fire 1");
	    		
	    	} else {
	    		this.fSquadFormation.fire(
	        			this.fTarget.getXLocation(),
	                    this.fTarget.getYLocation(),
	                    0
	        	);
	    	}
    	} else {
    		
    		if (AILogistics.dist(this.fTarget.getXLocation(), this.fTarget.getYLocation(), this.getLeader().getxLocation(),this.getLeader().getyLocation()) <= this.fStandardSpecialFireDistance){
    			
    			if (AILogistics.dist(this.fTarget.getXLocation(), this.fTarget.getYLocation(), this.getLeader().getxLocation(),this.getLeader().getyLocation()) <= this.fStandardSpecialFireDistance/2) {
	    			this.fSquadFormation.fire(
		        			this.fTarget.getXLocation(),
		                    this.fTarget.getYLocation(),
		                    2
		        	);
    			} else {
    				this.fSquadFormation.fire(
		        			this.fTarget.getXLocation(),
		                    this.fTarget.getYLocation(),
		                    1
		        	);
    			}
    		}else{
    		
	    		this.fSquadFormation.fire(
	        			this.fTarget.getXLocation(),
	                    this.fTarget.getYLocation(),
	                    this.fCurrentOrder.getFireType()
	        	);
    		}
    	}
    }
    
    /**
     * Finds the closest enemy ship to this squad
     * @return closest
     */
    private ShipDescription findClosestTarget(){
    	
    	//choose the closest enemy ship and go after it
    	ShipDescription closest = AILogistics.getCLosestSurroundingEnemyShip(this.fSquadFormation.getLeader().getxLocation(), this.fSquadFormation.getLeader().getyLocation(), this.fAI.getStandardCheckingRadius(), this.fAI);
          	
    	if(closest.getXLocation() == 0 && closest.getYLocation() == 0){
    		//if there is no ship in the proximity then scan the whole map
    		closest = AILogistics.getCLosestSurroundingEnemyShip(this.fSquadFormation.getLeader().getxLocation(), this.fSquadFormation.getLeader().getyLocation(), -3, this.fAI);
    	}
    	return closest;
    }
   
    /**
     * Overrides the current order the Ship AI performs
     * @param _newFCurrentOrder = the order the ship needs to perform next
     */
    public void setfCurrentOrder(BattleOrder _newFCurrentOrder) {
        this.fCurrentOrder = _newFCurrentOrder;
    }
    
    /**
     * @return the order this squad is currently executing
     */
    public BattleOrder getfCurrentOrder() {
        return this.fCurrentOrder;
    }
    

    /**
     *
     */
    public boolean isPerformingAnyAIGivenOrder(){
        return (this.fCurrentOrder != null);
    }

    /**
     * Overrides the run flag in order to stop the thread
     * @param _newFRunFlag = the new value of the flag(normally false)
     */
    public void setfRunFlag(boolean _newFRunFlag){
        this.fRunFlag = _newFRunFlag;
    }
    
    /** 
     * @return a list of description objects for the ships
     */
    public List<ShipDescription> getShipDescriptions(){
    	List<ShipDescription> auxList = new LinkedList<>();
    	for(Ship s : this.fSquadFormation.getShips()){
    		auxList.add(new ShipDescription(s));
    	}
    	auxList.stream().forEach(sd -> sd.setfShipTeam(0));
    	return auxList;
    }

    /**
     * @return a boolean whether the squad has been destroyed or not
     */
    public boolean hasSquadBeenDestroyed(){
        return false;
    }

    /**
     * @return the current size of the squad
     */
    public int getSquadSize(){
        return this.fSquadFormation.getShips().size();
    }
    
    /**
     * @return the leader of the squad
     */
    public Ship getLeader(){
    	return this.fSquadFormation.getLeader();
    } 
    
    /**
     * @return a list containing the ships in this squad
     */
    public List<Ship> getShips(){
    	return this.fSquadFormation.getShips();
    }
    
    /**
     * checks if this squad is the same as the given squad 
     * the criteria is the location of the leader
     * @param _squad = the given squad
     * @return
     */
    public boolean compareTo(SquadAI _squad){
    	return this.getLeader().getxLocation() == _squad.getLeader().getxLocation() && this.getLeader().getyLocation() == _squad.getLeader().getyLocation();
    }

	/**
	 * Returns the squad
	 * @return
	 */
	public SquadFormation getfSquadFormation(){
    	return this.fSquadFormation;
	}
}
