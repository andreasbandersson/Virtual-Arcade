package chat;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.SwingUtilities;

/*
 * TEMPORÄR KLASS. ENDAST FÖR ATT TESTA CHATTSYSTEMET.
 * Måns Grundberg
 */

@SuppressWarnings("serial")
public class ChatTestUI extends JPanel implements ActionListener, KeyListener {
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

		newMessage.addKeyListener(this);
		messages.addKeyListener(this);

		scrollMessages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollMessages.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollUsers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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
		sendButton.setFont(new Font("MONOSPACE", Font.BOLD, 14));
		btnSwitch.setFont(new Font("MONOSPACE", Font.BOLD, 14));

		addMessage("Type @username to send a private message");
		addMessage("Example: @Aragorn This is a message");
	}

	public void addMessage(String message) {
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
		if (e.getSource() == sendButton && !newMessage.getText().isEmpty()) {
			controller.sendMessage(newMessage.getText().trim());
			newMessage.setText(null);
			newMessage.setCaretPosition(0);
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
				frame.getRootPane().setDefaultButton(sendButton);
			}
		});
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			sendButton.doClick();
			e.consume();
		} else if (e.getKeyCode() == KeyEvent.VK_TAB) {
			btnSwitch.doClick();
			e.consume();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public static void main(String[] args) {
		new ChatTestUI(new ChatController());
	}
}
