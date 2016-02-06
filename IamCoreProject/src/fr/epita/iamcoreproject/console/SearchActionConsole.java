/**
 * 
 */
package fr.epita.iamcoreproject.console;

import java.util.List;
import java.util.Scanner;

import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
import fr.epita.iamcoreproject.datamodel.Identity;

/**
 * @author Usuario
 *
 */
public class SearchActionConsole implements ActionConsole{

	Scanner scanner = new Scanner(System.in);
	
	@Override
	public void execute() {
		System.out.println("Insert all the information that you want to search");
		System.out.println("*If you do not want to search a given field, left it EMPTY pressing just ENTER");
		Identity newIdentity = readDataIdentityConsole();
		IdentityDAOInterface dao = new IdentityXmlDAO();
		List<Identity> list = dao.search(newIdentity);
		System.out.println("Identities found:");
		Identity.printIdentityHeaders();
		for (Identity identity : list){
			System.out.println(identity.toReadableString());
		}	
	}
	private Identity readDataIdentityConsole(){
		String displayName, email, uid;
		System.out.println("Insert the Display Name:");
		displayName = scanner.nextLine();
		
		System.out.println("Insert the Email:");
		email = scanner.nextLine();
						
		System.out.println("Insert the UID:");
		uid = scanner.nextLine();
		
		Identity identity = new Identity(uid,email,displayName);
		return identity;
	}
}
