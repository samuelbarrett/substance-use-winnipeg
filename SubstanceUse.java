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

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SubstanceUse {
	private static Connection connect = null;

	// main
	public static void main(String[] args) {
		// instantiate SQLite JDBC for database connection
		gui();
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

	// 

	// execute the query provided
	public static ResultSet execute(String query) {
		ResultSet result = null;
		try {
			Statement statement = connect.createStatement();
			result = statement.executeQuery(query);
		} catch(SQLException e ) {
			System.out.println(e.getMessage());
		} 
		return result;
	}
 
	// setup the GUI using Swing
	public static void gui() {
		JFrame frame = new JFrame("Substance Use");
		JPanel panel = new JPanel();
		frame.setVisible(true);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Substance Use");
		frame.pack();
		frame.setSize(800, 500);

	}

	// which Wards have the most Narcan administrations?
	public static void narcanByWard() {
		String query = "select WardName, count(\"Narcan Administrations\") from Patient p, Neighbourhood n" + 
						" where p.\"Neighbourhood ID\" = n.NeighbourhoodID" + 
						" group by wardName";
		execute(query);
	}
}