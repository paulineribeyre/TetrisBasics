package pauline.mygame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.*;

public class MyActivity extends Activity {

    private static String saveFileName = "gamesave"; // file in which the current states of the game and settings are saved

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    // every time the app or an activity is closed, the current game is saved
    @Override
    public void onStop() {

        // Save the current state of the game
        saveGame();

        super.onStop();

    }

    // serializes the current states of the game and the settings and saves them in a file
    public boolean saveGame() {

        boolean saved = false;

        try {
            FileOutputStream fos = new FileOutputStream(this.getExternalFilesDir(null) + "/" + saveFileName);
            ObjectOutputStream os;
            os = new ObjectOutputStream(fos);
            SerializableUser user = new SerializableUser();
            os.writeObject(user);
            os.close();
            fos.close();
            saved = true;

        } catch (Exception e) {
            Log.d("mydebug", "Cannot save game - Caught Exception: " + e.getMessage());
        }

        return saved;

    }

    // reads the file containing the serialized current states of the game and the settings
    public boolean loadGame() {

        boolean loaded = false;

        SerializableUser user;
        FileInputStream fis;
        try {
            fis = new FileInputStream (new File(this.getExternalFilesDir(null) + "/" + saveFileName));
            ObjectInputStream is = new ObjectInputStream(fis);
            user = (SerializableUser) is.readObject();
            is.close();
            fis.close();
            loaded = true;

            // copy the previously serialized information to the current user
            User.bestScore = user.bestScore;
            User.currentGame = user.currentGame;
            User.useRandomColors = user.useRandomColors;
            User.touchToMove = user.touchToMove;
            User.nbCellsX = user.nbCellsX;
            User.nbCellsY = user.nbCellsY;
            User.levelHandler = user.levelHandler;

        } catch (Exception e) {
            Log.d("mydebug", "Cannot load saved game - Caught Exception: " + e.getMessage());
            new User(); // so that the constructor function is called --> new game
        }

        return loaded;

    }

}