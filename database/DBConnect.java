package database;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 
 * 
 *
 */
public class DBConnect {
		// JDBC driver name and database URL
	   private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   private static final String DB_URL = "jdbc:mysql://sql8.freesqldatabase.com:3306/sql8163674?autoReconnect=true&useSSL=false";

	   //  Database credentials
	   private static final String USER = "sql8163674";
	   private static final String PASS = "e14NiztztG";
	   
	   private Connection conn;
	   private Statement stmt;
	   private ResultSet rs;
	   
	public DBConnect(){
		connect();
	}
	
	private void connect(){
		conn = null;
		stmt = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      System.out.println("Connected database successfully...");
		      
		      //STEP 4: Execute a query
		      System.out.println("Inserting records into the table...");
		      stmt = conn.createStatement();

		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }
	}
	
	/**
	 * Actions on sql database
	 * @param SQL
	 */
	public void dbActions(String SQL){
		try {
			stmt.executeUpdate(SQL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Execute queries for sql database
	 * @param SQL
	 * @return
	 */
	public String executeQueryS(String SQL){
		try {
			rs = stmt.executeQuery(SQL);
			if(rs.next()){
				return rs.getString("Name");
			}
			return "";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public long executeQueryL(String SQL){
		try {
			rs = stmt.executeQuery(SQL);
			if(rs.next()){
				return rs.getLong("PSeed");
			}
			return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public long executeQueryInt(String SQL,String type){
		try {
			rs = stmt.executeQuery(SQL);
			if(rs.next()){
				return rs.getLong(type);
			}
			return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public ArrayList<Long> executeQueryIntA(String SQL,String type){
		ArrayList<Long> saveInfo = new ArrayList<>();
		try {
			rs = stmt.executeQuery(SQL);
			while(rs.next()){
				saveInfo.add(rs.getLong(type));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return saveInfo;
	}
	
	public ArrayList<Integer> executeQueryIntAI(String SQL,String type){
		ArrayList<Integer> saveInfo = new ArrayList<>();
		try {
			rs = stmt.executeQuery(SQL);
			while(rs.next()){
				saveInfo.add(rs.getInt(type));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return saveInfo;
	}
}
