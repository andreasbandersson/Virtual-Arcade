package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import chat.Highscore;
import chat.Message;
import chat.User;

/**
 * Handles all communication with the chat- and login systems client.
 * @author Måns Grundberg
 *
 */

public class Server {
	private ServerController controller;

	public Server(int chatPort, ServerController controller) {
		this.controller = controller;
		controller.addServer(this);
		new ConnectionListener(chatPort).start();
	}

	/**
	 * Sends specified object to specified client
	 * @param obj The object to send
	 * @param oos The ObjectOutputStream to send through
	 */
	public void sendObject(Object obj, ObjectOutputStream oos) {
		try {
			System.out.println(Thread.currentThread().getName() + "skickar meddelande");
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public void sendHighscore(LinkedList<Highscore> list, ObjectOutputStream oos) {
		try {
			System.out.println(Thread.currentThread().getName() + "skickar meddelande");
			oos.writeUnshared(list);
			oos.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * Listens for connecting clients, spawning ClientHandlers
	 * @author Mans
	 *
	 */
	private class ConnectionListener extends Thread {
		private int port;

		public ConnectionListener(int port) {
			this.port = port;
		}

		@Override
		public void run() {
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				while (true) {
					try {
						Socket socket = serverSocket.accept();
						new ClientInputHandler(socket).start();
					} catch (IOException e) {
						System.err.println(e);
					}
				}
			} catch (IOException e1) {
				System.err.println(e1);
			}
		}
	}

	/**
	 * Handles all the incoming data from a specific Client
	 * @author Mans
	 *
	 */
	private class ClientInputHandler extends Thread {
		private User user;
		private Socket socket;
		private boolean userConfirmed = false;

		public ClientInputHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
				while (!userConfirmed) { // Loop until user has been confirmed (login/user created successfully)
					try {
						String command = ois.readUTF();
						this.user = (User) ois.readObject();
						String password = ois.readUTF();
						userConfirmed = controller.login(command, user, password, oos);
					} catch (IOException | ClassNotFoundException e) {
						System.err.println(e);
						socket.close();
						break;
					}
				}
				while (true) { // Listens for incoming messages aslong as the client is connected
					try {
						System.out.println(Thread.currentThread().getName() + "tar emot meddelande");
						Object obj = ois.readObject();
						if (obj instanceof Message) {
						controller.newMessage((Message) obj);
						} else {
							controller.checkHighscore((Highscore) obj);
						}
					} catch (ClassNotFoundException e) {
						System.err.println(e);
					}
				}
			} catch (IOException e) {
				try {
					System.out.println(socket.getInetAddress().getHostAddress() + " kopplar ner");
					controller.disconnectUser(user);
					socket.close();
				} catch (Exception e2) {
				}
			}
		}
	}
}