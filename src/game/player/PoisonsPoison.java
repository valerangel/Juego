package game.player;

import game.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Angel on 23/06/2018.
 */
public class PoisonsPoison {

    public static final double DAMAGE_POISON = 0.2;
    public static final int POISON_TIME = 40;
    private static final int DIAMETER_POISON = (int) (Poison.DIAMETER_POISON / 2);

    public static final int POISON_TIME_IN_SCREEN = 200;

    private int x;
    private int y;
    private Player enemy;
    private int index;
    private Poison poison;
    private ImageIcon img;

    private int time;

    public PoisonsPoison(int x, int y, Player enemy, int index, Poison poison) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.poison = poison;
        this.img = new javax.swing.ImageIcon(getClass().getResource("/icon/PoisonsPoison.png"));

        this.enemy = enemy;

    }

    public void paint(Graphics2D g, Game game) {
        Image image = this.img.getImage();
        g.drawImage(image, x, y, DIAMETER_POISON, DIAMETER_POISON, game);
    }

    public void move() {

        if(time>= POISON_TIME_IN_SCREEN ){
            poison.eliminateThisPoison(index);
        } else{
            time++;
        }

        if (collision()) {
            enemy.poisonPlayer();
            poison.eliminateThisPoison(index);
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

    public Rectangle[] getBoundsArr() {
        Rectangle[] rectangles = new Rectangle[4];
        rectangles[0] = new Rectangle(x, y + DIAMETER_POISON / 3, DIAMETER_POISON, DIAMETER_POISON / 3);
        rectangles[1] = new Rectangle(x + DIAMETER_POISON / 12, y + DIAMETER_POISON / 6,
                5 * DIAMETER_POISON / 6, 2 * DIAMETER_POISON / 3);
        rectangles[2] = new Rectangle(x + DIAMETER_POISON / 6, y + DIAMETER_POISON / 12,
                2 * DIAMETER_POISON / 3, 5 * DIAMETER_POISON / 6);
        rectangles[3] = new Rectangle(x + DIAMETER_POISON / 3, y, DIAMETER_POISON / 3, DIAMETER_POISON);

        return rectangles;
    }

    private boolean collisionAttackPlanet() {
        if (this.enemy instanceof Planet) {

            return ((Planet) this.enemy).getAttack().getBounds().intersects(getBounds());
        }
        return false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETER_POISON, DIAMETER_POISON);
    }

}