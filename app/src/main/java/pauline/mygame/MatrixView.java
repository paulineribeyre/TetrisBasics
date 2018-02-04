package pauline.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class MatrixView extends View {

    private TetrisMatrix matrix;

    private int[] colorArray; // one color for each Tetris shape
    private int sizeCellX;
    private int sizeCellY;
    private Bitmap[] cellArray;

    private int moveDelay = 1200;

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new TetrisMatrix();
        initColorArray();
        //Log.d("mydebug", "MatrixView.MatrixView"); // TODO remove

        new Thread() {
            @Override public void run() {
                while (true) {
                    try {
                        Thread.sleep(moveDelay);
                        moveGame();
                    } catch ( InterruptedException e ) {}
                }
            }
        }.start();
    }

    private void calculateCellSize(int screenWidth, int screenHeight) {
        sizeCellX = (int)Math.floor(screenWidth / matrix.getNbCellsX());
        sizeCellY = (int)Math.floor(screenHeight / matrix.getNbCellsY());
        sizeCellX = sizeCellY = Math.min(sizeCellX, sizeCellY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        calculateCellSize(w, h);
    }

    public void initColorArray() {
        colorArray = new int[8];
        colorArray[0] = Color.WHITE;
        for (int i = 1; i < 8; i++) {
            colorArray[i] = getRandomColor();
        }
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint myPaint = new Paint();

        for (int y = 0; y <  matrix.getNbCellsY(); y ++) {
            for (int x = 0; x <  matrix.getNbCellsX(); x ++) {
                myPaint.setColor(colorArray[matrix.getArray()[y][x]]);
                canvas.drawRect(x * sizeCellX, y * sizeCellY, (x + 1) * sizeCellX, (y + 1) * sizeCellY, myPaint);
            }
        }
    }

    // TODO
    private void moveGame() {
        //matrix.dropPiece();
        //this.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //Log.d("mydebug", "MatrixView.onTouchEvent");
            matrix.dropPiece();
            this.invalidate();

            // do not catch more than one touch: wait 20ms
            /*try {
                Thread.sleep(100);
                Log.d("mydebug", "MatrixView.onTouchEvent slept");
            } catch (InterruptedException e) {
                Log.d("mydebug", "MatrixView.onTouchEvent InterruptedException:" + e);
            }*/
        }

        return true;
    }

}