/**
 * 
 */
package fr.epita.iamcoreproject.datamodel;

import java.util.Date;

/**
 * @author Adriana Santalla
 *
 */
public class Identity {
	private String uid;
	private String email;
	private String displayName;
	private Date birthDate;
	
	/**
	 * 
	 */
	public Identity() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param uid
	 * @param email
	 * @param displayName
	 */
	public Identity(String uid, String email, String displayName) {
		this.uid = uid;
		this.email = email;
		this.displayName = displayName;
		this.birthDate = birthDate;
	}
	/**
	 * @param uid
	 * @param email
	 * @param displayName
	 * @param birthDate
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
	
	
}
