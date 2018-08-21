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
    protected double health;
    protected double maxHealth;
    protected double damage;
    protected double speed;
    protected Number_of_player numPlayer;
    protected Player enemy;
    protected Game game;
    protected ImageIcon img;
    protected boolean invulnerable;
    protected int contInvulnerable;
    protected int poisoned;

    protected double x;
    protected double y;
    protected double speedXPos;
    protected double speedYPos;
    protected double speedXNeg;
    protected double speedYNeg;

    private static final int X_LIFE_BAR = 40;
    private static final int Y_LIFE_BAR = 30;
    private static final int WIDTH_LIFE_BAR = 150;
    private static final int HEIGHT_LIFE_BAR = 15;
    private static final int TIME_INVULNERABLE = 50;


    public Player(double health, double damage, double speed, Game game, Number_of_player numPlayer, Player enemy, int diameter) {
        this.health = health;
        this.maxHealth = this.health;
        this.damage = damage;
        this.speed = speed;
        this.numPlayer = numPlayer;
        this.game = game;
        this.diameter = diameter;
        this.enemy = enemy;
        this.invulnerable = false;
        this.contInvulnerable = 0;
        this.poisoned = 0;

        //Posición inicial
        if (this.numPlayer == Number_of_player.PLAYER1) {
            x = 50;
            y = 200;
        } else {
            x = 900;
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

        boolean diag = false;
        if ((speedXPos - speedXNeg) * (speedYPos - speedYNeg) != 0) diag = true;

        if (diag) {
            x = (int) (x + (speedXPos - speedXNeg) * 0.71);
            y = (int) (y + (speedYPos - speedYNeg) * 0.71);
        } else {
            x = x + speedXPos - speedXNeg;
            y = y + speedYPos - speedYNeg;
        }

        if(poisoned>0){
            poisoned--;
            this.dealDamage(PoisonsPoison.DAMAGE_POISON);
        }

        if(invulnerable){
            contInvulnerable++;
        }
        if(contInvulnerable > TIME_INVULNERABLE){
            contInvulnerable = 0;
            this.invulnerable = false;
        }

        if (this.health <= 0) game.gameOver(this.enemy.getNumPlayer());
    }


    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }

    abstract public void paint(Graphics2D g);

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, diameter, diameter);
    }

    public Rectangle[] getBoundsArr() {
        Rectangle[] rectangles = new Rectangle[4];
        rectangles[0] = new Rectangle((int)x,(int) y + diameter / 3, diameter, diameter / 3);
        rectangles[1] = new Rectangle((int)x + diameter / 12,(int) y + diameter / 6,
                5 * diameter / 6, 2 * diameter / 3);
        rectangles[2] = new Rectangle((int)x + diameter / 6, (int)y + diameter / 12,
                2 * diameter / 3, 5 * diameter / 6);
        rectangles[3] = new Rectangle((int)x + diameter / 3, (int)y, diameter / 3, diameter);

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

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getDiameter() {
        return this.diameter;
    }


    public void dealDamage(double damage) {
        if (!invulnerable)
            this.health -= damage;
    }

    public void dealDamageInv(int damage) {
        if (!invulnerable) {
            this.health -= damage;
            invulnerable = true;
        }
    }

    public Number_of_player getNumPlayer() {
        return this.numPlayer;
    }

    public double getMaxHealth() {
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
                        (int)(WIDTH_LIFE_BAR * this.health / (this.getMaxHealth())), HEIGHT_LIFE_BAR);
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
                        (int)(WIDTH_LIFE_BAR * this.health / this.getMaxHealth()), HEIGHT_LIFE_BAR);
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


    public void poisonPlayer(){
        this.poisoned = PoisonsPoison.POISON_TIME;
    }
}
