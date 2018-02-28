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

    private TetrisMatrix matrix;
    //private int matrixWidth, matrixHeight;

    private int[] colorArray; // one color for each Tetris shape
    private int sizeCellX, sizeCellY;
    //private Bitmap[] cellArray;

    private TextView levelTextView;

    //private int moveDelay = 300; //1200;
    private LevelHandler levelHandler = new LevelHandler();
    private RefreshHandler refreshHandler = new RefreshHandler();

    float initialX = 0; // X position of finger on initial touch
    float initialY = 0; // X position of finger on initial touch
    private final int movementSensibilityX = 20;
    private final int touchDelay = 20;

    /*public MatrixView(Context context) {
        this(context, null);
    }

    public MatrixView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        matrix = new TetrisMatrix();
        startGame();
    }*/

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new TetrisMatrix();

        startGame();
    }

    private void startGame() {
        initColorArray();
        refreshHandler.sendEmptyMessage(1); // start the game
    }

    public int getRandomColor(){ // TODO nicer colors
        Random rnd = new Random();
        int r, g, b;
        r = rnd.nextInt(256);
        if (r > 150)
            g = rnd.nextInt(150);
        else
            g = rnd.nextInt(256);
        if (g > 150)
            b = rnd.nextInt(150);
        else
            b = rnd.nextInt(256);
        return Color.argb(255, r, g, b);
    }

    public void initColorArray() {
        colorArray = new int[8];
        colorArray[0] = Color.WHITE;
        for (int i = 1; i < 8; i++) {
            colorArray[i] = getRandomColor();
        }
    }

    private void calculateCellSize() {
        sizeCellX = (int)Math.floor(screenWidth / (matrix.getNbCellsX() + 3));
        sizeCellY = (int)Math.floor(screenHeight / matrix.getNbCellsY());
        sizeCellX = sizeCellY = Math.min(sizeCellX, sizeCellY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        screenWidth = w;
        screenHeight = h;
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

        int yOrigin = screenHeight - sizeCellY * matrix.getNbCellsY();

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
        if (levelTextView != null)
            levelTextView.setText("Level " + levelHandler.getLevel() + " (Points: " + levelHandler.points + ")");

    }

    private void moveGame() {
        if (matrix.movePiece(TetrisPiece.DIRECTION.DOWN)) {
            //refreshHandler.removeMessages(0);
            //levelHandler.dropNormalSpeed();
            refreshHandler.sendEmptyMessageDelayed(0, levelHandler.getMoveDelay());
        }
        else { // the current piece cannot drop anymore
            //matrix.addNewPiece(1);
            refreshHandler.sendEmptyMessageDelayed(1, levelHandler.getMoveDelay());
        }
    }

    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
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
                    else
                        Log.d("mydebug", "Game over");
                }
            }

            else if (msg.what == 0) {
                moveGame(); //Log.d("mydebug", "MatrixView.RefreshHandler moving");
                //MatrixView.this.invalidate(); // redraw
            }

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
                    if (initialX > curX) { // rotate left
                        //Log.d("mydebug", "MatrixView.onTouchEvent rotate left");
                        matrix.rotatePiece(TetrisPiece.DIRECTION.LEFT);
                    } else { // rotate right
                        //Log.d("mydebug", "MatrixView.onTouchEvent rotate right");
                        matrix.rotatePiece(TetrisPiece.DIRECTION.RIGHT);
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
                    refreshHandler.sendEmptyMessage(2);
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

}