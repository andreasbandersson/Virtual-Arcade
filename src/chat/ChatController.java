package chat;

import javax.swing.SwingUtilities;

/**
 * Controller-class for the chat systemts client-side environment.
 * 
 * @author Mans
 *
 */

public class ChatController {
	private ChatTestUI ui; // TEMPORÄRT UI. ENDAST FÖR TEST-SYFTE.
	// private ChatUI ui;
	private LoginTestUI loginUi;
	private ChatClient client;
	private User user;
	private UserList userList;

	public ChatController() {
		client = new ChatClient(60000, "localhost", this);
		client.connect();
		loginUi = new LoginTestUI(this);
	}

	/**
	 * Called when a user tries to login
	 * 
	 * @param username The specified username
	 * @param password The specified password
	 */
	public void login(String username, String password) {
		if (username.length() <= 0 || password.length() <= 0) {
			loginUi.setResponse("Enter username & password");
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
			loginUi.setResponse("Username/password too short");
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
				ui.addMessage(message.getTimeStamp() + ": " + "To All: " + message.getText());
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
				ui.addMessage(message.getTimeStamp() + ": " + "To " + userList.get(i).getUsername() + " : "
						+ message.getText());
				found = true;
				break;
			}
		}
		if (!found) { // User not online/doesn't exist
			ui.addMessage("Couldn't send message: " + text.substring(1, text.indexOf(' ')) + " is not online");
		}
	}

	/**
	 * Handles all incoming objects sent from Client.
	 * 
	 * @param obj The object received (Message/User/UserList/String)
	 */
	public void incoming(Object obj) {
		if (obj instanceof Message) {
			Message message = (Message) obj;
			if (message.getSender() != null) {
				ui.addMessage(message.getTimeStamp() + ": " + message.getSender() + ": " + message.getText());
			} else {
				ui.addMessage(message.getTimeStamp() + ": " + message.getText());
			}
		} else if (obj instanceof String) {
			checkServerResponse((String) obj);
		} else if (obj instanceof User) {
			this.user = (User) obj;
		} else {
			userList = (UserList) obj;
			ui.updateUserList(userList);
		}
	}

	/**
	 * Evaluates responses from server regarding status of login
	 * 
	 * @param str The server response
	 */
	private void checkServerResponse(String str) {
		if (str.equals("LOGIN OK")) { // Login successful, open chat
			loginUi.disposeFrame();
			ui = new ChatTestUI(this);
			// ui = new ChatUI(this);
			ui.addMessage("Welcome back " + this.user.getUsername() + "!");
		} else if (str.equals("USER CREATED")) { // User created successfully, open chat
			loginUi.disposeFrame();
			ui = new ChatTestUI(this);
			// ui = new ChatUI(this);
			ui.addMessage("Welcome to Virtual Arcade " + this.user.getUsername() + "!");
		} else { // Login unsuccessful, e.g. password and/or username incorrect, username taken
					// etc
			loginUi.setResponse(str);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ChatController();
			}
		});
	}

}
