package pauline.mygame;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class User {

    // game settings
    public static boolean useRandomColors = true;
    public static boolean touchToMove = true;
    public static int nbCellsX = 10;
    public static int nbCellsY = 20;

    // game statistics
    public static int bestScore = 0;
    public static TetrisMatrix currentGame;

    public User(){
        /*nbCellsX = 10;
        nbCellsY = 20;*/
        currentGame = new TetrisMatrix(nbCellsY, nbCellsX); // create the matrix after initializing the number of cells
    }

}