package game;

import game.player.*;

import javax.swing.*;

public class StartGame {
    
    private Game game;

    public StartGame() throws InterruptedException {

        generateJFrame();
        refreshGame();
    }


    private void generateJFrame() {
        JFrame frame = new JFrame("My game");
        game = new Game();
        frame.add(game);
        frame.setSize(Game.WIDTH, Game.HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
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
