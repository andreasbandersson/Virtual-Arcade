package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.*;

import java.util.ArrayList;
import java.util.List;

/**
 * One of many Level-Classes. All these classes are identical except for which types of enemies are added to the enemy list.
 * @author Viktor Altintas
 */

public class Level1 extends Level {

    private static final List<List<Enemy>> enemyBlock = new ArrayList<>();
    static {
        for (int i  = 0 ; i < 4; i ++) {
            enemyBlock.add(new ArrayList<>());
            for (int j = 0; j < 8; j++ ) {
                if (i < 1)
                    enemyBlock.get(i).add(new Level2Enemy(new Position((200+ j*50),35+(i*30)),null,Difficulty.EASY));
                else if (i < 2){
                    enemyBlock.get(i).add(new Level3Enemy(new Position((200+ j*50),35+(i*30)),null,Difficulty.EASY));
                }
                else if (i < 3){
                    enemyBlock.get(i).add(new Level4Enemy(new Position((200+ j*50),35+(i*30)),null,Difficulty.EASY));
                }
                else
                    enemyBlock.get(i).add(new Level1Enemy(new Position((200+ j*50),35+(i*30)),null,Difficulty.EASY));

            }
        }
    }

    /**
     * constructor
     * @param difficulty the difficulty
     * @param controller the controller for the logic
     */
    public Level1(Difficulty difficulty, Controller controller) {
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
