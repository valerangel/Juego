package game.boost;

import game.Game;
import game.player.Player;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Angel on 28/06/2018.
 */
public abstract class Boost {


    protected int x;
    protected int y;
    protected int maxTime;
    protected int time;
    protected int diameter;
    protected ImageIcon img;
    protected int index;

    protected Game game;


    public Boost(int x, int y, int diameter, int maxTime, int index, String resourcePath, Game game) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.maxTime = maxTime;
        this.game = game;
        this.index = index;
        this.img = new javax.swing.ImageIcon(getClass().getResource(resourcePath));
    }

    public void paint(Graphics2D g, Game game) {
        Image image = this.img.getImage();
        g.drawImage(image, x, y, diameter, diameter, game);
    }

    abstract public void colision(Player player);

    abstract public void move(Player player1, Player player2);

}
