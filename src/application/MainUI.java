package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.SwingUtilities;

import chat.ChatController;
import chat.ChatTestUI;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

/**
 * @author Andreas Andersson
 */

public class MainUI extends Application {

	private Button leaderboardButton = new Button("LEADERBOARD");
	private Button settingsButton = new Button("SETTINGS");
	private Button logOutButton = new Button("LOG OUT");
	private Button pongPlayButton = new Button("START");
	private Button spacePlayButton = new Button("START");
	private Button snakePlayButton = new Button("START");
	private Image pong, space, snake;
	private ImageView pongView, spaceView, snakeView;
	private Scene scene2, pongScene;
	private GridPane mainRoot;
	private ChatTestUI chatTestUI = new ChatTestUI(new ChatController());
	private Label headerLabel = new Label("CHOOSE YOUR GAME"); 
	private Label virtualArcadeLabel = new Label("VIRTUAL\nARCADE");
	private Glow glow = new Glow(1.0);
	private Bloom bloom = new Bloom(0.9);


	private final int numOfCols = 48;
	private final int numOfRows = 24;

	public void start(Stage primaryStage) {
		mainRoot = new GridPane();
		mainRoot.setId("mainRoot");
		mainRoot.setPrefSize(1200.0, 600.0);
		//mainRoot.setGridLinesVisible(true);
		
		createColumnsandRows();
		
		//Adding and setting the Label for Virtual Arcade-header
		virtualArcadeLabel.setId("vaLabel");
		virtualArcadeLabel.setEffect(bloom);
		virtualArcadeLabel.setEffect(glow);
        mainRoot.add(virtualArcadeLabel, 1, 0, 10, 6);
        
        //Adding and setting the Label for the "Choose game"-header
		headerLabel.setId("headerLabel");
		FadeTransition fadeTransitionH = new FadeTransition(Duration.seconds(2.0), headerLabel);
        fadeTransitionH.setFromValue(1.0);
        fadeTransitionH.setToValue(0.2);
        fadeTransitionH.setCycleCount(Animation.INDEFINITE);
        fadeTransitionH.play();
		mainRoot.add(headerLabel, 9, 7, 20, 4);
		
		//Function for adding the arcade machine pictures.
		setArcadeMachineImage();
		
		//ADdin and setting the mainbuttons
		leaderboardButton.setId("mainButtons");
		leaderboardButton.setOnAction(e -> new Leaderboard().start(primaryStage));
		settingsButton.setId("mainButtons");
		mainRoot.add(leaderboardButton, 25, 0, 6, 3);
		mainRoot.add(settingsButton, 31, 0, 5, 3);
		logOutButton.setId("logOutButton");
		logOutButton.setOnAction(e -> {new LoginUI().start(primaryStage); });
		mainRoot.add(logOutButton, 19, 0, 6, 3);
		
		//Adding and setting the buttons for the different arcade games
		pongPlayButton.setId("arcadeButtons");
		pongPlayButton.setOnAction(e -> new Pong().start(primaryStage));
		mainRoot.add(pongPlayButton, 5, 17, 4, 2);
		spacePlayButton.setId("arcadeButtons");
		spacePlayButton.setOnAction(e -> new SpaceInvaders().start(primaryStage));
		mainRoot.add(spacePlayButton, 15, 20, 4, 2);
		snakePlayButton.setId("nokiaButton");
		snakePlayButton.setOnAction(e -> new Snake().start(primaryStage));
		mainRoot.add(snakePlayButton, 25, 20, 4, 1);

		//Setting the chatt
		SwingNode chatUI = new SwingNode();
		createSwingContent(chatUI);
		StackPane pane = new StackPane();
		pane.setId("swingPane");
		pane.setPrefSize(300.0, 600.0);
		pane.getChildren().add(chatUI);
		mainRoot.add(pane, 36, 0, 12, 24);
		
		//Sets the scene, adds all children nodes and sets the css style.
		scene2 = new Scene(mainRoot, 1200, 600);
		scene2.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
		
		//Sets the primaryStage
		primaryStage.setTitle("VIRTUAL ARCADE");
		//primaryStage.setFullScreen(true);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene2);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}
	
	private void createColumnsandRows() {
		// Sets the number and size-percentage of the rows and columns in the GridPane.
				for (int i = 0; i < numOfCols; i++) {
					ColumnConstraints colConst = new ColumnConstraints();
					colConst.setPercentWidth(100.0 / numOfCols);
					mainRoot.getColumnConstraints().add(colConst);
				}
				for (int i = 0; i < numOfRows; i++) {
					RowConstraints rowConst = new RowConstraints();
					rowConst.setPercentHeight(100.0 / numOfRows);
					mainRoot.getRowConstraints().add(rowConst);
				}
	}

	private void createSwingContent(final SwingNode swingNode) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				swingNode.setContent(chatTestUI);
				swingNode.requestFocus();
			}
		});
	}

	public void setArcadeMachineImage() {
		try {
			pong = new Image(new FileInputStream("images/pongGame.png"));
			space = new Image(new FileInputStream("images/spaceGame.png"));
			snake = new Image(new FileInputStream("images/snakeGame.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pongView = new ImageView(pong);
		spaceView = new ImageView(space);
		snakeView = new ImageView(snake);
		
		pongView.setFitWidth(250);
		pongView.setPreserveRatio(true);
		mainRoot.add(pongView, 2, 17);
		
		spaceView.setFitWidth(250);
		spaceView.setPreserveRatio(true);
		mainRoot.add(spaceView, 12, 17);
		
		snakeView.setFitWidth(250);
		snakeView.setPreserveRatio(true);
		mainRoot.add(snakeView, 22, 17);

	}
	
	public void goToLeaderboard(Stage primarystage) {
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
