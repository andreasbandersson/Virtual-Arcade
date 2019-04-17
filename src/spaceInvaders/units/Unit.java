package spaceInvaders.units;

import spaceInvaders.logic.Controller;

import java.awt.*;

/**
 * @author Viktor Altintas
 */

public abstract class Unit {

   protected int life;
   protected Position position;
   protected Image sprite;
   private int width;
   private int height;
   protected Controller controller;

   public Unit(int life, Position position, Image image, int width, int height, Controller controller) {
       this.life = life;
       this.position = position;
       this.sprite = image;
       this.width = width;
       this.height = height;
       this.controller = controller;
   }

   public void setLife(int life){
       this.life = life;
   }

   public synchronized void setPosition(Position position){
       this.position = position;
   }

   public void setSprite(Image image){
       this.sprite = image;
   }

    public Image getSprite() {
        return sprite;
    }

    public void setController(Controller controller) {
       this.controller = controller;
    }

    public int getLife() {
        return life;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public synchronized Position getPosition() {
        return position;
    }

    public abstract void shoot();

   public synchronized void move(int x, int y){
       position.setX(x);
       position.setY(y);
   }

    public synchronized void moveRelative(int x, int y){
        position.setX(position.getX() + x);
        position.setY(position.getY() + y);
    }

    public void move(Position position){
        this.position.setX(position.getX());
        this.position.setY(position.getY());
    }

    public abstract void registerHit();

    public abstract void die();


   public Rectangle getHitbox() {
       return new Rectangle(position.getX(),position.getY(),width,height);
   }

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
