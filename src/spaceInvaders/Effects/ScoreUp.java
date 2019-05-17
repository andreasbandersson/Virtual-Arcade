package spaceInvaders.Effects;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class ScoreUp extends Label implements Runnable{

    private boolean scoreFloating;
    private int score;
    private int y = 35;
    private Thread thread;

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
        thread = new Thread(this);
        thread.start();
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
        try {
            for (int i = 0; i < 13; i++) {
                y -= 3;
                setTranslateY(y);
                thread.sleep(30);
            }
            scoreFloating = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
