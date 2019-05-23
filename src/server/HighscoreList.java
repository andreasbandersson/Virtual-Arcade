package server;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

import chat.Highscore;

public class HighscoreList implements Serializable {
	private int maxSize = 10;
	private LinkedList<Highscore> list = new LinkedList<Highscore>();
	
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
	
	public synchronized Highscore getLast() {
		return list.getLast();
	}
	
	public synchronized Highscore get(int index) {
		return list.get(index);
	}
	
	public synchronized int size() {
		return list.size();
	}
	
	public synchronized LinkedList<Highscore> getList() {
		return this.list;
	}

}
