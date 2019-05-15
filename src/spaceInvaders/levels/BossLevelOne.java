package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.*;


import java.util.ArrayList;
import java.util.List;

public class BossLevelOne extends Level {

    private static final List<List<Enemy>> enemyBlock = new ArrayList<>();

    static {
        for (int i  = 0 ; i < 4; i ++) {
            enemyBlock.add(new ArrayList<>());
            for (int j = 0; j < 8; j++ ) {
                enemyBlock.get(i).add(new BossOne(5,new Position(250,200),null));
            }
        }
    }

    public BossLevelOne( Controller controller) {
        super( Difficulty.EASY, enemyBlock, controller);
        enemyBlock.add(new ArrayList<>());

    }

    @Override
    public List<Boss> getBosses() {
        return new ArrayList<>();
    }

}
