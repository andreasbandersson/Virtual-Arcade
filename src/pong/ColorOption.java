package pong;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ColorOption {
	
	private Paint color;
	private int width, height, x, y;
	
	public ColorOption(int x, int y, Paint colors) {
		this.x = x;
		this.y = y;
		this.width = Config.ColorOpt.width;
		this.height = Config.ColorOpt.height;
		this.color = colors;
	}
	
	public void update() {
		
	}
	
	public void render(GraphicsContext gc) {
		gc.setFill(this.color);
		gc.fillRect(x, y, width, height);
	}
	
	public Paint getColor() {
		return this.color;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x,y,width,height);
	}
	
}
