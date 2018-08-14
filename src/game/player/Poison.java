package game.player;

import game.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Angel on 23/06/2018.
 */
public class Poison extends Player {

    private static final double HEALTH_POISON = 120;
    private static final double DAMAGE_POISON = AttackPlanet.getDamage();
    private static final int SPEED_POISON = 7;
    protected static final int DIAMETER_POISON = 60;
    private static final int RECHARGE_TIME = 3;

    /*private static final int WIDTH_RECHARGE_BAR = 150;
    private static final int HEIGHT_RECHARGE_BAR = 15;
    private static final int X_RECHARGE_BAR = 40;
    private static final int Y_RECHARGE_BAR = Game.HEIGHT - 50 - HEIGHT_RECHARGE_BAR;*/

    private int recharge;
    private int numberShot;

    private ArrayList<PoisonsPoison> poisons;

    public Poison(Number_of_player numPlayer, Player enemy, Game game) {
        super(HEALTH_POISON, DAMAGE_POISON, SPEED_POISON, game, numPlayer, enemy, DIAMETER_POISON);
        this.recharge = 0;

        this.poisons = new ArrayList<PoisonsPoison>(0);
        this.numberShot = 0;
        this.setImage(numPlayer);
    }

    public Poison(Number_of_player numPlayer, Player enemy) {
        this(numPlayer, enemy, null);
    }

    @Override
    public void paint(Graphics2D g) {

        Image image = this.img.getImage();
        g.drawImage(image, x, y, DIAMETER_POISON, DIAMETER_POISON, game);

        for (int i = 0; i < poisons.size(); i++) {
            if (poisons.get(i) != null)
                poisons.get(i).paint(g, game);
        }

        super.paintLifeBar(g);
    }

    private void setImage(Number_of_player numPlayer) {
        String resourcePath;
        if (numPlayer == Number_of_player.PLAYER1) {
            resourcePath = "/icon/Poison1.png";
        } else {
            resourcePath = "/icon/Poison2.png";
        }
        this.img = new javax.swing.ImageIcon(getClass().getResource(resourcePath));
    }

    public void move() {
        super.move();

        if (recharge <= RECHARGE_TIME) {
            recharge++;
        } else {
            recharge = 0;
            numberShot = poisons.size();
            for (int i = 0; i < poisons.size(); i++) {
                if (poisons.get(i) == null) {
                    numberShot = i;
                    break;
                }
            }
            PoisonsPoison newPoison = new PoisonsPoison(x + diameter / 3, y + diameter / 3, this.enemy, this.numberShot, this);
            if (numberShot < poisons.size()) {
                poisons.set(numberShot, newPoison);
            } else {
                poisons.add(numberShot, newPoison);
            }
        }

        for (int i = 0; i < poisons.size(); i++) {
            if (poisons.get(i) != null)
                poisons.get(i).move();
        }
    }

    public void eliminateThisPoison(int index) {
        this.poisons.set(index, null);
    }

}
