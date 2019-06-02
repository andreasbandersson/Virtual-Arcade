package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.*;

import java.util.ArrayList;
import java.util.List;

/**
 * One of many Level-Classes. All these classes are identical except for which types of enemies are added to the enemy list.
 * @author Viktor Altintas
 */

public class Boss2  extends Level {

    private static final List<List<Enemy>> enemyBlock = new ArrayList<>();
    static {

        enemyBlock.add(new ArrayList<>());
        enemyBlock.get(0).add(new Level2Boss(new Position((200),100),null,Difficulty.HARD));

    }
    /**
     * @param difficulty the boss difficulty
     * @param controller the controller for the logic
     */
    public Boss2(Difficulty difficulty, Controller controller) {
        super(difficulty, enemyBlock, controller);
    }

    /**
     *
     * @return a new arraylist
     */
    @Override
    public List<Boss> getBosses() {
        return new ArrayList<>();
    }

}
