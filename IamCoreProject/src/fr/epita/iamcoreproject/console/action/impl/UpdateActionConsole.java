/**
 * 
 */
package fr.epita.iamcoreproject.console.action.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import fr.epita.iamcoreproject.console.action.ActionConsole;
import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
import fr.epita.iamcoreproject.datamodel.Identity;

/**
 * This class <code>UpdateActionConsole</code> implements the interface <code>ActionConsole</code> and
 * thus it has the <code>execute()</code> method.
 * 
 * <p>Access to an XML file is needed to be able to update an Identity coming from the user interface. 
 * This can be achieved using <code>IdentityXmlDAO</code> and its <code>
 * IdentityXmlDAO.update(Identity identity)</code>:
 *  
 * <blockquote><pre>{@code
 *     IdentityDAOInterface dao = new IdentityXmlDAO();
 *     dao.serach(identity)}
 *     </pre>
 * </blockquote>
 *  
 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO 
 * @author Adriana Santalla and David Cechak
 * @version 1;
 *
 */
public class UpdateActionConsole implements ActionConsole{
	
	Scanner scanner = new Scanner(System.in);
	/**
	 * Method to execute the <code>UpdateActionConsole</code> to search an Identity.
	 * 
	 * <p>The update function of an Identity process first needs the information of the Identity to update.
	 * 
	 * <p>The <code>Scanner</code> is used to ask the user for the uid from the identity that has to be 
	 * updated. If the user does not introduce a valid uid, the application keeps asking.
	 * 
	 * <p>The <code>Scanner</code> is used to ask the user for the new data to update. The user can update
	 * the displayName, email, uid and birthdate. The application ask for these fields. If the user does
	 * not want to update a given field, he/she just have to leave empty the field asked.
	 * 
	 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO
	 * @see fr.epita.iamcoreproject.services.match.impl.StartsWithIdentityMatcher
	 * @see fr.epita.iamcoreproject.services.match.impl.ContainsIdentityMatcher
	 */
	@Override
	public void execute() {
		//Creating an instance of the DAO that will provide access to the data storage
		IdentityDAOInterface dao = new IdentityXmlDAO();
		String uid;
		//do while loop to ask for a valid uid
		do {
			System.out.println("Insert the UID of the Identity you want to update:");
			//reading the uid from console
			uid = scanner.nextLine();
			//creating an identity to look for it
			Identity identity = new Identity();
			identity.setUid(uid);
			//if it has been found, try to update
			List<Identity> identityFound = dao.findIdentity(identity);
			if(!identityFound.isEmpty()){
				//Showing the data from the identity to be updated
				System.out.println("This is the actual data of the Identity:");
				identity = identityFound.get(0);
				System.out.println(identity.toReadableString());
				//verifying that is not an admin account
				if(!identity.getType().equals("admin")) {
					System.out.println("\nInsert the new information for the identity");
					System.out.println("*If you do not want to update the a given field, left it EMPTY pressing just ENTER");
					//reading the new data
					Identity newIdentity = readDataIdentityConsole();
					//delete the old identity
					dao.delete(identity);
					//merging the identities
					Identity combinedIdentity = dao.bindIdentities(identity, newIdentity);
					//save the new identity merged with old and new data
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
	/**
	 * Internal method to validate all the data inserted by the user through the console.
	 * <p>All the data is validated to avoid bad data inserted.
	 * <p>It is <b>important</b> to remark that the user can insert the <code>Identity.uid</code>, 
	 * but the application verifies that this uid has not been registered before.
	 * 
	 * In this case we allow empty data because the user might not want to update all fields, but if he/she
	 * enters a new value, is validated to be correct
	 * 
	 * @return Identity Created based in the information inserted by the user
	 * @return Identity 
	 */
	private Identity readDataIdentityConsole(){
		//Creating an instance of the DAO that will provide access to the data storage
		IdentityDAOInterface dao = new IdentityXmlDAO();
		//Declaring variables needed to read the data
		String displayName, email, uid, birthdate;
		Date date = null;
		System.out.println("Insert the Display Name:");
		displayName = scanner.nextLine();
		//verifying that if an email is inserted, is valid
		do{
			System.out.println("Insert the Email:");
			email = scanner.nextLine();
			if(!email.equals("")){
				if(!CreateActionConsole.isValidEmailAddress(email)){
					System.out.println("Insert a valid email address.");
					email=null;
				}
			}
		}
		while(email.equals(null));
		//validating that if an uid is inserted, is valid			
		do {
			System.out.println("Insert the UID:");
			uid = scanner.nextLine();
			if(!uid.equals("")){
				Identity identity = new Identity();
				identity.setUid(uid);
				if(!dao.findIdentity(identity).isEmpty()){
					System.out.println("UID already registered. Insert an another one.");
					uid=null;
				}
			}
		}
		while(uid==null);
		//validating that if a birthdate is inserted, is valid
		do{
			System.out.println("Insert the Birthdate (dd/MM/yyyy):");
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
		while(birthdate.equals(null));
		//creating the new identity with all the data validated
		Identity identity = new Identity(uid,email,displayName,date);
		return identity;
	}

}
