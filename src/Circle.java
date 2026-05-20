import java.awt.*;
import java.awt.geom.Ellipse2D;

public abstract class Circle {
    protected double x = 0;
    protected double y = 0;
    protected double vx = 0;
    protected double vy = 0;
    protected double radius;
    protected Color color;

    private final Ellipse2D.Double ellipse = new Ellipse2D.Double();

    public abstract void update();

    public boolean checkOffscreen() {
        return (x < 0 - radius || x > Game.width + radius || y < 0 - radius || y > Game.height + radius);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        ellipse.setFrame(x - radius, y - radius, radius * 2, radius * 2);
        g2d.fill(ellipse);
    }
}