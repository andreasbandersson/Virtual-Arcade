package spaceInvaders.units;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

/**
 * a class that holds the properties of a level one enemy.
 * @author Viktor Altintas
 */

public class Level1Enemy extends Enemy {

    private static Image enemySprite1;
    private static Image enemySprite2;

    static {
        try {
            enemySprite1 = new Image(new FileInputStream("Sprites/enemyYellow.png"),25,20,false,false);
            enemySprite2 = new Image(new FileInputStream("Sprites/enemyYellow2.png"),25,20,false,false);

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
    public Level1Enemy(Position position, Controller controller, Difficulty difficulty) {
        super(1, position, enemySprite1,enemySprite2,35,30, controller, 20, difficulty);
    }

    /**
     * calls the registershot method in controller
     */
    @Override
    public void shoot() {
        controller.registerShot(this);
    }

    /**
     * calculates if the enemy will shoot.
     * @return true if shot was made
     */
    @Override
    public boolean willShoot() {
        int r = rand.nextInt(1000);

        if (difficulty.equals(Difficulty.EASY))
            return (r <= 50 - controller.getAllUnits().size());

        if (difficulty.equals(Difficulty.MEDIUM))
            return (r <= 80 - controller.getAllUnits().size());

        if (difficulty.equals(Difficulty.HARD))
            return (r <= 100 - controller.getAllUnits().size());

        return false;
    }

    /**
     * getter
     * @return a clone of the Unit
     */
    @Override
    public Enemy clone() {
        return new Level1Enemy(new Position(this.position),this.controller,this.difficulty);
    }
}
