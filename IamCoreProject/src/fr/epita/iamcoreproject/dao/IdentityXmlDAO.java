package fr.epita.iamcoreproject.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
import fr.epita.iamcoreproject.services.match.impl.EqualsIdentityMatcher;
import fr.epita.iamcoreproject.services.match.impl.StartsWithIdentityMatcher;

/** Class to manage all the manipulations with storage
 * @author Adriana Santalla and David Cechak
 * @version 1
 */
public class IdentityXmlDAO implements IdentityDAOInterface {

	Document document;
	private Matcher<Identity> activeMatchingStrategy = new StartsWithIdentityMatcher();
	
	/** Costructor
	 * @param dbf instance of document builder factory
	 * @param db instance of document builder
	 * @param properties gets the properties out of a configuration file
	 * @param xmlFile gets the xml file
	 */
	public IdentityXmlDAO() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Properties properties = getPropertiesConfigurationFile();
			// getting the property values
			String xmlFile = properties.getProperty("xmlFile");
			
			document = db.parse(new File(xmlFile));
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (document != null){
				document.getDocumentElement();
			}
		}
	}
	
	/** Gets the properties out of a configuration file
	 * @param file the configuration file
	 * @param fileInput managing the file input
	 * @param properties loads the file input
	 * @return properties
	 */
	private static Properties getPropertiesConfigurationFile() throws IOException {
		File file = new File("config.properties");
		FileInputStream fileInput = new FileInputStream(file);
		Properties properties = new Properties();
		properties.load(fileInput);
		fileInput.close();
		return properties;
	}

	/** This is creating an anonymous implementation of the Matcher interface 
	 * and instantiating it at the same time
	 */
	public List<Identity> readAll() {

		return internalSearch(null, new Matcher<Identity>(){
			public boolean match(Identity criteria, Identity toBeMatched) {
				return true;
			}
		});
	}
	
	/** Read identities from the XML and matching them with criteria
	 * @param resultList list to keep identities in
	 * @param identitiesList identities from XML
	 * @param length length of identitiesList
	 * @param identity a single element from identitiesList
	 * @param identityInstance single identity read from XML element to by matched and optionally added to the resultList
	 * @return resultList
	 */
	private List<Identity> internalSearch(Identity criteria, Matcher<Identity> identityMatcher){
		ArrayList<Identity> resultList = new ArrayList<Identity>();
		NodeList identitiesList = document.getElementsByTagName("identity");
		int length = identitiesList.getLength();
		for (int i = 0; i < length; i++) {
			Element identity = (Element) identitiesList.item(i);
			Identity identityInstance = readIdentityFromXmlElement(identity);
			if(identityMatcher.match(criteria, identityInstance)){
				resultList.add(identityInstance);
			}
		}
		return resultList;		
	}

	/** Read the attributes of identity from the XML and returning an Identity
	 * @param properties stores elements from XML
	 * @param identityInstance identity instance to read into and return
	 * @param simpleDateFormat to manage a date format of birthDate
	 * @param property gets the element from XML
	 * @param attribute name of property
	 * @param value gets text content of the property
	 * @param parsedDate to manage a date format of birthDate
	 * @return identityInstance
	 */
	private Identity readIdentityFromXmlElement(Element identity) {
		NodeList properties = identity.getElementsByTagName("property");
		Identity identityInstance = new Identity();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		for (int j = 0; j < properties.getLength(); j++) {
			Element property = (Element) properties.item(j);
			String attribute = property.getAttribute("name");
			String value = property.getTextContent().trim();
			if(attribute.equals("birthDate"))
			{
				try {
					Date parsedDate = simpleDateFormat.parse(value);
					identityInstance.setBirthDate(parsedDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			else
			{
				Class<?> clazz = identityInstance.getClass();
				String capitalizedAttribute = Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
				try {
					Method method = clazz.getMethod("set"+capitalizedAttribute,String.class);
					method.invoke(identityInstance, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return identityInstance;
	}

	/** Search
	 * @return List<Identity>
	 */
	public List<Identity> search(Identity criteria) {
		return internalSearch(criteria, activeMatchingStrategy);
	}
	
	/** Search used for authentication to match login details
	 * @return List<Identity>
	 */
	public List<Identity> findIdentity(Identity criteria) {
		Matcher<Identity> activeMatchingStrategy = new EqualsIdentityMatcher();
		return internalSearch(criteria, activeMatchingStrategy);
	}

	/** Register an Identity into a XML file
	 * @param simpleDateFormat
	 * @param identitiesTag
	 * @param newIdentity
	 * @param displayNameProperty
	 * @param emailProperty
	 * @param uidProperty
	 * @param bithdateProperty
	 * @param passwordProperty
	 * @param typeProperty
	 */
	@Override
	public void create(Identity identity) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Element identitiesTag = document.getDocumentElement();
		Element newIdentity = document.createElement("identity");
		
		Element displayNameProperty = document.createElement("property");
		displayNameProperty.setAttribute("name","displayName");
		displayNameProperty.setTextContent(identity.getDisplayName());
		
		Element emailProperty = document.createElement("property");
		emailProperty.setAttribute("name","email");
		emailProperty.setTextContent(identity.getEmail());
		
		Element uidProperty = document.createElement("property");
		uidProperty.setAttribute("name","uid");
		uidProperty.setTextContent(identity.getUid());
		
		Element bithdateProperty = document.createElement("property");
		bithdateProperty.setAttribute("name","birthDate");
		bithdateProperty.setTextContent(simpleDateFormat.format(identity.getBirthDate()));
		
		Element passwordProperty = document.createElement("property");
		passwordProperty.setAttribute("name","password");
		passwordProperty.setTextContent(identity.getPassword());
		
		Element typeProperty = document.createElement("property");
		typeProperty.setAttribute("name","type");
		typeProperty.setTextContent(identity.getType());
		
		newIdentity.appendChild(displayNameProperty);
		newIdentity.appendChild(emailProperty);
		newIdentity.appendChild(uidProperty);
		newIdentity.appendChild(bithdateProperty);
		newIdentity.appendChild(passwordProperty);
		newIdentity.appendChild(typeProperty);
		
		
		identitiesTag.appendChild(newIdentity);
		
		modifyXmlFile();
        
	}

	/** Modifies a XML file when creating, updating or deleting
	 * @param source
	 * @param transformerFactory
	 * @param transformer
	 * @param result
	 */
	private void modifyXmlFile() throws TransformerFactoryConfigurationError {
		DOMSource source = new DOMSource(document);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			StreamResult result = new StreamResult("identities.xml");
	        transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Updates the XML file using delete and create
	 * @param identityUpdated identity being updated
	 */
	@Override
	public void update(Identity identityUpdated) throws DaoUpdateException {
		delete(identityUpdated);
		create(identityUpdated);
	}

	/** Finds and deletes identity using UID
	 * @param identityToDelete
	 * @param identitiesList stores elements from XML
	 * @param length length of identitiesList from XML
	 * @param identity single identity from XML
	 * @param properties 
	 * @param property to compare the UIDs
	 * @param identitiesTag to remove the identity
	 */
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
				if(property.getAttribute("name").equals("uid") && property.getTextContent().trim().equals(identityToDelete.getUid()))
				{
					Element identitiesTag = document.getDocumentElement();
					identitiesTag.removeChild(identity);
					modifyXmlFile();
					break outerloop;
				}
			}
		}
	}
	
	/** Compares stored identity with a new one, updates the stored attributes with attributes from new, 
	 * if any, and returns updated result
	 * @param identityStored
	 * @param newIdentity
	 * @param uid
	 * @param displayName
	 * @param email
	 * @param birthdate
	 * @return identityStored
	 */
	public Identity bindIdentities(Identity identityStored, Identity newIdentity){
		String uid, displayName, email;
		Date birthdate;
		uid = newIdentity.getUid();
		if(!uid.equals(""))
			identityStored.setUid(uid);
		displayName = newIdentity.getDisplayName();
		if(!displayName.equals(""))
			identityStored.setDisplayName(displayName);
		email = newIdentity.getEmail();
		if(!email.equals(""))
			identityStored.setEmail(email);
		birthdate = newIdentity.getBirthDate();
		if(birthdate != null)
			identityStored.setBirthDate(birthdate);
		return identityStored;
	}

}