package spaceInvaders.Effects;


import spaceInvaders.units.Position;

public class Explosion implements Runnable{
    private Position position;
    private boolean explosionHappening;

    public Explosion(Position position){
        this.position = position;
        explosionHappening = true;

    }

    public Position getPosition(){
        return position;
    }

    public boolean exploding() {
        return explosionHappening;
    }

    public void run() {
        try {
            Thread.sleep(300);
            explosionHappening = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
