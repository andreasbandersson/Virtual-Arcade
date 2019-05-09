package pong;

import static pong.Config.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import application.JukeBox;
import application.MainUI;
import chat.ChatUI;
import javafx.beans.InvalidationListener;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import pong.Game;

public class Pong {
	private MainUI mainUI;
	private ChatUI chatUI;
	private Scene scene;
	private Button backButton = new Button("BACK");
	private Button soundButton;
	private GridPane root = new GridPane();
	private final int numOfCols = 48;
	private final int numOfRows = 24;
	private Image muteSoundImage;
	private Image playSoundImage;
	private ImageView muteSoundImageView;
	private ImageView playSoundImageView;
	private JukeBox jukebox;
	private int width = 600;
	private int height = 400;
	private Game game;

	public Pong(MainUI mainUI, ChatUI chatUI, JukeBox jukebox) {
		this.mainUI = mainUI;
		this.chatUI = chatUI;
		this.jukebox = jukebox;
		init();
	}

	private void init() {
		Font.loadFont(getClass().getResource("arcade-normal.ttf").toString(), 0);

		game = new Game(WinScore);
		Group content = new Group();
		GameScreen gameScreen = new GameScreen(game);
		WelcomeScreen welcomeScreen = new WelcomeScreen();
		EndScreen endScreen = new EndScreen();
		content.getChildren().add(welcomeScreen);
		

		createColumnsandRows();

		root.setPrefSize(1200, 600);
		root.add(content, 6, 4, 24, 16);

		backButton.setId("logOutButton");
		root.add(backButton, 1, 21, 6, 2);

		// Adding an setting the button for mute and un-mute of login music
		soundButton = new Button();
		setSoundButtonImages();
		soundButton.setId("logOutButton");
		soundButton.setGraphic(playSoundImageView);
		root.add(soundButton, 32, 1);

		backButton.setFocusTraversable(false);
		soundButton.setFocusTraversable(false);

		root.setId("mainRoot");

		addActionListeners();

		/*
		 * Sk�rm byte.
		 */
		welcomeScreen.setOnStart(() -> {
			content.getChildren().clear();
			content.getChildren().add(gameScreen);
			gameScreen.requestFocus();
			game.start();
		});
		game.setOnGameEnd(() -> {
			content.getChildren().clear();
			content.getChildren().add(endScreen);
			endScreen.requestFocus();
			endScreen.setScore(game.getPlayer().getScore());
		});
		endScreen.setOnRestart(() -> {
			content.getChildren().clear();
			content.getChildren().add(gameScreen);
			gameScreen.requestFocus();
			game.start();
		});

		scene = new Scene(root, 1200, 600, Color.BLACK);
		try {
			scene.getStylesheets().add((new File("styles//pongStyle.css")).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		Scale scale = Transform.scale(1, 1, 0, 0);
		content.getTransforms().add(scale);

		/*
		 * The following listener is called whenever the scene is resized to update the
		 * scale and add letter- and pillarboxing.
		 */
//		InvalidationListener updateScale = value -> {
//			double scaleX = scene.getWidth() / WIDTH;
//			double scaleY = scene.getHeight() / HEIGHT;
//
//			if (scaleX < scaleY) {
//				/*
//				 * Letterboxing.
//				 */
//				scale.setX(scaleX);
//				scale.setY(scaleX);
//				double remainingHeight = scene.getHeight() - HEIGHT * scaleX;
//				content.setTranslateX(0);
//				content.setTranslateY(remainingHeight / 2);
//			} else if (scaleY < scaleX) {
//				/*
//				 * Pillarboxing.
//				 */
//				scale.setX(scaleY);
//				scale.setY(scaleY);
//				double remainingWidth = scene.getWidth() - WIDTH * scaleY;
//				content.setTranslateX(remainingWidth / 2);
//				content.setTranslateY(0);
//			} else {
//				/*
//				 * Regular scaling.
//				 */
//				scale.setX(scaleX);
//				scale.setY(scaleY);
//				content.setTranslateX(0);
//				content.setTranslateY(0);
//			}
//		};
//		scene.widthProperty().addListener(updateScale);
//		scene.heightProperty().addListener(updateScale);

		// content.requestFocus();
		welcomeScreen.setOnMouseMoved(e -> welcomeScreen.requestFocus());
		gameScreen.setOnMouseMoved(e -> gameScreen.requestFocus());
		endScreen.setOnMouseMoved(e -> endScreen.requestFocus());

		welcomeScreen.requestFocus(); /* This step is necessary to receive keyboard input. */
	}

	public Scene getScene() {
		root.add(chatUI, 36, 0, 12, 24);
		chatUI.setFocusTraversable(false);
		return this.scene;
	}

	private void createColumnsandRows() {

		for (int i = 0; i < numOfCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(100.0 / numOfCols);
			root.getColumnConstraints().add(colConst);
		}
		for (int i = 0; i < numOfRows; i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setPercentHeight(100.0 / numOfRows);
			root.getRowConstraints().add(rowConst);
		}
	}

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

	private void addActionListeners() {
		backButton.setOnAction(e -> {
			root.getChildren().remove(chatUI);
			game.endedGame();
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
	}
}
