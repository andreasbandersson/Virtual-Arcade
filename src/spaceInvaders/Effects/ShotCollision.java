package spaceInvaders.Effects;

import spaceInvaders.units.Position;


/**
 * holds a runnable that handles the shotCollision animation timers
 * @author Viktor Altintas
 */

public class ShotCollision implements Runnable{
    private Position position;
    private boolean enemyHitHappened;

    /**
     * constructor
     * @param position the given position for the shot collision
     */
    public ShotCollision(Position position){
        this.position = position;
        enemyHitHappened = true;
    }

    /**
     * getter
     * @return the position
     */
    public Position getPosition(){
        return position;
    }

    /**
     *
     * @return the boolean if the collision is still going on
     */
    public boolean enemyHitHappening() {
        return enemyHitHappened;
    }

    /**
     * run that turns the boolean false after 300 ms
     */
    public void run() {
        try {
            Thread.sleep(300);
            enemyHitHappened = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
