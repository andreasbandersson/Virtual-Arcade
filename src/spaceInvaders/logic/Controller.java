package spaceInvaders.logic;

import application.JukeBox;
import javafx.scene.canvas.Canvas;
import spaceInvaders.graphics.Painter;
import spaceInvaders.levels.*;
import spaceInvaders.units.*;

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
    private int levelCounter = 0;
    private boolean gamePaused = false;

    private List<Unit> allUnits = new ArrayList<>(); //used in collision

    private List<Boss> bosses = new ArrayList<>();
    private List<Shot> shots = new ArrayList<>();
    private List<List<Enemy>> enemies = new ArrayList<>(); //List of Lists = grid; used for moving

    private Boolean timerActivated = false;
    private Painter painter;

    private JukeBox jukeBox;
    private ArrayList<Level> levelList = new ArrayList<>();


    private int direction = +1; //+1 = right; -1 = left

    public Controller( Canvas canvas, Painter painter) {
        this.canvas = canvas;
        this.painter = painter;
        allUnits.add(player);
        createLevelList();
        initializeLevel(levelList.get(levelCounter));
    }

    public void createLevelList(){
       levelList = new LevelListBuilder(this).getLevelList();
    }

    public boolean getGamePaused() {
        return gamePaused;
    }

    public Player getPlayer() {
        return player;
    }

    private void initializeLevel(Level level){
        jukeBox = new JukeBox("sounds/newLevel.mp3");
        jukeBox.playWithCustomVol(0.3);

            bosses = level.getBosses();
            bosses.forEach(Boss::start);
            allUnits.addAll(bosses);


            enemies = level.getEnemyGrid();
            enemies.forEach(allUnits::addAll); //calling addAll of allUnits with every row in enemies

        for (Unit unit : allUnits){
            if (unit instanceof BossOne){
                new Thread(((BossOne) unit)::run).start();
            }
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
            shot = new Shot(new Position(shooter.getPosition().getX()+20,shooter.getPosition().getY()-10),0,true,this);
            System.out.println(ManagementFactory.getThreadMXBean().getThreadCount() + " threads active");
            timerActivated = true;
            setTimeout( ()-> timerActivated = false,150); //wait 1 second, then say that timerActivated is false
        }else if(shooter instanceof Enemy) {
            shot = new EnemyShot(new Position(shooter.getPosition()), ((Enemy) shooter).getDifficulty().ordinal()*2,false,this);
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
                    if (otherUnit instanceof Shot){
                        unit.sendShotPositionData(otherUnit.getPosition());
                    }
                    else if (unit instanceof Shot){
                        otherUnit.sendShotPositionData(unit.getPosition());
                    }
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
                    row.remove(unit);
                    painter.setExplosionData(unit.getPosition());
                    jukeBox = new JukeBox("sounds/Explosion.mp3");
                    jukeBox.playWithCustomVol(0.4);
                    score += ((Enemy) unit).getPoints();
                    return;
                }
            }
        }
        if(unit instanceof Boss){
            bosses.remove(unit);
        }
    }

   public void remotePainterAccess(Position position){
        painter.setShotCollisionData(position);
   }

    /**
     * the timeout for players reload
     * @param runnable
     * @param delay
     */
    public  void setTimeout(Runnable runnable, int delay){
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
        for(List<Enemy> row : new ArrayList<>(enemies)){
            for(Enemy e : row){
                if(e.getPosition().getX() + e.getWidth() + direction > canvas.getWidth() ||
                        e.getPosition().getX() + direction  < 0){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void run(){ //moves blocks of enemies
        new Thread(this::movementRun).start();
        while (!enemies.stream().allMatch(List::isEmpty)){ //if all objects delivered by the stream match the method isEmpty, statement is true
            // stream = take out the elements one by one
            while (gamePaused){
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (List<Enemy> row : new ArrayList<>(enemies)) {
                for (Enemy e : new ArrayList<>(row)) {
                    enemyFire(e);
                    e.updateAnimation();
                    requestHitboxCheck();
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

    public void movementRun() {
        while (!enemies.stream().allMatch(List::isEmpty)) { //if all objects delivered by the stream match the method isEmpty, statement is true
            // stream = take out the elements one by one
            while (gamePaused) {
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
                    if (!moveDown)
                        e.moveRelative(direction, 0);
                    else {
                        e.moveRelative(0, 5);
                    }
                }
            }
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void enemyFire(Enemy e){
        if (e.willShoot()){
            e.shoot();
        }
    }

    public void setGamePaused(){
        gamePaused = !gamePaused;
        for (Unit unit : allUnits){
            unit.setPaused();
        }
    }

    public int getLevelCounter() {
        return levelCounter;
    }

    public synchronized void levelWin() {
        levelCounter = (levelCounter+1) % levelList.size();
        System.out.println("U WIN LEL"); //gg
        initializeLevel(levelList.get(levelCounter));
    }
}
