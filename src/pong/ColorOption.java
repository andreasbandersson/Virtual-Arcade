package pong;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class ColorOption {
	
	private Color color;
	private int width, height, x, y;
	
	public ColorOption(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.width = Config.ColorOpt.width;
		this.height = Config.ColorOpt.height;
		this.color = color;
	}
	
	public void update() {
		
	}
	
	public void render(Graphics2D g) {
		g.setColor(this.color);
		g.fillRect(x, y, width, height);
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x,y,width,height);
	}
	
}
