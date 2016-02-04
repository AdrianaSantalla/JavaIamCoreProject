package fr.epita.iamcoreproject.services.match.impl;

import fr.epita.iamcoreproject.datamodel.Identity;
import fr.epita.iamcoreproject.services.match.Matcher;

public class StartsWithIdentityMatcher implements Matcher<Identity> {

	@Override
	public boolean match(Identity criteria, Identity toBeMatched) {
		
		String email = criteria.getEmail(), displayName = criteria.getDisplayName();
		boolean startsWithEmail=false;
		boolean startsWithDisplayName=false;
		if(email != null)
			startsWithEmail = toBeMatched.getEmail().startsWith(email);
		if(displayName != null)
			startsWithDisplayName = toBeMatched.getDisplayName().startsWith(displayName);
		return startsWithEmail || startsWithDisplayName;
	}

}
