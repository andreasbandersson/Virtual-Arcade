package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import chat.ClientController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The LoginUI class is a graphical user interface for the login screen to Virtual Arcade.
 * 
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
	private ClientController controller;

	/**
	 * The constructor takes a controller.
	 * 
	 * @param controller the controller parameter is used to login or create a new user.
	 */
	public LoginUI(ClientController controller) {
		this.controller = controller;
	}
	
	/**
	 * Initiates the graphics, styles, music and action listeners.
	 */
	public void start(Stage primaryStage) {

		// Setting the main Pane for the scene.
		loginRoot = new GridPane();
		loginRoot.setId("loginRoot");

		createColumnsandRows();

		//initiating the jukebox and giving it the login music.
		jukebox = new JukeBox("sounds/Login-Sound-1.mp3");
		jukebox.play();

		// Adding and setting the Label for Virtual Arcade-header
		Glow glow = new Glow(0.2);
		Bloom bloom = new Bloom(0.2);
		Label virtualArcadeLabel = new Label("VIRTUAL");
		virtualArcadeLabel.setId("vaLabel");
		virtualArcadeLabel.setEffect(bloom);
		virtualArcadeLabel.setEffect(glow);
		virtualArcadeLabel.setMinWidth(Region.USE_PREF_SIZE);
		loginRoot.add(virtualArcadeLabel, 1, 1, 15, 3);

		Label virtualArcadeLabel2 = new Label("ARCADE");
		virtualArcadeLabel2.setId("vaLabel");
		virtualArcadeLabel2.setEffect(bloom);
		virtualArcadeLabel2.setEffect(glow);
		virtualArcadeLabel2.setMinWidth(Region.USE_PREF_SIZE);
		loginRoot.add(virtualArcadeLabel2, 1, 4, 15, 3);

		// Adding and setting the Labels and TextFields for the login
		Label userNameLabel = new Label("Username:");
		loginRoot.add(userNameLabel, 18, 8, 10, 1);

		username.setPromptText("Min. 3 characters");
		loginRoot.add(username, 18, 9, 10, 2);

		Label passwordLabel = new Label("Password:");
		loginRoot.add(passwordLabel, 18, 11, 10, 1);

		password.setPromptText("Min. 6 characters");
		loginRoot.add(password, 18, 12, 10, 2);

		responseLabel = new Label();
		responseLabel.setWrapText(true);
		responseLabel.setId("responseLabel");

		loginRoot.add(responseLabel, 18, 15, 10, 3 );
		responseLabel.setMinWidth(Region.USE_PREF_SIZE);

		// Adding and setting the button for the login
		loginButton = new Button(" LOGIN ");
		loginButton.setId("loginButton");
		loginButton.setDefaultButton(true);
		loginRoot.add(loginButton, 16, 20, 9, 1);

		// Adding and setting the button for creating a new user
		newUserButton.setId("blueButton");
		newUserButton.setMinWidth(Region.USE_PREF_SIZE);
		loginRoot.add(newUserButton, 24, 20, 9, 1);

		// Adding an setting the button for mute and un-mute of login music
		soundButton = new Button();
		soundButton.setId("greyButton");
		soundButton.setGraphic(playSoundImageView);
		loginRoot.add(soundButton, 42, 2);

		addActionListeners(primaryStage);
		setSoundButtonImages();
		checkSound();

		// Sets the scene, adds all children nodes and sets the css-style.
		scene = new Scene(loginRoot, 700, 400);
		try {
			scene.getStylesheets().add((new File("styles//loginStyle.css")).toURI().toURL().toExternalForm());
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
		primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	/**
	 * Sets the number and size-percentage of the rows and columns in the GridPane.
	 */
	private void createColumnsandRows() {
		final int numOfCols = 48;
		final int numOfRows = 24;

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
	 * If it is paused the sound buttons symbol should show the correct symbol.
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
	 * A function that is called upon if a user or password is too short.
	 * 
	 * @param response the response 
	 */
	public void setResponse(String response) {
		Platform.runLater(new Runnable() {
			public void run() {
				responseLabel.setText(response);
				disableButtons(false);
			}
		});
	}

	/**
	 * A function that terminates the current MainUI object and stops the sound so that the sound wont overlap.
	 */
	public void terminate() {
		stage.close();
		jukebox.stopSound();
		try {
			this.stop();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Disables or enables the login and create new user button 
	 * 
	 * @param disabled the disabled parameter is set to either true or false depending on the login information
	 */
	private void disableButtons(boolean disabled) {
		loginButton.setDisable(disabled);
		newUserButton.setDisable(disabled);
	}
	
	/** 
	 * Function for adding and setting Action Listeners to all Buttons.
	 * 
	 * @param primaryStage the stage parameter is used so that everything is displayed in the same stage and not opened in a separate stage.
	 */
	private void addActionListeners(Stage primaryStage) {
		// Mans
		loginButton.setOnAction(e -> {
			controller.login(username.getText(), password.getText());
			disableButtons(true);
		});
		// Mans
		newUserButton.setOnAction(e -> {
			controller.newUser(username.getText(), password.getText());
			disableButtons(true);
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