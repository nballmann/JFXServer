package org.nic.jfxserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = null; 
		Socket connectionSocket = null;
		BufferedReader inFromClient = null;
		DataOutputStream outToClient = null;

		try {

			welcomeSocket = new ServerSocket(6789);

			while(true) {
				connectionSocket = welcomeSocket.accept();
				inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();
				capitalizedSentence = clientSentence.toUpperCase() + '\n';
				outToClient.writeBytes(capitalizedSentence);
			}

		}
		catch(IOException e) {
			System.out.println("Could not connect");
		}
		finally {
			welcomeSocket.close();
			connectionSocket.close();
			inFromClient.close();
			outToClient.close();
		}
	}

}
