package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.*;

import java.util.ArrayList;
import java.util.List;

public class Boss1 extends Level {

    private static final List<List<Enemy>> enemyBlock = new ArrayList<>();
    static {

            enemyBlock.add(new ArrayList<>());
            enemyBlock.get(0).add(new Level1Boss(new Position((200),100),null,Difficulty.EASY));

    }

    public Boss1(Difficulty difficulty, Controller controller) {
        super(difficulty, enemyBlock, controller);
    }

    @Override
    public List<Boss> getBosses() {
        return new ArrayList<>();
    }

}
