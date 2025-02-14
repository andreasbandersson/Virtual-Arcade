package chat;

import java.util.LinkedList;

import application.Leaderboard;
import application.LoginUI;
import application.MainUI;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

/**
 * Controller-class for the chat systemts client-side environment.
 * 
 * @author Måns Grundberg
 *
 */

public class ClientController {
	private ChatUI chatUI;
	private MainUI mainUI;
	private LoginUI loginUI;
	private ChatClient client;
	private User user;
	private UserList userList;
	private Leaderboard leaderboard;

	public ClientController() {
		client = new ChatClient(50000, "localhost", this);
		client.connect();
		initLoginUI();
		chatUI = new ChatUI(this);
	}

	public void addLeaderboard(Leaderboard leaderboard) {
		this.leaderboard = leaderboard;
	}

	private void initLoginUI() {
		new JFXPanel();
		Platform.runLater(new Runnable() {
			public void run() {
				loginUI = new LoginUI(ClientController.this);
				loginUI.start(LoginUI.stage);
			}
		});
	}

	private void initMainUI() {
		Platform.runLater(new Runnable() {
			public void run() {
				mainUI = new MainUI(chatUI, ClientController.this);
				mainUI.start(MainUI.primaryStage);
				chatUI.addMessage("Welcome to Virtual Arcade " + ClientController.this.user.getUsername() + "!", 1);
			}
		});
	}

	public void disconnect() {
		client.disconnect();
	}

	/**
	 * Called when a user tries to login
	 * 
	 * @param username The specified username
	 * @param password The specified password
	 */
	public void login(String username, String password) {
		if (username.length() <= 0 || password.length() <= 0) {
			loginUI.setResponse("Enter username & password");
		} else {
			client.login(new User(username), password);
		}
	}

	/**
	 * Called when the user tries to create a new user
	 * 
	 * @param username The specified username
	 * @param password The specified password
	 */
	public void newUser(String username, String password) {
		if (username.length() < 3 || password.length() < 6) {
			loginUI.setResponse("Username/password too short");
		} else {
			client.newUser(new User(username), password);
		}
	}

	/**
	 * Checks whether message is private or public
	 * 
	 * @param text The text message to send
	 */

	public void sendMessage(String text) {
		Message message;
		if (!text.equals("")) {
			if (text.charAt(0) == '@') { // Private message
				sendPrivateMessage(text);
			} else {
				message = new Message(user.getUsername(), text);
				client.sendMessage(message);
				chatUI.addMessage(message.getTimeStamp() + ": " + "To All: " + message.getText(), 0);
			}
		}
	}

	/**
	 * Used to send private messages.
	 * 
	 * @param text The text message to send
	 */
	private void sendPrivateMessage(String text) {
		boolean found = false;
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getUsername().equals(text.substring(1, text.indexOf(' ')))) {
				Message message = new Message(userList.get(i), user.getUsername(),
						text.substring(text.indexOf(' ') + 1));
				client.sendMessage(message);
				chatUI.addMessage(message.getTimeStamp() + ": " + "To " + userList.get(i).getUsername() + " : "
						+ message.getText(), 2);
				found = true;
				break;
			}
		}
		if (!found) { // User not online/doesn't exist
			chatUI.addMessage("Couldn't send message: " + text.substring(1, text.indexOf(' ')) + " is not online", 1);
		}
	}

	/**
	 * Handles all incoming objects sent from Client.
	 * 
	 * @param obj The object received (Message/User/UserList/String)
	 */
	@SuppressWarnings("unchecked")
	public void incoming(Object obj) {
		if (obj instanceof Message) {
			Message message = (Message) obj;
			if (message.getSender() != null) {
				int type = (message.getReceiver() == null) ? 0 : 2;
				chatUI.addMessage(message.getTimeStamp() + ": " + message.getSender() + ": " + message.getText(), type);
			} else {
				chatUI.addMessage(message.getTimeStamp() + ": " + message.getText(), 1);
			}
		} else if (obj instanceof String) {
			checkServerResponse((String) obj);
		} else if (obj instanceof User) {
			this.user = (User) obj;
		} else if (obj instanceof UserList) {
			Platform.runLater(new Runnable() {
				public void run() {
					userList = (UserList) obj;
					chatUI.updateUserList(userList);
				}
			});
		} else {
			updateHighscores((LinkedList<Highscore>) obj);
		}
	}

	/**
	 * Calls UI with updated highscore list.
	 * 
	 * @param list
	 */
	private void updateHighscores(LinkedList<Highscore> list) {
		Platform.runLater(new Runnable() {
			public void run() {
				leaderboard.updateHighscores(list);
			}
		});
	}

	/**
	 * Sends out new highscore through Client.
	 * 
	 * @param game  The game calling the method
	 * @param score The game score to check
	 */
	public void newHighscore(String game, int score) {
		client.sendHighscore(new Highscore(this.user, game, score));
	}

	/**
	 * Evaluates responses from server regarding status of login
	 * 
	 * @param str The server response
	 */
	private void checkServerResponse(String str) {
		if (str.equals("LOGIN OK") || str.equals("USER CREATED")) { // Login successful, open chat
			Platform.runLater(new Runnable() {
				public void run() {
					initMainUI();
					loginUI.terminate();
					try {
						loginUI.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else { // Login unsuccessful, e.g. password and/or username incorrect, username taken
			loginUI.setResponse(str);
		}
	}

	public static void main(String[] args) {
		new ClientController();
	}
}