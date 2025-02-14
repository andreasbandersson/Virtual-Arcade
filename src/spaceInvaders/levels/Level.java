package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.Boss;
import spaceInvaders.units.Enemy;
import spaceInvaders.units.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class that provides the framework for the various levels
 * @author Viktor Altintas
 */

public abstract class Level {

    private List<List<Enemy>> enemyGrid;
    private Difficulty difficulty;
    protected Controller controller;

    /**
     * constructor
     * @param difficulty the difficulty
     * @param controller the controller for the logic
     */
    public Level(Difficulty difficulty, List<List<Enemy>> enemies, Controller controller) {
        this.difficulty = difficulty;
        this.enemyGrid = enemies;
        this.controller = controller;
    }

    /**
     * creates a copy of the enemylist and returns it
     * @return the copied enemylist
     */
    public List<List<Enemy>> getEnemyGrid() {
        List<List<Enemy>> enemyGridClone = new ArrayList<>(); //makes a list that takes listobjects
        for(List<Enemy> row : enemyGrid) {
            ArrayList<Enemy> enemyList = new ArrayList<>(); // making a new row
            enemyGridClone.add(enemyList); //adding row to list of rows
            for (Enemy e : row) {
                Enemy clone = e.clone();
                enemyList.add(clone);
               clone.setController(controller);
            }
        }
        return enemyGridClone;
    }

    /**
     * @return new ArrayList
     */
    public abstract List<Boss> getBosses();

}
