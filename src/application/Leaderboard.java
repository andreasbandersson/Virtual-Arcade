package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.LinkedList;

import chat.ChatUI;
import chat.Highscore;
import chat.HighscoreList;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.TextAlignment;

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
	private Label pongHighscoreLabel = new Label();
	private Label snakeHighscoreLabel = new Label();
	private Label spaceHighscoreLabel = new Label();
	private Label globalHighscoreLabel = new Label();

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
		// leaderboardRoot.setGridLinesVisible(true);

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
		soundButton.setGraphic(playSoundImageView);
		leaderboardRoot.add(soundButton, 32, 1);

		addHighscoreLabels();
		addActionListeners();

		backButton.setFocusTraversable(false);
		soundButton.setFocusTraversable(false);

		System.out.println(System.getProperty("os.name").toString());

		scene = new Scene(leaderboardRoot, 1200, 600);
		try {
			scene.getStylesheets().add((new File("styles//mainStyle.css")).toURI().toURL().toExternalForm());
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

	// public void updateHighscoreLabels(Highscore[] snakeList) {
	// String snake = "";
	// int count = 1;
	// for (int i = snakeList.length; i > 0; i--) {
	// snake += count + " " + snakeList[i].getUser().getUsername() + " " +
	// snakeList[i].getScore();
	// count++;
	// }
	// }

	private void addHighscoreLabels() {
		// String pongHighscoreString = "";
		// pongHighscoreLabel.setId("highscoreLabel");
		// pongHighscoreLabel.setPrefSize(400.0, 200.00);
		// pongHighscoreLabel.setPadding(new Insets(0.0, 5.0, 0.0, 10.0));
		// for (int i = 0; i < 10; i++) {
		// pongHighscoreString += i + 1 + " Andreas " + "1000\n\n";
		// pongHighscoreLabel.setText("PONG HIGHSCORES\n\n" + pongHighscoreString);
		// }
		// leaderboardRoot.add(pongHighscoreLabel, 8, 4, 9, 8);
		
		

		snakeHighscoreLabel.setId("highscoreLabel");
		snakeHighscoreLabel.setPrefSize(300.0, 400.00);

		snakeHighscoreLabel.setTextAlignment(TextAlignment.LEFT);
		snakeHighscoreLabel.setMinHeight(400);
		snakeHighscoreLabel.setText("SNAKE");
		leaderboardRoot.add(snakeHighscoreLabel, 20, 4, 10, 16);
		snakeHighscoreLabel.setMinWidth(Region.USE_PREF_SIZE);
		snakeHighscoreLabel.setMinHeight(Region.USE_PREF_SIZE);

		spaceHighscoreLabel.setId("highscoreLabel");
		spaceHighscoreLabel.setPrefSize(300.0, 400.00);

		spaceHighscoreLabel.setTextAlignment(TextAlignment.LEFT);
		spaceHighscoreLabel.setText("SPACE INVADERS");
		spaceHighscoreLabel.setMinHeight(400);
		spaceHighscoreLabel.setMinWidth(Region.USE_PREF_SIZE);
		spaceHighscoreLabel.setMinHeight(Region.USE_PREF_SIZE);
		leaderboardRoot.add(spaceHighscoreLabel, 6, 4, 10, 16);
	}

	public void updateHighscores(LinkedList<Highscore> highscores) {
		if (highscores.size() > 0) {
			if (highscores.get(0).getGame().equals("Snake")) {
				String snakeHighscoreString = "";
				for (int i = 0; i < highscores.size(); i++) {
					if (i == 9) {
					snakeHighscoreString += i + 1 + " " + highscores.get(i).getUser().getUsername() + " "
							+ highscores.get(i).getScore() + "\n";
					} else {
						snakeHighscoreString += i + 1 + " " + highscores.get(i).getUser().getUsername() + " "
								+ highscores.get(i).getScore() + "\n";
					}
				}
				snakeHighscoreLabel.setText("SNAKE\n\n" + snakeHighscoreString);
				
			} else {
				String spaceHighscoreString = "";
				for (int i = 0; i < highscores.size(); i++) {
					if (i == 9) {
						spaceHighscoreString += i + 1 + " " + highscores.get(i).getUser().getUsername() + " "
								+ highscores.get(i).getScore() + "\n";
					} else {
						spaceHighscoreString += i + 1 + "  " + highscores.get(i).getUser().getUsername() + " "
								+ highscores.get(i).getScore() + "\n";
					}
				}
				spaceHighscoreLabel.setText("SPACE INVADERS\n\n" + spaceHighscoreString);
			}
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
	private void addActionListeners() {

		backButton.setOnAction(e -> {
			leaderboardRoot.getChildren().remove(chatUI);
			mainMenu.switchToMainUI();
		});

		soundButton.setOnAction(e -> {
			jukebox.muteUnmute();
			if (jukebox.isMute()) {
				soundButton.setGraphic(muteSoundImageView);
			} else {
				soundButton.setGraphic(playSoundImageView);
			}
		});

	}
}