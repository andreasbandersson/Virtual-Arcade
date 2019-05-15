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
	private String playerScoreStr = "PLAYER: ";
	private String computerScoreStr = "COMPUTER: ";
	private int playerScore = 0;
	private int computerScore = 0;
	private long timeSinceLastUpdate = 0;
	private Boolean paused = false;

	public Game() {
		init();
		start();

	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	private void init() {
		canvas = new Canvas(Pong.WIDTH, Pong.HEIGHT);
		canvas.setId("Pong");
		gc = canvas.getGraphicsContext2D();
		ball = new Ball();
		player = new Paddle(10);
		computer = new Paddle(Pong.WIDTH - 20);
		addActionListeners();
	}

	public void handle(long now) {
		if (now - timeSinceLastUpdate >= 8000000) {
			ball.move();
			moveComputer();
			checkPaddles();
			checkCollision();
			draw();
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
		gc.setFont(Font.font(12));
		gc.setFill(Color.WHITE);
		gc.fillText(playerScoreStr, (Pong.WIDTH / 2) - 100, 20);
		gc.fillText(computerScoreStr, (Pong.WIDTH / 2) + 50, 20);
	}
	
	private void drawPaused() {
		Text temp = new Text("GAME PAUSED");
		temp.setFont(Font.font(20));
		gc.setFont(Font.font(20));
		gc.fillText("GAME PAUSED", Pong.WIDTH / 2 - temp.getLayoutBounds().getWidth() / 2, Pong.HEIGHT / 2);
	}

	public void addActionListeners() {
		canvas.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.A) {
				movePlayerDown = true;
			} else if (e.getCode() == KeyCode.D) {
				movePlayerUp = true;
			} else if (e.getCode() == KeyCode.P) {
				if (paused) {
					this.start();
					paused = false;
				} else {
					this.stop();
					paused = true;
					drawPaused();
				}
			}
		});

		canvas.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.A) {
				movePlayerDown = false;
			} else if (e.getCode() == KeyCode.D) {
				movePlayerUp = false;
			}
		});
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
			computerScore++;
			ball.reset();
		} else if (ball.getXpos() >= Pong.WIDTH) {
			playerScore++;
			ball.reset();
		}

		playerScoreStr = "PLAYER: " + Integer.toString(playerScore);
		computerScoreStr = "COMPUTER: " + Integer.toString(computerScore);
	}

}
