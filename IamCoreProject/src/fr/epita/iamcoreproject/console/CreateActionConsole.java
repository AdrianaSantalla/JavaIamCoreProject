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
 * 
 * @author Usuario
 *
 */
public class CreateActionConsole implements ActionConsole{
	
	Scanner scanner = new Scanner(System.in);
	
	@Override
	public void execute() {
		IdentityDAOInterface dao = new IdentityXmlDAO();
		Identity newIdentity = readDataIdentityConsoleValidating();
		newIdentity.setPassword("");
		newIdentity.setType("normal");
		dao.create(newIdentity);
		System.out.println("Identity succesfully created!");
	}
		
	private Identity readDataIdentityConsoleValidating(){
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
}
