package org.nic.jfxserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

public class EveryClientShould {
	
	private Client client;
	private Socket socket;
	
	@Before
	public void init() {
		
		socket = new Socket();
		assertTrue(socket != null);
		
		client = new Client(socket);
		
	}
	
	@Test
	public void haveASocketAfterConstruction() throws UnknownHostException, IOException {
		
		Field f = null;
		Socket s = null;
		
		try {
			f = client.getClass().getDeclaredField("socket");
			f.setAccessible(true);
			
			s = (Socket)f.get(client);
		} 
		catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		} 
		catch (SecurityException e1) {
			e1.printStackTrace();
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		assertEquals(socket, s);
		
	}

}
