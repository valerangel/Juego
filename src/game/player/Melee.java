package game.player;

import game.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Angel on 22/06/2018.
 */
public class Melee extends Player {
    private static final int HEALTH_MELEE = 110;
    private static final int DAMAGE_MELEE = 18;
    private static final int SPEED_MELEE = 6;
    private static final int SPEED_MELEE_BOOSTED = 10;
    private static final int DIAMETER_MELEE = 30;

    private static final int RECHARGE_TIME = 200;
    private static final int WIDTH_RECHARGE_BAR = 150;
    private static final int HEIGHT_RECHARGE_BAR = 15;
    private static final int X_RECHARGE_BAR = 40;
    private static final int Y_RECHARGE_BAR = Game.HEIGHT - 50 - HEIGHT_RECHARGE_BAR;

    private int recharge;
    private boolean boosted;

    public Melee(Number_of_player numPlayer, Player enemy, Game game) {
        super(HEALTH_MELEE, DAMAGE_MELEE, SPEED_MELEE, game, numPlayer, enemy, DIAMETER_MELEE);
        this.setImage(numPlayer);
        boosted = false;
    }

    private void setImage(Number_of_player numPlayer) {
        String resourcePath;
        if (numPlayer == Number_of_player.PLAYER1) {
            resourcePath = "/icon/Melee1.png";
        } else {
            resourcePath = "/icon/Melee2.png";
        }
        this.img = new javax.swing.ImageIcon(getClass().getResource(resourcePath));
    }

    private void setImage() {
        String resourcePath;
        if (numPlayer == Number_of_player.PLAYER1) {
            if(!boosted)
            resourcePath = "/icon/Melee1.png";
            else{

                resourcePath = "/icon/Melee1Boosted.png";
            }
        } else {
            if(!boosted)
                resourcePath = "/icon/Melee2.png";
            else{

                resourcePath = "/icon/Melee2Boosted.png";
            }
        }
        this.img = new javax.swing.ImageIcon(getClass().getResource(resourcePath));
    }

    public Melee(Number_of_player numPlayer, Player enemy) {
        this(numPlayer, enemy, null);
    }

    public void paint(Graphics2D g) {
        this.setImage();
        Image image = getImageIcon().getImage();


        g.drawImage(image, x, y, diameter, diameter, game);

        super.paintLifeBar(g);
        this.paintChargeBar(g);
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

    @Override
    public void move() {

        if(recharge<=0) boosted = false;

        if (boosted && (recharge > 0)) {
            recharge -= 7;
            this.speed = SPEED_MELEE_BOOSTED;
            this.assignVelocity(SPEED_MELEE_BOOSTED);
        } else {
            if (recharge < RECHARGE_TIME) {
                recharge++;
            }
            this.speed = SPEED_MELEE;
            this.assignVelocity(SPEED_MELEE);
        }


        super.move();

        if (collision()) {
            enemy.dealDamageInv(Melee.DAMAGE_MELEE);
        }

    }

    public void assignVelocity(int vel) {

        if (speedXNeg > 0)
            speedXNeg = vel;
        if (speedXPos > 0)
            speedXPos = vel;
        if (speedYNeg > 0)
            speedYNeg = vel;
        if (speedYPos > 0)
            speedYPos = vel;
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

    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        if (e.getKeyCode() == KeyEvent.VK_T || e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
            this.boosted = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_T || e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
            this.boosted = true;
        }

    }
}
