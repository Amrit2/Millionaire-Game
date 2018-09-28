/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameproject;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Amrit
 */
public class LeaderBoardDatabase {
    public static Connection conn;
    public static String url = "jdbc:derby:LeaderBoard; create=true";
    public static String username = "game";
    public static String pass = "game";
    public int id = 0;
    public Statement statement;
    public String table = "LeaderBoard";
 
    public LeaderBoardDatabase(){
        establishConnection();
    }
    
    public void establishConnection(){
        try{
            conn = DriverManager.getConnection(url, username, pass);
            System.out.println("Database Connected(" + url + ").");
            statement = conn.createStatement();
            DatabaseMetaData data = conn.getMetaData();
            ResultSet rs = data.getTables(null, "GAME", "LEADERBOARD", null);
            if (!rs.next()){
                createTable();
            }
            
        }catch (SQLException ex){
            System.err.println("SQL Exception: " + ex.getMessage());
        }
    }
    
    public void createTable(){
            try {
                    
                String sqlCreateTable = "CREATE TABLE " + table + " (ID INT," 
                                    + "Name VARCHAR (100), Points INT)";
                statement.executeUpdate(sqlCreateTable); 
            }catch(SQLException ex){
                System.out.println("SQL Exception " + ex.getMessage());
            }
    }
    
    public void addToDatabase(String name, int money){
        try{
            System.out.println("Adding to database...");
            String insertData = "INSERT INTO " + table + " VALUES (" + id  + ", '"+ name + "', " + money + ")";
            statement.executeUpdate(insertData);
        }catch (SQLException ex){
            System.out.println("SQL Exception " + ex.getMessage());
        }
        id++;
    }
    
    public void getDatabase(){
        
    }
}
