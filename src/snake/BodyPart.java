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
	private int x;
	private int y;
	private int width;
	private int height;
	
	public BodyPart(int xCoordinate, int yCoordinate, int tileSize)
	{
		x = xCoordinate;
		y = yCoordinate;
		width = tileSize;
		height = tileSize;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.BLACK); //Sets the color of the snake to green. 
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
