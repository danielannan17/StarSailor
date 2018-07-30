package gameAI.battle;

import entities.Ship;

/**
 * Represents an order given by the battle AI to a squad AI to be executed
 * 
 */

public class BattleOrder {
	
	private float fDestinationXLocation;
	private float fDestinationYLocation;
	
	private ShipDescription fTargetShip; 
	private Ship fAssist;
	
	private int fFireType;
	
	/**
	 * Constructs a new order to be executed  
	 * @param _destinationXLocation	= the x coordinate of the location the squad executing the order needs to move at
	 * @param _destinationYLocation	= the y coordinate of the location the squad executing the order needs to move at
	 * @param _targetShip			= the enemy ship the squad executing the order needs to shoot at
	 * @param _assist				= the allied ship the squad executing the order must assist
	 * @param _fireType				= the type of projectile the squad executing this order must shoot
	 */
	
	public BattleOrder(float _destinationXLocation, float _destinationYLocation, ShipDescription _targetShip, Ship _assist, int _fireType){
		
		this.fDestinationXLocation = _destinationXLocation;
		this.fDestinationYLocation = _destinationYLocation;
		
		this.fTargetShip = _targetShip;
		this.fAssist	 = _assist;
		
		this.fFireType 	 = _fireType;
	}

	/**
	 * default constructor, mainly for test
 	 */

	public BattleOrder(){

		this.fDestinationXLocation = 0;
		this.fDestinationYLocation = 0;

		this.fTargetShip = null;
		this.fAssist	 = null;

		this.fFireType 	 = 0;
	}
	
	/**
	 * sets the x coordinate of the location towards which the squad executing the order has to move 
	 * @param _newValue
	 */
	
	public void setDestiantionXLocation(float _newValue){
		this.fDestinationXLocation = _newValue;
	}
	
	/**
	 * sets the y coordinate of the location towards which the squad executing the order has to move 
	 * @param _newValue
	 */
	
	public void setDestiantionYLocation(float _newValue){
		this.fDestinationYLocation = _newValue;
	}
	
	/**
	 * changes the enemy ship the squad executing the order has to attack
	 * @param _newTarget
	 */
	
	public void setTarget(ShipDescription _newTarget){
		this.fTargetShip = _newTarget;
	}
	
	/**
	 * changes the ship this squad is assisting
	 * @param _newAssist
	 */
	
	public void setAssist(Ship _newAssist){
		this.fAssist = _newAssist;
	}
	
	/**
	 * changes the type of projectile the squad executing this order must fire
	 * @param _newType
	 */
	public void setFireType(int _newType){
		this.fFireType = _newType;
	}
	
	/**
	 * @return the x coordinate of the location towards which the squad executing the order has to move 
	 */
	
	public float getDestinationXLocation(){
		return this.fDestinationXLocation;
	}
	
	/**
	 * @return the y coordinate of the location towards which the squad executing the order has to move 
	 */
	
	public float getDestiantionYLocation(){
		return this.fDestinationYLocation;
	}
	
	/**
	 * @return the target the squad executing the order is moving at
	 */
	
	public ShipDescription getTarget(){
		return this.fTargetShip;
	}
	
	/**
	 * @return the ship this squad needs to support
	 */
	public Ship getAssist(){
		return this.fAssist;
	}
	
	/**
	 * @return the type of projectile the squad executing this order must fire
	 */
	public int getFireType(){
		return this.fFireType;
	}
}
