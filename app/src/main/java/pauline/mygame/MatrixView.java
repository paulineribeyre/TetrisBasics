package pauline.mygame;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

public class MatrixView extends View {

    private int screenWidth;
    private int screenHeight;
    private int viewWidth;
    private int viewHeight;

    private TetrisMatrix matrix;
    //private int matrixWidth, matrixHeight;

    private int[] colorArray; // one color for each Tetris shape
    private int sizeCellX, sizeCellY;
    //private Bitmap[] cellArray;

    private TextView levelTextView;
    private TextView scoreTextView;
    private TextView bestScoreTextView;

    private boolean paused = false;

    //private int moveDelay = 300; //1200;
    private LevelHandler levelHandler;
    private RefreshHandler refreshHandler = new RefreshHandler();
    private boolean firstTimeGameStarts = true;

    float initialX = 0; // X position of finger on initial touch
    float initialY = 0; // Y position of finger on initial touch
    private final int movementSensibilityX = 30;
    //private final int touchDelay = 20;

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //startGame() is called automatically by onResume()
    }

    public void startGame() {
        matrix = User.currentGame; // new TetrisMatrix();
        //levelHandler = new LevelHandler();
        levelHandler = User.levelHandler;
        if (colorArray == null)
            initColorArray();
        //if (firstTimeGameStarts){
        refreshHandler.sendEmptyMessage(1); // start the game
        //    firstTimeGameStarts = false;
        //}
    }

    private void startNewGame() {
        User.startNewGame();
        startGame();
    }

    public int getRandomColor(){

        Random rnd = new Random();
        int r, g, b;
        double luminance;

        do {
            r = rnd.nextInt(256);
            g = rnd.nextInt(256);
            b = rnd.nextInt(256);
            /*if (r > 150)
                g = rnd.nextInt(150);
            else
                g = rnd.nextInt(256);
            if (g > 150)
                b = rnd.nextInt(150);
            else
                b = rnd.nextInt(256);*/
            luminance = r * 0.2126 + g * 0.7152 + b * 0.0722;
        } while (luminance < 80);

        return Color.argb(255, r, g, b);

    }

    public void initColorArray() {
        colorArray = new int[8];
        colorArray[0] = Color.WHITE; // Game matrix background
        for (int i = 1; i < 8; i++) {
            if (User.useRandomColors)
                colorArray[i] = getRandomColor();
            else
                colorArray[i] = new TetrisPiece(i).getColor();
        }
    }

    private void calculateCellSize() {
        sizeCellX = (int)Math.floor(viewWidth / (matrix.getNbCellsX() + 3));
        sizeCellY = (int)Math.floor(viewHeight / matrix.getNbCellsY());
        sizeCellX = sizeCellY = Math.min(sizeCellX, sizeCellY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((TetrisActivity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        viewWidth = w;
        viewHeight = h;
        calculateCellSize();
        //matrixWidth = sizeCellX * matrix.getNbCellsX();
        //matrixHeight = sizeCellY * matrix.getNbCellsY();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // color of the empty cells
        Paint myPaint = new Paint();
        myPaint.setColor(colorArray[0]);

        // color and picture of the cells containing a Tetris piece
        int type;
        Drawable cell = getResources().getDrawable(R.drawable.tetris_piece); // use drawable image instead of rectangles

        int yOrigin = viewHeight - sizeCellY * matrix.getNbCellsY();

        // draw the game matrix
        for (int y = 0; y <  matrix.getNbCellsY(); y ++) {
            for (int x = 0; x <  matrix.getNbCellsX(); x ++) {

                type = matrix.getArray()[y][x];

                // empty cell
                if (type == 0)
                    canvas.drawRect(x * sizeCellX, yOrigin + y * sizeCellY,
                            (x + 1) * sizeCellX, yOrigin + (y + 1) * sizeCellY,
                            myPaint);

                // cell containing a Tetris piece
                else {
                    cell.setColorFilter(colorArray[type], PorterDuff.Mode.MULTIPLY);
                    cell.setBounds(x * sizeCellX, yOrigin + y * sizeCellY,
                            (x + 1) * sizeCellX, yOrigin + (y + 1) * sizeCellY);
                    cell.draw(canvas);
                }

            }
        }

        // draw the next piece
        int x0 = matrix.getNbCellsX() + 1;
        int y0 = 5;
        type = matrix.getNextPiece().getType();
        for (int y = 0; y < matrix.getNextPiece().getHeight(); y++) {
            for (int x = 0; x < matrix.getNextPiece().getWidth(); x++) {
                if (matrix.getNextPiece().getShape()[y][x] == 1) {
                    /*canvas.drawRect(x0 * sizeCellX + x * sizeCellX / 2, y0 * sizeCellY + y * sizeCellY / 2,
                            x0 * sizeCellX + (x + 1) * sizeCellX / 2, y0 * sizeCellY + (y + 1) * sizeCellY / 2,
                            myPaint);*/
                    cell.setColorFilter(colorArray[type], PorterDuff.Mode.MULTIPLY);
                    cell.setBounds(x0 * sizeCellX + x * sizeCellX / 2, y0 * sizeCellY + y * sizeCellY / 2,
                            x0 * sizeCellX + (x + 1) * sizeCellX / 2, y0 * sizeCellY + (y + 1) * sizeCellY / 2);
                    cell.draw(canvas);
                }
            }
        }

        //TextView myTextView = (TextView) this.getParent().findViewById(R.id.level_text_view);
        //myTextView.setText("Level " + levelHandler.getLevel());
        if (levelTextView != null) {
            levelTextView.setText("LEVEL " + levelHandler.getLevel());
            scoreTextView.setText("SCORE: " + levelHandler.getScore());
            bestScoreTextView.setText("BEST SCORE:\n" + User.bestScore);
        }

    }

    private void moveGame() {
        if (!paused) {
            if (matrix.movePiece(TetrisPiece.DIRECTION.DOWN)) {
                //refreshHandler.removeMessages(0);
                //levelHandler.dropNormalSpeed();
                refreshHandler.sendEmptyMessageDelayed(0, levelHandler.getMoveDelay());
            } else { // the current piece cannot drop anymore
                //matrix.addNewPiece(1);
                refreshHandler.sendEmptyMessageDelayed(1, levelHandler.getMoveDelay());
            }
        }
    }

    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("mydebug", "RefreshHandler msg "+msg.what);

            if (msg.what == 1) { // new game, or the current piece cannot drop anymore
                if (matrix.getCurrentPiece() != null && matrix.movePiece(TetrisPiece.DIRECTION.DOWN)) { // case when the piece is moved after colliding
                    //refreshHandler.removeMessages(0);
                    refreshHandler.sendEmptyMessageDelayed(0, levelHandler.getMoveDelay());
                }
                else {
                    int nbOfClearedRows = matrix.clearRows(); // check if some rows are complete and clear them
                    if (nbOfClearedRows != 0) levelHandler.addPoints(nbOfClearedRows);

                    if (matrix.addNewPiece()) {
                        //MatrixView.this.invalidate(); // redraw
                        //this.removeMessages(0);
                        sendEmptyMessageDelayed(0, levelHandler.getMoveDelay());
                    }
                    else {
                        Log.d("mydebug", "Game over");
                        //User.bestScore = levelHandler.getScore();
                        startNewGame();
                    }
                }
            }

            else if (msg.what == 0) {
                moveGame(); //Log.d("mydebug", "MatrixView.RefreshHandler moving");
                //MatrixView.this.invalidate(); // redraw
            }

            // TODO explicit msg.what values
            //else if (msg.what == 2) { // stop moving
                // stop moving
            //}

            MatrixView.this.invalidate(); // redraw
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        //Log.d("mydebug", "MatrixView.onTouchEvent");

        synchronized (event) { // prevents touchscreen events from flooding the main thread

            //levelHandler.dropNormalSpeed();

            // initial touch
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                initialX = event.getRawX();
                initialY = event.getRawY();

                // touch at the bottom of the screen makes the piece drop faster
                if (initialY > screenHeight * 0.9) {
                    levelHandler.dropFastSpeed();
                }
                /*else
                    levelHandler.dropNormalSpeed();*/
            }

            // detect type of sliding movement
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                float curX = event.getRawX();
                float curY = event.getRawY();

                // stop touch at the bottom of the screen makes the piece drop at normal speed again
                if (levelHandler.isFast)
                    levelHandler.dropNormalSpeed();

                // X movement: rotation
                else if (Math.abs(initialX - curX) >= movementSensibilityX) {
                    if (initialX > curX) { // rotate right
                        //Log.d("mydebug", "MatrixView.onTouchEvent rotate right");
                        matrix.rotatePiece(TetrisPiece.DIRECTION.RIGHT);
                    } else { // rotate left
                        //Log.d("mydebug", "MatrixView.onTouchEvent rotate left");
                        matrix.rotatePiece(TetrisPiece.DIRECTION.LEFT);
                    }
                }

                // simple touch: move on X axis
                else {
                    // stop touch at the bottom of the screen makes the piece drop at normal speed again
                    //levelHandler.dropNormalSpeed();

                    // horizontal movement
                    if (initialX < screenWidth / 2) { // move left
                        //Log.d("mydebug", "MatrixView.onTouchEvent move left");
                        matrix.movePiece(TetrisPiece.DIRECTION.LEFT);
                    } else { // move right
                        //Log.d("mydebug", "MatrixView.onTouchEvent move right");
                        matrix.movePiece(TetrisPiece.DIRECTION.RIGHT);
                    }

                    //if (refreshHandler.hasMessages(1) == true) {
                        //Log.d("mydebug", "MatrixView.onTouchEvent C hasMessages");
                        //refreshHandler.removeMessages(1);
                    //}
                    refreshHandler.sendEmptyMessage(-1);
                }


            /*try {
                Thread.sleep(touchDelay);
            } catch (InterruptedException e) { }*/

                // redraw
                //this.invalidate();
            }

            /*if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                //levelHandler.dropNormalSpeed();
                Log.d("mydebug", "MatrixView.onTouchEvent ACTION_HOVER_EXIT");
            }*/

        }

        return true;
    }

    public void setLevelTextView(TextView levelTextView) {
        this.levelTextView = levelTextView;
    }

    public void setBestScoreTextView(TextView bestScoreTextView) {
        this.bestScoreTextView = bestScoreTextView;
    }

    public void setScoreTextView(TextView scoreTextView) {
        this.scoreTextView = scoreTextView;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

}