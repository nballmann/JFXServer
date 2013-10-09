package org.nic.jfxserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

public class DbProperties {
	
	private static final String URL = "db.url";
	private static final String USERNAME = "db.username";
	private static final String PASSWORD = "db.password";

	private static final String[] REQUIRED_KEYS = { URL, USERNAME, PASSWORD };
	
	private static final String FILENAME = "resources/db.properties";
	
	private final String filePath;
	private final Properties properties = new Properties();
	
	public DbProperties() {
		
		final File file = new File(FILENAME);
		filePath = file.getAbsolutePath();
		
		try (final InputStream inputStream = new FileInputStream(file)) {
			
			properties.load(inputStream);
			
			ensureAllKeysAvailable();
			
			getUrl();
			
		} catch (IOException e) {
			throw new IllegalArgumentException("problems while accessing " + 
						"db config file ´" + filePath + "´", e);
		}
		
	}

	public String getUrl() {

		final String url = properties.getProperty(URL, "");
		if(url.isEmpty()) {
			
			throw new IllegalStateException("db config file ´" + filePath +
											"´ is incomplete! Missing " +
											"value for key: ´" + URL + "´");
		}
		return url;
	}
	
	public String getUserName() {
		
		return properties.getProperty(USERNAME, "");		
	}
	
	public String getPassword() {
		
		return properties.getProperty(PASSWORD, "");
	}

	private void ensureAllKeysAvailable() {

		final SortedSet<String> missingKeys = new TreeSet<String>();
		
		for(final String key : REQUIRED_KEYS) {
			
			if (!properties.containsKey(key)) {
				missingKeys.add(key);
			}
			
			if(!missingKeys.isEmpty()) {
				
				throw new IllegalStateException("db config file ´" + filePath +
												"´ is incomplete! Missing keys: " + 
												missingKeys);
				
			}
			
			
			
		}
	}
			
	
}
