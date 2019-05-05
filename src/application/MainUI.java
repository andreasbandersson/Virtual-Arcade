package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.SwingUtilities;

import chat.ChatController;
import chat.ChatTestUI;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.TextAlignment;

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
	private Button settingsButton = new Button("SETTINGS");
	private Button logOutButton = new Button("LOG OUT");
	private Button pongPlayButton = new Button("START");
	private Button spacePlayButton = new Button("START");
	private Button snakePlayButton = new Button("START");
	private Scene scene;
	private GridPane mainRoot;
	private MediaPlayer mediaPlayer;
	private ChatTestUI chatTestUI = new ChatTestUI(new ChatController());
	private LoginUI login;
	private Leaderboard leaderBoard;
	private Pong pong;
	private Snake snake;
	private SpaceInvaders spaceInvaders;
	private final int numOfCols = 48;
	private final int numOfRows = 24;

	// Function that initiates the main menu and its components.
	public void start(Stage primaryStage) {

		// Setting the main Pane for the scene.
		mainRoot = new GridPane();
		mainRoot.setId("mainRoot");
		mainRoot.setPrefSize(1200.0, 600.0);

		startSound();
		setColumnsandRows();
		setArcadeMachineImage();
		setSoundButtonImages();
		

		// Adding and setting the Label for Virtual Arcade-header
		Label virtualArcadeLabel = new Label("VIRTUAL\nARCADE");
		Glow glow = new Glow(1.0);
		Bloom bloom = new Bloom(0.9);
		virtualArcadeLabel.setId("vaLabel");
		virtualArcadeLabel.setEffect(bloom);
		virtualArcadeLabel.setEffect(glow);
		mainRoot.add(virtualArcadeLabel, 1, 0, 10, 6);

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
		mainRoot.add(leaderboardButton, 21, 0, 6, 3);

		settingsButton.setId("mainButtons");
		mainRoot.add(settingsButton, 27, 0, 5, 3);

		logOutButton.setId("logOutButton");
		mainRoot.add(logOutButton, 1, 21, 6, 3);

		// Adding an setting the button for mute and un-mute of login music
		soundButton = new Button();
		soundButton.setId("logOutButton");
		soundButton.setGraphic(playSoundImageView);
		mainRoot.add(soundButton, 32, 1);

		// Adding and setting the "Play"-buttons for the different arcade games
		pongPlayButton.setId("arcadeButtons");
		mainRoot.add(pongPlayButton, 5, 17, 4, 2);

		spacePlayButton.setId("arcadeButtons");
		mainRoot.add(spacePlayButton, 16, 20, 4, 2);

		snakePlayButton.setId("nokiaButton");
		mainRoot.add(snakePlayButton, 27, 20, 4, 1);

		// Setting the chat
		SwingNode chatUI = new SwingNode();
		createSwingContent(chatUI);
		StackPane pane = new StackPane();
		pane.setId("swingPane");
		pane.setPrefSize(300.0, 600.0);
		pane.getChildren().add(chatUI);
		mainRoot.add(pane, 36, 0, 12, 24);

		// Sets the scene, adds all children nodes and sets the css-style.
		scene = new Scene(mainRoot, 1200, 600);
		scene.getStylesheets().addAll(this.getClass().getResource("styles/style.css").toExternalForm());

		addActionListeners(primaryStage);
		
		// Sets the primaryStage
		primaryStage.setTitle("VIRTUAL ARCADE");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();
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

	// Function that sets the received parameter as a SwingNode
	private void createSwingContent(final SwingNode swingNode) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				swingNode.setContent(chatTestUI);
				swingNode.requestFocus();
			}
		});
	}
	
	public ChatTestUI getChat() {
		return chatTestUI;
	}

	// Function that creates and sets the arcade machine images to the main menu.
	private void setArcadeMachineImage() {
		try {
			pongImage = new Image(new FileInputStream("images/pongGame.png"));
			spaceImage = new Image(new FileInputStream("images/spaceGame.png"));
			snakeImage = new Image(new FileInputStream("images/snakeGame.png"));
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

	// Sets the the login music and starts it.
	private void startSound() {
		String musicFile = "sounds/Lobby-Sound-1.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		mediaPlayer.setVolume(0.1);
	}

	// Mutes the login music if the music is playing, otherwise the function unmutes
	// the music.
	private void muteUnmuteSound() {
		if (mediaPlayer.isMute()) {
			mediaPlayer.setMute(false);
			mediaPlayer.setVolume(0.1);
			soundButton.setGraphic(playSoundImageView);
		} else {
			mediaPlayer.setMute(true);
			soundButton.setGraphic(muteSoundImageView);
		}
	}

	// Stops the music.
	private void stopSound() {
		if (mediaPlayer.getStatus() == Status.PLAYING) {
			mediaPlayer.stop();
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

	// Function for adding and setting Action Listeners to all Buttons.
	private void addActionListeners(Stage primaryStage) {

		leaderboardButton.setOnAction(e -> {
			leaderBoard = new Leaderboard();
			leaderBoard.setMainMenu(this);
			leaderBoard.start(primaryStage);
			stopSound();
			try {
				this.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		logOutButton.setOnAction(e -> {
			login = new LoginUI();
			login.start(primaryStage);
			stopSound();
			try {
				this.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		soundButton.setOnAction(e -> muteUnmuteSound());

		pongPlayButton.setOnAction(e -> {
			pong = new Pong();
			pong.start(primaryStage);
			stopSound();
			try {
				this.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		spacePlayButton.setOnAction(e -> {
			spaceInvaders = new SpaceInvaders();
			spaceInvaders.start(primaryStage);
			stopSound();
			try {
				this.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		snakePlayButton.setOnAction(e -> {
			snake = new Snake();
			snake.start(primaryStage);
			stopSound();
			try {
				this.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	// Main-method
//	public static void main(String[] args) {
//		launch(args);
//	}
}
