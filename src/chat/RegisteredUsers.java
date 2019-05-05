package chat;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores all registered users and their passwords.
 * @author Mans
 *
 */

public class RegisteredUsers {
	private HashMap<User, String> users = new HashMap<User, String>();

	public synchronized void put(User user, String password) {
		users.put(user, password);
	}

	/**
	 * Checks whether if specified password is correct 
	 * @param user The user trying to login
	 * @param password The specified password
	 * @return True if password is a match, otherwise false
	 */
	
	public synchronized boolean checkPassword(User user, String password) {
		if (users.get(user).equals(password)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return A list of all registered users
	 */
	
	public synchronized ArrayList<User> getKeySet() {
		return new ArrayList<User>(users.keySet());
	}

	public synchronized Boolean contains(User user) {
		return users.containsKey(user);
	}


}
