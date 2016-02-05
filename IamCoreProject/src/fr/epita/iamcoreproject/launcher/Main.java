/**
 * 
 */
package fr.epita.iamcoreproject.launcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Welcome to the Identities Application Management");
		if(authentication(scanner)) {
			int selectedOption=-1;
			do{
				showMenu();
				selectedOption = Integer.parseInt(scanner.nextLine());
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
					System.out.println("option 4");
					break;
				case 5: 
					System.out.println("option 5");
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

	private static boolean authentication(Scanner scanner) {
		IdentityDAOInterface dao = new IdentityXmlDAO();
		System.out.println("\nAuthentication");
		System.out.println("\nPlease enter your username: ");
		String username = scanner.nextLine();
		System.out.println("\nPlease enter your password: ");
		String password = scanner.nextLine();
		Identity user = new Identity();
		user.setDisplayName(username);
		if(!dao.findUser(user).isEmpty())
		{
			Identity userFound = dao.findUser(user).get(0);
			if(userFound.getType().equals("admin")) {
				if(userFound.getDisplayName().equals(username) && userFound.getPassword().equals(password)) {
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
	
	private static void showMenu(){
		System.out.println("\nMenu\n");
		System.out.println("Choose an option:");
		System.out.println("1. Show all Identities");
		System.out.println("2. Create an Identity");
		System.out.println("3. Update an Identity");
		System.out.println("4. Delete an Identity");
		System.out.println("5. Search an Identity");		
		System.out.println("0. Exit\n");
	}
	
	private static void readAllIdentities(){
		IdentityDAOInterface dao = new IdentityXmlDAO();
		List<Identity> list = dao.readAll();
		System.out.println("DisplayName\t\tEmail\t\tUID\t\tBirthday");
		for (Identity identity : list){
			System.out.println(identity.getDisplayName()+"\t\t"+
					identity.getEmail()+"\t\t"+identity.getUid()+"\t\t"+
					identity.getBirthDate());
		}
	}
	
	private static void createIdentity(){
		IdentityDAOInterface dao = new IdentityXmlDAO();
		String displayName = null, email = null, uid = null, birthday = null;
		Date date = null;
		do {
			System.out.println("Insert the Display Name:");
			displayName = scanner.nextLine();
		}
		while(displayName==null || displayName.equals(""));
		do {
			System.out.println("Insert the Email:");
			email = scanner.nextLine();
		}
		while(email==null || email.equals(""));
		do {
			System.out.println("Insert the UID:");
			uid = scanner.nextLine();
		}
		while(uid==null || uid.equals(""));
		do {
			System.out.println("Insert the Birthday (dd/MM/yyyy):");
			birthday = scanner.nextLine();
			if(birthday!=null && !birthday.equals("")){
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				try {
					date = simpleDateFormat.parse(birthday);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					System.out.println("Insert a valid date (dd/MM/yyyy)");
					birthday=null;
				}
			}
		}
		while(birthday==null || birthday.equals(""));
		Identity newIdentity = new Identity(uid,email,displayName,date);
		newIdentity.setPassword("");
		newIdentity.setType("normal");
		dao.create(newIdentity);
		System.out.println("Identity succesfully created!");
	}
	
	public static void updateIdentity(){
		
	}

}
