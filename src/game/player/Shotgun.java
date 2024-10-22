package game.player;

import game.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Angel on 23/06/2018.
 */
public class Shotgun extends Player {

    private static final double HEALTH_SHOTGUN = 120;
    private static final double DAMAGE_SHOTGUN = Bullet_Shotgun.getDamage();
    private static final int SPEED_SHOTGUN = 5;
    protected static final int DIAMETER_SHOTGUN = 60;
    private static final int RECHARGE_SHOTGUN = 80;
    private static final int BULLETS_PER_SHOT = 7;

    private static final int WIDTH_RECHARGE_BAR = 150;
    private static final int HEIGHT_RECHARGE_BAR = 15;
    private static final int X_RECHARGE_BAR = 40;
    private static final int Y_RECHARGE_BAR = Game.HEIGHT - 50 - HEIGHT_RECHARGE_BAR;

    private double speedBullet;
    private double angle;
    private int recharge;
    private int numberShot;
    private boolean shooting;

    private ArrayList<Bullet_Shotgun> bullets;

    public Shotgun(PlayerNumber numPlayer, Game game) {
        super(HEALTH_SHOTGUN, DAMAGE_SHOTGUN, SPEED_SHOTGUN, game, numPlayer, DIAMETER_SHOTGUN);
        this.recharge = 0;
        this.angle = 0;
        this.shooting = false;
        this.bullets = new ArrayList<>(0);
        this.numberShot = 0;
        this.setImage(numPlayer);
    }

    public Shotgun(PlayerNumber numPlayer) {
        this(numPlayer, null);
    }

    @Override
    public void paint(Graphics2D g) {

        Image image = this.img.getImage();
        if(!invulnerable || contInvulnerable % 2 == 0) {
            g.drawImage(image, (int) x, (int) y, DIAMETER_SHOTGUN, DIAMETER_SHOTGUN, game);
        }

        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i) != null)
                bullets.get(i).paint(g, game);
        }

        super.paintLifeBar(g);
        this.paintChargeBar(g);
    }

    private void setImage(PlayerNumber numPlayer) {
        String resourcePath;
        if (numPlayer == PlayerNumber.PLAYER1) {
            resourcePath = "/icon/Shotgun1.png";
        } else {
            resourcePath = "/icon/Shotgun2.png";
        }
        this.img = new javax.swing.ImageIcon(getClass().getResource(resourcePath));
    }

    private void paintChargeBar(Graphics2D g) {
        g.setColor(Color.BLACK);
        if (this.numPlayer == PlayerNumber.PLAYER1) {
            g.drawRect(X_RECHARGE_BAR, Y_RECHARGE_BAR, WIDTH_RECHARGE_BAR, HEIGHT_RECHARGE_BAR);
            g.fillRect(X_RECHARGE_BAR, Y_RECHARGE_BAR,
                    WIDTH_RECHARGE_BAR * recharge / RECHARGE_SHOTGUN, HEIGHT_RECHARGE_BAR);
        } else {
            g.drawRect(Game.WIDTH - X_RECHARGE_BAR - WIDTH_RECHARGE_BAR, Y_RECHARGE_BAR,
                    WIDTH_RECHARGE_BAR, HEIGHT_RECHARGE_BAR);
            g.fillRect(Game.WIDTH - X_RECHARGE_BAR - WIDTH_RECHARGE_BAR, Y_RECHARGE_BAR,
                    WIDTH_RECHARGE_BAR * recharge / RECHARGE_SHOTGUN, HEIGHT_RECHARGE_BAR);
        }
    }

    public void move() {
        super.move();

        if (recharge < RECHARGE_SHOTGUN) {
            recharge++;
        }
        if (shooting && recharge >= RECHARGE_SHOTGUN) {
            recharge = 0;
            this.game.sound("sound/shotgun.wav");
            this.game.sound("sound/shotgun.wav");
            this.game.sound("sound/shotgun.wav");
            for (int j = 0; j < BULLETS_PER_SHOT; j++) {
                numberShot = bullets.size();
                for (int i = 0; i < bullets.size(); i++) {
                    if (bullets.get(i) == null) {
                        numberShot = i;
                        break;
                    }
                }

                Bullet_Shotgun newBullet = new Bullet_Shotgun(x + 5 * diameter / 12, y + 2 * diameter / 5,
                        angle - Math.PI / 8 + j * Math.PI / 4 / (BULLETS_PER_SHOT - 1), this.enemy, this.numberShot, this);
                if (numberShot < bullets.size()) {
                    bullets.set(numberShot, newBullet);
                } else {
                    bullets.add(numberShot, newBullet);
                }
            }
        }

        for (Bullet_Shotgun bullet : bullets) {
            if (bullet != null)
                bullet.move();
        }
        shooting = false;
    }

    public void eliminateThisBullet(int index) {
        this.bullets.set(index, null);
    }

    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (this.game.isAttackLeft(e)) {
            angle = Math.PI;
            shooting = true;
        }
        if (this.game.isAttackRight(e)) {
            angle = 0;
            shooting = true;
        }
        if (this.game.isAttackUp(e)) {
            angle = 1.5 * Math.PI;
            shooting = true;
        }
        if (this.game.isAttackDown(e)) {
            angle = 0.5 * Math.PI;
            shooting = true;
        }

    }

}