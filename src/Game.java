import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Game extends JFrame {
    protected static int width = 1000;
    protected static int height = 1000;
    protected static boolean state = true;

    private int totalEnemiesSpawned = 0;
    private int spawnDelay;
    private final int INITIAL_DELAY = 500;
    private final int MIN_DELAY = 10;
    private final double DIFFICULTY_RAMP = 0.01;

    private final int TICK = 10;

    protected ArrayList<Enemy> enemies;
    private Player player;
    private GameCanvas canvas;

    public Game() {    
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        canvas = new GameCanvas();
        player = new Player(canvas);
        enemies = new ArrayList<Enemy>();
        
        add(canvas);
        setVisible(true);
        
        Timer gameLoop = new Timer(TICK, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (state) {
                    onUpdate();
                    canvas.repaint();
                }
            }
        });

        final Timer spawnLoop = new Timer(spawnDelay, null);
    
        spawnLoop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (state) {
                    // 1. Spawn the enemy
                    Enemy e = new Enemy();
                    enemies.add(e);
                    totalEnemiesSpawned++;

                    // 2. Use an asymptotic formula based on total enemies spawned instead of time.
                    // This makes it 100% stable and prevents historical timing catch-up bugs.
                    spawnDelay = (int) (MIN_DELAY + (INITIAL_DELAY - MIN_DELAY) / (1 + DIFFICULTY_RAMP * totalEnemiesSpawned));

                    // 3. Set the delay safely for the *next* spawn interval
                    spawnLoop.setDelay(spawnDelay);
                    
                    System.out.println("Enemies Spawned: " + totalEnemiesSpawned + " | Next Spawn In: " + spawnDelay + "ms");
                }
            }
        });
        gameLoop.start();
        spawnLoop.start();
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("sun.java2d.uiScale", "2.0");
        SwingUtilities.invokeLater(() -> new Game());
    }

    void onUpdate() {
        player.update();
        if(player.checkOffscreen()) {state = false;}
        
        for (int i = enemies.size() - 1; i >= 0; i--) { //run through arraylist backwards to avoid issues when removing enemies
            Enemy e = enemies.get(i);
            e.update();
            if(e.checkOffscreen()) {
                enemies.remove(i);
            } else {
                checkCollision(e);
            }
        }
    }

    void checkCollision(Enemy enemy) {
        double dx = player.x - enemy.x;
        double dy = player.y - enemy.y;

        double distance = Math.sqrt(dx*dx + dy*dy);
        double minDistance = player.radius + enemy.radius;

        if(distance < minDistance) {
            double nx = dx / distance; //normalize
            double ny = dy / distance;
            double overlapDistance = minDistance - distance;

            player.x = player.x + nx * overlapDistance;
            player.y = player.y + ny * overlapDistance;
        }
    }
    

    private class GameCanvas extends JPanel {
        public GameCanvas() {
            setBackground(Color.WHITE);
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //make circles look better

            if (player != null) {
                player.draw(g2d);
            }

            for (Enemy e : enemies) {
                e.draw(g2d);
            }

            Toolkit.getDefaultToolkit().sync();
        }
    }
}