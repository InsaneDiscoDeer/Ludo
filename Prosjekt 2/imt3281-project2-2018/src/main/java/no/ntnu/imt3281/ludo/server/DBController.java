package no.ntnu.imt3281.ludo.server;

import java.sql.Connection;  
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class DBController
{
	private boolean connection;
	private Connection connect;
	
	public DBController()
	{
		connection = false;
	}
	
	public void establishConnection()
	{
    	try
    	{
    		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/ludodb", "root", "");
    		System.out.println("Connection Successful");
    		connection = true;
    	}
    	catch (SQLException e)
    	{
    		System.out.println("error: "+e.getMessage());
		}
    }
	
	public void insertUser(final String username, final String password)
	{
    	try(Connection a = DriverManager.getConnection("jdbc:mysql://localhost:3306/ludodb", "root", ""))
    	{
    		//Insert user into user table
    		String query = "INSERT INTO user ("+"id,"+" name,"+"pass) VALUES (null, ?, ?)";
    		PreparedStatement st = a.prepareStatement(query);
    		st.setString(1, username);
    		st.setString(2, password);
    		st.executeUpdate();
    		
    		//get userID from user table
    		String query2 = "SELECT id FROM user WHERE name=?";
    		PreparedStatement st2 = a.prepareStatement(query2);
    		st2.setString(1, username);
    		ResultSet rs = st2.executeQuery();
    		rs.next();
    		
    		//Insert user into Mostwins table
    		String query3 = "INSERT INTO mostwins ("+"id,"+" userId,"+"numberOfWins) VALUES (null, ?, ?)";
    		PreparedStatement st3 = a.prepareStatement(query3);
    		st3.setInt(1, rs.getInt(1));
    		st3.setInt(2, 0);
    		st3.executeUpdate();
    		
    		//Insert user into Mostplayed table
    		String query4 = "INSERT INTO mostplayed ("+"id,"+" userId,"+"numberOfGames) VALUES (null, ?, ?)";
    		PreparedStatement st4 = a.prepareStatement(query4);
    		st4.setInt(1, rs.getInt(1));
    		st4.setInt(2, 0);
    		st4.executeUpdate();
    		
    		System.out.println("USER ADDED SUCCESSFULLY");
    	} 
    	catch (SQLException e)
    	{
    		System.out.println("user could not be added "+e.getMessage());
		}
    }
	
	public boolean checkIfUserExist(String username)
	{
		try(Connection a = DriverManager.getConnection("jdbc:mysql://localhost:3306/ludodb", "root", ""))
    	{    		
    		//get userID from user table
    		String query = "SELECT name FROM user WHERE name=?";
    		PreparedStatement st = a.prepareStatement(query);
    		st.setString(1, username);
    		ResultSet rs = st.executeQuery();
    		rs.next();
    		
    		if(username.equals(rs.getString(1)))
    		{
    			System.out.println("USER EXISTS");
    			return true; 
    		}
    		return false;
    	} 
    	catch (SQLException e)
    	{
    		System.out.println("user could not be found "+e.getMessage());
    		return false;
		}
	}

}
