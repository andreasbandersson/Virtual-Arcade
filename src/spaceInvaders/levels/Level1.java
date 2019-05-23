package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.*;

import java.util.ArrayList;
import java.util.List;

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

    public Level1(Difficulty difficulty, Controller controller) {
        super(difficulty, enemyBlock, controller);
    }

    @Override
    public List<Boss> getBosses() {
        return new ArrayList<>();
    }
}
