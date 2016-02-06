/**
 * 
 */
package fr.epita.iamcoreproject.dao.exceptions;

/**
 * Customized exception that is thrown when the reflection pattern fails
 * <p>It inherits from <code>Exception</code>
 * @author Adriana Santalla and David Cechak
 * @version 1;
 */
public class ReflectionException extends Exception{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Personalizing the message of the Exception
	 * @param problematicInstance object causing the exception
	 */
	public ReflectionException(Object problematicInstance) {
		// use String.valueOf() instead of toString() when you are not sure of the parameter initialization
		super("a problem occured while using reflection with " + String.valueOf(problematicInstance));

	}

}
