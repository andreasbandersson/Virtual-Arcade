package spaceInvaders.units;

import spaceInvaders.logic.Controller;

import javax.swing.*;
import java.awt.*;

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

    private static Image Shot = new ImageIcon("Sprites/shot.png").getImage().getScaledInstance(width,height,Image.SCALE_DEFAULT);


    public Shot(Position position, int speed, boolean direction, Controller controller){
        super(1,position,Shot, width,height, controller);

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
            controller.requestRepaint();
            try {
                Thread.sleep(9/(speed+1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
