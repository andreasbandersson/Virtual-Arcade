package spaceInvaders.logic;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.levels.Level;
import spaceInvaders.levels.Level1;
import spaceInvaders.units.*;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Viktor Altintas
 */

public class Controller implements Runnable {

    private final int FPS = 2;
    private final int enemySpeed = 20;

    private Canvas canvas;
    private Player player = new Player(this);
    private int score = 0;
    private int levelCounter = 1;
    private boolean gamePaused = false;

    private List<Unit> allUnits = new ArrayList<>(); //used in collision

    private List<Boss> bosses = new ArrayList<>();
    private List<Shot> shots = new ArrayList<>();
    private List<List<Enemy>> enemies = new ArrayList<>(); //List of Lists = grid; used for moving

    private Boolean timerActivated = false;

    private Image explosion;

    {
        try {
            explosion = new Image(new FileInputStream("Sprites/deathExplosion.png"),25,20,false,false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int direction = +1; //+1 = right; -1 = left

    public Controller( Canvas canvas) {
        this.canvas = canvas;
        allUnits.add(player);
        initializeLevel(new Level1(Difficulty.EASY,this));
    }

    public boolean getGamePaused() {
        return gamePaused;
    }

    public Player getPlayer() {
        return player;
    }

    private void initializeLevel(Level level){
        enemies = level.getEnemyGrid();
        bosses = level.getBosses();
        bosses.forEach(Boss::start);
        enemies.forEach(allUnits::addAll); //calling addAll of allUnits with every row in enemies
        allUnits.addAll(bosses);
        for (Unit unit : allUnits){
            System.out.println(unit.getClass().getSimpleName()); //testing to see what units were added
        }
        start();
    }

    private void start(){
        new Thread(this).start();
    }

    public synchronized List<Unit> getAllUnits() {
        return new ArrayList<>(allUnits);
    }

    public int getScore() {
        return score;
    }

    public void registerShot(Unit shooter){
        Shot shot;
        if (shooter instanceof Player && !timerActivated) {
            shot = new Shot(new Position(shooter.getPosition().getX()+20,shooter.getPosition().getY()-10),4,true,this);
            System.out.println(ManagementFactory.getThreadMXBean().getThreadCount() + " threads active");
            timerActivated = true;
            setTimeout( ()-> timerActivated = false,100); //wait 1 second, then say that timerActivated is false
        }else if(shooter instanceof Enemy) {
            shot = new EnemyShot(new Position(shooter.getPosition()), ((Enemy) shooter).getDifficulty().ordinal(),false,this);
        }else {
            return;
        }
        shot.start();
        shots.add(shot);
        allUnits.add(shot);
    }

    public synchronized void requestHitboxCheck(){

        ArrayList<Unit> temp = new ArrayList<>(allUnits);
        outerLoop: for (Unit unit : temp){
            for (Unit otherUnit : temp){
                if (unit.collidesWith(otherUnit)){
                    unit.registerHit();
                    otherUnit.registerHit();
                    break outerLoop;
                }
                if (unit.getPosition().getY() > canvas.getHeight() || unit.getPosition().getY() < 0){
                    removeUnit(unit);
                }
            }
        }
    }

    public void removeUnit(Unit unit){
        allUnits.remove(unit);
        if(unit instanceof Shot){
            shots.remove(unit);
            ((Shot) unit).remoteKill();
        }
        if(unit instanceof Enemy){
            for(List<Enemy> row : enemies){
                if(row.contains(unit)){
                  //  unit.setSprite(explosion);
                    row.remove(unit);
                    score += ((Enemy) unit).getPoints();
                    return;
                }
            }
        }
        if(unit instanceof Boss){
            bosses.remove(unit);
        }
    }

    /**
     * the timeout for players reload
     * @param runnable
     * @param delay
     */
    private static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    private boolean anyEnemyTouchesBorder(){
        for(List<Enemy> row : enemies){
            for(Enemy e : row){
                if(e.getPosition().getX() + e.getWidth() + direction*enemySpeed > canvas.getWidth() ||
                        e.getPosition().getX() + direction*enemySpeed < 0){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void run(){ //moves blocks of enemies
        System.out.println("runnable started");
        while (!enemies.stream().allMatch(List::isEmpty)){ //if all objects delivered by the stream match the method isEmpty, statement is true
            // stream = take out the elements one by one
            while (gamePaused){
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            boolean moveDown = false;
            if (anyEnemyTouchesBorder()) {
                direction *= -1;
                moveDown = true;
            }
            for (List<Enemy> row : new ArrayList<>(enemies)) {
                for (Enemy e : new ArrayList<>(row)) {
                    enemyFire(e);
                    e.updateAnimation();
                    requestHitboxCheck();
                    if (!moveDown)
                        e.moveRelative(direction * enemySpeed, 0);
                    else {
                        e.moveRelative((direction * enemySpeed)-5, 0);
                    }
                }
            }
            try {
                Thread.sleep(1500 / FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        levelWin();
    }

    public void enemyFire(Enemy e){
        if (e.willShoot()){
            e.shoot();
        }
    }

    public void setGamePaused(){
        gamePaused = !gamePaused;
    }

    public int getLevelCounter() {
        return levelCounter;
    }

    public synchronized void levelWin() {
        levelCounter++;
        System.out.println("U WIN LEL"); //gg
        initializeLevel(new Level1(Difficulty.MEDIUM,this)); //should be a list of levels that initialize retrieves from. CAUSES CONCURRENTMODIFICATION ATM
    }
}
