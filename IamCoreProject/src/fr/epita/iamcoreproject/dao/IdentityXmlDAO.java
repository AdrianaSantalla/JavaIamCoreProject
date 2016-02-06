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

import fr.epita.iamcoreproject.dao.exceptions.ReflectionException;
import fr.epita.iamcoreproject.datamodel.Identity;
import fr.epita.iamcoreproject.services.match.Matcher;
import fr.epita.iamcoreproject.services.match.impl.ContainsIdentityMatcher;
import fr.epita.iamcoreproject.services.match.impl.EqualsIdentityMatcher;
import fr.epita.iamcoreproject.services.match.impl.StartsWithIdentityMatcher;

/** Class to manage all the manipulations with storage.
 * <p>The <b>path to the XML</b> file to persist the data is in the config.properties that is the file 
 * configuration of this project.
 * 
 * <p>The <code>activeMatchingStrategy</code> property of this class is used to set the pattern according
 * to which the search function will work. This attribute is taken from config.properties file of the project
 *   
 * @author Adriana Santalla and David Cechak
 * @version 1
 */
public class IdentityXmlDAO implements IdentityDAOInterface {

	Document document;
	private Matcher<Identity> activeMatchingStrategy;
	
	/** <b>Constructor</b>. Creates <code>DocumentBuilderFactory</code> and <code>DocumentBuilder</code> instance to access the XML File.
	 * 	
	 * 	<p>When the constructor is executed, it also initializes the property <code>document</code> and
	 * 	<code>activeMatchingStrategy</code>, getting the properties out of the configuration file
	 */
	public IdentityXmlDAO() {
		try {
			//Object to access the XML File
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Class properties to read the configuration file
			Properties properties = getPropertiesConfigurationFile();
			// getting the property values
			String xmlFile = properties.getProperty("xmlFile");
			String activeMatchingStrategy = properties.getProperty("activeMatchingStrategy");
			//checking if the property was found
			if(activeMatchingStrategy!=null){
				// instantiating the activeMatchingStrategy according to the configuration
				switch (activeMatchingStrategy) {
				case "strartsWith": this.activeMatchingStrategy = new StartsWithIdentityMatcher();
					break;
				case "contains": this.activeMatchingStrategy = new ContainsIdentityMatcher();
					break;
				case "equals": this.activeMatchingStrategy = new EqualsIdentityMatcher();
					break;
				default: this.activeMatchingStrategy = new StartsWithIdentityMatcher();
					break;
				}
			}
			else{
				this.activeMatchingStrategy = new StartsWithIdentityMatcher();
			}
			document = db.parse(new File(xmlFile));
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (document != null){
				document.getDocumentElement();
			}
		}
	}
	
	/** Internal Method to get the properties out of the configuration file. It looks for the file
	 *  and loads the result in the class Properties.
	 * @exception IOException
	 * @return properties
	 */
	private Properties getPropertiesConfigurationFile() throws IOException {
		File file = new File("config.properties");
		FileInputStream fileInput = new FileInputStream(file);
		Properties properties = new Properties();
		properties.load(fileInput);
		fileInput.close();
		return properties;
	}

	/** This method is creating an anonymous implementation of the Matcher interface 
	 * 	and instantiating it at the same time forcing the match result to be <b>true</b>
	 * <p>This action allows to avoid code replication because the search method has to 
	 * go through all the XML file to find matches. The method <code>readAll</code> does the
	 * same but it does not compare anything.
	 * <p>That is the reason why here, a Matcher that returns always true without making
	 * any comparison is created. This forces the internal search method to return true for all the
	 * identities found.
	 */
	public List<Identity> readAll() {

		return internalSearch(null, new Matcher<Identity>(){
			public boolean match(Identity criteria, Identity toBeMatched) {
				return true;
			}
		});
	}
	
	/**
	 * Internal method that given an <code>NodeList</code> object, it iterates for all 
	 * the childs with the <b>identity</b> tag name.
	 * To split the properties from every child, is uses another internal method 
	 * <code>readIdentityFromXmlElement()</code>
	 * 
	 * <p>When this method returns the Identity object created, it is compared with the criteria.
	 * The matches are saved in the <code>ArrayList</code> resultList which is returned.
	 * @param criteria <code>Identity</code>
	 * @param identityMatcher <code>Matcher</code>
	 * @return resultList <code>ArrayList</code>
	 */
	private List<Identity> internalSearch(Identity criteria, Matcher<Identity> identityMatcher){
		ArrayList<Identity> resultList = new ArrayList<Identity>();
		NodeList identitiesList = document.getElementsByTagName("identity");
		int length = identitiesList.getLength();
		for (int i = 0; i < length; i++) {
			Element identity = (Element) identitiesList.item(i);
			Identity identityInstance = null;
			try {
				identityInstance = readIdentityFromXmlElement(identity);
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
			if(identityMatcher.match(criteria, identityInstance)){
				resultList.add(identityInstance);
			}
		}
		return resultList;		
	}

	/**
	 * Internal method that read the properties attributes from a child with tag name <b>property</b>.
	 * <p>In this method we use <b>reflection</b> to call the setters of given object. In this case
	 * the object is the Identity and we call the getters for all their attributes to avoid having a big 
	 * switch case asking for every property of Identity.
	 * 
	 * <p>In the case of the birthDate we cannot call its method using directly the reflection because we
	 * to transform the <code>String</code> value into <code>date</code>
	 * 
	 * @param Element
	 * @return Identity
	 * @throws ReflectionException 
	 */
	private Identity readIdentityFromXmlElement(Element identity) throws ReflectionException {
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
			{	//setting the attributes using reflection
				//we create an anonymous class that gets the type of our identity
				Class<?> clazz = identityInstance.getClass();
				//we capitalize the attribute to call the setter
				String capitalizedAttribute = Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
				Method method = null;
				try {
					//we call the setter of the given attribute
					method = clazz.getMethod("set"+capitalizedAttribute,String.class);
					method.invoke(identityInstance, value);
				} catch (Exception e) {
					//customized exception that takes the method causing the exception
					ReflectionException reflectionException = new ReflectionException(method);
					reflectionException.initCause(e);
					throw reflectionException;
				}
			}
		}
		return identityInstance;
	}

	/** Search
	 * @param criteria identity used to compare with while searching
	 * @return List<Identity>
	 */
	public List<Identity> search(Identity criteria) {
		return internalSearch(criteria, activeMatchingStrategy);
	}
	
	/** Search used for authentication to match login details
	 * @param criteria identity used to compare with while searching
	 * @return List<Identity>
	 */
	public List<Identity> findIdentity(Identity criteria) {
		Matcher<Identity> activeMatchingStrategy = new EqualsIdentityMatcher();
		return internalSearch(criteria, activeMatchingStrategy);
	}

	/** Register an Identity into a XML file
	 * This function should be optimized using <b>reflection</b> again, to call getters of the identity.
	 * <p>Under development for the next version 
	 * @param identity to be written down to XML
	 */
	@Override
	public void create(Identity identity) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Element identitiesTag = document.getDocumentElement();
		Element newIdentity = document.createElement("identity");
		
		//creating a child property with every attribute of identity
		
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
		
		//adding childs properties to identity
		newIdentity.appendChild(displayNameProperty);
		newIdentity.appendChild(emailProperty);
		newIdentity.appendChild(uidProperty);
		newIdentity.appendChild(bithdateProperty);
		newIdentity.appendChild(passwordProperty);
		newIdentity.appendChild(typeProperty);
		
		identitiesTag.appendChild(newIdentity);
		modifyXmlFile();        
	}

	/** Modifies an XML file when creating, updating or deleting
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

	/** Updates the XML file using delete and create to optimize code
	 * @param identityUpdated identity being updated
	 */
	@Override
	public void update(Identity identityUpdated) throws ReflectionException {
		delete(identityUpdated);
		create(identityUpdated);
	}

	/** Finds and deletes identity using UID
	 * @param identityToDelete
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
	 * if any, and returns updated result.
	 * <p>This function helps to update a given identity letting the user either insert new values or either
	 * left them empty.
	 * <p>
	 * @param identityStored
	 * @param newIdentity
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