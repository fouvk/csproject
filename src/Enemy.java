import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy extends Circle {

    public Enemy() {
        color = Color.BLACK;
        radius = ThreadLocalRandom.current().nextDouble(20, 150);

        y = 0 - radius;
        x = ThreadLocalRandom.current().nextDouble(0, Game.width);

        vx = ThreadLocalRandom.current().nextDouble(-2, 2);
        vy = ThreadLocalRandom.current().nextDouble(1, 6);
    }

    @Override
    public void update() {
       x += vx;
       y += vy;
    }
}