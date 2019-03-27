package chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/*
 *  TEMPORÄRT UI. ENDAST FÖR TESTNING.
 *  Måns Grundberg
 */

@SuppressWarnings("serial")
public class LoginTestUI extends JPanel implements ActionListener {
	private JLabel username = new JLabel("Username:");
	private JLabel password = new JLabel("Password:");
	private JLabel response = new JLabel("Wrong username and/or password");
	private JTextField inputUsername = new JTextField();
	private JPasswordField inputPassword = new JPasswordField();
	private JButton login = new JButton("Login");
	private JButton createUser = new JButton("Create User");
	private ChatController controller;
	private JPanel northPanel = new JPanel(new GridLayout(2, 2));
	private JPanel southPanel = new JPanel(new GridLayout(1, 2));

	public LoginTestUI() {
		init();
	}

	public void init() {
		setPreferredSize(new Dimension(250, 125));
		response.setForeground(Color.RED);
		username.setPreferredSize(new Dimension(120, 25));
		inputUsername.setPreferredSize(new Dimension(120, 25));
		password.setPreferredSize(new Dimension(120, 25));
		inputPassword.setPreferredSize(new Dimension(120, 25));
		response.setPreferredSize(new Dimension(250, 25));
		response.setHorizontalAlignment(JLabel.CENTER);

		northPanel.add(username);
		northPanel.add(inputUsername);
		northPanel.add(password);
		northPanel.add(inputPassword);
		southPanel.add(login);
		southPanel.add(createUser);
		add(northPanel);
		add(response);
		add(southPanel);

		login.addActionListener(this);
		createUser.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == login) {

		} else if (e.getSource() == createUser) {

		}
	}

	public void setResponse(String str) {
		response.setText(str);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("ServerUI");
				frame.add(new LoginTestUI());
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setLocation(0, 0);
				frame.setResizable(false);
			}
		});
	}

}
