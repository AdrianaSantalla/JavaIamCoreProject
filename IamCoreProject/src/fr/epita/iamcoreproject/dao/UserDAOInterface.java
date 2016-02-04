/**
 * 
 */
package fr.epita.iamcoreproject.dao;

import java.util.List;
import fr.epita.iamcoreproject.datamodel.User;

/**
 * @author Adriana Santalla
 *
 */
public interface UserDAOInterface {
public void create(User identity);
	
	public List<User> readAll();
	public List<User> search(User criteria);
	public void update(User identity);
	public void delete(User identity);
}
