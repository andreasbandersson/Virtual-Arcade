package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class that has Get-methods for the snakes X and Y position and a draw method for drawing the snakes body parts. 
 * @author Max Matthiasson
 */
public class BodyPart {
	
	private int snakeX;
	private int snakeY;
	private int unitWidth;
	private int unitHeight;
	private static Image snakeCharacterImage;
	
	/**
	 * Constructor for the snakes body parts. 
	 * @param snakeCharacterImage - The image representing the snakes body parts. 
	 * @param snakeX - The X-position for the snake.
	 * @param snakeY - The Y-position for the snake.
	 * @param unitWidth - The width of the the snakes body parts.
	 * @param unitHeight - The height of the snakes body parts.
	 */
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
