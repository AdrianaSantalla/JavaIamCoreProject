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
 * This class <code>SearchActionConsole</code> implements the interface <code>ActionConsole</code> and
 * thus it has the <code>execute()</code> method.
 * 
 * <p>Access to an XML file is needed to be able to search an Identity that has a criteria to find, 
 * coming from the user interface. This can be achieved using <code>IdentityXmlDAO</code> and its <code>
 * IdentityXmlDAO.search(Identity identity)</code>:
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
public class SearchActionConsole implements ActionConsole{

	Scanner scanner = new Scanner(System.in);
	/**
	 * Method to execute the <code>SearchActionConsole</code> to search an Identity.
	 * 
	 * <p>The search function process, needs first the information/criteria to search.
	 * 
	 * <p>The <code>Scanner</code> is used to ask the user for the criteria to search. The user can search
	 * by displayName, email and uid. The application ask for the four fields but if the user just want to
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
		System.out.println("Insert all the information that you want to search");
		//message to tell the user just to enter what he/she need to search for. It can be all of them
		System.out.println("* If you do not want to search a given field, left it EMPTY pressing just ENTER");
		//reading data from console and creating and identity to send to dao
		Identity newIdentity = readDataIdentityConsole();
		//searching using dao
		IdentityDAOInterface dao = new IdentityXmlDAO();
		//receiving all the matches
		List<Identity> list = dao.search(newIdentity);
		//printing those matches
		System.out.println("Identities found:");
		//printing the names of attributes
		Identity.printIdentityHeaders();
		for (Identity identity : list){
			//using the method toReadableString from Identity to show data understandable for the user
			System.out.println(identity.toReadableString());
		}	
	}
	/**
	 * Internal method to read the three criteria to search
	 * @return Identity Created based in the information inserted by the user
	 */
	private Identity readDataIdentityConsole(){
		String displayName, email, uid;
		//reading data from console without any validation
		System.out.println("Insert the Display Name:");
		displayName = scanner.nextLine();
		
		System.out.println("Insert the Email:");
		email = scanner.nextLine();
						
		System.out.println("Insert the UID:");
		uid = scanner.nextLine();
		//creating the identity with the received data
		Identity identity = new Identity(uid,email,displayName);
		return identity;
	}
}
