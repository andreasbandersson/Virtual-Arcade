package spaceInvaders.units;

import javafx.scene.image.Image;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * an extension of shot, made for enemies.
 * @author Viktor Altintas
 */

public class EnemyShot extends Shot {

    private static int width = 6;
    private static int height = 6;
    private static Image shot;
    private Difficulty difficulty;

    static {
        try {
            shot = new Image(new FileInputStream("Sprites/shotEnemy.png"),width,height,true,false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * constructor
     * @param position the position it was fired from
     * @param speed the speed the shot will travel
     * @param direction the direction of the shot
     * @param controller the controller for the logic
     */
    public EnemyShot(Position position, int speed, boolean direction, Controller controller) {
        super(position, speed, direction, controller);
        setSprite(shot);
    }

    /**
     * getter
     * @return the difficulty setting of the shooter
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }
}
