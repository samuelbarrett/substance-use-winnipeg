// SubstanceUse.java
// 
// December 2021
// COMP 3380
// Project Team 6

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.io.*;

import javax.swing.table.DefaultTableModel;


public class SubstanceUse {
	private static Connection connect = null;
	private static ProjectInterface gui = null;
	private static DefaultTableModel tableModel = null;
	private static String tableTitle = "blank.txt";
	// main
	public static void main(String[] args) {
		// instantiate SQLite JDBC for database connection
		gui = new ProjectInterface();
		gui.gui();

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
	}

	// execute the query provided, send data to buildTableModel and return its output
	public static DefaultTableModel execute(String query) {
		ResultSet result = null;
		DefaultTableModel model = null;
		try {
			Statement statement = connect.createStatement();
			result = statement.executeQuery(query);
			model = buildTableModel(result);
		} catch(SQLException e ) {
			System.out.println(e.getMessage());
		}
		tableModel = model;
		return model;
	}

	// given the result set from execute(), build a table model (for a JTable to read) and return it.
	public static DefaultTableModel buildTableModel(ResultSet data) throws SQLException {
		ResultSetMetaData metaData = data.getMetaData();
		// create the table and make it non-editable. Editing is not ideal.
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		// add the result columns to the table
		int cols = metaData.getColumnCount();
		for(int i = 0; i < cols; i++) {
			model.addColumn(metaData.getColumnLabel(i+1));
		}
		Object[] tuple;
		// go through each row, inserting into the table model
		while(data.next()) {
			tuple = new Object[cols];
			for(int j = 0; j < cols; j++) {
				tuple[j] = data.getObject(j+1);
			}
			model.addRow(tuple);
		}
		return model;
	}

	// output the most recent query results to CSV file
	public static void resultsCSV() {
		File out = new File(tableTitle + ".csv");
		if(tableModel != null) {
			try {
				FileWriter writer = new FileWriter(out);
				int cols = tableModel.getColumnCount();
				// write the column headers
				for(int i = 0; i < cols; i++) {
					writer.write(tableModel.getColumnName(i));
					if(i<cols-1) {
						writer.write(",");
					}
				}
				// write each row
				for(int j = 0; j < tableModel.getRowCount(); j++) {
					writer.write("\n");
					for(int k = 0; k < cols; k++) {
						String value = tableModel.getValueAt(j,k) == null ? "null" : tableModel.getValueAt(j,k).toString();
						writer.write(value);
						if(k < cols-1 ) {
							writer.write(",");
						}
					}
				}
				System.out.println("Saved table to " + tableTitle + ".csv in the project directory.");
				writer.close();
			} catch(IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	// ========================   QUERY FUNCTIONS   ========================
	//
	// (to be called by pressing buttons in our GUI)

	// GET TABLES
	public static DefaultTableModel viewConsumes() {
		String query = "select * from Consumes";
		tableTitle = "Consumes";
		return execute(query);
	}
	public static DefaultTableModel viewIncident() {
		String query = "select * from Incident";
		tableTitle = "Incident";
		return execute(query);
	}
	public static DefaultTableModel viewNeighbourhood() {
		String query = "select * from Neighbourhood";
		tableTitle = "Neighbourhood";
		return execute(query);
	}
	public static DefaultTableModel viewPatient() {
		String query = "select * from Patient";
		tableTitle = "Patient";
		return execute(query);
	}
	public static DefaultTableModel viewSubstance() {
		String query = "select * from Substance";
		tableTitle = "Substance";
		return execute(query);
	}
	public static DefaultTableModel viewWard() {
		String query = "select * from Ward";
		tableTitle = "Ward";
		return execute(query);
	}

	// ACTUAL QUERIES

	// 1. which Wards have the most Narcan administrations?
	public static DefaultTableModel narcanByWard() {
		tableTitle = "narcanByWard";
		String query = "select WardName, count(\"Narcan Administrations\") as numNarcan from Patient p, Neighbourhood n" + 
						" where p.\"Neighbourhood ID\" = n.NeighbourhoodID" + 
						" group by wardName" +
						" order by numNarcan";
		return execute(query);
	}

	// 2. Which age groups have the most narcan incidents?
	public static DefaultTableModel narcanByAge() {
		tableTitle = "narcanByAge";
		String query = "select age, count(\"Narcan Administrations\") as numNarcan from Patient p" +
					" group by age" +
					" order by numNarcan desc";
		return execute(query);
	}

	// 3. Biggest drug busts by number of people (maybe they were at a party)
	public static DefaultTableModel parties() {
		tableTitle = "parties";
		String query = "select \"Incident Number\", count(\"Patient Number\") as numPatients from Patient p" +
		" group by \"Incident Number\"" +
		" having numPatients > 2" + 
		" order by numPatients desc";
		return execute(query);
	}
	// 4. Which substances were at the parties people get wasted in? Most common? 
	public static DefaultTableModel partySubstances() {
		tableTitle = "Party Substances";
		String query = "select distinct \"Incident Number\", substance from Consumes c" +
		" where \"Incident Number\" in (" +
			" select \"Incident Number\" from Patient p" +
			" group by \"Incident Number\"" +
			" having  count(\"Patient Number\")> 2" +
			" order by  count(\"Patient Number\") desc)" +
		" order by \"Incident Number\"";
		return execute(query);
	}
	// 5. How many parties did each ward have?
	public static DefaultTableModel partiesByWard() {
		tableTitle = "Parties by Ward";
		String query = "select distinct wardName, count (distinct\"Incident Number\") as NumParties from patient join neighbourhood" +
		" on patient.\"neighbourhood ID\" = neighbourhood.neighbourhoodID" +
		" where \"Incident Number\" in(" +
			" select \"Incident Number\" from Patient p" +
			" group by \"Incident Number\"" +
			" having  count(\"Patient Number\")> 2" +
			" order by  count(\"Patient Number\") desc)" +
		" group by wardName" +
		" order by NumParties desc";
		return execute(query);
	}
	// 6. Most problematic areas for Substance X
	public static DefaultTableModel neighbourhoodForSubstance(String substance) {
		tableTitle = "Problematic Neighbourhoods";
		String query = "select distinct \"Neighbourhood\", count(substance) as numPatients from patient natural join consumes join Neighbourhood" +
		" on \"Neighbourhood ID\" = NeighbourhoodID" +
		" where substance == '" + substance + "'" +
		" group by \"Neighbourhood ID\", substance" +
		" order by numPatients desc, Neighbourhood" +
		" limit 5";
		return execute(query);
	}
	// 7. What are the most common age/substance combinations?
	public static DefaultTableModel ageSubstanceCombination() {
		tableTitle = "ageSubstance-combinations";
		String query = "select distinct age, substance, count(substance) as numPatients from patient natural join consumes" +
		" group by age, substance" +
		" order by numPatients desc" +
		" limit 10";
		return execute(query);
	}
	// 8. What are the most common age groups for substance X?
	public static DefaultTableModel ageForSubstance(String substance) {
		tableTitle = "Common age groups";
		String query = "select distinct age, count(substance) as numPatients from patient natural join consumes" +
		" where substance = '" + substance + "'" +
		" group by age, substance" +
		" order by numPatients desc" +
		" limit 5";
		return execute(query);
	}
	// 9. Which holidays have the highest prevalence of substance use?
	public static DefaultTableModel holidays() {
		tableTitle = "holidays";
		String query = "select \"Valentine's day\" as Date, count(*) as numPatients from patient where \"Dispatch Date\" like '02/14%' union select \"Christmas Eve\" as Date, count(*) from patient where \"Dispatch Date\" like '12/24%'" +
		" union select \"Christmas Day\" as Date, count(*) from patient where \"Dispatch Date\" like '12/25%' union select \"Boxing Day\" as Date, count(*) from patient where \"Dispatch Date\" like '12/26%'" +
		" union select \"Halloween\" as Date, count(*) from patient where \"Dispatch Date\" like '08/31%' union select \"New Years Eve\" as Date, count(*) from patient where \"Dispatch Date\" like '12/31%'" +
		" union select \"New Years Eve\" as Date, count(*) from patient where \"Dispatch Date\" like '12/31%' union select \"New Years Day\" as Date, count(*) from patient where \"Dispatch Date\" like '01/01%'" +
		" union select \"New Years Day\" as Date, count(*) from patient where \"Dispatch Date\" like '01/01%'" +
		" order by numPatients desc";
		return execute(query);
	}
	// 10. What hour of the day is the most common for substance use?
	public static DefaultTableModel hours() {
		tableTitle = "hoursOfDay";
		String query = "select \"1 AM\" as Time, count(*) as numPatients from patient where \"Dispatch Date\" like '___________01:______AM' union select \"2 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________02:______AM'" +
		" union select \"3 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________03:______AM' union select \"4 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________04:______AM'" +
		" union select \"5 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________05:______AM' union select \"6 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________06:______AM'" +
		" union select \"7 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________07:______AM' union select \"8 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________08:______AM'" +
		" union select \"9 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________09:______AM' union select \"10 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________10:______AM'" +
		" union select \"11 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________11:______AM' union select \"12 AM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________12:______AM'" +
		" union select \"1 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________01:______PM' union select \"2 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________01:______PM'" +
		" union select \"3 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________03:______PM' union select \"4 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________04:______PM'" +
		" union select \"5 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________05:______PM' union select \"6 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________06:______PM'" +
		" union select \"7 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________07:______PM' union select \"8 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________08:______PM'" +
		" union select \"9 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________09:______PM' union select \"10 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________10:______PM'" +
		" union select \"11 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________11:______PM' union select \"12 PM\" as Time, count(*) from patient where \"Dispatch Date\" like '___________12:______PM'" +
		" order by numPatients desc";
		return execute(query);
	}
}