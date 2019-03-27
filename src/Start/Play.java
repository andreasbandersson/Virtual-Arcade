package Start;

import Graphics.GameFrame;
import Graphics.Painter;
import Levels.Difficulty;
import Logic.Controller;

public class Play {

    public static void main(String[] args) {
        new GameFrame(new Controller(Difficulty.EASY));
    }
}
