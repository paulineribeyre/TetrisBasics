package pauline.mygame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// TODO list
// game over: replay or main menu (pop_up?)
// pause
// new game
// save best score
// save game
// music (+ add mute/volume in settings)
// noises
// google play
// settings switch rotate/move commands
// settings erase best score button
// settings matrix size
// settings default

public class TetrisActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        TextView pointsTextView = (TextView) findViewById(R.id.points_text_view);
        pointsTextView.setTypeface(Typefaces.get(this,"BAUHS93.TTF"));

        TextView levelTextView = (TextView) findViewById(R.id.level_text_view);
        levelTextView.setTypeface(Typefaces.get(this, "BAUHS93.TTF"));

        MatrixView matrixView = (MatrixView) findViewById(R.id.matrix_view);
        matrixView.setPointsTextView(pointsTextView);
        matrixView.setLevelTextView(levelTextView);
    }

}
