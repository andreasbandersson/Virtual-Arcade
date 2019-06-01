package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Client handling all communication between the chat system and the server
 * side.
 * 
 * @author Mans
 *
 */

public class ChatClient {
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private int port;
	private String ip;
	private boolean connected = false;
	private InputListener listener;
	private ClientController controller;

	public ChatClient(int port, String ip, ClientController controller) {
		this.controller = controller;
		this.ip = ip;
		this.port = port;
	}

	/**
	 * Connects to server.
	 */
	public void connect() {
		if (!connected) {
			try {
				socket = new Socket(ip, port);
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				this.listener = new InputListener();
				listener.start();
				connected = true;
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	/**
	 * Disconnects client from server.
	 */
	public void disconnect() {
		if (connected) {
			try {
				listener.terminate();
				oos.close();
				ois.close();
				socket.close();
				connected = false;
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	/**
	 * Sends specified user and password to server for check
	 * 
	 * @param user     The specified user
	 * @param password The specified password
	 */
	public void login(User user, String password) {
		try {
			oos.writeUTF("LOGIN");
			oos.writeObject(user);
			oos.writeUTF(password);
			oos.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * Sends specified user and password to server to create new user
	 * 
	 * @param user     The specified user
	 * @param password The specified password
	 */
	public void newUser(User user, String password) {
		try {
			oos.writeUTF("NEW USER");
			oos.writeObject(user);
			oos.writeUTF(password);
			oos.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * Sends message to server
	 * 
	 * @param message The message to send
	 */
	public void sendMessage(Message message) {
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void sendHighscore(Highscore highscore) {
		try {
			oos.writeObject(highscore);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * Listens for input from server, sending it along to controller
	 * 
	 * @author Mans
	 *
	 */
	private class InputListener extends Thread {
		private boolean running = true;

		@Override
		public void run() {
			while (running) {
				try {
					Object obj = ois.readObject();
					controller.incoming(obj);
				} catch (IOException | ClassNotFoundException e) {
					disconnect();
				}
			}
		}

		/**
		 * Terminates thread by interrupting it's run method
		 */
		public void terminate() {
			running = false;
			listener = null;
		}
	}
}