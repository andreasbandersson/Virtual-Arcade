package chat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Måns Grundberg
 */

public class Message implements Serializable {
	private static final long serialVersionUID = -2953445922981279895L;
	private String sender;
	private String text;
	private String timeStamp;
	private SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm");
	private User receiver;
	
	public Message(String sender, String text) {
		this.sender = sender;
		this.text = text;
	}
	
	public Message(String text) {
		this.text = text;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String getTimeStamp() {
		setTimeStamp();
		return this.timeStamp;
	}
	
	private void setTimeStamp() {
		this.timeStamp = simpleDate.format(new Date());
	}
	
	public User getReceiver() {
		return receiver;
	}
	
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

}
