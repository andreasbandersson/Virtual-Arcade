package chat;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisteredUsers {
	private HashMap<User, String> users = new HashMap<User, String>();

	public synchronized void put(User user, String password) {
		users.put(user, password);
	}

	public synchronized void remove(User user) {
		users.remove(user);
	}

	public synchronized boolean checkPassword(User user, String password) {
		if (users.get(user).equals(password)) {
			return true;
		} else {
			return false;
		}
	}

	public synchronized int size() {
		return users.size();
	}

	public synchronized ArrayList<User> getKeySet() {
		return new ArrayList<User>(users.keySet());
	}

	public synchronized Boolean contains(User user) {
		return users.containsKey(user);
	}


}
