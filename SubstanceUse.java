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

	// ========================   QUERY FUNCTIONS   ========================
	//
	// (to be called by pressing buttons in our GUI)

	// GET TABLES
	public static void viewConsumes() {
		String query = "select * from Consumes";
		execute(query);
	}
	public static void viewIncident() {
		String query = "select * from Incident";
		execute(query);
	}
	public static void viewNeighbourhood() {
		String query = "select * from Neighbourhood";
		execute(query);
	}
	public static void viewPatient() {
		String query = "select * from Patient";
		execute(query);
	}
	public static void viewSubstance() {
		String query = "select * from Substance";
		execute(query);
	}
	public static void viewWard() {
		String query = "select * from Ward";
		execute(query);
	}

	// QUERIES

	// 1. which Wards have the most Narcan administrations?
	public static void narcanByWard() {
		String query = "select WardName, count(\"Narcan Administrations\") from Patient p, Neighbourhood n" + 
						" where p.\"Neighbourhood ID\" = n.NeighbourhoodID" + 
						" group by wardName";
		execute(query);
	}

	// 2. Which age groups have the most narcan incidents?
	public static void narcanByAge() {
		String query = "select age, count(\"Narcan Administrations\") as numNarcan from Patient p" +
					" group by age";
		execute(query);
	}

	// 3. Biggest drug busts by number of people (maybe they were at a party)
	public static void parties() {
		String query = "select \"Incident Number\", count(\"Patient Number\") as numPatients from Patient p" +
		" group by \"Incident Number\"" +
		" having numPatients > 2" + 
		" order by numPatients desc";
		execute(query);
	}
	// 4. Which substances were at the parties people get wasted in? Most common? 
	public static void partySubstances() {
		String query = "select distinct \"Incident Number\", substance from Consumes" +
		"where \"Incident Number\" in(" +
			" select \"Incident Number\" from Patient p" +
			" group by \"Incident Number\"" +
			" having  count(\"Patient Number\")> 2" +
			" order by  count(\"Patient Number\") desc)" +
		" order by \"Incident Number\"";
		execute(query);
	}
	// 5. How many parties did each ward have?
	public static void partiesByWard() {
		String query = "select distinct wardName, count (distinct\"Incident Number\") as NumParties from patient join neighbourhood" +
		" on patient.\"neighbourhood ID\" = neighbourhood.neighbourhoodID" +
		" where \"Incident Number\" in(" +
			" select \"Incident Number\" from Patient p" +
			" group by \"Incident Number\"" +
			" having  count(\"Patient Number\")> 2" +
			" order by  count(\"Patient Number\") desc)" +
		" group by wardName" +
		" order by NumParties desc";
		execute(query);
	}
	// 6. Most problematic areas for Substance X
	public static void neighbourhoodForSubstance(String substance) {
		String query = "select distinct \"Neighbourhood\", substance, count(substance) as yup from patient natural join consumes join Neighbourhood" +
		" on \"Neighbourhood ID\" = NeighbourhoodID" +
		" where substance == '" + substance + "'" +
		" group by \"Neighbourhood ID\", substance" +
		" order by yup desc, Neighbourhood" +
		"limit 5";
		execute(query);
	}
	// 7. What are the most common age/substance combinations?
	public static void ageSubstanceCombination() {
		String query = "select distinct age, substance, count(substance) as yup from patient natural join consumes" +
		" group by age, substance" +
		" order by yup desc" +
		" limit 10";
		execute(query);
	}
	// 8. What are the most common age groups for substance X?
	public static void ageForSubstance(String substance) {
		String query = "select distinct age, substance, count(substance) as yup from patient natural join consumes" +
		" where substance = '" + substance + "'" +
		" group by age, substance" +
		" order by yup desc" +
		"limit 5";
		execute(query);
	}
	// 9. Which holidays have the highest prevalence of substance use?
	public static void holidays() {
		String query = "select \"Valentine's day\" as Date, count(*) from patient where \"Dispatch Date\" like '02/14%' union select \"Christmas Eve\" as Date, count(*) from patient where \"Dispatch Date\" like '12/24%'" +
		" union select \"Christmas Day\" as Date, count(*) from patient where \"Dispatch Date\" like '12/25%' union select \"Boxing Day\" as Date, count(*) from patient where \"Dispatch Date\" like '12/26%'" +
		" union select \"Halloween\" as Date, count(*) from patient where \"Dispatch Date\" like '08/31%' union select \"New Years Eve\" as Date, count(*) from patient where \"Dispatch Date\" like '12/31%'" +
		" union select \"New Years Eve\" as Date, count(*) from patient where \"Dispatch Date\" like '12/31%' union select \"New Years Day\" as Date, count(*) from patient where \"Dispatch Date\" like '01/01%'" +
		" union select \"New Years Day\" as Date, count(*) from patient where \"Dispatch Date\" like '01/01%'" +
		" order by count(*) desc";
		execute(query);
	}
	// 10. What hour of the day is the most common for substance use?
	public static void hours() {
		String query = "select \"1 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________01:______AM' union select \"2 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________02:______AM'" +
		" select \"3 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________03:______AM' union select \"4 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________04:______AM'" +
		" select \"5 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________05:______AM' union select \"6 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________06:______AM'" +
		" select \"7 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________07:______AM' union select \"8 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________08:______AM'" +
		" select \"9 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________09:______AM' union select \"10 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________10:______AM'" +
		" select \"11 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________11:______AM' union select \"12 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________12:______AM'" +
		" select \"1 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________01:______PM' union select \"2 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________01:______PM'" +
		" select \"3 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________03:______PM' union select \"4 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________04:______PM'" +
		" select \"5 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________05:______PM' union select \"6 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________06:______PM'" +
		" select \"7 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________07:______PM' union select \"8 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________08:______PM'" +
		" select \"9 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________09:______PM' union select \"10 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________10:______PM'" +
		" select \"11 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________11:______PM' union select \"12 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________12:______PM'" +
		" order by count(*) desc";
		execute(query);
	}
}