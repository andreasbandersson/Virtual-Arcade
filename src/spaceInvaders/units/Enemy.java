package spaceInvaders.units;



import application.JukeBox2;
import javafx.scene.image.Image;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.logic.Controller;


/**
 * an abstraction of unit, made for enemies
 * @author Viktor Altintas
 */

public abstract class Enemy extends Unit {

    private boolean alive = true;
    protected int points;
    protected Difficulty difficulty;
    private Image sprite1;
    private Image sprite2;
    private int animationCounter = 0;
    private JukeBox2 jukebox = new JukeBox2();

    /**
     * constructor, passes on most parameters of the unit onto the unit constructor, and the remaining parameters
     * solly belong to enemies.
     */
    public Enemy(int life, Position position, Image image1,Image image2, int width, int height, Controller controller, int points, Difficulty difficulty) {
        super(life, position, image1, width, height, controller);
        sprite1 = image1;
        sprite2 = image2;
        this.points = points;
        this.difficulty = difficulty;
    }

    /**
     * if this unit is hit, substracts from life and checks if the enemy is dead after the substraction.
     */
    public void registerHit(){
        life--;
        if (life != 0) {
            controller.remotePainterAccess(new Position(shotThatHitYouPosition));
           jukebox.playMP3(JukeBox2.ENEMYHIT);
        }
        else
            die();
    }


    /**
     * getter
     * @return the points awarded from this enemy
     */
    public int getPoints() {
        return points;
    }

    /**
     * getter
     * @return the difficulty of the enemy
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * alternates the sprite between sprite 1 or sprite 2
     * @param spriteNumber which sprite number is to be displayed
     */
    public void setSprite(int spriteNumber){
        switch (spriteNumber){
            case 0:
                sprite = sprite1;
                break;
            case 1:
                sprite  = sprite2;

        }
    }

    /**
     * sets the sprite to the given image
     * @param image the given image
     */
    public void setSprite(Image image){
        sprite = image;
    }

    /**
     * updates the sprite state for the enemy
     */
    public void updateAnimation() {
        setSprite(animationCounter);
        animationCounter++;
        animationCounter = (animationCounter) % 2;
    }

    /**
     * kills the enemy by setting its runnable boolean to false, and removing it from the controllers unitlist.
     */
    @Override
    public void die(){
        alive = false;
        controller.removeUnit(this);
    }

    public abstract boolean willShoot();

    public abstract Enemy clone();

}
