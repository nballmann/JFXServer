package org.nic.jfxserver.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.nic.jfxserver.model.Person;

public class DbConnection {
	
	private static Connection con = null;
	
	private static final String DATABASE = "server_db";

	private DbProperties properties;
	
	
	private DbConnection() {
		
		properties = new DbProperties();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(properties.getUrl() + 
					DATABASE + "?user=" + properties.getUserName() +
					"&password=" + properties.getPassword());
	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Connection getInstance() {
		
		if(con == null)
			new DbConnection();
		return con;
		
	}

	public static boolean insertIntoPerson(final Person person) {

		boolean returnValue = true;
		
		con = getInstance();
		
		PreparedStatement statement = null;
		
		if(con!=null) {
			
			try {
			String sql = "INSERT INTO persons(lastname, name, address, postal_code, phone, email, role) " +
						"VALUES(?, ?, ?, ?, ?, ?, ?)";
			statement = con.prepareStatement(sql);
			statement.setString(1, person.getLastname());
			statement.setString(2, person.getName());
			statement.setString(3, person.getAddress());
			statement.setString(4, person.getPostal_code());
			statement.setString(5, person.getPhone());
			statement.setString(6, person.getEmail());
			statement.setString(7, person.getRole());
			
			if(statement.executeUpdate() == 0)
				returnValue=false;
			}
			catch (SQLException e) {
				returnValue = false;
				e.printStackTrace();
			}
			finally {
				try {
					if(statement!=null)
						statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return returnValue;
	}

	public static ArrayList<Person> getAllPersonsFromDb() {

		con = getInstance();
		
		Statement query = null;
		
		ArrayList<Person> persons = new ArrayList<>();
		
		if(con != null) {
			
			try {
				
				query = con.createStatement();
				
				String sql = "SELECT * FROM persons";
				
				ResultSet results = query.executeQuery(sql);
				
				while(results.next()) {
					
					String lastname = results.getString(1);
					String name = results.getString(2);
					String address = results.getString(3);
					String postal_code = results.getString(4);
					String phone = results.getString(5);
					String email = results.getString(6);
					String role = results.getString(7);
					
					persons.add(Person.create(name, lastname, address, postal_code, phone, email, role));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				
				try {
					if(query!=null)
						query.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return persons;
	}

	public static void deleteEntryFromPersons(String name, String lastname) {

		con = getInstance();
		
		if(con != null) {
			
			PreparedStatement query = null;
			
			try {
				String sql = "DELETE FROM persons WHERE persons.name = \"" + name + 
						"\" AND  persons.lastname = \"" + lastname + "\"";
				
				query = con.prepareStatement(sql);
				
				query.executeUpdate();
			} 
			catch(SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if(query!=null)
						query.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public static void clearTable(final String tableName) {
		
		con = getInstance();
		
		if(con!=null) {
			
			Statement st = null;
			
			try {
				st = con.createStatement();
				
				st.execute("TRUNCATE TABLE " + tableName);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
	
	
}
