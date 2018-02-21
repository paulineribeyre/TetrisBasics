package pauline.mygame;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// TODO list
// display next shape
// game over: replay or main menu
// replace tiles with pictures
// pause
// save best score
// save game
// options (color) and save
// music
// google play
// app icon
// app name

public class TetrisActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        TextView levelTextView = (TextView) findViewById(R.id.level_text_view);
        MatrixView matrixView = (MatrixView) findViewById(R.id.matrix_view);
        matrixView.setLevelTextView(levelTextView);
    }

}
