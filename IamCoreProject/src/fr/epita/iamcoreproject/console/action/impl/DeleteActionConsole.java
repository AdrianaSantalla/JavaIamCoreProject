/**
 * 
 */
package fr.epita.iamcoreproject.console.action.impl;

import java.util.List;
import java.util.Scanner;

import fr.epita.iamcoreproject.console.action.ActionConsole;
import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
import fr.epita.iamcoreproject.datamodel.Identity;

/**
 * This class <code>DeleteActionConsole</code> implements the interface <code>ActionConsole</code> and
 * thus it has the <code>execute()</code> method.
 * 
 * <p>Access to an XML file is needed to be able to delete an Identity coming from the user interface.
 * This can be achieved using <code>IdentityXmlDAO</code> and its <code>IdentityXmlDAO.delete(Identity identity)</code>:
 *  
 * <blockquote><pre>{@code
 *     IdentityDAOInterface dao = new IdentityXmlDAO();
 *     dao.delete(identity)}
 *     </pre>
 * </blockquote>
 *  
 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO 
 * @author Adriana Santalla and David Cechak
 * @version 1;
 *
 */
public class DeleteActionConsole implements ActionConsole {
	
	Scanner scanner = new Scanner(System.in);
	/**
	 * Method to execute the <code>DeleteActionConsole</code> to delete an Identity.
	 * 
	 * <p>The deletion of an Identity process first needs the information of the Identity to delete.
	 * 
	 * <p>The <code>Scanner</code> is used to ask the user for the uid from the identity that has to be 
	 * deleted. If the user does not introduce a valid uid, the application keeps asking.
	 * 
	 * <p>When the uid is found, a message of confirmation is shown. The user has to insert "y" or "n" as 
	 * valid answers, in other case the application keeps asking for a valid option.
	 * 
	 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO 
	 */
	@Override
	public void execute() {
		//Creating an instance of the DAO that will provide access to the data storage
		IdentityDAOInterface dao = new IdentityXmlDAO();
		String uid;
		//do while loop to ask for a valid uid
		do {
			System.out.println("Insert the UID of the Identity you want to delete:");
			//reading the uid from console
			uid = scanner.nextLine();
			Identity identity = new Identity();
			//creating an identity to look for it	
			identity.setUid(uid);
			List<Identity> identityFound = dao.findIdentity(identity);
			//if it has been found, try to delete
			if(!identityFound.isEmpty()){
				//Showing the data from the identity to be deleted
				System.out.println("\nThis is the actual data of the Identity:");
				identity = identityFound.get(0);
				System.out.println(identity.toReadableString());
				//verifying that is not an admin account
				if(!identity.getType().equals("admin")) {
					String confirmation;
					//showing confirmation message in do while to keep asking if is an invalid option
					System.out.println("Are you sure you want to delete it? \ny | n");
					do{
						confirmation = scanner.nextLine();
						if(confirmation.equals("y")){
							//deleting identity using dao
							dao.delete(identity);
							System.out.println("Identity deleted");
						}
						else if(confirmation.equals("n")){
							System.out.println("Deletion cancelled");
						}
						else{
							System.out.println("Insert a valid option (y | n)");
							confirmation="";
						}
					}
					while(confirmation=="");				
				}
				else{
					System.out.println("You cannot delete admins' accounts");
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
}
