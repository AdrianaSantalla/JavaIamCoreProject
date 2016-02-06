/**
 * 
 */
package fr.epita.iamcoreproject.datamodel;

import java.text.SimpleDateFormat;
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
	private String password;
	private String type;
	
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
	
	public boolean isMatched(Identity other) {
		return getType().equals("admin") && getDisplayName().equals(other.getDisplayName()) 
				&& getPassword().equals(other.getPassword());
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Identity [uid=" + uid + ", email=" + email + ", displayName=" + displayName + ", birthDate=" + birthDate
				+ ", password=" + password + ", type=" + type + "]";
	}
	
	public String toReadableString() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return getDisplayName()+"\t\t"+
				getEmail()+"\t\t"+ getUid()+"\t\t"+
				simpleDateFormat.format(getBirthDate())+"\t\t"+getType();
	}
	
	public static void printIdentityHeaders() {
		System.out.println("DisplayName\t\tEmail\t\tUID\t\tBirthday\t\tType");
	}
}
