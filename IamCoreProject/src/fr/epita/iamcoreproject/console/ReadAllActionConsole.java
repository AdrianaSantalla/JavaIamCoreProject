/**
 * 
 */
package fr.epita.iamcoreproject.console;

import java.util.List;

import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
import fr.epita.iamcoreproject.datamodel.Identity;

/**
 * @author Usuario
 *
 */
public class ReadAllActionConsole implements ActionConsole{

	@Override
	public void execute() {
		IdentityDAOInterface dao = new IdentityXmlDAO();
		List<Identity> list = dao.readAll();
		Identity.printIdentityHeaders();
		for (Identity identity : list){
			System.out.println(identity.toReadableString());
		}
	}	
}
