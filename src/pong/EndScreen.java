package pong;

import static pong.Config.*;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class EndScreen extends Pane
{
    private Runnable onRestart = () -> {}; /* Do nothing for now. */
    
    public void setOnRestart(Runnable onRestart)
    {
        this.onRestart = onRestart;
    }
    
    private final Text header = new Text();
    
    public void setScore(int playerScore)
    {
        header.setText(playerScore == WinScore ? "you win" : "you lose");
    }
    
    public EndScreen()
    {
        header.boundsInLocalProperty().addListener(observable ->
        {
            /*
             * When using CSS, the width and height (with CSS applied) aren't available right away.
             * Therefore, we listen for changes and update the position once the width and height
             * are available.
             */
            header.setTranslateX((WIDTH - header.getBoundsInLocal().getWidth()) / 2); /* Centered. */
            header.setTranslateY( TxtMarginTB);
        });
        header.getStyleClass().add("header");
        
        Text info = new Text("press enter to restart\npress escape to quit");
        info.boundsInLocalProperty().addListener(observable ->
        {
            /*
             * When using CSS, the width and height (with CSS applied) aren't available right away.
             * Therefore, we listen for changes and update the position once the width and height
             * are available.
             */
            info.setTranslateX((WIDTH - info.getBoundsInLocal().getWidth()) / 2); /* Centered. */
            info.setTranslateY(HEIGHT -  TxtMarginTB - info.getBoundsInLocal().getHeight());
        });
        info.getStyleClass().add("info");
        
        setPrefSize(WIDTH, HEIGHT);
        getChildren().addAll(header, info);
        getStyleClass().add("screen");
        
        setOnKeyPressed(event ->
        {
            if (event.getCode() == KeyCode.ENTER) {
                onRestart.run();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });
    }
}