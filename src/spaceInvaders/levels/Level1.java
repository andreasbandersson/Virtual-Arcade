package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.Enemy;
import spaceInvaders.units.Level1Enemy;
import spaceInvaders.units.Position;

import java.util.ArrayList;

public class Level1 extends Level {

    private static final ArrayList<Enemy> enemies = new ArrayList<>();
    static {
        enemies.add(new Level1Enemy(new Position(400,500),null));
        for (int i  = 0 ; i < 5; i ++) {
            for (int j = 0; j < 10; j++ ) {
                enemies.add(new Level1Enemy(new Position((j*65),(i*45)),null));
            }
        }
    }

    public Level1(Difficulty difficulty, Controller controller) {
        super(difficulty, enemies, controller);
    }
}
