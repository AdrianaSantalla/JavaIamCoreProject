/**
 * 
 */
package fr.epita.iamcoreproject.console;

import java.util.InputMismatchException;
import java.util.Scanner;

import fr.epita.iamcoreproject.console.action.impl.AuthenticateActionConsole;
import fr.epita.iamcoreproject.console.action.impl.CreateActionConsole;
import fr.epita.iamcoreproject.console.action.impl.DeleteActionConsole;
import fr.epita.iamcoreproject.console.action.impl.ReadAllActionConsole;
import fr.epita.iamcoreproject.console.action.impl.SearchActionConsole;
import fr.epita.iamcoreproject.console.action.impl.UpdateActionConsole;

/**
 * This is the main Console from the Application to manage Identities.
 * <p>First the <code>AuthenticateActionConsole</code> method <code>execute()</code> is called 
 * because the first action to perform is the authentication for the user.
 * If the authentication is <b>successful</b>, the Menu is shown with the function <code>showMenu()</code>.
 * 
 * <p>The user is asked to select an option from the Menu. According to the selected option,
 * the corresponding type of <code>ConsoleAction</code> is called. For example, if the user selected the 
 * <b>create option</b>, the CreateActionConsole is called, creating an instance of this class:
 * <blockquote><pre>{@code
 *     new CreateActionConsole().execute();
 * }</pre></blockquote>
 * 
 * <p>In the same way, the other ActionConsoles related to the selected option are called. 
 *   
 * @author Adriana Santalla and David Cechak
 * @version 1;
 */
public class ApplicationConsole {
	//scanner to read the option selected by the user
	Scanner scanner = new Scanner(System.in);
	/**
	 * Method to execute the <code>ApplicationConsole</code>
	 * <p>First it creates an instance of <code>AuthenticateActionConsole</code>
	 * <p>Then instances of:
	 *  <ul>
	 * 		<li><code>AuthenticateActionConsole</code></li>
	 * 		<li><code>CreateActionConsole</code></li>
	 * 		<li><code>DeleteActionConsole</code></li>
	 * 		<li><code>ReadAllActionConsole</code></li>
	 * 		<li><code>SearchActionConsole</code></li>
	 * 		<li><code>UpdateActionConsole</code></li>
	 * </ul>
	 * According to the option selected by the user.
	 */	
	public void run() {
		System.out.println("Welcome to the Identities Application Management");
		//call to the Authentication process
		new AuthenticateActionConsole().execute();
		int selectedOption=-1;
		//do while loop to keep asking the user the option until he chooses the option 0
		do{
			showMenu();
			//try catch to verify if the inserted value is an integer value
			try {
	    		selectedOption = scanner.nextInt();
	    	} catch (InputMismatchException e) {
	    		//if not we print this error message and let the user to insert a valid option
	    	    System.err.println("Please input a number: ");
	    	    scanner.nextLine();
	    	    continue;
	    	}
			scanner.nextLine();
			//Switch case to find the option selected by the user
			switch(selectedOption){
			case 1://Show all option 
				new ReadAllActionConsole().execute();
				break;
			case 2: //Create option
				new CreateActionConsole().execute();
				break;
			case 3: //Update option
				new UpdateActionConsole().execute();
				break;
			case 4: //Delete option
				new DeleteActionConsole().execute();
				break;
			case 5: //Search option
				new SearchActionConsole().execute();
				break;
			case 0: //Exit option
				scanner.close();
				System.out.println("Goodbye!");
				break;
			default: //When the inserted value not corresponds to any option although it is numeric
				System.out.println("Invalid option");
				break;
			}
				
			}
			while(selectedOption!=0);					
	}
	/**
	 * Internal method to print the menu.
	 */
	private void showMenu(){
		// Display menu
	    System.out.println("____________________________");
	    System.out.println("|       MAIN MENU          |");
	    System.out.println("|__________________________|");
	    System.out.println("|  Choose an option:       |");
	    System.out.println("|        1. Show all       |");
	    System.out.println("|        2. Create         |");
	    System.out.println("|        3. Update         |");
	    System.out.println("|        4. Delete         |");
	    System.out.println("|        5. Search         |");
	    System.out.println("|        0. Exit           |");
	    System.out.println("|__________________________|");
	}
}
