package fr.epita.iamcoreproject.dao;

import java.io.File;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import fr.epita.iamcoreproject.services.match.impl.EqualsIdentityMatcher;
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
		} finally{
			if (document != null){
				document.getDocumentElement();
			}
		}
	}

	public List<Identity> readAll() {

		//This is creating an anonymous implementation of the Matcher interface and 
		//instantiating it at the same time
		return internalSearch(null, new Matcher<Identity>(){
			public boolean match(Identity criteria, Identity toBeMatched) {
				return true;
			}
		});
	}
	
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
					// TODO Check if the birthDate should provoke the cancellation of the current identity reading
					e.printStackTrace();
				}
			}
			else
			{
				Class<?> clazz = identityInstance.getClass();
				//clazz.getDeclaredField(attribute);
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

	public List<Identity> search(Identity criteria) {
		return internalSearch(criteria, activeMatchingStrategy);
	}
	
	public List<Identity> findIdentity(Identity criteria) {
		Matcher<Identity> activeMatchingStrategy = new EqualsIdentityMatcher();
		return internalSearch(criteria, activeMatchingStrategy);
	}

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