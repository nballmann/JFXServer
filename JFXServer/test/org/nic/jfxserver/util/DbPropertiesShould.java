package org.nic.jfxserver.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class DbPropertiesShould {
	
	DbProperties dbProperties;
	
	@Before
	public void init() {
		
		dbProperties = new DbProperties();
		
	}
	
	@Test
	public void beAbleToKnowItsDbURL() {
		
		assertEquals("jdbc:mysql://127.0.0.1:3306/", dbProperties.getUrl());
		
	}
	
	@Test
	public void beABleToKnowDbAssociatedUser() {
		
		assertEquals("root", dbProperties.getUserName());
	}
	
	@Test
	public void beABleToKnowDbAssociatedPassword() {
		
		assertEquals("", dbProperties.getPassword());
	}
	
	

}
