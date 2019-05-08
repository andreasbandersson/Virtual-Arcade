package spaceInvaders.units;


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
    private int animationCounter = 1;

    public Enemy(int life, Position position, Image image1,Image image2, int width, int height, Controller controller, int points, Difficulty difficulty) {
        super(life, position, image1, width, height, controller);
        sprite1 = image1;
        sprite2 = image2;
        this.points = points;
        this.difficulty = difficulty;
    }

    public void registerHit(){
        life--;
        if (life == 0) {
            die();
        }
    }
    public int getPoints() {
        return points;
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setSprite(int spriteNumber){
        switch (spriteNumber){
            case 1:
                sprite = sprite1;
                break;
            case 2:
                sprite  = sprite2;
        }
    }

    public void setSprite(Image image){
        sprite = image;
    }

    public void updateAnimation() {
        if (animationCounter == 2){
            animationCounter--;
        }
        else {
            animationCounter++;
        }
        setSprite(animationCounter);
        //animationCounter = (animationCounter+1) % 2;

    }
    @Override
    public void die(){
        alive = false;
        controller.removeUnit(this);
    }

    public abstract boolean willShoot();

    public abstract Enemy clone();

}
