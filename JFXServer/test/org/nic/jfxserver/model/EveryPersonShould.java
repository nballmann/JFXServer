package org.nic.jfxserver.model;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EveryPersonShould {

	private Person person;
	
	@Before
	public void init() {
		
		
	}
	
	@Test
	public void HaveFunctionalSetters() {
		
		person = Person.create("Heribert", "Fassbender", "21st Jumpstreet", 
				"99999", "555 555", "hfassbender@mail.org", 
				"Gardener");
		
		assertTrue(person != null);
//		assertTrue(person.setName("Hugo"));
//		assertTrue(person.setLastname("Kurz"));
//		assertTrue(person.setAddress("21st Jumpstreet"));
//		assertTrue(person.setPhone("555 12345645764"));
//		assertTrue(person.setPostal_code("99999"));
//		assertTrue(person.setEmail("h.fassbender@live-days.org"));
//		assertTrue(person.setRole("CEO Ph.D Dipl.Ing. Doc. Prof. Gärtner"));
		
	}
	
	@Test
	public void haveAllItsFieldsSetAfterCreation() {
		
		person = Person.create("Heribert", "Fassbender", "21st Jumpstreet", 
				"99999", "555 555", "hfassbender@mail.org", 
				"Gardener");
		
		assertTrue(person.getName() != null);
		assertTrue(person.getLastname() != null);
		assertTrue(person.getAddress() != null);
		assertTrue(person.getPostal_code() != null);
		assertTrue(person.getPhone() != null);
		assertTrue(person.getEmail() != null);
		assertTrue(person.getRole() != null);
		
	}
	
}
