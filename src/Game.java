import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    private int score;
    private JLabel scoreText;

    private final int TICK = 10;

    protected ArrayList<Enemy> enemies;
    private Player player;
    private GameCanvas canvas;

    private String currentUsername;
    private boolean scoreSaved = false;

    private Timer gameLoop;
    private Timer spawnLoop;

    public Game(String username) {   
        state = true;
        scoreSaved = false;
        currentUsername = username; 
        spawnDelay = INITIAL_DELAY;
        setTitle("Void Invasion - " + currentUsername);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        canvas = new GameCanvas();
        player = new Player(canvas);
        enemies = new ArrayList<Enemy>();
        scoreText = new JLabel();

        score = 0;
        scoreText.setForeground(Color.GRAY);
        scoreText.setFont(new Font("Monospaced", Font.BOLD, width/15));

        add(canvas);
        canvas.add(scoreText);
        setVisible(true);
        
        gameLoop = new Timer(TICK, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (state) {
                    onUpdate();
                    canvas.repaint();
                } else if (!scoreSaved) {
                    gameOver();
                }
            }
        });

        spawnLoop = new Timer(spawnDelay, null);
    
        spawnLoop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (state) {
                    Enemy e = new Enemy();
                    enemies.add(e);
                    totalEnemiesSpawned++;

                    spawnDelay = (int) (MIN_DELAY + (INITIAL_DELAY - MIN_DELAY) / (1 + DIFFICULTY_RAMP * totalEnemiesSpawned));

                    spawnLoop.setDelay(spawnDelay);

                    score += 1;
                    scoreText.setText(Integer.toString(score));
                }
            }
        });
        gameLoop.start();
        spawnLoop.start();
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> new LoginWindow());
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
    
    private void gameOver() {
        scoreSaved = true;

        if (gameLoop != null) gameLoop.stop();
        if (spawnLoop != null) spawnLoop.stop();

        File file = new File("highscores.txt");
        Map<String, Integer> highScores = new HashMap<>();


        if (file.exists()) { //read scores from file into map
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        highScores.put(parts[0], Integer.parseInt(parts[1]));
                    }
                }
            } catch (IOException e) {}
        }


        int existingBest = highScores.getOrDefault(currentUsername, 0);
        if (score > existingBest) {
            highScores.put(currentUsername, score); //only update if beating the same players previous best
        }


        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) { //write scores back into file
            for (Map.Entry<String, Integer> entry : highScores.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {}

        //sorts scores descending
        java.util.List<Map.Entry<String, Integer>> list = new ArrayList<>(highScores.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        StringBuilder leaderboard = new StringBuilder("LEADERBOARD\n\n");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : list) {
            leaderboard.append(rank).append(". ").append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
            if (++rank > 5) break; 
        }

        JOptionPane.showMessageDialog(this,
            "<html><body style='font-size:30px;'>" //need html for font sizing
            + "GAME OVER, " + currentUsername + "!<br>"
            + "Score: " + score + "<br><br>"
            + leaderboard.toString().replace("\n", "<br>") //replacing newline indicators to html
            + "</body></html>"
        );
        
        //close game window and return to login
        dispose();
        new LoginWindow();
    }

    private class GameCanvas extends JPanel { //inner private class to have access to player and enemies list
        public GameCanvas() {
            setBackground(Color.WHITE);
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) { //called by swing every repaint()
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //make circles look better

            if (player != null) {
                player.draw(g2d);
            }

            for (Enemy e : enemies) {
                e.draw(g2d);
            }

            Toolkit.getDefaultToolkit().sync(); //flushes display buffer reducing tearing 
        }
    }
}