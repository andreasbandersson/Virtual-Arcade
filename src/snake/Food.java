package snake;

import java.awt.Color;
import java.awt.Graphics;

public class Food 
{
	private int x, y;
	private int width, height;
	private int tileSize;
	
	public Food(int x, int y, int tileSize)
	{
		this.x = x;
		this.y = y;
		this.width = tileSize;
		this.height = tileSize;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.red);
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
