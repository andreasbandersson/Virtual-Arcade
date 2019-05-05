package chat;



import javafx.application.Application;
import javafx.css.Style;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChatUI extends Application {
	private TextArea messages = new TextArea();
	private ScrollPane messageScroll;
	private TextArea newMessage = new TextArea();
	private ScrollPane newMsgScroll;
	private Button sendBtn = new Button("Send Message");
	private Button switchBtn = new Button("Switch");
	private BorderPane pane = new BorderPane();
	private VBox vPane = new VBox(4);
	private TabPane tabPane = new TabPane();
	private TextArea onlineUsers = new TextArea();
	private ScrollPane userScroll = new ScrollPane();

	@Override
	public void start(Stage primaryStage) throws Exception {		
		messageScroll = new ScrollPane(messages);
		messageScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		messageScroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		messages.setWrapText(true);
		
		messages.setEditable(false);
		
		
		newMessage.setWrapText(true);
		newMsgScroll = new ScrollPane(newMessage);
		newMsgScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		newMsgScroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		userScroll = new ScrollPane(onlineUsers);
		userScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		userScroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
//		messages.setStyle("-fx-background-color: #000000;");
//		newMessage.setStyle("-fx-background-color: black;");
//		messageScroll.setStyle("-fx-background-color: #000000");
//		newMsgScroll.setStyle("-fx-background-color: black;");
//		onlineUsers.setStyle("-fx-background-color: black;");
//		userScroll.setStyle("-fx-background-color: black;");
//		newMessage.setStyle("-fx-text-fill: green");
		messages.setPrefSize(300, 450);
//		messages.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//		messageScroll.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
	
		messageScroll.setPrefSize(300, 450);
		newMsgScroll.setPrefSize(300, 50);
		sendBtn.setPrefSize(300, 50);
		switchBtn.setPrefSize(300, 50);

		newMessage.autosize();
		
//		tabPane.getTabs().add(msgTab);
//		tabPane.getTabs().add(userTab);
//		
//		GridPane btmPane = new GridPane();
//		btmPane.add(newMessage, 0, 0);
//		btmPane.add(sendBtn, 0, 1);
		sendBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		vPane.getChildren().add(messageScroll);
		vPane.getChildren().add(newMsgScroll);
		vPane.getChildren().add(sendBtn);
		vPane.getChildren().add(switchBtn);
		
//		addMessage("HEJHEJHejkjaldk askdjfl aksjdflkj lsakdjf", "SYSTEM");
//		addMessage("private message", "PRIVATE");
//		

		messages.setId("MessageArea");
		messageScroll.setId("MessageArea");
		newMessage.setId("MessageArea");
		newMsgScroll.setId("MessageArea");
		messages.appendText("Hejhej");
		
		Scene scene = new Scene(vPane, 300, 600);
		scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
		
		// vPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		
		primaryStage.setTitle("CHATT");
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();
		
		
	}

	public void addMessage(String msg, String type) {
		if (type.equals("SYSTEM")) {
			messages.setStyle("-fx-text-fill: red");
		} else if (type.equals("PRIVATE")) {
			messages.setStyle("-fx-text-fill: green");
		} else {
			messages.setStyle("-fx-text-fill: green");
		}
		messages.appendText(msg + "\n");
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
