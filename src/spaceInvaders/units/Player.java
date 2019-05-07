package spaceInvaders.units;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import spaceInvaders.logic.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Viktor Altintas
 */

public class Player extends Unit {

    private static Image playerSprite;

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

    }

    public Image getPlayerSprite() {
        return playerSprite;
    }

    @Override
    public void shoot() {
        controller.registerShot(this);
    }

    public Position getStartPosition(){
        return startPosition;
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
