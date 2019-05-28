package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Food class that has Setters and Getters for the food and a draw method for drawing the food. 
 * @author Max Matthiasson
 */
public class Food {
	
	private int foodX = 0;
	private int foodY = 0;
	private int unitWidth = 15;
	private int unitHeight = 15;
	private static Image foodImage;

	public Food(Image foodImage, int foodX, int foodY, int unitWidth, int unitHeight) {
		Food.foodImage = foodImage;
		this.foodX = foodX;
		this.foodY = foodY;
		this.unitWidth = unitWidth;
		this.unitHeight = unitHeight;
	}
	/**
	 * Draws the pieces of food.
	 * @param gc
	 */
	public void drawFoodPane(GraphicsContext gc) {
		gc.drawImage(foodImage, foodX, foodY, unitWidth, unitHeight);
	}
	
	public static Image getFoodImage() {
		return foodImage;
	}
	public static void setFoodImage(Image foodImage) {
		Food.foodImage = foodImage;
	}
	public int getFoodX() {
		return foodX;
	}

	public void setFoodX(int foodX) {
		this.foodX = foodX;
	}

	public int getFoodY() {
		return foodY;
	}

	public void setFoodY(int foodY) {
		this.foodY = foodY;
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
