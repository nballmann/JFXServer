package org.nic.jfxserver.model;

import java.io.Serializable;

public class Person implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6171857464475147997L;
	private String name;
	private String lastname;
	private String address;
	private String postal_code;
	private String phone;
	private String email;
	private String role;
	
	private static boolean last = false;
	
	private Person(String name, String lastname, String address,
			String postal_code, String phone, String email, String role) {
		
		last = (setName(name) & setLastname(lastname) & setAddress(address) & 
				setPostal_code(postal_code) & setPhone(phone) & setEmail(email)
				& setRole(role));
	}
	
	public static Person create(String name, String lastname, String address,
			String postal_code, String phone, String email, String role) {

		Person p = new Person(name, lastname, address,
				postal_code, phone, email, role);
		
		if(last)
			return p;
		
		return null;
	}

	public String getName() {
		return name;
	}

	public boolean setName(final String name) {
		
		if(name != null && !name.matches("[\\d!§$%&/()=?*+~'#;:-]+")) {
			this.name = name;
			return true;
		}
		return false;
	}

	public String getLastname() {
		return lastname;
	}

	public boolean setLastname(final String lastname) {
		if(lastname != null && !lastname.matches("[\\d!§$%&/()=?*+~'#;:-]+")) {
			this.lastname = lastname;
			return true;
		}
		return false;
	}

	public String getAddress() {
		return address;
	}

	public boolean setAddress(final String address) {
		if(address != null && !address.matches("[!§$%&/()=?*+~'#;:-]+")) {
			this.address = address;
			return true;
		}
		return false;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public boolean setPostal_code(final String postal_code) {
		if(postal_code != null && postal_code.matches("\\d{4,8}")) {
			this.postal_code = postal_code;
			return true;
		}
		return false;
	}

	public String getPhone() {
		return phone;
	}

	public boolean setPhone(final String phone) {
		if(phone != null && phone.matches("^\\+?(\\d{1,15}.?)+")) {
			this.phone = phone;	
			return true;
		}
		return false;
	}

	public String getEmail() {
		return email;
	}

	public boolean setEmail(final String email) {
		if(email!= null && email.matches("^[a-zA-Z0-9-\\+]+(\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z])*\\.[a-zA-Z]{2,6}")) {
			this.email = email;
			return true;
		}
		return false;
	}

	public String getRole() {
		return role;
	}

	public boolean setRole(final String role) {
		if(role != null && !role.matches("[!\\$&%\\?;\\\\/]+")) {
				this.role = role;
				return true;
		}
		return false;
	}

	

}
