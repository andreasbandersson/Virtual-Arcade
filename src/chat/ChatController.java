package chat;

/*
 * Måns Grundberg
 */
public class ChatController {
	private ChatTestUI ui; // TEMPORÄRT UI. ENDAST FÖR TEST-SYFTE.
	private ChatClient client;
	private User user;
	private UserList userList;

	public void login(String username, String password) {
		client.login(new User(username), password);
	}

	public void sendMessage(String text) {
		client.sendMessage(new Message(user, text));
	}

	public void incoming(Object obj) {
		if (obj instanceof Message) {
			Message message = (Message) obj;
			ui.addNewMessage(message.getTimeStamp() + ": " + message.getSender() + ": " + message.getText());
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
	public void checkLoginFailure(String str) {
		if (str == "Wrong password") {

		} else if (str == "Username already in use") {

		}
	}

}
