package snake;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import application.JukeBox;
import application.JukeBox2;
import application.MainUI;
import chat.ClientController;
import chat.ChatUI;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Class that draws the GUI and contains the logic for the game.
 * @author Max Matthiasson
 */
public class GUIPane implements Runnable {

	private Canvas canvas;
	
	// Different gameStates that change what happens during the game. 
	private static final int MENU_STATE = 1;
	private static final int INGAME_STATE = 2;
	private static final int GAME_OVER_STATE = 3;
	private static final int INSTRUCTIONS_STATE = 4;
	
	// Keeps track of which state the program is in (MENU_STATE, INSTRUCTIONS_STATE,
	// INGAME_STATE or GAME_OVER_STATE).
	private int gameState = MENU_STATE;

	// Int variables representing the direction the snake is going in.
	private static final int UP = 1;
	private static final int DOWN = 2;
	private static final int LEFT = 3;
	private static final int RIGHT = 4;

	private static final int GAME_WIDTH = 600; // Width of the game screen.
	private static final int GAME_HEIGHT = 400; // Height of the game screen.

	private int snakeSize = 5; // Length of the snake.
	private int score = 0; // Keeps track of the score.

	private int unitWidth = 15; // Width of the units in the game. (The snakes body parts, the food and the spider)
	private int unitHeight = 15; // Height of the units in the game. (The snakes body parts, the food and the spider)
	private int snakeX; // The snakes X-position.  
	private int snakeY; // The snakes Y-position.	
	private int foodX; // The foods X-position.	
	private int foodY; // The foods Y-position.
	private int obstacleX; // The spiders X-position.
	private int obstacleY; // The spiders Y-position.
	private int spiderTicks; // Decides how often the spider should move. 
	private int spiderDirection; //	Gets set to a random number between 0 and 3 that decides the spiders direction. 

	// Queue that holds integers representing the direction the snake is traveling
	// in. (UP, DOWN, LEFT, RIGHT).
	private Queue<Integer> movementQueue = new ArrayDeque<Integer>();

	private Random rand = new Random();

	private boolean paused = false; // Keeps track if the game has been paused or not.
	private boolean directionChangeAllowed = true; // Keep track if the snake has changed direction.

	private AnimationTimer gameAnimationTimer; // Timer that the game runs on.

	private BodyPart bodyPart;
	private ArrayList<BodyPart> listSnake; // List of BodyPart objects.

	private Food foodPiece;
	private ArrayList<Food> listFood; // List of Food objects.

	private Obstacle obstacle;
	private ArrayList<Obstacle> listObstacle; // List of Obstacle objects.

	private Scene gameScene;

	private MainUI mainUI;
	private ChatUI chatUI;
	private JukeBox2 jukeBox2 = new JukeBox2();
	private JukeBox jukeBox;
	private Button backButton = new Button("BACK");
	private Button soundButton = new Button();
	private GraphicsContext gc;
	private GridPane snakePane = new GridPane();
	private Image muteSoundImage;
	private Image playSoundImage;
	private Image snakeImage;
	private ImageView snakeView;
	private ImageView muteSoundImageView;
	private ImageView playSoundImageView;

	private static Image snakeCharacterImage;
	private static Image foodImage;
	private static Image obstacleImage;
	
	private Executor executor = Executors.newFixedThreadPool(2); // Thread pool used to play a sound multiple times when getting points. 

	private ClientController controller;

	static {
		try {
			snakeCharacterImage = new Image(new FileInputStream("images/SnakePieceV8.png"));
			foodImage = new Image(new FileInputStream("images/FoodPieceV2.png"));
			obstacleImage = new Image(new FileInputStream("images/PixelArtSpider.png"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public GUIPane(MainUI mainUI, ChatUI chatUI, JukeBox jukeBox, ClientController controller) {
		this.mainUI = mainUI;
		this.chatUI = chatUI;
		this.jukeBox = jukeBox;
		this.controller = controller;
		init();
	}

	public void init() {
		listSnake = new ArrayList<BodyPart>();
		listFood = new ArrayList<Food>();
		listObstacle = new ArrayList<Obstacle>();
		
		Pane root;

		canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
		gc = canvas.getGraphicsContext2D();

		root = new Pane();
		root.getChildren().addAll(canvas);
		root.setId("SnakeCanvas");

		createColumnsandRows();
		setSoundButtonImages();
		checkSound();
		setSnakeArcadeMachineImage();

		// Adding and setting the main buttons
		backButton.setId("greyButton");
		snakePane.add(backButton, 1, 21, 6, 2);

		// Adding an setting the sound buttons
		soundButton = new Button();
		soundButton.setId("greyButton");
		snakePane.add(soundButton, 32, 1);

		snakePane.add(root, 6, 4, 24, 16);

		gameScene = new Scene(snakePane, 1200, 600, Color.BLACK);
		snakePane.setId("mainRoot");
		
		//Sets the style of the scene
		try {
			gameScene.getStylesheets().add((new File("styles//snakeStyle.css")).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		backButton.setFocusTraversable(false);
		soundButton.setFocusTraversable(false);

		createAnimationTimer();
		addActionListeners();
		canvas.setOnMouseMoved(e -> canvas.requestFocus());
		movementQueue.add(RIGHT);
		gameState = MENU_STATE;
		drawShapes(gc);
		canvas.requestFocus();
		root.requestFocus();
	}

	/**
	 * Creates an AnimationTimer.
	 */
	private void createAnimationTimer() {
		gameAnimationTimer = new AnimationTimer() {
			private long lastUpdate = 0;
			private long updateSpeed = 75_000_000;

			@Override
			public void handle(long now) {
				// if-statement that happens every X amount of nanoseconds. (75_000_000)
				if (now - lastUpdate >= updateSpeed) {

					// Switch statement checking what int value (UP, DOWN, LEFT, RIGHT) is in the queue and changing the
					// snakes X or Y value accordingly.
					switch (movementQueue.peek()) {
						case UP:
							snakeY = snakeY - 15;
							break;
						case DOWN:
							snakeY = snakeY + 15;
							break;
						case LEFT:
							snakeX = snakeX - 15;
							break;
						case RIGHT:
							snakeX = snakeX + 15;
							break;
					}

					directionChangeAllowed = true; // Allows the user to change direction of the snake.

					spiderTicks++;

					// Moves the spider every updateSpeed * 5 (375_000_000 nanoseconds).
					if (spiderTicks >= 5) {

						spiderDirection = rand.nextInt(4);

						// If-statements making it so the spider doesn't stay at a wall.
						if (obstacleX >= 570) {
							spiderDirection = 1;
						}

						if (obstacleX <= 15) {
							spiderDirection = 0;
						}

						if (obstacleY >= 365) {
							spiderDirection = 3;
						}

						if (obstacleY <= 45) {
							spiderDirection = 2;
						}

						// Switch-statement moving the spider depending on the value of
						// spiderDirection which is random.
						switch (spiderDirection) {
							case 0:
								if (obstacleX < 570) {
									obstacleX = obstacleX + 15;
								}
								break;
							case 1:
								if (obstacleX > 15) {
									obstacleX = obstacleX - 15;
								}
								break;
							case 2:
								if (obstacleY < 379) {
									obstacleY = obstacleY + 15;
								}
								break;
							case 3:
								if (obstacleY > 45) {
									obstacleY = obstacleY - 15;
								}
								break;
						}

						spiderTicks = 0;
					}

					// Creates a new Obstacle object and adds it to the ArrayList of obstacles.
					obstacle = new Obstacle(obstacleImage, obstacleX, obstacleY, unitWidth, unitHeight);
					listObstacle.add(obstacle);

					// Creates a new BodyPart object and adds it to the ArrayList of bodyparts.
					bodyPart = new BodyPart(snakeCharacterImage, snakeX, snakeY, unitWidth, unitHeight);
					listSnake.add(bodyPart);

					// Removes the snakes trail.
					if (listSnake.size() > snakeSize) {
						listSnake.remove(0);
					}

					// Removes the spiders trail.
					if (listObstacle.size() > 0) {
						listObstacle.remove(0);
					}

					lastUpdate = now;
				}

				drawShapes(gc);

				// Collision code for the snake colliding with itself.
				for (int i = 0; i < listSnake.size(); i++) {
					if (snakeX == listSnake.get(i).getSnakeX() && snakeY == listSnake.get(i).getSnakeY()) {
						if (i != listSnake.size() - 1) {
							gameOver();
							gameAnimationTimer.stop();
							jukeBox2.playMP3(JukeBox2.SNAKEHIT2);
							drawShapes(gc);
						}
					}
				}
				// Collision for the snake colliding with the edges of the screen. 
				if (snakeX > 570 || snakeX < 15 || snakeY >= 380 || snakeY < 45) {
					gameOver();
					gameAnimationTimer.stop();
					jukeBox2.playMP3(JukeBox2.SNAKEHIT2);
					drawShapes(gc);
				}
				// Collision for the snake eating the food.
				for (int i = 0; i < listFood.size(); i++) {
					if (snakeX == listFood.get(i).getFoodX() && snakeY == listFood.get(i).getFoodY()) {
						listFood.remove(i);
						snakeSize++;
						activateThreadPool();
					}
				}
				// Collision for the spider eating the food.
				for (int i = 0; i < listFood.size(); i++) {
					if (obstacleX == listFood.get(i).getFoodX() && obstacleY == listFood.get(i).getFoodY()) {
						listFood.remove(i);
						System.out.println("The spider ate the food!");
					}
				}
				// Spawns the food.
				if (listFood.size() == 0) {
					foodX = rand.nextInt(30) * 15;
					foodY = rand.nextInt(20) * 15;
					 
					foodPiece = new Food(foodImage, foodX, foodY, unitWidth, unitHeight);
					listFood.add(foodPiece);

					// Removes the food if it spawns outside the rectangle representing the game screen.
					if (foodX >= 570 || foodX <= 15 || foodY >= 380 || foodY <= 45) {
						listFood.remove(0);
					}
				}
				// Spawns the obstacle.
				if (listObstacle.size() == 0) {
					obstacleX = 285;
					obstacleY = 150;

					obstacle = new Obstacle(obstacleImage, obstacleX, obstacleY, unitWidth, unitHeight);
					listObstacle.add(obstacle);

					// Removes the food if it spawns outside the rectangle representing the game screen.
					if (obstacleX >= 570 || obstacleX <= 15 || obstacleY >= 380 || obstacleY <= 45) {
						listObstacle.remove(0);
					}
				}
				// Collision for the snake colliding with an obstacle.
				for (int i = 0; i < listObstacle.size(); i++) {
					if (snakeX == listObstacle.get(i).getObstacleX() && snakeY == listObstacle.get(i).getObstacleY()) {
						listObstacle.remove(i);
						score = score - 50;
						jukeBox2.playMP3(JukeBox2.SNAKEHIT);

						if (score < 0) {
							score = 0;
						}
					}
				}
			}
		};
	}
	
	/**
	 * Activates the Thread pool. 
	 */
	public void activateThreadPool() {
		executor.execute(this);
	}

	/**
	 * Creates ActionListeners for keys.
	 */
	private void addActionListeners() {
		// Pressing the backButton brings you back to the main menu.
		backButton.setOnAction(e -> {
			snakePane.getChildren().remove(chatUI);
			gameAnimationTimer.stop();
			mainUI.switchToMainUI();
			if (gameState == GAME_OVER_STATE) {
				gameState = MENU_STATE;
				drawShapes(gc);
			}
		});
		// Pressing the soundButton will pause or play the game sound.
		// Andreas Andersson
		soundButton.setOnAction(e -> {
			jukeBox.pauseOrPlay();
			if (jukeBox.isPaused()) {
				soundButton.setGraphic(muteSoundImageView);
			} else {
					soundButton.setGraphic(playSoundImageView);
			}
		});
		
		// Keeps track of all the key presses in the gameScene.
		gameScene.setOnKeyPressed(e -> {
			// Pressing W will change the snakes direction to UP.
			if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.UP) {
				if (movementQueue.peek() != DOWN && directionChangeAllowed) {
					directionChangeAllowed = false;
					movementQueue.remove();
					movementQueue.add(UP);

				}
			}
			// Pressing S will change the snakes direction to DOWN.
			if (e.getCode() == KeyCode.S || e.getCode() == KeyCode.DOWN) {
				if (movementQueue.peek() != UP && directionChangeAllowed) {
					directionChangeAllowed = false;
					movementQueue.remove();
					movementQueue.add(DOWN);
				}
			}
			// Pressing A will change the snakes direction to LEFT.
			if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) {
				if (movementQueue.peek() != RIGHT && directionChangeAllowed) {
					directionChangeAllowed = false;
					movementQueue.remove();
					movementQueue.add(LEFT);
				}
			}
			// Pressing D will change the snakes direction to RIGHT.
			if (e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) {
				if (movementQueue.peek() != LEFT && directionChangeAllowed) {
					directionChangeAllowed = false;
					movementQueue.remove();
					movementQueue.add(RIGHT);
				}
			}
			// Pressing P pauses the game.
			if (e.getCode() == KeyCode.P) {
				if (gameState == INGAME_STATE) {
					pauseGame();
					drawShapes(gc);
				}
			}
			// Pressing R after you've lost restarts the game.
			if (e.getCode() == KeyCode.R) {
				if (gameState == GAME_OVER_STATE) {
					startGame();
				}
			}
			// Pressing Enter while in the menu screen starts the game.
			if (e.getCode() == KeyCode.ENTER) {
				if (gameState == MENU_STATE) {
					startGame();
				}
			}
			// Pressing I in the menu will show the instructions.
			if (e.getCode() == KeyCode.I) {
				if (gameState == MENU_STATE) {
					gameState = INSTRUCTIONS_STATE;
					drawShapes(gc);
				}
			}
			// Pressing backspace will return you to the main menu if you're not in the game.
			if (e.getCode() == KeyCode.BACK_SPACE) {
				if (gameState == INSTRUCTIONS_STATE) {
					gameState = MENU_STATE;
					drawShapes(gc);
				}
				if (gameState == GAME_OVER_STATE) {
					gameState = MENU_STATE;
					drawShapes(gc);
				}
			}
			e.consume();
		});
	}
	/**
	 * Run method for increasing the score and playing a sound.
	 */
	public void run() {

		for (int i = 0; i < 10; i++) {
			score++;
			jukeBox2.playMP3(JukeBox2.POINTSBLIP);

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Scene getScene() {
		snakePane.add(chatUI, 36, 0, 12, 24);
		chatUI.setFocusTraversable(false);
		return gameScene;
	}

	/**
	 * Resets the game.
	 */
	private void startGame() {
		gameState = INGAME_STATE;
		score = 0;
		snakeX = 15;
		snakeY = 60;
		foodX = rand.nextInt((30) * 15);
		foodY = rand.nextInt((20) * 15);
		obstacleX = 285;
		obstacleY = 150;
		snakeSize = 5;
		listObstacle.clear();
		listFood.clear();
		listSnake.clear();
		movementQueue.remove();
		movementQueue.add(RIGHT);
		gameAnimationTimer.start();
	}

	/**
	 * Changes the gameState to GAME_OVER_STATE
	 */
	private void gameOver() {
		gameState = GAME_OVER_STATE;
		jukeBox2.playMP3(JukeBox2.GAMEOVER);
		controller.newHighscore("Snake", this.score);
	}

	/**
	 * Pauses the game.
	 */
	private void pauseGame() {
		if (paused) {
			paused = false;
			gameAnimationTimer.start();
		} else if (!paused) {
			paused = true;
			gameAnimationTimer.stop();
			drawShapes(gc);
		}
	}

	/**
	 * Method used to draw. It draws different things depending on the gameState.
	 * @param gc
	 */
	public void drawShapes(GraphicsContext gc) {

		if (gameState == MENU_STATE) {
			gc.clearRect(0, 0, 600, 400);

			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 80));
			gc.setFill(Color.BLACK);
			gc.fillText("Snake", 190, 80);

			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 25));
			gc.fillText("[Enter] to start", 188, 180);
			gc.fillText("[I] for instructions", 168, 230);
		}

		if (gameState == INSTRUCTIONS_STATE) {
			gc.clearRect(0, 0, 600, 400);

			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 50));
			gc.setFill(Color.BLACK);
			gc.fillText("Instructions", 130, 60);
			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 20));
			gc.fillText("The goal of the game is to eat as much food as", 20, 100);
			gc.fillText("possible in order to get as many points as possible.", 20, 125);
			gc.fillText("Watch out for spiders, they reduce your points.", 20, 150);

			gc.fillText("Control keys:", 20, 190);
			gc.strokeLine(20, 195, 165, 195);
			gc.fillText("[W] or [UP arrow] = Go UP", 20, 220);
			gc.fillText("[A] or [LEFT arrow]= Go LEFT", 20, 250);
			gc.fillText("[S] or [DOWN arrow] = GO DOWN", 20, 280);
			gc.fillText("[D] or [RIGHT arrow] = Go RIGHT", 20, 310);
			gc.fillText("[P] = Pauses the game", 20, 340);
			gc.fillText("Press [Backspace] to return to the main menu", 20, 380);
		}

		if (gameState == INGAME_STATE) {
			gc.clearRect(0, 0, 600, 400);

			// Draws the the rectangle representing the game screen. 
			gc.setStroke(Color.BLACK);
			gc.strokeRect(15, 45, 570, 345);

			// Draws the snake.
			for (int i = 0; i < listSnake.size(); i++) {
				listSnake.get(i).drawSnakePane(gc);
			}
			// Draws the food.
			for (int i = 0; i < listFood.size(); i++) {
				listFood.get(i).drawFoodPane(gc);
			}
			// Draws the obstacles.
			for (int i = 0; i < listObstacle.size(); i++) {
				listObstacle.get(i).drawObstacle(gc);
			}
			// Draws the score.
			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 30));
			gc.setFill(Color.BLACK);
			if (score < 10) {
				gc.fillText("000" + score, 15, 40);
			}
			if (score >= 10 && score < 100) {
				gc.fillText("00" + score, 15, 40);
			}
			if (score >= 100 && score < 1000) {
				gc.fillText("0" + score, 15, 40);
			}
			if (score >= 1000) {
				gc.fillText("" + score, 15, 40);
			}

			if (paused) {
				gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 60));
				gc.setFill(Color.WHITE);
				gc.fillText("Paused", 210, 200);
				gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 30));
				;
				gc.fillText("Press P to resume", 165, 240);
			}
		}

		if (gameState == GAME_OVER_STATE) {
			gc.clearRect(0, 0, 600, 400);

			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 60));
			gc.setFill(Color.BLACK);
			gc.fillText("GAME OVER", 150, 150);

			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 30));
			gc.setFill(Color.BLACK);
			gc.fillText("Score:" + score, 225, 200);

			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 20));
			gc.setFill(Color.BLACK);
			gc.fillText("Press R to restart", 200, 340);
			gc.fillText("Press Backspace to return to the main menu", 60, 380);
		}
	}
	
	/**
	 * Sets the number and size-percentage of the rows and columns in the GridPane.
	 * @author Andreas Andersson
	 */
	private void createColumnsandRows() {

		final int numOfCols = 48;
		final int numOfRows = 24;

		for (int i = 0; i < numOfCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(100.0 / numOfCols);
			snakePane.getColumnConstraints().add(colConst);
		}
		for (int i = 0; i < numOfRows; i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setPercentHeight(100.0 / numOfRows);
			snakePane.getRowConstraints().add(rowConst);
		}
	}

	/**
	 *  Sets the sound buttons images.
	 *  @author Andreas Andersson
	 */
	private void setSoundButtonImages() {
		try {
			playSoundImage = new Image(new FileInputStream("images/sound.png"));
			muteSoundImage = new Image(new FileInputStream("images/mute.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		playSoundImageView = new ImageView(playSoundImage);
		muteSoundImageView = new ImageView(muteSoundImage);
	}

	/**
	 *  Checks if sound is paused or playing and sets image accordingly.
	 *  @author Andreas Andersson
	 */
	public void checkSound() {
		if (jukeBox.isPaused()) {
			soundButton.setGraphic(muteSoundImageView);
		} else {
			soundButton.setGraphic(playSoundImageView);
		}
	}

	/**
	 *  Sets and adds the arcade machine image for the SpaceInvaders game.
	 *  @author Andreas Andersson
	 */
	private void setSnakeArcadeMachineImage() {
		try {
			snakeImage = new Image(new FileInputStream("images/snakeScreen.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		snakeView = new ImageView(snakeImage);
		snakeView.setPreserveRatio(true);
		snakePane.add(snakeView, 0, 14);
	}
}

