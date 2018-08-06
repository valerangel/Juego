package game.player;

import game.Game;

import java.awt.*;

/**
 * Created by Angel on 22/06/2018.
 */
public class Melee extends Player {
    private static final int HEALTH_MELEE = 100;
    private static final int DAMAGE_MELEE = 1;
    private static final int SPEED_MELEE = 7;
    private static final int DIAMETER_MELEE = 25;

    public Melee(Number_of_player numPlayer, Player enemy, Game game) {
        super(HEALTH_MELEE, DAMAGE_MELEE, SPEED_MELEE, game, numPlayer, enemy, DIAMETER_MELEE);
        this.setImage(numPlayer);
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

    public Melee(Number_of_player numPlayer, Player enemy) {
        this(numPlayer, enemy, null);
    }


    public void paint(Graphics2D g) {
        Image image = getImageIcon().getImage();
        g.drawImage(image, x, y, diameter, diameter, game);

        super.paintLifeBar(g);
    }


    @Override
    public void move() {
        super.move();
        if (collision()) {
            enemy.dealDamage(Melee.DAMAGE_MELEE);
        }

    }

    private boolean collision() {
        Rectangle[] rectangles1 = this.getBoundsArr();
        Rectangle[] rectangles2 = this.enemy.getBoundsArr();

        for (int i = 0; i < rectangles1.length ; i++) {
            for (int j = 0; j <rectangles2.length ; j++) {
                if(rectangles1[i].intersects(rectangles2[j])){
                    return true;
                }
            }
        }

        return false;
    }
}
