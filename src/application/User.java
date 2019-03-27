package application;

import java.io.Serializable;

import javax.swing.ImageIcon;

/*
 * Klassen representerar en användare med användarnamn och användarbild.
 */

public class User implements Serializable {
	private String userName;
	private ImageIcon image;

	public User(String userName, ImageIcon image) {
		this.userName = userName;
		this.image = image;
	}

	public String getUserName() {
		return userName;
	}

	public ImageIcon getImage() {
		return image;
	}

	public int hashCode() {
		return userName.hashCode();
	}

	public boolean equals(Object obj) {
		return userName.equals(((User)obj).userName);
	}
}
