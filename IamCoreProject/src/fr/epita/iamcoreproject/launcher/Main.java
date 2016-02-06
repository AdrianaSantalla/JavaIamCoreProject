/**
 * 
 */
package fr.epita.iamcoreproject.launcher;

import fr.epita.iamcoreproject.console.ApplicationConsole;

/**
 * This is the launcher of the application. It instantiates the <code>ApplicationConsole</code> class
 * and calls the method run of this class.
 * 
 * @author Adriana Santalla and David Cechak
 * @version 1;
 */
public class Main {

	public static void main(String[] args) {
		//creating an instance of the ApplicationConsole
		new ApplicationConsole().run();
	}	
}