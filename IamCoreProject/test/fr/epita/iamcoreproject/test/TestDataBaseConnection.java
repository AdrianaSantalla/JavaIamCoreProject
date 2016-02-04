/**
 * 
 */
package fr.epita.iamcoreproject.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * @author Adriana Santalla
 *
 */
public class TestDataBaseConnection {
	private static String dbURL = "jdbc:derby://localhost:1527/IAMDataBase;create=true;user=adri;password=adri";
	private static String tableName = "identities";
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
    
    public static void main(String[] args)
    {
        createConnection();
        selectAll();
        shutdown();
    }
    
    private static void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }
    
    private static void selectAll()
    {
        try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + tableName);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
                int id = results.getInt(1);
                String displayName = results.getString(2);
                String email = results.getString(3);
                Date birthDate = results.getDate(4);
                String uid = results.getString(5);
                System.out.println(id + "\t\t" + displayName + "\t\t" + email + "\t\t" + birthDate + "\t\t" + uid);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
            
        }

    }
}
