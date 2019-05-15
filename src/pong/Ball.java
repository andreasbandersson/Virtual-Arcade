package pong;

import java.util.LinkedList;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
//Bollens storlek och hastighet
		this.width = Config.Ball.width;
		this.height = Config.Ball.height;
		this.speed = Config.Ball.speed;

		//Vilket h�ll bollen ska r�ra sig mot
		Random rand = new Random();
		directions = new int[]{-1, 1};
		this.dirX = directions[rand.nextInt(directions.length)];
		this.dirY = this.directions[rand.nextInt(this.directions.length)];

		//Bollen startar i mitten och tr�ffar ingen platform 
		this.hitPlatform = this.resetBall = false;
		
		//Bollens F�rg
		this.ballColor = Config.Ball.defaultColor;
	}

	//
	public void update(LinkedList<Platform> platforms, double dt) {
		this.x += (this.dirX * dt) * this.speed;

		if(this.x < (Config.Window.width/2+this.width) && Game.nextYLerp != -1) {
			Game.nextYLerp = this.y;
		}

		for(Platform platform: platforms) {
			if(platform.getRect().intersects(getRect().getX(), getRect().getY(), getRect().getWidth(), getRect().getHeight())); {
				if(platform.getPlayerId() == 1) {
					// reset nextYLerp to -1 because the ball is going to away
					Game.nextYLerp = -1;
				}

				this.ballColor = platform.getColor();

				// switch direction
				this.dirX *= 0.8 ;

				this.hitPlatform = true;

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
			Game.nextYLerp = y;
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

	public void render(GraphicsContext gc) {
		gc.setFill(this.ballColor);
		gc.fillOval(x, y, width, height);
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

	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}

}