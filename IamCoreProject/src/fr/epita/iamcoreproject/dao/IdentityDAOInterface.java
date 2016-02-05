package fr.epita.iamcoreproject.dao;

import java.util.List;

import fr.epita.iamcoreproject.dao.exceptions.DaoUpdateException;
import fr.epita.iamcoreproject.datamodel.Identity;

public interface IdentityDAOInterface {

	//functions to manage the basic operation with identities
	public void create(Identity identity);
	public List<Identity> readAll();
	public List<Identity> search(Identity criteria);
	public void update(Identity identity) throws DaoUpdateException;
	public void delete(Identity identity);
	
	//functions to manage the authentication
	public List<Identity> findUser(Identity criteria);
}
