package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
	private JScrollPane scrollPane = new JScrollPane(messages);
	private JTextArea newMessage = new JTextArea();
	private JScrollPane scrollPane2 = new JScrollPane(newMessage);
	private JPanel southPanel = new JPanel(new GridLayout(3, 0));
	private JButton sendButton = new JButton("Send Message");
	private JPopupMenu popUp = new JPopupMenu("Online users");

	public ChatTestUI() {
		init();
	}

	public void init() {
		setPreferredSize(new Dimension(300, 600));
		setLayout(new BorderLayout());
		newMessage.setForeground(Color.GREEN);
		newMessage.setBackground(Color.BLACK);
		messages.setBackground(Color.BLACK);
		messages.setForeground(Color.GREEN);

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		messages.setLineWrap(true);
		messages.setWrapStyleWord(true);
		newMessage.setLineWrap(true);
		newMessage.setWrapStyleWord(true);

		southPanel.setPreferredSize(new Dimension(300, 100));
		add(scrollPane, BorderLayout.CENTER);
		southPanel.add(scrollPane2);
		southPanel.add(sendButton);
		southPanel.add(popUp);
		add(southPanel, BorderLayout.SOUTH);

		sendButton.addActionListener(this);
	}

	public void addNewMessage(String message) {
		messages.append(message + "\n");
	}

	// TODO
	public void updateUserList(UserList userList) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ChatTestUI ui = new ChatTestUI();
				JFrame frame = new JFrame("ServerUI");
				frame.add(ui);
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setLocation(0, 0);
				frame.setResizable(false);

				ui.addNewMessage("Use @username to send a private message");
				ui.addNewMessage("Example: @Aragorn This is a message");
			}
		});
	}
}
