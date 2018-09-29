package game;

import javax.swing.*;

public class Main {

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new StartGame();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
