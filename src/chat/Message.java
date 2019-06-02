package chat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a message.
 * 
 * @author Mans
 *
 */

public class Message implements Serializable {
	private static final long serialVersionUID = -2953445922981279895L;
	private String sender;
	private String text;
	private SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm");
	private User receiver;

	public Message(String sender, String text) {
		this.sender = sender;
		this.text = text;
	}

	public Message(User receiver, String sender, String text) {
		this.receiver = receiver;
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
		return simpleDate.format(new Date());
	}

	/**
	 * Used to return the receiving user if message is sent as private
	 * 
	 * @return The receiving user
	 */
	public User getReceiver() {
		return receiver;
	}

}
