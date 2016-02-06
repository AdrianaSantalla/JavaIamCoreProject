/**
 * 
 */
package fr.epita.iamcoreproject.launcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
import fr.epita.iamcoreproject.dao.exceptions.DaoUpdateException;
import fr.epita.iamcoreproject.datamodel.Identity;

/**
 * @author Adriana Stantalla and David Cechak
 * @version 1
 * @param (methods and constructors only)
 * @return (methods only)
 * @exception (@throws is a synonym added in Javadoc 1.2)
 *
 */

/**
 * Main class for IAM project, displays menu, manages user choices, gathers user input for DAO functions and calls them
 * @author Adriana Stantalla and David Cechak
 * @version 1
 * 
 */
public class Main {

	static Scanner scanner = new Scanner(System.in);
	static Identity currentUser;
	
	/**
	 * Main method, manages user choices
	 * @author Adriana Stantalla and David Cechak
	 * @version 1
	 * @param selectedOption stores option selected by the user
	 * 
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to the Identities Application Management");
		authentication();
		int selectedOption = -1;
		do{
			showMenu();
			try {
	    		selectedOption = scanner.nextInt();
	    	} catch (InputMismatchException e) {
	    	    System.err.println("Please input a number: ");
	    	    scanner.nextLine();
	    	    continue;
	    	}
			scanner.nextLine();
			switch(selectedOption){
				case 1: 
					readAllIdentities();
					break;
				case 2: 
					createIdentity();
					break;
				case 3: 
					updateIdentity();
					break;
				case 4: 
					deleteIdentity();
					break;
				case 5: 
					searchIdentity();
					break;
				case 0: 
					scanner.close();
					System.out.println("Goodbye!");
					break;
				default:
					System.out.println("Invalid option");
					break;
			}
		}while(selectedOption!=0);				
	}
	
	/**
	 * Displays menu
	 * @author Adriana Stantalla and David Cechak
	 * @version 1
	 * 
	 */
	private static void showMenu(){
		// Display menu
	    System.out.println("____________________________");
	    System.out.println("|       MAIN MENU          |");
	    System.out.println("|__________________________|");
	    System.out.println("|  Choose an option:       |");
	    System.out.println("|        1. Show all       |");
	    System.out.println("|        2. Create         |");
	    System.out.println("|        3. Update         |");
	    System.out.println("|        4. Delete         |");
	    System.out.println("|        5. Search         |");
	    System.out.println("|        0. Exit           |");
	    System.out.println("|__________________________|");
	}

	/**
	 * Checks the user's login details
	 * @author Adriana Stantalla and David Cechak
	 * @version 1
	 * @param dao		IdentityXmlDAO to get access to the file and work with identities
	 * @param state 	holds the status of user's authentication
	 * @param username	stores user's login
	 * @param password	stores user's password
	 * @param user		identity to store authentication details
	 * @see	fr.epita.iamcoreproject.datamodel.Identity.setDisplayName(String displayName)
	 * @see	fr.epita.iamcoreproject.datamodel.Identity.getPassword()
	 * @see	fr.epita.iamcoreproject.datamodel.Identity.getType()
	 * @see	fr.epita.iamcoreproject.dao.findIdentity(Identity criteria)
	 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO.IdentityXmlDAO()
	 *
	 */
	private static void authentication() {
		IdentityDAOInterface dao = new IdentityXmlDAO();
		Identity user = new Identity();
		boolean state = false;
		
		while(!state){
			System.out.println("\nAuthentication");
			System.out.println("Please enter your username: ");
			String username = scanner.nextLine();
			System.out.println("Please enter your password: ");
			String password = scanner.nextLine();
			user.setDisplayName(username);
			if(!dao.findIdentity(user).isEmpty())
			{
				currentUser = dao.findIdentity(user).get(0);
				if(currentUser.getType().equals("admin")) {
					if(currentUser.getDisplayName().equals(username) && currentUser.getPassword().equals(password)) {
						System.out.println("\nWelcome "+username);
						state = true;
					}
					else {
						System.out.println("\nAuthentication failed");
						state = false;
					}
				}
				else {
					System.out.println("\nYou are not an admin user");
					state = false;
				}
			}
			else {
				System.out.println("\nUnexisting user");
				state = false;
			}
		}
	}
	
	/**
	 * Calls and prints all identities
	 * @author Adriana Stantalla and David Cechak
	 * @version 1
	 * @param dao IdentityXmlDAO to get access to the file and work with identities
	 * @param list stores identities to print
	 * @see	Main.printListIdentities(List<Identity> list)
	 * @see fr.epita.iamcoreproject.dao.IdentityDAOInterface.readAll()
	 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO.IdentityXmlDAO()
	 *
	 */
	private static void readAllIdentities(){
		IdentityDAOInterface dao = new IdentityXmlDAO();
		List<Identity> list = dao.readAll();
		printListIdentities(list);
	}

	/**
	 * Manages the format and prints identities details
	 * @author Adriana Stantalla and David Cechak
	 * @version 1
	 * @param list with stored identities to print
	 * @param simpleDataFormat ensures right date format
	 * @see fr.epita.iamcoreproject.datamodel.Identity
	 *
	 */
	private static void printListIdentities(List<Identity> list) {
		System.out.println("DisplayName\t\tEmail\t\tUID\t\tBirthday\t\tType");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		for (Identity identity : list){
			System.out.println(identity.getDisplayName()+"\t\t"+
					identity.getEmail()+"\t\t"+identity.getUid()+"\t\t"+
					simpleDateFormat.format(identity.getBirthDate())+"\t\t"+identity.getType());
		}
	}
	
	/**
	 * Creates new identity without admin rights
	 * @author Adriana Stantalla and David Cechak
	 * @version 1
	 * @param dao IdentityXmlDAO to get access to the file and work with identities
	 * @param newIdentity identity to be created
	 * @see Main.readDataIdentityConsoleValidating()
	 * @see fr.epita.iamcoreproject.datamodel.Identity.setPassword(String password)
	 * @see fr.epita.iamcoreproject.datamodel.Identity.setType(String type)
	 * @see fr.epita.iamcoreproject.dao.IdentityDAOInterface.create(Identity identity)
	 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO.IdentityXmlDAO()
	 *
	 */
	private static void createIdentity(){
		IdentityDAOInterface dao = new IdentityXmlDAO();
		Identity newIdentity = readDataIdentityConsoleValidating();
		newIdentity.setPassword("");
		newIdentity.setType("normal");
		dao.create(newIdentity);
		System.out.println("Identity succesfully created!");
	}
	
	/**
	 * Gathers user input for creating new identity, ensures that UID is unique,
	 *  date has the right format and all fields are being filled 
	 * @author Adriana Stantalla and David Cechak
	 * @version 1
	 * @param dao IdentityXmlDAO to get access to the file and work with identities
	 * @param displayName to set the name
	 * @param email to set the email
	 * @param uid to set the UID
	 * @param birthdate to set the birthdate
	 * @param date to save the birthdate in the right format
	 * @return Identity
	 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO.IdentityXmlDAO()
	 * @see fr.epita.iamcoreproject.datamodel.Identity.Identity()
	 *
	 */
	private static Identity readDataIdentityConsoleValidating(){
		IdentityDAOInterface dao = new IdentityXmlDAO();
		String displayName, email, uid, birthdate;
		Date date = null;
		do{
			System.out.println("Insert the Display Name:");
			displayName = scanner.nextLine();
		}
		while(displayName.equals(""));
		
		do{
			System.out.println("Insert the Email:");
			email = scanner.nextLine();
		}
		while(email.equals(""));
		
		do {
			System.out.println("Insert the UID:");
			uid = scanner.nextLine();
			if(!uid.equals("")){
				Identity identity = new Identity();
				identity.setUid(uid);
				List<Identity> identityFound = dao.findIdentity(identity);
				if(!identityFound.isEmpty()){
					System.out.println("UID already registered. Insert an another one.");
					uid="";
				}
			}
		}
		while(uid.equals(""));
		
		do{
			System.out.println("Insert the Birthday (dd/MM/yyyy):");
			birthdate = scanner.nextLine();
			if(!birthdate.equals("")){
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				try {
					date = simpleDateFormat.parse(birthdate);
				} catch (ParseException e) {
					System.out.println("Insert a valid date (dd/MM/yyyy)");
					birthdate="";
				}
			}
		}
		while(birthdate.equals(""));
		Identity identity = new Identity(uid,email,displayName,date);
		return identity;
	}
	
	/**
	 * @author Adriana Stantalla and David Cechak
	 * @version 1
	 * @param (methods and constructors only)
	 * @return (methods only)
	 * @exception (@throws is a synonym added in Javadoc 1.2)
	 *
	 */
	private static Identity readDataIdentityConsole(boolean letInsertExistingUID){
		IdentityDAOInterface dao = new IdentityXmlDAO();
		String displayName, email, uid, birthdate;
		Date date = null;
		System.out.println("Insert the Display Name:");
		displayName = scanner.nextLine();
		
		System.out.println("Insert the Email:");
		email = scanner.nextLine();
						
		do {
			System.out.println("Insert the UID:");
			uid = scanner.nextLine();
			if(!uid.equals("") && letInsertExistingUID==false){
				Identity identity = new Identity();
				identity.setUid(uid);
				if(!dao.findIdentity(identity).isEmpty()){
					System.out.println("UID already registered. Insert an another one.");
					uid=null;
				}
			}
		}
		while(uid==null);
		
		do{
			System.out.println("Insert the Birthday (dd/MM/yyyy):");
			birthdate = scanner.nextLine();
			if(!birthdate.equals("")){
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				try {
					date = simpleDateFormat.parse(birthdate);
				} catch (ParseException e) {
					System.out.println("Insert a valid date (dd/MM/yyyy)");
					birthdate=null;
				}
			}
		}
		while(birthdate==null);
		Identity identity = new Identity(uid,email,displayName,date);
		return identity;
	}
	
	/**
	 * @author Adriana Stantalla and David Cechak
	 * @version 1
	 * @param (methods and constructors only)
	 * @return (methods only)
	 * @exception (@throws is a synonym added in Javadoc 1.2)
	 */
	private static void updateIdentity(){
		IdentityDAOInterface dao = new IdentityXmlDAO();
		String uid;
		do {
			System.out.println("Insert the UID of the Identity you want to update:");
			uid = scanner.nextLine();
			Identity identity = new Identity();
			identity.setUid(uid);
			List<Identity> identityFound = dao.findIdentity(identity);
			if(!identityFound.isEmpty()){
				System.out.println("This is the actual data of the Identity:");
				printListIdentities(identityFound);
				identity = identityFound.get(0);
				if(!identity.getType().equals("admin")) {
					System.out.println("Insert the new information for the identity");
					System.out.println("*If you do not want to update the a given field, left it EMPTY pressing just ENTER");
					Identity newIdentity = readDataIdentityConsole(false);
					Identity combinedIdentity = dao.bindIdentities(identity, newIdentity);
					dao.delete(identity);
					dao.create(combinedIdentity);
					System.out.println("Identity updated");
				}
				else{
					System.out.println("You cannot update admins' accounts");
					uid=null;
				}
			}
			else {
				System.out.println("UID not founded. Insert another UID.");
				uid=null;
			}				
		}
		while(uid.equals(""));
	}
	
	/**
	 * @author Adriana Stantalla and David Cechak
	 * @version 1
	 * @param (methods and constructors only)
	 * @return (methods only)
	 * @exception (@throws is a synonym added in Javadoc 1.2)
	 *
	 */
	private static void deleteIdentity(){
		IdentityDAOInterface dao = new IdentityXmlDAO();
		String uid;
		do {
			System.out.println("Insert the UID of the Identity you want to delete:");
			uid = scanner.nextLine();
			Identity identity = new Identity();
			identity.setUid(uid);
			List<Identity> identityFound = dao.findIdentity(identity);
			if(!identityFound.isEmpty()){
				System.out.println("This is the actual data of the Identity:");
				printListIdentities(identityFound);
				identity = identityFound.get(0);
				if(!identity.getType().equals("admin")) {
					String confirmation;
					System.out.println("Are you sure you want to delete it? \ny | n");
					do{
						confirmation = scanner.nextLine();
						if(confirmation.equals("y")){
							dao.delete(identity);
							System.out.println("Identity deleted");
						}
						else if(confirmation.equals("n")){
							System.out.println("Deletion cancelled");
						}
						else{
							System.out.println("Insert a valid option (y | n)");
							confirmation=null;
						}
					}
					while(confirmation==null);				
				}
				else{
					System.out.println("You cannot delete admins' accounts");
					uid=null;
				}
			}
			else {
				System.out.println("UID not founded. Insert another UID.");
				uid=null;
			}				
		}
		while(uid.equals(""));
	}
	
	/**
	 * @author Adriana Stantalla and David Cechak
	 * @version 1
	 * @param (methods and constructors only)
	 * @return (methods only)
	 * @exception (@throws is a synonym added in Javadoc 1.2)
	 *
	 */
	private static void searchIdentity(){
		System.out.println("Insert all the information that you want to search");
		System.out.println("*If you do not want to search a given field, left it EMPTY pressing just ENTER");
		Identity newIdentity = readDataIdentityConsole(true);
		IdentityDAOInterface dao = new IdentityXmlDAO();
		List<Identity> list = dao.search(newIdentity);
		System.out.println("Identities found:");
		printListIdentities(list);
	}
	
}
