/**
 * 
 */
package fr.epita.iamcoreproject.services.match;

/**
 * This is an interface for all the Matchers in the Application.
 * <ul>
 * 		<li><code>ContainsIdentityMatcher</code></li>
 * 		<li><code>EqualsIdentityMatcher</code></li>
 * 		<li><code>StartsIdentityMatcher</code></li>
 * </ul>
 * <p>It has the unique method match() that has to be implemented in all Matchers.
 * @param <T> meaning Type. It can be replaced for any kind of object
 * @author Adriana Santalla and David Cechak
 * @version 1
 */
public interface Matcher<T> {
	/**
	 * 
	 * @param criteria Object containing the data to search
	 * @param toBeMatched Object containing the data to match with the search
	 * @return true if the criteria matches the toBeMatched instance
	 */
	public boolean match(T criteria, T toBeMatched);
}
