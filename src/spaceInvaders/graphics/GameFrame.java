package spaceInvaders.graphics;
import spaceInvaders.logic.Controller;

import javax.swing.*;

public class GameFrame extends JPanel {

    private JFrame frame;

    public GameFrame(Controller controller) {
        frame = new JFrame();
        frame.setVisible(true);
        frame.add(this);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(controller.getPainter());
        frame.addKeyListener(controller);
        frame.pack();
        controller.installFrame(this);
    }

    @Override
    public int getWidth(){
        return frame.getWidth();
    }
}
