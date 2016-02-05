package fr.epita.iamcoreproject.services.match.impl;

import fr.epita.iamcoreproject.datamodel.Identity;
import fr.epita.iamcoreproject.services.match.Matcher;

public class StartsWithIdentityMatcher implements Matcher<Identity> {

	@Override
	public boolean match(Identity criteria, Identity toBeMatched) {
		
		String email = criteria.getEmail();
		String displayName = criteria.getDisplayName();
		String uid = criteria.getUid();
		boolean startsWithEmail=false;
		boolean startsWithDisplayName=false;
		boolean startsWithUid=false;
		if(!email.equals(""))
			startsWithEmail = toBeMatched.getEmail().startsWith(email);
		if(!displayName.equals(""))
			startsWithDisplayName = toBeMatched.getDisplayName().startsWith(displayName);
		if(!uid.equals(""))
			startsWithUid = toBeMatched.getUid().startsWith(uid);
		return startsWithEmail || startsWithDisplayName || startsWithUid;
	}

}
