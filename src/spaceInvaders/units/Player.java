package spaceInvaders.units;

import application.JukeBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import spaceInvaders.graphics.Painter;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * the player unit
 * @author Viktor Altintas
 */

public class Player extends Unit implements Runnable {

    private static Image playerSprite;
    private boolean travelingLeft = false;
    private boolean travelingRight = false;
    private Painter painter;


    static {
        try {
            playerSprite = new javafx.scene.image.Image(new FileInputStream("Sprites/player.png"),40,30,true,false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Position startPosition = new Position(270,350);

    /**
     * constructor
     * @param controller the controller for the logic
     * @param painter the painter, made for sending positiondata of the player when it collides with a shot
     */
    public Player(Controller controller, Painter painter) {
        super(3, new Position(startPosition), playerSprite,50,40, controller);
        this.painter = painter;
        new Thread(this).start();

    }

    /**
     * calls registerShot in the controller
     */
    @Override
    public void shoot() {
        controller.registerShot(this);
    }

    /**
     * setter for the boolean travelingLeft
     */
    public void setTravelingLeftTrue(){
        travelingLeft = true;
    }

    /**
     * setter for the boolean travelingRight
     */
    public void setTravelingRightTrue(){
        travelingRight = true;
    }

    /**
     * setter for the boolean travelingLeft
     */
    public void setTravelingLeftF() {
        this.travelingLeft = false;
    }

    /**
     * setter for the boolean travelingRight
     */
    public void setTravelingRightF() {
        travelingRight = false;
    }

    /**
     * runnable that moves the player character as long as game isnt paused and the player is holding down the button to
     * steer in one direction
     */
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

    /**
     * sets lifepoints back to the given parameter
     * @param healthpoints the healthpoints the player should be restored to
     */
    public void renewLife(int healthpoints){
        life += healthpoints;
    }

    /**
     * subtracts life from the player, if 0 then game ends
     */
    @Override
    public void registerHit() {
        life--;
        if (life > 0){
            painter.setExplosionData(new Position(position));
            JukeBox jukeBox = new JukeBox("sounds/Explosion.mp3");
            jukeBox.playWithCustomVol(0.4);
           // move(Player.startPosition);
        }
        if (life == 0){
           painter.gameEnd();
        }
    }

    @Override
    public void die() {
    }
}
