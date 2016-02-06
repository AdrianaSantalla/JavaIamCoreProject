/**
 * 
 */
package fr.epita.iamcoreproject.console;

import java.util.Scanner;

import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
import fr.epita.iamcoreproject.datamodel.Identity;

/**
 * @author Usuario
 *
 */
public class AuthenticateActionConsole implements ActionConsole{
	
	Scanner scanner = new Scanner(System.in);
	Identity currentUser;
	@Override
	public void execute() {
		IdentityDAOInterface dao = new IdentityXmlDAO();
		boolean state = false;
		while(!state){
			System.out.println("\nAuthentication");
			System.out.println("Please enter your username: ");
			String username = scanner.nextLine();
			System.out.println("Please enter your password: ");
			String password = scanner.nextLine();
			Identity user = new Identity();
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

}
