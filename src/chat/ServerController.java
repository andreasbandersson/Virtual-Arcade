package chat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Handles the logic of the chat/login systems server side
 * 
 * @author Mans
 *
 */

public class ServerController {
	private ClientStreams clientStreams = new ClientStreams();
	private RegisteredUsers users;
	private ChatServer server;
//	private Highscore[] snakeScore = new Highscore[10];
//	private Highscore[] spaceScore = new Highscore[10];
	private HighscoreList snakeScore = new HighscoreList();
	private HighscoreList spaceScore = new HighscoreList();

	public ServerController() {
		loadUsers();
	}

	public void addServer(ChatServer server) {
		this.server = server;
	}

	/**
	 * Sends the incoming message to all connected users or, if a private message,
	 * to the specified receiver
	 * 
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
	 * 
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
	 * 
	 * @param command  The command sent by the client, indicating the users desired
	 *                 action
	 * @param user     The specified user
	 * @param password The specified password
	 * @param oos      The ObjectOutputStream connected to the Client in question
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
	 * Checks whether the specified password matches the stores password for the
	 * user in question
	 * 
	 * @param user     The specified user
	 * @param password The specified password
	 * @param oos      The ObjectOutputStream connected to the Client in question
	 * @return True if password is correct, otherwise false
	 */

	private boolean checkPassword(User user, String password, ObjectOutputStream oos) {
		if (users.contains(user) && users.checkPassword(user, password)) {
			clientStreams.put(user, oos);
			server.sendObject(user, oos);
			server.sendObject("LOGIN OK", oos);
			sendUserList(user);
			sendHighscoreList(snakeScore);
			sendHighscoreList(spaceScore);
			return true;
		} else {
			server.sendObject("Username and/or password is incorrect", oos);
			return false;
		}
	}

	/**
	 * Checks whether username is already in use. If not, adds user to list of
	 * registered users
	 * 
	 * @param user     The user to be created
	 * @param password The specified password
	 * @param oos      The ObjectOutputStream connected to the Client in question
	 * @return True if user is successfully created, false if username is already in
	 *         use
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
			saveUsers();
			return true;
		}
	}

	/**
	 * Removes the user and it's corresponding ObjectOutputStream from list
	 * 
	 * @param user The disconnecting user
	 */
	public void disconnectUser(User user) {
		clientStreams.remove(user);
	}

	/**
	 * Reads list of registered users from file
	 */
	private void loadUsers() {
		File contactFile = new File("files/registeredUsers.dat");
		if (contactFile.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(contactFile))) {
				users = (RegisteredUsers) ois.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if (users == null) {
			users = new RegisteredUsers();
		}
	}

	/**
	 * Writes list of registered users to file
	 */
	private void saveUsers() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/registeredUsers.dat"))) {
			oos.writeObject(users);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// TODO: Check/save highscores

	public void checkHighscore(Highscore highscore) {
		System.out.println("Server: new highscore");
		if (highscore.getGame().equals("Snake")) {
			if (snakeScore.add(highscore) == true) {
				newHighscore(highscore, snakeScore);
			}
		} else {
			if (spaceScore.add(highscore) == true) {
				newHighscore(highscore, spaceScore);
			}
		}
	}

	private void sendHighscoreList(HighscoreList highscoreList) {
		UserList temp = new UserList(clientStreams.getKeySet());

		for (int i = 0; i < temp.size(); i++) {
			server.sendObject(highscoreList, clientStreams.getOutputStream(temp.get(i)));
		}
		System.out.println("Server: Lista skickad");
	}
	
	private void newHighscore(Highscore highscore, HighscoreList highscoreList) {
		String str = highscore.getUser().getUsername() + " made it onto the " + highscore.getGame() + " Leaderboard with "
				+ highscore.getScore() + " points!";
		newMessage(new Message(str));
		sendHighscoreList(highscoreList);
	}
	
	

}
