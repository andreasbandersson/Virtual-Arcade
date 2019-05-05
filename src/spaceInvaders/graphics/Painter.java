package spaceInvaders.graphics;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.Player;
import spaceInvaders.units.Unit;

import javax.swing.*;
import java.awt.*;

/**
 * @author Viktor Altintas
 */

public class Painter extends JPanel {

    private Controller controller;
    private Player player;

    public  JLabel scoreLabel;
    public JLabel levelTitle;
    public JLabel pauseTitle;

    private static Image playerLifeSprite = new ImageIcon("Sprites/player.png").getImage().getScaledInstance(30,25,Image.SCALE_DEFAULT);

    public Painter(Controller controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(700,550));
        setBackground(Color.BLACK);
        setVisible(true);

        this.player = controller.getPlayer();

        scoreLabel = new JLabel("");
        scoreLabel.setFont(new Font(("byt till något fett"),Font.PLAIN,12));
        scoreLabel.setForeground(Color.green);

        levelTitle = new JLabel("Level " + controller.getLevelCounter());
        levelTitle.setFont(new Font(("byt till något fett"),Font.PLAIN,40));
        levelTitle.setForeground(Color.WHITE);

        pauseTitle = new JLabel("Game Paused \nto resume: Press P");
        pauseTitle.setFont(new Font(("byt till något fett"),Font.PLAIN,40));
        pauseTitle.setForeground(Color.WHITE);

        add(scoreLabel);
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(player.getPlayerSprite(),player.getPosition().getX(), player.getPosition().getY(), this);
        scoreLabel.setText("Score: " + controller.getScore());
        for (Unit unit : controller.getAllUnits()) {
            g.drawImage(unit.getSprite(), unit.getPosition().getX(), unit.getPosition().getY(), this);
        }
        for (int i = 0; i < player.getLife(); i++){
            g.drawImage(playerLifeSprite,510+((i+1)*39),10,this);
        }
    }

    public void showLevelTitle() {
        add(levelTitle);
        levelTitle.setText("Level " + controller.getLevelCounter());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        remove(levelTitle);
    }

    public void showPauseTitle() {
        add(levelTitle);
        repaint();
    }
    public void removePauseTitle() {
        remove(levelTitle);
        repaint();
    }

}

