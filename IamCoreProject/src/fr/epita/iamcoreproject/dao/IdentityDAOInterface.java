package fr.epita.iamcoreproject.dao;

import java.util.List;

import fr.epita.iamcoreproject.dao.exceptions.DaoUpdateException;
import fr.epita.iamcoreproject.datamodel.Identity;

public interface IdentityDAOInterface {

	
	public void create(Identity identity);
	
	public List<Identity> readAll();
	
	public List<Identity> search(Identity criteria);
	
	public void update(Identity identity) throws DaoUpdateException;
	public void delete(Identity identity);
	
}
