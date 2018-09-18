package game;

public class Main {

    public static void main(String[] args){
        try {
            new StartGame();
        } catch (InterruptedException e) {
            System.exit(0);
        }
    }

}
