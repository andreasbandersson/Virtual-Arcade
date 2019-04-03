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
	private Image pong;
	private ImageView pongView;
	private Scene scene2;
	private HBox hBox;
	private GridPane mainRoot;
	private ChatTestUI chatTestUI = new ChatTestUI();
	private final int numOfCols = 24;
	private final int numOfRows = 12;

	public void start(Stage primaryStage) {
		mainRoot = new GridPane();
		mainRoot.setId("mainRoot");
		mainRoot.setPrefSize(1200.0, 600.0);
		mainRoot.setGridLinesVisible(true);

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

		// Setting the HBox-node for the leader and settingsbutton.
		hBox = new HBox();
		hBox.setId("hBox");
		hBox.setPrefSize(900.0, 100.0);
		hBox.getChildren().add(leaderboardButton);
		hBox.getChildren().add(settingsButton);
		hBox.setSpacing(50.0);
		hBox.setAlignment(Pos.BASELINE_CENTER);

		leaderboardButton.setId("hBoxButtons");
		settingsButton.setId("hBoxButtons");
		settingsButton.setTextAlignment(TextAlignment.CENTER);
		// textArea.setPrefSize(300, 500);

		mainRoot.add(hBox, 0, 10, 18, 2);

		SwingNode chatUI = new SwingNode();
		createSwingContent(chatUI);
		StackPane pane = new StackPane();
		pane.setId("swingPane");
		pane.setPrefSize(300.0, 600.0);
		pane.getChildren().add(chatUI);
		pane.setAlignment(Pos.BASELINE_RIGHT);
		mainRoot.add(pane, 18, 0, 6, 12);

		setArcadeMachineImage();

		scene2 = new Scene(mainRoot, 1200, 600);
		scene2.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
		// primaryStage.setResizable(false);

		primaryStage.setTitle("VIRTUAL ARCADEs");
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
			pong = new Image(new FileInputStream("images/arcadeGreen.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pongView = new ImageView(pong);

		pongView.setFitWidth(250);
		pongView.setPreserveRatio(true);
		mainRoot.add(pongView, 1, 2);
		BorderPane.setAlignment(pongView, Pos.CENTER_LEFT);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
