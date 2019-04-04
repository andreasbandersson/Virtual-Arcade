package spaceInvaders.units;

import spaceInvaders.logic.Controller;

public class EnemyShot extends Shot {
    public EnemyShot(Position position, int speed, boolean direction, Controller controller) {
        super(position, speed, direction, controller);
    }
}
