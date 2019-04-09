package spaceInvaders.units;


import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;

import java.awt.*;

/**
 * @author Viktor Altintas
 */

public abstract class Enemy extends Unit {

    private boolean alive = true;
    protected int points;
    protected Difficulty difficulty;

    public Enemy(int life, Position position, Image image, int width, int height, Controller controller, int points, Difficulty difficulty) {
        super(life, position, image, width, height, controller);
        this.points = points;
        this.difficulty = difficulty;
    }

    public void registerHit(){
        life--;
        if (life == 0) {
            die();
        }
    }
    public int getPoints() {
        return points;
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public void die(){
        alive = false;
        controller.removeUnit(this);
    }

    public abstract boolean willShoot();

    public abstract Enemy clone();

}
