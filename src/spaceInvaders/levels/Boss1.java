package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.*;

import java.util.ArrayList;
import java.util.List;

/**
 * One of many Level-Classes. All these classes are identical except for which types of enemies are added to the enemy list.
 * @author Viktor Altintas
 */

public class Boss1 extends Level {

    private static final List<List<Enemy>> enemyBlock = new ArrayList<>();
    static {

            enemyBlock.add(new ArrayList<>());
            enemyBlock.get(0).add(new Level1Boss(new Position((200),100),null,Difficulty.EASY));

    }

    /**
     * @param difficulty the boss difficulty
     * @param controller the controller for the logic
     */
    public Boss1(Difficulty difficulty, Controller controller) {
        super(difficulty, enemyBlock, controller);
    }

    /**
     * returns new arrayList
     */
    @Override
    public List<Boss> getBosses() {
        return new ArrayList<>();
    }

}
