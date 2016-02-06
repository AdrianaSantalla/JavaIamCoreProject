package fr.epita.iamcoreproject.console;

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
public class DeleteActionConsole implements ActionConsole {
	
	Scanner scanner = new Scanner(System.in);
	
	@Override
	public void execute() {
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
				identity = identityFound.get(0);
				System.out.println(identity.toReadableString());
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
	
}
