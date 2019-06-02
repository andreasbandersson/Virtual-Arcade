package pong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import application.JukeBox;
import application.MainUI;
import chat.ClientController;
import chat.ChatUI;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

/**
 * The class integrates the Pong game with the MainUI and adds the Chat and the Jukebox.
 * 
 * @author Andreas Andersson & M�ns Grundberg
 */

public class Pong {
	private MainUI mainUI;
	private ChatUI chatUI;
	private Scene scene;
	private Button backButton = new Button("BACK");
	private Button soundButton;
	private GridPane root = new GridPane();
	private Image muteSoundImage;
	private Image playSoundImage;
	private ImageView muteSoundImageView;
	private ImageView playSoundImageView;
	private Image pongCabinettImage;
	private JukeBox jukebox;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 400;
	private Game game;
	private Canvas canvas;
	private ClientController controller;


	/**
	 * The constructor receives the mainUI, chatUI, jukebox, chat controller and initiates the class.
	 * @param mainUI the mainUI parameter lets the class switch back to the same main menu.
	 * @param chatUI the chatUI parameter shows the chat in the class.
	 * @param jukebox the jukebox parameter lets the music play without overlaps when switching between classes.
	 * @param controller the controller parameter holds the logic for the chat
	 */
	public Pong(MainUI mainUI, ChatUI chatUI, JukeBox jukebox, ClientController controller) {
		this.mainUI = mainUI;
		this.chatUI = chatUI;
		this.jukebox = jukebox;
		this.controller = controller;
		init();
	}
	/**
	 * initiates the structure, graphics, game, styles, music and adds actionslisteners etc.
	 */
	private void init() {
		game = new Game(controller);
		createColumnsandRows();
		setPongArcadeMachineImage();
		
		Pane pane = new Pane();
		pane.setPrefSize(600, 400);
		canvas = game.getCanvas();

		pane.getChildren().add(canvas);

		pane.setId("Pong");

		root.setPrefSize(1200, 600);
		root.add(pane, 6, 4, 24, 16);

		backButton.setId("greyButton");
		root.add(backButton, 1, 21, 6, 2);

		// Adding an setting the button for mute and un-mute of login music
		soundButton = new Button();
		setSoundButtonImages();
		soundButton.setId("greyButton");
		checkSound();
		root.add(soundButton, 32, 1);

		backButton.setFocusTraversable(false);
		soundButton.setFocusTraversable(false);

		root.setId("mainRoot");

		addActionListeners();
		
		// Sets the scenes styles
		scene = new Scene(root, 1200, 600, Color.BLACK);
		try {
			scene.getStylesheets().add((new File("styles//pongStyle.css")).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		backButton.setFocusTraversable(false);
		soundButton.setFocusTraversable(false);

		canvas.requestFocus();
		canvas.setOnMouseMoved(e -> canvas.requestFocus());
	}

	public Scene getScene() {
		root.add(chatUI, 36, 0, 12, 24);
		chatUI.setFocusTraversable(false);
		return this.scene;
	}
	
	/**
	 * Sets the number and size-percentage of the rows and columns in the GridPane.
	 * @author Andreas Andersson
	 */
	private void createColumnsandRows() {

		final int numOfCols = 48;
		final int numOfRows = 24;
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

	/** 
	 * Sets the sound buttons images.
	 * @author Andreas Andersson
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
	 * @author Andreas Andersson
	 */
	public void checkSound() {
		if (jukebox.isPaused()) {
			soundButton.setGraphic(muteSoundImageView);
		} else {
			soundButton.setGraphic(playSoundImageView);
		}
	}

	/**
	 *  Sets and adds the arcade machine image for the Pong game.
	 *  @author Andreas Andersson
	 */
	public void setPongArcadeMachineImage() {
		ImageView pongCabinettView;
		try {
			pongCabinettImage = new Image(new FileInputStream("images/pongScreen3.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pongCabinettView = new ImageView(pongCabinettImage);
		pongCabinettView.setPreserveRatio(true);
		root.add(pongCabinettView, 0, 14);
	}

	/**
	 * Adds and sets the action listeners for the sound and back button.
	 * @author Andreas Andersson
	 */
	private void addActionListeners() {
		backButton.setOnAction(e -> {
			game.setPaused();
			root.getChildren().remove(chatUI);
			mainUI.switchToMainUI();
		});

		soundButton.setOnAction(e -> {
			jukebox.pauseOrPlay();
			if (jukebox.isPaused()) {
				soundButton.setGraphic(muteSoundImageView);
			} else {
				soundButton.setGraphic(playSoundImageView);
			}
		});
	}

}
