package game.boost;

import game.Game;
import game.player.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created by Angel on 28/06/2018.
 */
public class Boost_Mine extends Boost {

    private static final int MAX_TIME = 2000;
    private static final int DIAMETER_MINE = 20;
    private static final int DAMAGE = 18;
    private static final String RESOURCHE_PATH = "/icon/Mine.png";

    public Boost_Mine(int index, Game game) {
        super((int) (Math.random() * (game.getWidth() - DIAMETER_MINE)),
                (int) (Math.random() * (game.getHeight() - DIAMETER_MINE)),
                DIAMETER_MINE, Boost_Mine.MAX_TIME, index, RESOURCHE_PATH, game);
    }

    @Override
    public void collision(Player player) {
        Rectangle[] rectangles1 = this.getBoundsArr();
        Rectangle[] rectangles2 = player.getBoundsArr();

        boolean damageOnlyOnce = true;

        for (int i = 0; i < rectangles1.length; i++) {
            for (int j = 0; j < rectangles2.length; j++) {
                if (rectangles1[i].intersects(rectangles2[j]) && damageOnlyOnce) {
                    player.dealDamage(DAMAGE);
                    damageOnlyOnce = false;
                    this.game.sound("sound/bomb.wav");
                    this.game.eliminateBoost(this.index);
                    break;
                }
            }
        }

    }

    @Override
    public void move(Player player1, Player player2) {
        this.collision(player1);
        this.collision(player2);
    }

    @NotNull
    private Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETER_MINE, DIAMETER_MINE);
    }

    public Rectangle[] getBoundsArr() {
        Rectangle[] rectangles = new Rectangle[4];
        rectangles[0] = new Rectangle(x, y + diameter / 3, diameter, diameter / 3);
        rectangles[1] = new Rectangle(x + diameter / 12, y + diameter / 6,
                5 * diameter / 6, 2 * diameter / 3);
        rectangles[2] = new Rectangle(x + diameter / 6, y + diameter / 12,
                2 * diameter / 3, 5 * diameter / 6);
        rectangles[3] = new Rectangle(x + diameter / 3, y, diameter / 3, diameter);

        return rectangles;
    }

}