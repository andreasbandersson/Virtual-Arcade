package snake;

import java.awt.Color;
import java.awt.Graphics;

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
		g.setColor(Color.green);
		g.fillRect(x * width, y * height, width, height);
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
