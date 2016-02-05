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
		return toBeMatched.getDisplayName().equals(criteria.getDisplayName());
	}
	
	
}
