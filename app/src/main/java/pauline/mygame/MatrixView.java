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

    private int[] colorArray; // one color for each Tetris shape
    private int sizeCellX;
    private int sizeCellY;
    private Bitmap[] cellArray;

    private int moveDelay = 300; //1200;
    private RefreshHandler refreshHandler = new RefreshHandler();

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
        if (!matrix.dropPiece()) {
            matrix.addNewPiece(1);
        }
    }

    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            sendEmptyMessageDelayed(0, moveDelay); // wait
            //Log.d("mydebug", "MatrixView.RefreshHandler moving");
            moveGame();
            MatrixView.this.invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //Log.d("mydebug", "MatrixView.onTouchEvent");
            //matrix.dropPiece();
            this.invalidate();
        }

        return true;
    }

}