package pong;



import java.util.HashMap;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform {

	private int x, y, speed;
	private int width,height, xOffset;
	private boolean isLeftPlatform;
	private int points;
	private int id;
	private Color platformColor;
	
	public Platform(boolean isLeftPlatform) {
		
		this.points = 0;
		this.width = Config.Platform.width;
		this.height = Config.Platform.height;
		this.xOffset = Config.Platform.xOffset;
		this.speed = Config.Platform.speed;
		this.platformColor = Config.Platform.color;
		
		if(isLeftPlatform) {
			this.isLeftPlatform = true;
			
			// Set x position for Left platform
			this.x = this.xOffset;
			this.id = 1;
			
			this.speed *= 0.2;
			
			Random rand = new Random();
			int colorIndex = rand.nextInt(Config.Game.colors.length-1);
			System.out.println(colorIndex + "AHSKLJDHKAJHSDKJ");
			this.platformColor = Config.Game.colors[colorIndex];
			
		} else {
			this.isLeftPlatform = false;
		
			// Set x position for Right platform
			this.x = Config.Window.width - this.width - this.xOffset;
			this.id = 2;

		
		}

		this.y = (Config.Window.height/2) - this.height/2;
		
	}
	
	public void update(/*HashMap<Integer, Boolean> keyDownMap, */double dt) {
		if(this.isLeftPlatform) {

		//	if(Game.nextYLerp == -1) {
		//		this.y = (int) Game.lerp(y, Config.Window.height/2-height/2, (float)dt);
		//		System.out.println(this.y);
		//	}

			
		} else {
			
			/*
			// arrow UP
			if(keyDownMap.containsKey(KeyEvent.VK_UP)) {
				this.y -= this.speed * dt;

			// arrow Down
			} else if(keyDownMap.containsKey(KeyEvent.VK_DOWN)) {
				this.y += this.speed * dt;

			}
			
			*/
		}
		
		
		// keep platform inside window
		if(this.y < 0) {
			this.y = 0;
		} else if(this.y > Config.Window.height - this.height) {
			this.y = Config.Window.height - this.height;
		}
	}
	
	public void render(GraphicsContext gc) {
	
		// Change color before drawing
		this.setColor(this.platformColor);
		
		// Draw the platform at position(x, y)
		gc.fillRect(x, y, width, height);
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
	
	public void setColor(Color newColor) {
		this.platformColor = newColor;
	}
	
	public Color getColor() {
		return this.platformColor;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void resetPoints() {
		this.points = 0;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
}
