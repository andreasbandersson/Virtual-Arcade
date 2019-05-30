package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class that has Get-methods for the obstacles X and Y position and a draw method for drawing the obstacles. 
 * @author Max Matthiasson
 */
public class Obstacle {
	
	private int obstacleX;
	private int obstacleY;
	private int unitWidth;
	private int unitHeight;
	private static Image obstacleImage;
	
	/**
	 * Constructor for the obstacles.
	 * @param obstacleImage - The image representing the obstacle. 
	 * @param obstacleX - The X-position of the obstacle.
	 * @param obstacleY - The Y-position of the obstacle.
	 * @param unitWidth - The width of the obstacle.
	 * @param unitHeight - The height of the obstacle. 
	 */
	public Obstacle(Image obstacleImage, int obstacleX, int obstacleY, int unitWidth, int unitHeight) {
		Obstacle.obstacleImage = obstacleImage;
		this.obstacleX = obstacleX;
		this.obstacleY = obstacleY;
		this.unitWidth = unitWidth;
		this.unitHeight = unitHeight;
	}
	
	/**
	 * Draws the obstacles. 
	 * @param gc
	 */
	public void drawObstacle(GraphicsContext gc) {
		gc.drawImage(obstacleImage, obstacleX, obstacleY, unitWidth, unitHeight); // Draws the image that makes up the obstacle.
	}

	public int getObstacleX() {
		return obstacleX;
	}

	public int getObstacleY() {
		return obstacleY;
	}
}
