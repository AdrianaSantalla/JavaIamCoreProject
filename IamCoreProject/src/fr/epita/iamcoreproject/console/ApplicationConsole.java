/**
 * 
 */
package fr.epita.iamcoreproject.console;

import java.util.InputMismatchException;
import java.util.Scanner;

import fr.epita.iamcoreproject.dao.IdentityDAOInterface;
import fr.epita.iamcoreproject.dao.IdentityXmlDAO;
import fr.epita.iamcoreproject.datamodel.Identity;

/**
 * This is the main Console from the Application.
 * <p>Here we first call the <code>AuthenticateActionConsole</code> method <code>execute()</code> 
 * because the first action to perform is the authentication for the user.
 * If the authentication is successful, the Menu is shown with the function <code>showMenu()</code>.
 * 
 * <p>We ask the user to select the action that he wants to execute. According to the selected option,
 * we call the corresponding type of <code>ConsoleAction</code>. For example, if the user selected the 
 * create option, we call the corresponding Action, creating an instance of this class:
 * <blockquote><pre>{@code
 *     new CreateActionConsole().execute();
 * }</pre></blockquote>
 *  
 * <p>This  
 * 
 * @author Adriana Santalla and David Cechak
 *
 */
public class ApplicationConsole {
	
	Scanner scanner = new Scanner(System.in);
	
	public void run() {
		System.out.println("Welcome to the Identities Application Management");
		new AuthenticateActionConsole().execute();
		int selectedOption=-1;
		do{
			showMenu();
			try {
	    		selectedOption = scanner.nextInt();
	    	} catch (InputMismatchException e) {
	    	    System.err.println("Please input a number: ");
	    	    scanner.nextLine();
	    	    continue;
	    	}
			scanner.nextLine();
			switch(selectedOption){
			case 1: 
				new ReadAllActionConsole().execute();
				break;
			case 2: 
				new CreateActionConsole().execute();
				break;
			case 3: 
				new UpdateActionConsole().execute();
				break;
			case 4: 
				new DeleteActionConsole().execute();
				break;
			case 5: 
				new SearchActionConsole().execute();
				break;
			case 0: 
				scanner.close();
				System.out.println("Goodbye!");
				break;
			default:
				System.out.println("Invalid option");
				break;
			}
				
			}
			while(selectedOption!=0);					
	}
	
	private static void showMenu(){
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
