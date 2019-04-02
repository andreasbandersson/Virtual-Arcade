package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * @author Andreas Andersson
 */

public class LoginUI extends Application {
	private Button loginButton = new Button("  START");
	private BorderPane loginRoot;
	private Scene scene;
	private GridPane gridPane;
	private Label userNameLabel = new Label("Username:");
	private Label passwordLabel = new Label("Password:");
	private Label startLabel = new Label("Press Start Button");
	private TextField usernameTextField = new TextField();
	private PasswordField passwordTextField = new PasswordField();

	public void start(Stage primaryStage) {
		loginRoot = new BorderPane();
		loginRoot.setId("loginRoot");

		gridPane = new GridPane();

		userNameLabel.setPadding(new Insets(80.0, 0.0, 0.0, 0.0));
		gridPane.add(userNameLabel, 0, 1);
		gridPane.add(usernameTextField, 0, 2);
		usernameTextField.setPromptText("Enter a username");
		passwordLabel.setPadding(new Insets(5.0, 0.0, 0.0, 0.0));
		gridPane.add(passwordLabel, 0, 4);
		gridPane.add(passwordTextField, 0, 5);
		passwordTextField.setPromptText("Enter a password");
		startLabel.setId("startLabel");
		startLabel.setPadding(new Insets(25.0, 0.0, 0.0, 0.0));
		gridPane.add(startLabel, 0, 7);
		gridPane.add(loginButton, 0, 10);
		GridPane.setConstraints(loginButton, 0, 10, 1, 1, null, null, Priority.NEVER, Priority.NEVER,
				new Insets(30.0, 0.0, 0.0, 24.0));
		loginButton.setId("hBoxButtons");

		gridPane.setAlignment(Pos.CENTER);

		loginButton.setOnAction(e -> new MainUI().start(primaryStage));

		loginRoot.setCenter(gridPane);
		BorderPane.setAlignment(gridPane, Pos.CENTER);

		scene = new Scene(loginRoot, 700, 400);
		scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());

		primaryStage.setTitle("VIRTUAL ARCADE");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
