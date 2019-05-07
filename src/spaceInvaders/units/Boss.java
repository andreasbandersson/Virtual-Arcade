package spaceInvaders.units;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import spaceInvaders.logic.Controller;



/**
 * @author Viktor Altintas
 */

public abstract class Boss extends Unit implements Runnable {

    public Boss(int life, Position position, Image image, int width, int height, Controller controller) {
        super(life, position, image, width, height, controller);
    }

    @Override
    public void shoot() {

    }

    @Override
    public void registerHit() {

    }

    public void start(){
        new Thread(this).start();
    }


}
