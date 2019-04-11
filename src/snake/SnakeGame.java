package snake;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * @author Max Matthiasson
 */
public class SnakeGame implements ActionListener, KeyListener
{

	public static SnakeGame snake;

	public JFrame frame;

	public RenderPanel renderPanel;

	public Timer timer = new Timer(20, this);	

	public ArrayList<Point> snakeParts = new ArrayList<Point>();

	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
	public static final int SCALE = 12; //Size of every piece of the snake as well as the food. 
	
	private int lastDirection;
	public int ticks = 0; 
	public int direction = RIGHT;
	public int score = 0;
	public int tailLength = 5;
	public int time = 0;

	public Point head, food;

	public Random random;

	public boolean over = false;
	public boolean paused = false;
	
//	private enum Movement { UP, DOWN, RIGHT, LEFT };
//	Queue<Movement> movementQueue = new ArrayDeque<Movement>();
	

	public SnakeGame()
	{
		frame = new JFrame("Snake");
		frame.setVisible(true);
		frame.setSize(605, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(renderPanel = new RenderPanel()); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		startGame();
	}

	public void startGame()
	{
		score = 0;
		time = 0;
		tailLength = 5;
		direction = RIGHT;
		head = new Point(1, 1);
		random = new Random();
		snakeParts.clear();
		food = new Point(random.nextInt(46), random.nextInt(46));
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		renderPanel.repaint();
		ticks++;

		if (ticks % 2 == 0 && head != null && !over && !paused)
		{
			time++;
			snakeParts.add(new Point(head.x, head.y));
			
			//Collision code for when the snake is going up.
			if (direction == UP)
			{
				if (head.y >= 0 && noTailAt(head.x, head.y - 1))
				{
					head = new Point(head.x, head.y - 1);
				}
				else
				{
					over = true;
				}
			}
			//Collision code for when the snake is going down.
			if (direction == DOWN)
			{
				if (head.y < 46 && noTailAt(head.x, head.y + 1))
				{
					head = new Point(head.x, head.y + 1);
				}
				else
				{
					over = true;
				}
			}
			//Collision code for when the snake is going left.
			if (direction == LEFT)
			{
				if (head.x >= 0 && noTailAt(head.x - 1, head.y))
				{
					head = new Point(head.x - 1, head.y);
				}
				else
				{
					over = true;
				}
			}
			//Collision code for when the snake is going right.
			if (direction == RIGHT)
			{
				if (head.x < 49 && noTailAt(head.x + 1, head.y))
				{
					head = new Point(head.x + 1, head.y);
				}
				else
				{
					over = true;
				}
			}

			if (snakeParts.size() > tailLength)
			{
				snakeParts.remove(0);
			}
			//Increases the score and adds to the snakes tailLength aswell as spawning a new piece of food when the snake hits a piece of food. 
			if (food != null)
			{
				if (head.equals(food))
				{
					score++;
					tailLength++;
					food.setLocation(random.nextInt(46), random.nextInt(46));
				}
			}
		}
	}

	public boolean noTailAt(int x, int y)
	{
		for (Point point : snakeParts)
		{
			if (point.equals(new Point(x, y)))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int i = e.getKeyCode();

		//Button for changing direction to LEFT. 
		if ((i == KeyEvent.VK_A || i == KeyEvent.VK_LEFT) && direction != RIGHT) 
		{
			direction = LEFT;
		}
		//Button for changing direction to RIGHT. 
		if ((i == KeyEvent.VK_D || i == KeyEvent.VK_RIGHT) && direction != LEFT)
		{
			direction = RIGHT;
		}
		//Button for changing direction to UP. 
		if ((i == KeyEvent.VK_W || i == KeyEvent.VK_UP) && direction != DOWN)
		{
			direction = UP;
		}
		//Button for changing direction to DOWN.
		if ((i == KeyEvent.VK_S || i == KeyEvent.VK_DOWN) && direction != UP)
		{
			direction = DOWN;
		}
		//Pause button. 
		if (i == KeyEvent.VK_SPACE)
		{
			if(paused == true)
			{
				paused = false;
			}
			else if(paused == false)
			{
				paused = true;
			}
		}
		//Restart button
		if(i == KeyEvent.VK_R) 
		{
			if(over == true)
			{
				over = false;
				startGame();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}
	
	public static void main(String[] args)
	{
		snake = new SnakeGame();
	}
	
	/**
	 * Saker som behöver fixas.
	 * 1. Det är en delay när man pausar och när man dör. Om man pausar en gång under spelets gång så är det ingen delay när man dör.
	 * 	  -FIXED- Lade till en font till score, length, time. 
	 * 2. Om man trycker på 2 knappar samtidigt så dör ormen ibland. (Använd en queue för att lösa eller kanske lägg en check efter ändring.)
	 * 3. Kollisionen funkar inte alltid som den ska. (skriv om koden så den kollisionen inte är baserad på ormens riktning.)
	 * 4. Borde skriva om koden så att man inte behöver använda public variabler. 
	 */

}