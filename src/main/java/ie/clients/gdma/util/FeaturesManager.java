package ie.clients.gdma.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

public class FeaturesManager {

	private static ThreadLocal<FeaturesManager> theInstance = new ThreadLocal<FeaturesManager>();

	private static Logger logger = Logger.getLogger(FeaturesManager.class);
	private final Properties config = new Properties();

	public static FeaturesManager getFeaturesManager() {
		if (theInstance.get() == null)
			theInstance.set(new FeaturesManager());
		return theInstance.get();
	}

	private FeaturesManager() {
		try {
			config.load(getClass().getClassLoader().getResourceAsStream("features.properties"));
		} catch (IOException ex) {
			logger.error("Could not load features", ex);
		}
	}

	public boolean isAllowed(String className) {
		String val = config.getProperty(className);
		if (val == null)
			return false;
		val = val.toLowerCase();
		return val.equals("true") || val.equals("yes") || val.equals("y");
	}

	public boolean isAllowed(Class<?> clazz) {
		return isAllowed(clazz.getName());
	}

	public Iterator<String> getEnabledFeatures() {
		ArrayList<String> rv = new ArrayList<String>();
		for (Object k : config.keySet()) {
			String s = k.toString();
			if (isAllowed(s))
				rv.add(s);
		}
		return rv.iterator();
	}
}
/**
 * $Author$
 * 
 * $Id$
 * 
 * $Rev$
 * 
 * $Date$
 */
