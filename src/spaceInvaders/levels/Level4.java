package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.*;

import java.util.ArrayList;
import java.util.List;

/**
 * One of many Level-Classes. All these classes are identical except for which types of enemies are added to the enemy list.
 * @author Viktor Altintas
 */

public class Level4  extends Level {

    private static final List<List<Enemy>> enemyBlock = new ArrayList<>();
    static {
        for (int i  = 0 ; i < 2; i ++) {
            enemyBlock.add(new ArrayList<>());
            for (int j = 0; j < 8; j++ ) {
                if (j < 4)
                    enemyBlock.get(i).add(new Level3Enemy(new Position((200+ j*50),35+(i*30)),null,Difficulty.HARD));
                else {
                    enemyBlock.get(i).add(new Level4Enemy(new Position((200+ j*50),35+(i*30)),null,Difficulty.HARD));

                }
            }
        }
    }

    /**
     * constructor
     * @param difficulty the difficulty
     * @param controller the controller for the logic
     */
    public Level4(Difficulty difficulty, Controller controller) {
        super(difficulty, enemyBlock, controller);
    }

    /**
     * @return new ArrayList
     */
    @Override
    public List<Boss> getBosses() {
        return new ArrayList<>();
    }


}
