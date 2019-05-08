package spaceInvaders.graphics;

import application.JukeBox;
import application.MainUI;
import chat.ChatUI;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import spaceInvaders.logic.Controller;
import spaceInvaders.units.Player;
import spaceInvaders.units.Unit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;


/**
 * @author Viktor Altintas
 */

public class Painter extends AnimationTimer {

    private Controller controller;
    private Player player;

    private Label scoreLabel;
    private Label levelTitle;
    private static Image playerLifeSprite;
    private GraphicsContext gc;
    private Pane root;
    private Canvas canvas;
    private Scene scene;
    private MainUI mainUI;
    private ChatUI chatUI;
    private GridPane spaceInvadersRoot;
    private Button backButton = new Button("BACK");
    private Button soundButton = new Button();
    private Image muteSoundImage;
    private Image playSoundImage;
    private Image spaceInvadersImage;
    private ImageView muteSoundImageView;
    private ImageView playSoundImageView;
    private ImageView spaceInvadersView;
    private JukeBox jukebox;

    private final int numOfCols = 48;
    private final int numOfRows = 24;

    static {
        try {
            playerLifeSprite = new Image(new FileInputStream("Sprites/player.png"),30,25,true,false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Painter(MainUI mainUI, ChatUI chatUI, JukeBox jukebox) {
        this.chatUI = chatUI;
        this.mainUI = mainUI;
        this.jukebox = jukebox;
        init();
    }

    @Override
    public void handle(long now) {
        gc.clearRect(0, 0, 600, 400);
        gc.drawImage(player.getPlayerSprite(),player.getPosition().getX(), player.getPosition().getY());
        scoreLabel.setText("Score: " + controller.getScore());
        levelTitle.setText("Level " + controller.getLevelCounter());
        for (Unit unit : controller.getAllUnits()) {
            gc.drawImage(unit.getSprite(), unit.getPosition().getX(), unit.getPosition().getY());
        }
        for (int i = 0; i < player.getLife(); i++){
            gc.drawImage(playerLifeSprite,440+((i+1)*39),10);
        }
    }

    public Scene getScene(){
        spaceInvadersRoot.add(chatUI,36,0,12,24);
        chatUI.setFocusTraversable(false);
        return scene;
    }

    public void init() {

        scoreLabel = new Label("");
        scoreLabel.setFont(new Font((12)));
        scoreLabel.setStyle("-fx-background-color:black;");

        canvas = new Canvas(600.0,400.0);
        canvas.setId("SpaceInvaders");

        spaceInvadersRoot = new GridPane();
        root = new Pane();
        createColumnsandRows();
        setSoundButtonImages();
        setSpaceInvadersArcadeMachineImage();
        
        root.setId("SpaceInvaders");

        // Adding and setting the main buttons
        backButton.setId("logOutButton");
        spaceInvadersRoot.add(backButton, 1, 21, 6, 2);

        // Adding an setting the button for mute and un-mute of login music
        soundButton = new Button();
        soundButton.setId("logOutButton");
        soundButton.setGraphic(playSoundImageView);
        spaceInvadersRoot.add(soundButton, 32, 1);

        // spaceInvadersRoot.setId("spaceInvadersRoot");
        spaceInvadersRoot.setPrefSize(1200.0, 600.0); // minus chattens bredd (300)

        scene = new Scene(spaceInvadersRoot,1200,600, Color.BLACK);

        try {
			scene.getStylesheets().add((new File("Virtual-Arcade\\styles\\spaceStyle.css")).toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

        gc = canvas.getGraphicsContext2D();
        addListeners(scene);
        
        backButton.setFocusTraversable(false);
        soundButton.setFocusTraversable(false);
        controller = new Controller(this,canvas);
        this.player = controller.getPlayer();

        levelTitle = new Label("Level " + controller.getLevelCounter());
        levelTitle.setFont(new Font(12));
        levelTitle.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        levelTitle.setLayoutX(300);
        levelTitle.setLayoutY(0);

        root.getChildren().add(scoreLabel);
        root.getChildren().add(levelTitle);
        root.getChildren().add(canvas);
        spaceInvadersRoot.add(root,6,4,24,16);

        spaceInvadersRoot.setId("mainRoot");
        canvas.requestFocus();
        canvas.setOnMouseMoved(e -> canvas.requestFocus());
           start(); // starts the animation timer
    }


    private void createColumnsandRows() {

        for (int i = 0; i < numOfCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numOfCols);
            spaceInvadersRoot.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numOfRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numOfRows);
            spaceInvadersRoot.getRowConstraints().add(rowConst);
        }
    }

    // Sets the sound buttons images.
    private void setSoundButtonImages() {
        try {
            playSoundImage = new Image(new FileInputStream("images/sound.png"));
            muteSoundImage = new Image(new FileInputStream("images/mute.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        playSoundImageView = new ImageView(playSoundImage);
        muteSoundImageView = new ImageView(muteSoundImage);
    }
    
	//Sets and adds the arcade machine image for the Pong game. 
	public void setSpaceInvadersArcadeMachineImage() {
		try {
			spaceInvadersImage = new Image(new FileInputStream("images/spaceScreen.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		spaceInvadersView = new ImageView(spaceInvadersImage);		
	//	pongView.setFitWidth(900);
		spaceInvadersView.setPreserveRatio(true);
		spaceInvadersRoot.add(spaceInvadersView, 0, 14);

	}

    public void addListeners(Scene scene) {

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {

                    case A:
                    case LEFT:
                        player.move(player.getPosition().getX()-15,player.getPosition().getY());
                        break;

                    case D:
                    case RIGHT:
                        player.move(player.getPosition().getX()+15,player.getPosition().getY());
                        break;

                    case SPACE:
                        player.shoot();
                        break;
                    case P:
                        controller.setGamePaused();
                }
            }
        });
        
    	backButton.setOnAction(e -> {
			spaceInvadersRoot.getChildren().remove(chatUI);
			mainUI.switchToMainUI();
		});

		soundButton.setOnAction(e -> {
			jukebox.muteUnmute();
			if (jukebox.isMute()) {
				soundButton.setGraphic(playSoundImageView);
			} else {
				soundButton.setGraphic(muteSoundImageView);
			}
		});
    }
}

