package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.Boss;
import spaceInvaders.units.Enemy;

import java.util.List;

public abstract class Level {

    private List<List<Enemy>> enemyGrid;
    private Difficulty difficulty;
    private Controller controller;

    public Level(Difficulty difficulty, List<List<Enemy>> enemies, Controller controller) {
        this.difficulty = difficulty;
        this.enemyGrid = enemies;
        this.controller = controller;
    }

    public List<List<Enemy>> getEnemyGrid() {
        for(List<Enemy> row : enemyGrid) {
            for (Enemy e : row) {
                e.setController(controller);
            }
        }
        return enemyGrid;
    }

    public abstract List<Boss> getBosses();

}
