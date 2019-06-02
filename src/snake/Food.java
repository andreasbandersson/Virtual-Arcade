package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class that has Get-methods for the foods X and Y position and a draw method for drawing the food. 
 * @author Max Matthiasson
 */
public class Food {
	
	private int foodX;
	private int foodY;
	private int unitWidth;
	private int unitHeight;
	private static Image foodImage;
	
	/**
	 * Constructor for the food pieces.
	 * @param foodImage - The image representing the pieces of food. 
	 * @param foodX - The X-position of the food piece.
	 * @param foodY - The Y-position of the food piece. 
	 * @param unitWidth - The width of the food piece. 
	 * @param unitHeight - The height of the food piece. 
	 */
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
	
	public int getFoodX() {
		return foodX;
	}

	public int getFoodY() {
		return foodY;
	}
}
