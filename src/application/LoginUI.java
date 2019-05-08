package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import chat.ChatController;
import javafx.application.Application;
import javafx.application.Platform;
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
	private Button newUserButton = new Button("CREATE USER");
	private GridPane loginRoot;
	private Scene scene;
	private Button soundButton;
	private JukeBox jukebox;
	private Label responseLabel;
	private TextField username = new TextField();
	private PasswordField password = new PasswordField();
	public static Stage stage = new Stage();
	private ChatController controller;
	
	private final int numOfCols = 48;
	private final int numOfRows = 24;
	
	public LoginUI (ChatController controller) {
		this.controller = controller;
	}

	public void start(Stage primaryStage) {
		
		// Setting the main Pane for the scene.
		loginRoot = new GridPane();
		loginRoot.setId("loginRoot");

		createColumnsandRows();
		setSoundButtonImages();
		
		jukebox = new JukeBox("sounds/Login-Sound-1.mp3");
		jukebox.play();


		// Adding and setting the Label for Virtual Arcade-header
		Label virtualArcadeLabel = new Label("VIRTUAL");
		Glow glow = new Glow(1.0);
		Bloom bloom = new Bloom(0.9);
		virtualArcadeLabel.setId("vaLabel");
		virtualArcadeLabel.setEffect(bloom);
		virtualArcadeLabel.setEffect(glow);
		loginRoot.add(virtualArcadeLabel, 1, 1, 15, 4);
		
		Label virtualArcadeLabel2 = new Label("ARCADE");
		virtualArcadeLabel2.setId("vaLabel");
		virtualArcadeLabel2.setEffect(bloom);
		virtualArcadeLabel2.setEffect(glow);
		loginRoot.add(virtualArcadeLabel2, 1, 5, 15, 4);

		// Adding and setting the Labels and TextFields for the login
		Label userNameLabel = new Label("Username:");
		loginRoot.add(userNameLabel, 18, 8, 10, 1);

		username.setPromptText("Enter a username");
		loginRoot.add(username, 18, 9, 10, 2);

		Label passwordLabel = new Label("Password:");
		loginRoot.add(passwordLabel, 18, 11, 10, 1);

		password.setPromptText("Enter a password");
		loginRoot.add(password, 18, 12, 10, 2);
		
		responseLabel = new Label();
		responseLabel.setWrapText(true);
		responseLabel.setId("responseLabel");
		loginRoot.add(responseLabel, 18, 15, 10, 3 );
		
		// Adding and setting the button for the login
		loginButton = new Button(" LOGIN ");
		loginButton.setId("loginButton");
		loginRoot.add(loginButton, 16, 20, 9, 1);

		// Adding and setting the button for creating a new user
		newUserButton.setId("logOutButton");
		loginRoot.add(newUserButton, 24, 20, 9, 1);
		
		//Adding an setting the button for mute and un-mute of login music
		soundButton = new Button();
		soundButton.setId("logOutButton");
		soundButton.setGraphic(muteSoundImageView);
		loginRoot.add(soundButton, 42, 2);
		
		addActionListeners(primaryStage);

		// Sets the scene, adds all children nodes and sets the css-style.
		scene = new Scene(loginRoot, 700, 400);
		
		try {
			scene.getStylesheets().add((new File("Virtual-Arcade\\styles\\loginStyle.css")).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		// Sets the primaryStage
		primaryStage.setTitle("VIRTUAL ARCADE");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();
		stage = primaryStage;
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
	
	// Måns
	public void setResponse (String response) {
		Platform.runLater(new Runnable() {
			public void run() {
				responseLabel.setText(response);
				disableButtons(false);
			}
		});
	}
	
	// Måns
	public void terminate() {
		stage.close();
		jukebox.stopSound();
		try {
			this.stop();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	// Måns
	private void disableButtons(boolean disabled) {
		loginButton.setDisable(disabled);
		newUserButton.setDisable(disabled);
	}
	

	
	//Function for adding and setting Action Listeners to all Buttons.
	private void addActionListeners(Stage primaryStage) {
		// Måns
		loginButton.setOnAction(e -> {
			controller.login(username.getText(), password.getText());
			disableButtons(true);
		});
		// Måns
		newUserButton.setOnAction(e -> {
			controller.newUser(username.getText(), password.getText());
			disableButtons(true);
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
