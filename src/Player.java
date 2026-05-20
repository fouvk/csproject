import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
public class Player extends Circle{
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private double speed = 0.4;

    public Player(JComponent canvas){
        x = Game.width/2;
        y = Game.height/2;
        color = Color.RED;
        radius = 20;

        setupKeyBindings(canvas);
    }

    @Override
    public void update() {
        if (upPressed) vy -= speed;
        if (downPressed) vy += speed;
        if (leftPressed) vx -= speed;
        if (rightPressed) vx += speed;

        x+=vx;
        y+=vy;
        
        vx*=0.9;
        vy*=0.9;
    }

    public void setupKeyBindings(JComponent canvas) {
        InputMap inputmap = canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionmap = canvas.getActionMap();

        inputmap.put(KeyStroke.getKeyStroke("UP"), "pressUp");
        inputmap.put(KeyStroke.getKeyStroke("released UP"), "releaseUp");
        
        actionmap.put("pressUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { upPressed = true; }
        });
        actionmap.put("releaseUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { upPressed = false; }
        });

        inputmap.put(KeyStroke.getKeyStroke("DOWN"), "pressDown");
        inputmap.put(KeyStroke.getKeyStroke("released DOWN"), "releaseDown");
        
        actionmap.put("pressDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { downPressed = true; }
        });
        actionmap.put("releaseDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { downPressed = false; }
        });

        inputmap.put(KeyStroke.getKeyStroke("LEFT"), "pressLeft");
        inputmap.put(KeyStroke.getKeyStroke("released LEFT"), "releaseLeft");
        
        actionmap.put("pressLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { leftPressed = true; }
        });
        actionmap.put("releaseLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { leftPressed = false; }
        });

        inputmap.put(KeyStroke.getKeyStroke("RIGHT"), "pressRight");
        inputmap.put(KeyStroke.getKeyStroke("released RIGHT"), "releaseRight");
        
        actionmap.put("pressRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { rightPressed = true; }
        });
        actionmap.put("releaseRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { rightPressed = false; }
        });
    }
}
