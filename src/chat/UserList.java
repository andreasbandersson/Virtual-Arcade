package chat;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Måns Grundberg
 */

public class UserList implements Serializable {
	private static final long serialVersionUID = -2112880894518873804L;
	private ArrayList<User> userList = new ArrayList<User>();

	public synchronized User get(int index) {
		return userList.get(index);
	}

	public synchronized User remove(int index) {
		return userList.remove(index);
	}

	public synchronized void add(User user) {
		userList.add(user);
	}
}
