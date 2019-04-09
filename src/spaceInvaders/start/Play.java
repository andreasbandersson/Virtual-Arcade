package spaceInvaders.start;

import spaceInvaders.graphics.GameFrame;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;

/**
 * @author Viktor Altintas
 */

public class Play {

    public static void main(String[] args) {
        new GameFrame(new Controller());
    }
}
