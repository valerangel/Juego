
package game;

import game.boost.Boost_Heal;
import game.boost.Boost_Mine;
import game.player.*;
import game.boost.Boost;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

public class Game extends JPanel {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    private static final int TIME_FOR_BOOST = 1000;

    private Player player1;
    private Player player2;
    private ArrayList<Boost> boosts;
    private int numBoost;
    private int countForBoost;
    public StatusGame statusGame;

    private int select1, select2;
    private boolean select1Done, select2Done;

    private final String[] pJAvailable = {"Planet", "Melee", "Range", "Poison", "Shotgun"};

    public Game(Player player1, Player player2) {
        this();
        this.player1 = player1;
        this.player2 = player2;

    }

    public Game() {
        this.statusGame = StatusGame.MENU_SELECTION;
        this.boosts = new ArrayList<>(0);
        this.numBoost = 0;
        this.countForBoost = 0;

        select1 = 0;
        select2 = 0;
        select1Done = false;
        select2Done = false;

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (player1Keys(e)) {
                    if (statusGame == StatusGame.ACTIVE) {
                        player1.keyReleased(e);
                    }
                    if (statusGame == StatusGame.MENU_SELECTION) {
                        changeSelectMenu(PlayerNumber.getNumber(PlayerNumber.PLAYER1), e);
                    }
                }
                if (player2Keys(e)) {
                    if (statusGame == StatusGame.ACTIVE) {
                        player2.keyReleased(e);
                    }
                    if (statusGame == StatusGame.MENU_SELECTION) {
                        changeSelectMenu(PlayerNumber.getNumber(PlayerNumber.PLAYER2), e);
                    }
                }

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (player1Keys(e)) {
                    if (statusGame == StatusGame.ACTIVE)
                        player1.keyPressed(e);
                }
                if (player2Keys(e)) {
                    if (statusGame == StatusGame.ACTIVE)
                        player2.keyPressed(e);
                }
            }
        });
        setFocusable(true);
    }

    private boolean player1Keys(KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_A ||
                e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_D ||
                e.getKeyCode() == KeyEvent.VK_F || e.getKeyCode() == KeyEvent.VK_G ||
                e.getKeyCode() == KeyEvent.VK_H || e.getKeyCode() == KeyEvent.VK_T;
    }

    private boolean player2Keys(KeyEvent e) {
        return ((e.getKeyCode() == KeyEvent.VK_LEFT) || (e.getKeyCode() == KeyEvent.VK_RIGHT) ||
                (e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_DOWN) ||
                (e.getKeyCode() == KeyEvent.VK_NUMPAD5) || (e.getKeyCode() == KeyEvent.VK_NUMPAD3) ||
                (e.getKeyCode() == KeyEvent.VK_NUMPAD2) || (e.getKeyCode() == KeyEvent.VK_NUMPAD1));
    }

    public void move() {
        if (this.statusGame == StatusGame.ACTIVE) {
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
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        this.paintBackground(g2d);

        if (this.statusGame == StatusGame.MENU_SELECTION) {
            paintMenu(g2d, PlayerNumber.getNumber(PlayerNumber.PLAYER1));
            paintMenu(g2d, PlayerNumber.getNumber(PlayerNumber.PLAYER2));
        } else {
            player1.paint(g2d);
            player2.paint(g2d);
            this.paintBoosts(g2d);
        }
    }

    private void paintMenu(Graphics2D g, int nPlayer) {
        int rows = HEIGHT / 13;
        int columns = WIDTH / 10;

        Font font = new Font("Serif", Font.PLAIN, 40);
        g.setFont(font);

        for (int i = 0; i < 5; i++) {
            g.setColor(Color.BLACK);
            g.fillRect(columns + 5 * columns * (nPlayer - 1), rows * (2 * i + 3),
                    3 * columns, rows);
            g.setColor(Color.WHITE);
            g.drawString(pJAvailable[i], columns + 5 * columns * (nPlayer - 1) + 90,
                    (int) (rows * (2 * i + 3) + 0.8 * rows));
        }

        g.setColor(Color.white);
        g.fillRect(columns + 5 * columns * (nPlayer - 1), rows, 3 * columns, rows);
        g.setColor(Color.red);
        g.drawString("PLAYER " + nPlayer, columns + 5 * columns * (nPlayer - 1) + 60,
                (int) (rows + 0.8 * rows));

        double thickness = 6;
        g.setStroke(new BasicStroke((float) thickness));
        g.drawRect(columns + 5 * columns * (nPlayer - 1),
                rows * (2 * select1 * (2 - nPlayer) + 2 * select2 * (nPlayer - 1) + 3), 3 * columns, rows);

        if ((select1Done && (2 - nPlayer) == 1) || (select2Done && (nPlayer - 1) == 1)) {
            thickness = 12;
            g.setColor(Color.yellow);
            g.setStroke(new BasicStroke((float) thickness));
            g.drawRect(columns + 5 * columns * (nPlayer - 1),
                    rows * (2 * select1 * (2 - nPlayer) + 2 * select2 * (nPlayer - 1) + 3), 3 * columns, rows);
        }
    }

    private void paintBackground(Graphics2D g2d) {
        ImageIcon img = new javax.swing.ImageIcon(getClass().getResource("/icon/Background.png"));
        Image image = img.getImage();
        g2d.drawImage(image, 0, 0, WIDTH, HEIGHT, this);
    }

    public void gameOver(PlayerNumber winner) {
        JOptionPane.showMessageDialog(this,
                "Game Over. El jugador " + PlayerNumber.getNumber(winner) + " ha ganado.",
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
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

        double random = Math.random();
        if (random < 0.3) {
            newBoost = new Boost_Heal(numBoost, this);
        } else {
            newBoost = new Boost_Mine(numBoost, this);
        }

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

    private void createPlayers() {
        player1 = setPlayerType(select1, PlayerNumber.PLAYER1);
        player2 = setPlayerType(select2, PlayerNumber.PLAYER2);

        player1.setEnemy(player2);
        player2.setEnemy(player1);
    }

    private void addPlayersToGame() {
        player1.setGame(this);
        player2.setGame(this);
    }

    private Player setPlayerType(int selectedPlayerType, PlayerNumber number_of_player) {

        Player player;

        if (selectedPlayerType == 0) {
            player = new Planet(number_of_player);
        } else if (selectedPlayerType == 1) {
            player = new Melee(number_of_player);
        } else if (selectedPlayerType == 2) {
            player = new Range(number_of_player);
        } else if (selectedPlayerType == 3) {
            player = new Poison(number_of_player);
        } else {
            player = new Shotgun(number_of_player);
        }

        return player;
    }

    private void changeSelectMenu(int number, KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            if (number == PlayerNumber.getNumber(PlayerNumber.PLAYER1)) {
                if (select1 > 0 && !select1Done) select1--;
            } else {
                if (select2 > 0 && !select2Done) select2--;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            if (number == PlayerNumber.getNumber(PlayerNumber.PLAYER1)) {
                if (select1 < 4 && !select1Done) select1++;
            } else {
                if (select2 < 4 && !select2Done) select2++;
            }
        }


        if (e.getKeyCode() == KeyEvent.VK_T || e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
            if (number == PlayerNumber.getNumber(PlayerNumber.PLAYER1)) {
                select1Done = true;
            } else {
                select2Done = true;
            }
            if (select1Done && select2Done) {
                this.createPlayers();
                this.addPlayersToGame();
                this.statusGame = StatusGame.ACTIVE;
            }
        }
    }


}