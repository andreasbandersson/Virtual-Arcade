package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.Enemy;

import java.util.ArrayList;

public abstract class Level {

    ArrayList<Enemy> enemyList;
    Difficulty difficulty;
    private Controller controller;

    public Level(Difficulty difficulty, ArrayList<Enemy> enemies, Controller controller) {
        this.difficulty = difficulty;
        this.enemyList = enemies;
        this.controller = controller;
    }

    public ArrayList<Enemy> getEnemyList() {
        for (Enemy e : enemyList){
            e.setController(controller);
        }
        return enemyList;
    }
}
