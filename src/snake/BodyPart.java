package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import snake.BodyPart;

/**
 * BodyPart class that has Setters and Getters for the snakes body parts and a draw method for drawing the snakes body parts. 
 * @author Max Matthiasson
 */
public class BodyPart {
	
	private int snakeX = 0;
	private int snakeY = 0;
	private int unitWidth = 15;
	private int unitHeight = 15;
	private static Image snakeCharacterImage;
	
	public BodyPart(Image snakeCharacterImage, int snakeX, int snakeY, int unitWidth, int unitHeight) {
		BodyPart.snakeCharacterImage = snakeCharacterImage;
		this.snakeX = snakeX;
		this.snakeY = snakeY;
		this.unitWidth = unitWidth;
		this.unitHeight = unitHeight;
	}
	/**
	 * Draws the body parts of the snake.  
	 * @param gc
	 */
	public void drawSnakePane(GraphicsContext gc) {
		gc.drawImage(snakeCharacterImage, snakeX, snakeY, unitWidth, unitHeight);
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
	public static Image getSnakeImage() {
		return snakeCharacterImage;
	}
	public static void setSnakeImage(Image snakeImage) {
		BodyPart.snakeCharacterImage = snakeCharacterImage;
	}
}
