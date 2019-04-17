package application;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.nio.file.Paths;

/**
 * @author Andreas Andersson
 */

public class LoginUI extends Application {
	private Button loginButton = new Button("LOGIN");
	private Button newUserButton = new Button("REGISTER");
	private GridPane loginRoot;
	private Scene scene;
	private Label userNameLabel = new Label("Username:");
	private Label passwordLabel = new Label("Password:");
	private Label virtualArcadeLabel = new Label("VIRTUAL\nARCADE");
	private TextField usernameTextField = new TextField();
	private PasswordField passwordTextField = new PasswordField();
	private Glow glow = new Glow(1.0);
	private Bloom bloom = new Bloom(0.9);
	private Media media;

	private final int numOfCols = 48;
	private final int numOfRows = 24;

	public void start(Stage primaryStage) {
		loginRoot = new GridPane();
		loginRoot.setId("loginRoot");
		// loginRoot.setGridLinesVisible(true);

		// Sets the number and size-percentage of the rows and columns in the GridPane.
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

		virtualArcadeLabel.setId("vaLabel");
		virtualArcadeLabel.setEffect(bloom);
		virtualArcadeLabel.setEffect(glow);
		loginRoot.add(virtualArcadeLabel, 1, 0, 15, 9);

		usernameTextField.setPromptText("Enter a username");
		loginRoot.add(userNameLabel, 18, 8, 10, 1);
		loginRoot.add(usernameTextField, 18, 9, 10, 2);

		passwordTextField.setPromptText("Enter a password");
		loginRoot.add(passwordLabel, 18, 11, 10, 1);
		loginRoot.add(passwordTextField, 18, 12, 10, 2);

		loginButton.setId("loginButton");
		loginRoot.add(loginButton, 20, 16, 9, 1);
		newUserButton.setId("logOutButton");
		loginRoot.add(newUserButton, 19, 20, 9, 1);

		loginButton.setOnAction(e -> new MainUI().start(primaryStage));

		scene = new Scene(loginRoot, 700, 400);
		scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());

		primaryStage.setTitle("VIRTUAL ARCADE");
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();
		//startSound();

	}

	public void startSound() {
		media = new Media("sounds/Knight-Rider-Theme-Song.mp3");
		MediaPlayer player = new MediaPlayer(media);
		MediaView mediaView = new MediaView(player);
		loginRoot.add(mediaView, 22, 38);
		player.play();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
