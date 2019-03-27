package Units;

import Logic.Controller;

import javax.swing.*;
import java.awt.*;

public class Level1Enemy extends Enemy {

    private static final Image sprite = new ImageIcon("Sprites/enemyYellow.png").getImage().getScaledInstance(35,30,Image.SCALE_DEFAULT);;
    private int counter = 0;

    public Level1Enemy(Position position, Controller controller) {
        super(1, position, sprite,35,30, controller);
    }

    @Override
    public void shoot() {
    }

    public void moveEnemy(){
        switch (counter) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                move(getPosition().getX()-15,getPosition().getY());
                break;
            case 5:
                move(getPosition().getX(),getPosition().getY()+40);
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                move(getPosition().getX()+15,getPosition().getY());
                break;
            case 11:
                move(getPosition().getX(),getPosition().getY()+40);
                break;
        }
        counter++;
        counter = counter % 12;
    }

    @Override
    public void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
