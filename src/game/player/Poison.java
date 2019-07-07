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
    private static final double DAMAGE_POISON = PoisonsPoison.getDamage();
    private static final int SPEED_POISON = 6;
    protected static final int DIAMETER_POISON = 60;
    private static final int RECHARGE_TIME = 3;
    private static final int RECHARGE_BOOST = 150;
    private static final int TIME_BOOSTED = 75;

    private static final int WIDTH_RECHARGE_BAR = 150;
    private static final int HEIGHT_RECHARGE_BAR = 15;
    private static final int X_RECHARGE_BAR = 40;
    private static final int Y_RECHARGE_BAR = Game.HEIGHT - 50 - HEIGHT_RECHARGE_BAR;

    private int recharge;
    private int rechargeBoost;
    private int timeBoosted;
    private boolean boosted;
    private int numberShot;

    private ArrayList<PoisonsPoison> poisons;

    public Poison(PlayerNumber numPlayer, Game game) {
        super(HEALTH_POISON, DAMAGE_POISON, SPEED_POISON, game, numPlayer, DIAMETER_POISON);
        this.recharge = 0;
        this.boosted = false;
        this.rechargeBoost = 0;
        this.poisons = new ArrayList<>(0);
        this.numberShot = 0;
        this.setImage(numPlayer, boosted);
        this.timeBoosted = 0;
    }

    public Poison(PlayerNumber numPlayer) {
        this(numPlayer, null);
    }

    @Override
    public void paint(Graphics2D g) {

        for (int i = 0; i < poisons.size(); i++) {
            if (poisons.get(i) != null)
                poisons.get(i).paint(g, game);
        }

        Image image = this.img.getImage();
        if(!invulnerable || contInvulnerable % 2 == 0) {
            g.drawImage(image, (int) x, (int) y, DIAMETER_POISON, DIAMETER_POISON, game);
        }


        super.paintLifeBar(g);
        this.paintChargeBar(g);
    }

    private void paintChargeBar(Graphics2D g) {
        g.setColor(Color.BLACK);
        if (this.numPlayer == PlayerNumber.PLAYER1) {
            g.drawRect(X_RECHARGE_BAR, Y_RECHARGE_BAR, WIDTH_RECHARGE_BAR, HEIGHT_RECHARGE_BAR);
            if (!boosted) {
                g.fillRect(X_RECHARGE_BAR, Y_RECHARGE_BAR,
                        WIDTH_RECHARGE_BAR * rechargeBoost / RECHARGE_BOOST, HEIGHT_RECHARGE_BAR);
            } else {
                g.setColor(Color.yellow);
                g.fillRect(X_RECHARGE_BAR, Y_RECHARGE_BAR,
                        (int) (WIDTH_RECHARGE_BAR * (1 - ((double) timeBoosted / (double) TIME_BOOSTED))), HEIGHT_RECHARGE_BAR);
            }
        } else {
            g.drawRect(Game.WIDTH - X_RECHARGE_BAR - WIDTH_RECHARGE_BAR, Y_RECHARGE_BAR,
                    WIDTH_RECHARGE_BAR, HEIGHT_RECHARGE_BAR);
            if (!boosted) {
                g.fillRect(Game.WIDTH - X_RECHARGE_BAR - WIDTH_RECHARGE_BAR, Y_RECHARGE_BAR,
                        WIDTH_RECHARGE_BAR * rechargeBoost / RECHARGE_BOOST, HEIGHT_RECHARGE_BAR);
            } else {
                g.setColor(Color.yellow);
                g.fillRect(Game.WIDTH - X_RECHARGE_BAR - WIDTH_RECHARGE_BAR, Y_RECHARGE_BAR,
                        (int) (WIDTH_RECHARGE_BAR * (1 - ((double) timeBoosted / (double) TIME_BOOSTED))), HEIGHT_RECHARGE_BAR);
            }
        }
    }

    private void setImage(PlayerNumber numPlayer, boolean boost) {
        String resourcePath;
        if (!boost) {
            if (numPlayer == PlayerNumber.PLAYER1) {
                resourcePath = "/icon/Poison1.png";
            } else {
                resourcePath = "/icon/Poison2.png";
            }
        } else {
            if (numPlayer == PlayerNumber.PLAYER1) {
                resourcePath = "/icon/Poison1Boosted.png";
            } else {
                resourcePath = "/icon/Poison2Boosted.png";
            }
        }
        this.img = new javax.swing.ImageIcon(getClass().getResource(resourcePath));
    }

    public void move() {
        if (!boosted) {
            super.move();
            this.setImage(this.numPlayer, boosted);
            if (rechargeBoost < RECHARGE_BOOST) {
                rechargeBoost++;
            }

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
                PoisonsPoison newPoison = new PoisonsPoison((int) x + diameter / 3, (int) y + diameter / 3,
                        this.enemy, this.numberShot, this);
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
        } else {
            this.setImage(this.numPlayer, boosted);
            if (timeBoosted <= TIME_BOOSTED) {
                timeBoosted++;
            } else {
                this.boosted = false;
                this.rechargeBoost = 0;
                this.timeBoosted = 0;
            }
        }
    }

    public void eliminateThisPoison(int index) {
        this.poisons.set(index, null);
    }

    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (this.game.isAttackUp(e)) {
            if (rechargeBoost >= RECHARGE_BOOST & this.boosted == false) {
                this.game.sound("sound/antonitt.wav");
                this.boosted = true;
            }
        }

    }

    public void dealDamage(double damage) {
        if (!boosted)
            super.dealDamage(damage);
    }

    public void dealDamageInv(int damage) {
        if (!boosted)
            super.dealDamageInv(damage);
    }
}