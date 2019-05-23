package pong2;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Game extends AnimationTimer {
	private Canvas canvas;
	private Paddle player;
	private Paddle computer;
	private Ball ball;
	private Boolean movePlayerUp = false;
	private Boolean movePlayerDown = false;
	private Boolean moveCompUp = false;
	private Boolean moveCompDown = false;
	private GraphicsContext gc;
	private String playerScoreStr = "SCORE: ";
	private int playerScore = 0;
	private long timeSinceLastUpdate = 0;
	private boolean paused = false;
	private boolean gameOver = false;
	private boolean started = false;

	public Game() {
		init();
		drawStart();
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	private void init() {
		canvas = new Canvas(Pong.WIDTH, Pong.HEIGHT);
		canvas.setId("Pong");
		gc = canvas.getGraphicsContext2D();
		ball = new Ball();
		player = new Paddle(10, 5);
		computer = new Paddle(Pong.WIDTH - 20, 2.9);
		addActionListeners();
	}

	public void handle(long now) {
		if (now - timeSinceLastUpdate >= 8000000) {
			draw();
			ball.move();
			moveComputer();
			checkPaddles();
			checkCollision();
			timeSinceLastUpdate = now;
		}
	}

	// Kollar om paddlar ska flyttas
	private void checkPaddles() {
		if (movePlayerUp == true) {
			player.moveUp();
		} else if (movePlayerDown == true) {
			player.moveDown();
		}
		if (moveCompUp == true) {
			computer.moveUp();
			moveCompUp = false;
		} else if (moveCompDown == true) {
			moveCompDown = false;
			computer.moveDown();
		}
	}
	
	private void drawStart() {
		draw();
		Text temp = new Text("GAME OVER");
		temp.setFont(Font.font(20));
		gc.setFont(Font.font(20));
		gc.fillText("Control Padle with UP ARROW KEY & DOWN ARROW KEY", Pong.WIDTH / 10 - (temp.getLayoutBounds().getWidth() / 10), Pong.HEIGHT / 2);
		gc.fillText("PRESS [SPACE] TO START", Pong.WIDTH / 2.6 - (temp.getLayoutBounds().getWidth() / 2.6),
				(Pong.HEIGHT / 2) + 50);
		
	}

	// Anropar draw-metoder
	private void draw() {
		gc.clearRect(0, 0, 600, 400);
		gc.setFill(Color.BLACK);
		gc.fill();
		drawBall();
		drawPaddles();
		drawScoreBoard();
	}

	private void drawBall() {
		gc.setFill(Color.WHITE);
		gc.fillOval(ball.getXpos(), ball.getYpos(), ball.getWidht(), ball.getHeight());
	}

	private void drawPaddles() {
		gc.setFill(Color.WHITE);
		gc.fillRect(player.getXpos(), player.getYpos(), player.getWidth(), player.getHeight());
		gc.fillRect(computer.getXpos(), computer.getYpos(), computer.getWidth(), computer.getHeight());
	}

	private void drawScoreBoard() {
		Text temp = new Text(playerScoreStr);
		temp.setFont(Font.font(20));
		gc.setFont(Font.font(20));
		gc.setFill(Color.WHITE);
		gc.fillText(playerScoreStr, (Pong.WIDTH / 2) - temp.getLayoutBounds().getWidth() / 2, 20);
	}

	private void drawGameOver() {
		Text temp = new Text("GAME OVER - SCORE    ");
		temp.setFont(Font.font(20));
		gc.setFont(Font.font(20));
		gc.fillText("GAME OVER - SCORE: " + playerScore, Pong.WIDTH / 2 - (temp.getLayoutBounds().getWidth() / 2), Pong.HEIGHT / 2);
		gc.fillText("PRESS [R] TO RESTART", Pong.WIDTH / 2 - (temp.getLayoutBounds().getWidth() / 2),
				(Pong.HEIGHT / 2) + 50);
	}

	private void drawPaused() {
		Text temp = new Text("GAME PAUSED");
		temp.setFont(Font.font(20));
		gc.setFont(Font.font(20));
		gc.fillText("GAME PAUSED", Pong.WIDTH / 2 - (temp.getLayoutBounds().getWidth() / 2), Pong.HEIGHT / 2);
		gc.fillText("PRESS [P] TO UNPAUSE", Pong.WIDTH / 2 - (temp.getLayoutBounds().getWidth() / 2),
				(Pong.HEIGHT / 2) + 50);
	}

	public void addActionListeners() {
		canvas.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.DOWN) {
				movePlayerDown = true;
			} else if (e.getCode() == KeyCode.UP) {
				movePlayerUp = true;
			} else if (e.getCode() == KeyCode.P && gameOver == false) {
				if (paused) {
					this.start();
					paused = false;
				} else {
					this.stop();
					paused = true;
					drawPaused();
				}
			} else if (e.getCode() == KeyCode.R && gameOver == true) {
				ball.reset();
				this.start();
				gameOver = false;
			} else if (e.getCode() == KeyCode.SPACE && !started) {
				this.start();
			}
			e.consume();
		});

		canvas.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.DOWN) {
				movePlayerDown = false;
			} else if (e.getCode() == KeyCode.UP) {
				movePlayerUp = false;
			}
		});
	}
	
	public void setPaused() {
		if (!paused && !gameOver) {
		this.stop();
		paused = true;
		drawPaused();
		} else if (gameOver) {
			ball.reset();
			drawStart();
			started = false;
			gameOver = false;
		}
	}
	
	

	// Datorn rör sig mot bollen
	private void moveComputer() {
		if (ball.getDx() > 0) {
			if (computer.getYpos() > ball.getYpos()) {
				moveCompDown = false;
				moveCompUp = true;
			} else if (computer.getYpos() < ball.getYpos()) {
				moveCompUp = false;
				moveCompDown = true;
			}
		} else {
			moveCompUp = false;
			moveCompDown = false;
		}
	}

	// Kollar om bollen kolliderar med racket eller vägg
	private void checkCollision() {

		if ((ball.getRect().intersects(player.getRect()) && ball.getDx() < 0)
				|| (ball.getRect().intersects(computer.getRect()) && ball.getDx() > 0)) {
			ball.changeDirection();
		} else if (ball.getYpos() <= 0 || ball.getYpos() + ball.getRadius() >= Pong.HEIGHT) {
			ball.bounceWall();
		} else if (ball.getXpos() <= 0) {
			if (gameOver == false) {
				this.stop();
				player.reset(10);
				computer.reset(Pong.WIDTH - 20);
				drawGameOver();
				playerScore = 0;
				gameOver = true;
			}
		} else if (ball.getXpos() >= Pong.WIDTH) {
			playerScore += 50;
			ball.reset();
		}
		playerScoreStr = "PLAYER: " + Integer.toString(playerScore);
	}

}
