package chat;

import java.io.File;
import java.net.MalformedURLException;

import javax.swing.GroupLayout.SequentialGroup;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import spaceInvaders.units.Position;

/**
 * 
 * @author M�ns Grundberg
 *
 */
public class ChatUI extends Pane {
	private TextFlow messages = new TextFlow();
	private TextArea newMessage = new TextArea();
	private Button sendBtn = new Button("Send Message");
	private Button switchBtn = new Button("Show online users");
	private VBox vPane = new VBox(4);
	private ScrollPane scroll;
	private StackPane sPane = new StackPane();
	private TextArea onlineUsers = new TextArea();
	private Boolean showingMessages = true;
	private ChatController controller;

	public ChatUI(ChatController controller) {
		this.controller = controller;
		init();
	}
	
	public static void main(String[] args) {
		new ChatUI(new ChatController());
	}

	private void init() {
		setPrefSize(300, 600);
		setId("pane");
		
		newMessage.setPromptText("Type @username to send a private message");
		onlineUsers.setWrapText(true);
		onlineUsers.setEditable(false);
		newMessage.setWrapText(true);
		onlineUsers.setPrefSize(300, 460);
		sendBtn.setPrefSize(280, 25);
		switchBtn.setPrefSize(280, 25);
		newMessage.setPrefSize(300, 50);
		
		//Andreas
		sendBtn.setId("buttons");
		switchBtn.setId("buttons");

		scroll = new ScrollPane(messages);
		scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setPrefSize(260, 460);
		scroll.viewportBoundsProperty();
		messages.setPrefSize(300, scroll.getPrefViewportHeight());
		
//		messages.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//		vPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//		newMessage.setBorder(new Border(
//				new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//		vPane.setBorder(new Border(
//				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//		
		vPane.setAlignment(Pos.CENTER);
		vPane.setPrefSize(300.0, 600.0);
		
		sPane.getChildren().add(scroll);
		vPane.getChildren().add(sPane);
		vPane.getChildren().add(newMessage);
		vPane.getChildren().add(sendBtn);
		vPane.getChildren().add(switchBtn);
		
		messages.setMaxWidth(300);
		messages.setMaxHeight(460);
		
		messages.setId("messageArea");
		onlineUsers.setId("messageArea");
		vPane.setId("vPane");
		newMessage.setId("messageArea");

		switchBtn.setOnAction(buttonHandler);
		sendBtn.setOnAction(buttonHandler);

		newMessage.addEventFilter(KeyEvent.KEY_PRESSED, keyFilter);
		messages.addEventFilter(KeyEvent.KEY_PRESSED, keyFilter);
		onlineUsers.addEventFilter(KeyEvent.KEY_PRESSED, keyFilter);
		vPane.addEventFilter(KeyEvent.KEY_PRESSED, keyFilter);

		addMessage("Messages are public by default", 2);
		addMessage("Type @username to send a private message", 2);
		addMessage("Example: @Aragorn This is a message", 2);
		
		
		getChildren().add(vPane);
		try {
			getStylesheets().add((new File("styles//chatStyle.css")).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void addMessage(String msg, int type) {
		Platform.runLater(new Runnable() {
			public void run() {
				Text temp = new Text(msg + "\n");
				if (type == 0) {
					temp.setStyle("-fx-fill: #00ff00;");
				} else if (type == 1) {
					temp.setStyle("-fx-fill: #d30c0c;");
				} else {
					temp.setStyle("-fx-fill: #18eef2;");
				}
				messages.getChildren().add(temp);
			}
		});
	}

	public void updateUserList(UserList userList) {
		Platform.runLater(new Runnable() {
			public void run() {
				onlineUsers.setText("");
				onlineUsers.appendText("USERS ONLINE:" + "\n");
				for (int i = 0; i < userList.size(); i++) {
					onlineUsers.appendText(userList.get(i).getUsername() + "\n");
				}
			}
		});
	}

	EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			if (event.getSource() == sendBtn && !newMessage.getText().isEmpty()) {
				controller.sendMessage(newMessage.getText().trim());
				newMessage.setText(null);
				newMessage.selectPositionCaret(0);
			} else if (event.getSource() == switchBtn) {
				if (showingMessages) {
					sPane.getChildren().clear();
					sPane.getChildren().add(onlineUsers);
					switchBtn.setText("Show messages");
					showingMessages = false;
				} else if (!showingMessages) {
					sPane.getChildren().clear();
					sPane.getChildren().add(scroll);
					switchBtn.setText("Show online users");
					showingMessages = true;
				}
			}
		}
	};

	EventHandler<KeyEvent> keyFilter = new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent e) {
			if (e.getCode() == KeyCode.ENTER) {
				sendBtn.fire();
				e.consume();
			} else if (e.getCode() == KeyCode.TAB) {
				switchBtn.fire();
				e.consume();
			}
		}
		
		
	};
	

}