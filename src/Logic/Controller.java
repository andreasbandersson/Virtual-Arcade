package Logic;

import Graphics.Painter;
import Levels.Difficulty;
import Levels.Level;
import Levels.Level1;
import Units.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Controller implements KeyListener {

    Painter painter;
    private Player player = new Player(this);
    private ArrayList<Unit> units  = new ArrayList<>();
    private Difficulty difficulty;

    public Controller(Difficulty difficulty) {
        this.difficulty = difficulty;
        units.add(player);
        painter = new Painter(this);
        initializeLevel(new Level1(difficulty,this));
    }

    public Player getPlayer() {
        return player;
    }

    public void initializeLevel(Level level){
        for (Unit unit: level.getEnemyList()){
            units.add(unit);
            unit.start();
        }
    }

    public Painter getPainter() {
       return painter;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getExtendedKeyCode()){

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                player.move(player.getPosition().getX()-15,player.getPosition().getY());
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                player.move(player.getPosition().getX()+15,player.getPosition().getY());
                break;

            case KeyEvent.VK_SPACE:
                player.shoot();
                break;
        }
        painter.repaint();
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }


    public void registerShot(Unit shooter){

        if (shooter instanceof Player){
            Shot shot = new Shot(new Position(shooter.getPosition().getX()+20,shooter.getPosition().getY()-10),2,true,this);
            shot.start();
            units.add(shot);
        }
        else {
            Shot shot = new Shot(new Position(shooter.getPosition()),(difficulty.ordinal()+1)*500,false,this);
            shot.start();
            units.add(shot);
        }
    }

    public void requestRepaint(){
        ArrayList<Unit> temp = new ArrayList<>(units);
        temp.forEach(unit ->{
            temp.forEach(otherUnit ->{
                if (unit.collidesWith(otherUnit)){
                    unit.registerHit();
                    otherUnit.registerHit();
                }
            });
        });
        painter.repaint();
    }

    public ArrayList<Unit> getUnits(){
        return units;
    }

    public void isDead(Unit unit){
        if (unit instanceof Player){
            player.setPosition(player.getStartPosition());
        }
        else
        units.remove(unit);
    }
}
