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
 */
public class ContainsIdentityMatcher implements Matcher<Identity> {
	/**
	 * <p>This method its capable to deal with incomplete criteria. It compares the criteria with the
	 * identity to be matched, using the function between strings <code>contains()</code>.
	 * <p>It compares the tree criteria to search an Identity, which are email,
	 * displayName, uid.
	 * <p>If any of the criteria matches it return <b>true</b>
	 */
	@Override
	public boolean match(Identity criteria, Identity toBeMatched) {
		//getting all the criteria to match
		String email = criteria.getEmail();
		String displayName = criteria.getDisplayName();
		String uid = criteria.getUid();
		//establishing that they do not match unless they match
		boolean startsWithEmail=false;
		boolean startsWithDisplayName=false;
		boolean startsWithUid=false;
		//if they are not empty, they can be compared using the contains function from String
		if(!email.equals(""))
			startsWithEmail = toBeMatched.getEmail().contains(email);
		if(!displayName.equals(""))
			startsWithDisplayName = toBeMatched.getDisplayName().contains(displayName);
		if(!uid.equals(""))
			startsWithUid = toBeMatched.getUid().contains(uid);
		return startsWithEmail || startsWithDisplayName || startsWithUid;
	}
}
