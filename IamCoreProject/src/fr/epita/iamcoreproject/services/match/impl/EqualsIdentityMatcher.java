package fr.epita.iamcoreproject.services.match.impl;

import fr.epita.iamcoreproject.datamodel.Identity;
import fr.epita.iamcoreproject.services.match.Matcher;

/**
 * 
 * @author Adriana Santalla
 *
 */
public class EqualsIdentityMatcher implements Matcher<Identity>{

	@Override
	public boolean match(Identity criteria, Identity toBeMatched) {
		String uid = criteria.getUid(), displayName = criteria.getDisplayName();
		boolean equalsUid=false;
		boolean equalsDisplayName=false;
		if(uid != null)
			equalsUid = toBeMatched.getUid().equals(uid);
		if(displayName != null)
			equalsDisplayName = toBeMatched.getDisplayName().equals(displayName);
		return equalsUid || equalsDisplayName;
	}
	
	
}
