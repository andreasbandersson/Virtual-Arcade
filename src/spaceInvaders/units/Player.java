package spaceInvaders.units;

import spaceInvaders.logic.Controller;

import javax.swing.*;
import java.awt.*;

public class Player extends Unit {

    private static Image playerSprite = new ImageIcon("Sprites/player.png").getImage().getScaledInstance(50,40,Image.SCALE_DEFAULT);
    private static Position startPosition = new Position(350,500);

    public Player(Controller controller) {
        super(3, startPosition, playerSprite,50,40, controller);

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
            controller.isDead(this);
        }
        if (life == 0){
            //TERMINATE GAME, YOU LOST
        }
    }
}
