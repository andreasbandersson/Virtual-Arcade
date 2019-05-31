package pong;

import java.awt.Rectangle;

public class Ball {
	private int xpos;
	private int ypos;
	private int radius;
	private double dx; // Bollens horisontella färdriktning
	private double dy; // Bollens vertikala färdriktning
	private int width;
	private int height;
	private int acceleration = 0;


	public Ball() {
		dx = 6;
		dy = 3;
		width = 10;
		height = 10;
		radius = 5;
		xpos = Pong.WIDTH / 2;
		ypos = Pong.HEIGHT / 2;
	}

	public void move() {
		xpos += dx + acceleration;
		ypos += dy + acceleration;
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

	public void changeDirection(int gameLevel) {
		this.dx = -this.dx;
		increaseSpeed(gameLevel);
	}

	public double getDx( ) {
		return this.dx;
	}

	public double getDy() {
		return this.dy;
	}

	// 0.5 (+-) (0.15 * 1) = 0.65 	-- level 1, (in/de)crease by 0.15
	// 0.5 (+-) (0.15 * 2) = 0.8	-- level 2, (in/de)crease by 0.3
	// 0.5 (+-) (0.15 * 3) = 0.95	-- level 3, (in/de)crease by 0.45
	private void increaseSpeed(int gameLevel) {
		if (this.dx < 0) {
			this.dx -= 0.5 - (0.15 * gameLevel);

		} else {
			this.dx += 0.5 + (0.15 * gameLevel);
		}
	}

	public void reset() {
		this.xpos = Pong.WIDTH - 100;
		this.ypos = Pong.HEIGHT / 2;
		this.dx = -6;
	}

	public Rectangle getRect() {
		return new Rectangle(xpos, ypos, width, height);
	}

}