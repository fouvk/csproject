import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

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
        if ((leftPressed || rightPressed) && (upPressed || downPressed)) { //if moving diagonally
            if (upPressed) vy -= speed * 0.70710678118;
            if (downPressed) vy += speed * 0.70710678118;
            if (leftPressed) vx -= speed * 0.70710678118;
            if (rightPressed) vx += speed * 0.70710678118;
        } 
        else {
            if (upPressed) vy -= speed;
            if (downPressed) vy += speed;
            if (leftPressed) vx -= speed;
            if (rightPressed) vx += speed;
        }

        x+=vx;
        y+=vy;
                
        vx*=0.9;
        vy*=0.9;
    }

    public void setupKeyBindings(JComponent canvas) { //uses inputmap and actionmap instead of keylistener to track both pressing and releasing actions, more smooth and functional
        InputMap inputmap = canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionmap = canvas.getActionMap();

        //up
        inputmap.put(KeyStroke.getKeyStroke("UP"), "pressUp");
        inputmap.put(KeyStroke.getKeyStroke("released UP"), "releaseUp");
        inputmap.put(KeyStroke.getKeyStroke("W"), "pressUp");
        inputmap.put(KeyStroke.getKeyStroke("released W"), "releaseUp");
        
        actionmap.put("pressUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { upPressed = true; }
        });
        actionmap.put("releaseUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { upPressed = false; }
        });

        //down
        inputmap.put(KeyStroke.getKeyStroke("DOWN"), "pressDown");
        inputmap.put(KeyStroke.getKeyStroke("released DOWN"), "releaseDown");
        inputmap.put(KeyStroke.getKeyStroke("S"), "pressDown");
        inputmap.put(KeyStroke.getKeyStroke("released S"), "releaseDown");
        
        actionmap.put("pressDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { downPressed = true; }
        });
        actionmap.put("releaseDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { downPressed = false; }
        });

        //left
        inputmap.put(KeyStroke.getKeyStroke("LEFT"), "pressLeft");
        inputmap.put(KeyStroke.getKeyStroke("released LEFT"), "releaseLeft");
        inputmap.put(KeyStroke.getKeyStroke("A"), "pressLeft");
        inputmap.put(KeyStroke.getKeyStroke("released A"), "releaseLeft");
        
        actionmap.put("pressLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { leftPressed = true; }
        });
        actionmap.put("releaseLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { leftPressed = false; }
        });

        //right
        inputmap.put(KeyStroke.getKeyStroke("RIGHT"), "pressRight");
        inputmap.put(KeyStroke.getKeyStroke("released RIGHT"), "releaseRight");
        inputmap.put(KeyStroke.getKeyStroke("D"), "pressRight");
        inputmap.put(KeyStroke.getKeyStroke("released D"), "releaseRight");
        
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
