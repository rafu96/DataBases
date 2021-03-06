	package de.dis2011;

import de.dis2011.data.EstateAgent;
import de.dis2011.data.Estate;
import de.dis2011.data.Contract;
import de.dis2011.data.Apartment;
import de.dis2011.data.House;
import de.dis2011.data.TenancyContract;
import de.dis2011.data.PurchaseContract;


import de.dis2011.data.DB2ConnectionManager;
import de.dis2011.data.Person;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;








/**
 * Hauptklasse
 */
public class Main {

        static EstateAgent actualEstateAgent;

	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {

		showMainMenu();
	}

	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_AGENT = 0;
                final int INSERT_PERSON = 1;
                final int SHOW_CONTRACTS = 2;
                final int LOGIN = 3;
		final int QUIT = 4;

		//Erzeuge Menü
		Menu mainMenu = new Menu("Main Menu");
		mainMenu.addEntry("Agent Administration", MENU_AGENT);
                mainMenu.addEntry("Insert Person", INSERT_PERSON);
                mainMenu.addEntry("Login", LOGIN);
                mainMenu.addEntry("Show Contracts", SHOW_CONTRACTS);
                //mainMenu.addEntry("Estate Administration", MENU_ESTATE);
                //mainMenu.addEntry("Contract Management", MENU_CONTRACT);
		mainMenu.addEntry("Terminate", QUIT);

		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();

			switch(response) {
				case MENU_AGENT:
                                    showEstateAgentMenu();
                                    break;
                                case SHOW_CONTRACTS:
                                    showContracts();
                                    break;
                                case INSERT_PERSON:
                                    insertPerson();
                                    break;
                                case LOGIN:
                                    if( login() )   showLoggedAgentMenu();
                                    break;
				case QUIT:
                                    return;
			}
		}
	}

	/**
	 * Shows agent management
	 */
	public static void showEstateAgentMenu() {
		//Menüoptionen1
		final int NEW_AGENT = 0;
    final int UPDATE_AGENT = 1;
    final int DELETE_AGENT = 2;
		final int BACK = 3;

		//agentverwaltungsmenü
		Menu estateAgentMenu = new Menu("Agent Administration");
		estateAgentMenu.addEntry("New Agent", NEW_AGENT);
    estateAgentMenu.addEntry("Update Agent", UPDATE_AGENT);
		estateAgentMenu.addEntry("Delete Agent", DELETE_AGENT);
		estateAgentMenu.addEntry("Back to Main Menu", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = estateAgentMenu.show();

			switch(response) {
				case NEW_AGENT:
					newEstateAgent();
					break;

				case BACK:
					return;
			}
		}
	}

	/**
	 * Shows estate management
	 */
	public static void showEstateMenu() {
		//Menüoptionen1
		final int NEW_APARTAMENT = 0;
		final int NEW_HOUSE = 1;
		final int UPDATE_APARTAMENT = 2;
		final int UPDATE_HOUSE = 3;
    final int DELETE_APARTAMENT = 4;
		final int DELETE_HOUSE = 5;
		final int BACK = 6;

		//agentverwaltungsmenü
		Menu estateMenu = new Menu("Estate Administration");
		estateMenu.addEntry("New Apartment", NEW_APARTAMENT);
		estateMenu.addEntry("New House", NEW_HOUSE);
		estateMenu.addEntry("Update Apartment", UPDATE_APARTAMENT);
		estateMenu.addEntry("Update House", UPDATE_HOUSE);
		estateMenu.addEntry("Delete Estate", DELETE_APARTAMENT);
		estateMenu.addEntry("Delete House", DELETE_HOUSE);
		estateMenu.addEntry("Back to Main Menu", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();

			switch(response) {
				case NEW_APARTAMENT:
					newApartmentEstate();
					break;
				case NEW_HOUSE:
					newHouseEstate();
					break;
				case UPDATE_APARTAMENT:
					updateApartment();
					break;
				case UPDATE_HOUSE:
					updateHouse();
					break;
				case DELETE_APARTAMENT:
					deleteApartment(FormUtil.readInt("ID"));
					break;
				case DELETE_HOUSE:
					deleteHouse(FormUtil.readInt("ID"));
					break;
					case BACK:
						return;
			}
		}
	}

        /**
	 * Shows Contract management
	 */
	public static void showContractMenu() {
		//Menüoptionen1
		final int NEW_TENANCY_CONTRACT = 0;
                final int NEW_PURCHASE_CONTRACT = 1;
		final int BACK = 2;

		//agentverwaltungsmenü
		Menu contractMenu = new Menu("Contract Administration");
		contractMenu.addEntry("New Tenancy Contract", NEW_TENANCY_CONTRACT);
                contractMenu.addEntry("New Purchase Contract", NEW_PURCHASE_CONTRACT);
		contractMenu.addEntry("Back to Main Menu", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = contractMenu.show();

			switch(response) {
				case NEW_TENANCY_CONTRACT:
					newTenancyContract();
					break;
				case NEW_PURCHASE_CONTRACT:
					newPurchaseContract();
					break;
				case BACK:
					return;
			}
		}
	}

        /**
	 * Shows Logged Agent Menu
	 */
        public static void showLoggedAgentMenu() {
		//Menüoptionen
                final int MENU_ESTATE = 0;
                final int MENU_CONTRACT = 1;
                final int LOGOUT = 2;
		final int QUIT = 3;

		//Erzeuge Menü
		Menu loggedAgentMenu = new Menu("Logged Agent Menu");
                loggedAgentMenu.addEntry("Estate Administration", MENU_ESTATE);
                loggedAgentMenu.addEntry("Contract Management", MENU_CONTRACT);
                loggedAgentMenu.addEntry("Logout", LOGOUT);

		//Verarbeite Eingabe
		while(true) {
			int response = loggedAgentMenu.show();

			switch(response) {
                                case MENU_ESTATE:
					showEstateMenu();
					break;
                                case MENU_CONTRACT:
					showContractMenu();
					break;
                                case LOGOUT:
                                        actualEstateAgent = null;
                                        return;
			}
		}
	}

	/**
	 * Creates a new agent after the user enters the appropriate data.
	 */
	public static void newEstateAgent() {
		EstateAgent m = new EstateAgent();

		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();

		System.out.println("Agent with Login "+m.getLogin()+" was generated.");
	}

    /**
	 * Updates a new agent after the user enters the appropriate data.
	 */
	public static void updateEstateAgent() {
            String login = FormUtil.readString("Enter the Login from the Agent to modify");

            // Get connected
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            try {
                    String selectSQL = "SELECT * FROM estateagent WHERE login = ?";
                    PreparedStatement pstmt = con.prepareStatement(selectSQL);

                    pstmt.setString(1, login);

                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        EstateAgent m = EstateAgent.load(login);

                        System.out.println("Enter the new data:");

                        m.setName(FormUtil.readString("Name"));
                        m.setAddress(FormUtil.readString("Adresse"));
                        m.setLogin(FormUtil.readString("Login"));
                        m.setPassword(FormUtil.readString("Passwort"));
                        m.save();

                        System.out.println("Agent with Login "+m.getLogin()+" was updated.");
                    }
                    else System.out.println("Invalid Agent Login.");

            } catch (SQLException e) {
                    e.printStackTrace();
            }
	}

	/**
	 * Deletes an agent from the database.
	 */
	public static void deleteEstateAgent(String login) {
		EstateAgent m = EstateAgent.load(login);

		if( m != null ){
                    m.delete();
                    System.out.println("Agent with Login "+login+" was deleted.");
                }
                else System.out.println("Unexistent Agent Login.");
	}


	/**
	 * Creates a new Apartment after the user enters the appropriate data.
	 */
	public static void newApartmentEstate() {
		Apartment e = new Apartment();


		e.setCity(FormUtil.readString("City"));
		e.setPostalCode(FormUtil.readInt("Postal Code"));
		e.setStreet(FormUtil.readString("Street"));
		e.setStreetNumber(FormUtil.readInt("Street Number"));
		e.setSquareArea(FormUtil.readInt("Square Area"));
		e.setFloor(FormUtil.readInt("Floor"));
		e.setRent(FormUtil.readInt("Rent"));
		e.setRooms(FormUtil.readInt("Rooms"));
                boolean balcony = (FormUtil.readInt("Balcony") != 0);
		e.setBalcony(balcony);
                boolean kitchen = (FormUtil.readInt("Kitchen") != 0);
                e.setKitchen(kitchen);
                e.setManager(actualEstateAgent.getLogin());
		e.save();



		System.out.println("Apartment with ID "+e.getId()+" was generated.");
	}



		/**
		 * Creates a new house after the user enters the appropriate data.
		 */
		public static void newHouseEstate() {
			House e = new House();


			e.setCity(FormUtil.readString("City"));
			e.setPostalCode(FormUtil.readInt("Postal Code"));
			e.setStreet(FormUtil.readString("Street"));
			e.setStreetNumber(FormUtil.readInt("Street Number"));
			e.setSquareArea(FormUtil.readInt("Square Area"));
			e.setFloors(FormUtil.readInt("Floors"));
			e.setPrice(FormUtil.readInt("Price"));
		                boolean garden = (FormUtil.readInt("Garden") != 0);
	                e.setGarden(garden);
                        e.setManager(actualEstateAgent.getLogin());
			e.save();
			System.out.println("House with ID "+e.getId()+" was generated.");
		}

	/**
        * Updates a Apartment after the user enters the appropriate data.
        */
	public static void updateApartment() {
						int id = FormUtil.readInt("Enter the ID from the Apartment to modify");

						// Get connected
						Connection con = DB2ConnectionManager.getInstance().getConnection();

						try {
										String selectSQL = "SELECT * FROM apartment WHERE estateid = ?";
										PreparedStatement pstmt = con.prepareStatement(selectSQL);

										pstmt.setInt(1, id);

										ResultSet rs = pstmt.executeQuery();
										if (rs.next()) {
												Apartment e = Apartment.load(id);



												String updateSQL1 = "UPDATE Estate SET city = ?, postal_code = ?, street = ?, street_number = ?, square_area = ? WHERE estateid = ?";
												PreparedStatement pstmt2 = con.prepareStatement(updateSQL1);

												// Set request parameters
												pstmt2.setString(1, FormUtil.readString("City"));
												pstmt2.setInt(2, FormUtil.readInt("Postal Code"));
												pstmt2.setString(3, FormUtil.readString("Street"));
												pstmt2.setInt(4, FormUtil.readInt("Street Number"));
												pstmt2.setInt(5,FormUtil.readInt("Square Area"));
												pstmt2.setInt(6, e.getId());


												pstmt2.executeUpdate();



												String updateSQL = "UPDATE Apartment SET floor = ?, rent = ?, rooms = ?, balcony = ?, kitchen = ? WHERE estateid = ?";
												PreparedStatement pstmt1 = con.prepareStatement(updateSQL);

												pstmt1.setInt(1, FormUtil.readInt("Floor"));
												pstmt1.setInt(2, FormUtil.readInt("Rent"));
												pstmt1.setInt(3, FormUtil.readInt("Rooms"));
												pstmt1.setInt(4, FormUtil.readInt("Balcony"));
												pstmt1.setInt(5, FormUtil.readInt("Kitchen"));
												pstmt1.setInt(6, e.getId());
												pstmt1.executeUpdate();

												rs.close();
												pstmt.close();
												pstmt1.close();
												pstmt2.close();


												System.out.println("Apartment with ID "+e.getId()+" was updated.");
										}
										else System.out.println("Invalid Estate ID.");

						} catch (SQLException e) {
										e.printStackTrace();
						}
	}


		/**
	        * Updates a House after the user enters the appropriate data.
	        */
		public static void updateHouse() {
							int id = FormUtil.readInt("Enter the ID from the House to modify");

							// Get connected
							Connection con = DB2ConnectionManager.getInstance().getConnection();

							try {
											String selectSQL = "SELECT * FROM House WHERE estateid = ?";
											PreparedStatement pstmt = con.prepareStatement(selectSQL);

											pstmt.setInt(1, id);

											ResultSet rs = pstmt.executeQuery();
											if (rs.next()) {
												 	House e = House.load(id);



													String updateSQL1 = "UPDATE Estate SET city = ?, postal_code = ?, street = ?, street_number = ?, square_area = ? WHERE estateid = ?";
													PreparedStatement pstmt2 = con.prepareStatement(updateSQL1);

													// Set request parameters
													pstmt2.setString(1, FormUtil.readString("City"));
													pstmt2.setInt(2, FormUtil.readInt("Postal Code"));
													pstmt2.setString(3, FormUtil.readString("Street"));
													pstmt2.setInt(4, FormUtil.readInt("Street Number"));
													pstmt2.setInt(5,FormUtil.readInt("Square Area"));
													pstmt2.setInt(6, e.getId());


													pstmt2.executeUpdate();



													String updateSQL = "UPDATE House SET floors = ?, price = ?, garden = ? WHERE estateid = ?";
													PreparedStatement pstmt1 = con.prepareStatement(updateSQL);

													pstmt1.setInt(1, FormUtil.readInt("Floors"));
													pstmt1.setInt(2, FormUtil.readInt("Price"));
													pstmt1.setInt(3, FormUtil.readInt("Garden"));
													pstmt1.setInt(4, e.getId());
													pstmt1.executeUpdate();

													rs.close();
													pstmt.close();
													pstmt1.close();
													pstmt2.close();


													System.out.println("House with ID "+e.getId()+" was updated.");
											}
											else System.out.println("Invalid Estate ID.");

							} catch (SQLException e) {
											e.printStackTrace();
							}
		}

        /**
	* Deletes an apartment from the database.
	*/
	public static void deleteApartment(int id) {
		Apartment e = Apartment.load(id);

		if( e != null ){
			e.delete();
			System.out.println("Apartment with ID "+Integer.toString(id)+" was deleted.");
		}
		else System.out.println("Unexistent Estate ID.");
	}

	/**
        * Deletes a House from the database.
        */
        public static void deleteHouse(int id) {
House e = House.load(id);

if( e != null ){
e.delete();
System.out.println("House with ID "+Integer.toString(id)+" was deleted.");
}
else System.out.println("Unexistent Estate ID.");
}
	/**
	 * Creates a new tenancy contract after the user enters the appropriate data.
	 */

	public static void newTenancyContract() {
		TenancyContract c = new TenancyContract();

                String p_first_name = FormUtil.readString("Tenant first name");
                String p_name = FormUtil.readString("Tenant name");
                String p_address = FormUtil.readString("Tenant address");
                Person p = Person.load(p_first_name, p_name, p_address);

                if( p == null ){
                    p = new Person(p_first_name, p_name, p_address);
                    p.save();
                }

                int ap_id = FormUtil.readInt("Apartment id");

                Apartment ap = Apartment.load(ap_id);

                if( ap == null ){
                    System.out.println("Invalid apartment id");
                    return;
                }

                c.setDate(FormUtil.readDate("Date"));
								c.setPlace(FormUtil.readString("Place"));
                c.setStartDate(FormUtil.readDate("Start Date"));
                c.setDuration(FormUtil.readInt("Duration"));
                c.setAdditionalCosts(FormUtil.readInt("Additional Cost"));

                Connection con = DB2ConnectionManager.getInstance().getConnection();

                try {
                    String insertSQL = "INSERT INTO Rents(id_person,id_apartment,tenancy_contract_number) VALUES (?,?,?)";

                    PreparedStatement pstmt = con.prepareStatement(insertSQL);

                    // Set request parameters and execute request
                    pstmt.setInt(1, p.getId());
                    pstmt.setInt(2,ap_id);
                    pstmt.setInt(3, c.getId());
                    pstmt.executeUpdate();

                    pstmt.close();
                    c.save();
                } catch (SQLException e) {
                    //e.printStackTrace();
                    System.out.println("The apartment already has a contract.");
		}

		System.out.println("Contract with number "+c.getId()+" was generated.");
	}

        /**
	 * Creates a new purchase contract after the user enters the appropriate data.
	 */
        public static void newPurchaseContract() {
								PurchaseContract c = new PurchaseContract();

                String p_first_name = FormUtil.readString("Buying person first name");
                String p_name = FormUtil.readString("Buying person name");
                String p_address = FormUtil.readString("Buying person address");
                Person p = Person.load(p_first_name, p_name, p_address);

                if( p == null ){
                    p = new Person(p_first_name, p_name, p_address);
                    p.save();
                }

                int ap_id = FormUtil.readInt("House id");

                House ap = House.load(ap_id);

                if( ap == null ){
                    System.out.println("Invalid apartment id");
                    return;
                }

								c.setDate(FormUtil.readDate("Date"));
								c.setPlace(FormUtil.readString("Place"));
                c.setInstallments(FormUtil.readInt("Installments"));
                c.setInterest(FormUtil.readInt("Interest Rate"));

                Connection con = DB2ConnectionManager.getInstance().getConnection();

                try {String insertSQL = "INSERT INTO Sells(id_person,id_house,purchase_contract_number) VALUES (?,?,?)";

                    PreparedStatement pstmt = con.prepareStatement(insertSQL);

                    // Set request parameters and execute request
                    pstmt.setInt(1, p.getId());
                    pstmt.setInt(2,ap_id);
                    pstmt.setInt(3, c.getId());
                    pstmt.executeUpdate();

                    pstmt.close();
                    c.save();
                } catch (SQLException e) {
                    //e.printStackTrace();
                    System.out.println("The house already has a contract.");
		}

		System.out.println("Contract with number "+c.getId()+" was generated.");
	}

	/**
	 * See all the contracts
	 */
	public static void showContracts() {

		// Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
                        System.out.println("Tenancy Contracts");

                        String selectSQL1 = "SELECT contract_number FROM TenancyContract";
                        PreparedStatement pstmt1 = con.prepareStatement(selectSQL1);


                        ResultSet rs1 = pstmt1.executeQuery();
                        while(rs1.next()) {
                                int c_number = rs1.getInt("contract_number");
                                TenancyContract tc = TenancyContract.load(c_number);

                                tc.print();
                        }

                        System.out.println("Purchase Contracts");

                        String selectSQL2 = "SELECT contract_number FROM PurchaseContract";
                        PreparedStatement pstmt2 = con.prepareStatement(selectSQL2);


                        ResultSet rs2 = pstmt2.executeQuery();
                        while(rs2.next()) {
                                int c_number = rs2.getInt("contract_number");
                                PurchaseContract pc = PurchaseContract.load(c_number);

                                pc.print();
                        }

		} catch (SQLException e) {
						e.printStackTrace();
		}
	}

	/**
	 * Creates a new contract after the user enters the appropriate data.
	 */
	public static void insertPerson() {
		Person p = new Person();

		p.setFirstName(FormUtil.readString("First name"));
                p.setName(FormUtil.readString("Name"));
                p.setAddress(FormUtil.readString("Address"));

                p.save();
	}

        /**
	 * Ask for an username and password and try to log in
	 */
	public static boolean login() {
                System.out.println("Please enter the username:");
                String username = "";

                username = FormUtil.readString("Name");

                // Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
                        String selectByLoginSQL = "SELECT * FROM estateagent WHERE login = ?";
                        PreparedStatement pstmt = con.prepareStatement(selectByLoginSQL);

                        pstmt.setString(1, username);

                        ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
                                String password = "";

                                password = FormUtil.readString("Password");

                                if( password.equals(rs.getString("password")) ){
                                    EstateAgent ts = new EstateAgent();
                                    ts.setName(rs.getString("name"));
                                    ts.setAddress(rs.getString("address"));
                                    ts.setLogin(rs.getString("login"));
                                    ts.setPassword(rs.getString("password"));

                                    rs.close();
                                    pstmt.close();
                                    actualEstateAgent = ts;

                                    System.out.println("Login successful.");
                                    return true;
                                }
                                else{
                                    System.out.println("Incorrect password.");
                                    return false;
                                }
			}
                        else{
                            System.out.println("Invalid username.");
                            return false;
                        }

                } catch (SQLException e) {
			e.printStackTrace();
		}


		return false;
	}

}
