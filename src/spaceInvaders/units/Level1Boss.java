package spaceInvaders.units;

import javafx.scene.image.Image;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

    public Level1Boss(Position position, Controller controller, Difficulty difficulty) {
        super(70, position, enemySprite1,enemySprite2,width,height, controller, 100, difficulty);
    }

    @Override
    public void shoot() {
        controller.registerShot(this);
    }

    @Override
    public boolean willShoot() {
        return true;
    }

    @Override
    public Enemy clone() {
        return new Level1Boss(new Position(this.position),this.controller,this.difficulty);
    }
}
