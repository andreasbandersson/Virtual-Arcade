package snake;

import java.awt.Color;
import java.awt.Graphics;

/**
 * BodyPart class that has Setters and Getters for the snakes body parts and a draw method for drawing the snakes body parts. 
 * @author max.matthiasson
 *
 */
public class BodyPart 
{
	private int x, y;
	private int width, height;
	private int tileSize;
	
	public BodyPart(int xCoordinate, int yCoordinate, int tileSize)
	{
		this.x = xCoordinate;
		this.y = yCoordinate;
		width = tileSize;
		height = tileSize;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.GREEN); //Sets the color of the snake to green. 
		g.fillRect(x * width, y * height, width, height); //Fills a rectangle to draw a piece of the snake. 
	}

	public int getX() 
	{
		return x;
	}

	public void setX(int x) 
	{
		this.x = x;
	}

	public int getY() 
	{
		return y;
	}

	public void setY(int y) 
	{
		this.y = y;
	}
}
