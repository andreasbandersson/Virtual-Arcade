package chat;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/*
 * TEMPORÄR KLASS. ENDAST FÖR ATT TESTA CHATTSYSTEMET.
 * Måns Grundberg
 */

@SuppressWarnings("serial")
public class ChatTestUI extends JPanel implements ActionListener {
	private JTextArea messages = new JTextArea();
	private JTextArea newMessage = new JTextArea();
	private JButton sendButton = new JButton("Send Message");
	private JButton btnSwitch = new JButton("Show online users");
	private JTextArea onlineUsers = new JTextArea();
	private CardLayout card = new CardLayout();
	private JPanel cardPanel;
	private boolean showingMessages = true;
	private ChatController controller;

	public ChatTestUI(ChatController controller) {
		this.controller = controller;
		init();
		setPanelInFrame(this);
	}

	private void init() {
		setPreferredSize(new Dimension(300, 600));
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		
		sendButton.setBackground(Color.BLACK);
		sendButton.setForeground(Color.BLACK);
		sendButton.setOpaque(true);
		
		JScrollPane scrollMessages = new JScrollPane(messages);
		JScrollPane scrollPane2 = new JScrollPane(newMessage);
		JPanel southPanel = new JPanel(new GridLayout(3, 0));
		cardPanel = new JPanel(card);
		JScrollPane scrollUsers = new JScrollPane(onlineUsers);
		
		scrollMessages.setBackground(Color.BLACK);
		scrollMessages.setForeground(Color.GREEN);
		scrollPane2.setBackground(Color.BLACK);
		scrollPane2.setForeground(Color.GREEN);
		scrollUsers.setBackground(Color.BLACK);
		southPanel.setBackground(Color.BLACK);
		
		
		
		messages.setEditable(false);
		onlineUsers.setEditable(false);
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
		
		newMessage.setCaretColor(Color.GREEN);

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

		addNewMessage("Type @username to send a private message");
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
			controller.sendMessage(newMessage.getText().trim());
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

	private void setPanelInFrame(JPanel panel) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Chatt");
				frame.add(panel);
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setLocation(0, 0);
				frame.setResizable(false);
			}
		});
	}
}
