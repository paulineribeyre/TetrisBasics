package pauline.mygame;

import android.util.Log;

import java.util.Arrays;
import java.util.Random;

public class TetrisMatrix {

    private int nbCellsX, nbCellsY; // width and height of the game window
    private int[][] array;
    private TetrisPiece currentPiece;

    public TetrisMatrix(int height, int width) {
        nbCellsX = width;
        nbCellsY = height;
        array = new int[nbCellsY][nbCellsX];
        for (int y = 0; y < nbCellsY; y ++) {
            Arrays.fill(array[y], 0);
        }

        //addNewPiece();
    }

    public TetrisMatrix() {
        this(20, 10);
    }

    private void placePiece(TetrisPiece p) {
        for (int y = 0; y < p.getHeight(); y++) {
            for (int x = 0; x < p.getWidth(); x++) {
                if (p.getShape()[y][x] == 1)
                    array[p.getOriginY() + y][p.getOriginX() + x] = p.getType();
            }
        }
    }

    // TODO
    public void clearCells(int rowNumber, int numberOfRows) {

    }

    public boolean addNewPiece() {
        boolean canAdd = true;

        int[] arr = {1, 5};
        int type = arr[new Random().nextInt(arr.length)]; // TODO remove
        type = 5;

        //int type = new Random().nextInt(8) + 1;
        TetrisPiece p = new TetrisPiece(type);
        p.setOriginX(nbCellsX / 2 - 1);
        p.setOriginY(0);
        currentPiece = p;

        if (!isCollision(DIRECTION.NONE, array))
            placePiece(p);
        else
            canAdd = false;

        return canAdd;
    }

    public enum DIRECTION {
        NONE,
        DOWN,
        LEFT,
        RIGHT;
    }

    // TODO change to any horizontal/vertical collision
    private boolean isCollision(DIRECTION d, int[][] array) {
        boolean collision = false;
        int newOriginX = currentPiece.getOriginX();
        int newOriginY = currentPiece.getOriginY();
        int newX, newY;

        // check if the piece will collide with another piece
        switch(d){
            case DOWN:
                newOriginY += 1;
                break;
            case LEFT:
                newOriginX -= 1;
                break;
            case RIGHT:
                newOriginX += 1;
                break;
        }

        for (int x = 0; x < currentPiece.getWidth(); x++) {
            newX = newOriginX + x;
            for (int y = 0; y < currentPiece.getHeight(); y++) {
                newY = newOriginY + y;
                if (currentPiece.getShape()[y][x] == 1 &&
                        (newX < 0 || newX >= nbCellsX || newY >= nbCellsY || array[newY][newX] != 0)) {
                    collision = true;
                    break;
                }
            }
        }

        if (collision) Log.d("mydebug", "TetrisMatrix.isCollision collision");

        return collision;
    }

    public boolean movePiece(DIRECTION d) {
        boolean moved = false;

        // remove the piece from its old position
        int[][] arrayWithoutPiece = new int[nbCellsY][nbCellsX];
        for (int y = 0; y < nbCellsY; y++) {
            for (int x = 0; x < nbCellsX; x++)
                arrayWithoutPiece[y][x] = array[y][x];
        }
        for (int y = 0; y < currentPiece.getHeight(); y++) {
            for (int x = 0; x < currentPiece.getWidth(); x++) {
                if (currentPiece.getShape()[y][x] == 1)
                    arrayWithoutPiece[currentPiece.getOriginY() + y][currentPiece.getOriginX() + x] = 0;
            }
        }

        // do not move the piece if it collides with the game window or another piece
        if (!isCollision(d, arrayWithoutPiece)) {

            array = arrayWithoutPiece.clone();

            // move the piece to its new position
            switch(d){
                case DOWN:
                    currentPiece.setOriginY(currentPiece.getOriginY() + 1);
                    break;
                case LEFT:
                    currentPiece.setOriginX(currentPiece.getOriginX() - 1);
                    break;
                case RIGHT:
                    currentPiece.setOriginX(currentPiece.getOriginX() + 1);
                    break;
            }

            placePiece(currentPiece);
            moved = true;

        }

        return moved;
    }

    public int getNbCellsX() {
        return nbCellsX;
    }

    public int getNbCellsY() {
        return nbCellsY;
    }

    public int[][] getArray() {
        return array;
    }

    public void setArray(int[][] array) {
        this.array = array;
    }

    public TetrisPiece getCurrentPiece() {
        return currentPiece;
    }
}