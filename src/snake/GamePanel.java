package snake;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * GamePanel class.
 * @author max.matthiasson
 *
 */

public class GamePanel extends JPanel implements ActionListener, KeyListener
{
	private Dimension panelSize = new Dimension(600, 600); //Dimension of the game screen. 
	
	private Font font1 = new Font("Monospaced", Font.PLAIN, 16);
	private Font font2 = new Font("Monospaced", Font.BOLD, 30);
	private Font menuTitleFont = new Font("Monospaced", Font.BOLD, 60);
	private Font instructionFont = new Font("Monospaced", Font.PLAIN, 20);
	
	private BodyPart bp;
	private ArrayList<BodyPart> snake;
	
	private Food food;
	private ArrayList<Food> foodList;
	
	private Timer timer = new Timer(50, this); //Timer that the game runs on. 
	
	private Random r = new Random();
	
	public static final int WIDTH = 600, HEIGHT = 600; //Height and width of the game screen. 

	private int x, y; //The snakes X and Y position. 
	private int size;; //Length of the snake. 
	private int score; //Keeps track of the score. 
	private int pieceSize = 15; //Size of every piece of the snake and the food. 
	private int gameState = 1;  //Keeps track of which state the program is in (MENUSTATE or INGAMESTATE). 
	
	private static final int MENUSTATE = 1;
	private static final int INGAMESTATE = 2;
	
	private static final int UP = 1;
	private static final int DOWN = 2;	
	private static final int LEFT = 3;
	private static final int RIGHT = 4;
	
	private Queue<Integer> movementQueue = new LinkedList<Integer>();
	
	private String gameOverString = "Game Over!";
	private String pausedString = "Paused!";
	private String restartString = "Press R to Restart";
	private String instructionsString = "Use the arrow keys or WASD to control the snake";
	
	private boolean paused = false;
	private boolean over = false;
	
	public GamePanel()
	{
		setSize(panelSize);
		setPreferredSize(panelSize);
		
		snake = new ArrayList<BodyPart>();
		foodList = new ArrayList<Food>();
		
		movementQueue.add(RIGHT);
		
		addKeyListener(this);
		
		startGame();
	}
	//Method that resets everything. score, x and y position of the snake, the size of the snake. Clears the list of food objects
	//and the list of snake objects. Removes any object in the movementQueue and adds RIGHT and starts the timer. 
	public void startGame()
	{
		score = 0;
		x = 5;
		y = 5;
		size = 5;
		foodList.clear();
		snake.clear();
		movementQueue.remove();
		movementQueue.add(RIGHT);
		timer.start();
	}
	//Method that pauses the game. 
	public void pauseGame()
	{
		if(paused == true)
		{
			paused = false;
			timer.restart();
		}
		else if(paused == false)
		{
			paused = true;
			timer.stop();
			repaint();
		}
	}
	//Method that stops the game when you lose. 
	public void gameOver()
	{
		timer.stop();
		over = true;
	}
	//Method for painting. 
	public void paint(Graphics g)
	{
		//Draws the game. 
		if(gameState == INGAMESTATE)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			//Draws the grid.*
			for(int i = 0; i < WIDTH / pieceSize; i++)
			{
				g.drawLine(i * pieceSize, 0, i * pieceSize, HEIGHT);
			}
			for(int i = 0; i < HEIGHT / pieceSize; i++)
			{
				g.drawLine(0, i * pieceSize, HEIGHT, i * pieceSize);
			}
			//*
			//Draws the snake.
			for(int i = 0; i < snake.size(); i++) 
			{
				snake.get(i).draw(g);
			}
			//Draws the food. 
			for(int i = 0; i < foodList.size(); i++) 
			{
				foodList.get(i).draw(g);
			}
			
			g.setColor(Color.WHITE);
			g.setFont(font1); 
			g.drawString("Score:" + score, (int) 270, 20);
			
			if(over == true)
			{
				g.setFont(font2);
				g.drawString(gameOverString, (int) 210, (int) 200);
				g.drawString("Score:" + score, 225, 250);
				g.drawString(restartString, 140, 500);
			}
			
			if(paused == true)
			{
				g.setFont(font2);
				g.drawString(pausedString, 240, 200);
			}
		}
		//Draws the menu. 
		if(gameState == MENUSTATE)
		{
			repaint();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			g.setColor(Color.GREEN);
			g.setFont(menuTitleFont);
			g.drawString("Snake", 210, 70);
			
			g.setColor(Color.WHITE);
			
			g.setFont(instructionFont);
			g.drawString(instructionsString, 20, 250);
			
			g.setFont(font2);
			g.drawString("Press P to Play", 160, 500);
		}
	}

	@Override
	//The code in this method is based on the Timer timer. 
	public void actionPerformed(ActionEvent e) 
	{
		if(gameState == INGAMESTATE)
		{
			repaint();
			
			//Checking what int value is in the queue to decide what direction the snake should move in. 
			switch(movementQueue.peek())
			{
			    case UP: y--; break;
			    case DOWN: y++; break;
			    case LEFT: x--; break;
			    case RIGHT: x++; break;
			}
			
			bp = new BodyPart(x, y, pieceSize);
			snake.add(bp);
			
			//Removes the snakes trail. 
			if(snake.size() > size)
			{
				snake.remove(0);
			}
			//Spawns the food. 
			if(foodList.size() == 0)
			{
				int x = r.nextInt(37);
				int y = r.nextInt(37);
				food = new Food(x, y, pieceSize);
				foodList.add(food);
			}
			//Collision code for the food.
			for(int i = 0; i < foodList.size(); i++)
			{
				if(x == foodList.get(i).getX() && y == foodList.get(i).getY())
				{
					foodList.remove(i);
					size++;
					score++;
					i++;
				}
			}
			//Collision code for the snake. 
			for(int i = 0; i < snake.size(); i++)
			{
				if(x == snake.get(i).getX() && y == snake.get(i).getY())
				{
					if(i != snake.size() - 1)
					{
						gameOver();
					}
				}
			}
			//Collision code for the edges of the screen. 
			if(x < 0 || x > 38 || y < 0 || y > 38)
			{
				gameOver();
			}
		}
	}
	

	@Override
	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode();
		
		//Pressing W or the up arrow makes the snake go UP.
		if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP)
		{
			if(movementQueue.peek() != DOWN)
			{
				movementQueue.poll();
				movementQueue.add(UP);
			}
		}
		//Pressing S or the down arrow makes the snake go DOWN.
		if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN)
		{
			if(movementQueue.peek() != UP)
			{
				movementQueue.poll();
				movementQueue.add(DOWN);
			}
		}
		//Pressing A  or the left arrow makes the snake go LEFT.
		if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)
		{
			if(movementQueue.peek() != RIGHT)
			{
				movementQueue.poll();
				movementQueue.add(LEFT);
			}
		}
		//Pressing D or the right arrow makes the snake go RIGHT.
		if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT)
		{
			if(movementQueue.peek() != LEFT)
			{
				movementQueue.poll();
				movementQueue.add(RIGHT);
			}
		}
		//Pressing spacebar pauses the game.
		if(key == KeyEvent.VK_SPACE)
		{
			if(over == false)
			{
				pauseGame();
			}
		}
		//Pressing R restarts the game. 
		if(key == KeyEvent.VK_R) 
		{
			if(over == true)
			{
				over = false;
				startGame();
			}
		}
		//Pressing P starts the game when you're in the menu. 
		if(key == KeyEvent.VK_P)
		{
			if(gameState == MENUSTATE)
			{
				gameState = INGAMESTATE;
			}
		}
		//Pressing Esc closes the game if you've lost. 
		if(key == KeyEvent.VK_ESCAPE)
		{
			if(over == true)
			{
				System.exit(10);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	
	public static void main(String[] args)
	{
		GamePanel gp = new GamePanel();
		
		JFrame frame = new JFrame("Snake");
		frame.add(gp);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.addKeyListener(gp);
		frame.setVisible(true);
		frame.setFocusable(true);
	}
}

//Saker att fixa:
//1. När man bytar riktning snabbt så kan man få ormen att åka in i sig själv. Testat använda både queue och stack men funkar fortfarande inte.
//	 Testat sätta en Thread.sleep innan den bytar riktning men funkar forfarande inte som det ska. Försökt använda KeyReleased, funkar inte. 
