package pauline.mygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainMenuActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Load the saved game
        //new User(); // so that the constructor function is called
        loadGame();

        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        titleTextView.setTypeface(Typefaces.get(this, "blocked.ttf"));

        TextView subtitleTextView = (TextView) findViewById(R.id.subtitle_text_view);
        subtitleTextView.setTypeface(Typefaces.get(this, "ITCKRIST.TTF"));

    }

    public void openGame(View view) {
        Intent intent = new Intent(MainMenuActivity.this, TetrisActivity.class);
        startActivity(intent);
    }

    public void openInstructions(View view) {

        String instructions = "";
        if (User.touchToMove)
            instructions += "To move horizontally, tap on the left or right half of the screen.\nTo rotate a tetromino, drag your finger to the left or right of the screen.\n";
        else
            instructions += "To move horizontally, drag your finger to the left or right of the screen.\nTo rotate a tetromino, tap on the left or right half of the screen.\n";
        instructions += "To turn on fast speed, press the bottom of the screen.\nYou can pause the game by closing the application or by going back to the main menu.";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("How to play");
        builder.setMessage(instructions);
        builder.setPositiveButton("Understood",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void openSettings(View view) {
        Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void closeGame(View view) {
        onStop();
        finish();
        System.exit(0);
    }

}
