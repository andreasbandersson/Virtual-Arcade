package spaceInvaders.Effects;

import spaceInvaders.units.Position;

public class ShotCollision implements Runnable{
    private Position position;
    private boolean enemyHitHappened;

    public ShotCollision(Position position){
        this.position = position;
        enemyHitHappened = true;
    }

    public Position getPosition(){
        return position;
    }

    public boolean enemyHitHappening() {
        return enemyHitHappened;
    }

    public void run() {
        try {
            Thread.sleep(300);
            enemyHitHappened = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
