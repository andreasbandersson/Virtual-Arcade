package spaceInvaders.Effects;


import spaceInvaders.units.Position;

public class Explosion extends Thread{
    private Position position;
    private boolean explosionHappening;

    public Explosion(Position position){
        this.position = position;
        explosionHappening = true;
        start();
    }

    public Position getPosition(){
        return position;
    }

    public boolean exploding() {
        return explosionHappening;
    }

    public void run() {
        try {
            this.sleep(300);
            explosionHappening = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
