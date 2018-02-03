package pauline.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
        Log.d("debug", "creating MatrixView"); // TODO remove

        new Thread() {
            @Override public void run() {
                while (true) {
                    try {
                        Thread.sleep(moveDelay);
                        initColorArray();
                    } catch ( InterruptedException e ) {}
                }
            }
        }.start();
    }

    private void calculateCellSize(int screenWidth, int screenHeigth) {
        sizeCellX = (int)Math.floor(screenWidth / matrix.getNbCellsX());
        sizeCellY = (int)Math.floor(screenHeigth /  matrix.getNbCellsY());
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
        Drawable cell = getResources().getDrawable(R.drawable.cell);
        int color;

        for (int i = 0; i <  matrix.getNbCellsY(); i ++) {
            for (int j = 0; j <  matrix.getNbCellsX(); j ++) {
                color = colorArray[matrix.getArray()[i][j]];
                cell.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                cell.setBounds(j * sizeCellY, i * sizeCellX, (j + 1) * sizeCellY, (i + 1) * sizeCellX);
                cell.draw(canvas);
            }
        }
    }

    // TODO
    private void moveGame() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        matrix.dropPiece();
        return true;
    }

}