package game;

import game.player.*;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Player player1;
        Player player2;

        int timeSleeping = 15;

        String[] pJAvailable = {"Planet", "Melee", "Range", "Poison", "Shotgun"};
        int select1 = JOptionPane.showOptionDialog(null, "PLAYER 1. SELECT CHARACTER",
                "Select", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, pJAvailable, pJAvailable[0]);
        int select2 = JOptionPane.showOptionDialog(null, "PLAYER 2. SELECT CHARACTER",
                "Select", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, pJAvailable, pJAvailable[0]);

        if (select1 == 0) {
            player1 = new Planet(Number_of_player.PLAYER1, null);
        } else if (select1 == 1) {
            player1 = new Melee(Number_of_player.PLAYER1, null);
        } else if (select1 == 2) {
            player1 = new Range(Number_of_player.PLAYER1, null);
        } else if(select1 == 3){
            player1 = new Poison(Number_of_player.PLAYER1, null);
        } else{
            player1 = new Shotgun(Number_of_player.PLAYER1, null);
        }

        if (select2 == 0) {
            player2 = new Planet(Number_of_player.PLAYER2, player1);
        } else if (select2 == 1) {
            player2 = new Melee(Number_of_player.PLAYER2, player1);
        } else if (select2 == 2) {
            player2 = new Range(Number_of_player.PLAYER2, player1);
        } else if(select2 == 3){
            player2 = new Poison(Number_of_player.PLAYER2, player1);
        } else{
            player2 = new Shotgun(Number_of_player.PLAYER2, player1);
        }

        player1.setEnemy(player2);


        JFrame frame = new JFrame("My game");
        Game game = new Game(player1, player2);
        frame.add(game);
        frame.setSize(Game.WIDTH, Game.HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        player1.setGame(game);
        player2.setGame(game);

        while (true) {
            game.move();
            game.repaint();
            Thread.sleep(timeSleeping);
        }
    }
}
