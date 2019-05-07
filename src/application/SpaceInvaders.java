package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.SwingUtilities;
import chat.ChatController;
import chat.ChatTestUI;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.stage.Stage;
import spaceInvaders.graphics.GameFrame;
import spaceInvaders.logic.Controller;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

/**
 * @author Andreas Andersson
 */

public class SpaceInvaders extends Application {

	private Button backButton = new Button("BACK");
	private Image spaceInvadersImage;
	private ImageView spaceInvadersView;
	private Scene scene;
	private GridPane spaceInvadersRoot;
	private ChatTestUI chatTestUI = new ChatTestUI(new ChatController());
	//private GameFrame spaceInvadersGame = new GameFrame(new Controller());

	private final int numOfCols = 48;
	private final int numOfRows = 24;

	public void start(Stage primaryStage) {
		spaceInvadersRoot = new GridPane();
		spaceInvadersRoot.setId("leaderboardRoot");
		spaceInvadersRoot.setPrefSize(1200.0, 600.0);
		// mainRoot.setGridLinesVisible(true);

		createColumnsandRows();		
		setArcadeMachineImage();

		// Adding and setting the main buttons
		backButton.setId("logOutButton");
		backButton.setOnAction(e -> {
			new MainUI().start(primaryStage);
		});
		spaceInvadersRoot.add(backButton, 1, 21, 6, 2);

		// Setting the chat
		SwingNode chatUI = new SwingNode();
		createSwingContent(chatUI);
		StackPane pane = new StackPane();
		pane.setId("swingPane");
		pane.setPrefSize(300.0, 600.0);
		pane.getChildren().add(chatUI);
		spaceInvadersRoot.add(pane, 36, 0, 12, 24);
		
		SwingNode snakeGame = new SwingNode();
		//createSpaceInvadersSwingContent(snakeGame);
		StackPane snakePane = new StackPane();
		snakePane.setId("swingPane");
		snakePane.setPrefSize(600.0, 600.0);
		snakePane.getChildren().add(snakeGame);
		spaceInvadersRoot.add(snakePane, 6, 0, 24, 24);
		

		// Sets the scene, adds all children nodes and sets the css style.
		scene = new Scene(spaceInvadersRoot, 1200, 600);
		scene.getStylesheets().addAll(this.getClass().getResource("styles/style.css").toExternalForm());

		// Sets the primaryStage
		primaryStage.setTitle("VIRTUAL ARCADE");
		// primaryStage.setFullScreen(true);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	// Sets the number and size-percentage of the rows and columns in the GridPane.
	private void createColumnsandRows() {

		for (int i = 0; i < numOfCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(100.0 / numOfCols);
			spaceInvadersRoot.getColumnConstraints().add(colConst);
		}
		for (int i = 0; i < numOfRows; i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setPercentHeight(100.0 / numOfRows);
			spaceInvadersRoot.getRowConstraints().add(rowConst);
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
	
//	private void createSpaceInvadersSwingContent(final SwingNode swingNode) {
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//
//				swingNode.setContent(spaceInvadersGame);
//				swingNode.requestFocus();
//			}
//		});
//	}
	
	//Sets and adds the arcade machine image for the Pong game. 
	public void setArcadeMachineImage() {
		try {
			spaceInvadersImage = new Image(new FileInputStream("images/spaceScreen.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		spaceInvadersView = new ImageView(spaceInvadersImage);		
	//	pongView.setFitWidth(900);
		spaceInvadersView.setPreserveRatio(true);
		spaceInvadersRoot.add(spaceInvadersView, 0, 14);

	}

	public static void main(String[] args) {
		launch(args);
	}
}
