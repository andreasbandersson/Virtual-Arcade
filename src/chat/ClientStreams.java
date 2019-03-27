package chat;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * Måns Grundberg
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

	public synchronized int size() {
		return streams.size();
	}

	public synchronized ArrayList<User> getKeySet() {
		return new ArrayList<User>(streams.keySet());
	}

	public synchronized Boolean contains(User user) {
		return streams.containsKey(user);
	}

}
