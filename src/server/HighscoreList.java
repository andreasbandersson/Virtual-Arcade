package server;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

import chat.Highscore;

/**
 * Used to store a list of highscores for a specific game
 * @author Måns Grundberg
 *
 */
public class HighscoreList implements Serializable {
	private static final long serialVersionUID = 1L;
	private int maxSize = 10;
	private LinkedList<Highscore> list = new LinkedList<Highscore>();
	
	/**
	 * Adds new highscore to the list
	 * @param highscore
	 */
	public synchronized void add(Highscore highscore) {
		if (list.size() < maxSize) {
			list.add(highscore);
			sort();
		} else if (list.size() >= maxSize && highscore.getScore() > list.getLast().getScore())  {
			list.removeLast();
			list.add(highscore);
			sort();
		}
	}
	
	/**
	 * Checks wether highscore should make it onto the list or not
	 * @param highscore The highscore to check
	 * @return True if highscore should be added to the list, otherwise false
	 */
	public synchronized boolean checkScore(Highscore highscore) {
		if (list.size() < maxSize) {
			return true;
		} else if (list.size() >= maxSize && highscore.getScore() > list.getLast().getScore()) {
			return true;
		} else return false;
	}
	
	public synchronized void sort() {
		list.sort(Collections.reverseOrder());
	}
	
	public synchronized LinkedList<Highscore> getList() {
		return this.list;
	}

}
