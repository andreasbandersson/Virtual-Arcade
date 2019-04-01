package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener
{
	//Spel-skärmens höjd och bredd.
	public static final int WIDTH = 500, HEIGHT = 500;
	
	private Thread thread;
	
	private boolean running;
	private boolean right = true;
	private boolean left = false;
	private boolean up = false;
	private boolean down = false;
	
	private BodyPart bp;
	private ArrayList<BodyPart> snake;
	
	private Food food;
	private ArrayList<Food> foodList;
	
	private Random r = new Random();
	
	private int x = 10; //X-position för ormen.
	private int y = 10;	//Y-position för ormen. 
	private int size = 5; //Längden på ormen.
	private int ticks = 0;
	private int score = 0;
	
	public GamePanel()
	{
		setFocusable(true);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);
		snake = new ArrayList<BodyPart>();
		foodList = new ArrayList<Food>();
		
		start();
	}
	public void start()
	{
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	public void stop()
	{
		running = false;
		try 
		{
			thread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	public void tick()
	{
		
		if(snake.size() == 0)
		{
			bp = new BodyPart(x, y, 10);
			snake.add(bp);
			
		}
		ticks++;
		
		if(ticks > 300000)
		{
			if(right == true) 
			{
				x++; //if(right = true) x++?
			}
			if(left == true)
			{
				x--;
			}
			if(up == true)
			{
				y--;
			}
			if(down == true)
			{
				y++;
			}
			
			ticks = 0;
			
			bp = new BodyPart(x, y, 10);
			snake.add(bp);
			
			if(snake.size() > size)
			{
				snake.remove(0);
			}
		}
		if(foodList.size() == 0)
		{
			int x = r.nextInt(49);
			int y = r.nextInt(49);
			
			food = new Food(x, y, 10);
			foodList.add(food);
		}
		
		for(int i = 0; i < foodList.size(); i++)
		{
			if(x == foodList.get(i).getX() && y == foodList.get(i).getY())
			{
				size++;
				score++;
				foodList.remove(i);
				i++;

			}
		}
		//Kollision för ormen. 
		for(int i = 0; i < snake.size(); i++)
		{
			if(x == snake.get(i).getX() && y == snake .get(i).getY())
			{
				if(i != snake.size() - 1)
				{
					System.out.print("Game over");
					System.out.println("Score: " + score);
					stop();
				}
			}
		}
		//Kollision för kanterna. 
		if(x < 0 || x > 50 || y < 0 || y > 50)
		{
			System.out.println("Game over");
			System.out.println("Score: " + score);
			stop();
		}
	}
	public void paint(Graphics g)
	{
		g.clearRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for(int i = 0; i < WIDTH / 10; i++)
		{
			g.drawLine(i * 10, 0, i * 10, HEIGHT);
		}
		for(int i = 0; i < HEIGHT / 10; i++)
		{
			g.drawLine(0, i * 10, HEIGHT, i * 10);
		}
		for(int i = 0; i < snake.size(); i++)
		{
			snake.get(i).draw(g);
		}
		for(int i = 0; i < foodList.size(); i++)
		{
			foodList.get(i).draw(g);
		}
	}
	@Override
	public void run() 
	{
		while(running)
		{
			tick();
			repaint();
		}
	}
	@Override
	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_RIGHT && !left)
		{
			right = true;
			up = false;
			down = false;
		}
		if(key == KeyEvent.VK_LEFT && !right)
		{
			left = true;
			up = false;
			down = false;
		}
		if(key == KeyEvent.VK_UP && !down)
		{
			up = true;
			left = false;
			right = false;
		}
		if(key == KeyEvent.VK_DOWN && !up)
		{
			down = true;
			left = false;
			right = false;
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
