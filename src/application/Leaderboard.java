package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import chat.ChatUI;
import chat.Highscore;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.TextAlignment;

/**
 * @author Andreas Andersson / Måns Grundberg
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
	private Label snakeHighscoreLabel = new Label();
	private Label spaceHighscoreLabel = new Label();
	private Label pongHighscoreLabel = new Label();
	private Pane pane = new Pane();
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

		// Setting amount of columns and rows for the GridPane
		createColumnsandRows();

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

		// Adding an setting the sound button
		soundButton = new Button();
		soundButton.setId("logOutButton");
		leaderboardRoot.add(soundButton, 32, 1);

		addHighscoreLabels();
		addActionListeners();
		setSoundButtonImages();
		checkSound();

		//Prevents these buttons from getting focus
		backButton.setFocusTraversable(false);
		soundButton.setFocusTraversable(false);

		//Setting the scene and its styles.
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

	//Getter for this objects scene.
	public Scene getScene() {
		addChatUI(chatUI);
		return this.scene;
	}

	/**
	 * Function that adds the chat to this object.
	 * @param chatUI the chat that is going to be set.
	 */
	private void addChatUI(ChatUI chatUI) {
		leaderboardRoot.add(chatUI, 36, 0, 12, 24);
	}

	/*
	 * Function that adds all of the labels for the highscores.
	 */
	private void addHighscoreLabels() {
		pongHighscoreLabel.setId("highscoreLabel");
		pongHighscoreLabel.setPrefSize(300.0, 350.00);

		snakeHighscoreLabel.setId("highscoreLabel");
		snakeHighscoreLabel.setPrefSize(300.0, 350.00);

		pongHighscoreLabel.setTextAlignment(TextAlignment.LEFT);
		pongHighscoreLabel.setMinHeight(400);
		pongHighscoreLabel.setText("PONG");
		pongHighscoreLabel.setMinWidth(Region.USE_PREF_SIZE);
		pongHighscoreLabel.setMinHeight(Region.USE_PREF_SIZE);
		pongHighscoreLabel.setContentDisplay(ContentDisplay.CENTER);
		pongHighscoreLabel.setLayoutX(0);
		pongHighscoreLabel.setLayoutY(0);
		pongHighscoreLabel.setAlignment(Pos.TOP_CENTER);
		pane.getChildren().add(pongHighscoreLabel);

		snakeHighscoreLabel.setTextAlignment(TextAlignment.LEFT);
		snakeHighscoreLabel.setMinHeight(400);
		snakeHighscoreLabel.setText("SNAKE");
		snakeHighscoreLabel.setMinWidth(Region.USE_PREF_SIZE);
		snakeHighscoreLabel.setMinHeight(Region.USE_PREF_SIZE);
		snakeHighscoreLabel.setContentDisplay(ContentDisplay.CENTER);
		snakeHighscoreLabel.setLayoutX(565);
		snakeHighscoreLabel.setLayoutY(0);
		snakeHighscoreLabel.setAlignment(Pos.TOP_CENTER);
		pane.getChildren().add(snakeHighscoreLabel);

		spaceHighscoreLabel.setId("highscoreLabel");
		spaceHighscoreLabel.setPrefSize(300.0, 350.00);

		spaceHighscoreLabel.setAlignment(Pos.TOP_CENTER);
		spaceHighscoreLabel.setTextAlignment(TextAlignment.LEFT);
		spaceHighscoreLabel.setText("SPACE INVADERS");
		spaceHighscoreLabel.setMinHeight(400);
		spaceHighscoreLabel.setMinWidth(Region.USE_PREF_SIZE);
		spaceHighscoreLabel.setMinHeight(Region.USE_PREF_SIZE);
		spaceHighscoreLabel.setLayoutX(285);
		spaceHighscoreLabel.setLayoutY(0);
		spaceHighscoreLabel.setContentDisplay(ContentDisplay.CENTER);
		pane.getChildren().add(spaceHighscoreLabel);
		pane.setId("highscoreLabel");
		leaderboardRoot.add(pane, 0, 6);
	}

	public void updateHighscores(LinkedList<Highscore> highscores) {
		if (highscores.size() > 0) {
			if (highscores.get(0).getGame().equals("Snake")) {
				String temp = "";
				for (int i = 0; i < highscores.size(); i++) {
					String name = highscores.get(i).getUser().getUsername();
					if (name.length() > 8) {
						String temp2 = name.substring(0, 7);
						name = temp2;
					}
					if (i == 9) {
						temp += i + 1 + " " + name + " "
								+ highscores.get(i).getScore() + "\n";
					} else {
						temp += i + 1 + "  " + name + " "
								+ highscores.get(i).getScore() + "\n";
					}
				}
				snakeHighscoreLabel.setText("SNAKE\n\n" + temp);

			} else if (highscores.get(0).getGame().equals("Space Invaders")) {
				String temp = "";
				for (int i = 0; i < highscores.size(); i++) {
					String name = highscores.get(i).getUser().getUsername();
					if (name.length() > 8) {
						String temp2 = name.substring(0, 7);
						name = temp2;
					}
					if (i == 9) {
						temp += i + 1 + " " + name + " "
								+ highscores.get(i).getScore() + "\n";
					} else {
						temp += i + 1 + "  " + name + " "
								+ highscores.get(i).getScore() + "\n";
					}
				}
				spaceHighscoreLabel.setText("SPACE INVADERS\n\n" + temp);
			} else {
				String temp = "";
				for (int i = 0; i < highscores.size(); i++) {
					String name = highscores.get(i).getUser().getUsername();
					if (name.length() > 8) {
						String temp2 = name.substring(0, 7);
						name = temp2;
					}
					if (i == 9) {
						temp += i + 1 + " " + name + " "
								+ highscores.get(i).getScore() + "\n";
					} else {
						temp += i + 1 + "  " + name + " "
								+ highscores.get(i).getScore() + "\n";
					}
				}
				pongHighscoreLabel.setText("PONG \n\n" + temp);
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

	/**
	 * Checks the sound if it is muted or not. 
	 * If it is muted the Soundbuttons symbol should show the correct symbol.
	 * This method could be called upon from outside of this class.
	 */
	public void checkSound() {
		if (jukebox.isMute()) {
			soundButton.setGraphic(muteSoundImageView);
		} else {
			soundButton.setGraphic(playSoundImageView);
		}
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