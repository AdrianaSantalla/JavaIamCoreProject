package fr.epita.iamcoreproject.services.match;

/**
 * @author Adriana Santalla and David Cechak
 * @version 1
 * @param <T>
 * @return (methods only)
 * @exception (@throws is a synonym added in Javadoc 1.2)
 */
public interface Matcher<T> {
	/**
	 * 
	 * @param criteria
	 * @param toBeMatched
	 * @return true if the criteria matches the toBeMatched instance
	 */
	public boolean match(T criteria, T toBeMatched);
}
