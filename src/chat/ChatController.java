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
		client = new ChatClient(60000, "localhost", this);
		client.connect();
		loginUi = new LoginTestUI(this);
		ui = new ChatTestUI(this);
	}

	public void login(String username, String password) {
		if (username.length() <= 0 || password.length() <= 0) {
			loginUi.setResponse("Enter username & password");
		} else {
			client.login(new User(username), password);
		}
	}

	public void newUser(String username, String password) {
		if (username.length() < 3 || password.length() < 5) {
			loginUi.setResponse("Username/password must be atleast 3/5 characters");
		} else {
			client.newUser(new User(username), password);
		}
	}

	public void sendMessage(String text) {
		client.sendMessage(new Message(user.getUsername(), text));
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

	// TODO
	private void checkLoginFailure(String str) {
		if (str.equals("LOGIN OK") || str.equals("USER CREATED")) {
			loginUi.disposeFrame();
		} else {
			loginUi.setResponse(str);
		}
	}

	public static void main(String[] args) {
		new ChatController();
	}

}
