package spaceInvaders.units;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * @author Viktor Altintas
 */

public class Shot extends Unit implements Runnable {

    private static int width = 2;
    private static int height = 15;
    private int speed;
    private boolean direction;
    private boolean paused = false;
    private boolean flying = true;

    private static Image shot;

    static {
        try {
            shot = new Image(new FileInputStream("Sprites/shot.png"),width,height,true,false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public Shot(Position position, int speed, boolean direction, Controller controller){
        super(1,position,shot, width,height, controller);

        this.speed = speed;
        this.direction = direction;
    }

    @Override
    public void shoot() {}

    @Override
    public void registerHit() {
        die();
    }

    @Override
    public void die() {
        flying = false;
        controller.removeUnit(this);
    }
    public void setPaused() {
        paused = !paused;
    }

    public void start(){
        new Thread(this).start();
    }


    public void run() {
        while (flying) {
            while (paused){
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (direction) {
                move(getPosition().getX(), getPosition().getY() - 1);
            }
            else {
                move(getPosition().getX(), getPosition().getY() + 1);
            }
            controller.requestHitboxCheck();
            try {
                Thread.sleep(7/(speed+1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
