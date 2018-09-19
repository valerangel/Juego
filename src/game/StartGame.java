package game;

import game.player.*;

import javax.swing.*;

public class StartGame {

    private final String titleOptionPanePlayer1 = "PLAYER 1. SELECT CHARACTER";
    private final String titleOptionPanePlayer2 = "PLAYER 2. SELECT CHARACTER";
    private final String[] pJAvailable = {"Planet", "Melee", "Range", "Poison", "Shotgun"};
    private Game game;
    private Player player1;
    private Player player2;

    public StartGame() throws InterruptedException {
        createPlayers();
        generateJFrame();
        addPlayersToGame();
        refreshGame();
    }

    private void createPlayers() {

        int select1 = showJOptionPane(titleOptionPanePlayer1);
        int select2 = showJOptionPane(titleOptionPanePlayer2);

        player1 = setPlayerType(select1, PlayerNumber.PLAYER1);
        player2 = setPlayerType(select2, PlayerNumber.PLAYER2);

        player1.setEnemy(player2);
        player2.setEnemy(player1);
    }

    private int showJOptionPane(String title) {
        return JOptionPane.showOptionDialog(null, title,
                "Select", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, pJAvailable, pJAvailable[0]);
    }

    private Player setPlayerType(int selectedPlayerType, PlayerNumber number_of_player) {

        Player player;

        if (selectedPlayerType == 0) {
            player = new Planet(number_of_player);
        } else if (selectedPlayerType == 1) {
            player = new Melee(number_of_player);
        } else if (selectedPlayerType == 2) {
            player = new Range(number_of_player);
        } else if(selectedPlayerType == 3){
            player = new Poison(number_of_player);
        } else{
            player = new Shotgun(number_of_player);
        }

        return player;
    }

    private void generateJFrame() {
        JFrame frame = new JFrame("My game");
        game = new Game(player1, player2);
        frame.add(game);
        frame.setSize(Game.WIDTH, Game.HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    private void addPlayersToGame() {
        player1.setGame(game);
        player2.setGame(game);
    }

    private void refreshGame() throws InterruptedException {
        int timeSleeping = 15;

        while (true) {
            game.move();
            game.repaint();
            Thread.sleep(timeSleeping);
        }
    }
}
