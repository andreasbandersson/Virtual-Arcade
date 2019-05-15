package spaceInvaders.units;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Viktor Altintas
 */

public class Player extends Unit implements Runnable {

    private static Image playerSprite;
    private boolean travelingLeft = false;
    private boolean travelingRight = false;
    private boolean standStill = true;

    static {
        try {
            playerSprite = new javafx.scene.image.Image(new FileInputStream("Sprites/player.png"),40,30,true,false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Position startPosition = new Position(270,350);

    public Player(Controller controller) {
        super(3, new Position(startPosition), playerSprite,50,40, controller);
        new Thread(this).start();

    }

    public Image getPlayerSprite() {
        return playerSprite;
    }

    @Override
    public void shoot() {
        controller.registerShot(this);
    }


    public void setTravelingLeftTrue(){
        travelingLeft = true;
    }

    public void setTravelingRightTrue(){
        travelingRight = true;
    }

    public void setTravelingLeftF() {
        this.travelingLeft = false;
    }

    public void setTravelingRightF() {
        travelingRight = false;
    }

    public void run() {
        while (true){
            while (!travelingLeft && !travelingRight || paused) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                if (travelingLeft){
                    this.move(getPosition().getX()-1,getPosition().getY());
                }
               if (travelingRight){
                    this.move(getPosition().getX()+1,getPosition().getY());
                }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void registerHit() {
        life--;
        if (life > 0){
            move(Player.startPosition);
        }
        if (life == 0){
           die();
        }
    }

    @Override
    public void die() {
        //game ends
    }
}
