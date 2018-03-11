package pauline.mygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsActivity extends MyActivity {

    ToggleButton randomColorsToggleButton;
    ToggleButton switchCommandsToggleButton;
    EditText gameWidthEditText;
    EditText gamHeightEditText;

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
                User.useRandomColors = isChecked;
            }
        });

        switchCommandsToggleButton = (ToggleButton) findViewById(R.id.settings_switch_commands_toggleButton);
        switchCommandsToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                User.touchToMove = !isChecked;
            }
        });

        gameWidthEditText = (EditText) findViewById(R.id.settings_game_width_editText);
        gameWidthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    User.nbCellsX = Integer.parseInt(s.toString());
                }
                catch (Exception e) {}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        gamHeightEditText = (EditText) findViewById(R.id.settings_game_height_editText);
        gamHeightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    User.nbCellsY = Integer.parseInt(s.toString());
                }
                catch (Exception e) {}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        copyUserSettings();
    }

    private void copyUserSettings() {
        randomColorsToggleButton.setChecked(User.useRandomColors);
        switchCommandsToggleButton.setChecked(!User.touchToMove);
        gameWidthEditText.setText(""+User.nbCellsX);
        gamHeightEditText.setText(""+User.nbCellsY);
    }

    public void startNewGame(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("");
        builder.setMessage("Do you really want to reset the current game?");
        builder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User.startNewGame();
                        saveGame();
                        Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void eraseBestScore(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("");
        builder.setMessage("Do you really want to reset your best score?");
        builder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User.bestScore = 0;
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void setDefaultSettings(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("");
        builder.setMessage("Do you really want to reset the settings?");
        builder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User.useRandomColors = true;
                        User.touchToMove = true;
                        User.nbCellsX = 10;
                        User.nbCellsY = 20;
                        copyUserSettings();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void openAbout(View view) {
        Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
        startActivity(intent);
    }

}