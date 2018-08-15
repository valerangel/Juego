package game.player;

import game.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Angel on 23/06/2018.
 */
public class Shoot_Range {

    private static final int DAMAGE_SHOT_RANGE = 5;
    private static final int SPEED_SHOT_RANGE = 9;
    private static final int DIAMETER_SHOT_RANGE = (int) (Range.DIAMETER_RANGE / 3);

    private int x;
    private int y;
    private int speedX;
    private int speedY;
    private Player enemy;
    private int index;
    private Range range;
    private ImageIcon img;

    public Shoot_Range(int x, int y, int speedX, int speedY, Player enemy, int index, Range range) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.range = range;
        this.img = new javax.swing.ImageIcon(getClass().getResource("/icon/Shoot.png"));

        if (speedX > 0) {
            this.speedX = 1;
        } else if (speedX < 0) {
            this.speedX = -1;
        } else {
            this.speedX = 0;
        }

        if (speedY > 0) {
            this.speedY = 1;
        } else if (speedY < 0) {
            this.speedY = -1;
        } else {
            this.speedY = 0;
        }

        this.enemy = enemy;

    }

    public void paint(Graphics2D g, Game game) {
        Image image = this.img.getImage();
        g.drawImage(image, x, y, DIAMETER_SHOT_RANGE, DIAMETER_SHOT_RANGE, game);
    }

    public void move() {
        x += speedX * SPEED_SHOT_RANGE;
        y += speedY * SPEED_SHOT_RANGE;
        if (collision()) {
            enemy.dealDamage(this.DAMAGE_SHOT_RANGE);
            range.eliminateThisShot(index);
        }

        if (x <= -DIAMETER_SHOT_RANGE || x >= Game.WIDTH ||
                y <= -DIAMETER_SHOT_RANGE || y >= Game.HEIGHT) {
            range.eliminateThisShot(index);
        }

        if (collisionAttackPlanet()) {
            range.eliminateThisShot(index);
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
        return DAMAGE_SHOT_RANGE;
    }

    public Rectangle[] getBoundsArr() {
        Rectangle[] rectangles = new Rectangle[4];
        rectangles[0] = new Rectangle(x, y + DIAMETER_SHOT_RANGE / 3, DIAMETER_SHOT_RANGE, DIAMETER_SHOT_RANGE / 3);
        rectangles[1] = new Rectangle(x + DIAMETER_SHOT_RANGE / 12, y + DIAMETER_SHOT_RANGE / 6,
                5 * DIAMETER_SHOT_RANGE / 6, 2 * DIAMETER_SHOT_RANGE / 3);
        rectangles[2] = new Rectangle(x + DIAMETER_SHOT_RANGE / 6, y + DIAMETER_SHOT_RANGE / 12,
                2 * DIAMETER_SHOT_RANGE / 3, 5 * DIAMETER_SHOT_RANGE / 6);
        rectangles[3] = new Rectangle(x + DIAMETER_SHOT_RANGE / 3, y, DIAMETER_SHOT_RANGE / 3, DIAMETER_SHOT_RANGE);

        return rectangles;
    }

    private boolean collisionAttackPlanet() {
        if (this.enemy instanceof Planet) {

            return ((Planet) this.enemy).getAttack().getBounds().intersects(getBounds());
        }
        return false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETER_SHOT_RANGE, DIAMETER_SHOT_RANGE);
    }

}
