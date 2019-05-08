package snake;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import snake.BodyPart;
import snake.Food;


public class GUIPane extends Application
{
	private Group root;
	private Canvas canvas;
	
	private Color customLawnGreen = Color.rgb(124, 252, 0);

	private VBox buttonLayout = new VBox(30);
	
	private Button startButton = new Button("Start game");
	private Button instructionsButton = new Button("Instructions");
	
	private static final int MENU_STATE = 1;
	private static final int INGAME_STATE = 2;
	private static final int GAME_OVER_STATE = 3;
	
	private static final int UP = 1;
	private static final int DOWN = 2;	
	private static final int LEFT = 3;
	private static final int RIGHT = 4;
	
	private static final int GAME_WIDTH = 600; //Width of the game screen. 
	private static final int GAME_HEIGHT = 400; //Height of the game screen. 
	 
	private int snakeSize = 5; //Length of the snake. 
	private int score = 0; //Keeps track of the score. 
	public int unitWidth = 20; 
	public int unitHeight = 20;	
	public int snakeX = 0;
	public int snakeY = 0;
	public int foodX = 0;
	public int foodY = 0;
	private int gameState = 1;  //Keeps track of which state the program is in (MENUSTATE or INGAMESTATE). 

	private Queue<Integer> movementQueue = new ArrayDeque<Integer>(); 
	
	private Random r = new Random();
	
	private boolean paused = false;
		
	private AnimationTimer gameAnimationTimer;
	
	private BodyPart bodyPart;
	private ArrayList<BodyPart> listSnake; //List of BodyPart objects. 
	
	private Food foodPiece;
	private ArrayList<Food> listFood; //List of Food objects.
	private Scene gameScene;
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		listSnake = new ArrayList<BodyPart>();
		listFood = new ArrayList<Food>();
		
		Canvas canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
		
		
		startButton.setFont(Font.font("Verdana", 14));
		startButton.setPrefSize(150, 60);
		
		instructionsButton.setFont(Font.font("Verdana", 14));
		instructionsButton.setPrefSize(150, 60);
		
		buttonLayout.getChildren().addAll(startButton, instructionsButton);
		buttonLayout.setAlignment(Pos.CENTER);
		
		
		buttonLayout.setLayoutX(225);
		buttonLayout.setLayoutY(120);
		root = new Group();
		root.getChildren().addAll(canvas, buttonLayout);
		
		gameScene = new Scene(root, 600, 400, customLawnGreen);

		
		startButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				System.out.println("Game started");
				startGame();
				buttonLayout.getChildren().remove(startButton);
				buttonLayout.getChildren().remove(instructionsButton);
			}
		});
		
		//Creates an AnimationTimer that the game runs on. 
		gameAnimationTimer = new AnimationTimer()
		{
			private long lastUpdate = 0;
			//private long updateSpeed = 75_000_000;

			@Override
			public void handle(long now) 
			{	
				//if-statement that happens every X amount of nanoseconds.
				if (now - lastUpdate >= 50_000_000)  
				{
            		switch(movementQueue.peek())
            		{
            		    case UP: snakeY = snakeY - 20; break;
            		    case DOWN: snakeY = snakeY + 20; break;
            		    case LEFT: snakeX = snakeX - 20; break;
            		    case RIGHT: snakeX = snakeX + 20; break;
            		}
            		
        			bodyPart = new BodyPart(snakeX, snakeY, unitWidth, unitHeight);
        			listSnake.add(bodyPart); 
        			
        			//Removes the snakes trail. 
        			if(listSnake.size() > snakeSize)
        			{
        				listSnake.remove(0);
        			}
                    
                    lastUpdate = now ;
                }
				
				drawShapes(gc);
			
    			//Collision code for the snake. 
    			for(int i = 0; i < listSnake.size(); i++)
    			{
    				if(snakeX == listSnake.get(i).getSnakeX() && snakeY == listSnake.get(i).getSnakeY())
    				{
    					if(i != listSnake.size() - 1)
    					{
    						gameState = GAME_OVER_STATE;
    						gameAnimationTimer.stop();
    						System.out.println("Collision with snake");
    						drawShapes(gc);
    					}
    				}
    			}
				//Collision for the edges of the screen.
				if(snakeX > 600 || snakeX < 0 || snakeY > 400 || snakeY < 0)
				{
					gameState = GAME_OVER_STATE;
					gameAnimationTimer.stop();
					System.out.println("Collision with the edge of the screen");
					drawShapes(gc);
				}
    			//Collision for the food. 
    			for(int i = 0; i < listFood.size(); i++)
    			{
    				if(snakeX == listFood.get(i).getFoodX() && snakeY == listFood.get(i).getFoodY())
    				{
    					listFood.remove(i);
    					snakeSize++;
    					score++;
    					i++;
    				}
    			}
    			//Spawns the food. 
    			if(listFood.size() == 0)
    			{
    				int foodPosX = r.nextInt(30) * 20;
    				int foodPosY = r.nextInt(20) * 20;
    				foodPiece = new Food(foodPosX, foodPosY, unitWidth, unitHeight);
    				listFood.add(foodPiece);
    			}
			}
		};
		
		gameScene.setOnKeyPressed(e -> 
		{
			//Pressing the up arrow key or W will change the snakes direction to UP. 
		    if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W) 
		    {
		    	//Thread.sleep fixar problemet att man kan åka in i sig själv om man trycker på knapparna tillräckligt snabbt.
    			//Fast det gör att spelet blir lite mindre "smooth". 
		    	if(movementQueue.peek() != DOWN)
		    	{
		    		movementQueue.remove();
			        movementQueue.add(UP);
		    	}
		    }
		    //Pressing the down arrow key or S will change the snakes direction to DOWN. 
		    if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) 
		    {
		    	if(movementQueue.peek() != UP)
		    	{
		    		movementQueue.remove();
			        movementQueue.add(DOWN);
		    	}
		    }
		    //Pressing the left arrow key or A will change the snakes direction to LEFT. 
		    if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) 
		    {
		    	if(movementQueue.peek() != RIGHT)
		    	{
		    		movementQueue.remove();
			        movementQueue.add(LEFT);
		    	}
		    }
		    //Pressing the right arrow key or D will change the snakes direction to RIGHT. 
		    if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) 
		    {
		    	if(movementQueue.peek() != LEFT)
		    	{
		    		movementQueue.remove();
				     movementQueue.add(RIGHT);
		    	}
		    }
		    //Pressing P pauses the game. 
		    if(e.getCode() == KeyCode.P)
		    {
		    	System.out.println("Button P pressed");
		    	pauseGame();
		    }
		    //Pressing Enter while in the menu screen starts the game.
		    if(e.getCode() == KeyCode.ENTER)
		    {
		    	if(gameState == MENU_STATE)
		    	{
		    		startGame();
		    	}
		    }
		    //Remove
		    if (e.getCode() == KeyCode.ESCAPE) 
		    {
		    	if(gameState == GAME_OVER_STATE )
		    	{
		    		System.exit(1);
		    	}
		    }
		    //Pressing R after you've lost restarts the game. 
		    if(e.getCode() == KeyCode.R)
		    {
		    	if(gameState == GAME_OVER_STATE)
		    	{
			    	startGame();
		    	}
		    }
		 
		});
		
		movementQueue.add(RIGHT);
		
		gameState = MENU_STATE;
		
		drawShapes(gc);
		
		primaryStage.setTitle("Snake");
		primaryStage.setScene(gameScene);
		primaryStage.show();
		
	}
	public Scene getScene() {
		return gameScene;
	}
	public void startGame()
	{
		gameState = INGAME_STATE;
		score = 0;
		snakeX = 0;
		snakeY = 0;
		foodX = r.nextInt(30) * 20;
		foodY = r.nextInt(20) * 20;
		snakeSize = 5;
		listFood.clear();
		listSnake.clear();
		movementQueue.remove();
		movementQueue.add(RIGHT);
		gameAnimationTimer.start();
	}
	//Pauses the game.
	public void pauseGame()
	{
		if(paused)
		{
			paused = false;
			gameAnimationTimer.start();
		}
		else if(paused == false)
		{
			paused = true;
			gameAnimationTimer.stop();
		}
	}
	//Draws the game. 
	public void drawShapes(GraphicsContext gc)
	{
		if(gameState == MENU_STATE)
		{
			gc.clearRect(0, 0, 600, 400);
			
			gc.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
			gc.setFill(Color.BLACK);
			gc.fillText("Snake", 200, 60);
		}
		
		if(gameState == INGAME_STATE)
		{
			gc.clearRect(0, 0, 600, 400);
			
			//Draws the snake.
			for(int i = 0; i < listSnake.size(); i++)
			{
				listSnake.get(i).drawSnakePane(gc);
			}
			//Draws the food. 
			for(int i = 0; i < listFood.size(); i++) 
			{
				listFood.get(i).drawFoodPane(gc);
			}
			
			gc.setFont(Font.font("Verdana", 15));
			gc.setFill(Color.BLACK);
			gc.fillText("Score: " + score, 280, 15);
			
			if(paused)
			{
				gc.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
				gc.setFill(Color.BLACK);
				gc.fillText("Paused", 240, 200);
			}
		}
		
		if(gameState == GAME_OVER_STATE)
		{
			gc.clearRect(0, 0, 600, 400);
			
			gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
			gc.setFill(Color.BLACK);
			gc.fillText("GAME OVER", 140, 150);
			
			gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
			gc.setFill(Color.BLACK);
			gc.fillText("Score: " + score, 230, 200);
			
			gc.setFont(Font.font("Verdana", 20));
			gc.setFill(Color.BLACK);
			gc.fillText("Press R to restart", 210, 380);
			
		}
		
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
