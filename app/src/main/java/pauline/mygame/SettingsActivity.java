package pauline.mygame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsActivity extends MyActivity {

    ToggleButton randomColorsToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        titleTextView.setTypeface(Typefaces.get(this, "blocked.ttf"));

        TextView subtitleTextView = (TextView) findViewById(R.id.subtitle_text_view);
        subtitleTextView.setTypeface(Typefaces.get(this, "ITCKRIST.TTF"));

        randomColorsToggleButton = (ToggleButton) findViewById(R.id.settings_random_colors_toggleButton);
        randomColorsToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    User.useRandomColors = true;
                } else {
                    User.useRandomColors = false;
                }
            }
        });

        copyUserSettings();
    }

    private void copyUserSettings() {
        randomColorsToggleButton.setChecked(User.useRandomColors);
        // TODO
    }

    public void startNewGame(View view) {
        User.startNewGame();
    }

    public void eraseBestScore(View view) {
        User.bestScore = 0;
    }

    public void setDefaultSettings(View view) {
        User.useRandomColors = true;
        User.touchToMove = true;
        User.nbCellsX = 10;
        User.nbCellsY = 20;
        copyUserSettings();
    }

}