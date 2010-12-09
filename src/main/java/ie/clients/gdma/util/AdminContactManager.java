package ie.clients.gdma.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

public class AdminContactManager {

	private static ThreadLocal<AdminContactManager> theInstance = new ThreadLocal<AdminContactManager>();

	private static Logger logger = Logger.getLogger(AdminContactManager.class);
	private final Properties config = new Properties();

	public static AdminContactManager getAdminContactManager() {
		if (theInstance.get() == null)
			theInstance.set(new AdminContactManager());
		return theInstance.get();
	}

	private AdminContactManager() {
		try {
			config.load(getClass().getClassLoader().getResourceAsStream("adminContact.properties"));
		} catch (IOException ex) {
			logger.error("Could not load contact details", ex);
		}
	}

	/*public boolean isAllowed(String className) {
		String val = config.getProperty(className);
		if (val == null)
			return false;
		val = val.toLowerCase();
		return val.equals("true") || val.equals("yes") || val.equals("y");
	}

	public boolean isAllowed(Class<?> clazz) {
		return isAllowed(clazz.getName());
	}*/

	public String[] getAdminContactDetails() {
		String[] rv = new String[3];
		int i = 0;
		String configValue = "";
		for (Object k : config.keySet()) {
			configValue = config.getProperty(k.toString());
			rv[i] = configValue;
			//if (isAllowed(s))
				i++;
		}
		return rv;
	}
}
/**
 * $Author: jroche $
 * 
 * $Id: AdminContactManager.java  2010-12-08 10:49:25Z jroche $
 * 
 * 
 * 
 * $Date: 2010-10-16 15:45:25 +0100 (Wed, 08 Nov 2010) $
 */

