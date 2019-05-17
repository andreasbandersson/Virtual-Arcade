package spaceInvaders.levels;

import spaceInvaders.logic.Controller;

import java.util.ArrayList;

public class LevelListBuilder {


    private ArrayList<Level> levelList = new ArrayList<>();

    public LevelListBuilder(Controller controller) {
        levelList.add(new Level2(Difficulty.EASY,controller));
        levelList.add(new Level2(Difficulty.EASY,controller));
    }

    public ArrayList getLevelList(){
        return levelList;
    }
}
