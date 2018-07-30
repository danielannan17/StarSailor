package gameAI.battle;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class AILogistics {

		
	/**
	 * Get the distance between two given points
	 * @param x1 = the x coordinate of the first point
	 * @param y1 = the y coordinate of the first point
	 * @param x2 = the x coordinate of the first point
	 * @param y2 = the y coordinate of the first point
	 * @return
	 */
	
	public static Double dist(double x1,double y1,double x2,double y2) {
		double p1 = (y2-y1)*(y2-y1);
		double p2 = (x2-x1)*(x2-x1);
		return Math.sqrt(p1+p2);
	}

	/**
	 * Detects the nearby objects in the space(a circle) surrounding the given location on a given radius
	 * @param xLocation = the x coordinate origin of the space to be checked
	 * @param yLocation = the y coordinate origin of the space to be checked
	 * @param radius    = the radius of the space to be checked
	 * @param battleAi	= the BattleAI of the ships in the team that requests the information
	 * @returns a list containing the objects around the given location
	 */
	public static List<ObjectDescription> checkSurroundingsOf(float xLocation, float yLocation, double radius, BattleAI battleAi){
		List<ObjectDescription> closeObjects = new LinkedList<>();

		for(ObjectDescription o : battleAi.getEntitiesOfTheEnvironment()){
			if(dist(xLocation,yLocation,o.getXLocation(),o.getYLocation()) < radius)
				closeObjects.add(o);
		}
		return closeObjects;
	}

	/**
	 * Returns a list of the obstacles in the space surrounding a given location
	 * @param xLocation = the x coordinate of the given point
	 * @param yLocation = the y coordinate of the given point
	 * @param radius 	= the radius of the space to be checked
	 * @param battleAI	= the BattleAI of the ships in the team that requests the information
	 * @return
	 */
	public static List<ObstacleDescription> getSurroundingObstacles(float xLocation, float yLocation, double radius, BattleAI battleAI){
		return battleAI.getTheObstaclesOfTheEnvironment().stream().filter(obs ->
				dist(obs.getXLocation(),obs.getYLocation(),xLocation,yLocation) <= radius).collect(Collectors.toList());
	}

	/**
	 * Returns a list of the projectiles that surround the given location
	 * @param xLocation = the x coordinate of the given location
	 * @param yLocation	= the y coordinate of the given location
	 * @param radius	= the radius of the space to be checked
	 * @param battleAI	= the BattleAI of the ships in the team that requests the information
	 * @return List<ProjectileDescription> surroundingProjectiles
	 */

	public static List<ProjectileDescription> getSurroundingProjectiles(float xLocation, float yLocation, double radius, BattleAI battleAI){

		LinkedList<ProjectileDescription> surroundingProjectiles = new LinkedList<>();

		for (ProjectileDescription projectile : battleAI.getTheProjectilesOfTheEnvironment()){
			if(dist(projectile.getXLocation(),projectile.getYLocation(),xLocation,yLocation) <= radius)
				surroundingProjectiles.add(projectile);
		}

		return surroundingProjectiles;
	}

	/**
	 * Returns a list of the enemy ships which are present in the space surrounding a given location
	 * @param xLocation = the x coordinate of the given point
	 * @param yLocation = the y coordinate of the given point
	 * @param radius 	= the radius of the space to be checked
	 * @param battleAI	= the BattleAI of the ships in the team that requests the information
	 * @return
	 */
	public static List<ShipDescription> getSurroundingEnemyShips(float xLocation, float yLocation, double radius, BattleAI battleAI){
		
		if(radius >= 0){
			return battleAI.getTheEnemyShipsOfTheEnvironment().stream().filter(eShip ->
					dist(eShip.getXLocation(),eShip.getYLocation(),xLocation,yLocation) < radius).collect(Collectors.toList());
		} else{
			
			List<ShipDescription> sl = battleAI.getTheEnemyShipsOfTheEnvironment().stream().collect(Collectors.toList());
			/*for (ShipDescription sd: sl){
				System.out.println("enemy x = " + sd.getXLocation() + " enemy y = " + sd.getYLocation());
			}*/
			return battleAI.getTheEnemyShipsOfTheEnvironment().stream().collect(Collectors.toList());
		}
	}
	
	/**
	 * Scout the surroundings of a given location and get the closest enemy ship
	 * @param xLocation = the x coordinate of the given point
	 * @param yLocation = the y coordinate of the given point
	 * @param radius 	= the radius of the space to be checked
	 * @param battleAI	= the BattleAI of the ships in the team that requests the information
	 * @return
	 */
	
	public static ShipDescription getCLosestSurroundingEnemyShip(float xLocation, float yLocation, double radius, BattleAI battleAI){
		
			
		List<ShipDescription> enemyShips = getSurroundingEnemyShips(xLocation, yLocation, radius,battleAI);
		
		if(enemyShips.isEmpty())
			return new ShipDescription();
		
		ShipDescription closestShip = enemyShips.get(0);
		
		for (ShipDescription sd :  enemyShips.subList(1, enemyShips.size())){
			if (dist(sd.getXLocation(),sd.getYLocation(),(double)xLocation,(double)yLocation) < dist(closestShip.getXLocation(),closestShip.getYLocation(),(double)xLocation,(double)yLocation)){}
		}	
		return closestShip;
	}
	
	/**
	 * From the perspective of the object at location 1 get the closest enemy ship to both locations
	 * @param x1Location	= the observer's x coordinate
	 * @param y1Location	= the observer's y coordinate
	 * @param x2Location	= the x coordinate of the observed location
	 * @param y2Location	= the y coordinate of the observed location
	 * @param radius		= the checking radius
	 * @param battleAI		= the AI of the team that is using the method 
	 * @return
	 */
	
	public static ShipDescription getClosestSurroundingEnemyShipFromTwoLocations(float x1Location, float y1Location, float x2Location, float y2Location, double radius, BattleAI battleAI){
		
		List<ShipDescription> enemyShips = getSurroundingEnemyShips(x2Location, y2Location, radius,battleAI);
		
		if(enemyShips.isEmpty())
			return new ShipDescription();
		
		ShipDescription closestShip = enemyShips.get(0);
		
		for (ShipDescription sd :  enemyShips.subList(1, enemyShips.size())){
			if (dist(sd.getXLocation(),sd.getYLocation(),(double)x1Location,(double)y1Location) + dist(sd.getXLocation(),sd.getYLocation(),(double)x2Location,(double)y2Location) 
					< dist(closestShip.getXLocation(),closestShip.getYLocation(),(double)x1Location,(double)y1Location) + dist(sd.getXLocation(),sd.getYLocation(),(double)x2Location,(double)y2Location)){
				closestShip = sd;
			}
		}
		return closestShip;
	}
	
	/**
	 * Scout the environment and return the closest allied squad to the location of the leader of the given squad
	 * @param squad		= the given squad
	 * @param battleAI	= the AI of the team the squad belongs to
	 * @return closestSquad
	 */
	
	public static SquadAI getClosestAlliedShip(SquadAI squad, BattleAI battleAI){
		
		List<SquadAI> alliedSquads = battleAI.getAlliedSquadsOfTheEnvironment();
		SquadAI closestSquad = alliedSquads.get(0);
		
		for (SquadAI s : alliedSquads.subList(1, alliedSquads.size()-1)){
			if	(!s.compareTo(squad)){
				if (dist(s.getLeader().getxLocation(), s.getLeader().getyLocation(), squad.getLeader().getxLocation(), squad.getLeader().getyLocation()) 
					< dist(s.getLeader().getxLocation(),s.getLeader().getyLocation(),closestSquad.getLeader().getxLocation(),closestSquad.getLeader().getyLocation())){
					closestSquad = s;
				}
			}
		}	
		return closestSquad;
	}
	
	/**
	 * Return a sorted version of the given list of squads using the distance to the given location as the main criteria
	 * @param xLocation	= the x coord of the given location
	 * @param yLocation	= the y coord of the given location
	 * @param squads	= the list of squads
	 * @return
	 */
	
	public static List<SquadAI> sortSquadsAfterTheDistanceToAGivenLocation(double xLocation, double yLocation, List<SquadAI> squads){
		
		squads.sort(
				new Comparator<SquadAI>(){
					@Override
					public int compare(final SquadAI squad1, final SquadAI squad2) {
			          return squad1.getName().compareTo(squad2.getName());
			    }
		});
		return squads;
	}		
}
