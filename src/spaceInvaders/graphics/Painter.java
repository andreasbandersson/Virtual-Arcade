package spaceInvaders.graphics;

import application.JukeBox;
import application.MainUI;
import chat.ChatController;
import chat.ChatUI;
import chat.Highscore;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.text.Text;
import spaceInvaders.Effects.Explosion;
import javafx.animation.AnimationTimer;
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
import spaceInvaders.Effects.ScoreUp;
import spaceInvaders.Effects.ShotCollision;
import spaceInvaders.logic.Controller;
import spaceInvaders.units.Player;
import spaceInvaders.units.Position;
import spaceInvaders.units.Unit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Viktor Altintas
 */

public class Painter extends AnimationTimer {

    private Controller controller;
    private Player player;

    private Label scoreLabel;
    private Label levelTitle;
    private Label pauseLabel;
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
    private ImageView backgroundImageView;
    private Pane backgroundLayer;
    private Text newScoreText;
    private Label scoreNumberFloating;
    private int gradualColor = 255;

    private final int numOfCols = 48;
    private final int numOfRows = 24;

    private static Image explosion;
    private static Image shotCollision;
    private static Image backgroundImage;
    private ChatController chatController;

    Executor executor;

    private ArrayList<Explosion> explosions = new ArrayList<>();
    private ArrayList<ShotCollision> shotCollisions = new ArrayList<>();
    private ArrayList<ScoreUp> scoreFloats = new ArrayList<>();

    static {
        try {
            playerLifeSprite = new Image(new FileInputStream("Sprites/player.png"),30,25,true,false);
            backgroundImage = new Image(new FileInputStream("Sprites/SIBackground.png"),900,600,true,false);
            explosion = new Image(new FileInputStream("Sprites/deathExplosion.png"),25,20,false,false);
            shotCollision = new Image(new FileInputStream("Sprites/smallExplosion.png"),10,10,false,false);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Painter(MainUI mainUI, ChatUI chatUI, JukeBox jukebox, ChatController controller) {
        this.chatUI = chatUI;
        this.mainUI = mainUI;
        this.jukebox = jukebox;
        executor = Executors.newFixedThreadPool(5);
        this.chatController = controller;
        init();
    }

    private void init() {
        scoreLabel = new Label("");
        scoreLabel.setTextFill(Color.rgb(255,255,255));
        scoreLabel.setFont(Font.loadFont("file:fonts/ARCADE.TTF", 25));

        //  backgroundLayer = new Pane();

        canvas = new Canvas(600.0,400.0);
        canvas.setId("SpaceInvaders");

        spaceInvadersRoot = new GridPane();

        // ImageView backgroundImageView = new ImageView(backgroundImage);

        //  backgroundLayer.getChildren().add((backgroundImageView));


        root = new Pane();
        // root.getChildren().add(backgroundLayer);

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
        spaceInvadersRoot.setPrefSize(1200.0, 600.0);

        backButton.setFocusTraversable(false);
        soundButton.setFocusTraversable(false);
        controller = new Controller(canvas,this);
        this.player = controller.getPlayer();

        levelTitle = new Label("Level " + controller.getLevelCounter());
        levelTitle.setFont(Font.loadFont("file:fonts/ARCADE.TTF", 24));
        levelTitle.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        levelTitle.setTextFill(Color.WHITE);

        levelTitle.setLayoutX(250);
        levelTitle.setLayoutY(0);

        pauseLabel = new Label("Game Paused\n[P] to Resume");
        pauseLabel.setFont(Font.loadFont("file:fonts/ARCADE.TTF", 30));
        pauseLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        pauseLabel.setTextFill(Color.WHITE);
        pauseLabel.setLayoutX(210);
        pauseLabel.setLayoutY(210);

        root.getChildren().add(scoreLabel);
        root.getChildren().add(levelTitle);
        root.getChildren().add(canvas);
        spaceInvadersRoot.add(root,6,4,24,16);

        spaceInvadersRoot.setId("mainRoot");

        scene = new Scene(spaceInvadersRoot,1200,600, Color.BLACK);

        try {
            scene.getStylesheets().add((new File("styles//spaceStyle.css")).toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        gc = canvas.getGraphicsContext2D();
        addListeners(scene);

        canvas.requestFocus();
        canvas.setOnMouseMoved(e -> canvas.requestFocus());
        start(); // starts the animation timer
    }
        public void gameEnd(){
                chatController.newHighscore("Space Invaders",controller.getScore());
            System.out.println("just registered new highscore");
        }
    @Override
    public void handle(long now) {

        gc.clearRect(0, 0, 600, 400);

        scoreLabel.setText("Score: " + controller.getScore());
        levelTitle.setText("Level " + controller.getLevelCounter());

        for (Unit unit : controller.getAllUnits()) {
            if (unit.getPaused()){
                gc.setGlobalAlpha(0.50);
                gc.drawImage(unit.getSprite(), unit.getPosition().getX(), unit.getPosition().getY());
            }
            else {
                gc.setGlobalAlpha(100);
                gc.drawImage(unit.getSprite(), unit.getPosition().getX(), unit.getPosition().getY());
            }

        }
        for (int i = 0; i < player.getLife(); i++){
            gc.drawImage(playerLifeSprite,440+((i+1)*39),10);
        }

        checkDeadObjects();

        if (explosions.stream().anyMatch(Explosion::exploding)){
            for (Explosion e : new ArrayList<>(explosions)){
                gc.drawImage(explosion, e.getPosition().getX(), e.getPosition().getY());
            }
        }
        if (shotCollisions.stream().anyMatch(ShotCollision::enemyHitHappening)){
            for (ShotCollision e : new ArrayList<>(shotCollisions)){
                gc.drawImage(shotCollision,e.getPosition().getX(),e.getPosition().getY()-10);
            }
        }


    }

    public Scene getScene(){
        spaceInvadersRoot.add(chatUI,36,0,12,24);
        chatUI.setFocusTraversable(false);
        return scene;
    }

    public void setExplosionData(Position position){
        Explosion explosion = new Explosion(position);
        executor.execute(explosion);
        explosions.add(explosion);

    }

    public void setShotCollisionData(Position position){
        ShotCollision shotCollision = new ShotCollision(position);
        executor.execute(shotCollision);
        shotCollisions.add(shotCollision);
    }

    public void addNewScoreFloat(int score){
        ScoreUp scoreUp = new ScoreUp(score);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                root.getChildren().add(scoreUp);
            }
        });
        scoreFloats.add(scoreUp);
        executor.execute(scoreUp);
    }

    public void checkDeadObjects(){
        for (ScoreUp e : new ArrayList<>(scoreFloats)){
            if (!e.floating()){
                scoreFloats.remove(e);
                root.getChildren().remove(e);
            }
        }
        for (Explosion e : new ArrayList<>(explosions)){
            if (!e.exploding()){
                explosions.remove(e);
            }
        }
        for (ShotCollision e : new ArrayList<>(shotCollisions)){
            if (!e.enemyHitHappening()){
                shotCollisions.remove(e);
            }
        }
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

    //Sets and adds the arcade machine image for the SpaceInvaders game.
    public void setSpaceInvadersArcadeMachineImage() {
        try {
            spaceInvadersImage = new Image(new FileInputStream("images/spaceScreen.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        spaceInvadersView = new ImageView(spaceInvadersImage);
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
                        if (controller.getGamePaused())
                            break;
                        player.setTravelingLeftTrue();
                        break;

                    case D:
                    case RIGHT:
                        if (controller.getGamePaused())
                            break;
                        player.setTravelingRightTrue();
                        break;

                    case SPACE:
                        if (controller.getGamePaused())
                            break;
                        player.shoot();
                        break;
                    case P:
                        if (controller.getGamePaused())
                        {
                            root.getChildren().remove(pauseLabel);
                        }
                        else {
                            root.getChildren().add(pauseLabel);
                        }
                        controller.setGamePaused();
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case A:
                    case LEFT:
                        if (controller.getGamePaused())
                            break;
                        player.setTravelingLeftF();
                        break;

                    case D:
                    case RIGHT:
                        if (controller.getGamePaused())
                            break;
                        player.setTravelingRightF();
                        break;

                }
            }
        });

        backButton.setOnAction(e -> {
            spaceInvadersRoot.getChildren().remove(chatUI);
            if (!controller.getGamePaused()) {
                controller.setGamePaused();
                root.getChildren().add(pauseLabel);
            }
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

