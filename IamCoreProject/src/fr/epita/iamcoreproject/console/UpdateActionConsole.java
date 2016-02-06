/**
 * 
 */
package fr.epita.iamcoreproject.console;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
import fr.epita.iamcoreproject.datamodel.Identity;

/**
 * @author Usuario
 *
 */
public class UpdateActionConsole implements ActionConsole{
	
	Scanner scanner = new Scanner(System.in);
	
	@Override
	public void execute() {
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
				identity = identityFound.get(0);
				System.out.println(identity.toReadableString());
				if(!identity.getType().equals("admin")) {
					System.out.println("\nInsert the new information for the identity");
					System.out.println("*If you do not want to update the a given field, left it EMPTY pressing just ENTER");
					Identity newIdentity = readDataIdentityConsole(false);
					dao.delete(identity);
					Identity combinedIdentity = dao.bindIdentities(identity, newIdentity);
					dao.create(combinedIdentity);
					System.out.println("Identity updated");
				}
				else{
					System.out.println("You cannot update admins' accounts");
					uid="";
				}
			}
			else {
				System.out.println("UID not founded. Insert another UID.");
				uid="";
			}				
		}
		while(uid.equals(""));
	}
	
	private Identity readDataIdentityConsole(boolean letInsertExistingUID){
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

}
