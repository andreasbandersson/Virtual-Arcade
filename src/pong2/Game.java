package pong2;



import application.JukeBox2;
import chat.ChatController;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Game extends AnimationTimer implements Runnable {
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
	private boolean firstGame = true;
	private JukeBox2 jukeBox2 = new JukeBox2();
	private Executor executor = Executors.newFixedThreadPool(2);
	private ChatController controller;

	// private Image pongBg;

	public Game(ChatController controller) {
		this.controller = controller;
		init();
		drawStart();
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	private void init() {
		canvas = new Canvas(Pong.WIDTH, Pong.HEIGHT);
		canvas.setId("Pong");
//		try {
//			pongBg = new Image(new FileInputStream("images/pongGridBg.png"));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		gc = canvas.getGraphicsContext2D();
		ball = new Ball();
		player = new Paddle(10, 5, 60);
		computer = new Paddle(Pong.WIDTH - 20, 3, 60);
		addActionListeners();
	}

	public void handle(long now) {
		if (now - timeSinceLastUpdate >= 8000000) {
			draw();
			ball.move();
			moveComputer();
			checkPaddles();
			checkCollision();
			// if (scored && count < 20) {
			// drawScore();
			// count++;
			// if (count == 20) {
			// count = 0;
			// scored = false;
			// }
			// }
			timeSinceLastUpdate = now;
		}
	}

	// Kollar om paddlar ska flyttas
	private void checkPaddles() {
		if (movePlayerUp == true) {
			player.moveUp();
			player.setAcceleration(player.getAcceleration()+1);
		} else if (movePlayerDown == true) {
			player.moveDown();
			player.setAcceleration(player.getAcceleration()+1);
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

		if (firstGame) {
			clearBackground();
			drawInstructions();
			return;
		}

		draw();
		Text temp = new Text("GAME OVER");
		temp.setFont(Font.font(20));
		gc.setFont(Font.font(20));
		// gc.fillText("Control Padle with A + D", Pong.WIDTH / 2 -
		// (temp.getLayoutBounds().getWidth() / 2), Pong.HEIGHT / 2);
		// gc.fillText("PRESS [SPACE] TO START", Pong.WIDTH / 2 -
		// (temp.getLayoutBounds().getWidth() / 2),
		// (Pong.HEIGHT / 2) + 50);

	}

	// Anropar draw-metoder
	private void draw() {
		clearBackground();

		gc.setLineWidth(2);
		gc.setLineDashes(10);
		gc.strokeLine(Pong.WIDTH / 2, 0, Pong.WIDTH / 2, Pong.HEIGHT);
		gc.setLineDashes(0);

		// gc.fill();
		drawBall();
		drawPaddles();
		drawScoreBoard();
	}

	private void clearBackground() {
		gc.setFill(Color.DARKGREEN);
		gc.fillRect(0, 0, 600, 400);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(5);
		gc.strokeRect(0, 0, 600, 400);
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

	private void drawInstructions() {
		clearBackground();

		Text temp = new Text("Instructions");

		gc.setFill(Color.WHITE);

		gc.setFont(new Font(40));
		gc.fillText("Instructions", temp.getLayoutBounds().getWidth(), 50);

		gc.setFont(new Font(30));

		int centerY = (int) (Pong.HEIGHT / 3 + gc.getFont().getSize());
		gc.fillText("Press [SPACE] to Start Game", temp.getLayoutBounds().getWidth(), centerY);
		centerY += gc.getFont().getSize();

		gc.fillText("Press [R] to Reset", temp.getLayoutBounds().getWidth(), centerY);
		centerY += gc.getFont().getSize();

		gc.fillText("Press [P] to Pause", temp.getLayoutBounds().getWidth(), centerY);
		centerY += gc.getFont().getSize();

		gc.fillText("Use 'W' and 'S' to move the paddle", temp.getLayoutBounds().getWidth(), centerY);

	}

	private void drawScoreBoard() {
		/*Text temp = new Text(playerScoreStr);
		temp.setStyle("-fx-font-weight: bold");

		temp.setFont(Font.font(20));*/
		gc.setFont(Font.font("Verdana", 20));
		gc.setFill(Color.WHITE);
		gc.fillText(playerScoreStr, 100, 25);
		// Text computerScore = new Text(computerScoreStr);
		// computerScore.setFont(Font.font(20));
		// gc.setFont(Font.font(20));
		// gc.setFill(Color.WHITE);
		// gc.fillText(computerScoreStr, (Pong.WIDTH - 200), 25);

	}

	private void drawGameOver() {
		clearBackground();

		int centerY = Pong.HEIGHT / 2;

		gc.setFont(Font.font(50));
		gc.setFill(Color.WHITE);
		gc.fillText("Game Over :(", Pong.WIDTH / 4, centerY);

		gc.setFont(Font.font(40));
		gc.fillText("Your Score: " + Integer.toString(playerScore), Pong.WIDTH / 4, centerY + 60);
		
		Text temp = new Text("PRESS [P] TO RESTART");
		temp.setFont(Font.font(20));
		gc.setFont(Font.font(20));
		gc.fillText("PRESS [R] TO RESTART", Pong.WIDTH / 2 - (temp.getLayoutBounds().getWidth() / 2), Pong.HEIGHT / 2 - 100);
		

	}

	private void drawPaused() {
		Text temp = new Text("GAME PAUSED");
		temp.setFont(Font.font(20));
		gc.setFont(Font.font(20));
		gc.fillText("GAME PAUSED", Pong.WIDTH / 2 - (temp.getLayoutBounds().getWidth() / 2), Pong.HEIGHT / 2);
		gc.fillText("PRESS [P] TO UNPAUSE", Pong.WIDTH / 2 -
		(temp.getLayoutBounds().getWidth() / 2),
		(Pong.HEIGHT / 2) + 50);
	}

	// private void drawScore() {
	// Text temp = new Text("SCORE!!");
	// temp.setFont(Font.font(20));
	// gc.setFont(Font.font(20));
	// gc.setFill(Color.FIREBRICK);
	// gc.fillText("SCORE!!", (Pong.WIDTH / 2) - (temp.getLayoutBounds().getWidth()
	// / 2), 50);
	// }

	private void addActionListeners() {
		canvas.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.S || e.getCode() == KeyCode.DOWN) {
				movePlayerDown = true;
			} else if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.UP) {
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

				if (firstGame)
					firstGame = false;

			} else if (e.getCode() == KeyCode.SPACE && !started) {
				if (firstGame)
					firstGame = false;

				if (gameOver == true) {
					ball.reset();
					gameOver = false;
				}
				this.start();
			} else if (e.getCode() == KeyCode.I && (!started || gameOver)) {
				drawInstructions();
			}
			e.consume();
		});

		canvas.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.S || e.getCode() == KeyCode.DOWN) {
				player.setAcceleration(0);
				movePlayerDown = false;
			} else if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.UP) {
				player.setAcceleration(0);
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
			drawInstructions();
			started = false;
			gameOver = false;
		}
	}

	private void gameOver() {
		this.stop();
		player.reset(10);
		computer.reset(Pong.WIDTH - 20);
		drawGameOver();
		controller.newHighscore("Pong", playerScore);
		playerScore = 0;
		gameOver = true;
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
		//	ball.setAcceleration(player.getAcceleration()/2);
		} else if (ball.getYpos() <= 0 && ball.getDy() < 0 || ball.getYpos() + ball.getRadius() >= Pong.HEIGHT) {
			ball.bounceWall();
		} else if (ball.getXpos() <= 0) {
			if (gameOver == false) {
				gameOver();
				gameOver = true;
			}
			ball.reset();
		} else if (ball.getXpos() >= Pong.WIDTH) {
			executor.execute(this);
			// scored = true;
			ball.reset();
		}
	}

	public void run() {
		for (int i = 0; i < 25; i++) {
			playerScore += 2;
			jukeBox2.playMP3(JukeBox2.POINTSBLIP);
			playerScoreStr = "Score: " + Integer.toString(playerScore);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// public void endGame() {
	// this.stop();
	// player.reset(10);
	// computer.reset(Pong.WIDTH - 20);
	// drawGameOver();
	// playerScore = 0;
	// computerScore = 0;
	// gameOver = true;
	// }

}
