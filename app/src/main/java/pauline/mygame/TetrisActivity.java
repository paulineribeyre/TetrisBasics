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
// music (+ add mute/volume in settings)
// noises
// google play
// settings switch rotate/move commands
// keep moving when hold left or right
// strings in resource file (android.R...)
// add after game over (AdMob)
// comment

public class TetrisActivity extends MyActivity {

    private MatrixView matrixView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        TextView levelTextView = (TextView) findViewById(R.id.level_text_view);
        levelTextView.setTypeface(Typefaces.get(this, "BAUHS93.TTF"));

        TextView scoreTextView = (TextView) findViewById(R.id.score_text_view);
        scoreTextView.setTypeface(Typefaces.get(this,"BAUHS93.TTF"));

        TextView bestScoreTextView = (TextView) findViewById(R.id.best_score_text_view);
        bestScoreTextView.setTypeface(Typefaces.get(this,"BAUHS93.TTF"));

        matrixView = (MatrixView) findViewById(R.id.matrix_view);
        matrixView.setLevelTextView(levelTextView);
        matrixView.setScoreTextView(scoreTextView);
        matrixView.setBestScoreTextView(bestScoreTextView);
    }

    @Override
    public void onPause() {
        matrixView.setPaused(true);
        super.onPause();
    }

    @Override
    public void onResume() {
        matrixView.setPaused(false);
        matrixView.startGame();
        super.onResume();
    }

}
