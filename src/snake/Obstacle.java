package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Obstacle class that has Setters and Getters for the obstacles and a draw method for drawing the obstacles. 
 * @author Max Matthiasson
 */
public class Obstacle {
	private int obstacleX = 0;
	private int obstacleY = 0;
	private int unitWidth = 15;
	private int unitHeight = 15;
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

	public void setObstacleX(int obstacleX) {
		this.obstacleX = obstacleX;
	}

	public int getObstacleY() {
		return obstacleY;
	}

	public void setObstacleY(int obstacleY) {
		this.obstacleY = obstacleY;
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

	public static Image getObstacleImage() {
		return obstacleImage;
	}

	public static void setObstacleImage(Image obstacleImage) {
		Obstacle.obstacleImage = obstacleImage;
	}
}
