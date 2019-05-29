package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Obstacle class that has Setters and Getters for the obstacles and a draw method for drawing the obstacles. 
 * @author Max Matthiasson
 */
public class Obstacle 
{
	private int obstacleX;
	private int obstacleY;
	private int unitWidth;
	private int unitHeight;
	private static Image obstacleImage;

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
