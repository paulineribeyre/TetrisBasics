package pauline.mygame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MatrixView extends View {

    private int screenWidth;
    private int screenHeight;
    private int viewWidth;
    private int viewHeight;

    private TetrisMatrix matrix; // tetris game matrix

    private int[] colorArray; // one color for each type of Tetromino
    private int sizeCellX, sizeCellY; // size of each tile of the matrix

    // textViews in which the level and score will be displayed
    private TextView levelTextView;
    private TextView scoreTextView;
    private TextView bestScoreTextView;

    private boolean paused = false; // is the user currently playing?

    private LevelHandler levelHandler;
    private RefreshHandler refreshHandler = new RefreshHandler();

    float initialX = 0; // X position of finger on initial touch
    float initialY = 0; // Y position of finger on initial touch
    private final int movementSensibilityX = 30; // threshold to decide if a touch is a tap or a drag

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //startGame() is called automatically by onResume()
    }

    // start playing
    public void startGame() {
        matrix = User.currentGame; // new TetrisMatrix();
        levelHandler = User.levelHandler;
        if (colorArray == null)
            initColorArray();
        refreshHandler.sendEmptyMessage(MESSAGE.COLLISION.ordinal()); // start the game
    }

    // start a new game of tetris
    private void startNewGame() {
        User.startNewGame();
        startGame();
    }

    // get a random color for a type of tetromino
    public int getRandomColor(){

        Random rnd = new Random();
        int r, g, b;
        double luminance;

        // only select light colors
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
        colorArray = new int[8]; // one color for each type of Tetromino and one for the background
        colorArray[0] = Color.WHITE; // Game matrix background
        for (int i = 1; i < 8; i++) {
            if (User.useRandomColors)
                colorArray[i] = getRandomColor();
            else
                colorArray[i] = new TetrisPiece(i).getColor();
        }
    }

    // calculates the size of the matrix tiles depending on the available screen space
    private void calculateCellSize() {
        sizeCellX = (int)Math.floor(viewWidth / (matrix.getNbCellsX() + 3));
        sizeCellY = (int)Math.floor(viewHeight / matrix.getNbCellsY());
        sizeCellX = sizeCellY = Math.min(sizeCellX, sizeCellY); // for now, the tiles are squares
    }

    // save the size of the available screen space
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

        calculateCellSize(); // (re)calculate the size of the tiles
    }

    // displays the tetris game matrix
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
                    cell.setColorFilter(colorArray[type], PorterDuff.Mode.MULTIPLY); // set a color filter to the color of this type of tetromino
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
                    cell.setColorFilter(colorArray[type], PorterDuff.Mode.MULTIPLY);
                    cell.setBounds(x0 * sizeCellX + x * sizeCellX / 2, y0 * sizeCellY + y * sizeCellY / 2,
                            x0 * sizeCellX + (x + 1) * sizeCellX / 2, y0 * sizeCellY + (y + 1) * sizeCellY / 2);
                    cell.draw(canvas);
                }
            }
        }

        // display the current level, score and best score
        if (levelTextView != null) {
            levelTextView.setText("LEVEL " + levelHandler.getLevel());
            scoreTextView.setText("SCORE: " + levelHandler.getScore());
            bestScoreTextView.setText("BEST SCORE:\n" + User.bestScore);
        }

    }

    // makes the current piece drop down one tile
    private void moveGame() {
        if (!paused) {
            if (matrix.movePiece(TetrisPiece.DIRECTION.DOWN)) {
                if (levelHandler.isFast) levelHandler.addFastPoints(1);
                refreshHandler.sendEmptyMessageDelayed(MESSAGE.MOVE_GAME.ordinal(), levelHandler.getMoveDelay()); // wait before moving again
            } else { // the current piece cannot drop anymore
                refreshHandler.sendEmptyMessage(MESSAGE.COLLISION.ordinal());
            }
        }
    }

    // types of messages handled by the RefreshHandler
    private enum MESSAGE {
        COLLISION,
        MOVE_GAME,
        REFRESH
    }

    // the handler decides what to do next
    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == MESSAGE.COLLISION.ordinal()) { // new game, or if the current piece cannot drop anymore

                // case when the piece is moved after colliding
                if (matrix.getCurrentPiece() != null && matrix.movePiece(TetrisPiece.DIRECTION.DOWN)) {
                    refreshHandler.sendEmptyMessageDelayed(MESSAGE.MOVE_GAME.ordinal(), levelHandler.getMoveDelay()); // wait before moving again
                }

                // the piece was not moved after colliding
                else {
                    int nbOfClearedRows = matrix.clearRows(); // check if some rows are complete and clear them
                    if (nbOfClearedRows != 0) levelHandler.addPoints(nbOfClearedRows); // add points to the current score

                    if (matrix.addNewPiece()) { // the current piece cannot move anymore, replace it with a new one
                        sendEmptyMessageDelayed(MESSAGE.MOVE_GAME.ordinal(), levelHandler.getMoveDelay()); // wait before moving again
                    }

                    // if a new piece cannot be added on top of the matrix, it's game over
                    else {

                        // a dialog asking if the user wants to try again is displayed
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setCancelable(true);
                        builder.setTitle("Game over!");
                        builder.setMessage("Do you want to start a new game?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startNewGame(); // start a new game
                                    }
                                });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                User.startNewGame(); // reset the current game
                                ((TetrisActivity) getContext()).finish(); // go back to the main menu
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }

            // make the current piece drop down
            } else if (msg.what == MESSAGE.MOVE_GAME.ordinal()) {
                moveGame();
            }

            // MESSAGE.REFRESH
            MatrixView.this.invalidate(); // redraw the matrix

        }
    }

    // when the user touches the screen
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        synchronized (event) { // prevents touchscreen events from flooding the main thread

            // initial touch
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                initialX = event.getRawX();
                initialY = event.getRawY();

                // touch at the bottom of the screen makes the piece drop faster
                if (initialY > screenHeight * 0.9) {
                    levelHandler.dropFastSpeed();
                }
            }

            // detect type of sliding movement
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                float curX = event.getRawX();

                // stoping the touch at the bottom of the screen makes the piece drop at normal speed again
                if (levelHandler.isFast)
                    levelHandler.dropNormalSpeed();

                // no X movement: move on X axis (except if the controls are reversed)
                else if (Math.abs(initialX - curX) < movementSensibilityX) {
                    if (initialX < screenWidth / 2) { // move left
                        if (User.touchToMove) matrix.movePiece(TetrisPiece.DIRECTION.LEFT);
                        else matrix.rotatePiece(TetrisPiece.DIRECTION.LEFT);
                    } else { // move right
                        if (User.touchToMove) matrix.movePiece(TetrisPiece.DIRECTION.RIGHT);
                        else matrix.rotatePiece(TetrisPiece.DIRECTION.RIGHT);
                    }
                }

                // X movement: rotation (except if the controls are reversed)
                else  if (initialX < curX) { // rotate right
                    if (User.touchToMove) matrix.rotatePiece(TetrisPiece.DIRECTION.RIGHT);
                    else matrix.movePiece(TetrisPiece.DIRECTION.RIGHT);
                } else { // rotate left
                    if (User.touchToMove) matrix.rotatePiece(TetrisPiece.DIRECTION.LEFT);
                    else matrix.movePiece(TetrisPiece.DIRECTION.LEFT);
                }

                refreshHandler.sendEmptyMessage(MESSAGE.REFRESH.ordinal()); // redraw the matrix
            }

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