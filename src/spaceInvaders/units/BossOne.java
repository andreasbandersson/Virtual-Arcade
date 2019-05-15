package spaceInvaders.units;

import javafx.scene.image.Image;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BossOne extends Enemy {


    private static Image bossSprite1;
    private static Image bossSprite2;
    private static Image bossSprite3;
    private static Image bossSprite4;
    private int animationCounter = 0;

    static {
        try {
            bossSprite1 = new Image(new FileInputStream("Sprites/boss1.1.png"),50,50,false,false);
            bossSprite2 = new Image(new FileInputStream("Sprites/boss1.2.png"),50,50,false,false);
            bossSprite3 = new Image(new FileInputStream("Sprites/boss1.3.png"),50,50,false,false);
            bossSprite4 = new Image(new FileInputStream("Sprites/boss1.4.png"),50,50,false,false);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public BossOne(int life, Position position,  Controller controller) {
        super(life, position, bossSprite1,bossSprite2, 35, 35, controller,500,Difficulty.EASY);
    }


    public void run() {
        while (true) {
            while (paused){
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            updateAnimation();
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSprite(int spriteNumber){
        switch (spriteNumber){
            case 0:
                sprite = bossSprite1;
                break;
            case 1:
                sprite  = bossSprite2;
                break;
            case 2:
                sprite = bossSprite3;
                break;
            case 3:
                sprite = bossSprite4;
        }
    }

    public void updateAnimation() {
        setSprite(animationCounter);
        animationCounter++;
        animationCounter = (animationCounter) % 4;
    }

    @Override
    public void shoot() {

    }

    @Override
    public void die() {

    }

    @Override
    public boolean willShoot() {
        return false;
    }

    @Override
    public Enemy clone() {
        return null;
    }
}
