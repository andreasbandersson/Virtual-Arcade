package spaceInvaders.units;

import javafx.scene.image.Image;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
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

    public EnemyShot(Position position, int speed, boolean direction, Controller controller) {
        super(position, speed, direction, controller);
        this.difficulty = difficulty;
        setSprite(shot);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
