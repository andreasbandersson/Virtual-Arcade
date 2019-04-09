package pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;
	
	private Thread thread;
	private boolean running, gamePaused;
	private long lastKeyPressTime;
	private Sound mainTheme;
	private boolean muteSound, muted, firstGame;
	
	public static int test = 5; 
	
	/*
	 *	when specific keys are pressed, their key_codes are stored in a hashMap 
	 */
	public static HashMap<Integer,Boolean> keyDownMap = new HashMap<Integer, Boolean>();
	private LinkedList<Platform> platforms = new LinkedList<Platform>();
	
	private List<ColorOption> leftPlatformColors = new ArrayList<ColorOption>();
	private List<ColorOption> rightPlatformColors = new ArrayList<ColorOption>();

	
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
	/*	while(running) {
			update(0.3);
			render();
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	*/	
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 30.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		
		while(running) {
			this.requestFocusInWindow();

			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update(delta);
				updates++;
				delta--;
			}
			
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				gFrame.updateTitle(updates+" ups, "+frames+" fps");
				updates = frames = 0;
			}
			
		
		}
	}
	
	
	//Ändra platform färg
	public void init() {
		
		addKeyListener(this);
		addMouseListener(this);
		
		this.running = this.firstGame = true;
		
		this.muteSound = this.muted = this.gamePaused = false;
		
		this.lastKeyPressTime = System.currentTimeMillis();
		
		this.platforms.add(new Platform(true)); // Left platform
		this.platforms.add(new Platform(false)); // Right platform
		this.ball = new Ball((Config.Window.width/2)-(Config.Ball.width/2), (Config.Window.height/2)-(Config.Ball.height/2));
	
		txtFont = new Font(Font.MONOSPACED, Font.BOLD, 20);
		
		int marginLeft = 5;
		Color[] colors = {Color.WHITE, Color.BLACK, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.MAGENTA};
		
		for(int i = 0; i < colors.length; i++) {
			leftPlatformColors.add(new ColorOption((i * Config.ColorOpt.width + marginLeft), 10, colors[i]));
			
			rightPlatformColors.add(
				new ColorOption(
					(Config.Window.width - Config.ColorOpt.width - marginLeft) - (i*Config.ColorOpt.width), 
					10, 
					colors[i]
				)
			);
		}
		
		//Sound.play("files/maintheme.wav");
		this.mainTheme = new Sound("files/maintheme.wav");
		
		
	}
	
	public void update(double dt) {
		
		// check if P key was pressed ( PAUSE )
		if(keyDownMap.containsKey(KeyEvent.VK_P) 
		&& (System.currentTimeMillis() - this.lastKeyPressTime >= 200)) {
			
			if(!this.gamePaused)
				this.gamePaused = true;
			else
				this.gamePaused = false;
			
			this.lastKeyPressTime = System.currentTimeMillis();
		}
		
		// check if M key was pressed ( MUTE )
		if(keyDownMap.containsKey(KeyEvent.VK_M)
		&& (System.currentTimeMillis() - this.lastKeyPressTime >= 200)) {
			if(!this.muteSound)
				this.muteSound = true;
			else
				this.muteSound = false;
			
			this.lastKeyPressTime = System.currentTimeMillis();

		}
		// If Enter was pressed
		if(keyDownMap.containsKey(KeyEvent.VK_ENTER)
		&& (System.currentTimeMillis() - this.lastKeyPressTime >= 200)) {			
			if(firstGame) {
				firstGame = false;
			}
			this.lastKeyPressTime = System.currentTimeMillis();

		}
		
		
		// Update everything while the game is NOT paused.
		if(!gamePaused && !firstGame) {
			
			// Update Colors
			for(ColorOption co: leftPlatformColors) {
				co.update();
			}
			for(ColorOption co: rightPlatformColors) {
				co.update();
			}
			
			
			// Update platforms
			// Check for ROUND WINNER
			if(this.ball.getX() + this.ball.getWidth() >= Config.Window.width) {
				
				for(Platform platform: this.platforms) {
					if(platform.getPlayerId() == 1) {
						System.out.println("increase point for P1");
						platform.increasePoints();
					}
					
					this.ball.resetPos();	
					platform.update(keyDownMap, dt);	
				}
				
			} else if(this.ball.getX() <= 0) {
				System.out.println("BALL OUT <");
				
				for(Platform platform: this.platforms) {
					if(platform.getPlayerId() == 2) {
						System.out.println("increase point for P2");
						platform.increasePoints();
					}
					
					this.ball.resetPos();	
				}
				
				
			}
			
			//Check for winner
			//if any players points are bigger than maxpoints, he she wins.  
			for(Platform platform: this.platforms) {
				platform.update(keyDownMap, dt);
				if(platform.getPoints() >= Config.Game.maxWins) {
					System.out.println("Player " + platform.getPlayerId() + " WINS !");
					resetGame();
					break;
				}
			}
				
			// Update pong ball
			this.ball.update(this.platforms, dt);
			
	
		} 
		
		
		
		
		// check if sound should be MUTED
		if(this.muteSound && !this.muted){
			this.mainTheme.stopSound();
			this.muted = true;
			
		// check if sound should be RESUMED
		} else if(!this.muteSound && this.muted){
			this.mainTheme.resumeSound();
			this.muted = false;
		}
		

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
	    
	    // RENDER ONLY WHEN THE GAME IS _NOT_ PAUSED
	    
	    if(firstGame) {
	    	displayText(g, "[ENTER] Start", Color.WHITE);
	    } else {
		    if(!gamePaused) {
		    	
				for(ColorOption co: leftPlatformColors) {
					co.render(g);
				}
				for(ColorOption co: rightPlatformColors) {
					co.render(g);
				}
				
		    	// Render(draw/paint) platforms
		 		for(Platform platform: this.platforms) {
		 			platform.render(g);
		 		}
		 		
		 		// Render pong ball
		 		this.ball.render(g);
		 		
		 		displayPoints(g);	
		    } else {
		    	
		    	// DISPLAY "paused" WHEN GAME IS PAUSED
		    	displayText(g, "Paused", Color.WHITE);
		    }
	    }
	    
	    ////////////////////////////////////////////////////////////
	    g.dispose();
	    bufferStrategy.show();
	}
	
	public void resetGame() {
		
		// reset player points
		for(Platform p: this.platforms) {
			p.resetPoints();
		}
		
		// reset ball position
		this.ball.resetPos();
		
		this.firstGame = true;
		this.gamePaused = false;
		this.muteSound = false;
		this.muted = false;
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
	
	public void displayText(Graphics2D g, String text, Color textColor) {
	
		g.setFont(txtFont);
	
		FontMetrics metrics = g.getFontMetrics(txtFont);
	
	    int x = (Config.Window.width/2) - metrics.stringWidth(text) / 2;
	    int y = 10 + (metrics.getHeight() / 2) + metrics.getAscent();
	    
		g.setColor(textColor);
		g.drawString(text, x, y);
	}
	
	//  LOOK FOR MOUSE CLICKS ON COLORS
	public void updatePaddleColor(MouseEvent e) {
		for(ColorOption co: leftPlatformColors) {
			if(co.getBounds().contains(e.getX(), e.getY())) {
				
				for(Platform p: this.platforms) {
					if(p.getPlayerId() == 1) {
						p.setColor(co.getColor());
					}
				}
				
			}
		}
		
		for(ColorOption co: rightPlatformColors) {
			if(co.getBounds().contains(e.getX(), e.getY())) {
				
				for(Platform p: this.platforms) {
					if(p.getPlayerId() == 2) {
						p.setColor(co.getColor());
					}
				}
				
			}
		}
		
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

	@Override
	public void mouseClicked(MouseEvent e) {
		updatePaddleColor(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
