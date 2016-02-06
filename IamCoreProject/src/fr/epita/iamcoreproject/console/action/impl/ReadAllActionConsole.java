/**
 * 
 */
package fr.epita.iamcoreproject.console.action.impl;

import java.util.List;

import fr.epita.iamcoreproject.console.action.ActionConsole;
import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
import fr.epita.iamcoreproject.datamodel.Identity;

/**
 * This class <code>ReadAllActionConsole</code> implements the interface <code>ActionConsole</code> and
 * thus it has the <code>execute()</code> method.
 * 
 * <p>Access to an XML file is needed to be able to read all the identities.
 * This can be achieved using <code>IdentityXmlDAO</code> and its <code>IdentityXmlDAO.readAll()</code>:
 *  
 * <blockquote><pre>{@code
 *     IdentityDAOInterface dao = new IdentityXmlDAO();
 *     dao.readAll()}
 *     </pre>
 * </blockquote>
 *  
 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO 
 * @author Adriana Santalla and David Cechak
 * @version 1;
 *
 */
public class ReadAllActionConsole implements ActionConsole{
	/**
	 * Method to execute the <code>ReadAllActionConsole</code> to show all Identities.
	 * 
	 * @see fr.epita.iamcoreproject.dao.IdentityXmlDAO
	 */
	@Override
	public void execute() {
		IdentityDAOInterface dao = new IdentityXmlDAO();
		//using dao to get all the identities
		List<Identity> list = dao.readAll();
		//printing the names of attributes
		Identity.printIdentityHeaders();
		for (Identity identity : list){
			//using the method toReadableString from Identity to show data understandable for the user
			System.out.println(identity.toReadableString());
		}
	}	
}
