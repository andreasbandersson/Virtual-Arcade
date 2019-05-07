package chat;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * 
 * @author Måns Grundberg
 *
 */
public class ChatUI extends Pane {
	private TextArea messages = new TextArea();
	private TextArea newMessage = new TextArea();
	private Button sendBtn = new Button("Send Message");
	private Button switchBtn = new Button("Switch");
	private VBox vPane = new VBox(4);
	private StackPane sPane = new StackPane();
	private TextArea onlineUsers = new TextArea();
	private Boolean showingMessages = true;
	private ChatController controller;

	public ChatUI(ChatController controller) {
		this.controller = controller;
		init();
	}

	private void init() {
		setPrefSize(300, 600);
		
		messages.setWrapText(true);
		messages.setEditable(false);
		onlineUsers.setWrapText(true);
		onlineUsers.setEditable(false);
		newMessage.setWrapText(true);
		messages.setPrefSize(300, 480);
		onlineUsers.setPrefSize(300, 480);
		sendBtn.setPrefSize(300, 25);
		switchBtn.setPrefSize(300, 25);
		newMessage.setPrefSize(300, 50);

		sPane.getChildren().add(messages);
		vPane.getChildren().add(sPane);
		vPane.getChildren().add(newMessage);
		vPane.getChildren().add(sendBtn);
		vPane.getChildren().add(switchBtn);

		messages.setId("MessageArea");
		newMessage.setId("MessageArea");
		onlineUsers.setId("MessageArea");


		vPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		newMessage.setBorder(new Border(
				new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		vPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		switchBtn.setOnAction(buttonHandler);
		sendBtn.setOnAction(buttonHandler);

		newMessage.addEventFilter(KeyEvent.KEY_PRESSED, keyFilter);
		messages.addEventFilter(KeyEvent.KEY_PRESSED, keyFilter);
		onlineUsers.addEventFilter(KeyEvent.KEY_PRESSED, keyFilter);

		addMessage("Type @username to send a private message");
		addMessage("Example: @Aragorn This is a message");
		
		getChildren().add(vPane);
	}

	public void addMessage(String msg) {
		Platform.runLater(new Runnable() {
			public void run() {
				messages.appendText(msg + "\n");
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
					sPane.getChildren().add(messages);
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
