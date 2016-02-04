/**
 * 
 */
package fr.epita.iamcoreproject.services.match.impl;

import fr.epita.iamcoreproject.datamodel.Identity;
import fr.epita.iamcoreproject.services.match.Matcher;

/**
 * @author Adriana Santalla
 *
 */
public class ContainsIdentityMatcher implements Matcher<Identity> {

	@Override
	public boolean match(Identity criteria, Identity toBeMatched) {
		return toBeMatched.getEmail().contains(criteria.getEmail())
				|| toBeMatched.getDisplayName().contains(criteria.getDisplayName());
	}
}
