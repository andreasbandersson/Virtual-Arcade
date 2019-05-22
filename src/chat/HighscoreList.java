package chat;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

public class HighscoreList implements Serializable {
	private int maxSize = 10;
	private LinkedList<Highscore> list = new LinkedList<Highscore>();
	
	public synchronized boolean add(Highscore highscore) {
		if (list.size() < maxSize) {
			list.addLast(highscore);
			sort();
			return true;
		} else if (list.size() >= maxSize && highscore.getScore() > list.getLast().getScore())  {
			list.removeLast();
			list.addLast(highscore);
			return true;
		} else return false;
	}
	
	public synchronized void sort() {
		list.sort(Collections.reverseOrder());
	}
	
	public synchronized Highscore getLast() {
		return list.getLast();
	}
	
	public synchronized Highscore get(int index) {
		return list.get(index);
	}
	
	public synchronized int size() {
		return list.size();
	}

}
