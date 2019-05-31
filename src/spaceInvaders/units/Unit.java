package spaceInvaders.units;

import javafx.scene.image.Image;
import spaceInvaders.logic.Controller;

import java.awt.*;

/**
 * the abstract class that holds the base methods and properties for all units in the game
 * @author Viktor Altintas
 */

public abstract class Unit {

   protected int life;
   protected Position position;
   protected Image sprite;
   private int width;
   private int height;
   protected Controller controller;
   protected Position shotThatHitYouPosition;
   protected boolean paused = false;

    /**
     *
     * @param life the life of a unit
     * @param position the position of a unit
     * @param image the sprite of a unit
     * @param width the width of a unit
     * @param height the height of a unit
     * @param controller the controller for the logic
     */
   public Unit(int life, Position position, Image image, int width, int height, Controller controller) {
       this.life = life;
       this.position = position;
       this.sprite = image;
       this.width = width;
       this.height = height;
       this.controller = controller;
   }

   /**
    * changes the pause state
    */
    public void setPaused() {
        paused = !paused;
    }

    /**
     * getter for the pause state
     */
    public boolean getPaused(){
       return paused;
    }

    /**
     * sets the position to the param
     * @param position the target position
     */
    public synchronized void setPosition(Position position){
       this.position = position;
   }

    /**
     * sets the sprite to the param
     * @param image the target sprite
     */
   public void setSprite(Image image){
       this.sprite = image;
   }

    /**
     * getter
     * @return the sprite
     */
    public Image getSprite() {
        return sprite;
    }

    /**
     * setter for the controller
     * @param controller the controller
     */
    public void setController(Controller controller) {
       this.controller = controller;
    }

    /**
     * getter for the life of a unit
     */
    public int getLife() {
        return life;
    }

    /**
     * getter for the height of a unit
     */
    public int getHeight() {
        return height;
    }

    /**
     * getter for the width of a unit
     */
    public int getWidth() {
        return width;
    }

    /**
     * getter for the position of a unit
     */
    public synchronized Position getPosition() {
        return position;
    }

    public abstract void shoot();

    /**
     * sets the position of the shot that hit this unit to the param position
     * @param position the position where the shot hit this unit
     */
   public void sendShotPositionData(Position position){
       shotThatHitYouPosition = new Position(position);
   }

    /**
     * moves the unit
     * @param x the target x
     * @param y the target y
     */
   public synchronized void move(int x, int y){
       position.setX(x);
       position.setY(y);
   }

    /**
     * moves the unit
     * @param x the target x
     * @param y the target y
     */
    public synchronized void moveRelative(int x, int y){
        position.setX(position.getX() + x);
        position.setY(position.getY() + y);
    }

    public abstract void registerHit();

    public abstract void die();


    /**
     * creates a rectangle object, acting as a hitbox for the unit
     * @return the rectangle object
     */
   private Rectangle getHitbox() {
       return new Rectangle(position.getX(),position.getY(),width,height);
   }

    /**
     * returns true or false if the reported collision was between objects that are allowed to collide or not.
     * @param unit the unit that collided with this unit
     * @return true or false if the collision is allowed in the game or not
     */
    public boolean collidesWith (Unit unit){

       if (this.equals(unit)){
           return false;
       }

       if (this instanceof EnemyShot && unit instanceof  Enemy){
           return false;
       }
        if (this instanceof Enemy && unit instanceof  EnemyShot){
            return false;
        }
       if (this instanceof Enemy && unit instanceof Enemy){
           return false;
       }
        if (this instanceof EnemyShot && unit instanceof Player){
            return this.getHitbox().intersects(unit.getHitbox());
        }
        if (this instanceof Player && unit instanceof EnemyShot){
            return this.getHitbox().intersects(unit.getHitbox());
        }

        if (this instanceof Shot && unit instanceof Player){
           return false;
       }
       if (this instanceof Player && unit instanceof Shot){
           return false;
       }
       return this.getHitbox().intersects(unit.getHitbox());
    }
}
