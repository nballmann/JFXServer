package org.nic.jfxserver;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import org.nic.jfxserver.model.Person;
import org.nic.jfxserver.util.DbConnection;

public class Client implements Runnable {

	private Socket socket;

	public Client(Socket socket) {

		this.socket = socket;

	}

	@Override
	public void run() {

		Scanner in = null;
		PrintWriter out = null;

		//		ObjectInputStream objectIn= null;
		ObjectOutputStream objectOut = null;

		try {
			in = new Scanner(socket.getInputStream());

			out = new PrintWriter(socket.getOutputStream());

			//			objectIn = new ObjectInputStream(socket.getInputStream());

			while(true) {

				if(in.hasNext()) {

					String input = in.nextLine();

					System.out.println("Client said: " + input);

					out.println("You said: " + input);

					out.flush();

					if( input.equals("select") ) {

						try {
							//							if(out!=null)
							//								out.close();
						}
						catch(Exception e) {

						}
						objectOut = new ObjectOutputStream(socket.getOutputStream());

						ArrayList<Person> results = DbConnection.getAllPersonsFromDb();
						out.println("Query done..");
						objectOut.writeObject(results);
						out.flush();
						try {
							//							if(objectOut != null)
							//								objectOut.close();
						} catch (Exception e) { }

//						out = new PrintWriter(socket.getOutputStream());

					}


				}

			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(in!=null)
				in.close();
			if(out!=null)
				out.close();
		}
	}

}
