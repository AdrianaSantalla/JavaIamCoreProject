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
 * This class <code>CreateActionConsole</code> implements the interface <code>ActionConsole</code> and
 * thus it has the <code>execute()</code> method.
 * 
 * <p>Access to an XML file is needed to be able to create a new Identity coming from the user interface.
 * This can be achieved using <code>IdentityXmlDAO</code> and its <code>IdentityXmlDAO.create(Identity identity)</code>:
 *  
 * <blockquote><pre>{@code
 *     IdentityDAOInterface dao = new IdentityXmlDAO();
 *     dao.create(identity)}
 *     </pre>
 * </blockquote>
 *  
 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO 
 * @author Adriana Santalla and David Cechak
 * @version 1;
 */
public class CreateActionConsole implements ActionConsole{
	
	Scanner scanner = new Scanner(System.in);
	/**
	 * Method to execute the <code>CreateActionConsole</code> to create a new Identity.
	 * 
	 * <p>The creation of a new Identity process, needs first the information of the new Identity.
	 * 
	 * <p>The internal method <code>readDataIdentityConsoleValidating()</code> uses the Scanner 
	 * to get all the necessary information to create a new Identity.
	 * 
	 * <p>When all the data has been inserted, the information for fields password and type 
	 * that belong to concept of user are added. The default values for new Identities are
	 * password="" and type="normal".
	 * 
	 * <p>In following versions, the functions to create, update and delete for users and the separation
	 * between users and identities could be implemented.
	 * 
	 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO 
	 */
	@Override
	public void execute() {
		//Creating an instance of the DAO that will provide access to the data storage
		IdentityDAOInterface dao = new IdentityXmlDAO();
		//getting the new Identity from the Console
		Identity newIdentity = readDataIdentityConsoleValidating();
		//completing data
		newIdentity.setPassword("");
		newIdentity.setType("normal");
		//creating Identity in the XML File
		dao.create(newIdentity);
		System.out.println("Identity succesfully created!");
	}
	/**
	 * Internal method to validate all the data inserted by the user through the console.
	 * <p>All the data is validated to avoid bad data inserted.
	 * <p>It is <b>important</b> to remark that the user can insert the <code>Identity.uid</code>, 
	 * but the application verifies that this uid has not been registered before.
	 * 
	 * @return Identity Created based in the information inserted by the user
	 */
	private Identity readDataIdentityConsoleValidating(){
		//Creating an instance of the DAO that will provide access to the data storage
		IdentityDAOInterface dao = new IdentityXmlDAO();
		//Declaring variables needed to read the data
		String displayName, email, uid, birthdate;
		Date date = null;
		//verifying that  the displayName is not empty
		do{
			System.out.println("Insert the Display Name:");
			displayName = scanner.nextLine();
		}
		while(displayName.equals(""));
		//verifying that  the email is not empty and that is a valid email address	
		do{
			System.out.println("Insert the Email:");
			email = scanner.nextLine();
			if(!email.equals("")){
				if(!isValidEmailAddress(email)){
					System.out.println("Insert a valid email address.");
					email="";
				}
			}
		}
		while(email.equals(""));
		//verifying that the uid is not empty and that it has not been registered before	
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
		//verifying that the birthdate is not empty and that is a valid date	
		do{
			System.out.println("Insert the Birthdate (dd/MM/yyyy):");
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
		//creating the new identity with all the data validated
		Identity identity = new Identity(uid,email,displayName,date);
		return identity;
	}
	/**
	 * This method was taken from:
	 * {@link http://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method}
	 * 
	 * It verifies whether an email is valid or not
	 * 
	 * @param email An string that contains the email
	 * @return boolean True if the email is valid
	 */
	public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
	}
}
