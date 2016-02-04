package fr.epita.iamcoreproject.services.match;

/**
 * 
 * @author Adriana Santalla
 * This is an interface to define the Matcher behavior
 *
 * @param <T>
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
