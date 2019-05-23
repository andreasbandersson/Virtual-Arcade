package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import chat.ChatController;
import chat.ChatUI;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import pong.Pong;
import snake.GUIPane;
import spaceInvaders.graphics.Painter;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

/**
 * @author Andreas Andersson
 */

public class MainUI extends Application {
	private Image pongImage;
	private Image spaceImage;
	private Image snakeImage;
	private Image muteSoundImage;
	private Image playSoundImage;
	private ImageView muteSoundImageView;
	private ImageView playSoundImageView;
	private Button soundButton;
	private Button leaderboardButton = new Button("LEADERBOARD");
	private Button logOutButton = new Button("LOG OUT");
	private Button pongPlayButton = new Button("START");
	private Button spacePlayButton = new Button("START");
	private Button snakePlayButton = new Button("START");
	private Scene scene;
	private GridPane mainRoot;
	private Leaderboard leaderboard;
	private pong2.Pong pong;
	private GUIPane snake;

	// private SpaceInvaders spaceInvaders;
	private Painter spaceInvaders;
	private JukeBox jukebox;
	private ChatController controller;
	private final int numOfCols = 48;
	private final int numOfRows = 24;
	private ChatUI chatUI;
	public static Stage primaryStage = new Stage();

	public MainUI(ChatUI chatUI, ChatController controller) {
		this.chatUI = chatUI;
		this.controller = controller;
		jukebox = new JukeBox();
		this.leaderboard = new Leaderboard(this, this.chatUI, this.jukebox);
		controller.addLeaderboard(leaderboard);
	}

	// Function that initiates the main menu and its components.
	public void start(Stage primaryStage) {

		// Setting the main Pane for the scene.
		mainRoot = new GridPane();
		mainRoot.setId("mainRoot");
		mainRoot.setPrefSize(1200.0, 600.0);

		setColumnsandRows();
		setArcadeMachineImage();
		setSoundButtonImages();

		jukebox.play();

		// Adding and setting the Label for Virtual Arcade-header
		Label virtualArcadeLabel = new Label("VIRTUAL");
		Glow glow = new Glow(0.1);
		Bloom bloom = new Bloom(0.1);
		virtualArcadeLabel.setId("vaLabel");
		virtualArcadeLabel.setEffect(bloom);
		virtualArcadeLabel.setEffect(glow);
		virtualArcadeLabel.setMinWidth(Region.USE_PREF_SIZE);
		mainRoot.add(virtualArcadeLabel, 1, 1);

		Label virtualArcadeLabel2 = new Label("ARCADE");
		virtualArcadeLabel2.setId("vaLabel");
		virtualArcadeLabel2.setEffect(bloom);
		virtualArcadeLabel2.setEffect(glow);
		virtualArcadeLabel2.setMinWidth(Region.USE_PREF_SIZE);
		mainRoot.add(virtualArcadeLabel2, 1, 4);

		// Adding and setting the Label for the "Choose game"-header
		Label headerLabel = new Label("CHOOSE YOUR GAME");
		headerLabel.setId("headerLabel");
		FadeTransition fadeTransitionH = new FadeTransition(Duration.seconds(2.0), headerLabel);
		fadeTransitionH.setFromValue(1.0);
		fadeTransitionH.setToValue(0.2);
		fadeTransitionH.setCycleCount(Animation.INDEFINITE);
		fadeTransitionH.play();
		mainRoot.add(headerLabel, 10, 7, 20, 4);

		// Adding and setting the main menu buttons
		leaderboardButton.setId("mainButtons");
		mainRoot.add(leaderboardButton, 26, 0, 6, 3);

		logOutButton.setId("logOutButton");
		mainRoot.add(logOutButton, 1, 21, 6, 3);

		// Adding an setting the button for mute and un-mute of login music
		soundButton = new Button();
		soundButton.setId("logOutButton");
		soundButton.setGraphic(playSoundImageView);
		mainRoot.add(soundButton, 32, 1);

		// Adding and setting the "Play"-buttons for the different arcade games
		pongPlayButton.setId("arcadeButtons");
		pongPlayButton.setMinWidth(Region.USE_PREF_SIZE);
		mainRoot.add(pongPlayButton, 5, 17, 4, 2);

		spacePlayButton.setId("arcadeButtons");
		spacePlayButton.setMinWidth(Region.USE_PREF_SIZE);
		mainRoot.add(spacePlayButton, 16, 19, 4, 2);

		snakePlayButton.setId("nokiaButton");
		snakePlayButton.setMinWidth(Region.USE_PREF_SIZE);
		mainRoot.add(snakePlayButton, 27, 20, 4, 1);

		mainRoot.add(chatUI, 36, 0, 12, 24);

		// Sets the scene, adds all children nodes and sets the css-style.
		scene = new Scene(mainRoot, 1200, 600);
		try {
			scene.getStylesheets().add((new File("styles//mainStyle.css")).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		addActionListeners(primaryStage);

		leaderboardButton.setFocusTraversable(false);
		soundButton.setFocusTraversable(false);
		logOutButton.setFocusTraversable(false);
		spacePlayButton.setFocusTraversable(false);
		snakePlayButton.setFocusTraversable(false);
		pongPlayButton.setFocusTraversable(false);

		// Sets the primaryStage
		primaryStage.setTitle("VIRTUAL ARCADE");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();
		// stage = primaryStage;

		primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				Platform.exit();
				System.exit(0);
			}
		});
		
		virtualArcadeLabel.requestFocus();

	}

	// Sets the number and size-percentage of the rows and columns in the GridPane.
	private void setColumnsandRows() {
		for (int i = 0; i < numOfCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(100.0 / numOfCols);
			mainRoot.getColumnConstraints().add(colConst);
		}
		for (int i = 0; i < numOfRows; i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setPercentHeight(100.0 / numOfRows);
			mainRoot.getRowConstraints().add(rowConst);
		}
	}

	// Function that creates and sets the arcade machine images to the main menu.
	private void setArcadeMachineImage() {
		try {
			pongImage = new Image(new FileInputStream("images/pongGame2.png"));
			spaceImage = new Image(new FileInputStream("images/spaceGame3.png"));
			snakeImage = new Image(new FileInputStream("images/snakeGame2.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ImageView pongView = new ImageView(pongImage);
		ImageView spaceView = new ImageView(spaceImage);
		ImageView snakeView = new ImageView(snakeImage);

		pongView.setFitWidth(250);
		pongView.setPreserveRatio(true);
		mainRoot.add(pongView, 2, 17);

		spaceView.setFitWidth(250);
		spaceView.setPreserveRatio(true);
		mainRoot.add(spaceView, 13, 17);

		snakeView.setFitWidth(250);
		snakeView.setPreserveRatio(true);
		mainRoot.add(snakeView, 24, 17);

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

	public void switchToMainUI() {
		mainRoot.add(chatUI, 36, 0, 12, 24);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void terminate() {
		primaryStage.close();
		jukebox.stopSound();
		try {
			this.stop();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	// Function for adding and setting Action Listeners to all Buttons.
	private void addActionListeners(Stage primaryStage) {

		leaderboardButton.setOnAction(e -> {
			mainRoot.getChildren().remove(chatUI);
			primaryStage.setScene(leaderboard.getScene());
			primaryStage.show();
		});

		logOutButton.setOnAction(e -> {
			this.terminate();
			controller.disconnect();
			controller = null;
			new ChatController();
		});

		soundButton.setOnAction(e -> {
			jukebox.muteUnmute();
			if (jukebox.isMute()) {
				soundButton.setGraphic(muteSoundImageView);
			} else {
				soundButton.setGraphic(playSoundImageView);
			}
		});

		// Skriv om. Instansiera pong och l�gg in i mainRoot som pane bara
		pongPlayButton.setOnAction(e -> {
			if (pong == null) {
				pong = new pong2.Pong(this, chatUI, jukebox, controller);
			}

			mainRoot.getChildren().remove(chatUI);
			primaryStage.setScene(pong.getScene());
			primaryStage.show();
		});

		// Skriv om. Instansiera Space Invaders och l�gg in i mainRoot som pane bara
		spacePlayButton.setOnAction(e -> {
			if (spaceInvaders == null) {
				spaceInvaders = new Painter(this, chatUI, jukebox, controller);
			}
			mainRoot.getChildren().remove(chatUI);
			primaryStage.setScene(spaceInvaders.getScene());
			primaryStage.show();
		});

		// Skriv om. Instansiera Snake och l�gg in i mainRoot som pane bara
		snakePlayButton.setOnAction(e -> {
			if (snake == null) {
				snake = new GUIPane(this, chatUI, jukebox, controller);
			}
			mainRoot.getChildren().remove(chatUI);
			primaryStage.setScene(snake.getScene());
			primaryStage.show();
		});
	}
}