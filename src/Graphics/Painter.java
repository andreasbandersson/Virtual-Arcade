package Graphics;

import Logic.Controller;
import Units.Enemy;
import Units.Player;
import Units.Shot;
import Units.Unit;

import javax.swing.*;
import java.awt.*;


public class Painter extends JPanel {


  private  Controller controller;
  private Player player;
  private Shot playerShot;
  private boolean shotFired;


    public Painter(Controller controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(700,550));
        setBackground(Color.BLACK);
        setVisible(true);
        this.player = controller.getPlayer();

    }

    public void setShotFired(boolean shotFired, Shot shot){
        this.shotFired = shotFired;
        playerShot = shot;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(player.getPlayerSprite(),player.getPosition().getX(), player.getPosition().getY(), this);
        for (Unit unit: controller.getUnits()) {
            g.drawImage(unit.getSprite(), unit.getPosition().getX(), unit.getPosition().getY(), this);
         //   Rectangle rect = unit.getHitbox(); these lines draw hitboxes for testing purposes
         //   Graphics2D g2 = (Graphics2D) g;
         //   g2.setPaint(Color.RED);
         //   g.drawRect(rect.x,rect.y,rect.width,rect.height);
        }
    }
}
