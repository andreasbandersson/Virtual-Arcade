package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import chat.User;

/**
 * Stores all registered users and their passwords.
 * @author Mans
 *
 */

public class RegisteredUsers implements Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<User, String> users;
	
	public RegisteredUsers() {
		users = new HashMap<User, String>();
	}
	
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
