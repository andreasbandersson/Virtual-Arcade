package spaceInvaders.units;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * this class holds the data for a shot and updates its movement
 * @author Viktor Altintas
 */

public class Shot extends Unit implements Runnable {

    private static int width = 3;
    private static int height = 15;
    private int speed;
    private boolean direction;
    private boolean flying = true;

    private static Image shot;

    static {
        try {
            shot = new Image(new FileInputStream("Sprites/shot.png"),width,height,true,false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * constructor
     * @param position the startposition
     * @param speed the speed of the shot
     * @param direction the direction it will travel
     * @param controller the controller for the logic
     */
    public Shot(Position position,int speed, boolean direction, Controller controller){
        super(1,position,shot, width,height, controller);
        this.speed = speed;
        this.direction = direction;
    }

    @Override
    public void shoot() {}

    /**
     * calls the die-method
     */
    @Override
    public void registerHit() {
        die();
    }

    /**
     * kills the shot
     */
    @Override
    public void die() {
        flying = false;
        controller.removeUnit(this);
    }

    /**
     * stops it from flying
     */
    public void remoteKill(){
        flying = false;
    }

    /**
     * moves the shot and requests a check if it hit something
     */
    public void run() {
        while (flying) {
            while (paused){
                try {
                    Thread.sleep(50);
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
                Thread.sleep(1 + speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
