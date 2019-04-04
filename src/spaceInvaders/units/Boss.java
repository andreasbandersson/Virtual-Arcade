package spaceInvaders.units;

import spaceInvaders.logic.Controller;

import java.awt.*;

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
