/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class DBproject{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public DBproject(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");
			
			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}
	
	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 * 
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
	public void executeUpdate (String sql) throws SQLException { 
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the update instruction
		stmt.executeUpdate (sql);

		// close the instruction
	    stmt.close ();
	}//end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and outputs the results to
	 * standard out.
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;
		
		//iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
			    }
			    System.out.println();
			    outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i)
				System.out.print (rs.getString (i) + "\t");
			System.out.println ();
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the results as
	 * a list of records. Each record in turn is a list of attribute values
	 * 
	 * @param query the input query string
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException { 
		//creates a statement object 
		Statement stmt = this._connection.createStatement (); 
		
		//issues the query instruction 
		ResultSet rs = stmt.executeQuery (query); 
	 
		/*
		 * obtains the metadata object for the returned result set.  The metadata 
		 * contains row and column info. 
		*/ 
		ResultSetMetaData rsmd = rs.getMetaData (); 
		int numCol = rsmd.getColumnCount (); 
		int rowCount = 0; 
	 
		//iterates through the result set and saves the data returned by the query. 
		boolean outputHeader = false;
		List<List<String>> result  = new ArrayList<List<String>>(); 
		while (rs.next()){
			List<String> record = new ArrayList<String>(); 
			for (int i=1; i<=numCol; ++i) 
				record.add(rs.getString (i)); 
			result.add(record); 
		}//end while 
		stmt.close (); 
		return result; 
	}//end executeQueryAndReturnResult
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the number of results
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		if(rs.next()){
			rowCount++;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to fetch the last value from sequence. This
	 * method issues the query to the DBMS and returns the current 
	 * value of sequence used for autogenerated keys
	 * 
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	
	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement ();
		
		ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
		if (rs.next()) return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
	         // ignored.
		}//end try
	}//end cleanup

	/**
	 * The main execution method
	 * 
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
	 */
	public static void main (String[] args) {
		if (args.length != 3) {
			System.err.println (
				"Usage: " + "java [-classpath <classpath>] " + DBproject.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if
		
		DBproject esql = null;
		
		try{
			System.out.println("(1)");
			
			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}
			
			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			
			esql = new DBproject (dbname, dbport, user, "");
			
			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add Plane");
				System.out.println("2. Add Pilot");
				System.out.println("3. Add Flight");
				System.out.println("4. Add Technician");
				System.out.println("5. Book Flight");
				System.out.println("6. List number of available seats for a given flight.");
				System.out.println("7. List total number of repairs per plane in descending order");
				System.out.println("8. List total number of repairs per year in ascending order");
				System.out.println("9. Find total number of passengers with a given status");
				System.out.println("10. < EXIT");
				
				switch (readChoice()){
					case 1: AddPlane(esql); break;
					case 2: AddPilot(esql); break;
					case 3: AddFlight(esql); break;
					case 4: AddTechnician(esql); break;
					case 5: BookFlight(esql); break;
					case 6: ListNumberOfAvailableSeats(esql); break;
					case 7: ListsTotalNumberOfRepairsPerPlane(esql); break;
					case 8: ListTotalNumberOfRepairsPerYear(esql); break;
					case 9: FindPassengersCountWithStatus(esql); break;
					case 10: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if				
			}catch(Exception e){
				// ignored.
			}
		}
	}

	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice

	public static void AddPlane(DBproject esql) {//1
		int ID;
		int age;
		int seats;
		String make;
		String model;

        // Start do while Plane ID Number
		do {
			System.out.print("Enter the Plane's ID Number: ");
			try {
				ID = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		}while (true);
		
        // start do while Plane Make		
		do {
			System.out.print("Enter the Plane's Make: ");
			try {
				make = in.readLine();
				if(make.length() <= 0 || make.length() > 32) {
					throw new RuntimeException("Plane Make cannot be null or exceed 32 characters");
				}
				break;
			}catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}while (true);
		
		// Start do while Plane Model		
		do {
			System.out.print("Enter the Plane's Model: ");
			try {
				model = in.readLine();
				if(model.length() <= 0 || model.length() > 64) {
					throw new RuntimeException("Plane Model cannot be null or exceed 64 characters");
				}
				break;
			}catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}while (true);
		
		// Start do while Plane Age		
		do {
			System.out.print("Enter the Plane's Age: ");
			try {
				age = Integer.parseInt(in.readLine());
				if(age < 0) {
					throw new RuntimeException("Plane Age cannot be negative");
				}
				break;
			}catch (NumberFormatException e) {
				System.out.println("Your input is invalid!");
				continue;
			}catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}while (true);
		
		// Start do while Number of Plane seats		
		do {
			System.out.print("Enter the Number of Plane Seats: ");
			try {
				seats = Integer.parseInt(in.readLine());
				if(age <= 0 || age >= 500) {
					throw new RuntimeException("Number of Plane Seats cannot be less than or equal to 0 or greater than or equal to 500");
				}
				break;
			}catch (NumberFormatException e) {
				System.out.println("Your input is invalid!");
				continue;
			}catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}while (true);
		
		try {
			String query = "INSERT INTO Plane (id, make, model, age, seats) VALUES (" + ID + ", \'" + make + "\', \'" + model + "\', " + age + ", " + seats + ");";
			
			esql.executeUpdate(query);
		}catch (Exception e) {
			System.err.println (e.getMessage());
		}
	
	}
	
	public static void AddPilot(DBproject esql) {//2
	
	 int ID;
	 String fullname;
	 String nationality;

	// Start do while ID integer
	do {
		System.out.print("Enter Pilot's ID Number: ");
		try {
			ID = Integer.parseInt(in.readLine());
			break;
		}catch (Exception e) {
			System.out.println("Your input is invalid!");
			continue;
		}
	}while (true);

	// Start do while fullname 
	do {
		System.out.print("Enter Pilot's full name : ");
		try {
			fullname = in.readLine();
			if(fullname.length() <= 0 || fullname.length() > 128) {
				throw new RuntimeException("Pilot's full name cannot be null or exceed 128 characters");
			break;
		}catch (Exception e) {
			System.out.println(e);
			continue;
		}
	}while (true);
	// Start so while pilot's nationality
	do {
		System.out.print("Enter the Pilot's nationality: ");
		try {
			nationality = in.readLine();
			if(nationality.length() <= 0 || nationality.length() > 24) {
				throw new RuntimeException("Pilot's nationality cannot be null or exceed 24 characters");
			}
			break;
		}catch (Exception e) {
			System.out.println(e);
			continue;
		}
	}while (true);
	
	try {
		String query = "INSERT INTO Pilot (id,fullname,nationality) VALUES (" + ID + ", \'" + name + "\', \'" + nationality + "\');";
		
		esql.executeUpdate(query);
	}catch (Exception e) {
		System.err.println (e.getMessage());
	}

   }
	public static void AddFlight(DBproject esql) {//3
		// Given a pilot, plane and flight, adds a flight in the DB
		int flight_number;
		int number_sold;
		int number_stops;
		float flight_cost;
		

		LocalDate departure_date;
		LocalDate arrival_date; 
		String depature_time;
		String arrival_time;
		String airport_departure;
		String airport_destination;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		 // Start do while flight's number 
		 do {
			System.out.print("Enter the flight's number: ");
			try {
				flight_number = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		}while (true);

		// Start do while flight cost 
		do {
			System.out.print("Enter the flight's cost: ");
			try {
				flight_cost = Float.parseFloat(in.readLine());
				if(flight_cost.length() <= 0){
					throw new RuntimeException("Flight cost cannot be zero");
				}
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		}while (true);
     // start do while number of flight seats sold
		do {
			System.out.print("Enter the number of flight seats sold: ");
			try {
				number_sold = Integer.parseInt(in.readLine());
				if(number_sold < 0) {
					throw new RuntimeException("Number of Seats Sold cannot be negative");
				}
				break;
			}catch (NumberFormatException e) {
				System.out.println("Your input is invalid!");
				continue;
			}catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}while (true);
		

		// start do while  
		do {
			System.out.print("Enter the number of flight stops: ");
			try {
				number_stops = Integer.parseInt(in.readLine());
				if(number_stops < 0) {
					throw new RuntimeException("Number of flight stops cannot be negative");
				}
				break;
			}catch (NumberFormatException e) {
				System.out.println("Your input is invalid!");
				continue;
			}catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}while (true);
		
		// start do while depature time 
		do {
			System.out.print("Enter the Departure Time (YYYY-MM-DD hh:mm): ");
			try {
				depature_time = in.readLine();
				departure_date = LocalDate.parse(depature_time, formatter);
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		}while (true);

		// start do while Arrival time 
		do {
			System.out.print("Enter the Arrival Time (YYYY-MM-DD hh:mm) (Note Arrival Time cannot be before Departure Time): ");
			try {
				arrival_time = in.readLine();
				arrival_date = LocalDate.parse(arrival_time, formatter);
				if(!arrival_date.isAfter(depature_time)) {
					throw new RuntimeException();
				}
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		}while (true);
		
		// start do while airport destination
		do {
			System.out.print("Enter the Destination Airport: ");
			try {
				airport_destination = in.readLine();
				if(airport_destination.length() <= 0 || airport_destination.length() > 5) {
					throw new RuntimeException("Desination Airport cannot be null or exceed 5 characters");
				}
				break;
			}catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}while (true);

		// start do while airport departure 
		do {
			System.out.print("Enter the Departure Airport: ");
			try {
				airport_departure = in.readLine();
				if(airport_departure.length() <= 0 || airport_departure.length() > 5) {
					throw new RuntimeException("Departure Airport cannot be null or exceed 5 characters");
				}
				break;
			}catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}while (true);
		
		try {
			String query = "INSERT INTO Flight (fnum, cost, num_sold, num_stops, actual_departure_date, actual_arrival_date, arrival_airport, departure_airport) VALUES (" + flight_number + ", " + flight_cost + ", " + number_sold + ", " + number_stops + ", \'" + depature_time; + "\', \'" + arrival_time + "\', \'" + airport_destination + "\', \'" + airport_departure + "\');";
			
			esql.executeUpdate(query);
		}catch (Exception e) {
			System.err.println (e.getMessage());
		}		
	
	}

	public static void AddTechnician(DBproject esql) {//4
		int technician_id;
		String technician_name;

		// start do while Technician ID number
		do {
			System.out.print("Enter the Technician ID Number: ");
			try {
				technician_id = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		}while (true);
			
		// start do while Technician Name
		do {
			System.out.print("Enter the Technician Name: ");
			try {
				technician_name = in.readLine();
				if(technician_name.length() <= 0 || technician_name.length() > 128) {
					throw new RuntimeException("Technician Name cannot be null or exceed 128 characters");
				}
				break;
			}catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}while (true);
		
		// start the query baby
		try {
			String query = "INSERT INTO Technician (id, full_name) VALUES (" + technician_id + ", \'" + technician_name + "\');";
			
			esql.executeUpdate(query);
		}catch (Exception e) {
			System.err.println (e.getMessage());
		}

	}

	public static void BookFlight(DBproject esql) {//5
		// Given a customer and a flight that he/she wants to book, add a reservation to the DB
	}

	public static void ListNumberOfAvailableSeats(DBproject esql) {//6
		// For flight number and date, find the number of availalbe seats (i.e. total plane capacity minus booked seats )
	}

	public static void ListsTotalNumberOfRepairsPerPlane(DBproject esql) {//7
		// Count number of repairs per planes and list them in descending order
	}

	public static void ListTotalNumberOfRepairsPerYear(DBproject esql) {//8
		// Count repairs per year and list them in ascending order
	}
	
	public static void FindPassengersCountWithStatus(DBproject esql) {//9
		// Find how many passengers there are with a status (i.e. W,C,R) and list that number.
	}
}