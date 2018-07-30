package gameAI.battle;

import entities.Ship;
import entities.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Represents the actual squad
 * Responsible for executing the commands the AI of the squad gives
 * 
 *
 */

public class SquadFormation {
	
	private ArrayList<Ship> fShips;
	private static int standardShipToShipDistance = 20;
	private static int standardWavingMagnitude	= 30;
	
	
	private int currentMagnitude;
	private int currentIncrement;
	
	
	public SquadFormation(ArrayList<Ship> _ships) {
		this.fShips = _ships;
		this.currentMagnitude = 0;
	}

	/**
	 * add ship to squad
	 * @param fShipN
	 */
	
	private void setShip(Ship[] fShipN){
		ArrayList<Ship> fShipA = new ArrayList<Ship>(Arrays.asList(fShipN));
		this.fShips = fShipA;
	}
	
	/**
	 * @returns a list of the ships in the current squad
	 * Should be only called by the corresponding SquadAI
	 */
	public List<Ship> getShips(){
		return this.fShips;
	}

	
	
	/**
	 * command the entire squad to fire and check which type of shot to fire
	 * @param _targetXLocation	= the x coordinate of the target
	 * @param _targetYLocation	= the y coordinate of the target
	 * @param _fireType 		= the type of shot that needs
	 */
	
	public void fire(int _targetXLocation, int _targetYLocation, int _fireType){
		
		switch (_fireType){
			case 0:
				this.fire(_targetXLocation, _targetYLocation);
				break;
			case 1:
				this.fire1(_targetXLocation, _targetYLocation);
			case 2:
				this.fire2(_targetXLocation, _targetYLocation);
			default:
				this.fire(_targetXLocation, _targetYLocation);
		}
	}
	
	
	/**
	 * command the entire squad to fire a simple shot
	 * @param _targetXLocation
	 * @param _targetYLocation
	 */
	
	private void fire(int _targetXLocation, int _targetYLocation){
		
		for (Ship s : this.fShips){
			//s.setDirection(Vector2D.convertToDirection(s.getxLocation(),s.getyLocation(),(int)(double)_targetXLocation,(int)(double)_targetYLocation));
			s.setDirection(Vector2D.convertToDirection(_targetXLocation, _targetYLocation, s.getxLocation(), s.getyLocation()));
			s.useSkill(0);
		}
	}
	
	/**
	 * command the entire squad to fire a type 1 shot
	 * @param _targetXLocation
	 * @param _targetYLocation
	 */
	private void fire1(int _targetXLocation, int _targetYLocation){
		
		for (Ship s : this.fShips){
			//s.setDirection(Vector2D.convertToDirection(s.getxLocation(),s.getyLocation(), _targetXLocation, _targetYLocation));
			s.setDirection(Vector2D.convertToDirection(_targetXLocation, _targetYLocation, s.getxLocation(), s.getyLocation()));
			s.useSkill(1);
		}
	}
	
	/**
	 * command the entire squad to fire a type 2 shot
	 * @param _targetXLocation
	 * @param _targetYLocation
	 */
	private void fire2(int _targetXLocation, int _targetYLocation){
		
		for (Ship s : this.fShips){
			//s.setDirection(Vector2D.convertToDirection(s.getxLocation(),s.getyLocation(), _targetXLocation, _targetYLocation));
			s.setDirection(Vector2D.convertToDirection(_targetXLocation, _targetYLocation, s.getxLocation(), s.getyLocation()));
			s.useSkill(2);
		}
	}
	
	/**
	 * Checks whether the members of the squad were destroyed or not
	 * @return
	 */
	public boolean isSquadAlive(){
		
		for (int i = 0; i < this.fShips.size(); i++){
			if (this.fShips.get(i).getHealth() <= 0){
				this.fShips.remove(i);
				i--;
			}
		}
		return !this.fShips.isEmpty();
	}
	
	
	
	protected boolean isInDanger(){
		
		int averageHealth = 0;
		for (Ship s: this.fShips){
			averageHealth += s.getHealth();
		}
		
		averageHealth /= this.fShips.size();
		
		return averageHealth < 25;
	}
	
	/**
	 * Returns the leader of the squad
	 * @return
	 */
	
	protected Ship getLeader(){
		return this.fShips.get(0);
	}
	
	/**
	 * Adjust the positions of the ships so they don't go outside the battle zone edge
	 * @param _width	= the width of the battle zone
	 * @param _height	= the height of the battle zone
	 * @param _threshold	= the threshold distance that a ship needs to maintain from the edges
	 */
	
	private void balanceBattleEdgeDistace(int _width, int _height, int _threshold){
		
		for (Ship s: this.fShips){
			
			// for the x coordinate
			if (s.getxLocation() < _threshold) {
				s.addAccelerationToQueue(
						new Vector2D(
								_threshold - s.getxLocation(),
								this.fShips.get(0).getyLocation() - s.getyLocation()
						)
				);
			} else if (s.getxLocation() > _width - _threshold) {
				
				s.addAccelerationToQueue(
						new Vector2D(
								this.fShips.get(0).getxLocation() - s.getxLocation(),
								_width - _threshold - s.getxLocation()
						)
				);	
			}
			
			// for the y coordinate
			if (s.getyLocation() < _threshold) {
				s.addAccelerationToQueue(
						new Vector2D(
								this.fShips.get(0).getxLocation() - s.getxLocation(),
								_threshold - s.getxLocation()
						)
				);
			} else if (s.getxLocation() > _width - _threshold) {
				
				s.addAccelerationToQueue(
						new Vector2D(
								_height - _threshold - s.getyLocation(),
								this.fShips.get(0).getyLocation() - s.getyLocation()
						)
				);
			}
		}
	}
	
	
	/**
	 * Finds the closest ship to the target and sets it as its new head(the first in the list) if its not
	 * already
	 * @param _ships			= the list of ships that are currently in formation
	 * @param _targetXLocation 	= the x coordinate of the target
	 * @param _targetYLocation	= the y coordinate of the target
	 */
	private void changeTheHeadOfTheSquad(List<Ship> _ships, float _targetXLocation, float _targetYLocation){

		int closestShipIndex = 0;
		double smallestDistance = Double.MAX_VALUE;
		
		try {

			for (int index = 0; index < _ships.size(); index ++)
				if(smallestDistance < AILogistics.dist(_targetXLocation,_targetYLocation, _ships.get(index).getxLocation(), _ships.get(index).getyLocation())) {
					closestShipIndex = index;
					smallestDistance = AILogistics.dist(_targetXLocation,_targetYLocation, _ships.get(index).getxLocation(), _ships.get(index).getyLocation());
				}

			//swap the closest ship with the current head
			//set the variable indicating which on is the leader and isInFlock
			if (closestShipIndex != 0){
				Ship swapAux = _ships.get(0);
				swapAux.isLeader = false;
				_ships.set(0,_ships.get(closestShipIndex));
				_ships.set(closestShipIndex,swapAux);
				_ships.get(0).isLeader = true;
			}

		}catch (NullPointerException e){
			//this means the squad is empty
		}
	}

	/**
	 * Actually moves the head of the squad(which is on position 0)
	 * an rearranges the others in order to keep the standard formation
	 * with the normal distances
	 * @param _targetXLocation
	 */
	public void moveSquadTowards(int _targetXLocation, int _targetYLocation, int _width, int _height, int _threshold){
		
		//the new head of the squad will be the ship that's closest to the target
		changeTheHeadOfTheSquad(this.fShips,_targetXLocation,_targetYLocation);
		
		
		//get the  opposite normal
		
		//check if the target is not too close
		
		if (AILogistics.dist(this.getLeader().getxLocation(), this.getLeader().getyLocation(), _targetXLocation, _targetYLocation) > _threshold){
			//set the destination for the new head of the squad
			this.fShips.get(0).addAccelerationToQueue(
					new Vector2D(
							_targetXLocation- this.fShips.get(0).getxLocation(),
							_targetYLocation-this.fShips.get(0).getyLocation()
					)
			);
		} /*else {
			// make the ship orbit around the target
			
			//get the normal vector in order to make is wave
			Vector2D normalVelocity = new Vector2D(this.getLeader().getyLocation()-_targetYLocation + _targetXLocation, _targetXLocation - this.getLeader().getxLocation() + _targetYLocation);
			
			//reduce the vector
			normalVelocity = normalVelocity.div(20);
			
			this.fShips.get(0).addAccelerationToQueue(normalVelocity);
			this.fShips.get(0).addAccelerationToQueue(normalVelocity.div(-1));
			this.fShips.get(0).addAccelerationToQueue(normalVelocity.div(-1));
		}*/
		
		
		
		// set the leader as destination of the other members of the squad
		
		for (Ship s : this.fShips.subList(1, this.fShips.size())){
			
			if (AILogistics.dist(s.getxLocation(), s.getyLocation(), _targetXLocation, _targetYLocation) > _threshold){
			
				s.addAccelerationToQueue(
						new Vector2D(
								this.fShips.get(0).getxLocation() - s.getxLocation(),
								this.fShips.get(0).getyLocation() - s.getyLocation()
						)
				);
			}
		}
		
		// adjust the ships so they don't get to close to the edges of the battle zone
		//this.balanceBattleEdgeDistace(_width, _height, _threshold);
	}
	
	/**
	 * Teleport the squad to a given location
	 * @param _targetXLocation = the x coordinate of the given location
	 * @param _targetYLocation = the y coordinate of the given location
	 */
	
	public void teleportTo(int _targetXLocation, int _targetYLocation){
		
		// teleport the leader to the given location
		
		this.getLeader().setDirection(Vector2D.convertToDirection(this.getLeader().getxLocation(),this.getLeader().getyLocation(),_targetXLocation, _targetYLocation));
		this.getLeader().useSkill(3);
		
		
		// teleport the other ships around the leader
		
		Random rand = new Random();
		
		for (int i = 1; i < this.fShips.size(); i ++) {
			
			int newXLocation = (SquadFormation.standardShipToShipDistance + rand.nextInt(5)) * (rand.nextInt()%2 == 0? 1 : -1) + _targetXLocation;
			int newYLocation = (SquadFormation.standardShipToShipDistance + rand.nextInt(5)) * (rand.nextInt()%2 == 0? 1 : -1) + _targetYLocation;
			
			//ask Danannan how to use teleport skill
			this.fShips.get(i).setDirection(Vector2D.convertToDirection(this.fShips.get(i).getxLocation(),this.fShips.get(i).getyLocation(),newXLocation, newYLocation));
			this.fShips.get(i).useSkill(3);
			
		}
	}
}
