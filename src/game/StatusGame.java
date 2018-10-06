package game;

import org.jetbrains.annotations.Contract;

/**
 * Created by Angel on 06/10/2018.
 */
public enum StatusGame {
    ACTIVE(0), MENU_SELECTION(1);

    private int number;

     StatusGame(int number){
         number = this.number;

    }

    public int getNumber(){
        return this.number;
    }

}
