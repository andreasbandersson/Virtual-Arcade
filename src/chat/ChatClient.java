package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * Måns Grundberg
 */

public class ChatClient {
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private int port;
	private String ip;
	private boolean connected = false;
	private InputListener listener;
	private ChatController controller;

	public ChatClient(int port, String ip, ChatController controller) {
		this.controller = controller;
		this.ip = ip;
		this.port = port;
	}

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

	// TODO: Koppla till UI
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

	public void sendMessage(Message message) {
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

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
		
		// TODO: Koppla till UI
		public void terminate() {
			running = false;
			listener = null;
		}
	}
}
