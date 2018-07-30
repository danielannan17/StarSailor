package database;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 *
 */

public class DBConnectTest {
	DBConnect db;
	long value;
	
	@Test
	public void DBTest(){
		long PSeed = 302441334;
		PSeed = PSeed * 100 + 50;
		db.dbActions("UPDATE SAccounts SET Crystal = 15 WHERE Name = 'quaderine';");
		assertEquals(db.executeQueryS("Select Name FROM SAccounts WHERE Crystal = 15;"),"quaderine");
		assertEquals(db.executeQueryS("Select Name FROM SAccounts WHERE Crystal = 99;"),"");
		assertEquals(db.executeQueryL("Select PSeed FROM Planets WHERE Name = 'quaderine';"),PSeed);
		assertEquals(db.executeQueryL("Select PSeed FROM Planets WHERE Name ='NOENTRY';"),0);
		assertEquals(db.executeQueryInt("Select Metal FROM Resources WHERE Name = 'quaderine';", "Metal"),1);
		assertEquals(db.executeQueryInt("Select Metal FROM Resources WHERE Name = 'NOENTRY';", "Metal"),0);
		
		ArrayList<Long> valuesFromDB = new ArrayList<>();
		ArrayList<Integer> valuesFromDBInt = new ArrayList<>();
		valuesFromDB = db.executeQueryIntA("Select Uran FROM Planets WHERE Name = 'Adi'", "Uran");
		valuesFromDBInt = db.executeQueryIntAI("Select Metal FROM Planets WHERE Name = 'Adi'", "Metal");
		for(int i=0; i< valuesFromDB.size(); i++){
			value += valuesFromDB.get(i);
		}
		assertEquals(value,69387);
		
		value = 0;
		for(int i=0; i< valuesFromDBInt.size(); i++){
			value += valuesFromDBInt.get(i);
		}
		assertEquals(value,68423);
	}

	@Before
	public void setUp() throws Exception {
		db = new DBConnect();
	}

}
