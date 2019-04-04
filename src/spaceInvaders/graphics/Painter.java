package spaceInvaders.graphics;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.Player;
import spaceInvaders.units.Unit;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Painter extends JPanel {


    private Controller controller;
    private Player player;
    public  JLabel scoreLabel;

    public Painter(Controller controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(700,550));
        setBackground(Color.BLACK);
        setVisible(true);

        this.player = controller.getPlayer();

        scoreLabel = new JLabel("");
        scoreLabel.setFont(new Font(("bla bla"),Font.PLAIN,12));
        scoreLabel.setForeground(Color.green);

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
    }
}

