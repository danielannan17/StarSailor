package gameLogic;

/**
 * 
 * 
 *
 */

public class Resources {

	private String type;
	private PlayerStats playerStats;
	private int level,increment;
	private long id;
	private int amountPerHour;
	private int[] LVL,storageAvailablePerLevel,populationAvailablePerLevelofRecruitmentAgency, waterAvailablePerLevel, moneyPerLevel;
	
	public Resources(String _type,int level, long id, int amountPerHour){
		this.type = _type;
		this.level = level;
		this.id = id;
		this.amountPerHour = amountPerHour;
		LVL = new int[] {45,53,62,71,83,96,111,129,150,176,204,237,276,321,374,434,506,587,683,800};
		storageAvailablePerLevel = new int[] {1000,1230,1550,1860,2285,2810,3454,4247,5222,6420,7883, 9715, 11942, 14670, 18037, 22177, 27266, 33573, 41017, 50000};
		populationAvailablePerLevelofRecruitmentAgency = new int[] {240, 281, 329, 386, 452, 530, 622, 729, 854, 1002, 1174, 1376, 1613, 1891, 2216, 2598, 3045, 3569, 4183, 5000};
		waterAvailablePerLevel = new int[] {53,62,71,83,96,111,129,150,176,204,237,276,321,374,434,506,587,673,785,900};
		moneyPerLevel = new int[] {90,106,124,142,166,192,222,258,300,351,408,474,532,642,748,868,1012,1174,1366,1600};
	}
	
	/**
	 * Upgrade the resource
	 * @param level the level you want to reach
	 */
	public void upgradeResources(int level){
		setAmountPerHour("" + level);
		this.level = level;
	}
	
	/**
	 * Set how many resource the player is going to receive according on the level the building is
	 * @param level the level of the resource
	 */
	public void setAmountPerHour(String level){
		switch(this.type){
		case "money":
			switch(level){
			case "1" : this.amountPerHour = moneyPerLevel[0]; break;
			case "2" : this.amountPerHour = moneyPerLevel[1]; break;
			case "3" : this.amountPerHour = moneyPerLevel[2]; break;
			case "4" : this.amountPerHour = moneyPerLevel[3]; break;
			case "5" : this.amountPerHour = moneyPerLevel[4]; break;
			case "6" : this.amountPerHour = moneyPerLevel[5]; break;
			case "7" : this.amountPerHour = moneyPerLevel[6]; break;
			case "8" : this.amountPerHour = moneyPerLevel[7]; break;
			case "9" : this.amountPerHour = moneyPerLevel[8]; break;
			case "10" : this.amountPerHour = moneyPerLevel[9]; break;
			case "11" : this.amountPerHour = moneyPerLevel[10]; break;
			case "12" : this.amountPerHour = moneyPerLevel[11]; break;
			case "13" : this.amountPerHour = moneyPerLevel[12]; break;
			case "14" : this.amountPerHour = moneyPerLevel[13]; break;
			case "15" : this.amountPerHour = moneyPerLevel[14]; break;
			case "16" : this.amountPerHour = moneyPerLevel[15]; break;
			case "17" : this.amountPerHour = moneyPerLevel[16]; break;
			case "18" : this.amountPerHour = moneyPerLevel[17]; break;
			case "19" : this.amountPerHour = moneyPerLevel[18]; break;
			case "20" : this.amountPerHour = moneyPerLevel[19]; break;
			}
		case "water":
			if(this.type == "money") break;
			switch(level){
			case "1" : this.amountPerHour = waterAvailablePerLevel[0]; break;
			case "2" : this.amountPerHour = waterAvailablePerLevel[1]; break;
			case "3" : this.amountPerHour = waterAvailablePerLevel[2]; break;
			case "4" : this.amountPerHour = waterAvailablePerLevel[3]; break;
			case "5" : this.amountPerHour = waterAvailablePerLevel[4]; break;
			case "6" : this.amountPerHour = waterAvailablePerLevel[5]; break;
			case "7" : this.amountPerHour = waterAvailablePerLevel[6]; break;
			case "8" : this.amountPerHour = waterAvailablePerLevel[7]; break;
			case "9" : this.amountPerHour = waterAvailablePerLevel[8]; break;
			case "10" : this.amountPerHour = waterAvailablePerLevel[9]; break;
			case "11" : this.amountPerHour = waterAvailablePerLevel[10]; break;
			case "12" : this.amountPerHour = waterAvailablePerLevel[11]; break;
			case "13" : this.amountPerHour = waterAvailablePerLevel[12]; break;
			case "14" : this.amountPerHour = waterAvailablePerLevel[13]; break;
			case "15" : this.amountPerHour = waterAvailablePerLevel[14]; break;
			case "16" : this.amountPerHour = waterAvailablePerLevel[15]; break;
			case "17" : this.amountPerHour = waterAvailablePerLevel[16]; break;
			case "18" : this.amountPerHour = waterAvailablePerLevel[17]; break;
			case "19" : this.amountPerHour = waterAvailablePerLevel[18]; break;
			case "20" : this.amountPerHour = waterAvailablePerLevel[19]; break;
			}
		case "population":
			if(this.type == "water" || this.type == "money") break;
			switch(level){
			case "1" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[0]; break;
			case "2" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[1]; break;
			case "3" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[2]; break;
			case "4" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[3]; break;
			case "5" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[4]; break;
			case "6" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[5]; break;
			case "7" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[6]; break;
			case "8" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[7]; break;
			case "9" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[8]; break;
			case "10" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[9]; break;
			case "11" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[10]; break;
			case "12" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[11]; break;
			case "13" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[12]; break;
			case "14" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[13]; break;
			case "15" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[14]; break;
			case "16" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[15]; break;
			case "17" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[16]; break;
			case "18" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[17]; break;
			case "19" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[18]; break;
			case "20" : this.amountPerHour = populationAvailablePerLevelofRecruitmentAgency[19]; break;
			}
		case "storage": 
			if(this.type == "population" || this.type == "water" || this.type == "money") break;
			switch(level){
			case "1" : this.amountPerHour = storageAvailablePerLevel[0]; break;
			case "2" : this.amountPerHour = storageAvailablePerLevel[1]; break;
			case "3" : this.amountPerHour = storageAvailablePerLevel[2]; break;
			case "4" : this.amountPerHour = storageAvailablePerLevel[3]; break;
			case "5" : this.amountPerHour = storageAvailablePerLevel[4]; break;
			case "6" : this.amountPerHour = storageAvailablePerLevel[5]; break;
			case "7" : this.amountPerHour = storageAvailablePerLevel[6]; break;
			case "8" : this.amountPerHour = storageAvailablePerLevel[7]; break;
			case "9" : this.amountPerHour = storageAvailablePerLevel[8]; break;
			case "10" : this.amountPerHour = storageAvailablePerLevel[9]; break;
			case "11" : this.amountPerHour = storageAvailablePerLevel[10]; break;
			case "12" : this.amountPerHour = storageAvailablePerLevel[11]; break;
			case "13" : this.amountPerHour = storageAvailablePerLevel[12]; break;
			case "14" : this.amountPerHour = storageAvailablePerLevel[13]; break;
			case "15" : this.amountPerHour = storageAvailablePerLevel[14]; break;
			case "16" : this.amountPerHour = storageAvailablePerLevel[15]; break;
			case "17" : this.amountPerHour = storageAvailablePerLevel[16]; break;
			case "18" : this.amountPerHour = storageAvailablePerLevel[17]; break;
			case "19" : this.amountPerHour = storageAvailablePerLevel[18]; break;
			case "20" : this.amountPerHour = storageAvailablePerLevel[19]; break;
			}
		default:
			if(this.type == "storage" || this.type == "population" || this.type == "water" || this.type == "money") break;
			switch(level){
			case "1" : this.amountPerHour = LVL[0]; break;
			case "2" : this.amountPerHour = LVL[1]; break;
			case "3" : this.amountPerHour = LVL[2]; break;
			case "4" : this.amountPerHour = LVL[3]; break;
			case "5" : this.amountPerHour = LVL[4]; break;
			case "6" : this.amountPerHour = LVL[5]; break;
			case "7" : this.amountPerHour = LVL[6]; break;
			case "8" : this.amountPerHour = LVL[7]; break;
			case "9" : this.amountPerHour = LVL[8]; break;
			case "10" : this.amountPerHour = LVL[9]; break;
			case "11" : this.amountPerHour = LVL[10]; break;
			case "12" : this.amountPerHour = LVL[11]; break;
			case "13" : this.amountPerHour = LVL[12]; break;
			case "14" : this.amountPerHour = LVL[13]; break;
			case "15" : this.amountPerHour = LVL[14]; break;
			case "16" : this.amountPerHour = LVL[15]; break;
			case "17" : this.amountPerHour = LVL[16]; break;
			case "18" : this.amountPerHour = LVL[17]; break;
			case "19" : this.amountPerHour = LVL[18]; break;
			case "20" : this.amountPerHour = LVL[19]; break;
			}
		}
	}
	
	/**
	 * Get the type of the resource
	 * @return the type of the resource
	 */
	public String getType(){
		return this.type;
	}

	/**
	 * Get the level of the resource
	 * @return the level of the resource
	 */
	public int getLevel(){
		return this.level;
	}
	
	/**
	 * Get the ID of the resource
	 * @return the ID of the resource
	 */
	public long getId(){
		return this.id;
	}
	
	/**
	 * Get the amount a certain resource is producing/giving to the player
	 * @return the amount a certain resource is producing/giving to the player
	 */
	public long getAmountPerHour(){
		return this.amountPerHour;
	}

	public void setLevel(int level) {
		this.level = level;
		
	}
}
