package game;

import game.player.*;

import javax.swing.*;

public class StartGame {


    private Game game;
    private Player player1;
    private Player player2;

    public StartGame() throws InterruptedException {

        generateJFrame();
        //addPlayersToGame();
        refreshGame();
    }


    private void generateJFrame() {
        JFrame frame = new JFrame("My game");
        //JMenuBar menuBar = createMenuBar();
        //frame.setJMenuBar(menuBar);
        game = new Game();
        frame.add(game);
        frame.setSize(Game.WIDTH, Game.HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuAbout = new JMenu("About");
        JMenuItem itemAbout = new JMenuItem("About");
        itemAbout.addActionListener(actionListener ->
                JOptionPane.showMessageDialog(null,
                        "About menu item clicked.",
                        "Title",
                        JOptionPane.INFORMATION_MESSAGE));

        menuAbout.add(itemAbout);
        menuBar.add(menuAbout);

        return menuBar;
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
