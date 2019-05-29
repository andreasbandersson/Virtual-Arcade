package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * BodyPart class that has Setters and Getters for the snakes body parts and a draw method for drawing the snakes body parts. 
 * @author Max Matthiasson
 */
public class BodyPart {
	
	private int snakeX;
	private int snakeY;
	private int unitWidth;
	private int unitHeight;
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


	public int getSnakeY() {
		return snakeY;
	}
}
