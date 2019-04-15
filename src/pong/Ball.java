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
	private Color ballColor;
	
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
		this.ballColor = Config.Ball.defaultColor;
	}
	
	public void update(LinkedList<Platform> platforms, double dt) {
		
		this.x += (this.dirX * dt) * this.speed;

		for(Platform platform: platforms) {
			if(platform.getBounds().intersects(getBounds())) {
				
				// switch direction
				this.dirX *= -1;
				
				this.hitPlatform = true;
				
				setColor(platform.getColor());
				
				break;
			}

		}
		
		
		
		if(this.resetBall) {
			resetPos();
			this.resetBall = false;
		}
		
		this.y += (this.dirY * dt) * this.speed;
		
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
		g.setColor(this.ballColor);
		g.fillOval(x, y, width, height);
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	
	//bollen ändrar färg till platform färg 
	public void setColor(Color newColor) {
		this.ballColor = newColor;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
}
