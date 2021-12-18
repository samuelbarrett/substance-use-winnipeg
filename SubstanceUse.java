// SubstanceUse.java
// 
// December 2021
// COMP 3380
// Project Team 6

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class SubstanceUse {
	private static Connection connect = null;

	// main
	public static void main(String[] args) {
		// instantiate SQLite JDBC for database connection
		try {
			Class.forName("org.sqlite.JDBC");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		// connect to the database file
		try {
			connect = DriverManager.getConnection("jdbc:sqlite:substance-use-wpg.sqlite");
			System.out.println("Connected to substance-use-wpg.sqlite");
			// everything else goes here (loop basically? until we exit the program?)

		} 
		catch(SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("CONNECTION FAILED");
		} 
		finally {
			// we close the connection if applicable
			try {
				if(connect != null) {
					connect.close();
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	// which Wards have the most Narcan administrations?
	public static ResultSet narcanByWard() {
		String query = "select WardName, count(\"Narcan Administrations\") from Patient p, Neighbourhood n" + 
						" where p.\"Neighbourhood ID\" = n.NeighbourhoodID" + 
						" group by wardName";
		ResultSet result = null;
		try {
			Statement statement = connect.createStatement();
			result = statement.executeQuery(query);
		} catch(SQLException e ) {
			System.out.println(e.getMessage());
		} 
		return result;
	}
}