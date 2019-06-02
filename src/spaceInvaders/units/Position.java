package spaceInvaders.units;

/**
 * a class made for holding x and y data.
 * @author Viktor Altintas
 */

public class Position {

    private int x;
    private int y;

    /**
     * constructor
     * @param x x
     * @param y y
     */
    public Position (int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * constructor that takes a given position object
     * @param position the position
     */
    public Position(Position position){
        this.x = position.getX();
        this.y = position.getY();
    }

    /**
     * getter
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * getter
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * setter
     * @param y y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * setter
     * @param x x
     */
    public void setX(int x) {
        this.x = x;
    }
}
