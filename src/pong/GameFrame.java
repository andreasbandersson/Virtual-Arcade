package pong;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame {
	private Game game;
	private JPanel gamePanel;

	public GameFrame(Game game) {
		super("Pong Game");

		this.game = game;
		this.game.setSize(new Dimension(Config.Window.width, Config.Window.height));

		this.gamePanel = new JPanel();
		
		gamePanel.add(this.game);
		add(gamePanel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(false);
	    setVisible(true);
	    setSize(new Dimension(Config.Window.width, Config.Window.height));
		pack();
		setLocationRelativeTo(null);
		
		startGame();
		
	}
	
	public void startGame() {
		this.game.start();
	}
	
	public void updateTitle(String newTitle) {
		this.setTitle(Config.Window.title+" | "+newTitle);
	}
}
