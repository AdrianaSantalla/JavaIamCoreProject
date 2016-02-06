/**
 * 
 */
package fr.epita.iamcoreproject.services.match.impl;

import fr.epita.iamcoreproject.datamodel.Identity;
import fr.epita.iamcoreproject.services.match.Matcher;

/**
 * This class <code>ContainsIdentityMatcher</code> implements the interface <code>Matcher</code> and
 * thus it has the <code>match()</code> method.
 * 
 * @author Adriana Santalla and David Cechak
 * @version 1;
 *
 */
public class EqualsIdentityMatcher implements Matcher<Identity>{
	/**
	 * <p>This method its capable to deal with incomplete criteria. It compares the criteria with the
	 * identity to be matched, using the function between strings <code>equals()</code>.
	 * <p>It compares the two criteria to search an Identity specific identity, which are email,
	 * displayName.
	 * This matcher is used for the <b>Authentication</b> process and when searching and <b>identity given an uid.</b> 
	 * <p>If any of the criteria matches it return <b>true</b>
	 */
	@Override
	public boolean match(Identity criteria, Identity toBeMatched) {
		//getting all the criteria to match
		String uid = criteria.getUid(), displayName = criteria.getDisplayName();
		//establishing that they do not match unless they match
		boolean equalsUid=false;
		boolean equalsDisplayName=false;
		//if they are not empty, they can be compared using the contains function from String
		if(uid != null)
			equalsUid = toBeMatched.getUid().equals(uid);
		if(displayName != null)
			equalsDisplayName = toBeMatched.getDisplayName().equals(displayName);
		return equalsUid || equalsDisplayName;
	}	
}