package pong2;

import java.awt.Rectangle;

public class Ball {
	private int xpos;
	private int ypos;
	private int radius;
	private int dx; // Bollens horisontella färdriktning
	private int dy; // Bollens vertikala färdriktning
	private int width;
	private int height;
	
	public Ball() {
		dx = 4;
		dy = 2;
		width = 10;
		height = 10;
		radius = 5;
		xpos = Pong.WIDTH / 2;
		ypos = Pong.HEIGHT / 2;
	}
	
	public void move() {
		xpos += dx;
		ypos += dy;
	}
	
	public int getWidht() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getXpos() {
		return this.xpos;
	}
	
	public int getYpos() {
		return this.ypos;
	}
	
	public int getRadius() {
		return this.radius;
	}
	
	public void bounceWall() {
		this.dy = -this.dy;
	}
	
	public void changeDirection() {
		this.dx = -this.dx;
	}
	
	public int getDx( ) {
		return this.dx;
	}
	
	public void reset() {
		this.xpos = Pong.WIDTH / 2;
		this.ypos = Pong.HEIGHT / 2;
		this.dx = -4;
	}
	
	public Rectangle getRect() {
		return new Rectangle (xpos, ypos, width, height);
	}

}
