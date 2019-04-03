package spaceInvaders.units;

import spaceInvaders.logic.Controller;

import javax.swing.*;
import java.awt.*;

public class Level1Enemy extends Enemy {

    private static final Image sprite = new ImageIcon("Sprites/enemyYellow.png").getImage().getScaledInstance(35,30,Image.SCALE_DEFAULT);;
    private int counter = 0;

    public Level1Enemy(Position position, Controller controller) {
        super(1, position, sprite,35,30, controller);
    }

    @Override
    public void shoot() {
    }

    public void moveEnemy(){}

    @Override
    public void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
