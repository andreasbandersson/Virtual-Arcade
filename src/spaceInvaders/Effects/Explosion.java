package spaceInvaders.Effects;


import spaceInvaders.units.Position;

/**
 * holds a runnable that handles the explosion animation timers
 * @author Viktor Altintas
 */

public class Explosion implements Runnable{
    private Position position;
    private boolean explosionHappening;

    /**
     * constructor
     * @param position the position of the explosion
     */
    public Explosion(Position position){
        this.position = position;
        explosionHappening = true;

    }

    /**
     * getter for position
     */
    public Position getPosition(){
        return position;
    }

    /**
     *
     * @return the boolean if explosion is happening
     */
    public boolean exploding() {
        return explosionHappening;
    }

    /**
     * the run that sets the boolean to false after 300 ms.
     */
    public void run() {
        try {
            Thread.sleep(300);
            explosionHappening = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
