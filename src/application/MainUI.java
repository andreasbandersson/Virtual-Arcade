package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.SwingUtilities;
import chat.ChatTestUI;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

	private Button leaderboardButton = new Button("  LEADERBOARD");
	private Button settingsButton = new Button("  SETTINGS");
	private Button pongPlayButton = new Button("  START");
	private Button spacePlayButton = new Button("  START");
	private Button snakePlayButton = new Button("START");
	private Image pong, space, snake;
	private ImageView pongView, spaceView, snakeView;
	private Scene scene2;
	private HBox hBox;
	private GridPane mainRoot;
	private ChatTestUI chatTestUI;
	private final int numOfCols = 48;
	private final int numOfRows = 24;

	public void start(Stage primaryStage) {
		mainRoot = new GridPane();
		mainRoot.setId("mainRoot");
		mainRoot.setPrefSize(1200.0, 600.0);
		//mainRoot.setGridLinesVisible(true);

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

		//Metod for adding the arcade machine pictures.
		setArcadeMachineImage();
		
		// Setting the HBox-node for the leaderbutton and settingsbutton.
		hBox = new HBox();
		hBox.setId("hBox");
		hBox.setPrefSize(900.0, 100.0);
		hBox.getChildren().add(leaderboardButton);
		hBox.getChildren().add(settingsButton);
		hBox.setSpacing(50.0);
		hBox.setAlignment(Pos.CENTER);

		leaderboardButton.setId("hBoxButtons");
		settingsButton.setId("hBoxButtons");
		mainRoot.add(hBox, 0, 20, 36, 4);
		
		pongPlayButton.setId("arcadeButtons");
		mainRoot.add(pongPlayButton, 5, 12, 4, 2);
		
		spacePlayButton.setId("arcadeButtons");
		mainRoot.add(spacePlayButton, 15, 15, 4, 2);
		
		snakePlayButton.setId("nokiaButton");
		mainRoot.add(snakePlayButton, 25, 15, 4, 1);

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

	private void createSwingContent(final SwingNode swingNode) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				swingNode.setContent(chatTestUI);
				swingNode.requestFocus();

				chatTestUI.addNewMessage("Use @username to send a private message");
				chatTestUI.addNewMessage("Example: @Aragorn This is a message");
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
		mainRoot.add(pongView, 2, 12);
		
		spaceView.setFitWidth(250);
		spaceView.setPreserveRatio(true);
		mainRoot.add(spaceView, 12, 12);
		
		snakeView.setFitWidth(250);
		snakeView.setPreserveRatio(true);
		mainRoot.add(snakeView, 22, 12);

	}

	public static void main(String[] args) {
		launch(args);
	}
}
