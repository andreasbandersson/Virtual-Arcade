package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.Boss;
import spaceInvaders.units.Enemy;
import spaceInvaders.units.Level1Enemy;
import spaceInvaders.units.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktor Altintas
 */

public class Level1 extends Level {


    private static final List<List<Enemy>> enemyBlock = new ArrayList<>();
    static {
        for (int i  = 0 ; i < 1; i ++) {
            enemyBlock.add(new ArrayList<>());
            for (int j = 0; j < 2; j++ ) {
                enemyBlock.get(i).add(new Level1Enemy(new Position((200+ j*60),40+(i*45)),null,Difficulty.EASY));
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
