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

public class Controller extends Thread implements KeyListener {

    private final int FPS = 2;
    private final int enemySpeed = 25;

    private GameFrame frame;
    private Painter painter;
    private Player player = new Player(this);
    private int score = 0;

    private List<Unit> allUnits = new ArrayList<>(); //used in collision

    private List<Boss> bosses = new ArrayList<>(); //not rly used
    private List<Shot> shots = new ArrayList<>(); //not rly used
    private List<List<Enemy>> enemies = new ArrayList<>(); //List of Lists = grid; used for moving

    private Boolean timerActivated = false;

    private int direction = +1; //+1 = right; -1 = left

    public Controller() {
        painter = new Painter(this);
        initializeLevel(new Level1(Difficulty.EASY,this));
    }

    public Player getPlayer() {
        return player;
    }

    private void initializeLevel(Level level){
        enemies = level.getEnemyGrid();
        bosses = level.getBosses();

        bosses.forEach(Boss::start);

        enemies.forEach(allUnits::addAll);
        allUnits.add(player);
        allUnits.addAll(bosses);
    }

    public void installFrame(GameFrame frame){
        this.frame = frame;
        this.start();
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
        Shot shot;
        if (shooter instanceof Player && !timerActivated) {
            shot = new Shot(new Position(shooter.getPosition().getX()+20,shooter.getPosition().getY()-10),2,true,this);
            timerActivated = true;
            setTimeout( ()-> timerActivated = false,1000);
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
        while (!enemies.stream().allMatch(List::isEmpty)){ //while any of the enemy rows has an enemy in it
            boolean moveDown = false;
            if (anyEnemyTouchesBorder()) {
                direction *= -1;
                moveDown = true;
            }

            for (List<Enemy> row : new ArrayList<>(enemies)) {
                for (Enemy e : row) {
                    enemyFire(e);
                    if (!moveDown)
                        e.moveRelative(direction * enemySpeed, 0);
                    else
                        e.moveRelative(0, enemySpeed);
                }
            }
            requestRepaint();
            try {
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("U WIN LEL"); //gg
    }

    public void enemyFire(Enemy e){
        if (e.willShoot()){
            e.shoot();
        }
    }
}
