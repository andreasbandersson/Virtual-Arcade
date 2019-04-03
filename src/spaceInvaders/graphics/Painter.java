package spaceInvaders.graphics;

import spaceInvaders.logic.Controller;
import spaceInvaders.units.Player;
import spaceInvaders.units.Unit;

import javax.swing.*;
import java.awt.*;


public class Painter extends JPanel {


    private Controller controller;
    private Player player;

    public Painter(Controller controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(700,550));
        setBackground(Color.BLACK);
        setVisible(true);
        this.player = controller.getPlayer();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(player.getPlayerSprite(),player.getPosition().getX(), player.getPosition().getY(), this);
        for (Unit unit : controller.getAllUnits()) {
            g.drawImage(unit.getSprite(), unit.getPosition().getX(), unit.getPosition().getY(), this);
        }
    }
}

