// SubstanceUse.java
// 
// December 2021
// COMP 3380
// Project Team 6

// notes: this will be the class with jdbc and methods interacting with our sqlite file.
// 		we will create an instance of this class and call its methods in another java file which will contain our main method.
//		that file will also have our UI (or at least calls the UI).

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SubstanceUse {

	// constructor
	public SubstanceUse() {

	}

	// main
	public static void main(String[] args) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		Connection connect = null;
		try {
			// connect to the database file (substance-use-wpg.sqlite)
			connect = DriverManager.getConnection("jdbc:sqlite:/substance-use-wpg.sqlite");

			// everything else goes here (loop basically? until we exit the program?)

		} 
		catch(SQLException e) {
			System.out.println(e.getMessage());
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




}