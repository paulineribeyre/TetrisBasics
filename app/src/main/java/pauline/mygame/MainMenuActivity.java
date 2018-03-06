package pauline.mygame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        new User(); // so that the constructor function is called
        loadGame();

        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        titleTextView.setTypeface(Typefaces.get(this, "blocked.ttf"));

        TextView subtitleTextView = (TextView) findViewById(R.id.subtitle_text_view);
        subtitleTextView.setTypeface(Typefaces.get(this, "ITCKRIST.TTF"));

        TextView bestscoreTextView = (TextView) findViewById(R.id.bestscore_text_view);
        //bestscoreTextView.setTypeface(Typefaces.get(this, "BAUHS93.TTF"));
        bestscoreTextView.setText("Best score: " + User.bestScore);

    }

    public void openGame(View view) {
        Intent intent = new Intent(MainMenuActivity.this, TetrisActivity.class);
        startActivity(intent);
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
