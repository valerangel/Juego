package game.player;

import game.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Angel on 23/06/2018.
 */
public class Bullet_Shotgun {

    private static final double DAMAGE_BULLET_SHOTGUN = 3.5;
    private static final double SPEED_BULLET_SHOTGUN = 11;
    private static final int DIAMETER_BULLET_SHOTGUN = (int) (Shotgun.DIAMETER_SHOTGUN / 6);
    private static final int TIME_BULLET = 25;

    private double x;
    private double y;
    private double cos;
    private double sin;
    private Player enemy;
    private int index;
    private Shotgun shotgun;
    private ImageIcon img;
    private int time;

    public Bullet_Shotgun(double x, double y, double angle, Player enemy, int index, Shotgun shotgun) {
        this.x = x;
        this.y = y;
        this.time = 0;
        this.index = index;
        this.shotgun = shotgun;
        this.img = new javax.swing.ImageIcon(getClass().getResource("/icon/Shoot.png"));
        this.cos = Math.cos(angle);
        this.sin = Math.sin(angle);
        this.enemy = enemy;

    }

    public void paint(Graphics2D g, Game game) {
        Image image = this.img.getImage();
        g.drawImage(image, (int) x, (int) y, DIAMETER_BULLET_SHOTGUN, DIAMETER_BULLET_SHOTGUN, game);
    }

    public void move() {
        if(time >= TIME_BULLET){
            this.shotgun.eliminateThisBullet(index);
        }

        time++;
        x += SPEED_BULLET_SHOTGUN*cos;
        y += SPEED_BULLET_SHOTGUN*sin;
        if (collision()) {
            enemy.dealDamage(this.DAMAGE_BULLET_SHOTGUN);
            shotgun.eliminateThisBullet(index);
        }

        if (x <= -DIAMETER_BULLET_SHOTGUN || x >= Game.WIDTH ||
                y <= -DIAMETER_BULLET_SHOTGUN || y >= Game.HEIGHT) {
            shotgun.eliminateThisBullet(index);
        }

        if (collisionAttackPlanet()) {
            shotgun.eliminateThisBullet(index);
        }


    }

    private boolean collision() {
        Rectangle[] rectangles1 = this.getBoundsArr();
        Rectangle[] rectangles2 = this.enemy.getBoundsArr();

        for (int i = 0; i < rectangles1.length; i++) {
            for (int j = 0; j < rectangles2.length; j++) {
                if (rectangles1[i].intersects(rectangles2[j])) {
                    return true;
                }
            }
        }

        return false;
    }

    public static double getDamage(){
        return DAMAGE_BULLET_SHOTGUN;
    }

    public Rectangle[] getBoundsArr() {
        Rectangle[] rectangles = new Rectangle[4];
        rectangles[0] = new Rectangle((int) x, (int) y + DIAMETER_BULLET_SHOTGUN / 3, DIAMETER_BULLET_SHOTGUN, DIAMETER_BULLET_SHOTGUN / 3);
        rectangles[1] = new Rectangle((int) x + DIAMETER_BULLET_SHOTGUN / 12, (int) y + DIAMETER_BULLET_SHOTGUN / 6,
                5 * DIAMETER_BULLET_SHOTGUN / 6, 2 * DIAMETER_BULLET_SHOTGUN / 3);
        rectangles[2] = new Rectangle((int) x + DIAMETER_BULLET_SHOTGUN / 6,(int) y + DIAMETER_BULLET_SHOTGUN / 12,
                2 * DIAMETER_BULLET_SHOTGUN / 3, 5 * DIAMETER_BULLET_SHOTGUN / 6);
        rectangles[3] = new Rectangle((int)x + DIAMETER_BULLET_SHOTGUN / 3, (int)y, DIAMETER_BULLET_SHOTGUN / 3, DIAMETER_BULLET_SHOTGUN);

        return rectangles;
    }

    private boolean collisionAttackPlanet() {
        if (this.enemy instanceof Planet) {

            return ((Planet) this.enemy).getAttack().getBounds().intersects(getBounds());
        }
        return false;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int) y, DIAMETER_BULLET_SHOTGUN, DIAMETER_BULLET_SHOTGUN);
    }

}
