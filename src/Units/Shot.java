package Units;

import Logic.Controller;

import javax.swing.*;
import java.awt.*;

public class Shot extends Unit {

    private static int width = 2;
    private static int height = 15;
    private int speed;
    private boolean direction;
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
        flying = false;
        controller.getUnits().remove(this);
    }

    public void run() {
        while (flying) {
            if (direction) {
                move(getPosition().getX(), getPosition().getY() - 1);
            }
            else {
                move(getPosition().getX(), getPosition().getY() + 1);
            }
            controller.requestRepaint();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
