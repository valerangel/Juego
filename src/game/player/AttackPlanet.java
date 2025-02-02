package game.player;

import java.awt.*;

/**
 * Created by Angel on 20/06/2018.
 */
public class AttackPlanet {

    private static final double ANGULAR_SPEED_ATTACK_MELEE = 0.092;
    private static final int DIAM = 20;
    private static final int DISTANCE = 120;
    private static final double DAMAGE = 4.1;

    private int a;
    private int b;
    private double angle;
    private Player enemy;

    private Planet planet;

    public AttackPlanet(Planet planet) {
        this.planet = planet;
        this.angle = 0;
    }

    public void move() {
        a = (int) (this.planet.getX() + (this.planet.getDiameter() - this.DIAM) / 2 + DISTANCE * Math.cos(angle));
        b = (int) (this.planet.getY() + (this.planet.getDiameter() - this.DIAM) / 2 + DISTANCE * Math.sin(angle));
        if (planet.getNumPlayer() == PlayerNumber.PLAYER1) {
            angle = angle + ANGULAR_SPEED_ATTACK_MELEE;
        } else {
            angle = angle - ANGULAR_SPEED_ATTACK_MELEE;
        }
        if (collision()) {
            enemy.dealDamage(this.DAMAGE);
        }
    }

    public void paint(Graphics2D g) {
        g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        g.fillRect(a, b, DIAM, DIAM);
    }

    private boolean collision() {

        Rectangle[] rectangles1 = this.getBoundsArr();
        Rectangle[] rectangles2 = this.enemy.getBoundsArr();

        for (int i = 0; i < rectangles1.length; i++) {
            for (int j = 0; j < rectangles2.length; j++) {
                if (rectangles1[i].intersects(rectangles2[j])) {
                    this.planet.game.sound("sound/planet.wav");
                    return true;
                }
            }
        }

        return false;
    }

    public Rectangle getBounds() {
        return new Rectangle(a, b, DIAM, DIAM);
    }

    public Rectangle[] getBoundsArr() {
        Rectangle[] rectangles = new Rectangle[4];
        rectangles[0] = new Rectangle(a, b + DIAM / 3, DIAM, DIAM / 3);
        rectangles[1] = new Rectangle(a + DIAM / 12, b + DIAM / 6,
                5 * DIAM / 6, 2 * DIAM / 3);
        rectangles[2] = new Rectangle(a + DIAM / 6, b + DIAM / 12,
                2 * DIAM / 3, 5 * DIAM / 6);
        rectangles[3] = new Rectangle(a + DIAM / 3, b, DIAM / 3, DIAM);

        return rectangles;
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }

    public static double getDamage() {
        return AttackPlanet.DAMAGE;
    }

}