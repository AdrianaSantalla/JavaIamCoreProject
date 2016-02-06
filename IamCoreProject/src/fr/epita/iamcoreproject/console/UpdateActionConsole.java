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
	 * the displayName, email, uid and birthdate. The application ask for these fields but if the user just want to
	 * search by email, he/she just have to leave empty the field asked.
	 * 
	 * <p>The <code>IdentityXmlDAO.search()</code> method has the implementation to search by all the
	 * identities that matches any of the criteria.
	 * 
	 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO
	 * @see fr.epita.iamcoreproject.services.match.impl.StartsWithIdentityMatcher
	 * @see fr.epita.iamcoreproject.services.match.impl.ContainsIdentityMatcher
	 */
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
					Identity newIdentity = readDataIdentityConsole();
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
	
	private Identity readDataIdentityConsole(){
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
		while(birthdate.equals(null));
		
		Identity identity = new Identity(uid,email,displayName,date);
		return identity;
	}

}
