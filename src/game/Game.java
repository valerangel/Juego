package game;

import game.boost.Boost_Heal;
import game.player.Number_of_player;
import game.player.Player;
import game.boost.Boost;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//HOLAAAA
public class Game extends JPanel {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    public static final int TIME_FOR_BOOST = 600;

    private Player player1;
    private Player player2;
    private ArrayList<Boost> boosts;
    private int numBoost;
    private int countForBoost;


    public Game(Player player1, Player player2) {

        this.player1 = player1;
        this.player2 = player2;

        this.boosts = new ArrayList<Boost>(0);
        this.numBoost = 0;
        this.countForBoost = 0;


        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT ||
                        e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    player1.keyReleased(e);
                }
                if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_A ||
                        e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_D) {
                    player2.keyReleased(e);
                }

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT ||
                        e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    player1.keyPressed(e);
                }
                if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_A ||
                        e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_D) {
                    player2.keyPressed(e);
                }
            }
        });
        setFocusable(true);
    }

    public void move() {
        player1.move();
        player2.move();


        for (int i = 0; i < this.boosts.size(); i++) {
            if (this.boosts.get(i) != null) {
                this.boosts.get(i).move(player1, player2);
            }
        }

        if (countForBoost <= TIME_FOR_BOOST) {
            countForBoost++;
        } else {
            countForBoost = 0;
            this.createBoost();
        }


    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        player1.paint(g2d);
        player2.paint(g2d);
        this.paintBoosts(g2d);
    }

    public void gameOver(Number_of_player winner) {
        JOptionPane.showMessageDialog(this, "Game Over. El jugador " +
                Number_of_player.getNumero(winner) + " ha ganado.", "Game Over", JOptionPane.YES_NO_OPTION);
        System.exit(ABORT);
    }

    public void eliminateBoost(int index) {
        this.boosts.set(index, null);
    }

    private void createBoost() {
        numBoost = boosts.size();
        for (int i = 0; i < boosts.size(); i++) {
            if (boosts.get(i) == null) {
                numBoost = i;
                break;
            }
        }
        Boost newBoost;

        //Random entre los diferentes boost, no solo Boost_Heal
        newBoost = new Boost_Heal( numBoost, this);

        if (numBoost < boosts.size()) {
            boosts.set(numBoost, newBoost);
        } else {
            boosts.add(numBoost, newBoost);
        }
    }

    private void paintBoosts(Graphics2D g) {
        for (int i = 0; i < boosts.size(); i++) {
            if (boosts.get(i) != null) {
                boosts.get(i).paint(g, this);
            }
        }
    }

}