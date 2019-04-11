package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

/**
 * @author Max Matthiasson
 */
public class RenderPanel extends JPanel
{


	public static final Color GREEN = new Color(1666073);

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		SnakeGame snake = SnakeGame.snake;

		g.setColor(Color.BLACK); //Sets the color of the background.
		
		g.fillRect(0, 0, 600, 600); //Fills in the rectangle that is the game screen. 

		g.setColor(Color.GREEN); //Sets the color of the snake.

		for (Point point : snake.snakeParts) //Draws the rectangles that makes up the snake.
		{
			g.fillRect(point.x * SnakeGame.SCALE, point.y * SnakeGame.SCALE, SnakeGame.SCALE, SnakeGame.SCALE);
		}
		
		g.fillRect(snake.head.x * SnakeGame.SCALE, snake.head.y * SnakeGame.SCALE, SnakeGame.SCALE, SnakeGame.SCALE);
		
		g.setColor(Color.RED); //Sets the color of the food. 
		
		g.fillRect(snake.food.x * SnakeGame.SCALE, snake.food.y * SnakeGame.SCALE, SnakeGame.SCALE, SnakeGame.SCALE);
		
		String string = "Score: " + snake.score + ", Length: " + snake.tailLength + ", Time: " + snake.time / 20;
		
		String stringGameOver = "Game Over!";
		
		String stringPaused = "Paused!";
		
		String stringRestart = "Press R to Restart";
		
		g.setColor(Color.white); //Sets the color of the text
		
		g.setFont(new Font("Dialog", Font.PLAIN, 15));  //Sets the font of the score, length and time text.  
		
		g.drawString(string, (int) 200, 20); //Draws score, length and time text. 
		
		g.setFont(new Font("Dialog", Font.BOLD, 30));  //Sets the font of the "Game Over!" and "Paused" text. 

		if (snake.over)
		{
			g.drawString(stringGameOver, (int) 200, (int) 200);
			g.drawString(string, 100, 250);
			g.drawString(stringRestart, 155, 500);
		}

		if (snake.paused && !snake.over)
		{
			g.drawString(stringPaused, (int) 230, (int) 250);
		}
	}
}



