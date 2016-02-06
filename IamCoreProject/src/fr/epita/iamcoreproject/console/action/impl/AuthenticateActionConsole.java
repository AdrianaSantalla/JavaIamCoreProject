/**
 * 
 */
package fr.epita.iamcoreproject.console.action.impl;

import java.util.Scanner;

import fr.epita.iamcoreproject.console.action.ActionConsole;
import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
import fr.epita.iamcoreproject.datamodel.Identity;

/**
 * This class <code>AuthenticateActionConsole</code> implements the interface <code>ActionConsole</code> and
 * thus it has the <code>execute()</code> method.
 * 
 * <p>When this class is executed, first the username and password of the user trying to execute the application,
 * are asked.
 *  
 * <p>In this project data is persisted in an XML file. To access data this data the <code>IdentityXmlDAO</code>
 * class is needed. For example, it can be created using:
 *  
 * <blockquote><pre>{@code
 *     IdentityDAOInterface dao = new IdentityXmlDAO();
 * }</pre></blockquote>
 * 
 * <p>This instance allows the access to the data. Here the function <code>IdentityXmlDAO.findIdentity(Identity user)</code> 
 * is used to compare username password matching. This method uses the matcher equals by default. 
 *  
 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO 
 * @author Adriana Santalla and David Cechak
 * @version 1;
 */
public class AuthenticateActionConsole implements ActionConsole{
	//scanner to read the username and password from the user
	Scanner scanner = new Scanner(System.in);
	
	/**
	 * Method to execute the <code>AuthenticateActionConsole</code> to authenticate the user
	 * 
	 * <p>An important assumption here, is that the <b>displayName</b> property from <code>Identity</code> is
	 * considered as the <b>username</b> and that we just let Identities with the property type="<b>admin</b>" to
	 * access the application.
	 * 
	 * <p>The authentication is accomplished comparing the data inserted by the user using <code>Scanner</code>
	 * and the data coming from the data storage using an instance of <code>IdentityXmlDAO</code> and its method 
	 * <code>IdentityXmlDAO.findIdentity(Identity user)</code>
	 * 
	 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO 
	 */
	@Override
	public void execute() {
		//Creating an instance of the DAO that will provide access to the data storage
		IdentityDAOInterface dao = new IdentityXmlDAO();
		boolean state = false;
		//while loop to keep asking the user an username and password when the authentication fails
		while(!state){
			//Asking for user input
			System.out.println("\nAuthentication");
			System.out.println("Please enter your username: ");
			String username = scanner.nextLine();
			System.out.println("Please enter your password: ");
			String password = scanner.nextLine();
			//creating a new identity to find it
			Identity user = new Identity();
			//here we use the property displayName for the username
			user.setDisplayName(username);
			//checking if the given username exits in the XML file
			if(!dao.findIdentity(user).isEmpty())
			{
				//getting the first user in the resulting list 
				user = dao.findIdentity(user).get(0);
				//verifying if it is an admin user and that the password matches
				if(user.getType().equals("admin") && user.getPassword().equals(password)) {
					System.out.println("\nWelcome "+username);
					state = true;
				}
				else {
					//printing error message
					System.out.println("\nAuthentication failed");
					state = false;
				}
			}
			else {
				//printing error message
				System.out.println("\nUnexisting user");
				state = false;
			}
		}		
	}
}