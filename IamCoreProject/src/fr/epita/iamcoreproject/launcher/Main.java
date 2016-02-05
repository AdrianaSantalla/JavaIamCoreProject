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
 * @author Adriana Santalla
 *
 */
public class Main {

	/**
	 * @param args
	 */
	static Scanner scanner = new Scanner(System.in);
	static Identity currentUser;
	
	public static void main(String[] args) {
		System.out.println("Welcome to the Identities Application Management");
		if(authentication(scanner)) {
			int selectedOption=-1;
			do{
				showMenu();
//				selectedOption = Integer.parseInt(scanner.nextLine());
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
					System.out.println("Goodbye!");
					break;
				default:
					System.out.println("Invalid option");
					break;
				}
				
			}
			while(selectedOption!=0);			
		}
//		else
//			System.exit(0);		
	}
	
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

	private static boolean authentication(Scanner scanner) {
		IdentityDAOInterface dao = new IdentityXmlDAO();
		System.out.println("\nAuthentication");
		System.out.println("\nPlease enter your username: ");
		String username = scanner.nextLine();
		System.out.println("\nPlease enter your password: ");
		String password = scanner.nextLine();
		Identity user = new Identity();
		user.setDisplayName(username);
		if(!dao.findIdentity(user).isEmpty())
		{
			currentUser = dao.findIdentity(user).get(0);
			if(currentUser.getType().equals("admin")) {
				if(currentUser.getDisplayName().equals(username) && currentUser.getPassword().equals(password)) {
					System.out.println("\nWelcome "+username);
					return true;
				}
				else {
					System.out.println("\nAuthentication failed");
					return false;
				}
			}
			else {
				System.out.println("\nYou are not an admin user");
				return false;
			}
		}
		else {
			System.out.println("\nUnexisting user");
			return false;
		}
	}
	
	private static void readAllIdentities(){
		IdentityDAOInterface dao = new IdentityXmlDAO();
		List<Identity> list = dao.readAll();
		printListIdentities(list);
	}

	private static void printListIdentities(List<Identity> list) {
		System.out.println("DisplayName\t\tEmail\t\tUID\t\tBirthday\t\tType");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		for (Identity identity : list){
			System.out.println(identity.getDisplayName()+"\t\t"+
					identity.getEmail()+"\t\t"+identity.getUid()+"\t\t"+
					simpleDateFormat.format(identity.getBirthDate())+"\t\t"+identity.getType());
		}
	}
	
	private static void createIdentity(){
		IdentityDAOInterface dao = new IdentityXmlDAO();
		Identity newIdentity = readDataIdentityConsoleValidating();
		newIdentity.setPassword("");
		newIdentity.setType("normal");
		dao.create(newIdentity);
		System.out.println("Identity succesfully created!");
	}
	
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
