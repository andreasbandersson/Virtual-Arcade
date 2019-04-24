package chat;

import java.io.ObjectOutputStream;

/**
 * Handles the logic of the chat and login systems server side
 * @author Mans
 *
 */

public class ServerController {
	private ClientStreams clientStreams = new ClientStreams();
	private RegisteredUsers users = new RegisteredUsers();
	private ChatServer server;

	public void addServer(ChatServer server) {
		this.server = server;
	}

	/**
	 * Sends the incoming message to all connected users or, if a private message, to the specified receiver
	 * @param message
	 */
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

	/**
	 * Distributes an updated list of connected users
	 * @param user
	 */
	private void sendUserList(User user) {
		UserList temp = new UserList(clientStreams.getKeySet());
		for (int i = 0; i < temp.size(); i++) {
			server.sendObject(temp, clientStreams.getOutputStream(temp.get(i)));
		}
		newMessage(new Message(user.getUsername() + " is now online"));
	}

	/**
	 * Checks whether user tries to login or create a new user
	 * @param command The command sent by the client, indicating the users desired action
	 * @param user The specified user
	 * @param password The specified password
	 * @param oos The ObjectOutputStream connected to the Client in question
	 * @return True if login/creating user is successful, otherwise false
	 */
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

	/**
	 * Checks whether the specified password matches the stores password for the user in question
	 * @param user The specified user
	 * @param password The specified password
	 * @param oos The ObjectOutputStream connected to the Client in question
	 * @return True if password is correct, otherwise false
	 */
	private boolean checkPassword(User user, String password, ObjectOutputStream oos) {
		if (users.contains(user) && users.checkPassword(user, password)) {
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

	/**
	 * Checks whether username is already in use. If not, adds user to list of registered users
	 * @param user The user to be created
	 * @param password The specified password
	 * @param oos The ObjectOutputStream connected to the Client in question
	 * @return True if user is successfully created, false if username is already in use
	 */
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

	/**
	 * Removes the user and it's corresponding ObjectOutputStream from list
	 * @param user The disconnecting user
	 */
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
