/**
 * 
 */
package fr.epita.iamcoreproject.test.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author Usuario
 * <p> From http://www.avajava.com/tutorials/lessons/how-do-i-read-a-properties-file.html
 */
public class TestReadProperties {

	/**
	 * @param args Strings to call main
	 * @throws Exception FileNotFound
	 */
	public static void main(String[] args) throws Exception {
		try {
			File file = new File("config.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();

			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
				System.out.println(key + ": " + value);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
