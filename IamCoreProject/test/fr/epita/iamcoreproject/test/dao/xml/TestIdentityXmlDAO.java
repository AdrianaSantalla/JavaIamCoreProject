package fr.epita.iamcoreproject.test.dao.xml;

import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
import fr.epita.iamcoreproject.dao.exceptions.DaoUpdateException;
import fr.epita.iamcoreproject.datamodel.Identity;

public class TestIdentityXmlDAO {
	
//	IdentityDAOInterface dao = new IdentityFileDAO();
//	static IdentityDAOInterface dao = new IdentityXmlDAO();
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
//		testSearch();
//		testCreate();
//		testDelete();
//		testUpdate();
		testReadAll();
	}

	private static void testReadAll() throws Exception {
//		IdentityDAOInterface dao = new IdentityFileDAO();
		IdentityDAOInterface dao = new IdentityXmlDAO();
		System.out.println(dao.readAll());
	}
	
	private static void testSearch() {
//		IdentityDAOInterface dao = new IdentityFileDAO();
		IdentityDAOInterface dao = new IdentityXmlDAO();
		Identity criteria1 = new Identity(null, "sg", null);
		Identity criteria2 = new Identity(null, null, "Quentin");
		System.out.println("criteria1: "+criteria1);
		System.out.println("criteria2: "+criteria2);
		System.out.println("result1:\n"+dao.search(criteria1));
		System.out.println("result2:\n"+dao.search(criteria2));
	}
	
	private static void testCreate() {
//		IdentityDAOInterface dao = new IdentityFileDAO();
		IdentityDAOInterface dao = new IdentityXmlDAO();
		Identity identity = new Identity("89498","adri@adri.com","Adriana");
		dao.create(identity);
	}
	
	private static void testDelete() {
//		IdentityDAOInterface dao = new IdentityFileDAO();
		IdentityDAOInterface dao = new IdentityXmlDAO();
		Identity identity = new Identity("89498","adri@adri.com","Adriana");
		dao.delete(identity);
	}
	
	private static void testUpdate() throws DaoUpdateException {
//		IdentityDAOInterface dao = new IdentityFileDAO();
		IdentityDAOInterface dao = new IdentityXmlDAO();
		Identity identity = new Identity("89498","adri2@adri2.com","Adriana2");
		dao.update(identity);
	}

}
