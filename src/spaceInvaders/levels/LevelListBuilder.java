package spaceInvaders.levels;

import spaceInvaders.logic.Controller;

import java.util.ArrayList;

/**
 * holds a list of levels that the controller will progress through each time a level is complete
 * @author Viktor Altintas
 */
public class LevelListBuilder {


    private ArrayList<Level> levelList = new ArrayList<>();

    /**
     * adds all the levels to the list
     * @param controller the controller for the logic
     */
    public LevelListBuilder(Controller controller) {
        levelList.add(new Level1(Difficulty.EASY,controller));
        levelList.add(new Level2(Difficulty.EASY,controller));
        levelList.add(new Level3(Difficulty.EASY,controller));
        levelList.add(new Boss1(Difficulty.EASY,controller));
        levelList.add(new Level4(Difficulty.MEDIUM,controller));
        levelList.add(new Level5(Difficulty.MEDIUM,controller));
        levelList.add(new Boss2(Difficulty.HARD,controller));
    }

    /**
     * getter
     * @return the levellist
     */
    public ArrayList getLevelList(){
        return levelList;
    }
}
