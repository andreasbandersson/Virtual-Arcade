package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import chat.ChatUI;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * @author Andreas Andersson
 */

public class Leaderboard {

	private Button backButton = new Button("BACK");
	private Image muteSoundImage;
	private Image playSoundImage;
	private ImageView muteSoundImageView;
	private ImageView playSoundImageView;
	private Button soundButton;
	private Label headerLabel = new Label("LEADERBOARD");
	private MainUI mainMenu;
	private Scene scene;
	private GridPane leaderboardRoot;
	private ChatUI chatUI;
	private JukeBox jukebox;

	private final int numOfCols = 48;
	private final int numOfRows = 24;

	public Leaderboard(MainUI mainMenu, ChatUI chatUI, JukeBox jukebox) {
		this.mainMenu = mainMenu;
		this.chatUI = chatUI;
		this.jukebox = jukebox;
		init();
	}

	private void init() {
		leaderboardRoot = new GridPane();
		leaderboardRoot.setId("leaderboardRoot");
		leaderboardRoot.setPrefSize(1200.0, 600.0); // minus chattens bredd (300)
//		leaderboardRoot.setGridLinesVisible(true);

		// Setting amount of columns and rows for the GridPane
		createColumnsandRows();
		setSoundButtonImages();

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
		soundButton.setGraphic(muteSoundImageView);
		leaderboardRoot.add(soundButton, 32, 1);

		addHighscoreLabels();
		addActionListeners();
		
		backButton.setFocusTraversable(false);
	    soundButton.setFocusTraversable(false);

		scene = new Scene(leaderboardRoot, 1200, 600);
		try {
			scene.getStylesheets().add((new File("styles\\mainStyle.css")).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
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

	public Scene getScene() {
		addChatUI(chatUI);
		return this.scene;
	}

	private void addChatUI(ChatUI chatUI) {
		leaderboardRoot.add(chatUI, 36, 0, 12, 24);
	}

	private void addHighscoreLabels() {
		Label pongHighscoreLabel = new Label();
		String pongHighscoreString = "";
		pongHighscoreLabel.setId("highscoreLabel");
		pongHighscoreLabel.setPrefSize(400.0, 200.00);
		pongHighscoreLabel.setPadding(new Insets(0.0, 5.0, 0.0, 10.0));
		for (int i = 0; i < 11; i++) {
			pongHighscoreString += i + 1 + " Andreas		" + "1000\n\n";
			pongHighscoreLabel.setText("PONG HIGHSCORES\n\n" + pongHighscoreString);
		}
		leaderboardRoot.add(pongHighscoreLabel, 8, 4, 9, 8);

		Label snakeHighscoreLabel = new Label();
		String snakeHighscoreString = "";
		snakeHighscoreLabel.setId("highscoreLabel");
		snakeHighscoreLabel.setPrefSize(400.0, 200.00);
		snakeHighscoreLabel.setPadding(new Insets(0.0, 5.0, 0.0, 10.0));
		for (int i = 0; i < 11; i++) {
			snakeHighscoreString += i + 1 + " Masse			" + "900\n\n";
			snakeHighscoreLabel.setText("SNAKE HIGHSCORES\n\n" + snakeHighscoreString);
		}
		leaderboardRoot.add(snakeHighscoreLabel, 20, 4, 9, 8);

		Label spaceHighscoreLabel = new Label();
		String spaceHighscoreString = "";
		spaceHighscoreLabel.setId("highscoreLabel");
		spaceHighscoreLabel.setPrefSize(400.0, 200.00);
		spaceHighscoreLabel.setPadding(new Insets(0.0, 5.0, 0.0, 10.0));
		for (int i = 0; i < 11; i++) {
			spaceHighscoreString += i + 1 + " Viktor		" + "1000\n\n";
			spaceHighscoreLabel.setText("SPACE INVADERS HIGHSCORES\n\n" + spaceHighscoreString);
		}
		leaderboardRoot.add(spaceHighscoreLabel, 8, 13, 9, 8);

		Label globalHighscoreLabel = new Label();
		String globalHighscoreString = "";
		globalHighscoreLabel.setId("highscoreLabel");
		globalHighscoreLabel.setPrefSize(400.0, 200.00);
		globalHighscoreLabel.setPadding(new Insets(0.0, 5.0, 0.0, 10.0));
		for (int i = 0; i < 11; i++) {
			globalHighscoreString += i + 1 + " Gulcin			" + "900\n\n";
			globalHighscoreLabel.setText("GLOBAL HIGHSCORES\n\n" + globalHighscoreString);
		}
		leaderboardRoot.add(globalHighscoreLabel, 20, 13, 9, 8);

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
	private void addActionListeners() {

		backButton.setOnAction(e -> {
			leaderboardRoot.getChildren().remove(chatUI);
			mainMenu.switchToMainUI();
		});

		soundButton.setOnAction(e -> {
			jukebox.muteUnmute();
			if (jukebox.isMute()) {
				soundButton.setGraphic(playSoundImageView);
			} else {
				soundButton.setGraphic(muteSoundImageView);
			}
		});

	}
}
