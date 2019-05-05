package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * Food class that has Setters and Getters for the food and a draw method for drawing the food. 
 * @author max.matthiasson
 *
 */
public class Food 
{
	private int x;
	private int y;
	private int width;
	private int height;
	
	public Food(int x, int y, int tileSize)
	{
		this.x = x;
		this.y = y;
		this.width = tileSize;
		this.height = tileSize;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.RED); //Sets color of the food to red.
		g.fillRect(x * width, y * height, width, height); //Fills a rectangle to draw the piece of food. 
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
