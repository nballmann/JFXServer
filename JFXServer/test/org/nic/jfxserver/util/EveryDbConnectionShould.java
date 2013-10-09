package org.nic.jfxserver.util;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nic.jfxserver.model.Person;

public class EveryDbConnectionShould {
	
	private Connection con;
	
	@Before
	public void init() {
		
//		con = new DbConnection();
		
	}
	
	@Test
	public void beAbleToReturnADbConnection() {
		
		con = DbConnection.getInstance();
		assertTrue(con != null);
		
	}
	
	@Test
	public void letTheUserInsertANewEntryIntoPersonTable() {
		
//		assertTrue(DbConnection.insertIntoPerson(
//				"Heribert", "Fassbender", "21st Jumpstreet", 
//				"99999", "555 573497", "hfassbender@mail.org", 
//				"Gardener"));
		assertTrue(DbConnection.insertIntoPerson(Person.create(
				"Heribert", "Fassbender", "21st Jumpstreet", 
				"99999", "555 573497", "hfassbender@mail.org", 
				"Gardener")
				));
	}
	
	@Test
	public void letTheUserQueryATable() {
		
		assertTrue(DbConnection.insertIntoPerson(Person.create(
				"Sandra", "Wayne", "On da Road 66", 
				"12345", "+01 123456", "s-wayne@mail-me.org", 
				"Navy Seal")));
		
		ArrayList<Person> persons = DbConnection.getAllPersonsFromDb();
		
		assertTrue(persons != null && !persons.isEmpty());
	}
	
	@Test
	public void beAbleToDeleteTableRows() {
		
		assertTrue(DbConnection.insertIntoPerson(Person.create(
				"Hans", "Volks", "21st Jumpstreet", 
				"32456", "555 59034758", "hvolks@mail.org", 
				"CEO")));
		
		ArrayList<Person> persons = DbConnection.getAllPersonsFromDb();
		
		assertTrue(persons != null && !persons.isEmpty());
		
		boolean incl = false;
		for(Person p : persons) {
			if(p.getName().equals("Hans")) {
				incl = true;
			}
		}
		assertTrue(incl);
		
		DbConnection.deleteEntryFromPersons("Hans", "Volks");
			
		persons = DbConnection.getAllPersonsFromDb();
		
		incl = false;
		for(Person p : persons) {
			if(p.getName().equals("Hans")) {
				incl = true;
			}
		}
		assertTrue(!incl);
		
		try {
			if(con!=null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void beAbleToTruncateAWholeTable() {
		
		assertTrue(DbConnection.insertIntoPerson(Person.create(
				"Hans", "Volks", "21st Jumpstreet", 
				"32456", "555 59034758", "hvolks@mail.org", 
				"CEO")));
		
		DbConnection.clearTable("persons");

		assertTrue(DbConnection.getAllPersonsFromDb().isEmpty());
		
	}
	
	@After
	public void finish() {
		
		DbConnection.clearTable("persons");
		
	}
	

}
