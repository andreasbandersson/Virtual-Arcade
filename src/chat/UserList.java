package chat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a list containing all online users
 * 
 * @author Mans
 *
 */

public class UserList implements Serializable {
	private static final long serialVersionUID = -2112880894518873804L;
	private ArrayList<User> userList;

	public UserList(ArrayList<User> userList) {
		this.userList = userList;
	}

	public synchronized User get(int index) {
		return userList.get(index);
	}

	public synchronized void add(User user) {
		userList.add(user);
	}

	public synchronized int size() {
		return userList.size();
	}
}
