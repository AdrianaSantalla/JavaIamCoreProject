/**
 * 
 */
package fr.epita.iamcoreproject.console;

/**This is an interface for all the types of Actions in the Console.
 * <ul>
 * 		<li><code>AuthenticateActionConsole</code></li>
 * 		<li><code>CreateActionConsole</code></li>
 * 		<li><code>DeleteActionConsole</code></li>
 * 		<li><code>ReadAllActionConsole</code></li>
 * 		<li><code>SearchActionConsole</code></li>
 * 		<li><code>UpdateActionConsole</code></li>
 * </ul>
 * <p>It has the unique method execute() that has to be implemented in all types of Action.
 * @author Adriana Santalla and David Cechak
 * @version 1;
 */
public interface ActionConsole {
	//method to be implemented in the classes ActionConsole 
	public void execute();
}
