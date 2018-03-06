package pauline.mygame;

import android.app.Activity;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MyActivity extends Activity {

    private static String saveFileName = "gamesave";

    @Override
    public void onStop() {

        // Save the game
        saveGame();

        super.onStop();

    }

    public boolean saveGame() {

        boolean saved = false;

        Log.d("mydebug", "saveGame() - best score: " + User.bestScore);

        try {
            /*File saveFile = new File(saveFileName);
            if (!saveFile.exists())
                saveFile.createNewFile();*/

            FileOutputStream fos = new FileOutputStream(this.getExternalFilesDir(null) + "/" + saveFileName);
            //FileOutputStream fos = this.openFileOutput(this.getExternalFilesDir(null) + "/" + saveFileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = null;
            os = new ObjectOutputStream(fos);
            User user = new User();
            Log.d("mydebug", "saveGame() - best score: " + user.bestScore);
            os.writeObject(user);
            os.close();
            fos.close();
            saved = true;
        } catch (Exception e) {
            Log.d("mydebug", "Cannot save game - Caught Exception: " + e.getMessage());
        }


        try {
            // Creates a file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.
            File testFile = new File(this.getExternalFilesDir(null), "TestFile.txt");
            if (!testFile.exists())
                testFile.createNewFile();

            // Adds a line to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile)); //, true /*append*/));
            writer.write("User best score: " + User.bestScore + "\n");
            writer.close();
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(this,
                    new String[]{testFile.toString()},
                    null,
                    null);
        } catch (Exception e) {
            Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
        }



        return saved;

    }

    public boolean loadGame() {

        boolean loaded = false;

        File f = new File(this.getExternalFilesDir(null) + "/" + saveFileName);

        String textFromFile = "";
        if(f.exists() && !f.isDirectory()) {
            StringBuilder stringBuilder = new StringBuilder();
            // Reads the data from the file
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(f));
                String line;

                while ((line = reader.readLine()) != null) {
                    textFromFile += line.toString();
                    textFromFile += "\n";
                }
                reader.close();
            } catch (Exception e) {
                Log.e("ReadWriteFile", "Unable to read the TestFile.txt file.");
            }
            Log.d("mydebug", "exists: "+textFromFile);
        }
        else
            Log.d("mydebug", "no exists");

        /*File[] files = this.getFilesDir().listFiles();
        for (int i = 0; i < files.length; i++)
            Log.d("mydebug", ""+files[i].getAbsolutePath());*/

        FileInputStream fis = null;
        try {
            fis = new FileInputStream (new File(this.getExternalFilesDir(null) + "/" + saveFileName));
            //fis = this.openFileInput(this.getExternalFilesDir(null) + "/" + saveFileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            User user = (User) is.readObject();
            //Log.d("mydebug", is.readObject().toString());
            Log.d("mydebug", "loadGame() - best score: " + user.bestScore);
            is.close();
            fis.close();
            loaded = true;
        } catch (Exception e) {
            Log.d("mydebug", "Cannot load saved game - Caught Exception: " + e.getMessage());
        }

        Log.d("mydebug", "loadGame() - best score: " + User.bestScore);


        textFromFile = "";
// Gets the file from the primary external storage space of the
// current application.
        File testFile = new File(this.getExternalFilesDir(null), "TestFile.txt");
        if (testFile != null) {
            StringBuilder stringBuilder = new StringBuilder();
            // Reads the data from the file
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(testFile));
                String line;

                while ((line = reader.readLine()) != null) {
                    textFromFile += line.toString();
                    textFromFile += "\n";
                }
                reader.close();
            } catch (Exception e) {
                Log.e("ReadWriteFile", "Unable to read the TestFile.txt file.");
            }
        }
        Log.d("mydebug", textFromFile);



        return loaded;

    }

}