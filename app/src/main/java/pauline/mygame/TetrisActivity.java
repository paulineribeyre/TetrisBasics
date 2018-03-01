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
// photoshop: title i, play button
// game over: replay or main menu
// pause
// save best score
// save game
// save settings (color)
// music
// google play
// switch rotate/move commands in options
// settings erase best score
// settings default

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
