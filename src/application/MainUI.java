package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import chat.ClientController;
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
	private Pong pong;
	private GUIPane snake;
	private Painter spaceInvaders;
	private JukeBox jukebox;
	private ClientController controller;
	private ChatUI chatUI;
	public static Stage primaryStage = new Stage();

	/**
	 * Constructor that starts the music and initiates a leaderboard and sends it to the controller.
	 * @param chatUI the chatUI parameter is used to display the chat.
	 * @param controller the controller parameter is used to handle the leaderboard object.
	 */
	public MainUI(ChatUI chatUI, ClientController controller) {
		this.chatUI = chatUI;
		this.controller = controller;
		jukebox = new JukeBox();
		jukebox.play();
		this.leaderboard = new Leaderboard(this, this.chatUI, this.jukebox);
		controller.addLeaderboard(leaderboard);
	}

	/**
	 * Function that initiates the main menu and its components.
	 */
	public void start(Stage primaryStage) {

		// Setting the main Pane for the scene.
		mainRoot = new GridPane();
		mainRoot.setId("mainRoot");
		mainRoot.setPrefSize(1200.0, 600.0);
		
		setColumnsandRows();
		setArcadeMachineImage();

		// Adding and setting the Label for Virtual Arcade-header
		Glow glow = new Glow(0.1);
		Bloom bloom = new Bloom(0.1);
		Label virtualArcadeLabel = new Label("VIRTUAL");
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

		// Adding and setting the Leaderboard button
		leaderboardButton.setId("mainButtons");
		mainRoot.add(leaderboardButton, 26, 0, 6, 3);

		// Adding and setting the Log out button
		logOutButton.setId("logOutButton");
		mainRoot.add(logOutButton, 1, 21, 6, 3);

		// Adding an setting the button for mute and un-mute of login music
		soundButton = new Button();
		soundButton.setId("logOutButton");
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

		//Adding the Chat
		mainRoot.add(chatUI, 36, 0, 12, 24);

		// Sets the scene, adds all children nodes and sets the css-style.
		scene = new Scene(mainRoot, 1200, 600);
		try {
			scene.getStylesheets().add((new File("styles//mainStyle.css")).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		setSoundButtonImages();
		checkSound();
		addActionListeners(primaryStage);

		//Prevents the buttons to get focus when hitting the Tab-button.
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
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				Platform.exit();
				System.exit(0);
			}
		});
		
		//The Virtual Arcade logo requests focus so that buttons doesn't steal it.
		virtualArcadeLabel.requestFocus();

	}

	/**
	 * Sets the number and size-percentage of the rows and columns in the GridPane.
	 */
	private void setColumnsandRows() {
		final int numOfCols = 48;
		final int numOfRows = 24;
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

	/**
	 * Function that creates and sets the arcade machine images to the main menu.
	 */
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
	
	/**
	 * Sets the sound buttons images.
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
	 * Checks the sound if it is paused or playing. 
	 * If it is paused the sound button symbol should show the correct symbol.
	 * This method could be called upon from outside of this class.
	 */
	private void checkSound() {
		if (jukebox.isPaused()) {
			soundButton.setGraphic(muteSoundImageView);
		} else {
			soundButton.setGraphic(playSoundImageView);
		}
	}

	/**  
	 * Function that switches to the MainUI's scene. Called upon from outside classes.
	 */
	public void switchToMainUI() {
		mainRoot.add(chatUI, 36, 0, 12, 24);
		primaryStage.setScene(scene);
		primaryStage.show();
		checkSound();
	}


	/**
	 * A function that terminates the current MainUI object and stops the sound so that the sound wont overlap.
	 */
	private void terminate() {
		primaryStage.close();
		jukebox.stopSound();
		try {
			this.stop();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/** 
	 * Function for adding and setting Action Listeners to all Buttons.
	 * 
	 * @param primaryStage the stage parameter is used so that everything is displayed in the same stage and not opened in a separate stage.
	 */
	private void addActionListeners(Stage primaryStage) {

		leaderboardButton.setOnAction(e -> {
			mainRoot.getChildren().remove(chatUI);
			primaryStage.setScene(leaderboard.getScene());
			primaryStage.show();
			leaderboard.checkSound();
		});

		logOutButton.setOnAction(e -> {
			this.terminate();
			controller.disconnect();
			controller = null;
			new ClientController();
		});

		soundButton.setOnAction(e -> {
			jukebox.pauseOrPlay();
			if (jukebox.isPaused()) {
				soundButton.setGraphic(muteSoundImageView);
			} else {
				soundButton.setGraphic(playSoundImageView);
			}
		});

		pongPlayButton.setOnAction(e -> {
			if (pong == null) {
				pong = new Pong(this, chatUI, jukebox, controller);
			}

			mainRoot.getChildren().remove(chatUI);
			primaryStage.setScene(pong.getScene());
			primaryStage.show();
			pong.checkSound();
		});

		spacePlayButton.setOnAction(e -> {
			if (spaceInvaders == null) {
				spaceInvaders = new Painter(this, chatUI, jukebox, controller);
			}
			mainRoot.getChildren().remove(chatUI);
			primaryStage.setScene(spaceInvaders.getScene());
			primaryStage.show();
			spaceInvaders.checkSound();
		});

		snakePlayButton.setOnAction(e -> {
			if (snake == null) {
				snake = new GUIPane(this, chatUI, jukebox, controller);
			}
			mainRoot.getChildren().remove(chatUI);
			primaryStage.setScene(snake.getScene());
			primaryStage.show();
			snake.checkSound();
		});
	}
}