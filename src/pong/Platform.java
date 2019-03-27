package pong;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Platform {

	private int x, y, speed;
	private int width,height, xOffset;
	private boolean isLeftPlatform;
	private int points;
	private int id;
	
	public Platform(boolean isLeftPlatform) {
		
		this.points = 0;
		this.width = Config.Platform.width;
		this.height = Config.Platform.height;
		this.xOffset = Config.Platform.xOffset;
		this.speed = Config.Platform.speed;
		
		if(isLeftPlatform) {
			this.isLeftPlatform = true;
			
			// Set x position for Left platform
			this.x = this.xOffset;
			this.id = 1;
			
		} else {
			this.isLeftPlatform = false;
		
			// Set x position for Right platform
			this.x = Config.Window.width - this.width - this.xOffset;
			this.id = 2;

		
		}

		this.y = (Config.Window.height/2) - this.height/2;
		
	}
	
	public void update(HashMap<Integer, Boolean> keyDownMap) {
		if(this.isLeftPlatform) {
			
			// UP
			if(keyDownMap.containsKey(KeyEvent.VK_W)) {
				this.y -= this.speed;
			// Down
			} else if(keyDownMap.containsKey(KeyEvent.VK_S)) {
				this.y += this.speed;
			}
		} else {
			
			// arrow UP
			if(keyDownMap.containsKey(KeyEvent.VK_UP)) {
				this.y -= this.speed;

			// arrow Down
			} else if(keyDownMap.containsKey(KeyEvent.VK_DOWN)) {
				this.y += this.speed;

			}
		}
		
		if(this.y < 0) {
			this.y = 0;
		} else if(this.y > Config.Window.height - this.height) {
			this.y = Config.Window.height - this.height;
		}
	}
	
	public void render(Graphics2D g) {
	
		// Change color before drawing
		g.setColor(Config.Platform.color);
		
		// Draw the platform at position(x, y)
		g.fillRect(x, y, width, height);
	}
	
	public int getPlayerId() {
		return this.id;
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public void increasePoints() {
		this.points++;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
}
