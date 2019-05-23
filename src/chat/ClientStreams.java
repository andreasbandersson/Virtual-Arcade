package chat;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import chat.User;

/**
 * Stores the ObjectOutputStreams for all connected users.
 * @author Mans
 *
 */

public class ClientStreams {
	private HashMap<User, ObjectOutputStream> streams = new HashMap<User, ObjectOutputStream>();

	public synchronized void put(User user, ObjectOutputStream oos) {
		streams.put(user, oos);
	}

	public synchronized void remove(User user) {
		streams.remove(user);
	}

	public synchronized ObjectOutputStream getOutputStream(User user) {
		return streams.get(user);
	}

	/**
	 * 
	 * @return An ArrayList containing all of the online users
	 */
	public synchronized ArrayList<User> getKeySet() {
		return new ArrayList<User>(streams.keySet());
	}

	public synchronized Boolean contains(User user) {
		return streams.containsKey(user);
	}

}
