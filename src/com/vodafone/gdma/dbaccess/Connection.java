/*
 * Created on Mar 15, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.vodafone.gdma.Config;

/**
 * @author RGILL
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Connection {

    private static String connectionClass;

    private static String connectionUrl;

    private static Properties connectionProp;

    public static java.sql.Connection getConnection()
            throws ClassNotFoundException, SQLException, IOException, Exception {

        //load the propertis for the conenction (only done th first time
        loadProperties();

        //load the JDBC class
        Class.forName(Config.getProperty("class"));

        // Connect to the database
        return DriverManager.getConnection(Config.getProperty("url"), connectionProp);
    }

    private static void loadProperties() throws IOException, Exception {
        if (connectionProp == null) {
            synchronized (Connection.class) {
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
    
    public static java.sql.Connection getConnection(String conenctionClass, String user, String pass, String url) throws SQLException, ClassNotFoundException{
  
        Class.forName(conenctionClass);

        // setup the properties
        Properties prop = new Properties();
        prop.put("user", user);
        prop.put("password", pass);

        // Connect to the database
        return DriverManager.getConnection(url, prop);
    }
}
