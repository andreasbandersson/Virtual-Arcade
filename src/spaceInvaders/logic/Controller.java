package spaceInvaders.logic;

import spaceInvaders.graphics.Painter;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.levels.Level;
import spaceInvaders.levels.Level1;
import spaceInvaders.units.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class Controller extends Thread implements KeyListener {

    Painter painter;
    private Player player = new Player(this);
    private ArrayList<Unit> units  = new ArrayList<>();
    private Difficulty difficulty;
    private Boolean timerActivated = false;

    public Controller(Difficulty difficulty) {
        this.difficulty = difficulty;
        units.add(player);
        painter = new Painter(this);
        initializeLevel(new Level1(difficulty,this));
        this.start();
    }

    public Player getPlayer() {
        return player;
    }

    public void initializeLevel(Level level){
        for (Unit unit: level.getEnemyList()){
            units.add(unit);
            if (unit instanceof Boss)
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

        if (shooter instanceof Player && !timerActivated){
            Shot shot = new Shot(new Position(shooter.getPosition().getX()+20,shooter.getPosition().getY()-10),2,true,this);
            shot.start();
            units.add(shot);
            timerActivated = true;
            setTimeout( ()-> {
                timerActivated = false;
            }
            ,1000);
        }
        else if (shooter instanceof Enemy) {
            Shot shot = new Shot(new Position(shooter.getPosition()),(difficulty.ordinal()+1)*500,false,this);
            shot.start();
            units.add(shot);
        }
    }

    public synchronized void requestRepaint(){
        ArrayList<Unit> temp = new ArrayList<>(units);
        outerLoop: for (Unit unit : temp){
            for (Unit otherUnit : temp){
                if (unit.collidesWith(otherUnit)){
                    unit.registerHit();
                    otherUnit.registerHit();
                    break outerLoop;
                }
            }
        }
        painter.repaint();
    }

    public ArrayList<Unit> getUnits(){
        return units;
    }

    public void moveEnemyBlock() {
     
}


    public void run() {

        while (true){
            for (Unit unit : units){
                if (unit instanceof Enemy){

                }
            }
        }
    }



    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
}
