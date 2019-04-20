package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.SwingUtilities;
import chat.ChatController;
import chat.ChatTestUI;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.stage.Stage;
import pong.Game;
import pong.GameFrame;
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

public class Pong extends Application {

	private Button backButton = new Button("BACK");
	private Image pong;
	private ImageView pongView;
	private Scene scene2;
	private GridPane pongRoot;
	private ChatTestUI chatTestUI = new ChatTestUI(new ChatController());
	private GameFrame gFrame = new GameFrame(new Game());

	private final int numOfCols = 48;
	private final int numOfRows = 24;

	public void start(Stage primaryStage) {
		pongRoot = new GridPane();
		pongRoot.setId("leaderboardRoot");
		pongRoot.setPrefSize(1200.0, 600.0);
		// mainRoot.setGridLinesVisible(true);

		createColumnsandRows();		
		setArcadeMachineImage();

		// Adding and setting the main buttons
		backButton.setId("logOutButton");
		backButton.setOnAction(e -> {
			new MainUI().start(primaryStage);
		});
		pongRoot.add(backButton, 1, 21, 6, 2);

		// Setting the chat
		SwingNode chatUI = new SwingNode();
		createSwingContent(chatUI);
		StackPane pane = new StackPane();
		pane.setId("swingPane");
		pane.setPrefSize(300.0, 600.0);
		pane.getChildren().add(chatUI);
		pongRoot.add(pane, 36, 0, 12, 24);
		
		SwingNode pongGame = new SwingNode();
//		createPongSwingContent(pongGame);
		StackPane pongPane = new StackPane();
		pongPane.setId("swingPane");
		pongPane.setPrefSize(400.0, 400.0);
		pongPane.getChildren().add(pongGame);
		pongRoot.add(pongPane, 6, 6);
		

		// Sets the scene, adds all children nodes and sets the css style.
		scene2 = new Scene(pongRoot, 1200, 600);
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
			pongRoot.getColumnConstraints().add(colConst);
		}
		for (int i = 0; i < numOfRows; i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setPercentHeight(100.0 / numOfRows);
			pongRoot.getRowConstraints().add(rowConst);
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
	
//	private void createPongSwingContent(final SwingNode swingNode) {
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				
//				swingNode.setContent(gFrame);
//				swingNode.requestFocus();
//			}
//		});
//	}
	
	//Sets and adds the arcade machine image for the Pong game. 
	public void setArcadeMachineImage() {
		try {
			pong = new Image(new FileInputStream("images/pongScreen2.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pongView = new ImageView(pong);		
	//	pongView.setFitWidth(900);
		pongView.setPreserveRatio(true);
		pongRoot.add(pongView, 0, 14);

	}

	public static void main(String[] args) {
		launch(args);
	}
}
