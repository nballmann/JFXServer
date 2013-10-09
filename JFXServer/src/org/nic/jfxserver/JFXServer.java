package org.nic.jfxserver;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.nic.jfxclient.model.DataPackage;

public class JFXServer extends Application {

	private static final int SERVER_PORT = 15001;

	private static String ip;

	private static ServerSocket server;

	private static boolean running = true;

	@FXML 
	private static ListView<String> clientListView;

	@FXML 
	private static Label ipLabel;

	private static ArrayList<Socket> socketList = new ArrayList<>();
	private static ArrayList<Integer> clientStates = new ArrayList<>();
	private static ObservableList<String> clientList = FXCollections.observableArrayList();
	private static ArrayList<DataPackage> dataList = new ArrayList<>();

	private static Runnable accept = new Runnable() {

		@Override
		public void run() {

			try {
				System.out.println("Starting Server on Port: " + SERVER_PORT);
				server = new ServerSocket(SERVER_PORT, 0, InetAddress.getLocalHost());

				new Thread(send).start();
				new Thread(receive).start();

				while(running) {

					try  {
						Socket socket = server.accept();

						synchronized (this) {

							System.out.println("Server/accept: new Client connected");

							ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

							final String username = (String) ois.readObject();

							System.out.println("Server/accept: read username: " + username);

							boolean accepted = true;

							for(int i = 0; i < dataList.size(); i++) {

								if(dataList.get(i).username.toLowerCase().equals(username.toLowerCase()))  {

									accepted = false;
									break;

								}

							}

							ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

							if(accepted) {

								oos.writeObject("Welcome to the Server!");

								System.out.println("Server/accept: wrote welcome message!");

								final String host = socket.getInetAddress().getHostAddress();

								Platform.runLater(new Runnable() {

									@Override
									public void run() {
										clientList.add(username + " - " + host);

									}

								});

								clientStates.add(0);

								DataPackage dp = new DataPackage();
								dp.username = username;

								dataList.add(dp);
								socketList.add(socket);
								System.out.println(socketList.size());
							}
							else {

								oos.writeObject("Your name is already taken!");

							}
						}
					}
					catch (InterruptedIOException e) {
						//						System.out.println("Connection Timeout");
						e.printStackTrace();
					} 
					catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}

				}
			} catch (InterruptedIOException e) {
				//				System.out.println("Connection Timeout");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {


			}

		}

	};

	private static Runnable send = new Runnable() {

		@Override
		public void run() {

			ObjectOutputStream oos;

			System.out.println("Send Thread started");

			while (running) {

				synchronized (this) {

					for ( int i = 0; i < socketList.size(); i++ ) {

						try {

							if(socketList.get(i) != null || !socketList.get(i).isClosed())
							{
								oos = new ObjectOutputStream(socketList.get(i).getOutputStream());
								int clientState = clientStates.get(i);
								oos.writeObject(clientState);

								//								System.out.println("Server/send: client state");

								oos = new ObjectOutputStream(socketList.get(i).getOutputStream());
								oos.writeObject(dataList);

								//								System.out.println("Server/send: data list");

								if( clientState == 1) // kicked by server
								{
									System.out.println("kicked by server");
									disconnectClient(i);
									i--;
								}
								else if(clientState == 2) // server disconnected
								{
									System.out.println("server disconnected");
									disconnectClient(i);
									i--;
								}
							}


						} 
						catch (Exception e) {
							e.printStackTrace(); 
						}

					}
				}

			}

		}

	};

	private static Runnable receive = new Runnable() {

		@Override
		public void run() {

			ObjectInputStream ois;

			System.out.println("Receive Thread started");


			while (running) {

				synchronized (this) {

					for (int i = 0; i < socketList.size(); i++) {

						try {

							if(socketList.get(i) != null || !socketList.get(i).isClosed())
							{
								ois = new ObjectInputStream(socketList.get(i).getInputStream());
								int receiveState = (Integer) ois.readObject();

								//							System.out.println("Server/receive: state");

								ois = new ObjectInputStream(socketList.get(i).getInputStream());
								DataPackage dp = (DataPackage) ois.readObject();

								//							System.out.println("Server/receive: datapackage");

								dataList.set(i, dp);

								if ( receiveState == 1 ) // client disconnected by user 
								{

									System.out.println("client disconnected by user");
									disconnectClient(i);
									i--;
								}
							}


						} 
						catch (Exception e) // client disconnected without notification of the server
						{
							System.out.println("bad client disconnect");
							disconnectClient(i);
							i--;
//							e.printStackTrace();
							
							for(String client : clientList) {
								
								System.out.println(client);
								
							}
//							break;
						}
					}

				}
			}

		}

	};


	public static void main(String[] args) {
		launch();
	}

	public static void disconnectClient(final int index) {

		try {

			Platform.runLater(new Runnable() {

				@Override
				public void run() {

					clientList.remove(index);
				}

			});

			dataList.remove(index);
			socketList.remove(index);
			clientStates.remove(index);

		} catch (Exception e) { 
//			e.printStackTrace();
			// not much to do here...
			}

	}

	@Override
	public void start(Stage stage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main_view.fxml"));

		StackPane pane = (StackPane) loader.load();

		Scene scene = new Scene(pane);

		stage.setScene(scene);
		stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent e) {

				cleanUpAndExit();
			}

		});


		//		this.stage = stage;

		//		clientListView = new ListView<String>(clientList);
		clientListView.setItems(clientList);

		ip = InetAddress.getLocalHost().getHostAddress() + ":" + SERVER_PORT;

		ipLabel.setText(ip);
		//		ipLabel = new Label(ip);

		stage.setTitle("JFX Server");
		stage.show();

		new Thread(accept).start();

	}

	@FXML
	private void handleDisconnectButton() {

		int selected = clientListView.getSelectionModel().getSelectedIndex();

		if(selected != -1) {

			try {

				System.out.println("trying to disconnect client " + selected);

				clientStates.set(selected, 1);

			} catch (Exception e) {}

		}
		else {
			System.out.println("no client selected");

		}

	}

	@FXML 
	private void handleClose() {

		cleanUpAndExit();

	}

	/**
	 * 
	 */
	private void cleanUpAndExit() {
		if(socketList.size() != 0) {

			try {

				for (int i = 0; i < clientStates.size(); i++) {

					clientStates.set(i, 2);

				}

				Thread.sleep(220);

				running = false;
				server.close();

			} catch (Exception ex) {}

		}

		receive = null;
		send = null;

		System.exit(0);
	}

}
