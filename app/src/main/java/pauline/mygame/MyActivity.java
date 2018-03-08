package pauline.mygame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.*;

public class MyActivity extends Activity {

    private static String saveFileName = "gamesave";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStop() {

        // Save the current state of the game
        saveGame();

        super.onStop();

    }

    public boolean saveGame() {

        boolean saved = false;

        try {
            FileOutputStream fos = new FileOutputStream(this.getExternalFilesDir(null) + "/" + saveFileName);
            ObjectOutputStream os = null;
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

    public boolean loadGame() {

        boolean loaded = false;

        SerializableUser user = new SerializableUser();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream (new File(this.getExternalFilesDir(null) + "/" + saveFileName));
            ObjectInputStream is = new ObjectInputStream(fis);
            user = (SerializableUser) is.readObject();
            is.close();
            fis.close();
            loaded = true;
        } catch (Exception e) {
            Log.d("mydebug", "Cannot load saved game - Caught Exception: " + e.getMessage());
            new User(); // so that the constructor function is called --> new game
        }

        User.bestScore = user.bestScore;
        User.currentGame = user.currentGame;
        User.useRandomColors = user.useRandomColors;
        User.touchToMove = user.touchToMove;
        User.nbCellsX = user.nbCellsX;
        User.nbCellsY = user.nbCellsY;

        return loaded;

    }

}