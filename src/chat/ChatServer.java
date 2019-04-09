package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Måns Grundberg
 */

public class ChatServer {
	private ServerController controller;

	public ChatServer(int chatPort, ServerController controller) {
		this.controller = controller;
		controller.addServer(this);
		new ConnectionListener(chatPort).start();
	}

	public synchronized void sendObject(Object obj, ObjectOutputStream oos) {
		try {
			oos.writeObject(obj);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

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
				while (!userConfirmed) {
					try {
						String command = ois.readUTF();
						this.user = (User) ois.readObject();
						String password = ois.readUTF();
						userConfirmed = controller.login(command, user, password, oos);
					} catch (IOException | ClassNotFoundException e) {
						System.err.println(e);
						controller.disconnectUser(user);
						socket.close();
					}
				}
				while (true) {
					try {
						controller.newMessage((Message) ois.readObject());
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
