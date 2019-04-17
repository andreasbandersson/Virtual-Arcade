package chat;

import java.io.ObjectOutputStream;

/*
 * Måns Grundberg
 */

public class ServerController {
	private ClientStreams clientStreams = new ClientStreams();
	private RegisteredUsers users = new RegisteredUsers();
	private ChatServer server;

	public void addServer(ChatServer server) {
		this.server = server;
	}

	public void newMessage(Message message) {
		UserList temp = new UserList(clientStreams.getKeySet());
		if (message.getReceiver() == null) { // Not a private message, send to all
			for (int i = 0; i < temp.size(); i++) {
				if (!temp.get(i).getUsername().equals(message.getSender())) { // Exclude sender
					server.sendObject(message, clientStreams.getOutputStream(temp.get(i)));
				}
			}
		} else {
			server.sendObject(message, clientStreams.getOutputStream(message.getReceiver()));
		}
	}

	private void sendUserList(User user) {
		UserList temp = new UserList(clientStreams.getKeySet());
		for (int i = 0; i < temp.size(); i++) {
			server.sendObject(temp, clientStreams.getOutputStream(temp.get(i)));
		}
		newMessage(new Message(user.getUsername() + " is now online"));
	}

	public boolean login(String command, User user, String password, ObjectOutputStream oos) {
		if (command.equals("LOGIN")) {
			if (clientStreams.contains(user)) {
				server.sendObject("User already logged in", oos);
				return false;
			} else {
				System.out.println("Kollar lösenord");
				return checkPassword(user, password, oos);
			}
		} else {
			return newUser(user, password, oos);
		}
	}

	private boolean checkPassword(User user, String password, ObjectOutputStream oos) {
		if (users.contains(user) && users.checkPassword(user, password)) {
			System.out.println("Lösenord OK");
			clientStreams.put(user, oos);
			server.sendObject(user, oos);
			server.sendObject("LOGIN OK", oos);
			sendUserList(user);
			return true;
		} else {
			server.sendObject("Username and/or password is incorrect", oos);
			return false;
		}
	}

	private boolean newUser(User user, String password, ObjectOutputStream oos) {
		if (users.contains(user)) {
			server.sendObject("Username already in use", oos);
			return false;
		} else {
			users.put(user, password);
			clientStreams.put(user, oos);
			server.sendObject(user, oos);
			server.sendObject("USER CREATED", oos);
			sendUserList(user);
			return true;
		}
	}

	public void disconnectUser(User user) {
		clientStreams.remove(user);
	}

	// TODO: Läs in användare från fil
	private void loadUsers() {

	}

	// TODO: Skriv användare till fil
	private void saveUsers() {

	}

}
