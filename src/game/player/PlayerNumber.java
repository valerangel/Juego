package game.player;

/**
 * Created by Angel on 19/06/2018.
 */
public enum PlayerNumber {

    PLAYER1, PLAYER2;

    public static int getNumber(PlayerNumber player) {
        if (player == PlayerNumber.PLAYER1) {
            return 1;
        }
        return 2;
    }
}
