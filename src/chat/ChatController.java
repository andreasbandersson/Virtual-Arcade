package chat;

/*
 * Måns Grundberg	
 */
public class ChatController {
	private ChatTestUI ui; // TEMPORÄRT UI. ENDAST FÖR TEST-SYFTE.
	private LoginTestUI loginUi;
	private ChatClient client;
	private User user;
	private UserList userList;

	public ChatController() {
		client = new ChatClient(60000, "10.2.3.221", this);
		client.connect();
		loginUi = new LoginTestUI(this);
	}

	public void login(String username, String password) {
		if (username.length() <= 0 || password.length() <= 0) {
			loginUi.setResponse("Enter username & password");
		} else {
			client.login(new User(username), password);
		}
	}

	public void newUser(String username, String password) {
		if (username.length() < 3 || password.length() < 6) {
			loginUi.setResponse("Username/password too short/long");
		} else {
			client.newUser(new User(username), password);
		}
	}

	public void sendMessage(String text) {
		Message message;
		if (text.charAt(0) == '@') {
			sendPrivateMessage(text);
		} else {
			message = new Message(user.getUsername(), text);
			client.sendMessage(message);
			ui.addNewMessage(message.getTimeStamp() + ": " + "To All: " + message.getText());
		}
	}

	private void sendPrivateMessage(String text) {
		boolean found = false;
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getUsername().equals(text.substring(1, text.indexOf(' ')))) {
				Message message = new Message(userList.get(i), user.getUsername(),
						text.substring(text.indexOf(' ') + 1));
				client.sendMessage(message);
				ui.addNewMessage(message.getTimeStamp() + ": " + "To " + userList.get(i).getUsername() + " : "
						+ message.getText());
				found = true;
				break;
			}
		}
		if (!found) {
			ui.addNewMessage("Couldn't send message: " + text.substring(1, text.indexOf(' ')) + " is not online");
		}
	}

	public void incoming(Object obj) {
		if (obj instanceof Message) {
			Message message = (Message) obj;
			if (message.getSender() != null) {
				ui.addNewMessage(message.getTimeStamp() + ": " + message.getSender() + ": " + message.getText());
			} else {
				ui.addNewMessage(message.getText());
			}
		} else if (obj instanceof String) {
			checkLoginFailure((String) obj);
		} else if (obj instanceof User) {
			this.user = (User) obj;
		} else {
			userList = (UserList) obj;
			ui.updateUserList(userList);
		}
	}

	private void checkLoginFailure(String str) {
		if (str.equals("LOGIN OK")) {
			loginUi.disposeFrame();
			ui = new ChatTestUI(this);
			ui.addNewMessage("Welcome back " + this.user.getUsername() + "!");
		} else if (str.equals("USER CREATED")) {
			loginUi.disposeFrame();
			ui = new ChatTestUI(this);
			ui.addNewMessage("Welcome to Virtual Arcade " + this.user.getUsername() + "!");
		} else {
			loginUi.setResponse(str);
		}
	}

	public static void main(String[] args) {
		new ChatController();
	}

}
