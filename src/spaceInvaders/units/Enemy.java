package spaceInvaders.units;


import application.JukeBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;


/**
 * @author Viktor Altintas
 */

public abstract class Enemy extends Unit {

    private boolean alive = true;
    protected int points;
    protected Difficulty difficulty;
    private Image sprite1;
    private Image sprite2;
    private int animationCounter = 0;
    JukeBox jukebox;

    public Enemy(int life, Position position, Image image1,Image image2, int width, int height, Controller controller, int points, Difficulty difficulty) {
        super(life, position, image1, width, height, controller);
        sprite1 = image1;
        sprite2 = image2;
        this.points = points;
        this.difficulty = difficulty;
    }

    public void registerHit(){
        life--;
        if (life != 0) {
            controller.remotePainterAccess(new Position(shotThatHitYouPosition));
            jukebox = new JukeBox("sounds/enemyHit.mp3");
            jukebox.playWithCustomVol(0.2);
        }
        else
            die();
    }
    public int getPoints() {
        return points;
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setSprite(int spriteNumber){
        switch (spriteNumber){
            case 0:
                sprite = sprite1;
                break;
            case 1:
                sprite  = sprite2;

        }
    }

    public void setSprite(Image image){
        sprite = image;
    }

    public void updateAnimation() {
        setSprite(animationCounter);
        animationCounter++;
        animationCounter = (animationCounter) % 2;
    }

    @Override
    public void die(){
        alive = false;
        controller.removeUnit(this);
    }

    public abstract boolean willShoot();

    public abstract Enemy clone();

}
