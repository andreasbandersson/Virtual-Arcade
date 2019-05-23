package pong2;

import java.awt.Rectangle;

public class Paddle {
	
	private int height;
	private int width;
	private int xPos;
	private int yPos;
	private double dy;
	
	public Paddle(int xPos, double dy) {
		this.dy = dy;
		this.height = 40;
		this.width = 10;
		this.xPos = xPos;
		this.yPos = (Pong.HEIGHT / 2) - height / 2;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getXpos() {
		return xPos;
	}
	
	public int getYpos() {
		return this.yPos;
	}
	
	public void moveUp() {
		if (this.yPos > 0) {
		this.yPos -= this.dy;
		}
	}
	
	public void moveDown() {
		if (this.yPos < Pong.HEIGHT - this.height) {
		this.yPos += this.dy;
		}
	}
	
	public void reset(int xpos) {
		this.xPos = xPos;
		this.yPos = (Pong.HEIGHT / 2) - height / 2;
	}
	
	public Rectangle getRect() {
		return new Rectangle(xPos, yPos, width, height);
	}

}
