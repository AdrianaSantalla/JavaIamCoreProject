/**
 * 
 */
package fr.epita.iamcoreproject.datamodel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the main concept of the project the Identity.
 * <p>An identity has the 6 attributes. The last two are to manage the login and type of account.
 * Password is only for admin accounts, as they are they ones able to login and manage other identities.</p>
 *  
 * 
 * @author Adriana Santalla and David Cechak
 * @version 1
 */
public class Identity {
	private String uid;
	private String email;
	private String displayName;
	private Date birthDate;
	private String password;
	private String type;
	
	/**
	 * 
	 */
	public Identity() {
	}
	/**
	 * @param uid unique id of an identity
	 * @param email email address
	 * @param displayName name
	 */
	public Identity(String uid, String email, String displayName) {
		this.uid = uid;
		this.email = email;
		this.displayName = displayName;
	}
	/**
	 * @param uid unique id of an identity
	 * @param email email address
	 * @param displayName name
	 * @param birthDate date of birth
	 */
	public Identity(String uid, String email, String displayName, Date birthDate) {
		this.uid = uid;
		this.email = email;
		this.displayName = displayName;
		this.birthDate = birthDate;
	}
	
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type the type to set whether admin or normal identity 
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Function to string overwritten
	 */
	@Override
	public String toString() {
		return "Identity [uid=" + uid + ", email=" + email + ", displayName=" + displayName + ", birthDate=" + birthDate
				+ ", password=" + password + ", type=" + type + "]";
	}
	/**
	 * Transforms classic toString into a better human-readable table to be print for the user.
	 * @return String
	 */
	public String toReadableString() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return getDisplayName()+"\t\t"+
				getEmail()+"\t\t"+ getUid()+"\t\t"+
				simpleDateFormat.format(getBirthDate())+"\t\t"+getType();
	}
	/**
	 * Print a header for the table to be displayed to the user.
	 */
	public static void printIdentityHeaders() {
		System.out.println("DisplayName\t\tEmail\t\tUID\t\tBirthday\t\tType");
	}
}
