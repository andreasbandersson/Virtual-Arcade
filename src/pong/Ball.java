package pong;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Random;

public class Ball {
	
	private int x, y, dirX, dirY;
	private int width, height;
	private int[] directions;
	
	private boolean hitPlatform, resetBall;
	
	private int speed;
	
	public Ball(int x, int y) {
		this.x = x;
		this.y = y;
		
		this.width = Config.Ball.width;
		this.height = Config.Ball.height;
		this.speed = Config.Ball.speed;
		
		Random rand = new Random();
		directions = new int[]{-1, 1};
		this.dirX = directions[rand.nextInt(directions.length)];
		this.dirY = this.directions[rand.nextInt(this.directions.length)];
		
		this.hitPlatform = this.resetBall = false;
	}
	
	public void update(LinkedList<Platform> platforms) {
		
		this.x += this.dirX * this.speed;

		for(Platform platform: platforms) {
			if(platform.getBounds().intersects(getBounds())) {
				
				// switch direction
				this.dirX *= -1;
				
				this.hitPlatform = true;
				
				break;
			}
		
			// +1 to left platform
			if(this.x+this.width >= Config.Window.width) {
				if(platform.getPlayerId() == 1)
					platform.increasePoints();
				
				this.resetBall = true;
			} else if(this.x <= 0) {
				if(platform.getPlayerId() == 2)
					platform.increasePoints();
			
				this.resetBall = true;
			}
		}
		
		
		
		if(this.resetBall) {
			resetPos();
			this.resetBall = false;
		}
		
		this.y += this.dirY * this.speed;
		
		if(this.y <= 0 || (this.y + this.height >= Config.Window.height)) {
			this.dirY *= -1;
		}
		
		if(this.hitPlatform) {
			Sound.play(Config.Ball.bounceSound);
			this.hitPlatform = false;
		}
	
				
	}
	
	public void resetPos() {
		this.x = (Config.Window.width/2)-(Config.Ball.width/2);
		this.y = (Config.Window.height/2)-(Config.Ball.height/2);

		Random rand = new Random();
		this.directions = new int[]{-1, 1};
		this.dirX = this.directions[rand.nextInt(this.directions.length)];
		this.dirY = this.directions[rand.nextInt(this.directions.length)];
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillOval(x, y, width, height);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
}
