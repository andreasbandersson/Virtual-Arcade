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
	private JLabel response = new JLabel();
	private JTextField inputUsername = new JTextField();
	private JPasswordField inputPassword = new JPasswordField();
	private JButton login = new JButton("Login");
	private JButton createUser = new JButton("Create User");
	private ChatController controller;
	private JFrame frame;

	public LoginTestUI(ChatController controller) {
		this.controller = controller;
		init();
		setPanelInFrame(this);
	}

	private void init() {
		setPreferredSize(new Dimension(300, 125));
		
		JLabel username = new JLabel("Username:");
		JLabel password = new JLabel("Password:");
		JPanel northPanel = new JPanel(new GridLayout(2, 2));
		JPanel southPanel = new JPanel(new GridLayout(1, 2));
		
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
		
		login.setOpaque(true);
		createUser.setOpaque(true);
		
		login.addActionListener(this);
		createUser.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == login) {
			controller.login(inputUsername.getText(), inputPassword.getText());
			login.setEnabled(false);
			createUser.setEnabled(false);
		} else if (e.getSource() == createUser) {
			controller.newUser(inputUsername.getText(), inputPassword.getText());
			login.setEnabled(false);
			createUser.setEnabled(false);
		}
	}

	public void setResponse(String str) {
		response.setText(str);
		login.setEnabled(true);
		createUser.setEnabled(true);
	}
	
	private void setPanelInFrame(JPanel panel) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame = new JFrame("LOGIN");
				frame.add(panel);
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setLocation(0, 0);
				frame.setResizable(false);
			}
		});
	}
	
	public void disposeFrame() {
		frame.dispose();
	}
}
