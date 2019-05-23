package spaceInvaders.levels;

import spaceInvaders.logic.Controller;

import java.util.ArrayList;

public class LevelListBuilder {


    private ArrayList<Level> levelList = new ArrayList<>();

    public LevelListBuilder(Controller controller) {
        levelList.add(new Level5(Difficulty.EASY,controller));
        levelList.add(new Level3(Difficulty.EASY,controller));
        levelList.add(new Level2(Difficulty.MEDIUM,controller));
        levelList.add(new Level2(Difficulty.MEDIUM,controller));
        levelList.add(new Level2(Difficulty.HARD,controller));
        levelList.add(new Level2(Difficulty.HARD,controller));
    }

    public ArrayList getLevelList(){
        return levelList;
    }
}
