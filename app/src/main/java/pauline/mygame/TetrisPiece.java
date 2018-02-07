package pauline.mygame;

import android.util.Log;

import java.util.Arrays;

public class TetrisPiece {

    private int type;
    private int width, height;
    private int[][] shape;

    private int originX, originY;

    public TetrisPiece(int type) {

        this.type = type;

        switch (type) {
            case 1: // O
                width = 2;
                height = 2;
                shape = new int[height][width];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        shape[y][x] = 1;
                    }
                }
                break;

            case 2: // I
                break;

            case 3: // T
                break;

            case 4: // S
                break;

            case 5: // Z
                width = 3;
                height = 2;
                shape = new int[height][width];
                for (int y = 0; y < height; y++) {
                    for (int x = y; x < y + width - 1; x++) {
                        shape[y][x] = 1;
                    }
                }
                break;

            case 6: // L
                break;

            case 7: // J
            default:
                break;
        }
    }

    // TODO
    private void rotate(int direction) { // -1: rotate left; 1: rotate right

    }

    public int getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getShape() {
        return shape;
    }

    public int getOriginX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

}