package chat;

import java.io.Serializable;

/*
 * Måns Grundberg
 */

public class User implements Serializable {
	private static final long serialVersionUID = 5256333028190488609L;
	private String username;
	
	public User(String username) {
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public int hashCode() {
		return username.hashCode();
	}

	public boolean equals(Object obj) {
		return username.equals(((User)obj).username);
	}
	

}
