package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;

/**
 * @author Andreas Andersson
 */

public class LoginUI extends Application {
	private Image playSoundImage;
	private Image muteSoundImage;
	private ImageView playSoundImageView;
	private ImageView muteSoundImageView;
	private Button loginButton;
	private GridPane loginRoot;
	private Scene scene;
	private Button soundButton;
	private MainUI mainMenu;
	private JukeBox jukebox;

	private final int numOfCols = 48;
	private final int numOfRows = 24;

	public void start(Stage primaryStage) {
		
		// Setting the main Pane for the scene.
		loginRoot = new GridPane();
		loginRoot.setId("loginRoot");

		createColumnsandRows();
		setSoundButtonImages();
		
		jukebox = new JukeBox("sounds/Login-Sound-1.mp3");
		jukebox.play();


		// Adding and setting the Label for Virtual Arcade-header
		Label virtualArcadeLabel = new Label("VIRTUAL\nARCADE");
		Glow glow = new Glow(1.0);
		Bloom bloom = new Bloom(0.9);
		virtualArcadeLabel.setId("vaLabel");
		virtualArcadeLabel.setEffect(bloom);
		virtualArcadeLabel.setEffect(glow);
		loginRoot.add(virtualArcadeLabel, 1, 0, 15, 9);

		// Adding and setting the Labels and TextFields for the login
		Label userNameLabel = new Label("Username:");
		loginRoot.add(userNameLabel, 18, 8, 10, 1);

		TextField usernameTextField = new TextField();
		usernameTextField.setPromptText("Enter a username");
		loginRoot.add(usernameTextField, 18, 9, 10, 2);

		Label passwordLabel = new Label("Password:");
		loginRoot.add(passwordLabel, 18, 11, 10, 1);

		PasswordField passwordTextField = new PasswordField();
		passwordTextField.setPromptText("Enter a password");
		loginRoot.add(passwordTextField, 18, 12, 10, 2);

		// Adding and setting the button for the login
		loginButton = new Button("LOGIN");
		loginButton.setId("loginButton");
		loginRoot.add(loginButton, 20, 16, 9, 1);

		// Adding and setting the button for creating a new user
		Button newUserButton = new Button("CREATE NEW USER");
		newUserButton.setId("logOutButton");
		loginRoot.add(newUserButton, 17, 20, 14, 1);
		
		//Adding an setting the button for mute and un-mute of login music
		soundButton = new Button();
		soundButton.setId("logOutButton");
		soundButton.setGraphic(muteSoundImageView);
		loginRoot.add(soundButton, 42, 2);
		
		addActionListeners(primaryStage);

		// Sets the scene, adds all children nodes and sets the css-style.
		scene = new Scene(loginRoot, 700, 400);
		scene.getStylesheets().addAll(this.getClass().getResource("styles/style.css").toExternalForm());
		
		// Sets the primaryStage
		primaryStage.setTitle("VIRTUAL ARCADE");
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	// Sets the number and size-percentage of the rows and columns in the GridPane.
	private void createColumnsandRows() {
		for (int i = 0; i < numOfCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(100.0 / numOfCols);
			loginRoot.getColumnConstraints().add(colConst);
		}
		for (int i = 0; i < numOfRows; i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setPercentHeight(100.0 / numOfRows);
			loginRoot.getRowConstraints().add(rowConst);
		}
	}

	
	//Sets the sound buttons images.
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
		
		loginButton.setOnAction(e -> {
			mainMenu = new MainUI();
			mainMenu.start(primaryStage);
			jukebox.stopSound();
			try {
				this.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		soundButton.setOnAction(e -> {
			jukebox.muteUnmute();
			if(jukebox.isMute()) {
				soundButton.setGraphic(playSoundImageView);
			}else {
				soundButton.setGraphic(muteSoundImageView);
			}
		});
		
	}

	// The main-method.
	public static void main(String[] args) {
		launch(args);
	}
}
