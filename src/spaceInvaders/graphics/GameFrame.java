package spaceInvaders.graphics;
import spaceInvaders.logic.Controller;

import javax.swing.*;

public class GameFrame extends JPanel {


    public GameFrame(Controller controller) {
        /**
         * frame instantiation
         */
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(controller.getPainter());
        frame.addKeyListener(controller);
        frame.pack();
    }
}
