package spaceInvaders.units;

import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * @author Viktor Altintas
 */

public class Level1Enemy extends Enemy {

    private static final Image sprite = new ImageIcon("Sprites/enemyYellow.png").getImage().getScaledInstance(35,30,Image.SCALE_DEFAULT);;
    private Random rand = new Random();

    public Level1Enemy(Position position, Controller controller, Difficulty difficulty) {
        super(1, position, sprite,35,30, controller, 20, difficulty);
    }

    @Override
    public void shoot() {
        controller.registerShot(this);
    }


    @Override
    public boolean willShoot() {
        int r = rand.nextInt(1000) - (getDifficulty().ordinal()*15) - controller.getAllUnits().size();
        return (r >= 890);
    }

    @Override
    public Enemy clone() {
        return new Level1Enemy(new Position(this.position),this.controller,this.difficulty);
    }
}
