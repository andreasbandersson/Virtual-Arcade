package pong;

import java.awt.Rectangle;

/**
	@author Gulcin Kanat & Måns Grundberg
**/
public class Paddle {

	private int height;
	private int width;
	private int xPos;
	private int yPos;
	private double dy;
	private int acceleration = 0;

	public Paddle(int xPos, double dy, int height) {
		this.dy = dy;
		this.height = height;
		this.width = 10;
		this.xPos = xPos;
		this.yPos = (Pong.HEIGHT / 2) - height / 2;
	}

	public void setAcceleration(int acceleration){
		if (acceleration < 4){
			this.acceleration = acceleration;
		}
	}

	/**
		Returns acceleration of Paddle
		@return acceleration
	**/
	public int getAcceleration() {
		return acceleration;
	}

	/**
		Returns height of Paddle
		@return height
	**/
	public int getHeight() {
		return this.height;
	}

	/**
		Returns width of Paddle
		@return width
	**/
	public int getWidth() {
		return this.width;
	}

	/**
		Returns xPos of Paddle
		@return xPos
	**/
	public int getXpos() {
		return xPos;
	}

	/**
		Returns yPos of Paddle
		@return yPos
	**/
	public int getYpos() {
		return this.yPos;
	}


	/**
		Moves paddle up based on dy
	**/
	public void moveUp() {

		if (this.yPos > 0) {
			this.yPos -= this.dy + acceleration;
		}
	}

	/**
		Moves paddle down based on dy
	**/
	public void moveDown() {
		if (this.yPos < Pong.HEIGHT - this.height) {
			this.yPos += this.dy + acceleration;
		} else {
			this.yPos = Pong.HEIGHT - this.height;
		}
	}

	/**
		Reset paddle position
	**/
	public void reset(int xpos) {
		this.xPos = xpos;
		this.yPos = (Pong.HEIGHT / 2) - height / 2;
	}

	/**
		Returns a Rectangle with current bounds
		@return Rectangle
	**/
	public Rectangle getRect() {
		return new Rectangle(xPos, yPos, width, height);
	}

}
