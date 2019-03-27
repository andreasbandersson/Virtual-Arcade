package pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;

import javax.sound.sampled.AudioInputStream;

public class Game extends Canvas implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	
	private Thread thread;
	private boolean running;
	
	/*
	 *	when specific keys are pressed, their key_codes are stored in a hashMap 
	 */
	public static HashMap<Integer,Boolean> keyDownMap = new HashMap<Integer, Boolean>();
	
	private LinkedList<Platform> platforms = new LinkedList<Platform>();
	private Ball ball;
	
	private static GameFrame gFrame;
	
	private Font txtFont;
		
	public static void main(String[] args) {
		gFrame = new GameFrame(new Game());
	}
	
	public void start() {
		if(this.thread == null) {
			this.thread = new Thread(this, "pong");
			this.thread.start();
		}
		
	}

	public void run() {

		init();
		while(running) {
			update();
			render();
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void init() {
		
		addKeyListener(this);
		this.running = true;
		
		this.platforms.add(new Platform(true)); // Left platform
		this.platforms.add(new Platform(false)); // Right platform
		this.ball = new Ball((Config.Window.width/2)-(Config.Ball.width/2), (Config.Window.height/2)-(Config.Ball.height/2));
	
		txtFont = new Font(Font.MONOSPACED, Font.BOLD, 20);
	}
	
	public void update() {
		
		// Update platforms
		for(Platform platform: this.platforms) {
			platform.update(keyDownMap);
		}

		
		// Update pong ball
		this.ball.update(this.platforms);
		
		// Display points

	}
	
	public void render() {
		BufferStrategy bufferStrategy = this.getBufferStrategy();

		// to initialize buffer once
		if(bufferStrategy == null) {
			this.createBufferStrategy(3);
			return;
		}
	    
	    Graphics2D g = (Graphics2D)bufferStrategy.getDrawGraphics();
	   
	    g.setColor(new Color(173, 50, 111));
	    g.fillRect(0, 0, Config.Window.width, Config.Window.height);
	    ////////////////////////////////////////////////////////////

	    
	    	// Render(draw/paint) platforms
	 		for(Platform platform: this.platforms) {
	 			platform.render(g);
	 		}
	 		
	 		// Render pong ball
	 		this.ball.render(g);
	 		
	 		displayPoints(g);
	 		
	    
	    ////////////////////////////////////////////////////////////
	    g.dispose();
	    bufferStrategy.show();
	}
	
	
	public void displayPoints(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(txtFont);
	
		FontMetrics metrics = g.getFontMetrics(txtFont);
		
	    String title = "";
	    for(Platform p: this.platforms) {
	    	title += " Player "+p.getPlayerId()+": "+p.getPoints()+(p.getPlayerId() == 2 ? "":" |");
	    }

	    int x = (Config.Window.width/2) - metrics.stringWidth(title) / 2;
	    int y = 10 + (metrics.getHeight() / 2) + metrics.getAscent();
	    
		g.drawString(title, x, y);
	}
	

	
	public void keyPressed(KeyEvent e)
	{
		keyDownMap.put(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e)
	{
		keyDownMap.remove(e.getKeyCode());
	}


	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
