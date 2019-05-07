package spaceInvaders.graphics;

import application.MainUI;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import spaceInvaders.logic.Controller;
import spaceInvaders.units.Player;
import spaceInvaders.units.Unit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * @author Viktor Altintas
 */

public class Painter {

    private Controller controller;
    private Player player;
    private Label scoreLabel;
    private Label levelTitle;
    private static Image playerLifeSprite;
    private GraphicsContext gc;
    private Group root;
    private Canvas canvas;
    private Scene scene;
    static {
        try {
            playerLifeSprite = new Image(new FileInputStream("Sprites/player.png"),30,25,true,false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Painter(MainUI mainUI) {
        init();
    }

    public void showLevelTitle() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                root.getChildren().remove(levelTitle);
                levelTitle.setText("Level " + controller.getLevelCounter());
                levelTitle.setLayoutX(300);
                levelTitle.setLayoutY(0);
                root.getChildren().add(levelTitle);

            }
        });
    }

    public synchronized void configureGraphicsContext(GraphicsContext gc){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gc.clearRect(0, 0, 600, 400);
                gc.drawImage(player.getPlayerSprite(),player.getPosition().getX(), player.getPosition().getY());
                scoreLabel.setText("Score: " + controller.getScore());
                for (Unit unit : controller.getAllUnits()) {
                    gc.drawImage(unit.getSprite(), unit.getPosition().getX(), unit.getPosition().getY());
                }
                for (int i = 0; i < player.getLife(); i++){
                    gc.drawImage(playerLifeSprite,440+((i+1)*39),10);
                }
            }
        });
    }

    public Scene getScene(){
        return scene;
    }

    public void init() {
        scoreLabel = new Label("");
        scoreLabel.setFont(new Font((12)));
        scoreLabel.setStyle("-fx-background-color:green;");

        canvas = new Canvas(600.0,400.0);

        root = new Group();
        root.getChildren().add(canvas);
        root.getChildren().add(scoreLabel);
        scene = new Scene(root,600,400,Color.BLACK);
        controller = new Controller(this,scene);
        this.player = controller.getPlayer();
        gc = canvas.getGraphicsContext2D();
        addListeners(scene);
        levelTitle = new Label("Level " + controller.getLevelCounter());
        levelTitle.setFont(new Font(12));
        levelTitle.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        showLevelTitle();
    }

    public GraphicsContext getGC() {
        return gc;
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
                }
                controller.requestRepaint();
            }
        });
    }
}

