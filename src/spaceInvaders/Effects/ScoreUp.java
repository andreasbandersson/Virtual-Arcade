package spaceInvaders.Effects;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class ScoreUp extends Label implements Runnable{

    private boolean scoreFloating;
    private int score;
    private int y = 35;

    public ScoreUp(int score){
        this.score = score;
        setText("+" +score);
        scoreFloating = true;
        setFont(Font.loadFont("file:fonts/ARCADE.TTF", 17));
        setTranslateX(110);
        if (score < 50)
        setTextFill(Color.YELLOW);

        else if (score < 100){
            setTextFill(Color.MAGENTA);
        }
        else {
            setTextFill(Color.ORANGE);
        }
    }

    public int getScore(){
        return score;
    }

    public int getY(){
        return y;
    }

    public boolean floating() {
        return scoreFloating;
    }

    public void run() {

            for (int i = 0; i < 30; i++) {
                y -= 1;
                setTranslateY(y);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            scoreFloating = false;
    }
}
