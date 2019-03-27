package Levels;

import Logic.Controller;
import Units.Enemy;
import Units.Level1Enemy;
import Units.Position;
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
