package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class Game extends Canvas implements Runnable /*, KeyListener, MouseListener*/ {
	private static final long serialVersionUID = 1L;
	
	private Thread thread;
	private boolean running, gamePaused;
	private long lastKeyPressTime, firstGameTime;
	private Sound mainTheme;
	private boolean muteSound, muted, firstGame;
	Group content = new Group();
	public static int test = 5; 
	public static HashMap<Integer,Boolean> keyDownMap = new HashMap<Integer, Boolean>();
	private LinkedList<Platform> platforms = new LinkedList<Platform>();
    //private List<ColorOption> leftPlatformColors = new ArrayList<ColorOption>();
	private List<ColorOption> rightPlatformColors = new ArrayList<ColorOption>();
	private Color rightChosenColor;
	public static int nextYLerp;
	private Ball ball;
	private static GameFrame gFrame;
	private Font txtFont;
		
	
	//ALL THE KEY LISTENER METHODS AND METHODS DEPENDED ON THIS 
	
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
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 30.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		while(running) {
			content.requestFocus();

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
	
	public void init() {
		/*
		addKeyListener(this);
		addMouseListener(this);
		*/
		
		this.rightChosenColor = Color.WHITE;
		
		this.running = this.firstGame = true;
		
		this.muteSound = this.muted = this.gamePaused = false;
		
		this.lastKeyPressTime = System.currentTimeMillis();
		
		this.platforms.add(new Platform(true)); // Left platform
		this.platforms.add(new Platform(false)); // Right platform
		this.ball = new Ball((Config.Window.width/2)-(Config.Ball.width/2), (Config.Window.height/2)-(Config.Ball.height/2));
	
		txtFont = new Font(Font.MONOSPACED, Font.BOLD, 20);
		
		int marginLeft = 5;
		
		for(int i = 0; i < Config.Game.colors.length; i++) {
			//leftPlatformColors.add(new ColorOption((i * Config.ColorOpt.width + marginLeft), 10, colors[i]));
			
			rightPlatformColors.add(
				new ColorOption(
					(Config.Window.width/2) - (i*Config.ColorOpt.width) + (Config.Game.colors.length*Config.ColorOpt.width)/2, 
					10, 
					Config.Game.colors[i]
				)
			);
		}
		
		//Sound.play("files/maintheme.wav");
		this.mainTheme = new Sound("files/maintheme.wav");
	}
	
	public void update(double dt) {
		// check if P key was pressed ( PAUSE )
		setOnKeyPressed(event -> {
		if(keyDownMap.containsKey(event.getCode()==KeyCode.PAUSE) 
		&& (System.currentTimeMillis() - this.lastKeyPressTime >= 200)) {
			
			if(!this.gamePaused)
				this.gamePaused = true;
			else
				this.gamePaused = false;
			
			this.lastKeyPressTime = System.currentTimeMillis();
		}
		
		// check if M key was pressed ( MUTE )
		if(keyDownMap.containsKey(event.getCode()==KeyCode.MUTE)
		&& (System.currentTimeMillis() - this.lastKeyPressTime >= 200)) {
			if(!this.muteSound)
				this.muteSound = true;
			else
				this.muteSound = false;
			
			this.lastKeyPressTime = System.currentTimeMillis();

		}
		
		if(keyDownMap.containsKey(event.getCode()==KeyCode.ENTER)
		&& (System.currentTimeMillis() - this.lastKeyPressTime >= 200)) {			
			if(firstGame) {
				firstGame = false;
			}
			this.lastKeyPressTime = System.currentTimeMillis();
			this.firstGameTime = System.currentTimeMillis();


		}
		
		
		
		// Update everything while the game is NOT paused.
		if(!gamePaused && !firstGame) {
			
			// Update platforms
			// Check for ROUND WINNER
			if(this.ball.getX() + this.ball.getWidth() >= Config.Window.width) {
				
				for(Platform platform: this.platforms) {
					if(platform.getPlayerId() == 1) {
												
						System.out.println("increase point for P1");
						platform.increasePoints();
					}
					
					this.ball.resetPos();
					platform.update(/*keyDownMap, */dt);	
				}
				
				nextYLerp = -1;
				
			} else if(this.ball.getX() <= 0) {
				System.out.println("BALL OUT <");
				
				for(Platform platform: this.platforms) {
					if(platform.getPlayerId() == 2) {
						System.out.println("increase point for P2");
						platform.increasePoints();
					}
					
					this.ball.resetPos();	
				}
				
				nextYLerp = -1;
				
			}
			
			for(Platform platform: this.platforms) {
				// make it follow ball in Y direction
				
				if(platform.getPlayerId() == 1) {
					// -1 = currently unknown 
					if(nextYLerp != -1) {
						if(this.ball.getX() < Config.Window.width/2) {
							platform.setY((int)lerp((float)platform.getBounds().getY(), (float)nextYLerp, 0.5f));
						}
					} else {

						platform.setY((int)lerp((float)platform.getBounds().getY(), (float)(Config.Window.height/2-platform.getBounds().getHeight()), 0.3f));						
					}					
				}
				
				platform.update(keyDownMap,dt);
				if(platform.getPoints() >= Config.Game.maxWins) {
					System.out.println("Player " + platform.getPlayerId() + " WINS !");
					resetGame();
					break;
				}
			}
				
			// start updating ball after 2 seconds
			if(System.currentTimeMillis() - this.firstGameTime >= Config.Ball.delayTime){
				// Update pong ball
				this.ball.update(this.platforms, dt);	
			}
			
	
		} else {
			// Update Colors
/*			for(ColorOption co: leftPlatformColors) {
				co.update();
			}
*/
			for(ColorOption co: rightPlatformColors) {
				co.update();
			}
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
	    
	    GraphicsContext gc = (GraphicsContext)bufferStrategy.getDrawGraphics();
	   
	    gc.setColor(new Color(173, 50, 111));
	    gc.fillRect(0, 0, Config.Window.width, Config.Window.height);
	    ////////////////////////////////////////////////////////////
	    
	    // RENDER ONLY WHEN THE GAME IS _NOT_ PAUSED
	    
	    if(firstGame) {
	    	displayText(gc, "[ENTER] Start", Color.WHITE, Config.Window.width/2, 60);
	    	displayText(gc, "[M] Mute", Color.WHITE, Config.Window.width/2, 80);
	    	displayText(gc, "[P] Pause", Color.WHITE, Config.Window.width/2, 100);
			
	    /*	for(ColorOption co: leftPlatformColors) {
				co.render(g);
			}*/
			for(ColorOption co: rightPlatformColors) {
				co.render(gc);
			}
			
			gc.setColor(this.rightChosenColor);
			gc.fillRect(Config.Window.width/2-25, Config.Window.height/2, 50, 50);
			

			
	    } else {
		    if(!gamePaused) {
		    	
		    	// Render(draw/paint) platforms
		 		for(Platform platform: this.platforms) {
		 			platform.render(gc);
		 		}
		 		
		 		// Render pong ball
		 		this.ball.render(gc);
		 		
		 		displayPoints(gc);	
		    } else {
		    	
		    	// DISPLAY "paused" WHEN GAME IS PAUSED
		    	displayText(gc, "- Paused -", Color.WHITE, Config.Window.width/2, 60);
		    	displayText(gc, "[P] Resume", Color.WHITE, Config.Window.width/2, 80);

		    }
	    }
	    
	    ////////////////////////////////////////////////////////////
	    gc.dispose();
	    bufferStrategy.show();
	}
	
	public static float lerp(float a, float b, float f) {
		  //return (1 - t) * v0 + t * v1;
		  return a + f * (b - a);
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
	
	public void displayPoints(GraphicsContext gc) {
		gc.setColor(Color.WHITE);
		gc.setFont(txtFont);
	
		FontMetrics metrics = gc.getFontMetrics(txtFont);
		
	    String title = "";
	    for(Platform p: this.platforms) {
	    	title += " Player "+p.getPlayerId()+": "+p.getPoints()+(p.getPlayerId() == 2 ? "":" |");
	    }

	    int x = (Config.Window.width/2) - metrics.stringWidth(title) / 2;
	    int y = 10 + (metrics.getHeight() / 2) + metrics.getAscent();
	    
		gc.drawString(title, x, y);
	}
	
	public void displayText(GraphicsContext gc, String text, Color textColor) {
		
		gc.setFont(txtFont);
	
		FontMetrics metrics = g.getFontMetrics(txtFont);
	
	    int x = (Config.Window.width/2) - metrics.stringWidth(text) / 2;
	    int y = 10 + (metrics.getHeight() / 2) + metrics.getAscent();
	    
		gc.setColor(textColor);
		gc.drawString(text, x, y);
	}
	
	public void displayText(GraphicsContext gc, String text, Color textColor, int posX, int posY) {
		gc.setFont(txtFont);
		
		FontMetrics metrics = gc.getFontMetrics(txtFont);
	
	    int x = posX - metrics.stringWidth(text) / 2;
	    int y = posY + (metrics.getHeight() / 2) + metrics.getAscent();
	    
		gc.setColor(textColor);
		gc.drawString(text, x, y);
	}
	
	//  LOOK FOR MOUSE CLICKS ON COLORS
	public void updatePaddleColor(MouseEvent e) {

		
		for(ColorOption co: rightPlatformColors) {
			if(co.getBounds().contains(e.getX(), e.getY())) {
				
				for(Platform p: this.platforms) {
					if(p.getPlayerId() == 2) {
						rightChosenColor = co.getColor();
						p.setColor(co.getColor());
					}
				}
				
			}
		}
		
	}
	

	
/*
	
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
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
*/
}
