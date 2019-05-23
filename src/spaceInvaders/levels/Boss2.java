package spaceInvaders.levels;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.*;

import java.util.ArrayList;
import java.util.List;

public class Boss2  extends Level {

    private static final List<List<Enemy>> enemyBlock = new ArrayList<>();
    static {

        enemyBlock.add(new ArrayList<>());
        enemyBlock.get(0).add(new Level2Boss(new Position((200),100),null,Difficulty.HARD));

    }

    public Boss2(Difficulty difficulty, Controller controller) {
        super(difficulty, enemyBlock, controller);
    }

    @Override
    public List<Boss> getBosses() {
        return new ArrayList<>();
    }

}
