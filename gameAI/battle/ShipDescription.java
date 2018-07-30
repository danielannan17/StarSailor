package gameAI.battle;

import entities.Ship;
import entities.Vector2D;

/**
 * A class that receives information from the a ship
 * in order to pass it to the BattleAI. This is done in order to
 * avoid passing the actual object of an enemy ship to the BattleAI,
 * since it doesn't need to(and it should not be allowed to) have
 * full control over it
 */
public class ShipDescription extends ObjectDescription{

    private Ship fShip;
    private Integer fShipTeam;
    
    public ShipDescription(){
    	super("ship");
    	this.fShip = null;
    }
    
    public ShipDescription(Ship _fShip){
    	super("ship");
        this.fShip = _fShip;
    }

    /**
     * Getter used to pass the information further to the BattleAI
     */

    public int getXLocation(){
    	if(this.fShip == null){
    		return 0;
    	}
    	return this.fShip.getxLocation();
    }

    public int getYLocation(){
    	if(this.fShip == null){
    		return 0;
    	}
    	return this.fShip.getyLocation();
    }

    public float getHealth(){
    	if(this.fShip == null){
    		return 0;
    	}
        return this.fShip.getHealth();
    }

    /**
     * Set the team to which the ship belongs
     * @param _team
     */
    public void setfShipTeam(int _team){
    	this.fShipTeam = _team;
    }

    /**
     * Changes the acceleration of the ship in order to move it to the
     * next location
     * @param _acceleration
     */
    public void changeDestination(Vector2D _acceleration){
        this.fShip.addAccelerationToQueue(_acceleration);
    }
    
    /**
     * @return the velocity of the ship
     */
    
	@Override
	public Vector2D getVelocity() {
		return this.getVelocity();
	}
}
