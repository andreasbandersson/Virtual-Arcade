package pong;

import application.JukeBox2;
import chat.ClientController;
import javafx.animation.AnimationTimer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
	@author Gulcin Kanat & Måns Grundberg
*/
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
	private Executor executor = Executors.newFixedThreadPool(2);
	private ClientController controller;
	private int gameLevel = 1;
	private boolean levelUp = false;
	private long lastTime = System.currentTimeMillis();

	private Color[] bgColors = {
			Color.rgb(69, 153, 55),
			Color.rgb(153, 143, 55),
			Color.rgb(153, 63, 55),
			Color.rgb(55, 100, 153),
			Color.rgb(193, 62, 92)
	};

	private int colorIndex = 0;

	// private Image pongBg;

	/**
		Game constructor initalizes clientController,
		calls init() & drawStart() methods.
	**/
	public Game(ClientController controller) {
		this.controller = controller;
		init();
		drawStart();
	}

	/**
		Returns current canvas object
		@return Canvas
	**/
	public Canvas getCanvas() {
		return this.canvas;
	}

	/**
		Initializes playerscore, canvas, GraphicsContext2D,
		ball, player, computer & adds Action Listeners
	**/
	private void init() {
		playerScoreStr = "SCORE: 0";
		canvas = new Canvas(Pong.WIDTH, Pong.HEIGHT);
		canvas.setId("Pong");
		gc = canvas.getGraphicsContext2D();
		ball = new Ball();
		player = new Paddle(10, 5, 60);
		computer = new Paddle(Pong.WIDTH - 20, 3, 60);
		addActionListeners();
	}

	/**
		Game Loop
	**/
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

	/**
		Checks if paddle movements were (requested?)
	**/
	private void checkPaddles() {
		if (movePlayerUp) {
			player.moveUp();
			player.setAcceleration(player.getAcceleration()+1);
		} else if (movePlayerDown) {
			player.moveDown();
			player.setAcceleration(player.getAcceleration()+1);
		}
		if (moveCompUp) {
			computer.moveUp();
			moveCompUp = false;
		} else if (moveCompDown) {
			moveCompDown = false;
			computer.moveDown();
		}
	}

	/**
		Paints start screen if firstGame set to true, otherwise Game Over will be painted.
	**/
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
	}

	/**
		Call draw methods
	**/
	private void draw() {
		clearBackground();

		gc.setLineWidth(2);
		gc.strokeLine(Pong.WIDTH / 2, 0, Pong.WIDTH / 2, Pong.HEIGHT);

		drawBall();
		drawPaddles();
		drawScoreBoard();
	}

	/**
		Render canvas background with a color picked from bgColors array
	**/
	private void clearBackground() {
		gc.setFill(this.bgColors[this.colorIndex % this.bgColors.length]);
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

	/**
		drawInstructions() is called by drawStart() method the first time game starts.
		Displays which keys to use for specific actions.
	**/
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
		gc.setFont(Font.font("Verdana", 20));
		gc.setFill(Color.WHITE);
		gc.fillText(playerScoreStr, 20, 25);
	}

	private void drawGameOver() {
		clearBackground();

		int centerY = Pong.HEIGHT / 2;

		gc.setFont(Font.font(50));
		gc.setFill(Color.WHITE);
		gc.fillText("Game Over :(", Pong.WIDTH / 4, centerY);

		gc.setFont(Font.font(40));
		gc.fillText("Your Score: " + playerScore, Pong.WIDTH / 4, centerY + 60);

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
		playerScoreStr = "SCORE: 0";
		gameLevel = 1;
		colorIndex = 0;
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
			ball.changeDirection(this.gameLevel);
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
			ball.reset();

		}
	}

	public void run() {
		for (int i = 0; i < 25; i++) {
			playerScore += 2;
			JukeBox2.playMP3(JukeBox2.POINTSBLIP);
			playerScoreStr = "Score: " + Integer.toString(playerScore);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(this.playerScore % 150 == 0 && this.playerScore > 0) {

			// Increase Level & bgColor index
			this.colorIndex++;
			this.gameLevel++;

			// Show level
			displayLevel();

			// reset ball
			ball.reset();

		}

		lastTime = System.currentTimeMillis();
	}

	/**
		Each time player earns +150 points, gameLevel is updated and displayed.
	**/
	public void displayLevel() {

		clearBackground();

		gc.setFont(new Font(40));
		gc.setFill(Color.WHITE);
		gc.fillText("LEVEL: " + gameLevel, Pong.WIDTH/2 - 50, Pong.HEIGHT/2);

		// stop animation timer
		this.stop();

		// save a reference of current AnimationTimer
		AnimationTimer self = this;

		// set timertask to be executed after 2s
		// starts AnimationTimer again.
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				self.start();
			}
		}, 2000);


	}

}
