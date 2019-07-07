package game.boost;

import game.Game;
import game.player.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created by Angel on 28/06/2018.
 */
public class Boost_Heal extends Boost {

    private static final int MAX_TIME = 2000;
    private static final int DIAMETER_HEAL = 20;
    private static final int HEALING = 25;
    private static final String RESOURCHE_PATH = "/icon/Heal.png";

    public Boost_Heal(int index, Game game) {
        super((int) (Math.random() * (game.getWidth() - DIAMETER_HEAL)),
                (int) (Math.random() * (game.getHeight() - DIAMETER_HEAL)),
                DIAMETER_HEAL, Boost_Heal.MAX_TIME, index, RESOURCHE_PATH, game);
    }

    @Override
    public void collision(Player player) {
        if (player.getBounds().intersects(getBounds())) {
            player.heal(HEALING);
            this.game.sound("sound/heal.wav");
            this.game.eliminateBoost(this.index);
        }
    }

    @Override
    public void move(Player player1, Player player2) {
        this.collision(player1);
        this.collision(player2);
    }

    @NotNull
    private Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETER_HEAL, DIAMETER_HEAL);
    }

}