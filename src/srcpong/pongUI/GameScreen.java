package pongUI;

import static pong.Config.*;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import pong.Platform;
import pong.Platform.Movement;
import pong.Game;

public class GameScreen extends Pane
{
    private final Game game;
    
    //ska ändras till en Cirkel 
   
    private final Rectangle ball = new Rectangle(BallSize, BallSize);
    private final Rectangle player = new Rectangle(PlatformWidth, PlatformHeight);
    private final Rectangle opponent = new Rectangle(PlatformWidth, PlatformHeight);
    
    private final Text playerScore = new Text("0");
    private final Text opponentScore = new Text("0");
    
    public GameScreen(Game game)
    {
        this.game = game;
    
        //"Ritar Objekt" Det som händer här är att den ritar inte utan binder ihop knutar i en annan klass 
        //och sen fyller de i färg så att de ser ut som platformar och boll 
        //Det är i style klassen som man färger dessa. 
        
    
       ball.translateXProperty().bind(game.getBall().xProperty());
       ball.translateYProperty().bind(game.getBall().yProperty());
       ball.getStyleClass().add("ball");
        
       player.translateXProperty().bind(game.getPlayer().xProperty());
       player.translateYProperty().bind(game.getPlayer().yProperty());
       player.getStyleClass().add("paddle");
        
       opponent.translateXProperty().bind(game.getComputer().xProperty());
       opponent.translateYProperty().bind(game.getComputer().yProperty());
       opponent.getStyleClass().add("paddle");
       
        
        playerScore.textProperty().bind(game.getPlayer().scoreProperty().asString());
        playerScore.boundsInLocalProperty().addListener(observable ->
        {
            /*
             * When using CSS, the width and height (with CSS applied) aren't available right away.
             * Therefore, we listen for changes and update the position once the width and height
             * are available.
             */
            playerScore.setTranslateX(WIDTH / 2 - ScoreDistance / 2 - playerScore.getBoundsInLocal().getWidth());
        });
        playerScore.setTranslateY(TxtMarginTB);
        playerScore.getStyleClass().add("score");
        
        opponentScore.textProperty().bind(game.getComputer().scoreProperty().asString());
        opponentScore.setTranslateX(WIDTH / 2 + ScoreDistance / 2);
        opponentScore.setTranslateY(TxtMarginTB);
        opponentScore.getStyleClass().add("score");
        
        setPrefSize(WIDTH, HEIGHT);
        getChildren().addAll(ball, player, opponent, playerScore, opponentScore);
        getStyleClass().add("screen");
        
        setOnKeyPressed(this::keyPressed);
        setOnKeyReleased(this::keyReleased);
    }
    
    private void keyPressed(KeyEvent event)
    {
        if (event.getCode() == KeyCode.P) {
            game.pause();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            game.endedGame();
        }
        
        if (game.getPlayer().getMovement() == Platform.Movement.NONE && event.getCode() == KeyCode.UP) {
            game.getPlayer().setMovement(Movement.UP);
        } else if (game.getPlayer().getMovement() == Movement.NONE && event.getCode() == KeyCode.DOWN) {
            game.getPlayer().setMovement(Movement.DOWN);
        }
    }

    private void keyReleased(KeyEvent event)
    {
        if (game.getPlayer().getMovement() == Movement.UP && event.getCode() == KeyCode.UP) {
            game.getPlayer().setMovement(Movement.NONE);
        } else if (game.getPlayer().getMovement() == Movement.DOWN && event.getCode() == KeyCode.DOWN) {
            game.getPlayer().setMovement(Movement.NONE);
        }
    }
}
