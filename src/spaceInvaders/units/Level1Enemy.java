package spaceInvaders.units;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

/**
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

    public Level1Enemy(Position position, Controller controller, Difficulty difficulty) {
        super(1, position, enemySprite1,enemySprite2,35,30, controller, 20, difficulty);
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
