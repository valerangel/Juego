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
    private static final double DAMAGE_RANGE = AttackPlanet.getDamage();
    private static final int SPEED_RANGE = 7;
    protected static final int DIAMETER_RANGE = 60;
    private static final int RECHARGE_TIME = 30;

    private static final int WIDTH_RECHARGE_BAR = 150;
    private static final int HEIGHT_RECHARGE_BAR = 15;
    private static final int X_RECHARGE_BAR = 40;
    private static final int Y_RECHARGE_BAR = Game.HEIGHT - 50 - HEIGHT_RECHARGE_BAR;

    private int speedXShoot;
    private int speedYShoot;
    private int recharge;
    private int numberShot;

    private ArrayList<Shoot_Range> shoots;

    public Range(Number_of_player numPlayer, Player enemy, Game game) {
        super(HEALTH_RANGE, DAMAGE_RANGE, SPEED_RANGE, game, numPlayer, enemy, DIAMETER_RANGE);
        this.recharge = 0;
        this.speedXShoot = this.speed;
        this.speedYShoot = 0;
        if (this.numPlayer == Number_of_player.PLAYER2) {
            this.speedXShoot = -this.speed;
        }
        this.shoots = new ArrayList<Shoot_Range>(0);
        this.numberShot = 0;
        this.setImage(numPlayer);
    }

    public Range(Number_of_player numPlayer, Player enemy) {
        this(numPlayer, enemy, null);
    }

    @Override
    public void paint(Graphics2D g) {

        Image image = this.img.getImage();
        g.drawImage(image, x, y, DIAMETER_RANGE, DIAMETER_RANGE, game);

        for (int i = 0; i < shoots.size(); i++) {
            if (shoots.get(i) != null)
                shoots.get(i).paint(g, game);
        }

        super.paintLifeBar(g);
        this.paintChargeBar(g);
    }

    private void setImage(Number_of_player numPlayer) {
        String resourcePath;
        if (numPlayer == Number_of_player.PLAYER1) {
            resourcePath = "/icon/Range1.png";
        } else {
            resourcePath = "/icon/Range2.png";
        }
        this.img = new javax.swing.ImageIcon(getClass().getResource(resourcePath));
    }

    private void paintChargeBar(Graphics2D g) {
        g.setColor(Color.BLACK);
        if (this.numPlayer == Number_of_player.PLAYER1) {
            g.drawRect(X_RECHARGE_BAR, Y_RECHARGE_BAR, WIDTH_RECHARGE_BAR, HEIGHT_RECHARGE_BAR);
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

    public void eliminateThisShot(int index) {
        this.shoots.set(index, null);
    }


    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_F || e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
            speedXShoot = -1;
            speedYShoot = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_H || e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
            speedXShoot = 1;
            speedYShoot = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_T || e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
            speedYShoot = -1;
            speedXShoot = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_G || e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
            speedYShoot = 1;
            speedXShoot = 0;
        }

    }


    /*public void move() {
        super.move();

        if (speedYPos - speedYNeg != 0) {
            this.lastSpeedY = speedYPos - speedYNeg;
        } else if (speedXPos - speedXNeg != 0) {
            this.lastSpeedY = 0;
        }
        if (speedXPos - speedXNeg != 0) {
            this.lastSpeedX = speedXPos - speedXNeg;
        } else if (speedYPos - speedYNeg != 0) {
            this.lastSpeedX = 0;
        }

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
            Shoot_Range newShot = new Shoot_Range(x + diameter / 3, y + diameter / 3, lastSpeedX,
                    lastSpeedY, this.enemy, this.numberShot, this);
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
    }*/
}
