package pauline.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class MatrixView extends View {

    private TetrisMatrix matrix;
    private int matrixWidth, matrixHeight;

    private int[] colorArray; // one color for each Tetris shape
    private int sizeCellX, sizeCellY;
    private Bitmap[] cellArray;

    private int moveDelay = 300; //1200;
    private RefreshHandler refreshHandler = new RefreshHandler();

    float initialX = 0; // X position of finger on initial touch
    private int movementSensibilityX = 20;
    private int touchDelay = 20;

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new TetrisMatrix();

        startGame();
    }

    private void startGame() {
        initColorArray();
        refreshHandler.sendEmptyMessage(1); // start the game
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void initColorArray() {
        colorArray = new int[8];
        colorArray[0] = Color.WHITE;
        for (int i = 1; i < 8; i++) {
            colorArray[i] = getRandomColor();
        }
    }

    private void calculateCellSize(int screenWidth, int screenHeight) {
        sizeCellX = (int)Math.floor(screenWidth / matrix.getNbCellsX());
        sizeCellY = (int)Math.floor(screenHeight / matrix.getNbCellsY());
        sizeCellX = sizeCellY = Math.min(sizeCellX, sizeCellY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        calculateCellSize(w, h);
        matrixWidth = sizeCellX * matrix.getNbCellsX();
        matrixHeight = sizeCellY * matrix.getNbCellsY();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint myPaint = new Paint();
        // Drawable cell = getResources().getDrawable(R.drawable.cell); // use drawable image instead of rectangles

        for (int y = 0; y <  matrix.getNbCellsY(); y ++) {
            for (int x = 0; x <  matrix.getNbCellsX(); x ++) {
                myPaint.setColor(colorArray[matrix.getArray()[y][x]]);
                canvas.drawRect(x * sizeCellX, y * sizeCellY, (x + 1) * sizeCellX, (y + 1) * sizeCellY, myPaint);
                // cell.setColorFilter(colorArray[matrix.getArray()[y][x]], PorterDuff.Mode.MULTIPLY);
                // cell.setBounds(y * sizeCellY, x * sizeCellX, (y + 1) * sizeCellY, (x + 1) * sizeCellX);
                // cell.draw(canvas);
            }
        }
    }

    private void moveGame() {
        if (matrix.movePiece(TetrisPiece.DIRECTION.DOWN)) {
            //refreshHandler.removeMessages(0);
            refreshHandler.sendEmptyMessageDelayed(0, moveDelay);
        }
        else { // the current piece cannot drop anymore
            //matrix.addNewPiece(1);
            refreshHandler.sendEmptyMessageDelayed(1, moveDelay);
        }
    }

    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { // new game, or the current piece cannot drop anymore
                if (matrix.getCurrentPiece() != null && matrix.movePiece(TetrisPiece.DIRECTION.DOWN)) { // special case when the piece is moved after colliding
                    //refreshHandler.removeMessages(0);
                    refreshHandler.sendEmptyMessageDelayed(0, moveDelay);
                }
                else if (matrix.addNewPiece()) {
                    MatrixView.this.invalidate(); // redraw
                    //this.removeMessages(0);
                    sendEmptyMessageDelayed(0, moveDelay);
                }
                else
                    Log.d("mydebug", "Game over");
            }

            else {
                moveGame(); //Log.d("mydebug", "MatrixView.RefreshHandler moving");
                MatrixView.this.invalidate(); // redraw
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        //Log.d("mydebug", "MatrixView.onTouchEvent");

        synchronized (event) { // prevents touchscreen events from flooding the main thread

            // initial touch
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                initialX = event.getRawX();
            }

            // detect type of sliding movement
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float curX = event.getRawX();
                float curY = event.getRawY();

                // X movement: rotation
                if (Math.abs(initialX - curX) >= movementSensibilityX) {
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
                    // TODO add touch at the bottom of the screen to drop piece fast
                    if (initialX < matrixWidth / 2) { // move left
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
                    //refreshHandler.sendEmptyMessageDelayed(0, 400);
                }


            /*try {
                Thread.sleep(touchDelay);
            } catch (InterruptedException e) { }*/

                // redraw
                //this.invalidate();
            }
        }

        return true;
    }

}