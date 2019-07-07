package game.player;

import game.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Angel on 23/06/2018.
 */
public class Range extends Player {

    private static final double HEALTH_RANGE = 95;
    private static final double DAMAGE_RANGE = Shoot_Range.getDamage();
    private static final int SPEED_RANGE = 7;
    protected static final int DIAMETER_RANGE = 60;
    private static final int RECHARGE_TIME = 20;

    private static final int WIDTH_RECHARGE_BAR = 150;
    private static final int HEIGHT_RECHARGE_BAR = 15;
    private static final int X_RECHARGE_BAR = 40;
    private static final int Y_RECHARGE_BAR = Game.HEIGHT - 50 - HEIGHT_RECHARGE_BAR;

    private double speedXShoot;
    private double speedYShoot;
    private int recharge;
    private int numberShot;

    private ArrayList<Shoot_Range> shoots;

    public Range(PlayerNumber numPlayer, Game game) {
        super(HEALTH_RANGE, DAMAGE_RANGE, SPEED_RANGE, game, numPlayer, DIAMETER_RANGE);
        this.recharge = 0;
        this.speedXShoot = this.speed;
        this.speedYShoot = 0;
        if (this.numPlayer == PlayerNumber.PLAYER2) {
            this.speedXShoot = -this.speed;
        }
        this.shoots = new ArrayList<>(0);
        this.numberShot = 0;
        this.setImage(numPlayer);
    }

    public Range(PlayerNumber numPlayer) {
        this(numPlayer, null);
    }

    @Override
    public void paint(Graphics2D g) {

        Image image = this.img.getImage();
        if(!invulnerable || contInvulnerable % 2 == 0) {
            g.drawImage(image, (int) x, (int) y, DIAMETER_RANGE, DIAMETER_RANGE, game);
        }
        for (int i = 0; i < shoots.size(); i++) {
            if (shoots.get(i) != null)
                shoots.get(i).paint(g, game);
        }

        super.paintLifeBar(g);
        this.paintChargeBar(g);
    }

    private void setImage(PlayerNumber numPlayer) {
        String resourcePath;
        if (numPlayer == PlayerNumber.PLAYER1) {
            resourcePath = "/icon/Range1.png";
        } else {
            resourcePath = "/icon/Range2.png";
        }
        this.img = new javax.swing.ImageIcon(getClass().getResource(resourcePath));
    }

    private void paintChargeBar(Graphics2D g) {
        g.setColor(Color.BLACK);
        if (this.numPlayer == PlayerNumber.PLAYER1) {
            g.drawRect(X_RECHARGE_BAR , Y_RECHARGE_BAR, WIDTH_RECHARGE_BAR, HEIGHT_RECHARGE_BAR);
            g.fillRect(X_RECHARGE_BAR, Y_RECHARGE_BAR,
                    WIDTH_RECHARGE_BAR * recharge / RECHARGE_TIME, HEIGHT_RECHARGE_BAR);
        } else {
            g.drawRect(Game.WIDTH - X_RECHARGE_BAR - WIDTH_RECHARGE_BAR, Y_RECHARGE_BAR,
                    WIDTH_RECHARGE_BAR, HEIGHT_RECHARGE_BAR);
            g.fillRect(Game.WIDTH - X_RECHARGE_BAR - WIDTH_RECHARGE_BAR, Y_RECHARGE_BAR,
                    WIDTH_RECHARGE_BAR * recharge / RECHARGE_TIME, HEIGHT_RECHARGE_BAR);
        }
    }

    public void move() {
        super.move();

        if (recharge <= RECHARGE_TIME) {
            recharge++;
        } else {
            recharge = 0;
            numberShot = shoots.size();
            for (int i = 0; i < shoots.size(); i++) {
                if (shoots.get(i) == null) {
                    numberShot = i;
                    break;
                }
            }
            Shoot_Range newShot = new Shoot_Range(x + diameter / 3, y + diameter / 3, speedXShoot,
                    speedYShoot, this.enemy, this.numberShot, this);
            if (numberShot < shoots.size()) {
                shoots.set(numberShot, newShot);
            } else {
                shoots.add(numberShot, newShot);
            }
        }

        for (int i = 0; i < shoots.size(); i++) {
            if (shoots.get(i) != null)
                shoots.get(i).move();
        }
    }

    public void eliminateThisShoot(int index) {
        this.shoots.set(index, null);
    }


    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (this.game.isAttackLeft(e)) {
            speedXShoot = -1;
            speedYShoot = 0;
        }
        if (this.game.isAttackRight(e)) {
            speedXShoot = 1;
            speedYShoot = 0;
        }
        if (this.game.isAttackUp(e)) {
            speedYShoot = -1;
            speedXShoot = 0;
        }
        if (this.game.isAttackDown(e)) {
            speedYShoot = 1;
            speedXShoot = 0;
        }

    }

}