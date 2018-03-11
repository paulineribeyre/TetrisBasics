package pauline.mygame;

import android.graphics.Color;

import java.io.Serializable;

public class TetrisPiece implements Serializable {

    private int type;
    private int width, height;
    private int[][] shape;
    private int color;

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
                color = Color.argb(255, 240, 240, 0);
                break;

            case 2: // I
                width = 1;
                height = 4;
                shape = new int[height][width];
                for (int y = 0; y < height; y++) {
                    shape[y][0] = 1;
                }
                color = Color.argb(255, 0, 240, 240);
                break;

            case 3: // T
                width = 3;
                height = 2;
                shape = new int[height][width];
                shape[0][1] = 1;
                for (int x = 0; x < width; x++) {
                    shape[1][x] = 1;
                }
                color = Color.argb(255, 160, 0, 240);
                break;

            case 4: // L
                width = 2;
                height = 3;
                shape = new int[height][width];
                shape[height - 1][1] = 1;
                for (int y = 0; y < height; y++) {
                    shape[y][0] = 1;
                }
                color = Color.argb(255, 240, 160, 0);
                break;

            case 5: // J
                width = 2;
                height = 3;
                shape = new int[height][width];
                shape[height - 1][0] = 1;
                for (int y = 0; y < height; y++) {
                    shape[y][1] = 1;
                }
                color = Color.argb(255, 0, 0, 240);
                break;

            case 6: // S
                width = 3;
                height = 2;
                shape = new int[height][width];
                for (int y = 0; y < height; y++) {
                    for (int x = 1 - y; x < width - y; x++) {
                        shape[y][x] = 1;
                    }
                }
                color = Color.argb(255, 0, 240, 0);
                break;

            case 7: // Z
            default:
                width = 3;
                height = 2;
                shape = new int[height][width];
                for (int y = 0; y < height; y++) {
                    for (int x = y; x < y + width - 1; x++) {
                        shape[y][x] = 1;
                    }
                }
                color = Color.argb(255, 240, 0, 0);
                break;

        }
    }

    public TetrisPiece clone() {
        TetrisPiece p = new TetrisPiece(type);
        p.width = width;
        p.height = height;
        p.shape = new int[height][width];
        for (int y = 0; y < height; y++) {
            p.shape[y] = shape[y].clone();
            /*for (int x = 0; x < width; x++) {
                p.shape[y][x] = shape[y][x];
            }*/
        }
        p.originX = originX;
        p.originY = originY;
        return p;
    }

    public enum DIRECTION {
        //NONE,
        DOWN,
        LEFT,
        RIGHT
    }

    public void rotate(DIRECTION d) {
        int[][] oldShape = new int[height][width];
        for (int y = 0; y < height; y++)
            oldShape[y] = shape[y].clone();

        int tmp = width;
        width = height;
        height = tmp;

        shape = new int[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                if (d == DIRECTION.RIGHT)
                    shape[y][x] = oldShape[width - x - 1][y];
                else
                    shape[y][x] = oldShape[x][height - y - 1];
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

    public int getColor() {
        return color;
    }

}