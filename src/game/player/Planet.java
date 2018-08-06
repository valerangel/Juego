package game.player;

import game.Game;

import java.awt.*;

/**
 * Created by Angel on 19/06/2018.
 */

public class Planet extends Player {
    private static final int HEALTH_PLANET = 150;
    private static final int DAMAGE_PLANET = AttackPlanet.getDamage();
    private static final int SPEED_PLANET = 7;
    private static final int DIAMETER_PLANET = 48;

    private AttackPlanet attackPlanet;

    public Planet(Number_of_player numPlayer, Player enemy, Game game) {
        super(HEALTH_PLANET, DAMAGE_PLANET, SPEED_PLANET, game, numPlayer, enemy, DIAMETER_PLANET);
        this.attackPlanet = new AttackPlanet(this, this.enemy);
        this.setImage(numPlayer);
    }

    public Planet(Number_of_player numPlayer, Player enemy) {
        this(numPlayer, enemy, null);
    }

    @Override
    public void move() {
        super.move();
        this.attackPlanet.move();
    }

    private void setImage(Number_of_player numPlayer) {
        String resourcePath;
        if (numPlayer == Number_of_player.PLAYER1) {
            resourcePath = "/icon/Planet1.png";
        } else {
            resourcePath = "/icon/Planet2.png";
        }
        this.img = new javax.swing.ImageIcon(getClass().getResource(resourcePath));
    }

    @Override
    public void paint(Graphics2D g) {
        Image image = getImageIcon().getImage();
        g.drawImage(image, x, y, diameter, diameter, game);
        /*Rectangle[] rectangles = this.getBoundsArr();
        for (int i = 0; i < rectangles.length; i++) {
            g.setColor(Color.BLACK);
            g.draw(rectangles[i]);
        }*/
        this.attackPlanet.paint(g);

        super.paintLifeBar(g);
    }

    @Override
    public void setEnemy(Player enemy) {
        super.setEnemy(enemy);
        this.attackPlanet.setEnemy(enemy);
    }

    /*@Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }*/

    public Rectangle[] getBoundsArr() {
        Rectangle[] rectangles = new Rectangle[4];
        rectangles[0] = new Rectangle(x, y + DIAMETER_PLANET / 3, DIAMETER_PLANET, DIAMETER_PLANET / 3);
        rectangles[1] = new Rectangle(x + DIAMETER_PLANET / 12, y + DIAMETER_PLANET / 6,
                5 * DIAMETER_PLANET / 6, 2 * DIAMETER_PLANET / 3);
        rectangles[2] = new Rectangle(x + DIAMETER_PLANET / 6, y + DIAMETER_PLANET / 12,
                2 * DIAMETER_PLANET / 3, 5 * DIAMETER_PLANET / 6);
        rectangles[3] = new Rectangle(x + DIAMETER_PLANET / 3, y, DIAMETER_PLANET / 3, DIAMETER_PLANET);

        return rectangles;
    }

    public AttackPlanet getAttack() {
        return this.attackPlanet;
    }
}