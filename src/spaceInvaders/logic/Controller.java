package spaceInvaders.logic;

import spaceInvaders.graphics.GameFrame;
import spaceInvaders.graphics.Painter;
import spaceInvaders.levels.Difficulty;
import spaceInvaders.levels.Level;
import spaceInvaders.levels.Level1;
import spaceInvaders.units.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktor Altintas
 */

public class Controller implements Runnable, KeyListener {

    private final int FPS = 2;
    private final int enemySpeed = 25;

    private GameFrame frame;
    private Painter painter;
    private Player player = new Player(this);
    private int score = 0;
    private int levelCounter = 1;
    private boolean gamePaused = false;

    private List<Unit> allUnits = new ArrayList<>(); //used in collision

    private List<Boss> bosses = new ArrayList<>(); //not rly used
    private List<Shot> shots = new ArrayList<>(); //not rly used
    private List<List<Enemy>> enemies = new ArrayList<>(); //List of Lists = grid; used for moving

    private Boolean timerActivated = false;

    private int direction = +1; //+1 = right; -1 = left

    public Controller() {
        painter = new Painter(this);
        painter.showLevelTitle();
        allUnits.add(player);
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

    public void installFrame(GameFrame frame){
        this.frame = frame;
        initializeLevel(new Level1(Difficulty.EASY,this));
    }

    public void start(){
        new Thread(this).start();
    }

    public Painter getPainter() {
        return painter;
    }

    public synchronized List<Unit> getAllUnits() {
        return new ArrayList<>(allUnits);
    }

    public int getScore() {
        return score;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        if (!gamePaused) {
            switch (e.getExtendedKeyCode()) {

                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    player.move(player.getPosition().getX() - 15, player.getPosition().getY());
                    break;

                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    player.move(player.getPosition().getX() + 15, player.getPosition().getY());
                    break;

                case KeyEvent.VK_SPACE:
                    player.shoot();
                    break;
            }
        }
        if (e.getExtendedKeyCode() == KeyEvent.VK_P) {
            gamePaused = !gamePaused;
            if (gamePaused){
                for (Shot shot : shots){
                    shot.setPaused();
                }
                painter.showPauseTitle();
            }
            else {
                painter.removePauseTitle();
                for (Shot shot : shots){
                    shot.setPaused();
                }
            }
        }
        painter.repaint();
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }


    public void registerShot(Unit shooter){
        Shot shot;
        if (shooter instanceof Player && !timerActivated) {
            shot = new Shot(new Position(shooter.getPosition().getX()+20,shooter.getPosition().getY()-10),4,true,this);
            timerActivated = true;
            setTimeout( ()-> timerActivated = false,1000); //wait 1 second, then say that timerActivated is false
        }else if(shooter instanceof Enemy) {
            shot = new EnemyShot(new Position(shooter.getPosition()), ((Enemy) shooter).getDifficulty().ordinal(),false,this);
        }else{
            return;
        }

        shot.start();
        shots.add(shot);
        allUnits.add(shot);
    }

    public synchronized void requestRepaint(){
        ArrayList<Unit> temp = new ArrayList<>(allUnits);
        outerLoop: for (Unit unit : temp){
            for (Unit otherUnit : temp){
                if (unit.collidesWith(otherUnit)){
                    unit.registerHit();
                    otherUnit.registerHit();
                    break outerLoop;
                }
                if (unit.getPosition().getY() > frame.getHeight() || unit.getPosition().getY() < 0){
                    removeUnit(unit);
                }
            }
        }
        painter.repaint();
    }

    public void removeUnit(Unit unit){
        allUnits.remove(unit);
        if(unit instanceof Shot){
            shots.remove(unit);
        }

        if(unit instanceof Enemy){
            for(List<Enemy> row : enemies){
                if(row.contains(unit)){
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
                if(e.getPosition().getX() + e.getWidth() + direction*enemySpeed > frame.getWidth() ||
                        e.getPosition().getX() + direction*enemySpeed < 0){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void run(){ //moves blocks of enemies
        while (!enemies.stream().allMatch(List::isEmpty)){ //if all objects delivered by the stream match the method isEmpty, statement is true
            // stream = take out the elements one by one
            while (gamePaused){
                try {
                    Thread.sleep(250);
                    requestRepaint();
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
                for (Enemy e : row) {
                    enemyFire(e);
                    if (!moveDown)
                        e.moveRelative((direction * enemySpeed)-5, 0);
                    else
                        e.moveRelative(0, enemySpeed);
                }
            }
            requestRepaint();
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

    public int getLevelCounter() {
        return levelCounter;
    }

    public void levelWin() {
        levelCounter++;
        painter.showLevelTitle();
        initializeLevel(new Level1(Difficulty.MEDIUM,this)); //should be a list of levels that initialize retrieves from
        System.out.println("U WIN LEL"); //gg
    }
}
