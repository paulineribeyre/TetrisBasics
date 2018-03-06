package pauline.mygame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        titleTextView.setTypeface(Typefaces.get(this, "blocked.ttf"));

        TextView subtitleTextView = (TextView) findViewById(R.id.subtitle_text_view);
        subtitleTextView.setTypeface(Typefaces.get(this, "ITCKRIST.TTF"));
    }

    public void openGame(View view) {
        Intent intent = new Intent(MainMenuActivity.this, TetrisActivity.class);
        startActivity(intent);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

}
