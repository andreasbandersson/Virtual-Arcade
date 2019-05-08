package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javafx.scene.canvas.GraphicsContext;

/**
 * BodyPart class that has Setters and Getters for the snakes body parts and a draw method for drawing the snakes body parts. 
 * @author Max Matthiasson
 *
 */
public class BodyPart 
{
	
	private int snakeX = 0;
	private int snakeY = 0;
	private int unitWidth = 20;
	private int unitHeight = 20;
	
	public BodyPart(int snakeX, int snakeY, int unitWidth, int unitHeight)
	{
		this.snakeX = snakeX;
		this.snakeY = snakeY;
		this.unitWidth = unitWidth;
		this.unitHeight = unitHeight;
	}
	
	public void drawSnakePane(GraphicsContext gc)
	{
		gc.setFill(javafx.scene.paint.Color.BLACK); //Sets color to a lawnGreen
		gc.fillRect(snakeX, snakeY, unitWidth, unitHeight); //Fills a rectangle to draw a piece of the snake. 
	}

	public int getSnakeX() {
		return snakeX;
	}

	public void setSnakeX(int snakeX) {
		this.snakeX = snakeX;
	}

	public int getSnakeY() {
		return snakeY;
	}

	public void setSnakeY(int snakeY) {
		this.snakeY = snakeY;
	}

	public int getUnitWidth() {
		return unitWidth;
	}

	public void setUnitWidth(int unitWidth) {
		this.unitWidth = unitWidth;
	}

	public int getUnitHeight() {
		return unitHeight;
	}

	public void setUnitHeight(int unitHeight) {
		this.unitHeight = unitHeight;
	}
}
