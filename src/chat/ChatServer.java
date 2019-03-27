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

	public ChatServer(int chatPort) {
		new ConnectionListener(chatPort).start();
	}

	public void senObject(Object obj, ObjectOutputStream oos) {
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
					ois.readUTF();

					/*
					 * TODO: OM utf = LOGIN -> testa att logga in Om utf = NEW USER -> skapa ny
					 * användare Loopen fortsätter lyssna tills controllern meddelar att user är
					 * "confirmed", d.v.s. antingen att ny user skapats eller att användarnamn +
					 * lösenord är korrekt
					 */
				}
				while (true) {
					// TODO: Lyssna efter meddelanden
				}
			} catch (IOException e) {
				try {
					System.out.println(socket.getInetAddress().getHostAddress() + " kopplar ner");
					socket.close();
				} catch (Exception e2) {
				}
			}
		}

		public void confirmUser(User user) {
			userConfirmed = true;
			this.user = user;
		}
	}

}
