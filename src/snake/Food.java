package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;

/**
 * Food class that has Setters and Getters for the food and a draw method for drawing the food. 
 * @author Max Matthiasson
 */
public class Food {
	
	private int foodX = 10;
	private int foodY = 10;
	private int unitWidth = 20;
	private int unitHeight = 20;

	public Food(int foodX, int foodY, int unitWidth, int unitHeight) {
		this.foodX = foodX;
		this.foodY = foodY;
		this.unitWidth = unitWidth;
		this.unitHeight = unitHeight;
	}
	/**
	 * Draws the pieces of food by giving it a color and filling in a rectangle representing the piece of food.
	 * @param gc
	 */
	public void drawFoodPane(GraphicsContext gc) {
		gc.setFill(javafx.scene.paint.Color.rgb(75, 75, 75)); //Sets color of the food to dark grey.
		gc.fillRect(foodX, foodY, unitWidth, unitHeight); //Fills a rectangle to draw the piece of food. 
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
