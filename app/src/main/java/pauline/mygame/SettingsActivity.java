package pauline.mygame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ToggleButton toggle = (ToggleButton) findViewById(R.id.settings_random_colors_toggleButton);
        toggle.setChecked(User.useRandomColors);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    User.useRandomColors = true;
                } else {
                    User.useRandomColors = false;
                }
            }
        });
    }

}