package game.player;

/**
 * Created by Angel on 19/06/2018.
 */
public enum Number_of_player {
    PLAYER1, PLAYER2;

    public static int getNumero(Number_of_player player) {
        if (player == Number_of_player.PLAYER1) return 1;
        else return 2;
    }
}
