package spaceInvaders.units;

/**
 * @author Viktor Altintas
 */

public class Position {

    private int x;
    private int y;

    public Position (int x, int y){
        this.x = x;
        this.y = y;
    }
    public Position(Position position){
        this.x = position.getX();
        this.y = position.getY();
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }
}
