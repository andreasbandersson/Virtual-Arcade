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

public class GUIPane {
	private Pane root;
	private Canvas canvas;

	private Color customLawnGreen = Color.rgb(124, 252, 0);

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
	public int unitWidth = 20;
	public int unitHeight = 20;
	public int snakeX = 0;
	public int snakeY = 0;
	public int foodX = 0;
	public int foodY = 0;
	private int gameState = 1; // Keeps track of which state the program is in (MENUSTATE or INGAMESTATE).

	private Queue<Integer> movementQueue = new ArrayDeque<Integer>();

	private Random r = new Random();

	private boolean paused = false;

	private AnimationTimer gameAnimationTimer;

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

		startButton.setFont(Font.font("Verdana", 14));
		startButton.setPrefSize(150, 60);

		instructionsButton.setFont(Font.font("Verdana", 14));
		instructionsButton.setPrefSize(150, 60);

		buttonLayout.getChildren().addAll(startButton, instructionsButton);
		buttonLayout.setAlignment(Pos.CENTER);
		buttonLayout.setLayoutX(225);
		buttonLayout.setLayoutY(120);

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

	private void createAnimationTimer() {
		gameAnimationTimer = new AnimationTimer() {
			private long lastUpdate = 0;
			// private long updateSpeed = 75_000_000;

			@Override
			public void handle(long now) {
				// if-statement that happens every X amount of nanoseconds.
				if (now - lastUpdate >= 50_000_000) {
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
				if (snakeX > 600 || snakeX < 0 || snakeY > 400 || snakeY < 0) {
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
						score++;
						i++;
					}
				}
				// Spawns the food.
				if (listFood.size() == 0) {
					int foodPosX = r.nextInt(30) * 20;
					int foodPosY = r.nextInt(20) * 20;
					foodPiece = new Food(foodPosX, foodPosY, unitWidth, unitHeight);
					listFood.add(foodPiece);
				}
			}
		};

	}

	private void addActionListeners() {
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Game started");
				startGame();
				canvas.requestFocus();
				buttonLayout.getChildren().remove(startButton);
				buttonLayout.getChildren().remove(instructionsButton);
			}
		});

		backButton.setOnAction(e -> {
			snakePane.getChildren().remove(chatUI);
			gameAnimationTimer.stop();
			mainUI.switchToMainUI();
		});

		soundButton.setOnAction(e -> {
			jukebox.muteUnmute();
			if (jukebox.isMute()) {
				soundButton.setGraphic(playSoundImageView);
			} else {
				soundButton.setGraphic(muteSoundImageView);
			}
		});

		gameScene.setOnKeyPressed(e -> {
			// Pressing the up arrow key or W will change the snakes direction to UP.
			if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W) {
				// Thread.sleep fixar problemet att man kan �ka in i sig sj�lv om man trycker p�
				// knapparna tillr�ckligt snabbt.
				// Fast det g�r att spelet blir lite mindre "smooth".
				if (movementQueue.peek() != DOWN) {
					movementQueue.remove();
					movementQueue.add(UP);
				}
			}
			// Pressing the down arrow key or S will change the snakes direction to DOWN.
			if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) {
				if (movementQueue.peek() != UP) {
					movementQueue.remove();
					movementQueue.add(DOWN);
				}
			}
			// Pressing the left arrow key or A will change the snakes direction to LEFT.
			if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) {
				if (movementQueue.peek() != RIGHT) {
					movementQueue.remove();
					movementQueue.add(LEFT);
				}
			}
			// Pressing the right arrow key or D will change the snakes direction to RIGHT.
			if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
				if (movementQueue.peek() != LEFT) {
					movementQueue.remove();
					movementQueue.add(RIGHT);
				}
			}
			// Pressing P pauses the game.
			if (e.getCode() == KeyCode.P) {
				System.out.println("Button P pressed");
				pauseGame();
			}
			// Pressing Enter while in the menu screen starts the game.
			if (e.getCode() == KeyCode.ENTER) {
				if (gameState == MENU_STATE) {
					startGame();
				}
			}
			// Remove
			if (e.getCode() == KeyCode.ESCAPE) {
				if (gameState == GAME_OVER_STATE) {
					System.exit(1);
				}
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

	private void startGame() {
		gameState = INGAME_STATE;
		score = 0;
		snakeX = 0;
		snakeY = 0;
		foodX = r.nextInt(30) * 20;
		foodY = r.nextInt(20) * 20;
		snakeSize = 5;
		listFood.clear();
		listSnake.clear();
		movementQueue.remove();
		movementQueue.add(RIGHT);
		gameAnimationTimer.start();
	}

	// Pauses the game.
	private void pauseGame() {
		if (paused) {
			paused = false;
			gameAnimationTimer.start();
		} else if (paused == false) {
			paused = true;
			gameAnimationTimer.stop();
		}
	}

	// Draws the game.
	public void drawShapes(GraphicsContext gc) {
		if (gameState == MENU_STATE) {
			gc.clearRect(0, 0, 600, 400);

			gc.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
			gc.setFill(Color.BLACK);
			gc.fillText("Snake", 200, 60);
		}

		if (gameState == INGAME_STATE) {
			gc.clearRect(0, 0, 600, 400);

			// Draws the snake.
			for (int i = 0; i < listSnake.size(); i++) {
				listSnake.get(i).drawSnakePane(gc);
			}
			// Draws the food.
			for (int i = 0; i < listFood.size(); i++) {
				listFood.get(i).drawFoodPane(gc);
			}

			gc.setFont(Font.font("Verdana", 15));
			gc.setFill(Color.BLACK);
			gc.fillText("Score: " + score, 280, 15);

			if (paused) {
				gc.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
				gc.setFill(Color.BLACK);
				gc.fillText("Paused", 240, 200);
			}
		}

		if (gameState == GAME_OVER_STATE) {
			gc.clearRect(0, 0, 600, 400);

			gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
			gc.setFill(Color.BLACK);
			gc.fillText("GAME OVER", 140, 150);

			gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
			gc.setFill(Color.BLACK);
			gc.fillText("Score: " + score, 230, 200);

			gc.setFont(Font.font("Verdana", 20));
			gc.setFill(Color.BLACK);
			gc.fillText("Press R to restart", 210, 380);

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
