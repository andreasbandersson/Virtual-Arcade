package application;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.SwingUtilities;

import chat.ChatController;
import chat.ChatTestUI;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

/**
 * @author Andreas Andersson
 */

public class Leaderboard extends Application {

	private Button leaderboardButton = new Button("LEADERBOARD");
	private Button settingsButton = new Button("SETTINGS");
	private Button backButton = new Button("BACK");
	private Button pongPlayButton = new Button("START");
	private Button spacePlayButton = new Button("START");
	private Button snakePlayButton = new Button("START");
	private Image pong, space, snake;
	private ImageView pongView, spaceView, snakeView;
	private Scene scene2, scene3;
	private GridPane leaderboardRoot;
	private ChatTestUI chatTestUI = new ChatTestUI(new ChatController());
	private Label headerLabel = new Label("LEADERBOARD");
	private Glow glow = new Glow(1.0);
	private Bloom bloom = new Bloom(0.9);

	private final int numOfCols = 48;
	private final int numOfRows = 24;

	public void start(Stage primaryStage) {
		leaderboardRoot = new GridPane();
		leaderboardRoot.setId("leaderboardRoot");
		leaderboardRoot.setPrefSize(1200.0, 600.0);
		// mainRoot.setGridLinesVisible(true);

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
		backButton.setOnAction(e -> {
			new MainUI().start(primaryStage);
		});
		leaderboardRoot.add(backButton, 1, 21, 6, 2);

		// Setting the chat
		SwingNode chatUI = new SwingNode();
		createSwingContent(chatUI);
		StackPane pane = new StackPane();
		pane.setId("swingPane");
		pane.setPrefSize(300.0, 600.0);
		pane.getChildren().add(chatUI);
		leaderboardRoot.add(pane, 36, 0, 12, 24);

		addHighscoreLabels();

		// Sets the scene, adds all children nodes and sets the css style.
		scene2 = new Scene(leaderboardRoot, 1200, 600);
		scene2.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());

		// Sets the primaryStage
		primaryStage.setTitle("VIRTUAL ARCADE");
		// primaryStage.setFullScreen(true);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene2);
		primaryStage.centerOnScreen();
		primaryStage.show();
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

	// Adds the chat to the GridPane
	private void createSwingContent(final SwingNode swingNode) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				swingNode.setContent(chatTestUI);
				swingNode.requestFocus();
			}
		});
	}

	public void addHighscoreLabels() {
		Label pongHighscoreLabel = new Label();
		String highscoreString = "";
		pongHighscoreLabel.setId("highscoreLabel");
		pongHighscoreLabel.setPrefSize(300.0, 300.00);
		pongHighscoreLabel.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
		for (int i = 0; i < 10; i++) {
			highscoreString += i + 1 + " Andreas\n";
			pongHighscoreLabel.setText("PONG HIGHSCORES\n" + highscoreString);
		}
		leaderboardRoot.add(pongHighscoreLabel, 4, 6, 10, 10);

	}

	public void goToLeaderboard(Stage primarystage) {

	}

	public static void main(String[] args) {
		launch(args);
	}
}
