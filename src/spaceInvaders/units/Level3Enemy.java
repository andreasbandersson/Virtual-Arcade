package spaceInvaders.units;

import javafx.scene.image.Image;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

/**
 * a class that holds the properties of a level three enemy.
 * @author Viktor Altintas
 */

public class Level3Enemy extends Enemy {  private static Image enemySprite1;
    private static Image enemySprite2;

    static {
        try {
            enemySprite1 = new Image(new FileInputStream("Sprites/enemyBlue.png"),25,20,false,false);
            enemySprite2 = new Image(new FileInputStream("Sprites/enemyBlue2.png"),25,20,false,false);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private Random rand = new Random();

    /**
     * constructor
     * @param position the startposition for the enemy
     * @param controller the controller for the logic
     * @param difficulty the difficulty of the enemy
     */
    public Level3Enemy(Position position, Controller controller, Difficulty difficulty) {
        super(2, position, enemySprite1,enemySprite2,35,30, controller, 50, difficulty);
    }

    /**
     * calls registerShot in the controller
     */
    @Override
    public void shoot() {
        controller.registerShot(this);
    }

    /**
     * calculates if the enemy will shoot
     * @return true if shot is generated
     */
    @Override
    public boolean willShoot() {
        int r = rand.nextInt(1000);

        if (difficulty.equals(Difficulty.EASY))
            return (r <= 70 - controller.getAllUnits().size());

        if (difficulty.equals(Difficulty.MEDIUM))
            return (r <= 100 - controller.getAllUnits().size());

        if (difficulty.equals(Difficulty.HARD))
            return (r <= 120 - controller.getAllUnits().size());

        return false;
    }

    /**
     * a clone of the unit
     * @return a clone of the unit
     */
    @Override
    public Enemy clone() {
        return new Level3Enemy(new Position(this.position),this.controller,this.difficulty);
    }
}
