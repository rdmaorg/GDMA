package com.vodafone.gdma.dbaccess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import com.vodafone.gdma.util.Config;

/**
 * @author RGILL
 * 
 */
public class DBUtil {

    private static String connectionClass;

    private static String connectionUrl;

    private static Properties connectionProp;

    /*
     *  returns the default connection for the GDMA database
     * 
     */
    public static Connection getConnection()
            throws ClassNotFoundException, SQLException, IOException, Exception {

        //load the propertis for the conenction (only done th first time
        loadProperties();

        //load the JDBC class
        Class.forName(Config.getProperty("class"));

        // Connect to the database
        return DriverManager.getConnection(Config.getProperty("url"), connectionProp);
    }

    /*
     * Loads the properties file
     */
    private static void loadProperties() throws IOException, Exception {
        if (connectionProp == null) {
            synchronized (DBUtil.class) {
                if (connectionProp == null) {
                    // Load properties file
                    connectionProp = new Properties();
                    connectionProp.put("user",Config.getProperty("user"));
                    connectionProp.put("password",Config.getProperty("password"));
                    connectionUrl = Config.getProperty("url");
                    connectionClass = Config.getProperty("class");                    
                }
            }
        }
    }
    
    /*
     * Get a conenction to any database 
     */
    public static Connection getConnection(String conenctionClass, 
            String user, String pass, String url) throws SQLException, ClassNotFoundException{
  
        Class.forName(conenctionClass);

        // setup the properties
        Properties prop = new Properties();
        prop.put("user", user);
        prop.put("password", pass);

        // Connect to the database
        return DriverManager.getConnection(url, prop);
    }
    
    public static Connection getConnection(ServerRegistration reg) throws Exception{
        ODBCProvider odbc = ODBCProviderFactory.getInstance().getODBCProvider(
                reg.getOdbcTypeID());
        Class.forName(odbc.getConnectionClass());

        // setup the properties
        Properties prop = new Properties();
        prop.put("user", reg.getUsername());
        prop.put("password", reg.getPassword());

        // Connect to the database
        return DriverManager.getConnection(reg.getConnectionURL(), prop);
    }
    
    /*
     * Used to test if a column is a date
     */
    public static boolean isDate(int type){
        return type == Types.DATE ||
        type == Types.TIME ||
        type == Types.TIMESTAMP;
    }
    
    /*
     * Used to test if a column is a number
     */    
    public static boolean isNumber(int type){
        return type == Types.TINYINT ||
        type == Types.INTEGER ||
        type == Types.BIGINT ||
        type == Types.DECIMAL ||
        type == Types.FLOAT ||
        type == Types.NUMERIC ||
        type == Types.REAL ;
    }
    
}
