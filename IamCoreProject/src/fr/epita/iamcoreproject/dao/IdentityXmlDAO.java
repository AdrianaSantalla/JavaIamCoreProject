package fr.epita.iamcoreproject.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.epita.iamcoreproject.dao.exceptions.DaoUpdateException;
import fr.epita.iamcoreproject.datamodel.Identity;
import fr.epita.iamcoreproject.services.match.Matcher;
import fr.epita.iamcoreproject.services.match.impl.StartsWithIdentityMatcher;

public class IdentityXmlDAO implements IdentityDAOInterface {

	Document document;
	private Matcher<Identity> activeMatchingStrategy = new StartsWithIdentityMatcher();
	
	public IdentityXmlDAO() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			document = db.parse(new File("identities.xml"));
		} catch (Exception e) {
			e.printStackTrace();
			// TODO handle exception
		}
	}

	public List<Identity> readAll() {

		ArrayList<Identity> resultList = new ArrayList<Identity>();
		NodeList identitiesList = document.getElementsByTagName("identity");
		int length = identitiesList.getLength();
		for (int i = 0; i < length; i++) {
			Element identity = (Element) identitiesList.item(i);
			NodeList properties = identity.getElementsByTagName("property");
			resultList.add(readIdentity(properties));
		}
		return resultList;
	}

	private Identity readIdentity(NodeList properties) {
		Identity identityInstance = new Identity();
		for (int j = 0; j < properties.getLength(); j++) {
			Element property = (Element) properties.item(j);
			String attribute = property.getAttribute("name");
//			System.out.println(attribute + " : "
//					+ property.getTextContent());
			String value = property.getTextContent().trim();
			switch (attribute) {
			case "displayName":
				identityInstance.setDisplayName(value);
				break;
			case "email":
				identityInstance.setEmail(value);
				break;

			case "guid":
				identityInstance.setUid(value);
				break;
			}
		}
		return identityInstance;
	}

	public List<Identity> search(Identity criteria) {
		List<Identity> resultsList = new ArrayList<Identity>();
		NodeList identitiesList = document.getElementsByTagName("identity");
		int length = identitiesList.getLength();
		for (int i = 0; i < length; i++) {
			Element identity = (Element) identitiesList.item(i);
			NodeList properties = identity.getElementsByTagName("property");
			Identity identityInstance = readIdentity(properties);
			if(activeMatchingStrategy.match(criteria, identityInstance))
				resultsList.add(identityInstance);
		}
		return resultsList;
	}

	@Override
	public void create(Identity identity) {
		Element identitiesTag = document.getDocumentElement();
		Element newIdentity = document.createElement("identity");
		
		Element displayNameProperty = document.createElement("property");
		displayNameProperty.setAttribute("name","displayName");
		displayNameProperty.setTextContent(identity.getDisplayName());
		
		Element emailProperty = document.createElement("property");
		emailProperty.setAttribute("name","email");
		emailProperty.setTextContent(identity.getEmail());
		
		Element uidProperty = document.createElement("property");
		uidProperty.setAttribute("name","guid");
		uidProperty.setTextContent(identity.getUid());
		
		newIdentity.appendChild(displayNameProperty);
		newIdentity.appendChild(emailProperty);
		newIdentity.appendChild(uidProperty);
		
		identitiesTag.appendChild(newIdentity);
		
		modifyXmlFile();
        
	}

	private void modifyXmlFile() throws TransformerFactoryConfigurationError {
		DOMSource source = new DOMSource(document);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			StreamResult result = new StreamResult("identities.xml");
	        transformer.transform(source, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Identity identityUpdated) throws DaoUpdateException {
		// TODO Auto-generated method stub
		NodeList identitiesList = document.getElementsByTagName("identity");
		int length = identitiesList.getLength();
		Element identitiesTag = document.getDocumentElement();
		
		outerloop:
		for (int i = 0; i < length; i++) {
			Element identity = (Element) identitiesList.item(i);
			NodeList properties = identity.getElementsByTagName("property");
			for (int j = 0; j < properties.getLength(); j++) {
				Element property = (Element) properties.item(j);
				if(property.getAttribute("name").equals("guid") && property.getTextContent().trim().equals(identityUpdated.getUid()))
				{
					identitiesTag.removeChild(identity);
					modifyXmlFile();
					create(identityUpdated);
					break outerloop;
				}
			}	
		}
	}

	@Override
	public void delete(Identity identityToDelete) {
		NodeList identitiesList = document.getElementsByTagName("identity");
		int length = identitiesList.getLength();
		outerloop:
		for (int i = 0; i < length; i++) {
			Element identity = (Element) identitiesList.item(i);
			NodeList properties = identity.getElementsByTagName("property");
			for (int j = 0; j < properties.getLength(); j++) {
				Element property = (Element) properties.item(j);
				if(property.getAttribute("name").equals("guid") && property.getTextContent().trim().equals(identityToDelete.getUid()))
				{
					Element identitiesTag = document.getDocumentElement();
					identitiesTag.removeChild(identity);
					modifyXmlFile();
					break outerloop;
				}
			}
		}
	}

}
