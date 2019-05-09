package snake;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

import application.JukeBox;
import application.MainUI;
import chat.ChatUI;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import snake.BodyPart;
import snake.Food;

/**
 * Class that draws the GUI and contains the logic for the game. 
 * @author Max Matthiasson
 */
public class GUIPane {
	
	private Pane root;
	private Canvas canvas;

	private VBox buttonLayout = new VBox(30);

	private Button startButton = new Button("Start game");
	private Button instructionsButton = new Button("Instructions");

	private static final int MENU_STATE = 1;
	private static final int INGAME_STATE = 2;
	private static final int GAME_OVER_STATE = 3;

	private static final int UP = 1;
	private static final int DOWN = 2;
	private static final int LEFT = 3;
	private static final int RIGHT = 4;

	private static final int GAME_WIDTH = 600; // Width of the game screen.
	private static final int GAME_HEIGHT = 400; // Height of the game screen.

	private int snakeSize = 5; // Length of the snake.
	private int score = 0; // Keeps track of the score.
	public int unitWidth = 20; // Width of the units in the game. (The snakes body parts and the food)
	public int unitHeight = 20; // Height of the units in the game. (The snakes body parts and the food)
	public int snakeX = 0; // The snakes X-position. 
	public int snakeY = 0; // The snakes Y-position.
	public int foodX = 0; // The foods X-position.
	public int foodY = 0; // The foods Y-position.
	private int gameState = MENU_STATE; // Keeps track of which state the program is in (MENU_STATE, INGAME_STATE or GAME_OVER_STATE).

	//Queue that holds integers representing the direction the snake is traveling in. (UP, DOWN, LEFT, RIGHT).
	private Queue<Integer> movementQueue = new ArrayDeque<Integer>(); 

	private Random r = new Random();

	private boolean paused = false; //Keeps track if the game has been paused or not. 

	private AnimationTimer gameAnimationTimer; //Timer that the game runs on. 

	private BodyPart bodyPart;
	private ArrayList<BodyPart> listSnake; // List of BodyPart objects.

	private Food foodPiece;
	private ArrayList<Food> listFood; // List of Food objects.
	
	private Scene gameScene;

	private MainUI mainUI;
	private ChatUI chatUI;
	private JukeBox jukebox;
	private Button backButton = new Button("BACK");
	private Button soundButton = new Button();
	private GraphicsContext gc;
	private final int numOfCols = 48;
	private final int numOfRows = 24;
	private GridPane snakePane = new GridPane();
	private Image muteSoundImage;
	private Image playSoundImage;
	private ImageView muteSoundImageView;
	private ImageView playSoundImageView;

	public GUIPane(MainUI mainUI, ChatUI chatUI, JukeBox jukebox) {
		this.mainUI = mainUI;
		this.chatUI = chatUI;
		this.jukebox = jukebox;
		init();
	}

	public void init() {
		listSnake = new ArrayList<BodyPart>();
		listFood = new ArrayList<Food>();

		canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
		gc = canvas.getGraphicsContext2D();

		startButton.setFont(Font.loadFont("file:fonts/lunchds.ttf", 18));
		startButton.setPrefSize(150, 60);

		instructionsButton.setFont(Font.loadFont("file:fonts/lunchds.ttf", 18));
		instructionsButton.setPrefSize(150, 60);

		buttonLayout.getChildren().addAll(startButton, instructionsButton);
		buttonLayout.setAlignment(Pos.CENTER);
		buttonLayout.setLayoutX(225);
		buttonLayout.setLayoutY(140);

		root = new Pane();
		root.getChildren().addAll(canvas, buttonLayout);
		root.setId("SnakeCanvas");

		createColumnsandRows();
		setSoundButtonImages();

		// Adding and setting the main buttons
		backButton.setId("logOutButton");
		snakePane.add(backButton, 1, 21, 6, 2);

		// Adding an setting the button for mute and un-mute of login music
		soundButton = new Button();
		soundButton.setId("logOutButton");
		soundButton.setGraphic(playSoundImageView);
		snakePane.add(soundButton, 32, 1);

		snakePane.add(root, 6, 4, 24, 16);

		gameScene = new Scene(snakePane, 1200, 600, Color.BLACK);
		snakePane.setId("mainRoot");

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
	 * Method that creates an AnimationTimer containing all the things that continuously happen during the game.
	 */
	private void createAnimationTimer() {
		gameAnimationTimer = new AnimationTimer() {
			private long lastUpdate = 0;

			@Override
			public void handle(long now) {
				// if-statement that happens every X amount of nanoseconds. (75_000_000)
				if (now - lastUpdate >= 75_000_000) {
					
					//Switch statement checking what int value is in the queue and changing the snakes X or Y value accordingly. 
					switch (movementQueue.peek()) {
					case UP:
						snakeY = snakeY - 20;
						break;
					case DOWN:
						snakeY = snakeY + 20;
						break;
					case LEFT:
						snakeX = snakeX - 20;
						break;
					case RIGHT:
						snakeX = snakeX + 20;
						break;
					}

					bodyPart = new BodyPart(snakeX, snakeY, unitWidth, unitHeight);
					listSnake.add(bodyPart);

					// Removes the snakes trail.
					if (listSnake.size() > snakeSize) {
						listSnake.remove(0);
					}

					lastUpdate = now;
				}

				drawShapes(gc);

				// Collision code for the snake.
				for (int i = 0; i < listSnake.size(); i++) {
					if (snakeX == listSnake.get(i).getSnakeX() && snakeY == listSnake.get(i).getSnakeY()) {
						if (i != listSnake.size() - 1) {
							gameState = GAME_OVER_STATE;
							gameAnimationTimer.stop();
							System.out.println("Collision with snake");
							drawShapes(gc);
						}
					}
				}
				// Collision for the edges of the screen.
				if(snakeX >= 580 || snakeX < 20 || snakeY >= 380 || snakeY < 40){
					gameState = GAME_OVER_STATE;
					gameAnimationTimer.stop();
					System.out.println("Collision with the edge of the screen");
					drawShapes(gc);
				}
				// Collision for the food.
				for (int i = 0; i < listFood.size(); i++) {
					if (snakeX == listFood.get(i).getFoodX() && snakeY == listFood.get(i).getFoodY()) {
						listFood.remove(i);
						snakeSize++;
						score = score + 10;
						i++;
					}
				}
    			// Spawns the food. 
    			if(listFood.size() == 0)
    			{
    				int foodX = r.nextInt(30) * 20;
    				int foodY = r.nextInt(20) * 20;
    				foodPiece = new Food(foodX, foodY, unitWidth, unitHeight);
    				listFood.add(foodPiece);
    				
        			// Removes the food if it spawns outside the rectangle representing the game screen. 
    				if(foodX >= 560 || foodX <= 20 || foodY >= 340 || foodY <= 40)
    				{
    					listFood.remove(0);
    				}
    			}
			}
		};

	}
	/**
	 * Method that creates ActionListeners for buttons and keys. 
	 */
	private void addActionListeners() {
		// Pressing the startButton starts the game. 
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				startGame();
				canvas.requestFocus();
				buttonLayout.getChildren().remove(startButton);
				buttonLayout.getChildren().remove(instructionsButton);
			}
		});
		// Pressing the backButton brings you back to the main menu. 
		backButton.setOnAction(e -> {
			snakePane.getChildren().remove(chatUI);
			gameAnimationTimer.stop();
			mainUI.switchToMainUI();
		});
		// Pressing the soundButton will mute or unmute the game sound. 
		soundButton.setOnAction(e -> {
			jukebox.muteUnmute();
			if (jukebox.isMute()) {
				soundButton.setGraphic(playSoundImageView);
			} else {
				soundButton.setGraphic(muteSoundImageView);
			}
		});
		// Keeps track of all the key presses in the gameScene. 
		gameScene.setOnKeyPressed(e -> {
			// Pressing W will change the snakes direction to UP.
			if (e.getCode() == KeyCode.W) {
				if (movementQueue.peek() != DOWN) {
					movementQueue.remove();
					movementQueue.add(UP);
				}
			}
			// Pressing S will change the snakes direction to DOWN.
			if (e.getCode() == KeyCode.S) {
				if (movementQueue.peek() != UP) {
					movementQueue.remove();
					movementQueue.add(DOWN);
				}
			}
			// Pressing A will change the snakes direction to LEFT.
			if (e.getCode() == KeyCode.A) {
				if (movementQueue.peek() != RIGHT) {
					movementQueue.remove();
					movementQueue.add(LEFT);
				}
			}
			// Pressing D will change the snakes direction to RIGHT.
			if (e.getCode() == KeyCode.D) {
				if (movementQueue.peek() != LEFT) {
					movementQueue.remove();
					movementQueue.add(RIGHT);
				}
			}
			// Pressing P pauses the game.
			if (e.getCode() == KeyCode.P) {
				pauseGame();
			}
			// Pressing R after you've lost restarts the game.
			if (e.getCode() == KeyCode.R) {
				if (gameState == GAME_OVER_STATE) {
					startGame();
				}
			}
		});
	}

	public Scene getScene() {
		snakePane.add(chatUI, 36, 0, 12, 24);
		chatUI.setFocusTraversable(false);
		// canvas.requestFocus();
		return gameScene;
	}
	
	/**
	 * Method that resets the game. 
	 */
	private void startGame() {
		gameState = INGAME_STATE;
		score = 0;
		snakeX = 20;
		snakeY = 60;
		foodX = r.nextInt((30) * 20);
		foodY = r.nextInt((20) * 20);
		snakeSize = 5;
		listFood.clear();
		listSnake.clear();
		movementQueue.remove();
		movementQueue.add(RIGHT);
		gameAnimationTimer.start();
	}

	/**
	 * Method that pauses the game.
	 */
	private void pauseGame() {
		if (paused) {
			paused = false;
			gameAnimationTimer.start();
		} else if (paused == false) {
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
			gc.fillText("Snake", 200, 90);
		}

		if (gameState == INGAME_STATE) {
			gc.clearRect(0, 0, 600, 400);
			
			// Draws the rectangle representing the game screen. 
			gc.setStroke(Color.BLACK);
			gc.strokeRect(20, 40, 560, 340);

			// Draws the snake.
			for (int i = 0; i < listSnake.size(); i++) {
				listSnake.get(i).drawSnakePane(gc);
			}
			// Draws the food.
			for (int i = 0; i < listFood.size(); i++) {
				listFood.get(i).drawFoodPane(gc);
			}
			
			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 20));
			gc.setFill(Color.BLACK);
			gc.fillText("Score: " + score, 20, 35);
			
			if (paused) {
				gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 60));
				gc.setFill(Color.WHITE);
				gc.fillText("Paused", 210, 200);
				gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 30));;
				gc.fillText("Press P to resume", 165, 240);
			}
		}

		if (gameState == GAME_OVER_STATE) {
			gc.clearRect(0, 0, 600, 400);
			
			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 60));
			gc.setFill(Color.BLACK);
			gc.fillText("GAME OVER", 160, 150);
			
			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 30));
			gc.setFill(Color.BLACK);
			gc.fillText("Score: " + score, 225, 200);
			
			gc.setFont(Font.loadFont("file:fonts/lunchds.ttf", 20));
			gc.setFill(Color.BLACK);
			gc.fillText("Press R to restart", 200, 380);

		}
	}

	private void createColumnsandRows() {

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

	// Sets the sound buttons images.
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
}
