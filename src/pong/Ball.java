package pong;

import java.awt.Rectangle;

/**
	@author Gulcin Kanat & Måns Grundberg
**/
public class Ball {
	private int xpos;
	private int ypos;
	private int radius;
	private double dx; // Bollens horisontella färdriktning
	private double dy; // Bollens vertikala färdriktning
	private int width;
	private int height;
	private int acceleration = 0;

	/**
		Ball constructor
	**/
	public Ball() {
		dx = 6;
		dy = 3;
		width = 10;
		height = 10;
		radius = 5;
		xpos = Pong.WIDTH / 2;
		ypos = Pong.HEIGHT / 2;
	}

	/**
		Moves ball object in x and y directions depending on dx, dy & acceleration
	**/
	public void move() {
		xpos += dx + acceleration;
		ypos += dy + acceleration;
	}

	/**
		@return returns width of ball
	**/
	public int getWidht() {
		return this.width;
	}

	/**
		@return returns height of ball
	**/
	public int getHeight() {
		return this.height;
	}

	/**
		@return returns X-position of ball
	**/
	public int getXpos() {
		return this.xpos;
	}

	/**
		@return returns Y-position of ball
	**/
	public int getYpos() {
		return this.ypos;
	}

	/**
		@return returns radius of ball
	**/
	public int getRadius() {
		return this.radius;
	}

	/**
		changes (y)direction to the opposite when it collides with walls
	**/
	public void bounceWall() {
		this.dy = -this.dy;
	}

	/**
		Changes x(direction) when the ball hits a paddle
		and calls increaseSpeed()
	**/
	public void changeDirection(int gameLevel) {
		this.dx = -this.dx;
		increaseSpeed(gameLevel);
	}

	/**
		@return dx ( horizontal step )
	**/
	public double getDx( ) {
		return this.dx;
	}

	/**
		@return dy ( vertical step )
	**/
	public double getDy() {
		return this.dy;
	}

	/**
		(in/de)creases ball speed, with a small amount,
		based on the direction ball is traveling.
	**/
	private void increaseSpeed(int gameLevel) {

		// 0.5 (+-) (0.15 * 1) = 0.65 	-- level 1, (in/de)crease by 0.15
		// 0.5 (+-) (0.15 * 2) = 0.8	-- level 2, (in/de)crease by 0.3
		// 0.5 (+-) (0.15 * 3) = 0.95	-- level 3, (in/de)crease by 0.45

		if (this.dx < 0) {
			this.dx -= 0.5 - (0.15 * gameLevel);

		} else {
			this.dx += 0.5 + (0.15 * gameLevel);
		}

	}

	/**
		Resets ball position & dx
	**/
	public void reset() {
		this.xpos = Pong.WIDTH - 100;
		this.ypos = Pong.HEIGHT / 2;
		this.dx = -6;
	}

	/**
		Returns a rectangle with current bounds.
		@return Rectangle
	**/
	public Rectangle getRect() {
		return new Rectangle(xpos, ypos, width, height);
	}

}
