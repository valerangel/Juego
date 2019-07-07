
package game;

import game.boost.Boost;
import game.boost.Boost_Heal;
import game.boost.Boost_Mine;
import game.player.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.io.IOException;

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
    private int confNum1, confNum2;
    private boolean configuration1, configuration2;

    private int p1MoveUp, p1MoveDown, p1MoveLeft, p1MoveRight,
            p1AttackUp, p1AttackDown, p1AttackLeft, p1AttackRight;
    private int p2MoveUp, p2MoveDown, p2MoveLeft, p2MoveRight,
            p2AttackUp, p2AttackDown, p2AttackLeft, p2AttackRight;


    private final String[] pJAvailable = {"Planet", "Melee", "Range", "Poison", "Shotgun"};
    private final String[] controls = {"Move UP", "Move Down", "Move Left", "Move Right",
                                        "Attack UP","Attack Down", "Attack Left", "Attack Right"};

    public Game(Player player1, Player player2) {
        this();
        this.player1 = player1;
        this.player2 = player2;

    }

    public Game() {
        this.resetGame();

        this.music();

        p1MoveUp = KeyEvent.VK_W; p1MoveDown = KeyEvent.VK_S;
        p1MoveLeft = KeyEvent.VK_A; p1MoveRight = KeyEvent.VK_D;
        p1AttackUp = KeyEvent.VK_T; p1AttackDown = KeyEvent.VK_G;
        p1AttackLeft = KeyEvent.VK_F; p1AttackRight = KeyEvent.VK_H;
        p2MoveUp = KeyEvent.VK_UP; p2MoveDown = KeyEvent.VK_DOWN;
        p2MoveLeft = KeyEvent.VK_LEFT; p2MoveRight = KeyEvent.VK_RIGHT;
        p2AttackUp = KeyEvent.VK_O; p2AttackDown = KeyEvent.VK_L;
        p2AttackLeft = KeyEvent.VK_K; p2AttackRight = 0; //It's the Keycode of Ñ

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
        return e.getKeyCode() == p1MoveUp || e.getKeyCode() == p1MoveDown ||
                e.getKeyCode() == p1MoveLeft || e.getKeyCode() == p1MoveRight ||
                e.getKeyCode() == p1AttackUp || e.getKeyCode() == p1AttackDown ||
                e.getKeyCode() == p1AttackLeft || e.getKeyCode() == p1AttackRight;
    }

    private boolean player2Keys(KeyEvent e) {
        return  e.getKeyCode() == p2MoveUp || e.getKeyCode() == p2MoveDown ||
                e.getKeyCode() == p2MoveLeft || e.getKeyCode() == p2MoveRight ||
                e.getKeyCode() == p2AttackUp || e.getKeyCode() == p2AttackDown ||
                e.getKeyCode() == p2AttackLeft || e.getKeyCode() == p2AttackRight;
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
        int rows = HEIGHT / 105;
        int columns = WIDTH / 10;




        boolean conf;
        if(nPlayer == 1) conf = configuration1;
        else conf = configuration2;

        if(!conf) {
            Font font = new Font("Serif", Font.PLAIN, 32);
            g.setFont(font);
            //this for draws rectangles with the different classes for each pj
            for (int i = 0; i < 5; i++) {
                g.setColor(Color.BLACK);
                g.fillRect(columns + 5 * columns * (nPlayer - 1), 7 * rows * (2 * i + 3),
                        3 * columns, 7 * rows);
                g.setColor(Color.WHITE);
                g.drawString(pJAvailable[i], columns + 5 * columns * (nPlayer - 1) + 100,
                        (int) (7 * rows * (2 * i + 3) + 0.8 * 7 * rows));
            }

            //Here we draw the rectangle with the name of the Player.
            g.setColor(Color.white);
            g.fillRect(columns + 5 * columns * (nPlayer - 1), 7 * rows, 3 * columns, 7 * rows);
            g.setColor(Color.red);
            g.drawString("PLAYER " + nPlayer, columns + 5 * columns * (nPlayer - 1) + 75,
                    (int) (7 * rows + 0.8 * 7 * rows));

            //Here we draw the selection.
            double thickness = 6;
            g.setStroke(new BasicStroke((float) thickness));
            g.drawRect(columns + 5 * columns * (nPlayer - 1),
                    7 * rows * (2 * select1 * (2 - nPlayer) + 2 * select2 * (nPlayer - 1) + 3),
                    3 * columns, 7 * rows);

            g.drawRect(columns + 5 * columns * (nPlayer - 1),
                    7 * rows * (2 * select1 * (2 - nPlayer) + 2 * select2 * (nPlayer - 1) + 3),
                    3 * columns, 7 * rows);

            //We draw the option button.
            g.setColor(Color.YELLOW);
            g.fillRect(columns + 5 * columns * (nPlayer - 1), 7 * rows * (13),
                    3 * columns, 7 * rows);
            g.setColor(Color.BLACK);
            g.drawString("Configuration", columns + 5 * columns * (nPlayer - 1) + 60,
                    (int) (7 * rows * (13) + 0.8 * 7 * rows));


        } else{
            Font font = new Font("Serif", Font.PLAIN, 24);
            g.setFont(font);
            //this for draws rectangles with the different classes for each pj
            for (int i = 0; i < 8; i++) {
                g.setColor(Color.BLACK);
                g.fillRect(columns + 5 * columns * (nPlayer - 1), 5 * rows * (2 * i + 3),
                        3 * columns, 5 * rows);
                g.setColor(Color.WHITE);
                g.drawString(controls[i], columns + 5 * columns * (nPlayer - 1) + 100,
                        (int) (5 * rows * (2 * i + 3) + 0.8 * 5 * rows));
            }

            //Here we draw the rectangle with the name of the Player.
            g.setColor(Color.white);
            g.fillRect(columns + 5 * columns * (nPlayer - 1), 5 * rows, 3 * columns, 5 * rows);
            g.setColor(Color.red);
            g.drawString("PLAYER " + nPlayer, columns + 5 * columns * (nPlayer - 1) + 75,
                    (int) (5 * rows + 0.8 * 5 * rows));

            //Here we draw the selection.
            double thickness = 6;
            g.setStroke(new BasicStroke((float) thickness));
            g.drawRect(columns + 5 * columns * (nPlayer - 1),
                    5 * rows * (2 * confNum1 * (2 - nPlayer) + 2 * confNum2 * (nPlayer - 1) + 3),
                    3 * columns, 5 * rows);

            g.drawRect(columns + 5 * columns * (nPlayer - 1),
                    5 * rows * (2 * confNum1 * (2 - nPlayer) + 2 * confNum2 * (nPlayer - 1) + 3),
                    3 * columns, 5* rows);

            //We draw the option button.
            g.setColor(Color.YELLOW);
            g.fillRect(columns + 5 * columns * (nPlayer - 1), 5 * rows * (19),
                    3 * columns, 5 * rows);
            g.setColor(Color.BLACK);
            g.drawString("Return to selection", columns + 5 * columns * (nPlayer - 1) + 60,
                    (int) (5 * rows * (19) + 0.8 * 5 * rows));
            }

        //If both select a player, we begin
        if ((select1Done && (2 - nPlayer) == 1) || (select2Done && (nPlayer - 1) == 1)) {
            int thickness = 12;
            g.setColor(Color.yellow);
            g.setStroke(new BasicStroke((float) thickness));
            g.drawRect(columns + 5 * columns * (nPlayer - 1),
                    7 * rows * (2 * select1 * (2 - nPlayer) + 2 * select2 * (nPlayer - 1) + 3),
                    3 * columns, 7 * rows);
        }


    }

    private void paintBackground(Graphics2D g2d) {
        ImageIcon img = new javax.swing.ImageIcon(getClass().getResource("/icon/Background.png"));
        Image image = img.getImage();
        g2d.drawImage(image, 0, 0, WIDTH, HEIGHT, this);
    }

    public void gameOver(PlayerNumber winner) {
        this.sound("sound/gameOver.wav");
        JOptionPane.showMessageDialog(this,
                "Game Over. El jugador " + PlayerNumber.getNumber(winner) + " ha ganado.",
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
        this.resetGame();
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
        if (this.isMoveUp(e)) {
            if (number == PlayerNumber.getNumber(PlayerNumber.PLAYER1)) {
                if(!configuration1) {
                    if (select1 > 0 && !select1Done) {
                        select1--;
                    }
                } else{
                    if (confNum1 > 0) {
                        confNum1--;
                    }
                }
            } else {
                if(!configuration2) {
                    if (select2 > 0 && !select2Done) {
                        select2--;
                    }
                } else{
                    if (confNum2 > 0) {
                        confNum2--;
                    }
                }
            }
        }

        if (isMoveDown(e)) {
            if (number == PlayerNumber.getNumber(PlayerNumber.PLAYER1)) {
                if(!configuration1) {
                    if (select1 < 5 && !select1Done) {
                        select1++;
                    }
                } else{
                    if(confNum1 < 8){
                        confNum1++;
                    }
                }
            } else {
                if(!configuration2) {
                    if (select2 < 5 && !select2Done) {
                        select2++;
                    }
                } else{
                    if(confNum2 < 8){
                        confNum2++;
                    }
                }
            }
        }

        if (isAttackUp(e)) {
            if (number == PlayerNumber.getNumber(PlayerNumber.PLAYER1)) {
                if(!configuration1){
                    if(select1 < 5) {
                        select1Done = true;
                    }else{
                        configuration1 = true;
                    }
                } else{
                    if(confNum1 < 8) {
                        configureKey(number, confNum1);
                    }else{
                        configuration1 = false;
                    }
                }
            } else {
                if(!configuration2) {
                    if (select2 < 5) {
                        select2Done = true;
                    } else {
                        configuration2 = true;
                    }
                } else{
                    if (confNum2 < 8) {
                        configureKey(number, confNum2);
                    } else {
                        configuration2 = false;
                    }
                }
            }

            if (select1Done && select2Done) {
                this.createPlayers();
                this.addPlayersToGame();
                this.statusGame = StatusGame.ACTIVE;
            }
        }

    }

    private void resetGame(){
        this.statusGame = StatusGame.MENU_SELECTION;
        this.boosts = new ArrayList<>(0);
        this.numBoost = 0;
        this.countForBoost = 0;

        select1 = 0;
        select2 = 0;
        select1Done = false;
        select2Done = false;
        confNum1 = 8;
        confNum2 = 8;
        configuration1 = false;
        configuration2 = false;

    }

    private void configureKey (int number, int confNum){
        String text;
        do {
            text = JOptionPane.showInputDialog("Type the letter");
        } while(text.isEmpty());
        System.out.println(text);
        char letter = text.charAt(0);
        System.out.println(letter);
        int key = KeyEvent.getExtendedKeyCodeForChar(letter);
        System.out.println(key);

        if(number == 1){
            if(confNum == 0){
                p1MoveUp = key;
            } else if(confNum == 1){
                p1MoveDown = key;
            }else if(confNum == 2){
                p1MoveLeft = key;
            }else if(confNum == 3){
                p1MoveRight = key;
            }else if(confNum == 4){
                p1AttackUp = key;
            }else if(confNum == 5){
                p1AttackDown = key;
            }else if(confNum == 6){
                p1AttackLeft = key;
            }else{
                p1AttackRight = key;
            }
        } else{
            if(confNum == 0){
                p2MoveUp = key;
            } else if(confNum == 1){
                p2MoveDown = key;
            }else if(confNum == 2){
                p2MoveLeft = key;
            }else if(confNum == 3){
                p2MoveRight = key;
            }else if(confNum == 4){
                p2AttackUp = key;
            }else if(confNum == 5){
                p2AttackDown = key;
            }else if(confNum == 6){
                p2AttackLeft = key;
            }else{
                p2AttackRight = key;
            }

        }
    }

    public boolean isMoveUp( KeyEvent e){
        return (e.getKeyCode() == p1MoveUp || e.getKeyCode() == p2MoveUp);
    }

    public boolean isMoveDown( KeyEvent e){
        return (e.getKeyCode() == p1MoveDown || e.getKeyCode() == p2MoveDown);
    }

    public boolean isMoveLeft( KeyEvent e){
        return (e.getKeyCode() == p1MoveLeft || e.getKeyCode() == p2MoveLeft);
    }

    public boolean isMoveRight( KeyEvent e){
        return (e.getKeyCode() == p1MoveRight || e.getKeyCode() == p2MoveRight);
    }

    public boolean isAttackUp( KeyEvent e){
        return (e.getKeyCode() == p1AttackUp || e.getKeyCode() == p2AttackUp);
    }

    public boolean isAttackDown( KeyEvent e){
        return (e.getKeyCode() == p1AttackDown || e.getKeyCode() == p2AttackDown);
    }

    public boolean isAttackLeft( KeyEvent e){
        return (e.getKeyCode() == p1AttackLeft || e.getKeyCode() == p2AttackLeft);
    }

    public boolean isAttackRight( KeyEvent e){
        return (e.getKeyCode() == p1AttackRight || e.getKeyCode() == p2AttackRight);
    }

    /**
     * Esta función se encarga de poner múscia.
     */
    private static void music() {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("sound/mainSong.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException uae) {
            System.out.println(uae);
        }
    }

    public static void sound(String path) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException uae) {
            System.out.println(uae);
        }
    }



}
