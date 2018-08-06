package game.player;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Angel on 19/06/2018.
 */
public abstract class Player {

    protected int diameter;
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected int speed;
    protected Number_of_player numPlayer;
    protected Player enemy;
    protected Game game;
    protected ImageIcon img;

    protected int x;
    protected int y;
    protected int speedXPos;
    protected int speedYPos;
    protected int speedXNeg;
    protected int speedYNeg;

    private static final int X_LIFE_BAR = 40;
    private static final int Y_LIFE_BAR = 30;
    private static final int WIDTH_LIFE_BAR = 150;
    private static final int HEIGHT_LIFE_BAR = 15;


    public Player(int health, int damage, int speed, Game game, Number_of_player numPlayer, Player enemy, int diameter) {
        this.health = health;
        this.maxHealth = this.health;
        this.damage = damage;
        this.speed = speed;
        this.numPlayer = numPlayer;
        this.game = game;
        this.diameter = diameter;
        this.enemy = enemy;

        //Posición inicial
        if (this.numPlayer == Number_of_player.PLAYER1) {
            x = 100;
            y = 200;
        } else {
            x = 500;
            y = 200;
        }

        //Velocidad
        speedXPos = 0;
        speedYPos = 0;
        speedXNeg = 0;
        speedYNeg = 0;
    }

    public void move() {
        if (x + speedXPos - speedXNeg < 0)
            speedXNeg = 0;
        if (x + speedXPos - speedXNeg > game.getWidth() - diameter)
            speedXPos = 0;
        if (y + speedYPos - speedYNeg < 0)
            speedYNeg = 0;
        if (y + speedYPos - speedYNeg > game.getHeight() - diameter)
            speedYPos = 0;

        x = x + speedXPos - speedXNeg;
        y = y + speedYPos - speedYNeg;

        if (this.health <= 0) game.gameOver(this.enemy.getNumPlayer());
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }

    abstract public void paint(Graphics2D g);

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
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

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
            speedXNeg = 0;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
            speedXPos = 0;
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
            speedYNeg = 0;
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
            speedYPos = 0;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
            speedXNeg = this.speed;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
            speedXPos = this.speed;
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
            speedYNeg = this.speed;
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
            speedYPos = this.speed;
    }


    public void setGame(Game game) {
        this.game = game;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getDiameter() {
        return this.diameter;
    }


    public void dealDamage(int damage) {
        this.health -= damage;
    }

    public Number_of_player getNumPlayer() {
        return this.numPlayer;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    protected void paintLifeBar(Graphics2D g) {
        g.setColor(Color.black);
        if (this.numPlayer == Number_of_player.PLAYER1) {
            g.drawRect(X_LIFE_BAR, Y_LIFE_BAR, WIDTH_LIFE_BAR, HEIGHT_LIFE_BAR);
            g.setColor(Color.red);
            if (this.health >= 0.4 * this.getMaxHealth()) {
                g.fillRect(X_LIFE_BAR, Y_LIFE_BAR, (int) (WIDTH_LIFE_BAR * 0.4), HEIGHT_LIFE_BAR);
                g.setColor(Color.yellow);
                if (this.health >= 0.7 * this.getMaxHealth()) {
                    g.fillRect((int) (X_LIFE_BAR + WIDTH_LIFE_BAR * 0.4), Y_LIFE_BAR, (int) (WIDTH_LIFE_BAR * 0.3), HEIGHT_LIFE_BAR);

                    g.setColor(Color.green);
                    g.fillRect((int) (X_LIFE_BAR + WIDTH_LIFE_BAR * 0.7), Y_LIFE_BAR,
                            (int) (WIDTH_LIFE_BAR * this.health / this.getMaxHealth() - WIDTH_LIFE_BAR * 0.7), HEIGHT_LIFE_BAR);
                } else {
                    g.fillRect((int) (X_LIFE_BAR + WIDTH_LIFE_BAR * 0.4), Y_LIFE_BAR,
                            (int) (WIDTH_LIFE_BAR * this.health / this.getMaxHealth() - WIDTH_LIFE_BAR * 0.4), HEIGHT_LIFE_BAR);
                }
            } else {
                g.fillRect(X_LIFE_BAR, Y_LIFE_BAR,
                        WIDTH_LIFE_BAR * this.health / this.getMaxHealth(), HEIGHT_LIFE_BAR);
            }

        } else {
            g.drawRect(Game.WIDTH - X_LIFE_BAR - WIDTH_LIFE_BAR, Y_LIFE_BAR, WIDTH_LIFE_BAR, HEIGHT_LIFE_BAR);

            g.setColor(Color.red);
            if (this.health >= 0.4 * this.getMaxHealth()) {
                g.fillRect(Game.WIDTH - X_LIFE_BAR - WIDTH_LIFE_BAR, Y_LIFE_BAR, (int) (WIDTH_LIFE_BAR * 0.4), HEIGHT_LIFE_BAR);
                g.setColor(Color.yellow);
                if (this.health >= 0.7 * this.getMaxHealth()) {
                    g.fillRect((int) (Game.WIDTH - X_LIFE_BAR - WIDTH_LIFE_BAR + WIDTH_LIFE_BAR * 0.4), Y_LIFE_BAR, (int) (WIDTH_LIFE_BAR * 0.3), HEIGHT_LIFE_BAR);

                    g.setColor(Color.green);
                    g.fillRect((int) (Game.WIDTH - X_LIFE_BAR - WIDTH_LIFE_BAR + WIDTH_LIFE_BAR * 0.7), Y_LIFE_BAR,
                            (int) (WIDTH_LIFE_BAR * this.health / this.getMaxHealth() - WIDTH_LIFE_BAR * 0.7), HEIGHT_LIFE_BAR);
                } else {
                    g.fillRect((int) (Game.WIDTH - X_LIFE_BAR - WIDTH_LIFE_BAR + WIDTH_LIFE_BAR * 0.4), Y_LIFE_BAR,
                            (int) (WIDTH_LIFE_BAR * this.health / this.getMaxHealth() - WIDTH_LIFE_BAR * 0.4), HEIGHT_LIFE_BAR);
                }
            } else {
                g.fillRect(Game.WIDTH - X_LIFE_BAR - WIDTH_LIFE_BAR, Y_LIFE_BAR,
                        WIDTH_LIFE_BAR * this.health / this.getMaxHealth(), HEIGHT_LIFE_BAR);
            }
        }
    }

    public ImageIcon getImageIcon() {
        return this.img;
    }

    public void heal(int heal) {
        if (this.health + heal < this.maxHealth) {
            this.health += heal;
        } else {
            this.health = this.maxHealth;
        }
    }
}