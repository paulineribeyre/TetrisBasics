package pauline.mygame;

import android.os.Bundle;
import android.widget.TextView;

// TODO list
// music (+ add mute/volume in settings)
// noises
// google play
// strings in resource file (android.R...)
// ad after game over (AdMob)
// choose colors in settings

public class TetrisActivity extends MyActivity {

    private MatrixView matrixView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        // set the fonts
        TextView levelTextView = (TextView) findViewById(R.id.level_text_view);
        levelTextView.setTypeface(Typefaces.get(this, "BAUHS93.TTF"));

        TextView scoreTextView = (TextView) findViewById(R.id.score_text_view);
        scoreTextView.setTypeface(Typefaces.get(this,"BAUHS93.TTF"));

        TextView bestScoreTextView = (TextView) findViewById(R.id.best_score_text_view);
        bestScoreTextView.setTypeface(Typefaces.get(this,"BAUHS93.TTF"));

        // textViews in which the level and score will be displayed
        matrixView = (MatrixView) findViewById(R.id.matrix_view);
        matrixView.setLevelTextView(levelTextView);
        matrixView.setScoreTextView(scoreTextView);
        matrixView.setBestScoreTextView(bestScoreTextView);
    }

    @Override
    public void onPause() {
        matrixView.setPaused(true); // pause the current game
        super.onPause();
    }

    @Override
    public void onResume() {
        matrixView.setPaused(false); // start playing again
        matrixView.startGame();
        super.onResume();
    }

}
