package spaceInvaders.units;

import javafx.scene.image.Image;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * this class holds the properties for the first boss.
 * @author Viktor Altintas
 */
public class Level1Boss extends Enemy {

    private static Image enemySprite1;
    private static Image enemySprite2;
    private static int width = 100;
    private static int height = 80;

    static {
        try {
            enemySprite1 = new Image(new FileInputStream("Sprites/enemyYellow.png"),width,height,false,false);
            enemySprite2 = new Image(new FileInputStream("Sprites/enemyYellow2.png"),width,height,false,false);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * constructor
     * @param position the startposition for the boss
     * @param controller the controller for the logic
     * @param difficulty the difficulty of the boss
     */
    public Level1Boss(Position position, Controller controller, Difficulty difficulty) {
        super(70, position, enemySprite1,enemySprite2,width,height, controller, 100, difficulty);
    }


    /**
     * calls the registershot method in controller
     */
    @Override
    public void shoot() {
        controller.registerShot(this);
    }

    /**
     * calculates if the enemy will shoot. always true because the bosses always shoot
     * @return true
     */
    @Override
    public boolean willShoot() {
        return true;
    }

    /**
     * getter
     * @return a clone of the bossUnit
     */
    @Override
    public Enemy clone() {
        return new Level1Boss(new Position(this.position),this.controller,this.difficulty);
    }
}
