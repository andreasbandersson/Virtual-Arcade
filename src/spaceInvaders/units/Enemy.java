package spaceInvaders.units;


import spaceInvaders.logic.Controller;

import java.awt.*;

public abstract class Enemy extends Unit {

    private boolean alive = true;

    public Enemy(int life, Position position, Image image, int width, int height, Controller controller) {
        super(life, position, image, width, height, controller);
    }

    public void registerHit(){
        life--;
        if (life == 0) {
            alive = false;
            isDead();
        }
    }

    public abstract void moveEnemy();
    public abstract void sleep();

    public void run() {
        while(alive){
            moveEnemy();
            controller.requestRepaint();
            sleep();
        }
    }
}
