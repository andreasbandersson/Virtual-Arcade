package chat;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/*
 * TEMPORÄR KLASS. ENDAST FÖR ATT TESTA CHATTSYSTEMET.
 * Måns Grundberg
 */

@SuppressWarnings("serial")
public class ChatTestUI extends JPanel implements ActionListener {
	private JTextArea messages = new JTextArea();
	private JScrollPane scrollMessages = new JScrollPane(messages);
	private JTextArea newMessage = new JTextArea();
	private JScrollPane scrollPane2 = new JScrollPane(newMessage);
	private JPanel southPanel = new JPanel(new GridLayout(3, 0));
	private JButton sendButton = new JButton("Send Message");
	private JButton btnSwitch = new JButton("Show online users");
	private JTextArea onlineUsers = new JTextArea();
	private boolean showingMessages = true;
	private ChatController controller;
	private CardLayout card = new CardLayout();
	private JPanel cardPanel = new JPanel(card);
	private JScrollPane scrollUsers = new JScrollPane(onlineUsers);

	public ChatTestUI(ChatController controller) {
		this.controller = controller;
		init();
		setPanelInFrame(this);
	}

	public void init() {
		setPreferredSize(new Dimension(300, 600));
		setLayout(new BorderLayout());
		newMessage.setForeground(Color.GREEN);
		newMessage.setBackground(Color.BLACK);
		messages.setBackground(Color.BLACK);
		messages.setForeground(Color.GREEN);
		onlineUsers.setBackground(Color.BLACK);
		onlineUsers.setForeground(Color.GREEN);

		scrollMessages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollMessages.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollUsers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		messages.setLineWrap(true);
		messages.setWrapStyleWord(true);
		newMessage.setLineWrap(true);
		newMessage.setWrapStyleWord(true);
		onlineUsers.setLineWrap(true);
		onlineUsers.setWrapStyleWord(true);

		southPanel.setPreferredSize(new Dimension(300, 100));
		cardPanel.add(scrollMessages);
		cardPanel.add(scrollUsers);
		southPanel.add(scrollPane2);
		southPanel.add(sendButton);
		southPanel.add(btnSwitch);
		add(southPanel, BorderLayout.SOUTH);

		add(cardPanel, BorderLayout.CENTER);

		btnSwitch.addActionListener(this);
		sendButton.addActionListener(this);

		addNewMessage("Use @username to send a private message");
		addNewMessage("Example: @Aragorn This is a message");
	}

	public void addNewMessage(String message) {
		messages.append(message + "\n");
	}

	// TODO
	public void updateUserList(UserList userList) {
		onlineUsers.setText("");
		onlineUsers.append("USERS ONLINE:" + "\n");
		for (int i = 0; i < userList.size(); i++) {
			onlineUsers.append(userList.get(i).getUsername() + "\n");
		}
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendButton) {
			controller.sendMessage(newMessage.getText());
			newMessage.setText("");
		} else if (e.getSource() == btnSwitch) {
			if (showingMessages) {
				card.next(cardPanel);
				btnSwitch.setText("Show messages");
				showingMessages = false;
				revalidate();
			} else if (!showingMessages) {
				card.first(cardPanel);
				btnSwitch.setText("Show online users");
				showingMessages = true;
				revalidate();
			}
		}
	}

	public void setPanelInFrame(JPanel panel) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("CHATTA RÅÅ!!!!");
				frame.add(panel);
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setLocation(0, 0);
				frame.setResizable(false);
			}
		});
	}

	public static void main(String[] args) {
		new ChatTestUI(new ChatController());
	}
}
