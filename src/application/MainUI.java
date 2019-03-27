package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;

public class MainUI extends Application {

	private Button leaderboardButton = new Button("  LEADERBOARD");
	private Button settingsButton = new Button("  SETTINGS");
	private Button loginButton = new Button("  START");
	private Image pong;
	private ImageView pongView;
	private BorderPane mainRoot, loginRoot;
	private Scene scene, scene2;
	private HBox hBox;
	private GridPane gridPane;
	private User user;
	private Label userNameLabel = new Label("Username:");
	private Label passwordLabel = new Label("Password:");
	private Label startLabel = new Label("Press Start Button");
	private TextField usernameTextField = new TextField();
	private PasswordField passwordTextField = new PasswordField();
	private TextArea textArea = new TextArea();

	@Override
	public void start(Stage primaryStage) {
		setLoginScene(primaryStage);
	}

	public void setLoginScene(Stage primaryStage) {
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
		GridPane.setConstraints(loginButton, 0, 10, 1, 1, null, null, Priority.NEVER, Priority.NEVER, new Insets(30.0, 0.0, 0.0, 24.0));
		loginButton.setId("hBoxButtons");
		
		gridPane.setAlignment(Pos.CENTER);
		
		loginButton.setOnAction(e -> setMainMenuScene(primaryStage));

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

	public void setMainMenuScene(Stage primaryStage) {
		mainRoot = new BorderPane();
		mainRoot.setId("mainRoot");

		hBox = new HBox();
		hBox.setId("hBox");
		hBox.setPrefSize(1200.0, 100.0);
		hBox.getChildren().add(leaderboardButton);
		hBox.getChildren().add(settingsButton);
		hBox.setSpacing(50.0);
		hBox.setAlignment(Pos.BASELINE_CENTER);

		leaderboardButton.setId("hBoxButtons");
		settingsButton.setId("hBoxButtons");
		settingsButton.setTextAlignment(TextAlignment.CENTER);
		textArea.setPrefSize(300, 500);

		mainRoot.setBottom(hBox);
		mainRoot.setRight(textArea);
		
		setArcadeMachineImage();
		
		scene2 = new Scene(mainRoot, 1200, 600);
		scene2.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
		// primaryStage.setResizable(false);

		primaryStage.setTitle("VIRTUAL ARCADE");
		primaryStage.setResizable(true);
		primaryStage.setScene(scene2);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	public void setArcadeMachineImage() {
		try {
			pong = new Image(new FileInputStream("images/arcadeGreen.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pongView = new ImageView(pong);

		pongView.setFitWidth(250);
		pongView.setPreserveRatio(true);
		mainRoot.setCenter(pongView);
		BorderPane.setAlignment(pongView, Pos.CENTER_LEFT);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
