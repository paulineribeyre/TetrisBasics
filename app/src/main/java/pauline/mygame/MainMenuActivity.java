package pauline.mygame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainMenuActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // load the saved game
        loadGame();

        // set the fonts
        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        titleTextView.setTypeface(Typefaces.get(this, "blocked.ttf"));

        TextView subtitleTextView = (TextView) findViewById(R.id.subtitle_text_view);
        subtitleTextView.setTypeface(Typefaces.get(this, "ITCKRIST.TTF"));

    }

    // on click on the play button, the game activity is opened
    public void openGame(View view) {
        Intent intent = new Intent(MainMenuActivity.this, TetrisActivity.class);
        startActivity(intent);
    }

    // on click on the instructions button, a dialog describing the controls is displayed
    public void openInstructions(View view) {

        String instructions = "";
        if (User.touchToMove) // the controls depends on the settings chosen by the user
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

    // on click on the settings button, the settings activity is opened
    public void openSettings(View view) {
        Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    // on click on the exit button, the app closes
    public void closeGame(View view) {
        onStop();
        finish();
        System.exit(0);
    }

}
