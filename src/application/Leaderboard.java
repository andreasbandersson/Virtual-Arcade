package application;

import java.awt.Color;
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
import javafx.geometry.Insets;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

/**
 * @author Andreas Andersson
 */

public class Leaderboard extends Application {

	private Button backButton = new Button("BACK");
	private Image muteSoundImage;
	private Image playSoundImage;
	private ImageView muteSoundImageView;
	private ImageView playSoundImageView;
	private Button soundButton;
	private Scene scene2, scene3;
	private GridPane leaderboardRoot;
	// private ChatTestUI chatTestUI = new ChatTestUI(new ChatController());
	private Label headerLabel = new Label("LEADERBOARD");
	private Glow glow = new Glow(1.0);
	private Bloom bloom = new Bloom(0.9);
	private MediaPlayer mediaPlayer;
	private MainUI mainMenu;

	private final int numOfCols = 48;
	private final int numOfRows = 24;

	public void start(Stage primaryStage) {
		leaderboardRoot = new GridPane();
		leaderboardRoot.setId("leaderboardRoot");
		leaderboardRoot.setPrefSize(1200.0, 600.0); // minus chattens bredd (300)
		leaderboardRoot.setGridLinesVisible(true);

		// Setting amount of columns and rows for the GridPane
		createColumnsandRows();
		setSoundButtonImages();
		startSound();
		
		// Adding and setting the Label for the Leaderboard.
		headerLabel.setId("leaderboardLabel");
		FadeTransition fadeTransitionH = new FadeTransition(Duration.seconds(0.5), headerLabel);
		fadeTransitionH.setFromValue(1.0);
		fadeTransitionH.setToValue(0.6);
		fadeTransitionH.setCycleCount(Animation.INDEFINITE);
		fadeTransitionH.autoReverseProperty();
		fadeTransitionH.play();
		leaderboardRoot.add(headerLabel, 8, 0, 30, 5);

		// Adding and setting the main buttons
		backButton.setId("logOutButton");
		leaderboardRoot.add(backButton, 1, 21, 6, 2);
		
		// Adding an setting the button for mute and un-mute of login music
		soundButton = new Button();
		soundButton.setId("logOutButton");
		soundButton.setGraphic(playSoundImageView);
		leaderboardRoot.add(soundButton, 32, 1);

		// Setting the chat
//		SwingNode chatUI = new SwingNode();
//		createSwingContent(chatUI);
//		StackPane pane = new StackPane();
//		pane.setId("swingPane");
//		pane.setPrefSize(300.0, 600.0);
//		pane.getChildren().add(chatUI);
//		leaderboardRoot.add(pane, 36, 0, 12, 24);

		addHighscoreLabels();
		addActionListeners(primaryStage);

		// Sets the scene, adds all children nodes and sets the css style.
		scene2 = new Scene(leaderboardRoot, 1200, 600);
		scene2.getStylesheets().addAll(this.getClass().getResource("styles/style.css").toExternalForm());

		// Sets the primaryStage
		primaryStage.setTitle("VIRTUAL ARCADE");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene2);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	// Sets the number and size-percentage of the rows and columns in the GridPane.
	private void createColumnsandRows() {

		for (int i = 0; i < numOfCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(100.0 / numOfCols);
			leaderboardRoot.getColumnConstraints().add(colConst);
		}
		for (int i = 0; i < numOfRows; i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setPercentHeight(100.0 / numOfRows);
			leaderboardRoot.getRowConstraints().add(rowConst);
		}
	}
	
	public void setMainMenu(MainUI mainMenu) {
		this.mainMenu = mainMenu;
	}

	// Adds the chat to the GridPane
	private void createSwingContent(final SwingNode swingNode) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				swingNode.setContent(mainMenu.getChat());
				swingNode.requestFocus();
			}
		});
	}

	public void addHighscoreLabels() {
		Label pongHighscoreLabel = new Label();
		String pongHighscoreString = "";
		pongHighscoreLabel.setId("highscoreLabel");
		pongHighscoreLabel.setPrefSize(400.0, 200.00);
		pongHighscoreLabel.setPadding(new Insets(0.0, 5.0, 0.0, 10.0));
		for (int i = 0; i < 10; i++) {
			pongHighscoreString += i + 1 + " Andreas		" + "1000\n\n";
			pongHighscoreLabel.setText("PONG HIGHSCORES\n\n" + pongHighscoreString);
		}
		leaderboardRoot.add(pongHighscoreLabel, 8, 4, 9, 8);
		
		Label snakeHighscoreLabel = new Label();
		String snakeHighscoreString = "";
		snakeHighscoreLabel.setId("highscoreLabel");
		snakeHighscoreLabel.setPrefSize(400.0, 200.00);
		snakeHighscoreLabel.setPadding(new Insets(0.0, 5.0, 0.0, 10.0));
		for (int i = 0; i < 10; i++) {
			snakeHighscoreString += i + 1 + " Masse			" + "900\n\n";
			snakeHighscoreLabel.setText("SNAKE HIGHSCORES\n\n" + snakeHighscoreString);
		}
		leaderboardRoot.add(snakeHighscoreLabel, 20, 4, 9, 8);
		
		Label spaceHighscoreLabel = new Label();
		String spaceHighscoreString = "";
		spaceHighscoreLabel.setId("highscoreLabel");
		spaceHighscoreLabel.setPrefSize(400.0, 200.00);
		spaceHighscoreLabel.setPadding(new Insets(0.0, 5.0, 0.0, 10.0));
		for (int i = 0; i < 10; i++) {
			spaceHighscoreString += i + 1 + " Viktor		" + "1000\n\n";
			spaceHighscoreLabel.setText("SPACE INVADERS HIGHSCORES\n\n" + spaceHighscoreString);
		}
		leaderboardRoot.add(spaceHighscoreLabel, 8, 13, 9, 8);
		
		Label globalHighscoreLabel = new Label();
		String globalHighscoreString = "";
		globalHighscoreLabel.setId("highscoreLabel");
		globalHighscoreLabel.setPrefSize(400.0, 200.00);
		globalHighscoreLabel.setPadding(new Insets(0.0, 5.0, 0.0, 10.0));
		for (int i = 0; i < 10; i++) {
			globalHighscoreString += i + 1 + " Gulcin			" + "900\n\n";
			globalHighscoreLabel.setText("GLOBAL HIGHSCORES\n\n" + globalHighscoreString);
		}
		leaderboardRoot.add(globalHighscoreLabel, 20, 13, 9, 8);

	}
	// Sets the the login music and starts it.
	private void startSound() {
		String musicFile = "sounds/Knight-Rider-Theme-Song.mp3";
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
	
	//Function for adding and setting Action Listeners to all Buttons.
	private void addActionListeners(Stage primaryStage) {
		
		backButton.setOnAction(e -> {
			mainMenu.start(primaryStage);
			stopSound();
			try {
				this.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		soundButton.setOnAction(e -> muteUnmuteSound());
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
