package pauline.mygame;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // set the fonts
        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        titleTextView.setTypeface(Typefaces.get(this, "blocked.ttf"));

        TextView subtitleTextView = (TextView) findViewById(R.id.subtitle_text_view);
        subtitleTextView.setTypeface(Typefaces.get(this, "ITCKRIST.TTF"));
    }

}