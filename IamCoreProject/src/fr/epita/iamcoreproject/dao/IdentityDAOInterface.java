package fr.epita.iamcoreproject.dao;

import java.util.List;

import fr.epita.iamcoreproject.dao.exceptions.DaoUpdateException;
import fr.epita.iamcoreproject.datamodel.Identity;

/**This is an interface for data access object implementations.
 * @author Adriana Santalla and David Cechak
 * @version 1;
 */
public interface IdentityDAOInterface {

	//functions to manage the basic operation with identities
	public void create(Identity identity);
	public List<Identity> readAll();
	public List<Identity> search(Identity criteria);
	public void update(Identity identity) throws DaoUpdateException;
	public void delete(Identity identity);
	
	//function to find and specific user
	public List<Identity> findIdentity(Identity criteria);
	
	//function to "bind" an identity stored and an identity comming from GUI
	public Identity bindIdentities(Identity identityStored, Identity newIdentity);
}
