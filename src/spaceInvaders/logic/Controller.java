package spaceInvaders.logic;

import application.JukeBox;
import application.JukeBox2;
import javafx.scene.canvas.Canvas;
import spaceInvaders.graphics.Painter;
import spaceInvaders.levels.*;
import spaceInvaders.units.*;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * a controller that holds all the logics for Space Invaders
 * @author Viktor Altintas
 */

public class Controller implements Runnable {

    private Canvas canvas;
    private int score = 0;
    private int levelCounter = 1;
    private  int levelCounter2 = 0;
    private boolean gamePaused = false;
    private boolean levelLoading = false;

    private List<Unit> allUnits = new ArrayList<>(); //used in collision

    private List<Boss> bosses = new ArrayList<>();
    private List<Shot> shots = new ArrayList<>();
    private List<List<Enemy>> enemies = new ArrayList<>(); //List of Lists = grid; used for moving

    private Boolean timerActivated = false;
    private Painter painter;

    private Player player;
    private Executor executor;
    private JukeBox2 jukeBox2 = new JukeBox2();
    private ArrayList<Level> levelList = new ArrayList<>();


    private int direction = +1; //+1 = right; -1 = left

    /**
     * constructor that creates the player, the threadpool, and starts the first level
     * @param canvas the canvas, used for size measurements so enemies dont go offscreen.
     * @param painter the painter, so the controller can give updates to the painter.
     */
    public Controller( Canvas canvas, Painter painter) {
        this.canvas = canvas;
        this.painter = painter;
        player = new Player(this, painter);
        executor = Executors.newFixedThreadPool(15);
        allUnits.add(player);
        createLevelList();
        initializeLevel(levelList.get(levelCounter2));
    }

    /**
     * restarts the game state to a fresh state.
     */
    public void restart(){
        allUnits.add(player);
        levelCounter = 1;
        levelCounter2 = 0;
        initializeLevel(levelList.get(levelCounter2));
    }

    /**
     * creates the levelList
     */
    private void createLevelList(){
        levelList = new LevelListBuilder(this).getLevelList();
    }

    /**
     * @return if game is paused
     */
    public boolean getGamePaused() {
        return gamePaused;
    }

    /**
     * @return the player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * initialises the level by extracting the data from the current level and adding the objects to their respective lists
     * and then starting the runnable
     * @param level the current level
     */
    private void initializeLevel(Level level){
        jukeBox2.playMP3(JukeBox2.NEWLEVEL);

        bosses = level.getBosses();
        enemies = level.getEnemyGrid();
        enemies.forEach(allUnits::addAll); //calling addAll of allUnits with every row in enemies
        bosses.forEach(Boss::start);
        allUnits.addAll(bosses);
        levelLoading = true;
        start();
    }

    /**
     * starts a new thread with the runnable
     */
    private void start(){
        new Thread(this).start();
    }

    /**
     * getter
     * @return a copy of the all units list
     */
    public synchronized List<Unit> getAllUnits() {
        return new ArrayList<>(allUnits);
    }

    /**
     *
     * @return the players score
     */
    public int getScore() {
        return score;
    }

    /**
     *
     * @param score the new score
     */
    public void setScore(int score){
        this.score = score;
    }

    /**
     * if a shot is successfully registered, this method is called, which creates a different type of shot depending on
     * who registered the shot request.
     * @param shooter the unit that got its shot request successfully approved.
     */
    public void registerShot(Unit shooter){
        Shot shot;
        if (shooter instanceof Player && !timerActivated) {
            shot = new Shot(new Position(shooter.getPosition().getX()+20,shooter.getPosition().getY()-10),0,true,this);
            timerActivated = true;
            setTimeout( ()-> timerActivated = false,150); //wait 1 second, then say that timerActivated is false
        }else if(shooter instanceof Enemy) {
            shot = new EnemyShot(new Position(shooter.getPosition().getX() + (shooter.getWidth()/2),shooter.getPosition().getY()+shooter.getHeight()), ((Enemy) shooter).getDifficulty().ordinal()*2,false,this);
        }
        else {
            return;
        }
        executor.execute(shot);
        shots.add(shot);
        allUnits.add(shot);
    }

    /**
     * @return the boolean that pauses the game while true, set true while a level is being created
     */
    public boolean getLevelLoading(){
        return levelLoading;
    }

    /**
     * this method checks if any units rectangle hitbox intersects with any others, and if so, it reports a registered hit.
     */
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

    /**
     * removes a unit from all its lists.
     * @param unit the unit that will be removed
     */
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
                    jukeBox2.playMP3(JukeBox2.EXPLOSION);
                    score += ((Enemy) unit).getPoints();
                    // painter.addNewScoreFloat(((Enemy) unit).getPoints());
                    return;
                }
            }
        }
        if(unit instanceof Boss){
            bosses.remove(unit);
        }
    }

    /**
     * sends a position to the painter where a shot collision animation is to happen
     * @param position the position
     */
    public void remotePainterAccess(Position position) {
        painter.setShotCollisionData(position);
    }

    /**
     * the timeout for players reload
     * @param runnable
     * @param delay for how long the timeout should be
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

    /**
     * checks if any enemy has reached the end of the canvas
     * @return true or false if any enemy has reached the edge
     */
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

    /**
     * this run handles the main game logic, the movement of the characters, and generates shots from the enemies randomly.
     */
    @Override
    public void run(){ //moves blocks of enemies
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        levelLoading = false;
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
                final int FPS = 2;
                Thread.sleep(1500 / FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (painter.getGameEnded()){
            return;
        }
        levelWin();
    }

    /**
     * checks if any enemy touches the border, and if so changes the direction of all enemies, otherwise it just moves them
     * all normally.
     */
    private void movementRun() {
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

    /**
     * method that calls the units shoot method if their random shot chance comes out positive.
     * @param e the unit that might shoot.
     */
    private void enemyFire(Enemy e){
        if (e.willShoot()){
            e.shoot();
        }
    }

    /**
     * sets the paused state to its current opposite
     */
    public void setGamePaused(){
        gamePaused = !gamePaused;
        for (Unit unit : allUnits){
            unit.setPaused();
        }
    }

    /**
     * getter
     * @return the level counter
     */
    public int getLevelCounter() {
        return levelCounter;
    }

    /**
     * this method increments the levelcounters, and initalises the next level in the list.
     */
    private synchronized void levelWin() {
        levelCounter++;
        levelCounter2 = (levelCounter2+1) % levelList.size();
        initializeLevel(levelList.get(levelCounter2));
    }

    /**
     * clears the game field, meaning removes all units from the unit lists.
     */
    public void clearGameField(){
        allUnits.clear();
        bosses.clear();
        shots.clear();
        enemies.clear();
        jukeBox2.playMP3(JukeBox2.GAMEOVER);
    }
}
