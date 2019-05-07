package chat;

import java.io.Serializable;

/**
 * 
 * @author Måns Grundberg
 *
 */

public class Highscore implements Comparable<Highscore>, Serializable {
	private static final long serialVersionUID = 1L;
	private String game;
	private User user;
	private int score;
	
	public Highscore (User user, String game, int score) {
		this.user = user;
		this.game = game;
		this.score = score;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public String getGame() {
		return this.game;
	}
	
	public int getScore() {
		return this.score;
	}
	
	@Override
	public int compareTo (Highscore highscore) {
		return this.score - highscore.getScore();
	}
}
