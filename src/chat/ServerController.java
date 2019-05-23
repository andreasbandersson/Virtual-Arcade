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
import java.util.LinkedList;

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
	private HighscoreList snakeScore;
	private HighscoreList spaceScore;

	public ServerController() {
		loadUsers();
		loadHighscores(snakeScore, "snake");
		loadHighscores(spaceScore, "space");
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
			if (snakeScore != null) {
			sendHighscoreList(snakeScore.getList());
			}
			if (spaceScore != null) {
			sendHighscoreList(spaceScore.getList());
			}
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
			if (snakeScore.checkScore(highscore) == true) {
				snakeScore.add(highscore);
				newHighscore(highscore, snakeScore.getList());
				saveHighscores(snakeScore, "snake");
			}
		} else {
			if (spaceScore.checkScore(highscore) == true) {
				spaceScore.add(highscore);
				System.out.println("Server: highscore tillagt");
				newHighscore(highscore, spaceScore.getList());
				saveHighscores(spaceScore, "space");
			}
		}
	}
	

	private void sendHighscoreList(LinkedList<Highscore> list) {
		UserList temp = new UserList(clientStreams.getKeySet());
		
		if (list.size() > 0) {
		System.out.println("SendHighscoreList: " + list.get(0).getScore());
		}
		for (int i = 0; i < temp.size(); i++) {
			server.sendHighscore(list, clientStreams.getOutputStream(temp.get(i)));
		}
		System.out.println("Server: Lista skickad");
	}
	
	private void newHighscore(Highscore highscore, LinkedList<Highscore> list) {
		String str = highscore.getUser().getUsername() + " made it onto the " + highscore.getGame() + " Leaderboard with "
				+ highscore.getScore() + " points!";
		newMessage(new Message(str));
		sendHighscoreList(list);
	}
	
	private void loadHighscores(HighscoreList list, String name) {
		File highscoreFile = new File("files/" + name + ".dat");
		if (highscoreFile.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(highscoreFile))) {
				if (name.equals("space")) {
				spaceScore = (HighscoreList) ois.readObject();
				} else if (name.equals("snake")) {
					snakeScore = (HighscoreList) ois.readObject();
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if (name.equals("snake")) {
			snakeScore = new HighscoreList();
			System.out.println("Initierat highscorelist");
		} else if (name.equals("space")) {
			spaceScore = new HighscoreList();
		}
	}

	private void saveHighscores(HighscoreList list, String name) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/" + name + ".dat"))) {
			oos.writeObject(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
