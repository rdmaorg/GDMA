/*
 * Created on Mar 15, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.vodafone.gdma.exceptions.PropertyNotFoundException;

/**
 * @author RGILL
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Config {

    private static String connectionClass;

    private static String connectionUrl;

    private static Properties connectionProp;

    private static String propFile = "/default.properties";

    public static String getProperty(String key) throws Exception {
        String ret = null;
        // load the propertis for the conenction (only done th first time
        loadProperties();
        ret = connectionProp.getProperty(key);
        if (ret == null)
            throw new PropertyNotFoundException("Property [" + key + "] not found in " + propFile);

        return ret;
    }

    private static void loadProperties() throws IOException, Exception {
        if (connectionProp == null) {
            synchronized (Config.class) {
                if (connectionProp == null) {
                    try {
                        InputStream is = Config.class.getResourceAsStream(propFile);
                        // Load properties file
                        connectionProp = new Properties();
                        connectionProp.load(is);
                    } catch (Exception e) {
                        // there was a problem loading the properties
                        connectionProp = null;
                        e.printStackTrace();
                        throw e;
                    }
                }
            }
        }
    }

    /**
     * @return Returns the propFile.
     */
    public static String getPropFile() {
        return propFile;
    }

    /**
     * @param propFile
     *            The propFile to set.
     */
    public static void setPropFile(String propFile) {
        Config.propFile = propFile;
    }
}
