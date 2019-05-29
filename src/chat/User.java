package chat;

import java.io.Serializable;

/**
 * Stores the information of a user
 * @author Mans
 *
 */

public class User implements Serializable {
	private static final long serialVersionUID = 5256333028190488609L;
	private String username;
	
	public User(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	/**
	 * Alters the hashcode in order for the objects username to function as the key in hashmaps
	 */
	public int hashCode() {
		return username.hashCode();
	}

	/**
	 * Overriding the equals method
	 */
	public boolean equals(Object obj) {
		return username.equals(((User)obj).username);
	}
	

}
